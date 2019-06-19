/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uts.aai.pbmn.test;

import java.io.File;
import uts.aai.mf.configuration.MLComponentConfiguration;
import uts.aai.pbmn.SurrogatePipelineMapping;
import uts.aai.pn.engine.PetriNetsExecutionEngine;
import uts.aai.pn.model.PetriNetsPipeline;
import uts.aai.pn.utils.IOUtils;

/**
 *
 * @author ntdun
 */
public class MappingApp {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        String bpmnModel = "C:\\DATA\\Projects\\eclipse-workspace\\aai_aw\\weka-3-7-7\\combination\\dataset_1\\MORST_100.bpmn";
        String surrogateModelOuput = "C:\\DATA\\Projects\\eclipse-workspace\\aai_aw\\weka-3-7-7\\data\\testing\\output\\pn_1.xml";
        
        MLComponentConfiguration.initConfiguration();
        
       
        SurrogatePipelineMapping spm = new SurrogatePipelineMapping();
        PetriNetsPipeline petriNetsPipeline = spm.mappingFromBPMN2PetriNetsPipeline(new File(bpmnModel));
        //System.out.println("\n" + petriNetsPipeline.toString());
        
        IOUtils iou = new IOUtils();
        iou.overWriteData(petriNetsPipeline.toString(), surrogateModelOuput);
        
        PetriNetsExecutionEngine engine = new PetriNetsExecutionEngine(petriNetsPipeline);
        long startTime = System.currentTimeMillis();
        
        engine.execute();
        
        long endTime = System.currentTimeMillis();
                
        System.out.println("Surrogate Pipeline Validation Time: " + (endTime-startTime) + " ms");
    }
    
}
