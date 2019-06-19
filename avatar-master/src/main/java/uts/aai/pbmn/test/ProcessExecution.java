package uts.aai.pbmn.test;



import java.util.HashMap;

import org.drools.KnowledgeBase;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import uts.aai.mf.configuration.MLComponentConfiguration;

public class ProcessExecution {

	public static final void main(String[] args) {
            
            
                MLComponentConfiguration.initConfiguration();
            
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		kbuilder.add(ResourceFactory.newClassPathResource("MROST_480.bpmn"), ResourceType.BPMN2);
                
                //kbuilder.add(ResourceFactory.newClassPathResource("demo.bpmn"), ResourceType.BPMN2);
                
		KnowledgeBase kbase = kbuilder.newKnowledgeBase();
		StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
		
		HashMap<String, Object> params = new HashMap<String, Object>();
		//params.put("name", "Francesco");
		
                long startTime = System.currentTimeMillis();
                
		ksession.startProcess("ml.process",params);
		ksession.dispose();
                
                long endTime = System.currentTimeMillis();
                
                System.out.println("BPMN Pipeline Execution Time: " + (endTime-startTime) + " ms");
                
              
	}
}