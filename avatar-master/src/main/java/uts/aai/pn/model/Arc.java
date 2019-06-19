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
public class Arc {

    private String id;
    private String placeId;
    private String transitionId;
    private boolean isPlaceToTransition;

    public Arc() {
    }

    public Arc(String id, String placeId, String transitionId, boolean isPlaceToTransition) {
        this.id = id;
        this.placeId = placeId;
        this.transitionId = transitionId;
        this.isPlaceToTransition = isPlaceToTransition;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getTransitionId() {
        return transitionId;
    }

    public void setTransitionId(String transitionId) {
        this.transitionId = transitionId;
    }

    public boolean isIsPlaceToTransition() {
        return isPlaceToTransition;
    }

    public void setIsPlaceToTransition(boolean isPlaceToTransition) {
        this.isPlaceToTransition = isPlaceToTransition;
    }

    public String toString(int graphicPosition) {
        String str = "";
        int x1 = 0;
        int x2 = 0;

        if (isPlaceToTransition) {
            str
                    += "       <arc id=\"" + getId() + "\" source=\"" + getPlaceId() + "\" target=\"" + getTransitionId() + "\">\n";

            x1 = (int) (26 + 120 * ((graphicPosition + 1) / 2));
            x2 = (int) (66 + 120 * ((graphicPosition + 1) / 2));
        } else {
            str
                    += "       <arc id=\"" + getId() + "\" source=\"" + getTransitionId() + "\" target=\"" + getPlaceId() + "\">\n";
            x1 = (int) (76 + 120 * (graphicPosition / 2));
            x2 = (int) (117 + 120 * (graphicPosition / 2));
        }

        str
                += "         <graphics />\n"
                + "         <inscription>\n"
                + "            <value>Default,1</value>\n"
                + "            <graphics />\n"
                + "         </inscription>\n"
                + "         <tagged>\n"
                + "            <value>false</value>\n"
                + "         </tagged>\n"
                + "         <arcpath id=\"000\" x=\"" + x1 + "\" y=\"117\" curvePoint=\"false\" />\n"
                + "         <arcpath id=\"001\" x=\"" + x2 + "\" y=\"117\" curvePoint=\"false\" />\n"
                + "         <type value=\"normal\" />\n"
                + "      </arc>";

        return str;
    }

}
