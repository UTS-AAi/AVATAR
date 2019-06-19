/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uts.aai.pn.model;

import java.util.List;

/**
 *
 * @author ntdun
 */
public class Token {

    private String id;
    private List<Parameter> parameterList;

    public Token() {
    }

    public Token(String id, List<Parameter> parameterList) {
        this.id = id;
        this.parameterList = parameterList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Parameter> getParameterList() {
        return parameterList;
    }

    public void setParameterList(List<Parameter> parameterList) {
        this.parameterList = parameterList;
    }

    @Override
    public String toString() {
        String str = "      <token id=\"Default\" enabled=\"true\" red=\"0\" green=\"0\" blue=\"0\" >";

        for (Parameter parameter : getParameterList()) {
            str += "\n";
            str += "            <parameter id=\"" + parameter.getParamName() + "\" value=\"" + parameter.getParamValue() + "\" />";
        }

        str += "</token>";
        return str;
    }
}
