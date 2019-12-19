/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uts.aai.pn.model;

import java.util.List;
import uts.aai.avatar.model.MLComponent;

/**
 *
 * @author ntdun
 */
public interface TransitionFunction {
    public Token fire(Token token);
    public Token fireAlg(Token token, String algorithm, List<MLComponent> loadedListOfMLComponents);
}
