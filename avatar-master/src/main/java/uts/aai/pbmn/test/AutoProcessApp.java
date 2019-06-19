/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uts.aai.pbmn.test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.drools.KnowledgeBase;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import uts.aai.mf.configuration.MLComponentConfiguration;
import uts.aai.pn.utils.IOUtils;

/**
 *
 * @author ntdun
 */
public class AutoProcessApp {

    public void executeFile(File file) {

        IOUtils iou = new IOUtils();
        File directory = new File("C:\\DATA\\Projects\\eclipse-workspace\\aai_aw\\weka-3-7-7\\data\\testing\\output");
        try {
            FileUtils.cleanDirectory(directory);
        } catch (IOException ex) {
            Logger.getLogger(AutoProcessTestApp.class.getName()).log(Level.SEVERE, null, ex);
        }

//        String pipelineFilePath = "C:\\DATA\\Projects\\eclipse-workspace\\aai_aw\\weka-3-7-7\\combination\\dataset_1\\MORST_144.bpmn";
//
//        File file = new File(pipelineFilePath);
        String fileName = file.getName();
        String outputLog = fileName + ",";
        iou.writeData(outputLog, "C:\\DATA\\Projects\\eclipse-workspace\\aai_aw\\weka-3-7-7\\data\\testing\\dataset_1_validation.txt");

        long startTime = System.currentTimeMillis();

        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        kbuilder.add(ResourceFactory.newFileResource(file), ResourceType.BPMN2);

        //kbuilder.add(ResourceFactory.newClassPathResource("demo.bpmn"), ResourceType.BPMN2);
        KnowledgeBase kbase = kbuilder.newKnowledgeBase();
        StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();

        HashMap<String, Object> params = new HashMap<String, Object>();
        //params.put("name", "Francesco");

        ksession.startProcess("ml.process", params);
        ksession.dispose();

        long endTime = System.currentTimeMillis();

        System.out.println("BPMN Pipeline Execution Time: " + (endTime - startTime) + " ms");

        outputLog = String.valueOf(endTime - startTime + "\n");

        iou.writeData(outputLog, "C:\\DATA\\Projects\\eclipse-workspace\\aai_aw\\weka-3-7-7\\data\\testing\\dataset_1_validation.txt");

    }

    public void readAllFiles(String folderName) {
        
        MLComponentConfiguration.initConfiguration();

        File folder = new File(folderName);
        for (final File fileEntry : folder.listFiles()) {
            if (!fileEntry.isDirectory() && fileEntry.getName().contains(".bpmn")) {
                System.out.println(fileEntry.getName());
                executeFile(fileEntry);
            }
        }
    }

}
