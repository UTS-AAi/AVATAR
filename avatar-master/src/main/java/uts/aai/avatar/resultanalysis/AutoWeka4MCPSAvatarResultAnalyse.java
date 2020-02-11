/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uts.aai.avatar.resultanalysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import uts.aai.utils.CSVUtils;

/**
 *
 * @author ntdun
 */
public class AutoWeka4MCPSAvatarResultAnalyse {

    public String analyse(String folderPath, String seed) {
        
        String bestPipeline="-";

        String runAndResultFilePath = folderPath + "\\out\\autoweka\\state-run" + seed;

        String[] pathnames;
        

        File f = new File(runAndResultFilePath);
        pathnames = f.list();

        String rsFileName = "";
        for (String pathname : pathnames) {
           // System.out.println(pathname);
            if (pathname.contains("runs_and_results") && pathname.contains(".csv")) {
                rsFileName = pathname;
                break;
            }
        }

        if (!rsFileName.equals("")) {
           // System.out.println("FOUND RESULT OUTPUT FILE !!!!! \n\n\n");
            rsFileName = runAndResultFilePath + "\\" + rsFileName;

            CSVUtils cSVUtils = new CSVUtils();
            List<String[]> listOfRows = cSVUtils.readCSVFile(rsFileName);
            
            Double bestRunQuality = Double.MAX_VALUE;
            int bestRunIndex = -1;
            
            for (int i=1;i<listOfRows.size();i++) {
                Double currentRunQuality = Double.parseDouble(listOfRows.get(i)[10]);
                if (currentRunQuality<bestRunQuality) {
                    bestRunQuality = currentRunQuality;
                    bestRunIndex = i;
                }
            }
            
            String avatarPipelineLogFilePath = folderPath + "\\avatar_log\\" + "pipelines.txt";
            List<String[]> listOfPipelines = cSVUtils.readCSVFile(avatarPipelineLogFilePath);
            
            System.out.println("Best Run Index: " + bestRunIndex);
            System.out.println("Best Run Quality: " + bestRunQuality);
            System.out.println("Total evaluated Pipeline: " + (listOfRows.size()-1));
            System.out.println("Best Pipeline: " + listOfPipelines.get(bestRunIndex-1)[0]);
            
            bestPipeline = listOfPipelines.get(bestRunIndex-1)[0];
        } else {
            //System.out.println("CAN'T FIND RESULT OUTPUT FILE !!!!!");
        }
        
        
        return bestPipeline;
        

    }
    
    

}
