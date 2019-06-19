/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uts.aai.pn.model;

import java.util.List;
import uts.aai.pn.model.Parameter;

/**
 *
 * @author ntdun
 */
public class AlgorithmConfiguration {
    
    private String id;
    private String name;
    private String path;
    
    private List<Parameter> capabilityList;
    private List<Parameter> effectList;

    public AlgorithmConfiguration() {
    }

    public AlgorithmConfiguration(String id, String name, String path, List<Parameter> capabilityList, List<Parameter> effectList) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.capabilityList = capabilityList;
        this.effectList = effectList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<Parameter> getCapabilityList() {
        return capabilityList;
    }

    public void setCapabilityList(List<Parameter> capabilityList) {
        this.capabilityList = capabilityList;
    }

    public List<Parameter> getEffectList() {
        return effectList;
    }

    public void setEffectList(List<Parameter> effectList) {
        this.effectList = effectList;
    }
    
    
    
    
    
}
