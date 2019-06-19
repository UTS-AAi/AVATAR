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
public class Place {

    private String id;
    private Token token;

    public Place() {
    }

    public Place(String id, Token token) {
        this.id = id;
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public String toString(int graphicPosition) {
        String str
                = "       <place id=\"" + getId() + "\">\n"
                + "         <graphics>\n"
                + "            <position x=\"" + graphicPosition * 120 + "\" y=\"105.0\" />\n"
                + "         </graphics>\n"
                + "         <name>\n"
                + "            <value>" + "" + "</value>\n"
                + "            <graphics>\n"
                + "               <offset x=\"0.0\" y=\"0.0\" />\n"
                + "            </graphics>\n"
                + "         </name>\n"
                + "         <initialMarking>\n";

        if (graphicPosition == 1) {

            str += "            <value>Default,1</value>\n";
        } else {
            str += "            <value>Default,0</value>\n";
        }

        str += "            <graphics>\n"
                + "               <offset x=\"0.0\" y=\"0.0\" />\n"
                + "            </graphics>\n"
                + "         </initialMarking>\n"
                + "         <capacity>\n"
                + "            <value>0</value>\n"
                + "         </capacity>\n"
                + "      </place>";

        return str;
    }

}
