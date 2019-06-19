/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uts.aai.pn.test;

import uts.aai.pn.model.Parameter;
import uts.aai.pn.model.Transition;
import uts.aai.pn.model.MetaFeatureEvaluationTransitionFunction;
import uts.aai.pn.model.Arc;
import uts.aai.pn.model.Place;
import uts.aai.pn.model.PetriNetsPipeline;
import uts.aai.pn.model.Token;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ntdun
 */
public class SamplePipeline1 {

    public PetriNetsPipeline sample() {

        List<Place> placeList = new ArrayList<>();
        placeList.add(getPlace1());
        placeList.add(getPlace2());
        placeList.add(getPlace3());

        List<Transition> transitionList = new ArrayList<>();
        transitionList.add(getTransition1());
        transitionList.add(getTransition2());

        List<Arc> arcList = new ArrayList<>();
        arcList.add(getArc1());
        arcList.add(getArc2());
        arcList.add(getArc3());
        arcList.add(getArc4());

        PetriNetsPipeline petriNetsPipeline = new PetriNetsPipeline("pipeline1", placeList, arcList, transitionList);

        return petriNetsPipeline;
    }

    private Place getPlace1() {
        Place place = new Place();
        Parameter p1 = new Parameter("MISSING_VALUES", 1);
        Parameter p2 = new Parameter("NOMINAL_CLASS", 1);
        Parameter p3 = new Parameter("OUTLIER", 0);

        List<Parameter> paramList = new ArrayList<>();
        paramList.add(p1);
        paramList.add(p2);
        paramList.add(p3);

        Token token1 = new Token("token1", paramList);

        place.setId("place1");
        place.setToken(token1);

        return place;
    }

    private Place getPlace2() {
        Place place = new Place();

        place.setId("place2");
        place.setToken(null);

        return place;
    }

    private Place getPlace3() {
        Place place = new Place();

        place.setId("place3");
        place.setToken(null);

        return place;
    }

    private Transition getTransition1() {

        Transition transition = new Transition();

        transition.setId("transition1");
        transition.setName("ReplaceMissingValues");
        transition.setTransitionFunction(new MetaFeatureEvaluationTransitionFunction());

        return transition;

    }

    private Transition getTransition2() {

        Transition transition = new Transition();

        transition.setId("transition2");
        transition.setName("NaiveBayes");
        transition.setTransitionFunction(new MetaFeatureEvaluationTransitionFunction());

        return transition;

    }

    private Arc getArc1() {

        Arc arc = new Arc("arc1", "place1", "transition1", true);

        return arc;

    }

    private Arc getArc2() {

        Arc arc = new Arc("arc2", "place2", "transition1", false);

        return arc;

    }

    private Arc getArc3() {

        Arc arc = new Arc("arc3", "place2", "transition2", true);

        return arc;

    }

    private Arc getArc4() {

        Arc arc = new Arc("arc4", "place3", "transition2", false);

        return arc;

    }

}
