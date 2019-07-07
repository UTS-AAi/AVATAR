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
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBException;
import org.apache.commons.io.FileUtils;
import org.drools.KnowledgeBase;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.definition.process.Connection;
import org.drools.definition.process.Node;
import org.drools.io.Resource;
import org.drools.io.ResourceFactory;
import org.drools.io.impl.ByteArrayResource;
import org.drools.runtime.StatefulKnowledgeSession;
import org.jbpm.ruleflow.core.RuleFlowProcess;
import org.jbpm.workflow.core.node.ActionNode;
import org.jbpm.workflow.core.node.EndNode;
import org.jbpm.workflow.core.node.StartNode;
import uts.aai.avatar.model.AllEvaluationResult;
import uts.aai.avatar.model.EvaluationResult;
import uts.aai.global.AppConst;
import uts.aai.mf.configuration.MLComponentConfiguration;
import uts.aai.mf.model.MLComponent;
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
    private String outputFolder;

    public RandomSearch(String datasetPath, long timeBudgetInMinutes, boolean isAvatar,String outputFolder) {
        this.datasetPath = datasetPath;
        this.timeBudgetInMinutes = timeBudgetInMinutes;
        this.isAvatar = isAvatar;
        this.outputFolder = outputFolder;
    }

    private void initRandomSearch() {
        evaluationResultList = new ArrayList<>();
        MLComponentConfiguration.initDefault();
    }

    public void start() {

        initRandomSearch();

        long startExperimentTime = System.currentTimeMillis();

        while (!isTimeOut(startExperimentTime)) //boolean isDiff = false;
        //while(!isDiff)
        {

            cleanTemp();

            String bpmnPipeline = createRandomPipeline();
            
            
            long startEvaluationTime = System.currentTimeMillis();
            EvaluationResult evaluationResult = null;
            String rs1 = "";
            String rs2 = "";

            if (isAvatar) {
                System.out.println("START AVATAR");

                evaluationResult = evaluateAvatar(bpmnPipeline);

                if (evaluationResult == null) {

                    evaluationResult = new EvaluationResult(bpmnPipeline, false, null);
                }
                rs2 = String.valueOf(evaluationResult.isValidity());
                cleanTemp();
                System.out.println("END AVATAR");
            } else {
                System.out.println("START BPMN");
                evaluationResult = evaluateBPMNPipeline(bpmnPipeline);
                if (evaluationResult == null) {

                    evaluationResult = new EvaluationResult(bpmnPipeline, false, null);
                }
                rs1 = String.valueOf(evaluationResult.isValidity());
                cleanTemp();

                System.out.println("END BPMN");
            }

            System.out.println(rs1 + "-" + rs2);
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

    private void fromArffToCSV(String sourceArff, String outputCSV) {

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
//        RandomPipelineGenerator randomPipelineGenerator = new RandomPipelineGenerator(datasetPath,outputFolder);
//        String bpmnPipeline = randomPipelineGenerator.generateBPMNPipeline();
        int numberOfPreprocessingComponent = randInt(0, 5);

        RandomPipelineGenerator generator = new RandomPipelineGenerator(datasetPath,outputFolder);
        //String bpmnPipeline = generator.generateBPMNTemplate(0);
        String bpmnPipeline = generator.generateBPMNPipelineWithRandomComponents(numberOfPreprocessingComponent);

        return bpmnPipeline;
    }
    
    private int randInt(int min, int max) {

        int randomNum = ThreadLocalRandom.current().nextInt(min, max + 1);
        return randomNum;
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

    public void mappingFromBPMN2NativeWekaCommand(String bpmnPipeline) {

        ArrayList<MLComponent> listOfMLComponents = new ArrayList<>();
        
        
        Resource resource = new ByteArrayResource(bpmnPipeline.getBytes());

        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        kbuilder.add(resource, ResourceType.BPMN2);

        KnowledgeBase kbase = kbuilder.newKnowledgeBase();

        RuleFlowProcess process = (RuleFlowProcess) kbase.getProcess("ml.process");

        StartNode startNode = process.getStart();
        System.out.println("start node " + startNode.getName());

        ActionNode currentActionNode = null;

        boolean isReachEndEvent = false;

        while (!isReachEndEvent) {

            List<Connection> connectionList = null;
            // start node

            if (currentActionNode == null) {
                connectionList = startNode.getDefaultOutgoingConnections();
            } else {
                connectionList = currentActionNode.getDefaultOutgoingConnections();
            }

            if (connectionList == null) {
                isReachEndEvent = true;
            } else {

                for (Connection outConnection : connectionList) {
                    Node node = outConnection.getTo();
                    System.out.println("connection: " + outConnection.getToType());
                    if (node instanceof EndNode) {
                        isReachEndEvent = true;
                        System.out.println("endnote: " + node.getName());
//                        Place endPlace = createPlace(((EndNode) node).getUniqueId());
//                        placeList.add(endPlace);
                    } else {

                        currentActionNode = (ActionNode) node;

                        MLComponent mLComponent = MLComponentConfiguration.getComponentByID(currentActionNode.getName());
                        listOfMLComponents.add(mLComponent);
                        System.out.println("actionNode: " + currentActionNode.getName());
                    }
                }

            }

        }

        generateFilteredClassiferWeka(listOfMLComponents);

    }

    private String prepareFullCommand(String outputFile, String pipeline) {
        String command = "java -classpath C:/DATA/Projects/eclipse-workspace/aai_aw/weka-3-7-7/weka.jar weka.classifiers.meta.FilteredClassifier -t "
                //+ "C:/DATA/Projects/eclipse-workspace/aai_aw/weka-3-7-7/data/iris.arff"
                + datasetPath
                + " "
                + "-d " + AppConst.TEMP_EVALUATION_RESULT_PATH + outputFile + ".model "
                + "-F \"weka.filters.MultiFilter "
                + pipeline;

//                + preprocessingConfigurations
//                + "\" "
//                + classifierConfiguration;
        return command;
    }

    private String generateFilteredClassiferWeka(ArrayList<MLComponent> orderedPipelineComponents) {
        String pipeline = orderedPipelineComponents.get(0).getComponentExecutionScriptFilteredClassifierWeka()
                + orderedPipelineComponents.get(1).getComponentExecutionScriptFilteredClassifierWeka()
                + orderedPipelineComponents.get(2).getComponentExecutionScriptFilteredClassifierWeka()
                + orderedPipelineComponents.get(3).getComponentExecutionScriptFilteredClassifierWeka()
                + orderedPipelineComponents.get(4).getComponentExecutionScriptFilteredClassifierWeka()
                + "\" "
                + orderedPipelineComponents.get(5).getComponentExecutionScriptFilteredClassifierWeka();

        String outputFileName = "output_model";
        String fullCommand = prepareFullCommand(outputFileName, pipeline);
       
        return fullCommand;
    }

}
