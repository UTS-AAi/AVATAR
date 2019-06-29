/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uts.aai.avatar.optimisation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBException;
import org.apache.commons.io.FileUtils;
import org.drools.KnowledgeBase;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.Resource;
import org.drools.io.ResourceFactory;
import org.drools.io.impl.ByteArrayResource;
import org.drools.runtime.StatefulKnowledgeSession;
import uts.aai.avatar.model.AllEvaluationResult;
import uts.aai.avatar.model.EvaluationResult;
import uts.aai.global.AppConst;
import uts.aai.mf.configuration.MLComponentConfiguration;
import uts.aai.mf.model.MLComponentIO;
import uts.aai.mf.service.DatasetMetaFeatures;
import uts.aai.pbmn.SurrogatePipelineMapping;
import uts.aai.pbmn.test.AutoProcessTestApp;
import uts.aai.pn.engine.PetriNetsExecutionEngine;
import uts.aai.pn.model.Parameter;
import uts.aai.pn.model.PetriNetsPipeline;
import uts.aai.pn.model.Token;
import uts.aai.pn.utils.IOUtils;
import uts.aai.pn.utils.JSONUtils;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.core.converters.CSVSaver;

/**
 *
 * @author ntdun
 */
public class RandomSearch {

    private String datasetPath;
    private long timeBudgetInMinutes;
     private boolean isAvatar;
    private ArrayList<EvaluationResult> evaluationResultList;

    public RandomSearch(String datasetPath, long timeBudgetInMinutes, boolean isAvatar) {
        this.datasetPath = datasetPath;
        this.timeBudgetInMinutes = timeBudgetInMinutes;
        this.isAvatar = isAvatar;
    }

    private void initRandomSearch() {
        evaluationResultList = new ArrayList<>();
        MLComponentConfiguration.initDefault();
    }

    public void start() {

        initRandomSearch();
        
        
      
        

        long startExperimentTime = System.currentTimeMillis();

        while (!isTimeOut(startExperimentTime)) 
       
       //boolean isDiff = false;
       
       //while(!isDiff)
        {

            cleanTemp();

            String bpmnPipeline = createRandomPipeline();
            long startEvaluationTime = System.currentTimeMillis();
            EvaluationResult evaluationResult = null;
            String rs1="";
            String rs2="";
        
            
            if (isAvatar) 
            {
                System.out.println("START AVATAR");
                
                evaluationResult = evaluateAvatar(bpmnPipeline);

                if (evaluationResult == null) {

                    evaluationResult = new EvaluationResult(bpmnPipeline, false, null);
                }
               rs2 = String.valueOf(evaluationResult.isValidity());
               cleanTemp();
               System.out.println("END AVATAR");
            } 
            else 
            {
                System.out.println("START BPMN");
                evaluationResult = evaluateBPMNPipeline(bpmnPipeline);
                if (evaluationResult == null) {

                    evaluationResult = new EvaluationResult(bpmnPipeline, false, null);
                }
                rs1 = String.valueOf(evaluationResult.isValidity());
                cleanTemp();
                
                System.out.println("END BPMN");
            }
            
            System.out.println(rs1+"-"+rs2);
//
//            
//            if (!rs1.equals(rs2)) {
//                isDiff = true;
//            }
            long endEvaluationTime = System.currentTimeMillis();
            evaluationResult.setEvaluationTime(endEvaluationTime - startEvaluationTime);
            evaluationResultList.add(evaluationResult);

            System.out.println(evaluationResult.isValidity() + " - " + (endEvaluationTime - startEvaluationTime));

        }

    }
    
    private String getDataPathFromScript(String sourceArff) {
        
      
      
        String targetCSV = sourceArff.replace(".arff", ".csv");
        
        fromArffToCSV(sourceArff, targetCSV);
        
        return targetCSV;
    }
    
    private void fromArffToCSV(String sourceArff, String outputCSV){
       
        ArffLoader loader = new ArffLoader();
        try {
            loader.setSource(new File(sourceArff));
            Instances data = loader.getDataSet();
            
            CSVSaver saver = new CSVSaver();
            saver.setInstances(data);
            
            saver.setFile(new File(outputCSV));
            saver.writeBatch();
        } catch (IOException ex) {
            Logger.getLogger(SurrogatePipelineMapping.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    public void finalise() {

        int numberOfInvalid = 0;
        int numberOfValid = 0;

        EvaluationResult bestEvaluationResult = null;
        Double highestAccuracy = 0.0;

        String dataOutput = "";

        for (EvaluationResult evaluationResult : evaluationResultList) {

            if (evaluationResult.isValidity()) {
                numberOfValid++;
            } else {
                numberOfInvalid++;
            }

            if (evaluationResult.getAccuracy() != null && evaluationResult.getAccuracy() > highestAccuracy) {
                highestAccuracy = evaluationResult.getAccuracy();
                bestEvaluationResult = evaluationResult;
            }

        }
        //System.out.println("BEST Pipeline: " + bestEvaluationResult.getBpmnPipeline());
        //System.out.println("Accuracy: " + bestEvaluationResult.getAccuracy());

        AllEvaluationResult allEvaluationResult = new AllEvaluationResult(evaluationResultList);

        try {
            String allResultStr = JSONUtils.marshal(allEvaluationResult, AllEvaluationResult.class);
            IOUtils iou = new IOUtils();
            iou.overWriteData(allResultStr, AppConst.RANDOM_SEARCH_OUTPUT_PATH);

        } catch (JAXBException ex) {
            Logger.getLogger(RandomSearch.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("TOTAL OF EVALUATED MODEL: " + evaluationResultList.size());
        System.out.println("TOTAL OF INVALID MODEL: " + numberOfInvalid);
        System.out.println("TOTAL OF VALID MODEL: " + numberOfValid);

    }

    private boolean isTimeOut(long startExperimentTime) {

        long usedTime = System.currentTimeMillis() - startExperimentTime;

        if (usedTime <= timeBudgetInMinutes * 60 * 1000) {
            return false;
        } else {
            return true;
        }
    }

    private String createRandomPipeline() {
        RandomPipelineGenerator randomPipelineGenerator = new RandomPipelineGenerator(datasetPath);
        String bpmnPipeline = randomPipelineGenerator.generate();

        return bpmnPipeline;
    }

    private EvaluationResult evaluateBPMNPipeline(String bpmnPipeline) {

        try {

            Resource resource = new ByteArrayResource(bpmnPipeline.getBytes());

            KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
            kbuilder.add(resource, ResourceType.BPMN2);

            //kbuilder.add(ResourceFactory.newClassPathResource("demo.bpmn"), ResourceType.BPMN2);
            KnowledgeBase kbase = kbuilder.newKnowledgeBase();
            StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();

            HashMap<String, Object> params = new HashMap<String, Object>();
            //params.put("name", "Francesco");

            ksession.startProcess("ml.process", params);
            ksession.dispose();

            IOUtils iou = new IOUtils();
            String evaluationResultStr = iou.readData(AppConst.TEMP_EVALUATION_RESULT_PATH);

            EvaluationResult evaluationResult = JSONUtils.unmarshal(evaluationResultStr, EvaluationResult.class);
            evaluationResult.setBpmnPipeline(bpmnPipeline);
            return evaluationResult;

        } catch (Exception e) {
        }

        return null;

    }

    private EvaluationResult evaluateAvatar(String bpmnPipeline) {

        try {
            
        
        SurrogatePipelineMapping spm = new SurrogatePipelineMapping();
        PetriNetsPipeline petriNetsPipeline = spm.mappingFromBPMN2PetriNetsPipelineFromBPMNString(bpmnPipeline);
        //System.out.println("\n" + petriNetsPipeline.toString());

        
        PetriNetsExecutionEngine engine = new PetriNetsExecutionEngine(petriNetsPipeline);
        boolean result = engine.execute();
        
        
        if (result) {
            EvaluationResult evaluationResult = evaluateBPMNPipeline(bpmnPipeline);
            return evaluationResult;
        }
        
        } catch (Exception e) {
        }
        
        return null;
    }
    
   

    private void cleanTemp() {
        IOUtils iou = new IOUtils();
        File directory = new File(AppConst.TEMP_OUTPUT_PATH);
        try {
            FileUtils.cleanDirectory(directory);
        } catch (IOException ex) {
            Logger.getLogger(AutoProcessTestApp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private Token createToken(String filePath) {
        
    
        
        List<Parameter> parameterList = new ArrayList<>();
        DatasetMetaFeatures datasetMetaFeatures = new DatasetMetaFeatures(filePath);
        
        List<MLComponentIO> listOfMetaFeatures = datasetMetaFeatures.analyseMetaFeatures();
        System.out.println("");
        for (MLComponentIO mLComponentIO : listOfMetaFeatures) {
            System.out.println(mLComponentIO.getmLComponentCapability() + " : " + mLComponentIO.getValue());
            Parameter parameter = new Parameter(mLComponentIO.getmLComponentCapability().name(), mLComponentIO.getValue());
            parameterList.add(parameter);
            
        }
        
        Token token = new Token("metatoken", parameterList);
        
        
        return token;
    }

}
