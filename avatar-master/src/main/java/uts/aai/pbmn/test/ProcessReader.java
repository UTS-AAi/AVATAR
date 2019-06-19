/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uts.aai.pbmn.test;

import java.util.HashMap;
import java.util.List;
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
import org.jbpm.workflow.core.node.StartNode;
import uts.aai.pn.model.Place;

/**
 *
 * @author ntdun
 */
public class ProcessReader {
    public static final void main(String[] args) {
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		//kbuilder.add(ResourceFactory.newClassPathResource("demo.bpmn"), ResourceType.BPMN2);
                //kbuilder.add(ResourceFactory.newFileResource("C:\\Users\\ntdun\\Documents\\NetBeansProjects\\PipelineValidation\\src\\main\\resources\\ml_pipeline_3.bpmn"), ResourceType.BPMN2);
                kbuilder.add(ResourceFactory.newFileResource("C:\\Users\\ntdun\\Documents\\NetBeansProjects\\PipelineValidation\\src\\main\\resources\\ml_pipeline_3.bpmn"), ResourceType.BPMN2);
                
                
                
		KnowledgeBase kbase = kbuilder.newKnowledgeBase();
		StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
		
               
                //RuleFlowProcess process = (RuleFlowProcess) kbase.getProcess("greeting.process");
                RuleFlowProcess process = (RuleFlowProcess) kbase.getProcess("ml.process");
                
                StartNode startNode = process.getStart();
                System.out.println("start node " + startNode.getName());
                
                List<Connection> connectionList = startNode.getDefaultOutgoingConnections();
                
                Connection outConnection = connectionList.get(0);
                
                ActionNode node = (ActionNode) outConnection.getTo();
                
                System.out.println("node: " + node.getName());
                
                System.out.println("script: " + node.getAction().toString());
                
                
                
               
	}
    
    
       
    
}
