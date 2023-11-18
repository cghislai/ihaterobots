/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.charlyghislain.ihaterobots.game;

import com.charlyghislain.ihaterobots.GameTask;
import com.charlyghislain.ihaterobots.game.env.GameEnv;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author cghislai
 */
public class LoadingState extends BasicGameState {

    private Image image;
    private UnicodeFont font;
    private final GameEnv env;
    private boolean renderedOnce;
    private String text;
    private LoaderTask task;

    public LoadingState(GameEnv env) {
        this.env = env;
        renderedOnce = false;
    }

    @Override
    public int getID() {
        return Utils.STATE_ID_LOADING;
    }

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        image = new Image("assets/img/bigpicture.png");
        font = new UnicodeFont("assets/font/homespun.ttf", "assets/font/homespun.hiero");
        font.addAsciiGlyphs();
        font.addGlyphs(400, 600);
        font.loadGlyphs();
    }

    @Override
    public void enter(GameContainer container, StateBasedGame game) throws SlickException {
//        env.init();
//        task = new LoaderTask(env);
//        task.start();
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        g.drawImage(image, 0, 0);

        g.setFont(font);
        g.setColor(Color.orange);
        if (text != null) {
            g.drawString(text, 70, 560);
        }
        renderedOnce = true;

    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
//        if (task != null && task.isRunning()) {
//            text = (String) task.getValue();
//        }
        if (renderedOnce) {
            env.init();
            MapEditorState mapEditorState = (MapEditorState) game.getState(Utils.STATE_ID_MAPEDITOR);
            mapEditorState.initNiftyStuff();
            MainMenuState mainMenuState = (MainMenuState) game.getState(Utils.STATE_ID_MAINMENU);
            mainMenuState.initNiftyStuff();
            LevelEntranceState entranceState = (LevelEntranceState) game.getState(Utils.STATE_ID_LEVEL_ENTRANCE);
            entranceState.initStuff();
            GameOverState gameOverState = (GameOverState) game.getState(Utils.STATE_ID_GAMEOVER);
            gameOverState.initStuff();
            InGameState inGameState = (InGameState) game.getState(Utils.STATE_ID_INGAME);
            inGameState.initStuff();
            GameEndState newHighscoreState = (GameEndState) game.getState(Utils.STATE_ID_END);
            newHighscoreState.initStuff();

            env.getGameManager().loadOption();
            env.getGameManager().applyOptions();
            env.getGameManager().loadHighScores();
            goToMainMenu();
        }
////        if (!started) {
////            futureTask.run();
////            started = true;
////            return;
////        }
////        try {
////            if (futureTask.isDone() || futureTask.get(100, TimeUnit.MICROSECONDS)) {
////                goToMainMenu();
////                return;
////            }
////        } catch (InterruptedException ex) {
////            Logger.getLogger(LoadingState.class.getName()).log(Level.SEVERE, null, ex);
////        } catch (ExecutionException ex) {
////            Logger.getLogger(LoadingState.class.getName()).log(Level.SEVERE, null, ex);
////        } catch (TimeoutException ex) {
////        }
    }

    private void goToMainMenu() {
        env.getSoundManager().startMusic();
        env.getGame().enterState(Utils.STATE_ID_MAINMENU);
    }

    private class LoaderTask extends GameTask {

        private GameEnv env;

        public LoaderTask(GameEnv env) {
            super("Loading task");
            this.env = env;
        }

        @Override
        public void execute() {
            StateBasedGame game = env.getGame();

            publish("Loading ressources...");
            MapEditorState mapEditorState = (MapEditorState) game.getState(Utils.STATE_ID_MAPEDITOR);
            MainMenuState mainMenuState = (MainMenuState) game.getState(Utils.STATE_ID_MAINMENU);
            LevelEntranceState entranceState = (LevelEntranceState) game.getState(Utils.STATE_ID_LEVEL_ENTRANCE);
            GameOverState gameOverState = (GameOverState) game.getState(Utils.STATE_ID_GAMEOVER);
            InGameState inGameState = (InGameState) game.getState(Utils.STATE_ID_INGAME);
            GameEndState newHighscoreState = (GameEndState) game.getState(Utils.STATE_ID_END);

            publish("Loading UI...");
            mapEditorState.initNiftyStuff();
            mainMenuState.initNiftyStuff();
            entranceState.initStuff();
            gameOverState.initStuff();
            inGameState.initStuff();
            newHighscoreState.initStuff();
            publish("Loading files...");
            env.getGameManager().loadOption();
            env.getGameManager().applyOptions();
            env.getGameManager().loadHighScores();
        }

        @Override
        protected void onSucceeded() {
            goToMainMenu();
        }
    }
}
