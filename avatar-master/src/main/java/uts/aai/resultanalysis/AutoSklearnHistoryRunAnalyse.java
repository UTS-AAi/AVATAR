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

    public void analyseAutosklearnHistoryRun(String filePath) {
        IOUtils iou = new IOUtils();
        String jsonStr = iou.readData(filePath);
        //System.out.println("jsonstr: "+jsonStr);

        JSONObject obj = new JSONObject(jsonStr);
        System.out.println("" + obj.toString());
    }
}
