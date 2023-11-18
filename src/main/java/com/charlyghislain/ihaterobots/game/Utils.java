/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.charlyghislain.ihaterobots.game;

import com.charlyghislain.ihaterobots.game.serializables.MapData;
import com.charlyghislain.ihaterobots.game.serializables.MapRotation;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.PopupBuilder;
import de.lessvoid.nifty.builder.TextBuilder;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import de.lessvoid.nifty.controls.scrollpanel.builder.ScrollPanelBuilder;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;

/**
 * @author charly
 */
public class Utils {
    public static final boolean EASY_SKIP = true;
    // DRAWBLE PRIORITIES
    public static final short DRAWABLE_PRIORITY_BACKGROUND = 0;
    public static final short DRAWABLE_PRIORITY_NORMAL = 1;
    public static final short DRAWABLE_PRIORITY_FRONT = 2;
    public static final short DRAWABLE_PRIORITY_UI = 3;
    // TILES
    public static final int TILE_SIZE = 32;
    public static final int TILE_ID_BACK1 = 0;
    public static final int TILE_ID_ROCK = 1;
    public static final int TILE_ID_DIRT = 2;
    public static final int TILE_ID_SAND = 3;
    public static final int TILE_ID_LADDER = 4;
    public static final int TILE_ID_GOLD1 = 5;
    public static final int TILE_ID_ROBOT1 = 6;
    public static final int TILE_ID_PLAYER = 7;
    public static final int TILE_ID_SINGLE_FUEL = 8;
    public static final int TILE_ID_DOUBLE_FUEL = 9;
    public static final int TILE_ID_EMPTY_DROP = 10;
    public static final int TILE_ID_DROP = 11;
    public static final int TILE_ID_DOOR_TOPLEFT = 12;
    public static final int TILE_ID_DOOR = 13;
    public static final int TILE_ID_ROBOT2 = 14;
    public static final int TILE_ID_ROCKET = 15;
    public static final int TILE_ID_PO_TOPLEFT = 16;
    public static final int TILE_ID_PO_TOPRIGHT = 17;
    public static final int TILE_ID_PO_BOTLEFT = 18;
    public static final int TILE_ID_PO_BOTRIGHT = 19;
    public static final int TILE_ID_PO_TOPLEFTRIGHT = 20;
    public static final int TILE_ID_PO_RIGHTTOPBOT = 21;
    public static final int TILE_ID_PO_BOTLEFTRIGHT = 22;
    public static final int TILE_ID_PO_LEFTTOPBOT = 23;
    public static final int TILE_ID_PO_NOTTOPLEFT = 24;
    public static final int TILE_ID_PO_NOTTOPRIGHT = 25;
    public static final int TILE_ID_PO_NOTBOTLEFT = 26;
    public static final int TILE_ID_PO_NOTBOTRIGHT = 27;
    public static final int TILE_ID_PO_BALL = 28;
    public static final int TILE_ID_PO_ROLL_LEFT = 29;
    public static final int TILE_ID_PO_ROLL_RIGHT = 30;
    public static final int TILE_ID_LADDER_UP = 31;
    public static final int TILE_ID_LADDER_DOWN = 32;
    public static final int TILE_ID_PO_ICE = 33;
    public static final int TILE_ID_PO_GRASS = 34;
    public static final int TILE_ID_BACK_ROCK = 35;
    public static final int TILE_ID_BACK_DIRT = 36;
    public static final int TILE_ID_BACK_SAND = 37;
    public static final int TILE_ID_PO_FRONT = 38;
    public static final int TILE_ID_RED_DOOR_VERT = 41;
    public static final int TILE_ID_RED_DOOR_HOR = 42;
    public static final int TILE_ID_RED_DOOR_TRIGGER_UP = 43;
    public static final int TILE_ID_RED_DOOR_TRIGGER_DOWN = 44;
    public static final int TILE_ID_RED_DOOR_TRIGGER_AUTO = 45;
    public static final int TILE_ID_RED_TELEPORT = 46;
    public static final int TILE_ID_BLUE_DOOR_VERT = 47;
    public static final int TILE_ID_BLUE_DOOR_HOR = 48;
    public static final int TILE_ID_BLUE_DOOR_TRIGGER_UP = 49;
    public static final int TILE_ID_BLUE_DOOR_TRIGGER_DOWN = 50;
    public static final int TILE_ID_BLUE_DOOR_TRIGGER_AUTO = 51;
    public static final int TILE_ID_BLUE_TELEPORT = 52;
    public static final int TILE_ID_GREEN_DOOR_HOR = 53;
    public static final int TILE_ID_GREEN_DOOR_TRIGGER_UP = 54;
    public static final int TILE_ID_GREEN_DOOR_TRIGGER_DOWN = 55;
    public static final int TILE_ID_GREEN_DOOR_TRIGGER_AUTO = 56;
    public static final int TILE_ID_GREEN_TELEPORT = 57;
    public static final int TILE_ID_GREEN_DOOR_VERT = 58;
    public static final int TILE_ID_SPIKES = 59;
    public static final int TILE_ID_GOLD2 = 60;
    public static final int TILE_ID_GOLD_STATUE = 61;
    public static final int TILE_ID_LADDER_PIMP = 62;
    public static final int TILE_ID_VERT_JUMPER = 63;
    public static final int TILE_ID_PO_ROCK_TOPLEFT = 64;
    public static final int TILE_ID_PO_ROCK_TOPRIGHT = 65;
    public static final int TILE_ID_PO_ROCK_BOTLEFT = 66;
    public static final int TILE_ID_PO_ROCK_BOTRIGHT = 67;
    public static final int TILE_ID_PO_ROCK_TOP = 68;
    public static final int TILE_ID_PO_ROCK_LEFT = 69;
    public static final int TILE_ID_PO_ROCK_RIGHT = 70;
    public static final int TILE_ID_PO_ROCK_DOWN = 71;
    public static final int TILE_ID_PO_ROCK_NOTLEFT = 72;
    public static final int TILE_ID_PO_ROCK_NOTRIGHT = 73;
    public static final int TILE_ID_PO_ROCK_NOTTOP = 74;
    public static final int TILE_ID_PO_ROCK_NOTDOWN = 75;
    public static final int TILE_ID_PO_ROCK_ALL = 76;
    public static final int TILE_ID_PO_ROCK_TOPBOT = 77;
    public static final int TILE_ID_PO_ROCK_LEFTRIGHT = 78;
    public static final int TILE_ID_PO_WINDOW = 79;
    public static final int TILE_ID_GREEN_TELEPORT_ANIM = 80;
    public static final int TILE_ID_RED_TELEPORT_ANIM = 81;
    public static final int TILE_ID_BLUE_TELEPORT_ANIM = 82;
    public static final int TILE_ID_QUICK_BREAK = 83;
    public static final int TILE_ID_WOODCRATE = 84;
    public static final int TILE_ID_COLUMN = 85;
    public static final int TILE_ID_BARRIER = 86;
    public static final int TILE_ID_WARNING_SIGN = 87;
    public static final int TILE_ID_POWERUP_LIGHT = 88;
    public static final int TILE_ID_POWERUP_BLUESTAR = 89;
    public static final int TILE_ID_DIGGER = 90;
    public static final int TILE_ID_FLYINGJET = 91;
    public static final int TILE_ID_FUEL_FILL = 92;
    public static final int TILE_ID_FUEL_WASTE = 93;
    public static final int TILE_ID_SPIKES_LEFT = 94;
    public static final int TILE_ID_SPIKES_RIGHT = 95;
    public static final int TILE_ID_SPIKES_TOP = 96;
    public static final int TILE_ID_FIRE_BALL = 97;
    public static final int TILE_ID_COLUMN_BOT = 98;
    public static final int TILE_ID_COLUMN_TOP = 99;
    public static final int TILE_ID_COLUMN_MID = 100;
    public static final int TILE_ID_COLUMN_WOOD = 101;
    public static final int TILE_ID_BRICKS = 102;
    public static final int TILE_ID_METAL = 103;
    public static final int TILE_ID_GOLD = 104;
    // ENTITIES STUFF
    public static final int ENTITY_TYPE_BACKGROUND = 0;
    public static final int ENTITY_TYPE_PLAYER = 1;
    public static final int ENTITY_TYPE_MAPEDIT_CURSOR = 2;
    public static final int ENTITY_TYPE_ENNEMY = 3;
    // SCORE & OTHERS
    public static final int SCORE_GOLD1 = 500;
    public static final int SCORE_GOLD2 = 100;
    public static final int SCORE_GOLD3 = 2000;
    public static final int SCORE_DROP = 50;
    public static final float ROLL_SPEED = 0.04f;
    public static final int QUICK_BREAK_TIMER = 1000;
    // STATES
    public final static int STATE_ID_LOADING = 0;
    public final static int STATE_ID_MAINMENU = 1;
    public final static int STATE_ID_INGAME = 2;
    public final static int STATE_ID_MAPEDITOR = 3;
    public final static int STATE_ID_LEVEL_ENTRANCE = 4;
    public final static int STATE_ID_WAITING_INGAME = 5;
    public final static int STATE_ID_END = 6;
    public final static int STATE_ID_GAMEOVER = 7;
    public final static int STATE_ID_HIGHSCORES = 8;
    // SPRITE SHEETS
    public final static int SHEET_BOWL = 1;
    public final static int SHEET_DIGGER = 2;
    public final static int SHEET_LADDER_PIMP = 3;
    public final static int SHEET_ROBOT1_WALK = 4;
    public final static int SHEET_ROCKET = 5;
    public final static int SHEET_VERTJUMPER = 6;
    public final static int SHEET_PLAYER_STANDING = 7;
    public final static int SHEET_PLAYER_FALLING = 8;
    public final static int SHEET_PLAYER_FLYING = 9;
    public final static int SHEET_PLAYER_WALKING = 10;
    public final static int SHEET_PLAYER_CLIMBING = 11;
    public final static int SHEET_PLAYER_FIRING = 12;
    public final static int SHEET_PLAYER_DYING = 13;
    public final static int SHEET_BREAKABLE_DIRT = 14;
    public final static int SHEET_BREAKABLE_SAND = 15;
    public final static int SHEET_BREAKABLE_QUICKBREAK = 16;
    public final static int SHEET_PLAYER_WON = 17;
    public final static int SHEET_BREAKABLE_BRICKS = 18;
    public final static int SHEET_PLAYER_EXPLODING = 19;
    public final static int SHEET_FLYINGJET = 20;
    // IMAGES
    public final static int IMAGE_ROBOT1_FALLING = 1;
    public final static int IMAGE_ROBOT1_CLIMBING = 2;
    public final static int IMAGE_PLAYER_JUMP = 3;
    public final static int IMAGE_FIREBALL = 4;
    // 4 empty
    public final static int IMAGE_ENTERING_LEVEL_BOX = 5;
    public final static int IMAGE_ENTERING_LEVEL_DOOR = 6;
    // MAPDATA
    public static final int MAP_Z_STARTENTITIES = 5;
    public static final int MAP_Z_PLACEOVER_FRONT = 4;
    public static final int MAP_Z_PLACEOVER_NORMAL = 3;
    public static final int MAP_Z_FRONT = 2;
    public static final int MAP_Z_NORMAL = 1;
    public static final int MAP_Z_BACK = 0;
    public static final String SAVE_PATH = System.getProperty("user.home") + "/.iHateRobots/";
    public static final String MAP_SAVE_PATH = SAVE_PATH + "maps/";
    public static final String MAP_EXT = ".map";
    public static final String MAP_ROTATION_EXT = ".rot";
    public final static int MAP_DATA_VERSION = 1;
    public final static int MAP_ROTATION_VERSION = 1;
    public final static String GAME_STATUS_FILENAME = "status.xml";
    public final static String GAME_OPTIONS_FILENAME = "options.xml";
    public final static String HIGHSCORE_FILENAME = "highscores.xml";

    // METHODS
    public static int getTilePosZ(int tileId) {
        if (tileId < 0) {
            return -1;
        }
        switch (tileId) {
            case Utils.TILE_ID_BACK1:
            case Utils.TILE_ID_BACK_ROCK:
            case Utils.TILE_ID_BACK_DIRT:
            case Utils.TILE_ID_BACK_SAND:
                return MAP_Z_BACK;
            case Utils.TILE_ID_COLUMN:
            case Utils.TILE_ID_COLUMN_BOT:
            case Utils.TILE_ID_COLUMN_MID:
            case Utils.TILE_ID_COLUMN_TOP:
            case Utils.TILE_ID_COLUMN_WOOD:
            case Utils.TILE_ID_BARRIER:
                return MAP_Z_FRONT;
            case Utils.TILE_ID_ROBOT1:
            case Utils.TILE_ID_ROBOT2:
            case Utils.TILE_ID_ROCKET:
            case Utils.TILE_ID_LADDER_PIMP:
            case Utils.TILE_ID_VERT_JUMPER:
            case Utils.TILE_ID_DIGGER:
            case Utils.TILE_ID_FIRE_BALL:
            case Utils.TILE_ID_FLYINGJET:
            case Utils.TILE_ID_PLAYER:
                return MAP_Z_STARTENTITIES;
            case Utils.TILE_ID_PO_TOPLEFT:
            case Utils.TILE_ID_PO_TOPRIGHT:
            case Utils.TILE_ID_PO_BOTLEFT:
            case Utils.TILE_ID_PO_BOTRIGHT:
            case Utils.TILE_ID_PO_NOTTOPLEFT:
            case Utils.TILE_ID_PO_NOTTOPRIGHT:
            case Utils.TILE_ID_PO_NOTBOTLEFT:
            case Utils.TILE_ID_PO_NOTBOTRIGHT:
            case Utils.TILE_ID_PO_TOPLEFTRIGHT:
            case Utils.TILE_ID_PO_RIGHTTOPBOT:
            case Utils.TILE_ID_PO_BOTLEFTRIGHT:
            case Utils.TILE_ID_PO_LEFTTOPBOT:
            case Utils.TILE_ID_PO_BALL:
            case Utils.TILE_ID_PO_GRASS:
            case Utils.TILE_ID_PO_ICE:
            case Utils.TILE_ID_PO_ROLL_LEFT:
            case Utils.TILE_ID_PO_ROLL_RIGHT:
            case Utils.TILE_ID_PO_ROCK_ALL:
            case Utils.TILE_ID_PO_ROCK_BOTLEFT:
            case Utils.TILE_ID_PO_ROCK_BOTRIGHT:
            case Utils.TILE_ID_PO_ROCK_DOWN:
            case Utils.TILE_ID_PO_ROCK_LEFT:
            case Utils.TILE_ID_PO_ROCK_NOTDOWN:
            case Utils.TILE_ID_PO_ROCK_NOTLEFT:
            case Utils.TILE_ID_PO_ROCK_NOTRIGHT:
            case Utils.TILE_ID_PO_ROCK_NOTTOP:
            case Utils.TILE_ID_PO_ROCK_RIGHT:
            case Utils.TILE_ID_PO_ROCK_TOP:
            case Utils.TILE_ID_PO_ROCK_TOPLEFT:
            case Utils.TILE_ID_PO_ROCK_TOPRIGHT:
            case Utils.TILE_ID_PO_ROCK_TOPBOT:
            case Utils.TILE_ID_PO_ROCK_LEFTRIGHT:
                return MAP_Z_PLACEOVER_NORMAL;
            case Utils.TILE_ID_PO_WINDOW:
            case Utils.TILE_ID_PO_FRONT:
                return MAP_Z_PLACEOVER_FRONT;
        }
        return MAP_Z_NORMAL;
    }

    public static int getBreakableTileTimeout(int tileId) {
        switch (tileId) {
            case Utils.TILE_ID_DIRT:
                return 1500;
            case Utils.TILE_ID_SAND:
                return 500;
            case Utils.TILE_ID_BRICKS:
                return 1000;
            case Utils.TILE_ID_QUICK_BREAK:
            case Utils.TILE_ID_WOODCRATE:
                return 100;
        }
        return -1;
    }

    public static void writeXML(Object object, FileOutputStream fos) throws Exception {
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        XMLEncoder encoder = new XMLEncoder(bos);
        encoder.writeObject(object);
        encoder.close();
        fos.close();
    }

    public static Object loadXML(InputStream fis) throws Exception {
        BufferedInputStream bis = new BufferedInputStream(fis);
        XMLDecoder decoder = new XMLDecoder(bis, null,
                e -> {
                    throw new RuntimeException(e);
                },
                Utils.class.getClassLoader());
        Object data = decoder.readObject();
        decoder.close();
        return data;
    }

    public static boolean saveMap(MapData data) {
        String fileName = Utils.MAP_SAVE_PATH + data.getName() + Utils.MAP_EXT;
        try {
            File file = new File(fileName);
            FileOutputStream fos = new FileOutputStream(file);
            writeXML(data, fos);
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    public static MapData loadMap(InputStream fis) {
        MapData data = null;
        try {
            data = (MapData) loadXML(fis);
        } catch (Exception ex) {
            return null;
        }
        return data;
    }

    public static boolean saveMapRotation(MapRotation rotation) {
        String fileName = Utils.MAP_SAVE_PATH + rotation.getName() + Utils.MAP_ROTATION_EXT;
        try {
            File file = new File(fileName);
            FileOutputStream fos = new FileOutputStream(file);
            writeXML(rotation, fos);
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    public static MapRotation loadMapRotation(InputStream fis) {
        MapRotation rot;
        try {
            rot = (MapRotation) loadXML(fis);
        } catch (Exception ex) {
            return null;
        }
        return rot;
    }

    public static String timeToString(long millisec) {
        long sec = (long) Math.floor(millisec / 1000f);
        int min = (int) Math.floor(sec / 60);
        sec = sec % 60;
        return min + " : " + (sec < 10 ? "0" : "") + sec;
    }

    public static class LoadRotationCallable implements Callable<List<String>> {
        @Override
        public List<String> call() throws Exception {
            File path = new File(Utils.MAP_SAVE_PATH);
            if (!path.exists()) {
                return null;
            }
            List<String> files = new ArrayList<String>();
            for (String file : path.list()) {
                if (file.endsWith(Utils.MAP_ROTATION_EXT)) {
                    files.add(file.replace(Utils.MAP_ROTATION_EXT, ""));
                }
            }
            return files;
        }
    }

    public static void buildTextPopup(InputStream textInputStream, String id, Nifty nifty, final String backMethod) {
        int lines = 0;
        int maxWidth = 0;

        String newLine = System.getProperty("line.separator");
        final StringBuilder stringBuilder = new StringBuilder(newLine);
        Scanner scanner = new Scanner(textInputStream);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            stringBuilder.append(line).append(newLine);
            lines++;
            maxWidth = Math.max(maxWidth, line.length());
        }
        scanner.close();
        final int width = (int) ((float) maxWidth * 5.5f);
        final int height = lines * 17;

        PopupBuilder builder = new PopupBuilder(id) {
            {
                childLayoutCenter();
                panel(new PanelBuilder() {
                    {
                        style("nifty-panel-red");
                        childLayoutVertical();
                        alignCenter();
                        valignCenter();
                        width(pixels(width + 60));
                        height(pixels(300));
                        control(new ScrollPanelBuilder("scroolpanel#id") {
                            {
                                alignCenter();
                                valignCenter();
                                childLayoutVertical();
                                width(pixels(width + 40));
                                height(pixels(240));
                                parameter("horizontal", "false");
                                parameter("autoScroll", "off");
                                panel(new PanelBuilder() {
                                    {
                                        width(pixels(width + 20));
                                        height(pixels(height + 20));
                                        paddingTop(pixels(10));
                                        childLayoutVertical();
                                        text(new TextBuilder() {
                                            {
                                                text(stringBuilder.toString());
                                                width(percentage(100));
                                                height(pixels(height));
                                                valignTop();
                                                textHAlignCenter();
                                                textVAlignTop();
                                                style("nifty-label");
                                            }
                                        });
                                    }
                                });
                            }
                        });
                        control(new ButtonBuilder("backButton#id") {
                            {
                                label("Back");
                                alignCenter();
                                interactOnClick(backMethod);
                            }
                        });
                    }
                });
            }
        };
        builder.visibleToMouse();
        builder.registerPopup(nifty);
    }


    public static String pixels(int v) {
        return Integer.toString(v) + "px";
    }

    public static String percentage(int v) {
        return Integer.toString(v) + "%";
    }
}
