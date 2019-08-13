/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uts.aai.resultanalysis;

import org.json.JSONObject;
import uts.aai.pn.utils.IOUtils;

/**
 *
 * @author ntdun
 */
public class TPOTHistoryRunAnalyse {
    public void analyse(String filePath) {
        
        int validPipelineCounter = 0;
        int invalidPipelineCounter = 0;
        
        IOUtils iou = new IOUtils();
        String jsonStr = iou.readData(filePath);
        
        String[] strs = jsonStr.split("\n");
        
        for (String str: strs) {
            String[] element = str.split(";");
            if (!element[0].equals("")) {
                boolean validity = Boolean.parseBoolean(element[0]);
                if (validity) {
                    validPipelineCounter++;
                } else {
                    invalidPipelineCounter++;
                }
                        
            } 
                
            
        }
        
        System.out.println("Valid Pipelines: " + validPipelineCounter);
        System.out.println("Invalid Pipelines: " + invalidPipelineCounter);
        
    }
}
