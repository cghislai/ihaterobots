/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.charlyghislain.ihaterobots;

import com.charlyghislain.ihaterobots.game.MainGame;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

/**
 *
 * @author charly
 */
public class IHateRobots {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            AppGameContainer container = new AppGameContainer(new MainGame());
            container.setDisplayMode(800, 600, false);
            container.setTargetFrameRate(50);
            container.setMinimumLogicUpdateInterval(10);
            container.setMaximumLogicUpdateInterval(30);
            container.start();
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }
}
