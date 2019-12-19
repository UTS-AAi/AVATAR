/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uts.aai.pn.model;

import java.util.ArrayList;
import java.util.List;
import uts.aai.avatar.model.MLMetafeature;
import uts.aai.avatar.model.MetafeatureType;
import uts.aai.avatar.model.MetafeatureTypeConfig;

import uts.aai.avatar.configuration.MLComponentConfiguration;
import uts.aai.avatar.model.MLComponent;

/**
 *
 * @author ntdun
 */
public class MetaFeatureEvaluationTransitionFunction implements TransitionFunction {

    
    
    

    @Override
    public Token fire(Token token) {

        
        for (Parameter p : token.getParameterList()) {
            p.setParamValue(p.getParamValue() + 1);
        }

        return token;
    }

    @Override
    public Token fireAlg(Token token, String algorithm, List<MLComponent> loadedListOfMLComponents) {
        
        

        AlgorithmConfiguration algorithmConfiguration = MLComponentConfiguration.getAlgorithmConfiguration(algorithm,loadedListOfMLComponents);
        
     

        if (token.getParameterList() == null) {
            System.out.println("NULL HERE: token.getParameterList()");
        }

           
        if (algorithmConfiguration.getCapabilityList() == null) {
            System.out.println("NULL HERE: algorithmConfiguration.getCapabilityList()");
        }

       
        if (checkComponentValidity(token.getParameterList(), algorithmConfiguration.getCapabilityList())) {
           
            List<Parameter> outputParameterList = calculateOutputToken(token.getParameterList(), algorithmConfiguration.getEffectList());
         
           
            token.setParameterList(outputParameterList);
        } else {
            token = null;
        }

      
        return token;
    }

    private boolean checkComponentValidity(List<Parameter> metaFeatureList, List<Parameter> capabilityList) {

        for (Parameter metaFeature : metaFeatureList) {

            Parameter capability = getParameter(metaFeature.getParamName(), capabilityList);
            if (metaFeature.getParamValue() == 1 && (capability == null || capability.getParamValue() == 0)) {
                return false;
            }
        }

        return true;
    }

    private List<Parameter> calculateOutputToken(List<Parameter> metaFeatureList, List<Parameter> effectList) {

        List<Parameter> outputParameterList = new ArrayList<>();

        for (Parameter metaFeature : metaFeatureList) {
            if (!isContain(metaFeature.getParamName(), outputParameterList)) {
                Parameter param = new Parameter(metaFeature.getParamName(), 0);
                outputParameterList.add(param);
            }
        }

        for (Parameter effect : effectList) {
            if (!isContain(effect.getParamName(), outputParameterList)) {
                Parameter param = new Parameter(effect.getParamName(), 0);
                outputParameterList.add(param);
            }
        }

        for (Parameter outputParam : outputParameterList) {

            Parameter metaFeature = getParameter(outputParam.getParamName(), metaFeatureList);
            Parameter effect = getParameter(outputParam.getParamName(), effectList);
            Integer inputValue = 0;
            Integer effectValue = 0;
            if (metaFeature!=null) {
                inputValue = metaFeature.getParamValue();
            }
            
            if (effect!=null) {
                effectValue = effect.getParamValue();
            }
            
            
            Integer changedValue = inputValue + effectValue;
            
            if (changedValue<0) {
                changedValue = 0;
            } else if (changedValue>1) {
                changedValue = 1;
            }
            
            outputParam.setParamValue(changedValue);
            
//            MLMetafeature mLMetafeature = MLMetafeature.valueOf(outputParam.getParamName());
//
//            if (MetafeatureTypeConfig.getMetafeatureType(mLMetafeature).equals(MetafeatureType.QUALITY) ) {
//                System.out.println("YES - quality");
//                if (metaFeature != null && metaFeature.getParamValue() == 1 && (effect == null || effect.getParamValue() == 0)) {
//                    outputParam.setParamValue(1);
//                }
//
//            } else if (MetafeatureTypeConfig.getMetafeatureType(mLMetafeature).equals(MetafeatureType.ATTRIBUTE_TRANSFORMATION)) {
//                if ((metaFeature == null || metaFeature.getParamValue() == 0) && (effect == null || effect.getParamValue() == 0)) {
//                    outputParam.setParamValue(0);
//                } else {
//                    outputParam.setParamValue(1);
//                }
//                
//                if (effect!=null && effect.getParamValue()==1) {
//                    
//                    for (Parameter local : outputParameterList) {
//                        if (!local.getParamName().equals(outputParam.getParamName()) 
//                                && MetafeatureTypeConfig.getMetafeatureType(MLMetafeature.valueOf(outputParam.getParamName())).equals(MetafeatureType.ATTRIBUTE_TRANSFORMATION)) {
//                            local.setParamValue(0);
//                        }
//                    }
//                }
//
//            } else if (MetafeatureTypeConfig.getMetafeatureType(mLMetafeature).equals(MetafeatureType.CLASS_TRANSFORMATION)) {
//                if ((metaFeature == null || metaFeature.getParamValue() == 0) && (effect == null || effect.getParamValue() == 0)) {
//                    outputParam.setParamValue(0);
//                } else {
//                    outputParam.setParamValue(1);
//                }
//                
//                if (effect!=null && effect.getParamValue()==1) {
//                    
//                    for (Parameter local : outputParameterList) {
//                        if (!local.getParamName().equals(outputParam.getParamName()) 
//                                && MetafeatureTypeConfig.getMetafeatureType(MLMetafeature.valueOf(outputParam.getParamName())).equals(MetafeatureType.CLASS_TRANSFORMATION)) {
//                            local.setParamValue(0);
//                        }
//                    }
//                }
//
//            }
            

        }

        return outputParameterList;

    }

    private boolean isContain(String paramName, List<Parameter> parameterList) {

        for (Parameter parameter : parameterList) {
            if (parameter.getParamName().equals(paramName)) {
                return true;
            }
        }
        return false;
    }

    private Parameter getParameter(String parameterName, List<Parameter> parameterList) {
        for (Parameter parameter : parameterList) {
            if (parameter.getParamName().equals(parameterName)) {
                return parameter;
            }
        }
        return null;
    }

}
