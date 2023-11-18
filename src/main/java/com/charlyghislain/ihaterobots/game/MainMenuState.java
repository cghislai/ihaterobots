/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.charlyghislain.ihaterobots.game;

import com.charlyghislain.ihaterobots.game.env.GameEnv;
import com.charlyghislain.ihaterobots.game.serializables.MapData;
import com.charlyghislain.ihaterobots.ui.*;
import de.lessvoid.nifty.controls.Button;
import de.lessvoid.nifty.controls.FocusHandler;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.input.NiftyInputMapping;
import de.lessvoid.nifty.input.NiftyStandardInputEvent;
import de.lessvoid.nifty.input.keyboard.KeyboardInputEvent;
import de.lessvoid.nifty.screen.KeyInputHandler;
import de.lessvoid.nifty.screen.Screen;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author cghislai
 */
public class MainMenuState extends MyNiftyOverlayState {

    private final GameEnv env;
    private final MainMenuController mainMenuController;
    private final PlayMenuController playMenuController;
    private final OptionsMenuController optionsMenuController;
    private int fromStateId;

    public MainMenuState(GameEnv env) {
        mainMenuController = new MainMenuController(env);
        playMenuController = new PlayMenuController(env);
        optionsMenuController = new OptionsMenuController(env, this);
        this.env = env;
    }

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        try {
            Files.createDirectories(Paths.get(Utils.MAP_SAVE_PATH));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void initNiftyStuff() {
        initNifty();
        loadXml("assets/ui/mainMenu.xml", UIConstants.SCREEN_MAIN_MENU, mainMenuController, playMenuController, optionsMenuController);
        gotoScreenIfNotActive(UIConstants.SCREEN_MAIN_MENU);
        initInput();
    }

    @Override
    public void update(GameContainer arg0, StateBasedGame arg1, int arg2) throws SlickException {
        if (!env.getGameManager().isGamePaused()) {
            env.getUpdatableManager().update(arg2);
        }
        super.update(arg0, arg1, arg2);
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        env.getDrawableManager().draw(g);
        super.render(container, game, g);
    }

    @Override
    public boolean processMouseEvent(int mouseX, int mouseY, int mouseWheel, int button, boolean buttonDown) {
        return false;
    }

    @Override
    public boolean processKeyboardEvent(KeyboardInputEvent keyEvent) {
        if (optionsMenuController.isWaitingInput() && keyEvent.isKeyDown()) {
            optionsMenuController.keyPressed(keyEvent.getKey());
        }
        if (keyEvent.isKeyDown() && keyEvent.getKey() == Input.KEY_ESCAPE) {
            showOverlay(true);
            return true;
        }
        return false;
    }

    @Override
    public void enter(GameContainer container, StateBasedGame game) throws SlickException {
        super.enter(container, game);
        env.getSoundManager().resumeMusic();
        Button resumeButton = getNifty().getScreen(UIConstants.SCREEN_MAIN_MENU).findNiftyControl(UIConstants.BUTTON_RESUME, Button.class);
        if (env.getGameManager().isGameRunning()) {
            resumeButton.getElement().setVisible(true);
            return;
        }
        resumeButton.getElement().setVisible(false);
        env.getGameManager().prepareNewGame();
        String path = "assets/map/ihaterobots.map";
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(path)) {
            MapData map = Utils.loadMap(is);
            env.getGameManager().setMapData(map);
            env.getGameManager().startMap();
            env.getGameManager().setGamePaused(false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void initInput() {
        final Screen playScreen = getNifty().getScreen(UIConstants.SCREEN_PLAY_MENU);
        NiftyInputMapping mapping = new NiftyInputMapping() {

            @Override
            public NiftyInputEvent convert(KeyboardInputEvent inputEvent) {
                if (!inputEvent.isKeyDown()) {
                    return null;
                }
                if (inputEvent.getKey() == KeyboardInputEvent.KEY_ESCAPE) {
                    return NiftyStandardInputEvent.Escape;
                }
                if (inputEvent.getKey() == KeyboardInputEvent.KEY_UP) {
                    return NiftyStandardInputEvent.MoveCursorUp;
                }
                if (inputEvent.getKey() == KeyboardInputEvent.KEY_DOWN) {
                    return NiftyStandardInputEvent.MoveCursorDown;
                }
                return null;
            }
        };
        KeyInputHandler mainInputHandler = new KeyInputHandler() {

            @Override
            public boolean keyEvent(NiftyInputEvent inputEvent) {
                if (inputEvent == NiftyStandardInputEvent.Escape) {
                    if (env.getGameManager().isGameRunning()) {
                        env.getGame().enterState(Utils.STATE_ID_INGAME);
                        return true;
                    }
                    showOverlay(false);
                    return true;
                }
                return false;
            }
        };
        KeyInputHandler playScreenInputHandler = new KeyInputHandler() {

            @Override
            public boolean keyEvent(NiftyInputEvent inputEvent) {
                if (inputEvent == NiftyStandardInputEvent.Escape) {
                    getNifty().gotoScreen(UIConstants.SCREEN_MAIN_MENU);
                    return true;
                }
                if (inputEvent == NiftyStandardInputEvent.MoveCursorDown) {
                    focusNextElement(playScreen);
                    return true;
                }
                if (inputEvent == NiftyStandardInputEvent.MoveCursorUp) {
                    focuPrevElement(playScreen);
                    return true;
                }
                return false;
            }
        };
        getNifty().getScreen(UIConstants.SCREEN_MAIN_MENU).addKeyboardInputHandler(mapping, mainInputHandler);
        playScreen.addKeyboardInputHandler(mapping, playScreenInputHandler);

    }

    private void focusNextElement(Screen playScreen) {
        final FocusHandler focusHandler = playScreen.getFocusHandler();
        Element element = focusHandler.getKeyboardFocusElement();
        String elementId = null;
        if (element == null) {
            elementId = UIConstants.BUTTON_CONTINUE_GAME;
        } else {
            elementId = element.getId();
        }
        if (elementId.equals(UIConstants.BUTTON_CONTINUE_GAME)) {
            Element next = playScreen.findElementByName(UIConstants.BUTTON_NEW_GAME);
            focusHandler.setKeyFocus(next);
            return;
        }
        if (elementId.equals(UIConstants.BUTTON_NEW_GAME)) {
            Element next = playScreen.findElementByName(UIConstants.DROPDOWN_LEVELSET);
            focusHandler.setKeyFocus(next);
            return;
        }
        if (elementId.equals(UIConstants.DROPDOWN_LEVELSET)) {
            Element next = playScreen.findElementByName(UIConstants.BUTTON_BACK_TO_MAIN);
            focusHandler.setKeyFocus(next);
            return;
        }
    }

    private void focuPrevElement(Screen playScreen) {
        final FocusHandler focusHandler = playScreen.getFocusHandler();
        String elementId = focusHandler.getKeyboardFocusElement().getId();
        if (elementId.equals(UIConstants.BUTTON_BACK_TO_MAIN)) {
            Element next = playScreen.findElementByName(UIConstants.DROPDOWN_LEVELSET);
            focusHandler.setKeyFocus(next);
            return;
        }
        if (elementId.equals(UIConstants.DROPDOWN_LEVELSET)) {
            Element next = playScreen.findElementByName(UIConstants.BUTTON_NEW_GAME);
            focusHandler.setKeyFocus(next);
            return;
        }
        if (elementId.equals(UIConstants.BUTTON_NEW_GAME)) {
            Element next = playScreen.findElementByName(UIConstants.BUTTON_CONTINUE_GAME);
            focusHandler.setKeyFocus(next);
            return;
        }
    }

    @Override
    public int getID() {
        return Utils.STATE_ID_MAINMENU;
    }
}
