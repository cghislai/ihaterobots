/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.charlyghislain.ihaterobots.entities;

/**
 *
 * @author cghislai
 */
public interface EntityListener {

    public void movingChanged();

    public void teleportingChanged();

    public void climbingChanged();

    public void fallingChanged();

    public void facingLeftChanged();

    public void stuckChanged();

    public void collidedLeft();

    public void collidedRight();

    public void collidedUp();

    public void collidedDown();
}
