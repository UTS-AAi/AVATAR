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
import uts.aai.avatar.model.EvaluationModel;
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
            
            
        } catch (JAXBException ex) {
            Logger.getLogger(RandomSearchResultAnalyse.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
    public void analyse(String randomSearchResultFilePath){

        MLComponentConfiguration.initDefault();
        
        IOUtils iou = new IOUtils();
        String allEvaluationResultStr = iou.readData(randomSearchResultFilePath);
        try {
            AllEvaluationResult allEvaluationResult = JSONUtils.unmarshal(allEvaluationResultStr, AllEvaluationResult.class);
            
            System.out.println("AVATAR RESULT --------------------- \n");
            analyseModelIndex(allEvaluationResult, 0);
            
            System.out.println("BPMN RESULT --------------------- \n");
            analyseModelIndex(allEvaluationResult, 1);
            
            System.out.println("DIFF ANALYSE RESULT --------------------- \n");
            analyseDiff(allEvaluationResult, randomSearchResultFilePath);
            
        } catch (JAXBException ex) {
            Logger.getLogger(RandomSearchResultAnalyse.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
    }
    
    private void analyseModelIndex(AllEvaluationResult allEvaluationResult, int modelIndex){
        int validPipeline = 0;
            int invalidPipeline = 0;
            
            long sumTime = 0;
            long sumValid = 0;
            long sumInvalid = 0;
            
            for (EvaluationResult evaluationResult : allEvaluationResult.getEvaluationResultList()) {
                
                EvaluationModel evalModel = evaluationResult.getEvaluationModel().get(modelIndex);
                
                if (evalModel.getValidity()) {
                    validPipeline++;
                    sumValid += evalModel.getEvalTime();
                } else {
                    invalidPipeline++;
                    sumInvalid += evalModel.getEvalTime();
                    
                }
                
                sumTime+=evalModel.getEvalTime();
            }
            
            System.out.println("Number of Pipelines: " + allEvaluationResult.getEvaluationResultList().size());
            System.out.println("Valid Pipeline: " +validPipeline);
            System.out.println("Invalid Pipeline: " +(allEvaluationResult.getEvaluationResultList().size()-validPipeline));
            System.out.println("AVG Total Time: " +sumTime/allEvaluationResult.getEvaluationResultList().size());
            if (validPipeline!=0) System.out.println("AVG valid pipeline evaluation time: " + sumValid/validPipeline);
            System.out.println("AVG INvalid pipeline evaluation time: " + sumInvalid/invalidPipeline);
    }
    
    private void analyseDiff(AllEvaluationResult allEvaluationResult, String randomSearchResultFilePath){
            IOUtils iou = new IOUtils();
            int differentEvalCounter = 0;
            int similarEvalCounter = 0;
            int counter =0;
            int timeOutCounter = 0;
            
            for (EvaluationResult evaluationResult : allEvaluationResult.getEvaluationResultList()) {
                
                EvaluationModel evalModel0 = evaluationResult.getEvaluationModel().get(0);
                EvaluationModel evalModel1 = evaluationResult.getEvaluationModel().get(1);
               
                if (evalModel0.getValidity().equals(evalModel1.getValidity())) {
                    similarEvalCounter++;
                } else {
                    differentEvalCounter++;
                    String diffPath = randomSearchResultFilePath+"-dif-"+String.valueOf(counter) +".bpmn";
                    iou.overWriteData(evalModel1.getSpecs(), diffPath);
                    counter++;
                    
                }
                
                if (evalModel1.getSpecs().equals("timeout")) {
                    timeOutCounter++;
                    String timeoutPath = randomSearchResultFilePath+"-timeout-"+String.valueOf(timeOutCounter) +".bpmn";
                    iou.overWriteData(evalModel1.getSpecs(), timeoutPath);
                }
            }
            
            System.out.println("Number of Pipelines: " + allEvaluationResult.getEvaluationResultList().size());
            System.out.println("Similar Evaluation Pipeline: " +similarEvalCounter);
            System.out.println("Different Evaluation Pipeline: " +differentEvalCounter);
            System.out.println("Timeout Pipeline: " +timeOutCounter);
         
    }
    
   
    
}
