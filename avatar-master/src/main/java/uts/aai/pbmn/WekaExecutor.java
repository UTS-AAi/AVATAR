/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uts.aai.pbmn;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;
import uts.aai.global.AppConst;
import uts.aai.mf.configuration.MLComponentConfiguration;
import uts.aai.mf.model.MLComponentType;
import uts.aai.pn.utils.IOUtils;

/**
 *
 * @author ntdun
 */
public class WekaExecutor {
    
    
    public boolean executeAlgorithm(String inputData, String outputData, String filterId){
        
        boolean result = true;
        
        if (MLComponentConfiguration.getComponentByID(filterId).getmLComponentType().equals(MLComponentType.CLASSIFIER)) {
            result = executePredictor(inputData, outputData, filterId);
        } else {
            result = executeFilter(inputData, outputData, filterId);
            
        }
        
        File file = new File(outputData);
        System.out.println("Output Length: " + file.length());
        if (file.length()==0) {
            result = false;
        }
        return result;
        
    }

    public boolean executeFilter(String inputData, String outputData, String filterId) {

        boolean result = true;
        
        
        try {

            String wekajar = AppConst.WEKA_JAR_PATH;

            String commandStr = "java -classpath " + wekajar + " "
                    + MLComponentConfiguration.getComponentByID(filterId).getComponentExecutionScriptSingleComponentWeka()
                    + " -i " + inputData
                    + " -o " + outputData;

            System.out.println("command: \n" + commandStr);
            Process process = Runtime.getRuntime().exec(commandStr);
            System.out.println("Waiting for batch file ...");
            process.waitFor();
            System.out.println("Batch file done.");
            System.out.println("Done! " + filterId);
            
            
        } catch (Exception ex) {
            Logger.getLogger(WekaExecutor.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        if (Files.notExists(new File(outputData).toPath())) {
            result = false;
            
        }
        
        
        return result;
    }

    public boolean executePredictor(String inputData, String outputModel, String predictorId) {

        boolean result = true;

        try {

            String wekajar = AppConst.WEKA_JAR_PATH;

            String commandStr = "java -classpath " + wekajar + " "
                    + MLComponentConfiguration.getComponentByID(predictorId).getComponentExecutionScriptSingleComponentWeka()
                    + " -t " + inputData
                    + " -d " + outputModel + "\n";
            System.out.println(commandStr);
            Process process = Runtime.getRuntime().exec(commandStr);
           

         

            String processOutput = "";
            String s = "";
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));

            BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            // read the output from the command
            System.out.println("Here is the standard output of the command:\n");
            while ((s = stdInput.readLine()) != null) {
                processOutput += s;
                System.out.println(s);
            }

            String error = "";
            // read any errors from the attempted command
            System.out.println("Here is the standard error of the command (if any):\n");
            while ((s = stdError.readLine()) != null) {
                error += s;

                System.out.println(s);
            }

            if (!error.equals("")) {
                result = false;

            }
            if (!processOutput.contains("Time taken to build model")) {
                result = false;
            }
            
            
            if (result) {
                System.out.println("Process Execution SUCCESS");
            } else {
                System.out.println("Process Execution FAILED");
                
            }
            
             System.out.println("Waiting for batch file ...");
            process.waitFor();
            System.out.println("Batch file done.");
            System.out.println("Done! " + predictorId);

            String outputLog = String.valueOf(result)+",";
            IOUtils iou = new IOUtils();
            iou.writeData(outputLog, AppConst.WEKA_EXECUTOR_LOG);

        } catch (Exception ex) {
            Logger.getLogger(WekaExecutor.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

    private void test() {

    }

}
