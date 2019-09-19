/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uts.aai.avatar.model.pipeline;

/**
 *
 * @author ntdun
 */
public class MLComponentHyperparameter {
    private String configureString;
    private String configureValue;
    private MLPipeline subPipeline;

    public MLComponentHyperparameter() {
    }

    public MLComponentHyperparameter(String configureString, String configureValue) {
        this.configureString = configureString;
        this.configureValue = configureValue;
    }

    public String getConfigureString() {
        return configureString;
    }

    public void setConfigureString(String configureString) {
        this.configureString = configureString;
    }

    public String getConfigureValue() {
        return configureValue;
    }

    public void setConfigureValue(String configureValue) {
        this.configureValue = configureValue;
    }

    public MLPipeline getSubPipeline() {
        return subPipeline;
    }

    public void setSubPipeline(MLPipeline subPipeline) {
        this.subPipeline = subPipeline;
    }
    
    
}
