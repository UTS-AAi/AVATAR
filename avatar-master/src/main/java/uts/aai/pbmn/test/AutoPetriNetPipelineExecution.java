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
import uts.aai.pbmn.SurrogatePipelineMapping;
import uts.aai.pn.engine.PetriNetsExecutionEngine;
import uts.aai.pn.model.PetriNetsPipeline;
import uts.aai.pn.utils.IOUtils;

/**
 *
 * @author ntdun
 */
public class AutoPetriNetPipelineExecution {
     public void executeFile(File file) {

        IOUtils iou = new IOUtils();
        String fileName = file.getName();
        String outputLog = fileName + ",";
        iou.writeData(outputLog, "C:\\DATA\\Projects\\eclipse-workspace\\aai_aw\\weka-3-7-7\\data\\testing\\dataset_1_pn_validation.txt");

        long startTime = System.currentTimeMillis();
        
       
        SurrogatePipelineMapping spm = new SurrogatePipelineMapping();
        PetriNetsPipeline petriNetsPipeline = spm.mappingFromBPMN2PetriNetsPipeline(file);
        //System.out.println("\n" + petriNetsPipeline.toString());

        
        PetriNetsExecutionEngine engine = new PetriNetsExecutionEngine(petriNetsPipeline);
        boolean result = engine.execute();
        
        long endTime = System.currentTimeMillis();
                
        System.out.println("Surrogate Pipeline Validation Time: " + (endTime-startTime) + " ms");
         
         
         
  

        outputLog = String.valueOf(result) + "," + String.valueOf(endTime - startTime + "\n");
        

        iou.writeData(outputLog, "C:\\DATA\\Projects\\eclipse-workspace\\aai_aw\\weka-3-7-7\\data\\testing\\dataset_1_pn_validation.txt");

    }

    public void readAllFiles(String folderName) {
        
        //MLComponentConfiguration.initConfiguration();
        MLComponentConfiguration.initDefault();
        
        
        File folder = new File(folderName);
        for (final File fileEntry : folder.listFiles()) {
            if (!fileEntry.isDirectory() && fileEntry.getName().contains(".bpmn")) {
                System.out.println(fileEntry.getName());
                executeFile(fileEntry);
            }
        }
    }
}
