/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uts.aai.avatar.service;

import antlr.StringUtils;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.bind.JAXBException;
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
import uts.aai.avatar.configuration.MLComponentConfiguration;
import uts.aai.avatar.model.MLComponent;
import uts.aai.avatar.model.MLComponentIO;
import uts.aai.avatar.model.pipeline.MLPipeline;
import uts.aai.avatar.model.pipeline.MLPipelineConnection;
import uts.aai.avatar.model.pipeline.MLPipelineStructure;
import uts.aai.avatar.service.DatasetMetaFeatures;
import uts.aai.pn.model.Arc;
import uts.aai.pn.model.MetaFeatureEvaluationTransitionFunction;
import uts.aai.pn.model.Parameter;
import uts.aai.pn.model.PetriNetsPipeline;
import uts.aai.pn.model.Place;
import uts.aai.pn.model.Token;
import uts.aai.pn.model.Transition;
import uts.aai.utils.IOUtils;
import uts.aai.utils.JSONUtils;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.core.converters.CSVSaver;

/**
 *
 * @author ntdun
 */
public class SurrogatePipelineMapping {
    
    private List<MLComponent> loadedListOfMLComponents;

    public SurrogatePipelineMapping(List<MLComponent> loadedListOfMLComponents) {
        this.loadedListOfMLComponents = loadedListOfMLComponents;
    }

    
    

    public PetriNetsPipeline mappingFromBPMN2PetriNetsPipelineFromBPMNString(String bpmnPipeline) {

        PetriNetsPipeline pipeline = null;
        try {
            List<Place> placeList = new ArrayList<>();
            List<Transition> transitionList = new ArrayList<>();
            List<Arc> arcList = new ArrayList<>();

            System.out.println("CHECK B1");
            Resource resource = new ByteArrayResource(bpmnPipeline.getBytes());
            System.out.println("CHECK B2");
            KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
            kbuilder.add(resource, ResourceType.BPMN2);

            System.out.println("CHECK B3");

            KnowledgeBase kbase = kbuilder.newKnowledgeBase();

            System.out.println("CHECK B4");

            RuleFlowProcess process = (RuleFlowProcess) kbase.getProcess("ml.process");

            System.out.println("CHECK B5");

            StartNode startNode = process.getStart();
            System.out.println("start node " + startNode.getName());

            Place startPlace = createPlace(startNode.getUniqueId());
            placeList.add(startPlace);
            boolean isNodeAfterStart = true;

            ActionNode currentActionNode = null;
            Place currentPlace = null;
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

                            // for Arc1
                            String placeId1 = "";

                            if (currentActionNode == null) {
                                placeId1 = startNode.getUniqueId();
                            } else {
                                placeId1 = currentPlace.getId();
                            }

                            currentActionNode = (ActionNode) node;

                            //get token
                            if (isNodeAfterStart) {

                                String script = currentActionNode.getAction().toString();

                                String dataPath = getDataPathFromScript(script);
                                System.out.println(dataPath);
                                Token token = createToken(dataPath);
                                startPlace.setToken(token);

                                isNodeAfterStart = false;
                            }

                            Transition currentTransition = createTransition(currentActionNode.getName(), currentActionNode.getName());
                            transitionList.add(currentTransition);

                            String arcId1 = placeId1 + "_" + currentTransition.getId();
                            Arc arc1 = new Arc(arcId1, placeId1, currentTransition.getId(), true);
                            arcList.add(arc1);

                            // middle Place
                            Place middlePlace = createPlace("mid-" + currentActionNode.getName());
                            placeList.add(middlePlace);

                            // add Arc 2
                            String arcId2 = currentTransition.getId() + "_" + middlePlace.getId();
                            Arc arc2 = new Arc(arcId2, middlePlace.getId(), currentTransition.getId(), false);
                            arcList.add(arc2);
                            currentPlace = middlePlace;
                            System.out.println("actionNode: " + currentActionNode.getName());
                        }
                    }

                }

            }

            for (Place pl : placeList) {
                System.out.println("place: " + pl.getId());

            }

            for (Arc pl : arcList) {
                System.out.println("arc: " + pl.getId());

            }

            for (Transition pl : transitionList) {
                System.out.println("trans: " + pl.getId());

            }

            pipeline = new PetriNetsPipeline(process.getId(), placeList, arcList, transitionList);

            //System.out.println("script: " + node.getAction().toString());
        } catch (Exception e) {
            System.out.println(e);
        }
        return pipeline;
    }

    public PetriNetsPipeline mappingFromBPMN2PetriNetsPipeline(File bpmnModel) {

        List<Place> placeList = new ArrayList<>();
        List<Transition> transitionList = new ArrayList<>();
        List<Arc> arcList = new ArrayList<>();

        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();

        kbuilder.add(ResourceFactory.newFileResource(bpmnModel), ResourceType.BPMN2);

        KnowledgeBase kbase = kbuilder.newKnowledgeBase();

        RuleFlowProcess process = (RuleFlowProcess) kbase.getProcess("ml.process");

        StartNode startNode = process.getStart();
        System.out.println("start node " + startNode.getName());

        Place startPlace = createPlace(startNode.getUniqueId());
        placeList.add(startPlace);
        boolean isNodeAfterStart = true;

        ActionNode currentActionNode = null;
        Place currentPlace = null;
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

                        // for Arc1
                        String placeId1 = "";

                        if (currentActionNode == null) {
                            placeId1 = startNode.getUniqueId();
                        } else {
                            placeId1 = currentPlace.getId();
                        }

                        currentActionNode = (ActionNode) node;

                        //get token
                        if (isNodeAfterStart) {

                            String script = currentActionNode.getAction().toString();

                            String dataPath = getDataPathFromScript(script);
                            System.out.println(dataPath);
                            Token token = createToken(dataPath);
                            startPlace.setToken(token);

                            isNodeAfterStart = false;
                        }

                        Transition currentTransition = createTransition(currentActionNode.getName(), currentActionNode.getName());
                        transitionList.add(currentTransition);

                        String arcId1 = placeId1 + "_" + currentTransition.getId();
                        Arc arc1 = new Arc(arcId1, placeId1, currentTransition.getId(), true);
                        arcList.add(arc1);

                        // middle Place
                        Place middlePlace = createPlace("mid-" + currentActionNode.getName());
                        placeList.add(middlePlace);

                        // add Arc 2
                        String arcId2 = currentTransition.getId() + "_" + middlePlace.getId();
                        Arc arc2 = new Arc(arcId2, middlePlace.getId(), currentTransition.getId(), false);
                        arcList.add(arc2);
                        currentPlace = middlePlace;
                        System.out.println("actionNode: " + currentActionNode.getName());
                    }
                }

            }

        }

        for (Place pl : placeList) {
            System.out.println("place: " + pl.getId());

        }

        for (Arc pl : arcList) {
            System.out.println("arc: " + pl.getId());

        }

        for (Transition pl : transitionList) {
            System.out.println("trans: " + pl.getId());

        }

        PetriNetsPipeline pipeline = new PetriNetsPipeline(process.getId(), placeList, arcList, transitionList);

        //System.out.println("script: " + node.getAction().toString());
        return pipeline;
    }

    public PetriNetsPipeline mappingFromWekaPipeline2PetriNetsPipeline(ArrayList<String> listOfComponents, String dataPath) {

        
        //MLComponentConfiguration.initDefault();
        //ArrayList<String> listOfComponents = filterMLComponents(wrapperArgs);

        List<Place> placeList = new ArrayList<>();
        List<Transition> transitionList = new ArrayList<>();
        List<Arc> arcList = new ArrayList<>();

        Token token = createToken(dataPath);

        Place startPlace = createPlace(randUUID());
        placeList.add(startPlace);
        startPlace.setToken(token);

        Place currentPlace = null;

        for (int i = 0; i < listOfComponents.size(); i++) {

            // for Arc1
            String placeId1 = "";

            if (i == 0) {
                placeId1 = startPlace.getId();

            } else {
                placeId1 = currentPlace.getId();
            }

            Transition currentTransition = createTransition(listOfComponents.get(i), listOfComponents.get(i));
            transitionList.add(currentTransition);

            String arcId1 = placeId1 + "_" + currentTransition.getId();
            Arc arc1 = new Arc(arcId1, placeId1, currentTransition.getId(), true);
            arcList.add(arc1);

            // middle Place
            Place middlePlace = createPlace("mid-" + listOfComponents.get(i));
            placeList.add(middlePlace);

            // add Arc 2
            String arcId2 = currentTransition.getId() + "_" + middlePlace.getId();
            Arc arc2 = new Arc(arcId2, middlePlace.getId(), currentTransition.getId(), false);
            arcList.add(arc2);
            currentPlace = middlePlace;
            System.out.println("actionNode: " + listOfComponents.get(i));
        }

        for (Place pl : placeList) {
            System.out.println("place: " + pl.getId());
        }

        for (Arc pl : arcList) {
            System.out.println("arc: " + pl.getId());
        }

        for (Transition pl : transitionList) {
            System.out.println("trans: " + pl.getId());
        }

        PetriNetsPipeline pipeline = new PetriNetsPipeline(randUUID(), placeList, arcList, transitionList);

        return pipeline;
    }

    
    
     

    private String getDataPathFromScript(String script) {

        String dataPath = getBetweenStrings(script, "kcontext.setVariable(\"input-data-set-1\",\"", ".arff\");");
        dataPath.replaceAll("\"", "");
        String sourceArff = dataPath + ".arff";
        //String targetCSV = dataPath + ".csv";

        //fromArffToCSV(sourceArff, targetCSV);
        return sourceArff;
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

    private Place createPlace(String placeId) {

        Place place = new Place();

        place.setId(placeId);
        place.setToken(null);

        return place;
    }

    private Place createStartPlace(String placeId, Token token) {

        Place place = new Place();

        place.setId(placeId);
        place.setToken(token);

        return place;
    }

    private Token createToken(String filePath) {

        String tokenPath = filePath + ".token";
        System.out.println("token path: " + tokenPath);
        IOUtils iou = new IOUtils();

        File tempFile = new File(tokenPath);
        Token token = null;
        if (tempFile.exists()) {

            String tokenJson = iou.readData(tokenPath);
            try {
                token = JSONUtils.unmarshal(tokenJson, Token.class);
            } catch (JAXBException ex) {
                Logger.getLogger(SurrogatePipelineMapping.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            List<Parameter> parameterList = new ArrayList<>();
            DatasetMetaFeatures datasetMetaFeatures = new DatasetMetaFeatures(filePath);

            List<MLComponentIO> listOfMetaFeatures = datasetMetaFeatures.analyseMetaFeaturesArff();
            System.out.println("");
            for (MLComponentIO mLComponentIO : listOfMetaFeatures) {
                System.out.println(mLComponentIO.getmLComponentCapability() + " : " + mLComponentIO.getValue());
                Parameter parameter = new Parameter(mLComponentIO.getmLComponentCapability().name(), mLComponentIO.getValue());
                parameterList.add(parameter);

            }

            token = new Token("metatoken", parameterList);

            try {
                String tokenjson = JSONUtils.marshal(token, Token.class);
                iou.overWriteData(tokenjson, tokenPath);
            } catch (JAXBException ex) {
                Logger.getLogger(SurrogatePipelineMapping.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return token;
    }

    private Transition createTransition(String id, String name) {
        Transition transition = new Transition();

        transition.setId(id);
        transition.setName(name);
        transition.setTransitionFunction(new MetaFeatureEvaluationTransitionFunction());

        return transition;
    }

    private void fromArffToCSV(String sourceArff, String outputCSV) {

        File tempFile = new File(outputCSV);

        if (!tempFile.exists()) {

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

    private MLPipelineConnection getFirstComponent(ArrayList<MLPipelineConnection> listOfConnections) {
        for (MLPipelineConnection mLPipelineConnection : listOfConnections) {
            if (mLPipelineConnection.getSource() == null) {
                return mLPipelineConnection;
            }
        }
        return null;
    }

    private MLPipelineConnection getLastComponent(ArrayList<MLPipelineConnection> listOfConnections) {
        for (MLPipelineConnection mLPipelineConnection : listOfConnections) {
            if (mLPipelineConnection.getTarget() == null) {
                return mLPipelineConnection;
            }
        }
        return null;
    }

    private MLPipelineConnection getNextComponent(String sourceId, ArrayList<MLPipelineConnection> listOfConnections) {
        for (MLPipelineConnection mLPipelineConnection : listOfConnections) {
            if (mLPipelineConnection.getTarget() == null && mLPipelineConnection.getSource().equals(sourceId)) {
                return mLPipelineConnection;
            }
        }
        return null;
    }

    public PetriNetsPipeline mappingFromEvolMLPipeline2PetriNetsPipeline(MLPipeline mLPipeline) {

        List<Place> placeList = new ArrayList<>();
        List<Transition> transitionList = new ArrayList<>();
        List<Arc> arcList = new ArrayList<>();

        MLPipelineStructure mLPipelineStructure = mLPipeline.getmLPipelineStructure();

        ArrayList<MLPipelineConnection> listOfLPipelineConnections = mLPipelineStructure.getListOfConnections();

//        
//        
//        
//        Place startPlace = createPlace(startNode.getUniqueId());
//        placeList.add(startPlace);
//        boolean isNodeAfterStart = true;
//
//        ActionNode currentActionNode = null;
//        Place currentPlace = null;
//        boolean isReachEndEvent = false;
//
//        while (!isReachEndEvent) {
//
//            List<Connection> connectionList = null;
//            // start node
//
//            if (currentActionNode == null) {
//                connectionList = startNode.getDefaultOutgoingConnections();
//            } else {
//                connectionList = currentActionNode.getDefaultOutgoingConnections();
//            }
//
//            if (connectionList == null) {
//                isReachEndEvent = true;
//            } else {
//
//                for (Connection outConnection : connectionList) {
//                    Node node = outConnection.getTo();
//                    System.out.println("connection: " + outConnection.getToType());
//                    if (node instanceof EndNode) {
//                        isReachEndEvent = true;
//                        System.out.println("endnote: " + node.getName());
////                        Place endPlace = createPlace(((EndNode) node).getUniqueId());
////                        placeList.add(endPlace);
//                    } else {
//
//                        // for Arc1
//                        String placeId1 = "";
//
//                        if (currentActionNode == null) {
//                            placeId1 = startNode.getUniqueId();
//                        } else {
//                            placeId1 = currentPlace.getId();
//                        }
//
//                        currentActionNode = (ActionNode) node;
//
//                        //get token
//                        if (isNodeAfterStart) {
//
//                            String script = currentActionNode.getAction().toString();
//
//                            String dataPath = getDataPathFromScript(script);
//                            System.out.println(dataPath);
//                            Token token = createToken(dataPath);
//                            startPlace.setToken(token);
//
//                            isNodeAfterStart = false;
//                        }
//
//                        Transition currentTransition = createTransition(currentActionNode.getName(), currentActionNode.getName());
//                        transitionList.add(currentTransition);
//
//                        String arcId1 = placeId1 + "_" + currentTransition.getId();
//                        Arc arc1 = new Arc(arcId1, placeId1, currentTransition.getId(), true);
//                        arcList.add(arc1);
//
//                        // middle Place
//                        Place middlePlace = createPlace("mid-" + currentActionNode.getName());
//                        placeList.add(middlePlace);
//
//                        // add Arc 2
//                        String arcId2 = currentTransition.getId() + "_" + middlePlace.getId();
//                        Arc arc2 = new Arc(arcId2, middlePlace.getId(), currentTransition.getId(), false);
//                        arcList.add(arc2);
//                        currentPlace = middlePlace;
//                        System.out.println("actionNode: " + currentActionNode.getName());
//                    }
//                }
//
//            }
//
//        }
//
//        for (Place pl : placeList) {
//            System.out.println("place: " + pl.getId());
//
//        }
//
//        for (Arc pl : arcList) {
//            System.out.println("arc: " + pl.getId());
//
//        }
//
//        for (Transition pl : transitionList) {
//            System.out.println("trans: " + pl.getId());
//
//        }
//
//        PetriNetsPipeline pipeline = new PetriNetsPipeline(process.getId(), placeList, arcList, transitionList);
        //System.out.println("script: " + node.getAction().toString());
//        return pipeline;
        return null;
    }

    public PetriNetsPipeline mappingFromListOfComponents(ArrayList<String> lisfOfSMACArgs, String dataPath) {

        List<Place> placeList = new ArrayList<>();
        List<Transition> transitionList = new ArrayList<>();
        List<Arc> arcList = new ArrayList<>();

        Place startPlace = createPlace(randUUID());
        placeList.add(startPlace);
        Token token = createToken(dataPath);
        startPlace.setToken(token);

        Place previousPlace = startPlace;

        for (String smacArg : lisfOfSMACArgs) {

            Transition currentTransition = createTransition(randUUID(), smacArg);
            transitionList.add(currentTransition);

            String arcId1 = previousPlace.getId() + "_" + currentTransition.getId();
            Arc arc1 = new Arc(arcId1, previousPlace.getId(), currentTransition.getId(), true);
            arcList.add(arc1);

            // middle Place
            Place middlePlace = createPlace("mid-" + randUUID());
            placeList.add(middlePlace);

            // add Arc 2
            String arcId2 = currentTransition.getId() + "_" + middlePlace.getId();
            Arc arc2 = new Arc(arcId2, middlePlace.getId(), currentTransition.getId(), false);
            arcList.add(arc2);
            previousPlace = middlePlace;

        }

        for (Place pl : placeList) {
            System.out.println("place: " + pl.getId());

        }

        for (Arc pl : arcList) {
            System.out.println("arc: " + pl.getId());

        }

        for (Transition pl : transitionList) {
            System.out.println("trans: " + pl.getId());

        }

        PetriNetsPipeline pipeline = new PetriNetsPipeline(randUUID(), placeList, arcList, transitionList);

        return pipeline;
    }

    private String randUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

}
