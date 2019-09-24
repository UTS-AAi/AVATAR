/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uts.aai.avatar.resultanalysis;

import org.json.JSONObject;
import uts.aai.utils.IOUtils;

/**
 *
 * @author ntdun
 */
public class TPOTHistoryRunAnalyse {
    public void analyse(String filePath) {
        
        int validPipelineCounter = 0;
        double validTime =0.0;
        int invalidPipelineCounter = 0;
        double invalidTime =0.0;
        int allCounter = 0;
        IOUtils iou = new IOUtils();
        String jsonStr = iou.readData(filePath);
        
        String[] strs = jsonStr.split("\n");
        
        for (String str: strs) {
            String[] element = str.split(";");
            if (!element[0].equals("")) {
                boolean validity = Boolean.parseBoolean(element[0]);
                double runtime = Double.parseDouble(element[1]);
                if (validity) {
                    validPipelineCounter++;
                    validTime += runtime;
                } else {
                    invalidPipelineCounter++;
                    invalidTime += runtime;
                }
                allCounter++;
                        
            } 
                
            
        }
        
        
        
        
        System.out.println("Valid Pipelines: " + validPipelineCounter);
        System.out.println("Invalid Pipelines: " + invalidPipelineCounter);
        System.out.println("RS Pipelines: " + invalidPipelineCounter+"/"+allCounter);
        System.out.println("Invalid pipeline evaluation time: " + (int)(invalidTime/invalidPipelineCounter/Math.pow(10, 6)) + " ms");
        System.out.println("Invalid/Total pipeline evaluation time: " + (int)(invalidTime/invalidPipelineCounter/Math.pow(10, 6)) 
                + "/" + (int)((validTime+invalidTime)/(validPipelineCounter+invalidPipelineCounter)/Math.pow(10, 6)) + " ms");
        System.out.println("Wasted time: " + invalidTime*100/(1*60*60*Math.pow(10, 9)));
        
       
    }
}
