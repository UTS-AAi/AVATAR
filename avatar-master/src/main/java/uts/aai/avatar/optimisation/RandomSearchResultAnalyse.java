/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uts.aai.avatar.optimisation;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBException;
import org.drools.KnowledgeBase;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.Resource;
import org.drools.io.impl.ByteArrayResource;
import org.drools.runtime.StatefulKnowledgeSession;
import sun.nio.ch.IOUtil;
import uts.aai.avatar.model.AllEvaluationResult;
import uts.aai.avatar.model.EvaluationResult;
import uts.aai.global.AppConst;
import uts.aai.mf.configuration.MLComponentConfiguration;
import uts.aai.pn.utils.IOUtils;
import uts.aai.pn.utils.JSONUtils;

/**
 *
 * @author ntdun
 */
public class RandomSearchResultAnalyse {
    
    
    public void writeTheLastPipeline(String randomSearchResultFilePath, String lastPipelinePath) {
        
        MLComponentConfiguration.initDefault();
        
        IOUtils iou = new IOUtils();
        String allEvaluationResultStr = iou.readData(randomSearchResultFilePath);
        try {
            AllEvaluationResult allEvaluationResult = JSONUtils.unmarshal(allEvaluationResultStr, AllEvaluationResult.class);
            
   
            EvaluationResult evaluationResult = allEvaluationResult.getEvaluationResultList().
                    get(allEvaluationResult.getEvaluationResultList().size()-1);
         
            //evaluateBPMNPipeline(bestResult.getBpmnPipeline());
            iou.overWriteData(evaluationResult.getBpmnPipeline(), lastPipelinePath);
            
        } catch (JAXBException ex) {
            Logger.getLogger(RandomSearchResultAnalyse.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
    public void analyse(String randomSearchResultFilePath, String outputBestPipelineFilePath){
        int counter=0;
        MLComponentConfiguration.initDefault();
        
        IOUtils iou = new IOUtils();
        String allEvaluationResultStr = iou.readData(randomSearchResultFilePath);
        try {
            AllEvaluationResult allEvaluationResult = JSONUtils.unmarshal(allEvaluationResultStr, AllEvaluationResult.class);
            
            EvaluationResult bestResult = null;
            Double highestAccuracy = 0.0;
            int validPipeline = 0;
            int invalidPipeline = 0;
            
            long sumTime = 0;
            long sumValid = 0;
            long sumInvalid = 0;
            
            for (EvaluationResult evaluationResult : allEvaluationResult.getEvaluationResultList()) {
                if (evaluationResult.isValidity() && evaluationResult.getAccuracy()!=null) {
                    
//                    if (evaluationResult.getAccuracy()>highestAccuracy) {
//                        bestResult = evaluationResult;
//                        highestAccuracy = evaluationResult.getAccuracy();
//                    }
                    
                    
                }
                
                if (evaluationResult.isValidity()) {
                    validPipeline++;
                    sumValid += evaluationResult.getEvaluationTime();
                } else {
                    invalidPipeline++;
                    sumInvalid += evaluationResult.getEvaluationTime();
                }
                
                sumTime+=evaluationResult.getEvaluationTime();
                
                
//                if (!evaluationResult.isValidity()) {
//                    String tmpPipeline = evaluationResult.getBpmnPipeline();
//                    String outPath = randomSearchResultFilePath+String.valueOf(counter)+".bpmn";
//                    iou.overWriteData(tmpPipeline, outPath);
//                    counter++;
//                    
//                }
                
            }
            
            System.out.println("Number of Pipelines: " + allEvaluationResult.getEvaluationResultList().size());
            System.out.println("Valid Pipeline: " +validPipeline);
            System.out.println("Invalid Pipeline: " +(allEvaluationResult.getEvaluationResultList().size()-validPipeline));
            System.out.println("AVG Total Time: " +sumTime/allEvaluationResult.getEvaluationResultList().size());
            if (validPipeline!=0) System.out.println("AVG valid pipeline evaluation time: " + sumValid/validPipeline);
            System.out.println("AVG INvalid pipeline evaluation time: " + sumInvalid/invalidPipeline);
            System.out.println("Highest Accuracy: " + highestAccuracy);
            
            
            //evaluateBPMNPipeline(bestResult.getBpmnPipeline());
          //  iou.overWriteData(bestResult.getBpmnPipeline(), outputBestPipelineFilePath);
            
        } catch (JAXBException ex) {
            Logger.getLogger(RandomSearchResultAnalyse.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
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
    
}
