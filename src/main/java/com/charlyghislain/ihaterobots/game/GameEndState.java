/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.charlyghislain.ihaterobots.game;

import com.charlyghislain.ihaterobots.game.env.GameEnv;
import com.charlyghislain.ihaterobots.game.serializables.GameStatus;
import com.charlyghislain.ihaterobots.ui.MyNiftyOverlayState;
import com.charlyghislain.ihaterobots.ui.UIConstants;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.builder.TextBuilder;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import de.lessvoid.nifty.controls.textfield.builder.TextFieldBuilder;
import de.lessvoid.nifty.input.keyboard.KeyboardInputEvent;
import de.lessvoid.nifty.loaderv2.NiftyLoader;
import de.lessvoid.nifty.loaderv2.types.NiftyType;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import static com.charlyghislain.ihaterobots.game.Utils.percentage;
import static com.charlyghislain.ihaterobots.game.Utils.pixels;

/**
 * @author charly
 */
public class GameEndState extends MyNiftyOverlayState implements ScreenController {
    private GameEnv env;

    public GameEndState(GameEnv env) {
        this.env = env;
    }

    public void initStuff() {
        initNifty();
        buildScreens();
    }

    public void onOkButtonClicked() {
        TextField field = getNifty().getCurrentScreen().findNiftyControl(UIConstants.TEXTFIELD_HIGHSCORE_PLAYER, TextField.class);
        String playerName = field.getText();
        GameStatus status = env.getGameManager().getStatus();
        env.getGameManager().addHighScore(playerName, status.getGameTime(), status.getLevel(), status.getScore());
        env.getGameManager().saveHighScores();
        env.getGame().enterState(Utils.STATE_ID_HIGHSCORES);
    }

    @Override
    public int getID() {
        return Utils.STATE_ID_END;
    }

    private void buildScreens() {
        NiftyType niftyType = new NiftyType();
        try {
            NiftyLoader niftyLoader = getNifty().getLoader();
            niftyLoader.loadStyleFile("nifty-styles.nxs", "nifty-default-styles.xml", niftyType, getNifty());
            niftyLoader.loadControlFile("nifty-controls.nxs",
                    "nifty-default-controls.xml", niftyType);
            niftyType.create(getNifty(), getNifty().getTimeProvider());
            niftyType.output();
        } catch (Exception e) {
            e.printStackTrace();
        }
        final ScreenController controller = this;

        Screen screen = new ScreenBuilder(UIConstants.SCREEN_NEW_HIGHSCORE) {
            {
                controller(controller);
                layer(new LayerBuilder() {
                    {
                        childLayoutCenter();
                        panel(new PanelBuilder() { //MAIN PANEL
                            {
                                childLayoutVertical();
                                alignCenter();
                                valignCenter();
                                width(pixels(300));
                                height(pixels(200));

                                text(new TextBuilder() {
                                    {
                                        style("nifty-label");
                                        text("Congratulation ! Enter your name for the highscores list");
                                    }
                                });
                                panel(new PanelBuilder() {
                                    {
                                        childLayoutHorizontal();
                                        alignLeft();
                                        width(percentage(100));
                                        control(new TextFieldBuilder(UIConstants.TEXTFIELD_HIGHSCORE_PLAYER) {
                                            {
                                                width(percentage(80));
                                            }
                                        });
                                        control(new ButtonBuilder(UIConstants.BUTTON_HIGHSCORE_OK) {
                                            {
                                                label("OK");
                                                interactOnClick("onOkButtonClicked()");
                                            }
                                        });
                                    }
                                });
                            }
                        });

                    }
                });

            }
        }.build(getNifty());
        getNifty().addScreen(screen.getScreenId(), screen);
        gotoScreenIfNotActive(screen.getScreenId());
    }

    @Override
    public boolean processMouseEvent(int mouseX, int mouseY, int mouseWheel, int button, boolean buttonDown) {
        return false;
    }

    @Override
    public boolean processKeyboardEvent(KeyboardInputEvent keyEvent) {
        return false;
    }

    @Override
    public void bind(Nifty nifty, Screen screen) {
    }

    @Override
    public void onStartScreen() {
    }

    @Override
    public void onEndScreen() {
    }

    @Override
    public void enter(GameContainer container, StateBasedGame game) throws SlickException {
        super.enter(container, game);
        env.getGameManager().saveGameStatus();
        gotoScreenIfNotActive(UIConstants.SCREEN_NEW_HIGHSCORE);
        Screen currentScreen = getNifty().getCurrentScreen();
        if (currentScreen == null) {
            return;
        }
        TextField field = currentScreen.findNiftyControl(UIConstants.TEXTFIELD_HIGHSCORE_PLAYER, TextField.class);
        field.setText("");
    }


}
