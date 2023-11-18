/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.charlyghislain.ihaterobots.game.env;

import com.charlyghislain.ihaterobots.interfaces.Entity;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author charly
 */
public class MovablesManager {

    private final List<Entity> movables;

    public void addMovable(Entity movable) {
        movables.add(movable);
    }

    public void removeMovable(Entity movable) {
        movables.remove(movable);
    }

    public MovablesManager() {
        movables = new ArrayList<Entity>();
    }

    public List<Entity> getMovables() {
        return movables;
    }
}
