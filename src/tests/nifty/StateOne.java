/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tests.nifty;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import de.lessvoid.nifty.controls.label.builder.LabelBuilder;
import de.lessvoid.nifty.input.keyboard.KeyboardInputEvent;
import de.lessvoid.nifty.loaderv2.NiftyLoader;
import de.lessvoid.nifty.loaderv2.types.NiftyType;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import ihaterobots.ui.MyNiftyOverlayState;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 * MyNiftyOverlayState differs from NiftyOverlayState on the following points:
 *  - no xml file is required. void xmlLoader() is absent. subclasses have to
 * manually call setXml and loadXml. It allows subcalsses to initialize themselves
 * before nifty. It also allows subclasses to use the nifty builders
 *  - correctYAxis retuns mouseY (no change)
 * @author charly
 */
public class StateOne extends MyNiftyOverlayState implements ScreenController {
   public static final String MOUSEBUTTONDOWNLABEL = "mouseButtonDownLabel";
   public static final String MOUSEBUTTONLABEL = "mouseButtonLabel";
   public static final String MOUSEYLABEL = "mouseYLabel";
   private static final String MOUSEXLABEL = "mouseXLabel";
   private static final String STATE_NAME = "State 1";
   private Screen screen;
   private int lastPosX;
   private int lastPosY;
   private int lastButton;
   private boolean lastButtonDown;
   private Label mouseXLabel;
   private Label mouseYLabel;
   private Label mouseButtonLabel;
   private Label mouseButtonDownLabel;
   private StateBasedGame game;

   public StateOne(int id) {
      initNifty();
      initNiftyStuff(this);
      mouseXLabel = screen.findNiftyControl(MOUSEXLABEL, Label.class);
      mouseYLabel = screen.findNiftyControl(MOUSEYLABEL, Label.class);
      mouseButtonLabel = screen.findNiftyControl(MOUSEBUTTONLABEL, Label.class);
      mouseButtonDownLabel = screen.findNiftyControl(MOUSEBUTTONDOWNLABEL, Label.class);
   }

   @Override
   public void init(GameContainer container, StateBasedGame game) throws SlickException {
      this.game = game;
   }

   @Override
   public boolean processMouseEvent(int mouseX, int mouseY, int mouseWheel, int button, boolean buttonDown) {
      lastPosX = mouseX;
      lastPosY = mouseY;
      lastButton = button;
      lastButtonDown = buttonDown;
      updateLabels();
      return false;
   }

   @Override
   public int getID() {
      return 0;
   }

   @Override
   public boolean processKeyboardEvent(KeyboardInputEvent keyEvent) {
      return false;
   }

   private void updateLabels() {
      mouseXLabel.setText(Integer.toString(lastPosX));
      mouseYLabel.setText(Integer.toString(lastPosY));
      mouseButtonLabel.setText(Integer.toString(lastButton));
      mouseButtonDownLabel.setText(Boolean.toString(lastButtonDown));
   }

   public void switchState() {
      game.enterState((getID() == 0 ? 1 : 0));
   }

   private void initNiftyStuff(final ScreenController controller) {
      NiftyType niftyType = new NiftyType();


      /**
       *  This stuff is a workaround to have nifty styles and default controls available in the builder 
       **/
      try {
         NiftyLoader niftyLoader = getNifty().getLoader();
         niftyLoader.loadStyleFile("nifty-styles.nxs", "nifty-default-styles.xml", niftyType, getNifty());
         niftyLoader.loadControlFile("nifty-controls.nxs",
                                     "nifty-default-controls.xml", niftyType);
         niftyType.create(getNifty(), getNifty().getTimeProvider());
      } catch (Exception e) {
         e.printStackTrace();
      }

      screen = new ScreenBuilder("mainScreen") {
         {
            controller(controller);
            inputMapping("de.lessvoid.nifty.input.mapping.DefaultScreenMapping");
            layer(new LayerBuilder() {
               {
                  childLayoutAbsolute();
                  panel(new PanelBuilder("mouseInfoPanel") {
                     {
                        x(pixels(20));
                        y(pixels(20));
                        width(pixels(300));
                        height(pixels(200));
                        style("nifty-panel-simple");
                        childLayoutVertical();
                        control(new LabelBuilder() {
                           {
                              text("Last mouse event received :");
                              alignCenter();
                           }
                        });
                        panel(new PanelBuilder() {
                           {
                              childLayoutHorizontal();
                              control(new LabelBuilder() {
                                 {
                                    text("Last X coord:");
                                    width(pixels(100));
                                    alignLeft();
                                    textHAlignLeft();
                                 }
                              });
                              control(new LabelBuilder(MOUSEXLABEL) {
                                 {
                                    text("");
                                    width(pixels(100));
                                    alignLeft();
                                    textHAlignLeft();
                                 }
                              });
                           }
                        });
                        panel(new PanelBuilder() {
                           {
                              childLayoutHorizontal();
                              control(new LabelBuilder() {
                                 {
                                    text("Last Y coord:");
                                    width(pixels(100));
                                    alignLeft();
                                    textHAlignLeft();
                                 }
                              });
                              control(new LabelBuilder(MOUSEYLABEL) {
                                 {
                                    text("");
                                    width(pixels(100));
                                    alignLeft();
                                    textHAlignLeft();
                                 }
                              });
                           }
                        });
                        panel(new PanelBuilder() {
                           {
                              childLayoutHorizontal();
                              control(new LabelBuilder() {
                                 {
                                    text("Event button:");
                                    width(pixels(100));
                                    alignLeft();
                                    textHAlignLeft();
                                 }
                              });
                              control(new LabelBuilder(MOUSEBUTTONLABEL) {
                                 {
                                    text("");
                                    width(pixels(100));
                                    alignLeft();
                                    textHAlignLeft();
                                 }
                              });
                           }
                        });
                        panel(new PanelBuilder() {
                           {
                              childLayoutHorizontal();
                              control(new LabelBuilder() {
                                 {
                                    text("Button down:");
                                    width(pixels(100));
                                    alignLeft();
                                    textHAlignLeft();
                                 }
                              });
                              control(new LabelBuilder(MOUSEBUTTONDOWNLABEL) {
                                 {
                                    text("");
                                    width(pixels(100));
                                    alignLeft();
                                    textHAlignLeft();
                                 }
                              });
                           }
                        });
                     }
                  });

                  panel(new PanelBuilder("stateInfoPanel") {
                     {
                        x(pixels(500));
                        y(pixels(300));
                        width(pixels(200));
                        height(pixels(200));
                        backgroundColor("#880000");
                        childLayoutVertical();
                        control(new LabelBuilder() {
                           {
                              text("This is state " + STATE_NAME);
                              alignCenter();
                           }
                        });
                        control(new ButtonBuilder("switchStateButton") {
                           {
                              label("Next State");
                              interactOnClick("switchState()");
                              alignCenter();
                           }
                        });
                        control(new ButtonBuilder("otherButton") {
                           {
                              label("Switch state");
                              interactOnClick("switchState()");
                              alignCenter();
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
   public void bind(Nifty nifty, Screen screen) {
   }

   @Override
   public void onStartScreen() {
   }

   @Override
   public void onEndScreen() {
   }
}
