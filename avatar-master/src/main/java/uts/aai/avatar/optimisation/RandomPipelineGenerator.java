/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uts.aai.avatar.optimisation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import uts.aai.feature.configuration.MLComponentConfiguration;
import uts.aai.feature.model.MLComponent;
import uts.aai.pn.utils.IOUtils;

/**
 *
 * @author ntdun
 */
public class RandomPipelineGenerator {

    private String datasetPath;
    private String outputFolder;

    public RandomPipelineGenerator(String datasetPath, String outputFolder) {
        this.datasetPath = datasetPath;
        this.outputFolder = outputFolder;
    }

    public String generateBPMNPipeline() {
//        String componentStr = generateRandomComponents();
//        System.out.println(componentStr);
        String componentTemplate = generateRandomComponents();
        ArrayList<MLComponent> orderedPipelineComponents = randomSelectAlgorithmsForPipeline(componentTemplate);
        String bpmnPipeline = RandomPipelineGenerator.this.generateBPMNPipeline(orderedPipelineComponents);

        return bpmnPipeline;
    }

    public String generateBPMNPipelineWithRandomComponents(int numberOfPreprocessingComponents) {

        String componentTemplate = generateRandomComponents2(numberOfPreprocessingComponents);
        ArrayList<MLComponent> orderedPipelineComponents = randomSelectAlgorithmsForPipeline(componentTemplate);
        String bpmnPipeline = generateBPMNPipelineWithRandomTemplate(orderedPipelineComponents);

        return bpmnPipeline;
    }

    public String generateRandomComponents2(int numberOfPreprocessingComponents) {

        ArrayList<String> componentList = new ArrayList<String>();
        componentList.add("M");
        componentList.add("O");
        componentList.add("T");
        componentList.add("R");
        componentList.add("S");

        Collections.shuffle(componentList);

        String componentTemplate = "";
        for (int i = 0; i < numberOfPreprocessingComponents; i++) {
            String component = componentList.get(i);
            componentTemplate += component;
        }

        return componentTemplate;
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
        ArrayList<MLComponent> orderedPipelineComponents = new ArrayList<>();

        for (int i = 0; i < componentTemplate.length(); i++) {
            String preprocessingType = String.valueOf(componentTemplate.charAt(i));
            List<MLComponent> filterList = initFilterListByType(preprocessingType);
            Collections.shuffle(filterList);
            listOfConfigurations.add(filterList);
            orderedPipelineComponents.add(filterList.get(0));

        }

        List<MLComponent> classifierList = MLComponentConfiguration.initClassifier();
        Collections.shuffle(classifierList);
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

    private String generateBPMNPipelineWithRandomTemplate(ArrayList<MLComponent> orderedPipelineComponents) {

        String templateFileContent = generateBPMNTemplate(orderedPipelineComponents.size() - 1);
        templateFileContent = templateFileContent.replaceAll("#input_dataset#", datasetPath);

        for (int i = 0; i < orderedPipelineComponents.size() - 1; i++) {
            String filterName = "#filter_" + (i + 1) + "#";
            templateFileContent = templateFileContent.replaceAll(filterName, orderedPipelineComponents.get(i).getComponentId());
        }

        templateFileContent = templateFileContent.replaceAll("#classifier#", orderedPipelineComponents.get(orderedPipelineComponents.size() - 1).getComponentId());

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

    public String generateBPMNTemplate(int numberOfPreprocessingComponents) {

        String pipelineTemplate = "";

        ArrayList<String> listOfUUIDs = new ArrayList<>();

        String scriptTaskBegin = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<definitions xmlns=\"http://www.omg.org/spec/BPMN/20100524/MODEL\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:bpmndi=\"http://www.omg.org/spec/BPMN/20100524/DI\" xmlns:dc=\"http://www.omg.org/spec/DD/20100524/DC\" xmlns:di=\"http://www.omg.org/spec/DD/20100524/DI\" xmlns:g=\"http://www.jboss.org/drools/flow/gpd\" xmlns:tns=\"http://www.jboss.org/drools\" id=\"Definition\" targetNamespace=\"http://www.jboss.org/drools\" expressionLanguage=\"http://www.mvel.org/2.0\" typeLanguage=\"http://www.java.com/javaTypes\" exporter=\"Camunda Modeler\" exporterVersion=\"2.2.4\" xsi:schemaLocation=\"http://www.omg.org/spec/BPMN/20100524/MODEL BPMN20.xsd\">\n"
                + "  <process id=\"ml.process\" name=\"ML Process\" processType=\"Private\" isExecutable=\"true\">\n"
                + "    <startEvent id=\"StartEvent\" name=\"StartEvent\">\n"
                + "      <outgoing>#start_event_outgoing#</outgoing>\n"
                + "    </startEvent>";

        String scriptTaskEnd = "\n<scriptTask id=\"#classifier#\" name=\"#classifier#\" scriptFormat=\"http://www.java.com/java\">\n"
                + "      <incoming>#script_classifier_incoming#</incoming>\n"
                + "      <outgoing>SequenceFlow_0tiw9jt</outgoing>\n"
                + "      <script>kcontext.setVariable(\"#filter_input_data_name#\",\"#filter_input_data_value#\");\n"
                + "                kcontext.setVariable(\"output-model-1\",\"" + outputFolder + "model-1-output.model\");\n"
                + "                \n"
                + "                String predictorName = \"#classifier#\";\n"
                + "          \n"
                + "                uts.aai.pbmn.WekaExecutor wekaExecutor = new uts.aai.pbmn.WekaExecutor();\n"
                + "                wekaExecutor.executePredictor(String.valueOf(kcontext.getVariable(\"#filter_input_data_name#\")),String.valueOf(kcontext.getVariable(\"output-model-1\")),predictorName);</script>\n"
                + "    </scriptTask>\n"
                + "    \n"
                + "    \n"
                + "    <endEvent id=\"EndEvent\" name=\"EndEvent\">\n"
                + "      <incoming>SequenceFlow_0tiw9jt</incoming>\n"
                + "      <terminateEventDefinition />\n"
                + "    </endEvent>";

        String filterScriptTaskTemplate = "\n<scriptTask id=\"#filter_#script_filter_no##\" name=\"#filter_#script_filter_no##\" scriptFormat=\"http://www.java.com/java\">\n"
                + "      <incoming>#script_filter_incoming#</incoming>\n"
                + "      <outgoing>#script_filter_outgoing#</outgoing>\n"
                + "      <script>kcontext.setVariable(\"#filter_input_data_name#\",\"#filter_input_data_value#\");\n"
                + "                kcontext.setVariable(\"#filter_output_data_name#\",\"#filter_output_data_value#\");\n"
                + "                \n"
                + "                String filterName = \"#filter_#script_filter_no##\";\n"
                + "          \n"
                + "                uts.aai.pbmn.WekaExecutor wekaExecutor = new uts.aai.pbmn.WekaExecutor();\n"
                + "                wekaExecutor.executeFilter(String.valueOf(kcontext.getVariable(\"#filter_input_data_name#\")),String.valueOf(kcontext.getVariable(\"#filter_output_data_name#\")),filterName);</script>\n"
                + "    </scriptTask>";

        String sequenceFlowTemplate = "\n<sequenceFlow id=\"#sequence_flow_id#\" sourceRef=\"#sequence_flow_src#\" targetRef=\"#sequence_flow_target#\" />";

        //int numberOfPreprocessingComponents = randInt(1, 5);
        pipelineTemplate += scriptTaskBegin;

        if (numberOfPreprocessingComponents == 0) {

            String uuidClassifier = randUUID();
            pipelineTemplate = pipelineTemplate.replaceAll("#start_event_outgoing#", uuidClassifier);

            String classifierStr = new String(scriptTaskEnd);
            classifierStr = classifierStr.replaceAll("#script_classifier_incoming#", uuidClassifier);

            int i = 0;
            String filterInputName = "input-data-set-" + String.valueOf(i);
            String filterInputValue = "#input_dataset#";

            classifierStr = classifierStr.replaceAll("#filter_input_data_name#", filterInputName);
            classifierStr = classifierStr.replaceAll("#filter_input_data_value#", filterInputValue);
            listOfUUIDs.add(uuidClassifier);
            pipelineTemplate += classifierStr;
        }

        for (int i = 1; i <= numberOfPreprocessingComponents; i++) {

            String uuid = randUUID();

            if (i == 1) {
                pipelineTemplate = pipelineTemplate.replaceAll("#start_event_outgoing#", uuid);

                String filterStr = new String(filterScriptTaskTemplate);
                filterStr = filterStr.replaceAll("#script_filter_incoming#", uuid);
                filterStr = filterStr.replaceAll("#script_filter_no#", String.valueOf(i));

                String filterInputName = "input-data-set-" + String.valueOf(i);
                String filterInputValue = "#input_dataset#";

                String filterOutputName = "output-data-set-" + String.valueOf(i);
                String filterOutputValue = outputFolder + "temp-data-out-" + String.valueOf(i) + ".arff";

                filterStr = filterStr.replaceAll("#filter_input_data_name#", filterInputName);
                filterStr = filterStr.replaceAll("#filter_input_data_value#", filterInputValue);
                filterStr = filterStr.replaceAll("#filter_output_data_name#", filterOutputName);
                filterStr = filterStr.replaceAll("#filter_output_data_value#", filterOutputValue);
                pipelineTemplate += filterStr;
                listOfUUIDs.add(uuid);

            } else if (i > 1) {

                pipelineTemplate = pipelineTemplate.replaceAll("#script_filter_outgoing#", uuid);

                String filterStr = new String(filterScriptTaskTemplate);
                filterStr = filterStr.replaceAll("#script_filter_incoming#", uuid);
                filterStr = filterStr.replaceAll("#script_filter_no#", String.valueOf(i));

                String filterInputName = "input-data-set-" + String.valueOf(i);
                String filterInputValue = outputFolder + "temp-data-out-" + String.valueOf(i - 1) + ".arff";

                String filterOutputName = "output-data-set-" + String.valueOf(i);
                String filterOutputValue = outputFolder + "temp-data-out-" + String.valueOf(i) + ".arff";

                filterStr = filterStr.replaceAll("#filter_input_data_name#", filterInputName);
                filterStr = filterStr.replaceAll("#filter_input_data_value#", filterInputValue);
                filterStr = filterStr.replaceAll("#filter_output_data_name#", filterOutputName);
                filterStr = filterStr.replaceAll("#filter_output_data_value#", filterOutputValue);

                pipelineTemplate += filterStr;
                listOfUUIDs.add(uuid);
            }

            if (i == numberOfPreprocessingComponents) {

                String uuidClassifier = randUUID();
                pipelineTemplate = pipelineTemplate.replaceAll("#script_filter_outgoing#", uuidClassifier);

                String classifierStr = new String(scriptTaskEnd);
                classifierStr = classifierStr.replaceAll("#script_classifier_incoming#", uuidClassifier);

                String filterInputName = "input-data-set-" + String.valueOf(i + 1);
                String filterInputValue = outputFolder + "temp-data-out-" + String.valueOf(i) + ".arff";

                classifierStr = classifierStr.replaceAll("#filter_input_data_name#", filterInputName);
                classifierStr = classifierStr.replaceAll("#filter_input_data_value#", filterInputValue);
                listOfUUIDs.add(uuidClassifier);
                pipelineTemplate += classifierStr;
            }

        }

        listOfUUIDs.add("SequenceFlow_0tiw9jt");

        for (int i = 0; i < listOfUUIDs.size(); i++) {
            String sequenceFlowId = listOfUUIDs.get(i);
            String sequenceFlowSource = "#filter_" + i + "#";
            String sequenceFlowTarget = "#filter_" + (i + 1) + "#";

            if (i == 0) {
                sequenceFlowSource = "StartEvent";

            }

            if (i == listOfUUIDs.size() - 2) {
                sequenceFlowTarget = "#classifier#";
            }

            if (i == listOfUUIDs.size() - 1) {
                sequenceFlowSource = "#classifier#";
                sequenceFlowTarget = "EndEvent";
            }

            String sequenceFlowStr = new String(sequenceFlowTemplate);
            sequenceFlowStr = sequenceFlowStr.replaceAll("#sequence_flow_id#", sequenceFlowId);
            sequenceFlowStr = sequenceFlowStr.replaceAll("#sequence_flow_src#", sequenceFlowSource);
            sequenceFlowStr = sequenceFlowStr.replaceAll("#sequence_flow_target#", sequenceFlowTarget);

            pipelineTemplate += sequenceFlowStr;

        }

        pipelineTemplate += "\n</process>\n </definitions>";

        return pipelineTemplate;
    }

    private String randUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

}
