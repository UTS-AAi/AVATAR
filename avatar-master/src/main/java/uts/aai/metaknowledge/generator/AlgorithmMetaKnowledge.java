/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uts.aai.metaknowledge.generator;

import java.util.List;
import uts.aai.mf.model.MLComponent;

/**
 *
 * @author ntdun
 */
public class AlgorithmMetaKnowledge {
    
  
    private List<MLComponent> listOfMLComponents;

    public AlgorithmMetaKnowledge() {
    }

    public AlgorithmMetaKnowledge(List<MLComponent> listOfMLComponents) {
        this.listOfMLComponents = listOfMLComponents;
    }

    public List<MLComponent> getListOfMLComponents() {
        return listOfMLComponents;
    }

    public void setListOfMLComponents(List<MLComponent> listOfMLComponents) {
        this.listOfMLComponents = listOfMLComponents;
    }

   
}
