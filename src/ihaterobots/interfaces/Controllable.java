/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ihaterobots.interfaces;

import ihaterobots.entities.controller.Controller.Action;

/**
 *
 * @author charly
 */
public interface Controllable {

    public void processAction(Action action, boolean started);
}
