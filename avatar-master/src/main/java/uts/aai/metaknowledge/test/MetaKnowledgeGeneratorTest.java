/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uts.aai.metaknowledge.test;

import uts.aai.metaknowledge.generator.MetaKnowledgeGenerator;

/**
 *
 * @author ntdun
 */
public class MetaKnowledgeGeneratorTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        String outputPath = "C:\\DATA\\Projects\\eclipse-workspace\\aai_aw\\weka-3-7-7\\data\\testing\\output\\metadata-temp-out.arff";
        String dataFolderName = "C:\\DATA\\Projects\\eclipse-workspace\\aai_aw\\weka-3-7-7\\data\\testing\\synthetic";
        String metaKnowledgeFile = "C:\\DATA\\Projects\\eclipse-workspace\\aai_aw\\weka-3-7-7\\data\\meta_knowledge\\meta_knowledge.json";
        MetaKnowledgeGenerator mkg = new MetaKnowledgeGenerator(outputPath, dataFolderName, metaKnowledgeFile);
        //mkg.run();
        mkg.update("C:\\experiments\\datasets\\arff\\convex_train.arff");
        
    }
    
}
