/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uts.aai.mf.model;

import java.util.ArrayList;

/**
 *
 * @author ntdun
 */

public class MLHyperparameter {
    
    private String command;
    private MLHyperparameterType hyperparameterType;
    
    private ArrayList<String> listOfNomnialValues;
    private String defaultNominalValue;
    
    private Double maxNumericValue;
    private Double minNumericValue;
    private Double defaultNumericValue;
    
    private Integer maxIntValue;
    private Integer minIntValue;
    private Integer defaultIntValue;
    
    private Boolean defaultBoolValue; 

    public MLHyperparameter(String command, MLHyperparameterType hyperparameterType) {
        this.command = command;
        this.hyperparameterType = hyperparameterType;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public MLHyperparameterType getHyperparameterType() {
        return hyperparameterType;
    }

    public void setHyperparameterType(MLHyperparameterType hyperparameterType) {
        this.hyperparameterType = hyperparameterType;
    }

    public ArrayList<String> getListOfNomnialValues() {
        return listOfNomnialValues;
    }

    public void setListOfNomnialValues(ArrayList<String> listOfNomnialValues) {
        this.listOfNomnialValues = listOfNomnialValues;
    }

    public String getDefaultNominalValue() {
        return defaultNominalValue;
    }

    public void setDefaultNominalValue(String defaultNominalValue) {
        this.defaultNominalValue = defaultNominalValue;
    }

    public Double getMaxNumericValue() {
        return maxNumericValue;
    }

    public void setMaxNumericValue(Double maxNumericValue) {
        this.maxNumericValue = maxNumericValue;
    }

    public Double getMinNumericValue() {
        return minNumericValue;
    }

    public void setMinNumericValue(Double minNumericValue) {
        this.minNumericValue = minNumericValue;
    }

    public Double getDefaultNumericValue() {
        return defaultNumericValue;
    }

    public void setDefaultNumericValue(Double defaultNumericValue) {
        this.defaultNumericValue = defaultNumericValue;
    }

    public Integer getMaxIntValue() {
        return maxIntValue;
    }

    public void setMaxIntValue(Integer maxIntValue) {
        this.maxIntValue = maxIntValue;
    }

    public Integer getMinIntValue() {
        return minIntValue;
    }

    public void setMinIntValue(Integer minIntValue) {
        this.minIntValue = minIntValue;
    }

    public Integer getDefaultIntValue() {
        return defaultIntValue;
    }

    public void setDefaultIntValue(Integer defaultIntValue) {
        this.defaultIntValue = defaultIntValue;
    }

    public Boolean getDefaultBoolValue() {
        return defaultBoolValue;
    }

    public void setDefaultBoolValue(Boolean defaultBoolValue) {
        this.defaultBoolValue = defaultBoolValue;
    }
            
    
}
