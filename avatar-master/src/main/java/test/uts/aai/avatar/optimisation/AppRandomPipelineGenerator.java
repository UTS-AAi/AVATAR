/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.uts.aai.avatar.optimisation;

import uts.aai.avatar.optimisation.RandomPipelineGenerator;
import uts.aai.pn.utils.IOUtils;

/**
 *
 * @author ntdun
 */
public class AppRandomPipelineGenerator {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        String datasetPath = "C:/experiments/datasets/arff/abalone_train.arff";
        String outputFolder= "C:/experiments/tools/avatar/output/";
        
        
        RandomPipelineGenerator generator = new RandomPipelineGenerator(datasetPath,outputFolder);
        //String bpmnPipeline = generator.generateBPMNTemplate(0);
        String bpmnPipeline = generator.generateBPMNPipelineWithRandomComponents(5);
        
        System.out.println("\n\n\n" + bpmnPipeline);
        
        IOUtils iou = new IOUtils();
        iou.overWriteData(bpmnPipeline, "C:/experiments/tools/avatar/output/bpmntemplate.bpmn");
    }
    
}
