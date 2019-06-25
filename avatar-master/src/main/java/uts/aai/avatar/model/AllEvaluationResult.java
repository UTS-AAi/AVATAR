/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uts.aai.avatar.model;

import java.util.ArrayList;

/**
 *
 * @author ntdun
 */
public class AllEvaluationResult {
    private ArrayList<EvaluationResult> evaluationResultList;

    public AllEvaluationResult() {
    }

    public AllEvaluationResult(ArrayList<EvaluationResult> evaluationResultList) {
        this.evaluationResultList = evaluationResultList;
    }

    public ArrayList<EvaluationResult> getEvaluationResultList() {
        return evaluationResultList;
    }

    public void setEvaluationResultList(ArrayList<EvaluationResult> evaluationResultList) {
        this.evaluationResultList = evaluationResultList;
    }
    
    
}
