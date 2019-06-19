/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uts.aai.pbmn.test;

import java.io.File;
import uts.aai.mf.configuration.MLComponentConfiguration;
import uts.aai.pbmn.SurrogatePipelineMapping;
import uts.aai.pbmn.WekaFilterMappingFromBPMN;
import uts.aai.pn.engine.PetriNetsExecutionEngine;
import uts.aai.pn.model.PetriNetsPipeline;
import uts.aai.pn.utils.IOUtils;

/**
 *
 * @author ntdun
 */
public class AutoGenerateNativeWekaCommand {
     public void executeFile(File file) {

        IOUtils iou = new IOUtils();
        String fileName = file.getName();
        String outputLog = fileName + ",";
        iou.writeData(outputLog, "C:\\DATA\\Projects\\eclipse-workspace\\aai_aw\\weka-3-7-7\\data\\testing\\dataset_1_pn_weka.txt");

        long startTime = System.currentTimeMillis();
        
         WekaFilterMappingFromBPMN wfmfbpmn = new WekaFilterMappingFromBPMN();
         wfmfbpmn.mappingFromBPMN2NativeWekaCommand(file);
       
        
    
        long endTime = System.currentTimeMillis();
                
        System.out.println("Mapping Pipeline To Weka Command Time: " + (endTime-startTime) + " ms");
         
         
         
  

        outputLog = String.valueOf(endTime - startTime + "\n");
        

        iou.writeData(outputLog, "C:\\DATA\\Projects\\eclipse-workspace\\aai_aw\\weka-3-7-7\\data\\testing\\dataset_1_pn_weka.txt");

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
