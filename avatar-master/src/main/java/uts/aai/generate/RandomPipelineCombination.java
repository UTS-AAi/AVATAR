/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uts.aai.generate;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import uts.aai.feature.configuration.MLComponentConfiguration;
import uts.aai.feature.model.MLComponent;
import uts.aai.pn.utils.IOUtils;

/**
 *
 * @author ntdun
 */
public class RandomPipelineCombination {

    public int counter = 0;
    public String result = "";
    //public String datasetPath = "C:/DATA/Projects/eclipse-workspace/aai_aw/weka-3-7-7/data/testing/iris-missingvalues.arff";
    public String datasetPath = "C:/DATA/Projects/eclipse-workspace/aai_aw/weka-3-7-7/data/synthetic/dataset_1.arff";

    public String randomPipelinePath = "C:/DATA/Projects/eclipse-workspace/aai_aw/weka-3-7-7/combination/dataset_1/";
    public String outputWekaModelPath = "C:/DATA/Projects/eclipse-workspace/aai_aw/weka-3-7-7/data/testing/model/";

    public void permute(String[] arr) {
        permuteHelper(arr, 0);
    }

    private void permuteHelper(String[] arr, int index) {
        if (index >= arr.length - 1) { //If we are at the last element - nothing left to permute
            //System.out.println(Arrays.toString(arr));
            //Print the array

            String orderOfPreprocessing = "";

            for (int i = 0; i < arr.length; i++) {
                orderOfPreprocessing += arr[i];
                //preprocessingConfigurations += getConfigureOfType(arr[i]);
            }

            hanldeAlgorithmsForSelectedTemplate(orderOfPreprocessing);

            System.out.println(orderOfPreprocessing);

            return;
        }

        for (int i = index; i < arr.length; i++) { //For each index in the sub array arr[index...end]

            //Swap the elements at indices index and i
            String t = arr[index];
            arr[index] = arr[i];
            arr[i] = t;

            //Recurse on the sub array arr[index+1...end]
            permuteHelper(arr, index + 1);

            //Swap the elements back
            t = arr[index];
            arr[index] = arr[i];
            arr[i] = t;
        }
    }

    private void hanldeAlgorithmsForSelectedTemplate(String template) {

        MLComponentConfiguration.initConfiguration();

        List<List<MLComponent>> listOfConfigurations = new ArrayList<>();

        for (int i = 0; i < template.length(); i++) {
            String preprocessingType = String.valueOf(template.charAt(i));

            List<MLComponent> filterList = initFilterListByType(preprocessingType);
            listOfConfigurations.add(filterList);

        }

        List<MLComponent> filterList0 = listOfConfigurations.get(0);
        List<MLComponent> filterList1 = listOfConfigurations.get(1);
        List<MLComponent> filterList2 = listOfConfigurations.get(2);
        List<MLComponent> filterList3 = listOfConfigurations.get(3);
        List<MLComponent> filterList4 = listOfConfigurations.get(4);

        List<MLComponent> classifierList = MLComponentConfiguration.initClassifier();

        for (MLComponent filter0 : filterList0) {
            for (MLComponent filter1 : filterList1) {
                for (MLComponent filter2 : filterList2) {
                    for (MLComponent filter3 : filterList3) {
                        for (MLComponent filter4 : filterList4) {

                            for (MLComponent classifier : classifierList) {

                                ArrayList<MLComponent> orderedPipelineComponents = new ArrayList<>();

                                orderedPipelineComponents.add(filter0);
                                orderedPipelineComponents.add(filter1);
                                orderedPipelineComponents.add(filter2);
                                orderedPipelineComponents.add(filter3);
                                orderedPipelineComponents.add(filter4);

                                orderedPipelineComponents.add(classifier);

//                                    for (MLComponent cp : orderedPipelineComponents) {
//                                        System.out.println(cp.getComponentId());
//                                    }
//                                    System.out.println("---");
                                generateFilteredClassiferWeka(orderedPipelineComponents, template);
                                generateBPMNPipeline(orderedPipelineComponents, template);

                                counter++;
                                //break;

                            }
                        }

                    }
                }
            }
        }

    }

    private void generateFilteredClassiferWeka(ArrayList<MLComponent> orderedPipelineComponents, String template) {
        String pipeline = orderedPipelineComponents.get(0).getComponentExecutionScriptFilteredClassifierWeka()
                + orderedPipelineComponents.get(1).getComponentExecutionScriptFilteredClassifierWeka()
                + orderedPipelineComponents.get(2).getComponentExecutionScriptFilteredClassifierWeka()
                + orderedPipelineComponents.get(3).getComponentExecutionScriptFilteredClassifierWeka()
                + orderedPipelineComponents.get(4).getComponentExecutionScriptFilteredClassifierWeka()
                + "\" "
                + orderedPipelineComponents.get(5).getComponentExecutionScriptFilteredClassifierWeka();

        String outputFileName = template + counter;
        String fullCommand = prepareFullCommand(outputFileName, pipeline);
        String outputPipelineFilePath = randomPipelinePath + template + "_" + counter + ".txt";

        IOUtils iou = new IOUtils();
        iou.overWriteData(fullCommand, outputPipelineFilePath);
    }

    private void generateBPMNPipeline(ArrayList<MLComponent> orderedPipelineComponents, String template) {

        ClassLoader classLoader = getClass().getClassLoader();
//	File file = new File(classLoader.getResource("ml_pipeline_template.bpmn").getFile());

        IOUtils iou = new IOUtils();

        String templateFileContent = iou.readData(classLoader.getResource("ml_pipeline_template.bpmn").getFile());

        templateFileContent = templateFileContent.replaceAll("#filter_1#", orderedPipelineComponents.get(0).getComponentId());

        templateFileContent = templateFileContent.replaceAll("#input_dataset#", datasetPath);

        templateFileContent = templateFileContent.replaceAll("#filter_1#", orderedPipelineComponents.get(0).getComponentId());
        templateFileContent = templateFileContent.replaceAll("#filter_2#", orderedPipelineComponents.get(1).getComponentId());
        templateFileContent = templateFileContent.replaceAll("#filter_3#", orderedPipelineComponents.get(2).getComponentId());
        templateFileContent = templateFileContent.replaceAll("#filter_4#", orderedPipelineComponents.get(3).getComponentId());
        templateFileContent = templateFileContent.replaceAll("#filter_5#", orderedPipelineComponents.get(4).getComponentId());

        templateFileContent = templateFileContent.replaceAll("#classifier#", orderedPipelineComponents.get(5).getComponentId());

        String outputPipelineFilePath = randomPipelinePath + template + "_" + counter + ".bpmn";
        iou.overWriteData(templateFileContent, outputPipelineFilePath);

    }

    private List<MLComponent> initFilterListByType(String preprocessingType) {

        List<MLComponent> filterList = null;

        switch (preprocessingType) {
            case "B":
                filterList = MLComponentConfiguration.initDataBalancerComponent();
                break;
            case "M":
                filterList = MLComponentConfiguration.initMissingValueComponent();
                break;
            case "O":
                filterList = MLComponentConfiguration.initOutlierRemovalComponent();
                break;
            case "T":
                filterList = MLComponentConfiguration.initDataTransformationComponent();
                break;
            case "R":
                filterList = MLComponentConfiguration.initDimensionalityReductionComponent();
                break;
            case "S":
                filterList = MLComponentConfiguration.initDataSamplingComponent();
                break;
            default:
                break;
        }

        return filterList;
    }

    private String prepareFullCommand(String outputFile, String pipeline) {
        String command = "java -classpath C:/DATA/Projects/eclipse-workspace/aai_aw/weka-3-7-7/weka.jar weka.classifiers.meta.FilteredClassifier -t "
                //+ "C:/DATA/Projects/eclipse-workspace/aai_aw/weka-3-7-7/data/iris.arff"
                + datasetPath
                + " "
                + "-d " + outputWekaModelPath + outputFile + ".model "
                + "-F \"weka.filters.MultiFilter "
                + pipeline;

//                + preprocessingConfigurations
//                + "\" "
//                + classifierConfiguration;
        return command;
    }

}
