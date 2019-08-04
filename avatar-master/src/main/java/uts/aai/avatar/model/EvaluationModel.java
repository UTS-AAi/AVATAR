/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uts.aai.avatar.model;

/**
 *
 * @author ntdun
 */
public class EvaluationModel {
    
    EvaluationModeType evalModelType;
    String specs;
    Boolean validity;
    Long evalTime;
    Double accuracy;

    public EvaluationModel() {
    }

    public EvaluationModel(EvaluationModeType evalModelType, String specs, Boolean validity, Long evalTime, Double accuracy) {
        this.evalModelType = evalModelType;
        this.specs = specs;
        this.validity = validity;
        this.evalTime = evalTime;
        this.accuracy = accuracy;
    }

    public EvaluationModeType getEvalModelType() {
        return evalModelType;
    }

    public void setEvalModelType(EvaluationModeType evalModelType) {
        this.evalModelType = evalModelType;
    }

    public String getSpecs() {
        return specs;
    }

    public void setSpecs(String specs) {
        this.specs = specs;
    }

    public Boolean getValidity() {
        return validity;
    }

    public void setValidity(Boolean validity) {
        this.validity = validity;
    }

    public Long getEvalTime() {
        return evalTime;
    }

    public void setEvalTime(Long evalTime) {
        this.evalTime = evalTime;
    }

    public Double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(Double accuracy) {
        this.accuracy = accuracy;
    }

    

    
    
    
}
