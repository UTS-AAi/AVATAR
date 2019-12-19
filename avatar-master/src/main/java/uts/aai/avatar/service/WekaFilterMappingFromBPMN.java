/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uts.aai.avatar.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.drools.KnowledgeBase;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.definition.process.Connection;
import org.drools.definition.process.Node;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.jbpm.ruleflow.core.RuleFlowProcess;
import org.jbpm.workflow.core.node.ActionNode;
import org.jbpm.workflow.core.node.EndNode;
import org.jbpm.workflow.core.node.StartNode;
import uts.aai.avatar.configuration.MLComponentConfiguration;
import uts.aai.avatar.model.MLComponent;
import uts.aai.avatar.model.MLComponentIO;
import uts.aai.avatar.service.DatasetMetaFeatures;

import uts.aai.pn.model.Arc;
import uts.aai.pn.model.MetaFeatureEvaluationTransitionFunction;
import uts.aai.pn.model.Parameter;
import uts.aai.pn.model.PetriNetsPipeline;
import uts.aai.pn.model.Place;
import uts.aai.pn.model.Token;
import uts.aai.pn.model.Transition;
import uts.aai.utils.IOUtils;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.core.converters.CSVSaver;

/**
 *
 * @author ntdun
 */
public class WekaFilterMappingFromBPMN {

    public String datasetPath = "C:/DATA/Projects/eclipse-workspace/aai_aw/weka-3-7-7/data/synthetic/dataset_1.arff";

    public String randomPipelinePath = "C:/DATA/Projects/eclipse-workspace/aai_aw/weka-3-7-7/combination/dataset_1_weka/";
    public String outputWekaModelPath = "C:/DATA/Projects/eclipse-workspace/aai_aw/weka-3-7-7/data/testing/model/";

    public File bpmnModel;
    
    private List<MLComponent> loadedListOfMLComponents;

    public WekaFilterMappingFromBPMN() {
        MLComponentConfiguration.initDefault();
        loadedListOfMLComponents = MLComponentConfiguration.getListOfMLComponents();
    }
    
    
    
    

    public void mappingFromBPMN2NativeWekaCommand(File bpmnModel) {

        this.bpmnModel = bpmnModel;

        ArrayList<MLComponent> listOfMLComponents = new ArrayList<>();

        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();

        kbuilder.add(ResourceFactory.newFileResource(bpmnModel), ResourceType.BPMN2);

        KnowledgeBase kbase = kbuilder.newKnowledgeBase();

        RuleFlowProcess process = (RuleFlowProcess) kbase.getProcess("ml.process");

        StartNode startNode = process.getStart();
        System.out.println("start node " + startNode.getName());

        ActionNode currentActionNode = null;

        boolean isReachEndEvent = false;

        while (!isReachEndEvent) {

            List<Connection> connectionList = null;
            // start node

            if (currentActionNode == null) {
                connectionList = startNode.getDefaultOutgoingConnections();
            } else {
                connectionList = currentActionNode.getDefaultOutgoingConnections();
            }

            if (connectionList == null) {
                isReachEndEvent = true;
            } else {

                for (Connection outConnection : connectionList) {
                    Node node = outConnection.getTo();
                    System.out.println("connection: " + outConnection.getToType());
                    if (node instanceof EndNode) {
                        isReachEndEvent = true;
                        System.out.println("endnote: " + node.getName());
//                        Place endPlace = createPlace(((EndNode) node).getUniqueId());
//                        placeList.add(endPlace);
                    } else {

                        currentActionNode = (ActionNode) node;

                        MLComponent mLComponent = MLComponentConfiguration.getComponentByID(currentActionNode.getName(),loadedListOfMLComponents);
                        listOfMLComponents.add(mLComponent);
                        System.out.println("actionNode: " + currentActionNode.getName());
                    }
                }

            }

        }

        generateFilteredClassiferWeka(listOfMLComponents);

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

    private void generateFilteredClassiferWeka(ArrayList<MLComponent> orderedPipelineComponents) {
        String pipeline = orderedPipelineComponents.get(0).getComponentExecutionScriptFilteredClassifierWeka()
                + orderedPipelineComponents.get(1).getComponentExecutionScriptFilteredClassifierWeka()
                + orderedPipelineComponents.get(2).getComponentExecutionScriptFilteredClassifierWeka()
                + orderedPipelineComponents.get(3).getComponentExecutionScriptFilteredClassifierWeka()
                + orderedPipelineComponents.get(4).getComponentExecutionScriptFilteredClassifierWeka()
                + "\" "
                + orderedPipelineComponents.get(5).getComponentExecutionScriptFilteredClassifierWeka();

        String outputFileName = "output_model";
        String fullCommand = prepareFullCommand(outputFileName, pipeline);
        String outputPipelineFilePath = randomPipelinePath + bpmnModel.getName() + ".txt";

        IOUtils iou = new IOUtils();
        iou.overWriteData(fullCommand, outputPipelineFilePath);
    }

    private String getBetweenStrings(
            String text,
            String textFrom,
            String textTo) {

        String result = "";

        // Cut the beginning of the text to not occasionally meet a      
        // 'textTo' value in it:
        result
                = text.substring(
                        text.indexOf(textFrom) + textFrom.length(),
                        text.length());

        // Cut the excessive ending of the text:
        result
                = result.substring(
                        0,
                        result.indexOf(textTo));

        return result;
    }

    private void fromArffToCSV(String sourceArff, String outputCSV) {

        ArffLoader loader = new ArffLoader();
        try {
            loader.setSource(new File(sourceArff));
            Instances data = loader.getDataSet();

            CSVSaver saver = new CSVSaver();
            saver.setInstances(data);

            saver.setFile(new File(outputCSV));
            saver.writeBatch();
        } catch (IOException ex) {
            Logger.getLogger(SurrogatePipelineMapping.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
