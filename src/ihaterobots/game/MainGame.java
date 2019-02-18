/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ihaterobots.game;

import ihaterobots.game.env.GameEnv;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author charly
 */
public class MainGame extends StateBasedGame {

    private GameEnv env;

    public MainGame() {
        super("I hate robots!");
    }

    @Override
    public void initStatesList(GameContainer container) throws SlickException {

        env = new GameEnv(this);
        LoadingState loadingState = new LoadingState(env);
        MainMenuState mainMenuState = new MainMenuState(env);
        MapEditorState mapEditorState = new MapEditorState(env);
        InGameState inGameState = new InGameState(env);
        LevelEntranceState levelEntranceState = new LevelEntranceState(env);
        WaitingInGameState waitingInGameState = new WaitingInGameState(env);
        GameEndState gameEndState = new GameEndState(env);
        GameOverState gameOverState = new GameOverState(env);
        HighscoresState highscoresState = new HighscoresState(env);

        addState(loadingState);
        addState(mainMenuState);
        addState(mapEditorState);
        addState(inGameState);
        addState(levelEntranceState);
        addState(waitingInGameState);
        addState(gameEndState);
        addState(gameOverState);
        addState(highscoresState);
    }
}
