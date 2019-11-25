/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uts.aai.avatar.resultanalysis;

import java.util.ArrayList;
import java.util.Collections;
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
import uts.aai.avatar.configuration.AppConst;
import uts.aai.avatar.configuration.MLComponentConfiguration;
import uts.aai.utils.IOUtils;
import uts.aai.utils.JSONUtils;

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
                    get(allEvaluationResult.getEvaluationResultList().size() - 1);

            //evaluateBPMNPipeline(bestResult.getBpmnPipeline());
        } catch (JAXBException ex) {
            Logger.getLogger(RandomSearchResultAnalyse.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void analyse(String randomSearchResultFilePath) {

        MLComponentConfiguration.initDefault();

        IOUtils iou = new IOUtils();
        String allEvaluationResultStr = iou.readData(randomSearchResultFilePath);
        try {
            AllEvaluationResult allEvaluationResult = JSONUtils.unmarshal(allEvaluationResultStr, AllEvaluationResult.class);

            System.out.println("AVATAR RESULT --------------------- \n");
            analyseModelIndex(allEvaluationResult, 0);

            System.out.println("BPMN RESULT --------------------- \n");
            analyseModelIndex(allEvaluationResult, 1);

            System.out.println("TPOT STRATEGY RESULT --------------------- \n");
            //analyseModelIndex(allEvaluationResult, 2);

            System.out.println("AVATAR DIFF ANALYSE RESULT --------------------- \n");
            //analyseDiff(allEvaluationResult, randomSearchResultFilePath,0,1);

            System.out.println("TPOT STRAGEGY DIFF ANALYSE RESULT --------------------- \n");
            //analyseDiff(allEvaluationResult, randomSearchResultFilePath,2,1);
            
            
            rankModels(allEvaluationResult,randomSearchResultFilePath);

        } catch (JAXBException ex) {
            Logger.getLogger(RandomSearchResultAnalyse.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void analyseModelIndex(AllEvaluationResult allEvaluationResult, int modelIndex) {
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

            sumTime += evalModel.getEvalTime();
        }

        System.out.println("Number of Pipelines: " + allEvaluationResult.getEvaluationResultList().size());
        System.out.println("Valid Pipeline: " + validPipeline);
        System.out.println("Invalid Pipeline: " + (allEvaluationResult.getEvaluationResultList().size() - validPipeline));
        System.out.println("AVG Total Time: " + sumTime / allEvaluationResult.getEvaluationResultList().size());
        if (validPipeline != 0) {
            System.out.println("AVG valid pipeline evaluation time: " + sumValid / validPipeline);
        }
        System.out.println("AVG INvalid pipeline evaluation time: " + sumInvalid / invalidPipeline);

        System.out.printf("The total evaluation time of invalid/valid pipelines: %.1f/%.1f", (sumInvalid * 1.0 / 1000), (sumValid * 1.0 / 1000));
        System.out.println("\n\n\n--------------\n\n\n");
    }

    private void analyseDiff(AllEvaluationResult allEvaluationResult, String randomSearchResultFilePath, int modelIndex0, int modelIndex1) {
        IOUtils iou = new IOUtils();
        int differentEvalCounter = 0;
        int similarEvalCounter = 0;
        int counter = 0;
        int timeOutCounter = 0;

        for (EvaluationResult evaluationResult : allEvaluationResult.getEvaluationResultList()) {

            EvaluationModel evalModel0 = evaluationResult.getEvaluationModel().get(modelIndex0);
            EvaluationModel evalModel1 = evaluationResult.getEvaluationModel().get(modelIndex1);

            if (evalModel0.getValidity().equals(evalModel1.getValidity())) {
                similarEvalCounter++;
            } else {
                differentEvalCounter++;
                String diffPath = randomSearchResultFilePath + "-" + String.valueOf(modelIndex0) + "vs" + String.valueOf(modelIndex1) + "-dif-" + String.valueOf(counter) + ".bpmn";

                System.out.println(diffPath + "             " + String.valueOf(evalModel0.getValidity()) + " vs " + String.valueOf(evalModel1.getValidity()));

                iou.overWriteData(evalModel1.getSpecs(), diffPath);
                counter++;

            }

            if (evalModel1.getSpecs().equals("timeout")) {
                timeOutCounter++;
                String timeoutPath = randomSearchResultFilePath + "-" + String.valueOf(modelIndex0) + "vs" + String.valueOf(modelIndex1) + "-timeout-" + String.valueOf(timeOutCounter) + ".bpmn";
                iou.overWriteData(evalModel1.getSpecs(), timeoutPath);
            }
        }

        System.out.println("Number of Pipelines: " + allEvaluationResult.getEvaluationResultList().size());
        System.out.println("Similar Evaluation Pipeline: " + similarEvalCounter);
        System.out.println("Different Evaluation Pipeline: " + differentEvalCounter);
        System.out.println("Timeout Pipeline: " + timeOutCounter);

    }

    private void rankModels(AllEvaluationResult allEvaluationResult,String randomSearchResultFilePath) {
        IOUtils iou = new IOUtils();
        ArrayList<EvaluationResult> evaluationResultList = allEvaluationResult.getEvaluationResultList();

        for (int i = 0; i < evaluationResultList.size(); i++) {
            EvaluationResult evaluationResultI = evaluationResultList.get(i);
            for (int j = 0; j < evaluationResultList.size(); j++) {
                EvaluationResult evaluationResultJ = evaluationResultList.get(j);

                if (i != j) {
                    if (evaluationResultJ.getEvaluationModel().get(1).getEvalTime()
                            < evaluationResultI.getEvaluationModel().get(1).getEvalTime()) {
                        Collections.swap(evaluationResultList, i, j);
                    }

                }

            }
        }
        
        

        for (int i = 0; i < 100; i++) {
            EvaluationModel evaluationModel1 = evaluationResultList.get(i).getEvaluationModel().get(1);
            EvaluationModel evaluationModel2 = evaluationResultList.get(i).getEvaluationModel().get(0);
            if (!evaluationModel1.getValidity() && !evaluationModel2.getValidity()) {
                 System.out.println("evalTime: " + evaluationModel1.getEvalTime() + " - "  + evaluationModel2.getEvalTime() + " - validity: " + evaluationModel1.getValidity() + " vs " + evaluationModel2.getValidity());
            
                String timeoutPath =randomSearchResultFilePath + "rank"+  i+".bpmn";
                iou.overWriteData(evaluationModel1.getSpecs(), timeoutPath);
            }
            
            
           
        }

    }

}
