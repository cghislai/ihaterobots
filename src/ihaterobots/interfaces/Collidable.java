/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ihaterobots.interfaces;

import org.newdawn.slick.geom.Shape;

/**
 *
 * @author charly
 */
public interface Collidable {

    public void onCollision(Entity otherEntity);

    public boolean isWholeTileCollisable();

    public Shape getCollisionShape();
}
