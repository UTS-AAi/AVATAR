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

    public String generateBPMNPipeline() {
        String componentStr = generateRandomComponents();
        System.out.println(componentStr);
        String componentTemplate = generateRandomComponents();
        ArrayList<MLComponent> orderedPipelineComponents = randomSelectAlgorithmsForPipeline(componentTemplate);
        String bpmnPipeline = RandomPipelineGenerator.this.generateBPMNPipeline(orderedPipelineComponents);

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

    public void generateBPMNTemplate() {
        
        String pipelineTemplate = "";
        
        ArrayList<String> listOfUUIDs = new ArrayList<>();

        String scriptTaskBegin = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<definitions xmlns=\"http://www.omg.org/spec/BPMN/20100524/MODEL\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:bpmndi=\"http://www.omg.org/spec/BPMN/20100524/DI\" xmlns:dc=\"http://www.omg.org/spec/DD/20100524/DC\" xmlns:di=\"http://www.omg.org/spec/DD/20100524/DI\" xmlns:g=\"http://www.jboss.org/drools/flow/gpd\" xmlns:tns=\"http://www.jboss.org/drools\" id=\"Definition\" targetNamespace=\"http://www.jboss.org/drools\" expressionLanguage=\"http://www.mvel.org/2.0\" typeLanguage=\"http://www.java.com/javaTypes\" exporter=\"Camunda Modeler\" exporterVersion=\"2.2.4\" xsi:schemaLocation=\"http://www.omg.org/spec/BPMN/20100524/MODEL BPMN20.xsd\">\n"
                + "  <process id=\"ml.process\" name=\"ML Process\" processType=\"Private\" isExecutable=\"true\">\n"
                + "    <startEvent id=\"StartEvent\" name=\"StartEvent\">\n"
                + "      <outgoing>#start_event_outgoing#</outgoing>\n"
                + "    </startEvent>";

        
        String scriptTaskEnd = "<scriptTask id=\"#classifier#\" name=\"#classifier#\" scriptFormat=\"http://www.java.com/java\">\n"
                + "      <incoming>#script_classifier_incoming#</incoming>\n"
                + "      <outgoing>SequenceFlow_0tiw9jt</outgoing>\n"
                + "      <script>kcontext.setVariable(\"#filter_input_data_name#\",\"#filter_input_data_value#\");\n"
                + "                kcontext.setVariable(\"output-model-1\",\"C:/experiments/tools/avatar/output/model-1-output.model\");\n"
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
        
        
        String filterScriptTaskTemplate = "<scriptTask id=\"#filter_#script_filter_no##\" name=\"#filter_#script_filter_no##\" scriptFormat=\"http://www.java.com/java\">\n"
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
        
        
        String sequenceFlowTemplate = "<sequenceFlow id=\"#sequence_flow_id#\" sourceRef=\"#sequence_flow_src#\" targetRef=\"#sequence_flow_target#\" />";
        String str5 = "";

        int numberOfPreprocessingComponents = randInt(1, 5);
        
        pipelineTemplate +=scriptTaskBegin;
        
   
        for (int i=1;i<=numberOfPreprocessingComponents;i++) {
            
           String uuid = randUUID();
           
           if (i==1) {
               pipelineTemplate.replaceAll("#start_event_outgoing#", uuid);
               
               String filterStr = new String(filterScriptTaskTemplate);
               filterStr.replaceAll("#script_filter_incoming#", uuid);
               filterStr.replaceAll("#script_filter_no#", String.valueOf(i));
               
               String filterInputName = "input-data-set-" + String.valueOf(i);
               String filterInputValue = "#input_dataset#";
               
               String filterOutputName = "output-data-set-" + String.valueOf(i);
               String filterOutputValue = "C:/experiments/tools/avatar/output/temp-data-out-"+String.valueOf(i)+".arff";
               
               
               filterStr.replaceAll("#filter_input_data_name#", filterInputName);
               filterStr.replaceAll("#filter_input_data_value#", filterInputValue);
               filterStr.replaceAll("#filter_output_data_name#", filterOutputName);
               filterStr.replaceAll("#filter_output_data_value#", filterOutputValue);
               pipelineTemplate +=filterStr;
               listOfUUIDs.add(uuid);
               
           } else if (i>1) {
               
              
               pipelineTemplate.replaceAll("#script_filter_outgoing#", uuid);
               
               String filterStr = new String(filterScriptTaskTemplate);
               filterStr.replaceAll("#script_filter_incoming#", uuid);
               filterStr.replaceAll("#script_filter_no#", String.valueOf(i));
               
               String filterInputName = "input-data-set-" + String.valueOf(i);
               String filterInputValue = "C:/experiments/tools/avatar/output/temp-data-out-"+String.valueOf(i-1)+".arff";
               
               String filterOutputName = "output-data-set-" + String.valueOf(i);
               String filterOutputValue = "C:/experiments/tools/avatar/output/temp-data-out-"+String.valueOf(i)+".arff";
               
               
               filterStr.replaceAll("#filter_input_data_name#", filterInputName);
               filterStr.replaceAll("#filter_input_data_value#", filterInputValue);
               filterStr.replaceAll("#filter_output_data_name#", filterOutputName);
               filterStr.replaceAll("#filter_output_data_value#", filterOutputValue);
               
               
               pipelineTemplate +=filterStr;
               listOfUUIDs.add(uuid);
           }
           
           
           if (i==numberOfPreprocessingComponents) {
               
               
                String uuidClassifier = randUUID();
                pipelineTemplate.replaceAll("#script_filter_outgoing#", uuidClassifier);
                
                String classifierStr = new String(scriptTaskEnd);
                classifierStr.replaceAll("#script_classifier_incoming#", uuidClassifier);
                
               
                
               String filterInputName = "input-data-set-" + String.valueOf(i+1);
               String filterInputValue = "C:/experiments/tools/avatar/output/temp-data-out-"+String.valueOf(i)+".arff";
               
               classifierStr.replaceAll("#filter_input_data_name#", filterInputName);
               classifierStr.replaceAll("#filter_input_data_value#", filterInputValue);
               listOfUUIDs.add(uuidClassifier);
               pipelineTemplate += classifierStr;
           }
           
           
        }
    

        listOfUUIDs.add("SequenceFlow_0tiw9jt");
        
        for (int i=0;i<listOfUUIDs.size();i++) {
            String sequenceFlowId = listOfUUIDs.get(i);
            String sequenceFlowSource = "";
            String sequenceFlowTarget = "";
            
            if (i==0) {
                sequenceFlowSource = "StartEvent"; 
            } else if (i==listOfUUIDs.size()-1) {
                
            }
            
            
        }
        
        
        
    }

    private int randInt(int min, int max) {

        int randomNum = ThreadLocalRandom.current().nextInt(min, max + 1);
        return randomNum;
    }
    
    private String randUUID(){
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

}
