/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uts.aai.avatar.model;

import java.util.List;

/**
 *
 * @author ntdun
 */
public class EvaluationResult {
    
    private String id;
    private List<EvaluationModel> evaluationModel;
            

    public EvaluationResult() {
    }

    public EvaluationResult(String id, List<EvaluationModel> evaluationModel) {
        this.id = id;
        this.evaluationModel = evaluationModel;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<EvaluationModel> getEvaluationModel() {
        return evaluationModel;
    }

    public void setEvaluationModel(List<EvaluationModel> evaluationModel) {
        this.evaluationModel = evaluationModel;
    }

    

    
    
    
}
