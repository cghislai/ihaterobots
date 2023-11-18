/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.charlyghislain.ihaterobots.interfaces;

import com.charlyghislain.ihaterobots.entities.controller.Controller.Action;

/**
 *
 * @author charly
 */
public interface Controllable {

    public void processAction(Action action, boolean started);
}
