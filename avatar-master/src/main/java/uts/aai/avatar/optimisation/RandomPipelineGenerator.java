/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uts.aai.avatar.optimisation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import uts.aai.mf.configuration.MLComponentConfiguration;
import uts.aai.mf.model.MLComponent;
import uts.aai.pn.utils.IOUtils;

/**
 *
 * @author ntdun
 */
public class RandomPipelineGenerator {

    private String datasetPath;

    public RandomPipelineGenerator(String datasetPath) {
        this.datasetPath = datasetPath;
       
    }

    public String generate() {
        String componentStr = generateRandomComponents();
        System.out.println(componentStr);
        String componentTemplate = generateRandomComponents();
        ArrayList<MLComponent> orderedPipelineComponents = randomSelectAlgorithmsForPipeline(componentTemplate);
        String bpmnPipeline = generateBPMNPipeline(orderedPipelineComponents);

        return bpmnPipeline;
    }

    public String generateRandomComponents() {

        ArrayList<String> componentList = new ArrayList<String>();
        componentList.add("M");
        componentList.add("O");
        componentList.add("T");
        componentList.add("R");
        componentList.add("S");

        Collections.shuffle(componentList);

        String componentTemplate = "";
        for (String component : componentList) {
            componentTemplate += component;
        }

        return componentTemplate;
    }

    private ArrayList<MLComponent> randomSelectAlgorithmsForPipeline(String componentTemplate) {

        List<List<MLComponent>> listOfConfigurations = new ArrayList<>();

        for (int i = 0; i < componentTemplate.length(); i++) {
            String preprocessingType = String.valueOf(componentTemplate.charAt(i));
            List<MLComponent> filterList = initFilterListByType(preprocessingType);
            listOfConfigurations.add(filterList);
        }

        List<MLComponent> filterList0 = listOfConfigurations.get(0);
        List<MLComponent> filterList1 = listOfConfigurations.get(1);
        List<MLComponent> filterList2 = listOfConfigurations.get(2);
        List<MLComponent> filterList3 = listOfConfigurations.get(3);
        List<MLComponent> filterList4 = listOfConfigurations.get(4);
        List<MLComponent> classifierList = MLComponentConfiguration.initClassifier();
        
        Collections.shuffle(filterList0);
        Collections.shuffle(filterList1);
        Collections.shuffle(filterList2);
        Collections.shuffle(filterList3);
        Collections.shuffle(filterList4);
        Collections.shuffle(classifierList);
   

        ArrayList<MLComponent> orderedPipelineComponents = new ArrayList<>();

        orderedPipelineComponents.add(filterList0.get(0));
        orderedPipelineComponents.add(filterList1.get(0));
        orderedPipelineComponents.add(filterList2.get(0));
        orderedPipelineComponents.add(filterList3.get(0));
        orderedPipelineComponents.add(filterList4.get(0));
        orderedPipelineComponents.add(classifierList.get(0));

        return orderedPipelineComponents;

       
    }

    private String generateBPMNPipeline(ArrayList<MLComponent> orderedPipelineComponents) {

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

        //String outputPipelineFilePath = randomPipelinePath + template + "_" + pipelineId + ".bpmn";
        //iou.overWriteData(templateFileContent, outputPipelineFilePath);
        return templateFileContent;
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

}
