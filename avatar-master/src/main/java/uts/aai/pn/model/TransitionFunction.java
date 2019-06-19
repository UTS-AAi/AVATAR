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
public interface TransitionFunction {
    public Token fire(Token token);
    public Token fireAlg(Token token, String algorithm);
}
