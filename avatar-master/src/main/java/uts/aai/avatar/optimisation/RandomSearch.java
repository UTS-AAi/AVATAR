/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uts.aai.avatar.optimisation;

import uts.aai.avatar.service.RandomPipelineGenerator;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBException;
import org.apache.commons.io.FileDeleteStrategy;
import org.apache.commons.io.FileUtils;
import org.drools.KnowledgeBase;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.definition.process.Connection;
import org.drools.definition.process.Node;
import org.drools.io.Resource;
import org.drools.io.ResourceFactory;
import org.drools.io.impl.ByteArrayResource;
import org.drools.runtime.StatefulKnowledgeSession;
import org.jbpm.ruleflow.core.RuleFlowProcess;
import org.jbpm.workflow.core.node.ActionNode;
import org.jbpm.workflow.core.node.EndNode;
import org.jbpm.workflow.core.node.StartNode;
import uts.aai.avatar.model.AllEvaluationResult;
import uts.aai.avatar.model.EvaluationModeType;
import uts.aai.avatar.model.EvaluationModel;
import uts.aai.avatar.model.EvaluationResult;
import uts.aai.avatar.configuration.AppConst;
import uts.aai.avatar.configuration.MLComponentConfiguration;
import uts.aai.avatar.model.MLComponent;
import uts.aai.avatar.model.MLComponentIO;
import uts.aai.avatar.service.DatasetMetaFeatures;
import uts.aai.avatar.service.SurrogatePipelineMapping;
import uts.aai.avatar.service.WekaExecutor;
import uts.aai.avatar.service.PetriNetsExecutionEngine;
import uts.aai.pn.model.Parameter;
import uts.aai.pn.model.PetriNetsPipeline;
import uts.aai.pn.model.Token;
import uts.aai.utils.IOUtils;
import uts.aai.utils.JSONUtils;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.core.converters.CSVSaver;

/**
 *
 * @author ntdun
 */
public class RandomSearch {

    private String datasetPath;
    private long timeBudgetInMinutes;
    private boolean isAvatar;
    private ArrayList<EvaluationResult> evaluationResultList;
    private String outputFolder;
    private int counter;

    public RandomSearch(String datasetPath, long timeBudgetInMinutes, boolean isAvatar, String outputFolder) {
        this.datasetPath = datasetPath;
        this.timeBudgetInMinutes = timeBudgetInMinutes;
        this.isAvatar = isAvatar;
        this.outputFolder = outputFolder;
    }

    private void initRandomSearch() {
        counter = 0;
        evaluationResultList = new ArrayList<>();
        MLComponentConfiguration.initDefault();
    }

    public void start() {

        initRandomSearch();

        long startExperimentTime = System.currentTimeMillis();

        while (!isTimeOut(startExperimentTime)) {

            cleanTemp();
            int numberOfPreprocessingComponent = randInt(0, 5);
            String bpmnPipeline = createRandomPipeline(numberOfPreprocessingComponent);

            List<EvaluationModel> listOfEvaluationModels = new ArrayList<>();

            //if (isAvatar) 
            {

                System.out.println("START AVATAR");

                long startEvaluationTime = System.currentTimeMillis();
                EvaluationModel evaluationModel = evaluateAvatar(bpmnPipeline, numberOfPreprocessingComponent);
                long endEvaluationTime = System.currentTimeMillis();
                long evaluationTime = endEvaluationTime - startEvaluationTime;

                if (evaluationModel == null) {
                    System.out.println("AVATAR EVALs NULL");
                    evaluationModel = new EvaluationModel(EvaluationModeType.PETRI_NET, "",
                            Boolean.FALSE, evaluationTime, null);
                    listOfEvaluationModels.add(evaluationModel);
                } else {
                    evaluationModel.setEvalTime(evaluationTime);
                    listOfEvaluationModels.add(evaluationModel);
                }

                System.out.println("END AVATAR: " + evaluationModel.getValidity());

            }
            //else 

            {

                System.out.println("START BPMN");

                long startEvaluationTime = System.currentTimeMillis();
                EvaluationModel evaluationModel = evaluateBPMNPipeline(bpmnPipeline, numberOfPreprocessingComponent);
                long endEvaluationTime = System.currentTimeMillis();
                long evaluationTime = endEvaluationTime - startEvaluationTime;

                if (evaluationModel == null) {
                    System.out.println("BPMN EVALs NULL");
                    evaluationModel = new EvaluationModel(EvaluationModeType.BPMN, bpmnPipeline,
                            Boolean.FALSE, evaluationTime, null);
                    listOfEvaluationModels.add(evaluationModel);
                } else {
                    evaluationModel.setEvalTime(evaluationTime);
                    listOfEvaluationModels.add(evaluationModel);
                }

                System.out.println("END BPMN: " + evaluationModel.getValidity());

            }

            EvaluationResult evaluationResult = new EvaluationResult(String.valueOf(counter), listOfEvaluationModels);
            evaluationResultList.add(evaluationResult);
            counter++;

        }

    }

    private String getDataPathFromScript(String sourceArff) {

        String targetCSV = sourceArff.replace(".arff", ".csv");

        fromArffToCSV(sourceArff, targetCSV);

        return targetCSV;
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

    public void finalise() {

        AllEvaluationResult allEvaluationResult = new AllEvaluationResult(evaluationResultList);

        try {
            String allResultStr = JSONUtils.marshal(allEvaluationResult, AllEvaluationResult.class);
            IOUtils iou = new IOUtils();
            iou.overWriteData(allResultStr, AppConst.RANDOM_SEARCH_OUTPUT_PATH);

        } catch (JAXBException ex) {
            Logger.getLogger(RandomSearch.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("TOTAL OF EVALUATED MODEL: " + evaluationResultList.size());

    }

    private boolean isTimeOut(long startExperimentTime) {

        long usedTime = System.currentTimeMillis() - startExperimentTime;

        if (usedTime <= timeBudgetInMinutes * 60 * 1000) {
            return false;
        } else {
            return true;
        }
    }

    private String createRandomPipeline(int numberOfPreprocessingComponent) {
//        RandomPipelineGenerator randomPipelineGenerator = new RandomPipelineGenerator(datasetPath,outputFolder);
//        String bpmnPipeline = randomPipelineGenerator.generateBPMNPipeline();

        RandomPipelineGenerator generator = new RandomPipelineGenerator(datasetPath, outputFolder);
        //String bpmnPipeline = generator.generateBPMNTemplate(0);
        String bpmnPipeline = generator.generateBPMNPipelineWithRandomComponents(numberOfPreprocessingComponent);

        return bpmnPipeline;
    }

    private int randInt(int min, int max) {

        int randomNum = ThreadLocalRandom.current().nextInt(min, max + 1);
        return randomNum;
    }

    private EvaluationModel evaluateBPMNPipeline(String bpmnPipeline, int numberOfPreprocessingComponent) {

        EvaluationModel evaluationModel = null;
        try {

            IOUtils iou = new IOUtils();
            Resource resource = new ByteArrayResource(bpmnPipeline.getBytes());

            KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
            kbuilder.add(resource, ResourceType.BPMN2);

            KnowledgeBase kbase = kbuilder.newKnowledgeBase();
            StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();

            HashMap<String, Object> params = new HashMap<String, Object>();

            ksession.startProcess("ml.process", params);
            ksession.dispose();
            String accuracyStr = iou.readData(AppConst.TEMP_EVAL_RESULT_OUTPUT_PATH);
            accuracyStr = accuracyStr.trim();
            accuracyStr = accuracyStr.replaceAll("\n", "");
            System.out.println("accracy string: -" + accuracyStr + "-");

            if (accuracyStr.equals("false")) {
                evaluationModel = new EvaluationModel(EvaluationModeType.BPMN, bpmnPipeline, Boolean.FALSE, null, null);
            } else if (accuracyStr.equals("true")) {
                evaluationModel = new EvaluationModel(EvaluationModeType.BPMN, bpmnPipeline, Boolean.TRUE, null, null);
            } else if (accuracyStr.equals("timeout")) {
                evaluationModel = new EvaluationModel(EvaluationModeType.BPMN, "timeout", Boolean.TRUE, null, null);
            }

            System.out.println("bpmn evaluationResult: " + evaluationModel.getValidity());

        } catch (Exception e) {
        }

        return evaluationModel;

    }

    private EvaluationModel evaluateAvatar(String bpmnPipeline, int numberOfPreprocessingComponent) {
        EvaluationModel evaluationModel = null;
        try {

            SurrogatePipelineMapping spm = new SurrogatePipelineMapping();
            PetriNetsPipeline petriNetsPipeline = spm.mappingFromBPMN2PetriNetsPipelineFromBPMNString(bpmnPipeline);
            //System.out.println("\n" + petriNetsPipeline.toString());

            PetriNetsExecutionEngine engine = new PetriNetsExecutionEngine(petriNetsPipeline);
            boolean result = engine.execute();

            evaluationModel = new EvaluationModel(EvaluationModeType.PETRI_NET, petriNetsPipeline.toString(), result, null, null);

//            if (result) {
//                evaluationResult = evaluateBPMNPipeline(bpmnPipeline, numberOfPreprocessingComponent);
//                System.out.println("BPMN EVAL: " + evaluationResult.isValidity());
//            }
        } catch (Exception e) {
            System.out.println(e);
        }

        return evaluationModel;
    }

    private void cleanTemp() {

        File directory = new File(AppConst.TEMP_OUTPUT_PATH);
        try {
            FileUtils.cleanDirectory(directory);

        } catch (IOException ex) {
            
        }
    }

    public void mappingFromBPMN2NativeWekaCommand(String bpmnPipeline) {

        ArrayList<MLComponent> listOfMLComponents = new ArrayList<>();

        Resource resource = new ByteArrayResource(bpmnPipeline.getBytes());

        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        kbuilder.add(resource, ResourceType.BPMN2);

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

                        MLComponent mLComponent = MLComponentConfiguration.getComponentByID(currentActionNode.getName());
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
                + "-d " + AppConst.TEMP_EVALUATION_RESULT_PATH + outputFile + ".model "
                + "-F \"weka.filters.MultiFilter "
                + pipeline;

//                + preprocessingConfigurations
//                + "\" "
//                + classifierConfiguration;
        return command;
    }

    private String generateFilteredClassiferWeka(ArrayList<MLComponent> orderedPipelineComponents) {
        String pipeline = orderedPipelineComponents.get(0).getComponentExecutionScriptFilteredClassifierWeka()
                + orderedPipelineComponents.get(1).getComponentExecutionScriptFilteredClassifierWeka()
                + orderedPipelineComponents.get(2).getComponentExecutionScriptFilteredClassifierWeka()
                + orderedPipelineComponents.get(3).getComponentExecutionScriptFilteredClassifierWeka()
                + orderedPipelineComponents.get(4).getComponentExecutionScriptFilteredClassifierWeka()
                + "\" "
                + orderedPipelineComponents.get(5).getComponentExecutionScriptFilteredClassifierWeka();

        String outputFileName = "output_model";
        String fullCommand = prepareFullCommand(outputFileName, pipeline);

        return fullCommand;
    }

}
