/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uts.aai.pn.model;

/**
 *
 * @author ntdun
 */
public class Transition {

    private String id;
    private String name;
    private TransitionFunction transitionFunction;

    public Transition() {
    }

    public Transition(String id, TransitionFunction transitionFunction) {
        this.id = id;
        this.transitionFunction = transitionFunction;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TransitionFunction getTransitionFunction() {
        return transitionFunction;
    }

    public void setTransitionFunction(TransitionFunction transitionFunction) {
        this.transitionFunction = transitionFunction;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    

    public String toString(int graphicPosition) {
        String str
                = "      <transition id=\"" + getId() + "\">\n"
                + "         <graphics>\n"
                + "            <position x=\"" + (60 + graphicPosition * 120) + "\" y=\"105.0\" />\n"
                + "         </graphics>\n"
                + "         <name>\n"
                + "            <value>"+getName()+"</value>\n"
                + "            <graphics>\n"
                + "               <offset x=\"-5.0\" y=\"35.0\" />\n"
                + "            </graphics>\n"
                + "         </name>\n"
                + "         <orientation>\n"
                + "            <value>0</value>\n"
                + "         </orientation>\n"
                + "         <rate>\n"
                + "            <value>1.0</value>\n"
                + "         </rate>\n"
                + "         <timed>\n"
                + "            <value>false</value>\n"
                + "         </timed>\n"
                + "         <infiniteServer>\n"
                + "            <value>false</value>\n"
                + "         </infiniteServer>\n"
                + "         <priority>\n"
                + "            <value>1</value>\n"
                + "         </priority>\n"
                + "      </transition>";

        return str;
    }

}
