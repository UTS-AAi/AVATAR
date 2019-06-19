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
public class PetriNetsPipeline {

    private String id;
    private List<Place> placeList;
    private List<Arc> arcList;
    private List<Transition> transitionList;

    public PetriNetsPipeline() {
    }

    public PetriNetsPipeline(String id, List<Place> placeList, List<Arc> arcList, List<Transition> transitionList) {
        this.id = id;
        this.placeList = placeList;
        this.arcList = arcList;
        this.transitionList = transitionList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Place> getPlaceList() {
        return placeList;
    }

    public void setPlaceList(List<Place> placeList) {
        this.placeList = placeList;
    }

    public List<Arc> getArcList() {
        return arcList;
    }

    public void setArcList(List<Arc> arcList) {
        this.arcList = arcList;
    }

    public List<Transition> getTransitionList() {
        return transitionList;
    }

    public void setTransitionList(List<Transition> transitionList) {
        this.transitionList = transitionList;
    }

    private Token getToken() {

        Token token = null;

        for (Place place : placeList) {

            if (place.getToken() != null) {
                token = place.getToken();
            }

        }

        return token;
    }

    @Override
    public String toString() {

        String str = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<pnml>\n"
                + "   <net id=\"Net-One\" type=\"P/T net\">";

        Token token = getToken();
        str += "\n";
        str += token.toString();

        for (int i = 0; i < placeList.size(); i++) {
            str += "\n";;
            str += placeList.get(i).toString(i + 1);

        }

        for (int i = 0; i < transitionList.size(); i++) {
            str += "\n";
            str += transitionList.get(i).toString(i + 1);

        }

        for (int i = 0; i < arcList.size(); i++) {
            str += "\n";
            str += arcList.get(i).toString(i + 1);

        }

        str += "\n";
        str += "   </net>\n"
                + "</pnml>";
        return str;
    }

}
