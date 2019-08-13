/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uts.aai.resultanalysis;


import uts.aai.pn.utils.IOUtils;
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
        int others = 0;
       
        JSONArray objData = objRoot.getJSONArray("data");
        for (int i=0; i<objData.length();i++) {
            
            JSONArray objElement =  objData.getJSONArray(i);
            String pipelineStatus = objElement.getJSONArray(1).get(2).toString();
  
            if (pipelineStatus.contains("SUCCESS")) {
                success++;
            } else if (pipelineStatus.contains("CRASHED")) {
                crashed++;
            } else if (pipelineStatus.contains("MEMOUT")) {
                memout++;
            } else {
                others++;
            }
            
           
            
        }
        
        System.out.println("Success: " + success);
        System.out.println("crashed: " + crashed);
        System.out.println("memout: " + memout);
        System.out.println("others: " + others);
        
        //System.out.println("objData: " + objData.toString());
    }
    
    
    
    
    
}
