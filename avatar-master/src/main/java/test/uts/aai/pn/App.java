/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.uts.aai.pn;

import uts.aai.pn.engine.PetriNetsExecutionEngine;
import uts.aai.pn.model.PetriNetsPipeline;
import uts.aai.pn.utils.IOUtils;

/**
 *
 * @author ntdun
 */
public class App {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        
        SamplePipeline1 spg = new SamplePipeline1();
        PetriNetsPipeline pipeline = spg.sample();
        
        System.out.println("pp" + pipeline.toString());

        IOUtils iou = new IOUtils();
        iou.overWriteData(pipeline.toString(), "C:\\DATA\\Projects\\eclipse-workspace\\aai_aw\\weka-3-7-7\\data\\testing-mapping\\pn_1.xml");
        
        PetriNetsExecutionEngine engine = new PetriNetsExecutionEngine(pipeline);
        engine.execute();
    }

}
