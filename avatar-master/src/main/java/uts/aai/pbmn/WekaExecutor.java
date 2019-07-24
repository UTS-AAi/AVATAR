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
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.bind.JAXBException;
import uts.aai.avatar.model.EvaluationResult;
import uts.aai.avatar.optimisation.RandomSearch;

import uts.aai.global.AppConst;
import uts.aai.mf.configuration.MLComponentConfiguration;
import uts.aai.mf.model.MLComponentType;
import uts.aai.pn.utils.IOUtils;
import uts.aai.pn.utils.JSONUtils;

/**
 *
 * @author ntdun
 */
public class WekaExecutor {

    public boolean executeAlgorithm(String inputData, String outputData, String filterId) {

        boolean result = true;

        if (MLComponentConfiguration.getComponentByID(filterId).getmLComponentType().equals(MLComponentType.CLASSIFIER)) {
            result = executePredictor(inputData, outputData, filterId);
        } else {
            result = executeFilter(inputData, outputData, filterId);

        }

        File file = new File(outputData);
        System.out.println("Output Length: " + file.length());
        if (file.length() == 0) {
            result = false;
        }
        return result;

    }

    public boolean executeFilter(String inputData, String outputData, String filterId) {
        System.out.println("executeFilter xxxxxxxxxxxx");
        boolean result = true;

        if (Files.notExists(new File(inputData).toPath())) {
            result = false;
            System.out.println("No Input File ------------------------");
        } else if (new File(inputData).length() < 5) {
            result = false;
            System.out.println("Invalid Input File ------------------------");
        }

        if (result) {

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

        }
        return result;
    }

    public boolean executePredictor(String inputData, String outputModel, String predictorId) {
        System.out.println("executePredictor xxxxxxxxxxxx");
        boolean result = true;

        if (Files.notExists(new File(inputData).toPath())) {
            result = false;
            System.out.println("No Input File ------------------------");
        } else if (new File(inputData).length() < 5) {
            result = false;
            System.out.println("Invalid Input File ------------------------");
        }

        if (result) {

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

                }

                if (!error.equals("")) {
                    result = false;

                }
                if (!processOutput.contains("Time taken to build model")) {
                    result = false;
                }
                //System.out.println(error);
                Double accuracy = null;
                if (result) {
                    System.out.println("Process Execution SUCCESS");
                    accuracy = getAccuracyFromLog(processOutput);

                } else {
                    System.out.println("Process Execution FAILED");

                }

                System.out.println("Waiting for batch file ...");
                process.waitFor(3, TimeUnit.MINUTES);
                
                
                if (Files.notExists(new File(outputModel).toPath())) {
                    result = false;
                } else if (new File(outputModel).length() < 5) {
                    result = false;
                }
                
       
                System.out.println("Batch file done.");
                System.out.println("Done! " + predictorId);
                System.out.println("accuracy: " + accuracy);

                EvaluationResult evaluationResult = new EvaluationResult("", result, accuracy);

                String evaluationResultString = JSONUtils.marshal(evaluationResult, EvaluationResult.class);

                IOUtils iou = new IOUtils();

//            String outputLog = String.valueOf(result)+",";
//            iou.writeData(outputLog, AppConst.WEKA_EXECUTOR_LOG);
                iou.overWriteData(evaluationResultString, AppConst.TEMP_EVALUATION_RESULT_PATH);

            } catch (Exception ex) {
                if (Files.notExists(new File(outputModel).toPath())) {
                    result = false;
                } else if (new File(outputModel).length() < 5) {
                    result = false;
                }
            }
        } else {

            EvaluationResult evaluationResult = new EvaluationResult("", result, null);
            String evaluationResultString = "";
            try {
                evaluationResultString = JSONUtils.marshal(evaluationResult, EvaluationResult.class);
                IOUtils iou = new IOUtils();
                iou.overWriteData(evaluationResultString, AppConst.TEMP_EVALUATION_RESULT_PATH);
            } catch (JAXBException ex) {
                Logger.getLogger(WekaExecutor.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        return result;
    }

    public Double getAccuracyFromLog(String log) {

        System.out.println("getAccuracyFromLog");

        String pattern1 = "Correctly Classified Instances";
        String pattern2 = "%";

        Pattern p = Pattern.compile(Pattern.quote(pattern1) + "(.*?)" + Pattern.quote(pattern2));
        Matcher m = p.matcher(log);
        m.find();

        try {
            String accuracyStr = m.group(1);
            accuracyStr = accuracyStr.trim().replaceAll("\\s{2,}", " ");
            String[] acc = accuracyStr.split(" ");

            return Double.parseDouble(acc[1]);
        } catch (Exception e) {
        }

        return null;
    }

    private void test() {

    }

}
