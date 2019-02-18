/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ihaterobots.game.env;

import ihaterobots.game.serializables.GameStatus;
import ihaterobots.game.serializables.GameOptions;
import ihaterobots.entities.Player;
import ihaterobots.entities.Bowl;
import ihaterobots.entities.Digger;
import ihaterobots.entities.FireBall;
import ihaterobots.entities.LadderPimp;
import ihaterobots.entities.Robot1;
import ihaterobots.entities.Robot2;
import ihaterobots.entities.Rocket;
import ihaterobots.entities.VertJumper;
import ihaterobots.entities.controller.Controller.Action;
import ihaterobots.entities.controller.KeyboardController;
import ihaterobots.game.Utils;
import ihaterobots.game.serializables.Highscore;
import ihaterobots.game.serializables.MapData;
import ihaterobots.game.serializables.MapRotation;
import ihaterobots.interfaces.Entity;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author charly
 */
public class GameManager {

    private GameEnv env;
    private MapData mapData;
    private MapRotation mapRotation;
    private GameStatus status;
    private GameOptions gameOptions;
    private Highscore highscore;
    private List<int[]> redTeleports;
    private List<int[]> blueTeleports;
    private List<int[]> greenTeleports;
    private int[] playerPos;
    private int currentLevel;
    private long levelTimer;
    private boolean gamePaused;
    private boolean gameRunning;
    private int remainingdrops;
    private boolean blueLock;
    private boolean redLock;
    private boolean greenLock;
    private boolean ennemiesPaused;

    public GameManager() {
        redTeleports = new ArrayList<int[]>();
        blueTeleports = new ArrayList<int[]>();
        greenTeleports = new ArrayList<int[]>();
        clearAll();
    }

    public void init(GameEnv env) {
        this.env = env;
    }

    public boolean nextLevel() {
        currentLevel++;
        if (currentLevel >= mapRotation.getMaps().size()) {
            gameFinished();
            return false;
        }
        saveGameStatus();
        return true;
    }

    private void gameFinished() {
        env.getGame().enterState(Utils.STATE_ID_END);
    }

    public void saveGameStatus() {
        status.setLevel(currentLevel);
        status.setRotation(mapRotation);
        status.setScore(env.getPlayer().getScore());
        status.setGameTime(status.getGameTime() + levelTimer);
        status.setLives(env.getPlayer().getLives());

        String filePath = Utils.SAVE_PATH + Utils.GAME_STATUS_FILENAME;
        try {
            File file = new File(filePath);
            FileOutputStream fos = new FileOutputStream(file);
            Utils.writeXML(status, fos);
        } catch (Exception ex) {
            Logger.getLogger(GameManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Load map from rotation
     */
    public void loadMap() {
        String mapName = getMapRotation().getMaps().get(getCurrentLevel());
        if (getMapRotation().getName().equals("official")) {
            InputStream is = getClass().getClassLoader().getResourceAsStream("assets/map/" + mapName + ".map");
            MapData data = Utils.loadMap(is);
            setMapData(data);
            return;
        } else {
            try {
                File file = new File(Utils.MAP_SAVE_PATH + mapName + Utils.MAP_EXT);
                MapData data = Utils.loadMap(new FileInputStream(file));
                setMapData(data);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(GameManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * send map to GameMap, process it
     */
    public void startMap() {
        sendMapData();
        processMapData(mapData.getTiles());
        levelTimer = 0;
        if (remainingdrops == 0) {
            env.getMap().getMapDrawer().triggerDoor();
        }
        resetPlayerPos();
        if (playerPos[0] >= 0 && playerPos[1] >= 0) {
            env.getPlayer().register(env);
        }
    }

    public void prepareNewGame() {
        prepareNewMap();
        env.getPlayer().renewPlayer();
        status = new GameStatus();
//      env.getPlayer().unregister();
    }

    public void prepareNewMap() {
        clearAll();
        env.getEntitiesManager().unregisterAll();
        env.getMap().getMapDrawer().resetDoor();
    }

    public void clearAll() {
        remainingdrops = 0;
        blueLock = false;
        redLock = false;
        greenLock = false;
        ennemiesPaused = false;
        playerPos = new int[]{-1, -1};
        redTeleports.clear();
        blueTeleports.clear();
        greenTeleports.clear();
    }

    public int[] findOtherTeleport(int i, int j, int tileId) {
        switch (tileId) {
            case Utils.TILE_ID_RED_TELEPORT:
                Collections.shuffle(redTeleports);
                for (int[] coord : redTeleports) {
                    if (coord[0] != i || coord[1] != j) {
                        return coord;
                    }
                }
                break;
            case Utils.TILE_ID_BLUE_TELEPORT:
                Collections.shuffle(blueTeleports);
                for (int[] coord : blueTeleports) {
                    if (coord[0] != i || coord[1] != j) {
                        return coord;
                    }
                }
                break;
            case Utils.TILE_ID_GREEN_TELEPORT:
                Collections.shuffle(greenTeleports);
                for (int[] coord : greenTeleports) {
                    if (coord[0] != i || coord[1] != j) {
                        return coord;
                    }
                }
                break;
        }
        return new int[]{i, j};
    }

    public void addRedTeleport(int x, int y) {
        redTeleports.add(new int[]{x, y});
    }

    public void addBlueTeleport(int x, int y) {
        blueTeleports.add(new int[]{x, y});
    }

    public void addGreenTeleport(int x, int y) {
        greenTeleports.add(new int[]{x, y});
    }

    public void setPlayerPos(int x, int y) {
        playerPos = new int[]{x, y};
    }

    public int getPlayerPosX() {
        return playerPos[0];
    }

    public int getPlayerPosY() {
        return playerPos[1];
    }

    public boolean isBlueLock() {
        return blueLock;
    }

    public void setBlueLock(boolean blueLock) {
        if (this.blueLock == blueLock) {
            return;
        }
        this.blueLock = blueLock;
        if (blueLock) {
            env.getSoundManager().playDoorClose();
        } else {
            env.getSoundManager().playDoorOpen();
        }
    }

    public boolean isEnnemiesPaused() {
        return ennemiesPaused;
    }

    public void setEnnemiesPaused(boolean ennemiesPaused) {
        this.ennemiesPaused = ennemiesPaused;
    }

    public boolean isGreenLock() {
        return greenLock;
    }

    public void setGreenLock(boolean greenLock) {
        if (this.greenLock == greenLock) {
            return;
        }
        this.greenLock = greenLock;
        if (greenLock) {
            env.getSoundManager().playDoorClose();
        } else {
            env.getSoundManager().playDoorOpen();
            for (Entity entity : env.getEntitiesManager().getEntities()) {
                entity.doorLockChanged();
            }
        }
    }

    public boolean isRedLock() {
        return redLock;
    }

    public void setRedLock(boolean redLock) {
        if (this.redLock == redLock) {
            return;
        }
        this.redLock = redLock;
        if (redLock) {
            env.getSoundManager().playDoorClose();
        } else {
            env.getSoundManager().playDoorOpen();
        }
    }

    public int getRemainingdrops() {
        return remainingdrops;
    }

    public void setRemainingdrops(int remainingdrops) {
        this.remainingdrops = remainingdrops;
    }

    private void processMapData(int[][][] mapData) {
        playerPos = new int[]{-1, -1};
        redLock = false;
        blueLock = false;
        greenLock = false;

        for (int x = 0; x < mapData.length; x++) {
            for (int y = 0; y < mapData[x].length; y++) {
                int tileId = mapData[x][y][Utils.MAP_Z_NORMAL];
                switch (tileId) {
                    case Utils.TILE_ID_DROP:
                        remainingdrops++;
                        break;
                    case Utils.TILE_ID_RED_TELEPORT:
                        redTeleports.add(new int[]{x, y});
                        break;
                    case Utils.TILE_ID_BLUE_TELEPORT:
                        blueTeleports.add(new int[]{x, y});
                        break;
                    case Utils.TILE_ID_GREEN_TELEPORT:
                        greenTeleports.add(new int[]{x, y});
                        break;
                    case Utils.TILE_ID_RED_DOOR_TRIGGER_UP:
                        redLock = true;
                        break;
                    case Utils.TILE_ID_BLUE_DOOR_TRIGGER_UP:
                        blueLock = true;
                        break;
                    case Utils.TILE_ID_GREEN_DOOR_TRIGGER_UP:
                        greenLock = true;
                        break;
                }

                int entityTileId = mapData[x][y][Utils.MAP_Z_STARTENTITIES];
                switch (entityTileId) {
                    case Utils.TILE_ID_PLAYER:
                        playerPos = new int[]{x, y};
                        resetPlayerPos();
                        break;
                    case Utils.TILE_ID_ROBOT1:
                        Robot1 robot1 = new Robot1();
                        robot1.setPosX((x + .5f) * Utils.TILE_SIZE);
                        robot1.setPosY((y + .5f) * Utils.TILE_SIZE);
                        robot1.register(env);
                        break;
                    case Utils.TILE_ID_ROBOT2:
                        Bowl bowl = new Bowl();
                        bowl.setPosX((x + .5f) * Utils.TILE_SIZE);
                        bowl.setPosY((y + .5f) * Utils.TILE_SIZE);
                        bowl.register(env);
                        break;
                    case Utils.TILE_ID_ROCKET:
                        Rocket rocket = new Rocket();
                        rocket.setPosX((x + .5f) * Utils.TILE_SIZE);
                        rocket.setPosY((y + .5f) * Utils.TILE_SIZE);
                        rocket.register(env);
                        break;
                    case Utils.TILE_ID_LADDER_PIMP:
                        LadderPimp ladderPimp = new LadderPimp();
                        ladderPimp.setPosX((x + .5f) * Utils.TILE_SIZE);
                        ladderPimp.setPosY((y + .5f) * Utils.TILE_SIZE);
                        ladderPimp.register(env);
                        break;
                    case Utils.TILE_ID_VERT_JUMPER:
                        VertJumper vertJumper = new VertJumper();
                        vertJumper.setPosX((x + .5f) * Utils.TILE_SIZE);
                        vertJumper.setPosY((y + .5f) * Utils.TILE_SIZE);
                        vertJumper.register(env);
                        break;
                    case Utils.TILE_ID_DIGGER:
                        Digger digger = new Digger();
                        digger.setPosX((x + .5f) * Utils.TILE_SIZE);
                        digger.setPosY((y + .5f) * Utils.TILE_SIZE);
                        digger.register(env);
                        break;
                    case Utils.TILE_ID_FIRE_BALL:
                        FireBall fireBall = new FireBall();
                        fireBall.setPosX((x + .5f) * Utils.TILE_SIZE);
                        fireBall.setPosY((y + .5f) * Utils.TILE_SIZE);
                        fireBall.register(env);
                        break;
                    case Utils.TILE_ID_FLYINGJET:
                        Robot2 robot2 = new Robot2();
                        robot2.setPosX((x + .5f) * Utils.TILE_SIZE);
                        robot2.setPosY((y + .5f) * Utils.TILE_SIZE);
                        robot2.register(env);
                        break;
                }
            }
        }
    }

    public void resetPlayerPos() {
        Player p = env.getPlayer();
        p.setPosX((playerPos[0] + .5f) * Utils.TILE_SIZE);
        p.setPosY((playerPos[1] + .5f) * Utils.TILE_SIZE);
    }

    public MapData getMapData() {
        return mapData;
    }

    public void setMapData(MapData mapData) {
        this.mapData = mapData;
        sendMapData();
    }

    public void sendMapData() {
        env.getMap().setMapData(mapData.getTiles());
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }

    public MapRotation getMapRotation() {
        return mapRotation;
    }

    public void setMapRotation(MapRotation mapRotation) {
        this.mapRotation = mapRotation;
    }

    public GameStatus getStatus() {
        return status;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
        currentLevel = status.getLevel();
        mapRotation = status.getRotation();
        env.getPlayer().setLives(status.getLives());
        env.getPlayer().setScore(status.getScore());
    }

    public long getLevelTimer() {
        return levelTimer;
    }

    public void updatePlayTimer(int time) {
        levelTimer += time;
    }

    public GameOptions getGameOptions() {
        return gameOptions;
    }

    public void setGameOptions(GameOptions gameOptions) {
        this.gameOptions = gameOptions;
    }

    public void applyOptions() {
        env.getSoundManager().setFxVolume(gameOptions.getFxVol());
        env.getSoundManager().setMusicVolume(gameOptions.getMusicVol());
        KeyboardController plCtrler = env.getPlayer().getController();
        plCtrler.clearKeys();
        plCtrler.registerKey(Action.LEFT, gameOptions.getKeyLeft());
        plCtrler.registerKey(Action.RIGHT, gameOptions.getKeyRight());
        plCtrler.registerKey(Action.UP, gameOptions.getKeyUp());
        plCtrler.registerKey(Action.DOWN, gameOptions.getKeyDown());
        plCtrler.registerKey(Action.FIRE, gameOptions.getKeyFire());
        plCtrler.registerKey(Action.DELETE, gameOptions.getKeyDie());
    }

    public void loadOption() {
        String path = Utils.SAVE_PATH + Utils.GAME_OPTIONS_FILENAME;
        try {
            File file = new File(path);
            gameOptions = (GameOptions) Utils.loadXML(new FileInputStream(file));
        } catch (Exception ex) {
            gameOptions = new GameOptions();
        }
    }

    public void saveOptions() {
        String path = Utils.SAVE_PATH + Utils.GAME_OPTIONS_FILENAME;
        try {
            File file = new File(path);
            FileOutputStream fos = new FileOutputStream(file);
            Utils.writeXML(gameOptions, fos);
        } catch (Exception ex) {
            Logger.getLogger(GameManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadHighScores() {
        String path = Utils.SAVE_PATH + Utils.HIGHSCORE_FILENAME;
        try {
            File file = new File(path);
            highscore = (Highscore) Utils.loadXML(new FileInputStream(file));
        } catch (Exception ex) {
            highscore = new Highscore();
        }
    }

    public void saveHighScores() {
        String path = Utils.SAVE_PATH + Utils.HIGHSCORE_FILENAME;
        try {
            File file = new File(path);
            FileOutputStream fos = new FileOutputStream(file);
            Utils.writeXML(highscore, fos);
            System.out.println("highscore saved");
        } catch (Exception ex) {
            Logger.getLogger(GameManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addHighScore(String player, long time, int level, int score) {
        int indx = 0;
        for (indx = 0; indx < highscore.levels.size(); indx++) {
            if (indx == highscore.levels.size()) {
                break;
            }
            if (highscore.scores.get(indx) < score) {
                break;
            }
        }
        highscore.levels.add(indx, level);
        highscore.names.add(indx, player);
        highscore.scores.add(indx, score);
        highscore.times.add(indx, time);
        System.out.println("Highscore added : " + highscore.toString() + "  " + highscore.names.toString());

    }

    public Highscore getHighscore() {
        return highscore;
    }

    public boolean isGamePaused() {
        return gamePaused;
    }

    public void setGamePaused(boolean gamePaused) {
        this.gamePaused = gamePaused;
    }

    public boolean isGameRunning() {
        return gameRunning;
    }

    public void setGameRunning(boolean gameRunning) {
        this.gameRunning = gameRunning;
    }
}
