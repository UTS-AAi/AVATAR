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
public class EvaluationResult {
    
    private String bpmnPipeline;
    private boolean validity;
    private Double accuracy;
    private long evaluationTime;

    public EvaluationResult() {
    }

    public EvaluationResult(String bpmnPipeline, boolean validity, Double bestAccuracy) {
        this.bpmnPipeline = bpmnPipeline;
        this.validity = validity;
        this.accuracy = bestAccuracy;
    }

    public String getBpmnPipeline() {
        return bpmnPipeline;
    }

    public void setBpmnPipeline(String bpmnPipeline) {
        this.bpmnPipeline = bpmnPipeline;
    }

    public boolean isValidity() {
        return validity;
    }

    public void setValidity(boolean validity) {
        this.validity = validity;
    }

    public Double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(Double accuracy) {
        this.accuracy = accuracy;
    }

    public long getEvaluationTime() {
        return evaluationTime;
    }

    public void setEvaluationTime(long evaluationTime) {
        this.evaluationTime = evaluationTime;
    }
    
    
    
}
