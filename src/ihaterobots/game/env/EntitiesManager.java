/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ihaterobots.game.env;

import ihaterobots.interfaces.Entity;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author charly
 */
public class EntitiesManager {
    
    private final Set<Entity> entities;
    private final Set<Entity> toAddEntities;
    private final Set<Entity> toRemoveEntities;

    public EntitiesManager() {
        entities = new HashSet<Entity>();
        toAddEntities = new HashSet<Entity>();
        toRemoveEntities = new HashSet<Entity>();
    }
    
    public void addEntity(Entity e) {
        toAddEntities.add(e);
    }
    
    public void removeEntity(Entity e) {
        toRemoveEntities.add(e);
    }
    
    public Set<Entity> getEntities() {
        for (Entity e : toRemoveEntities) {
            entities.remove(e);
        }
        for (Entity e : toAddEntities) {
            entities.add(e);
        }
        toRemoveEntities.clear();
        toAddEntities.clear();
        return entities;
    }
    
    public void unregisterAll() {
        for (Entity e : getEntities()) {
            e.unregister();
        }
    }
}
