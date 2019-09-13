/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uts.aai.avatar.configuration;

import java.util.ArrayList;
import java.util.List;
import uts.aai.avatar.configuration.MLComponentConfiguration;
import uts.aai.avatar.model.MLComponent;
import uts.aai.avatar.model.MLComponentIO;
import uts.aai.pn.model.AlgorithmConfiguration;
import uts.aai.pn.model.Parameter;

/**
 *
 * @author ntdun
 */
public class Configuration {

//    private static List<AlgorithmConfiguration> algorithmConfigList = new ArrayList<>();
//    
//    public static void init(){
//        
//        {
//            String id = "weka.filters.unsupervised.attribute.ReplaceMissingValues";
//            String name = "ReplaceMissingValues";
//            String path = "weka.filters.unsupervised.attribute.ReplaceMissingValues";
//            
//            List<Parameter> capabilityList = new ArrayList<>();
//            capabilityList.add(new Parameter("MISSING_VALUES", 0));
//            capabilityList.add(new Parameter("NOMINAL_CLASS", 1));
//            capabilityList.add(new Parameter("NUMERIC_CLASS", 1));
//            
//            
//            List<Parameter> effectList = new ArrayList<>();
//            effectList.add(new Parameter("MISSING_VALUES", 1));
//            
//            AlgorithmConfiguration algorithmConfiguration = new AlgorithmConfiguration(id, name, path, capabilityList, effectList);
//            algorithmConfigList.add(algorithmConfiguration);
//            
//        }
//        
//        {
//            String id = "weka.jar weka.classifiers.bayes.NaiveBayes";
//            String name = "NaiveBayes";
//            String path = "weka.jar weka.classifiers.bayes.NaiveBayes";
//            
//            List<Parameter> capabilityList = new ArrayList<>();
//            capabilityList.add(new Parameter("MISSING_VALUES", 0));
//            capabilityList.add(new Parameter("NOMINAL_CLASS", 1));
//            capabilityList.add(new Parameter("NUMERIC_CLASS", 1));
//            
//            
//            List<Parameter> effectList = new ArrayList<>();
//            effectList.add(new Parameter("MISSING_VALUES", 0));
//           
//            
//            
//            AlgorithmConfiguration algorithmConfiguration = new AlgorithmConfiguration(id, name, path, capabilityList, effectList);
//            algorithmConfigList.add(algorithmConfiguration);
//        }
//        
//        
//    }
//    
    public static AlgorithmConfiguration getAlgorithmConfiguration(String algorithm) {

        System.out.println("Finding .. " + algorithm);
       
        MLComponent mLComponent = MLComponentConfiguration.getComponentByID(algorithm);

        if (mLComponent != null) {
            
         
            String id = mLComponent.getComponentId();
            String name = mLComponent.getComponentName();
            String path = mLComponent.getComponentId();

            List<Parameter> capabilityList = new ArrayList<>();

            for (MLComponentIO mLComponentIO : mLComponent.getListOfCapabilities()) {
                Parameter capability = new Parameter(mLComponentIO.getmLComponentCapability().name(), mLComponentIO.getValue());
                capabilityList.add(capability);

            }

            List<Parameter> effectList = new ArrayList<>();
            for (MLComponentIO mLComponentIO : mLComponent.getListOfEffects()) {
                Parameter effect = new Parameter(mLComponentIO.getmLComponentCapability().name(), mLComponentIO.getValue());
                effectList.add(effect);

            }

            AlgorithmConfiguration algorithmConfiguration = new AlgorithmConfiguration(id, name, path, capabilityList, effectList);

            return algorithmConfiguration;

        } else {
            
            return null;
        }
    }

}
