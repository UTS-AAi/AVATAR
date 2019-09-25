/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uts.aai.avatar.resultanalysis;


import uts.aai.utils.IOUtils;
import org.json.*;

/**
 *
 * @author ntdun
 */
public class AutoSklearnHistoryRunAnalyse {

    public void analyse(String filePath) {
        IOUtils iou = new IOUtils();
        String jsonStr = iou.readData(filePath);
        //System.out.println("jsonstr: "+jsonStr);

        JSONObject objRoot = new JSONObject(jsonStr);
        //System.out.println("" + objRoot.toString(4));
        
        int success = 0;
        int crashed= 0;
        int memout = 0;
        int timeout = 0;
        int others = 0;
        
        double sucesss_time = 0.0;
        double crashed_time = 0.0;
        double memout_time = 0.0;
        double timeout_time = 0.0;
        double others_time = 0.0;
       
        JSONArray objData = objRoot.getJSONArray("data");
        for (int i=0; i<objData.length();i++) {
            
            JSONArray objElement =  objData.getJSONArray(i);
            
   
            
            String pipelineStatus = objElement.getJSONArray(1).get(2).toString();
  
            if (pipelineStatus.contains("SUCCESS")) {
                success++;
                double tmp_time = Double.parseDouble(objElement.getJSONArray(1).get(1).toString());
                sucesss_time += tmp_time;
            } else if (pipelineStatus.contains("CRASHED")) {
                crashed++;
                double tmp_time = Double.parseDouble(objElement.getJSONArray(1).get(1).toString());
                crashed_time += tmp_time;
            } else if (pipelineStatus.contains("MEMOUT")) {
                memout++;
                double tmp_time = Double.parseDouble(objElement.getJSONArray(1).get(1).toString());
                memout_time += tmp_time;
            } else if (pipelineStatus.contains("TIMEOUT")) {
                timeout++;
                double tmp_time = Double.parseDouble(objElement.getJSONArray(1).get(1).toString());
                timeout_time += tmp_time;
            } else {
                others++;
                double tmp_time = Double.parseDouble(objElement.getJSONArray(1).get(1).toString());
                others_time += tmp_time;
                System.out.println("pipelineStatus: " + pipelineStatus);
            }
            
           
            
        }
        
        System.out.println("Success: " + success + " - " + sucesss_time);
        System.out.println("crashed: " + crashed + " - " + crashed_time);
        System.out.println("memout: " + memout + " - " + memout_time);
        System.out.println("timeout: " + timeout + " - " + timeout_time);
        System.out.println("others: " + others + " - " + others_time);
        
        System.out.println("Invalid pipelines/all: " + crashed + "/" + (success+crashed+memout+timeout+others));
        
        double avgInvalidPipelineEvaluationTime = crashed_time*100/crashed;
        double avgTotalPipelineEvaluationTime = (sucesss_time+crashed_time+memout_time+timeout_time+others_time)*100/(success+crashed+memout+timeout+others);
        
        System.out.println("Invalid pipelines/all evaluation time: " + (int)avgInvalidPipelineEvaluationTime +"/" + (int)avgTotalPipelineEvaluationTime );
        System.out.println("Wasted Time: " + (crashed_time/(sucesss_time+crashed_time+memout_time+timeout_time+others_time))*100);
        
    }
    
    
    
    
    
}
