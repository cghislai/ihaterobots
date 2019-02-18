/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ihaterobots.game;

import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.input.NiftyInputMapping;
import de.lessvoid.nifty.input.keyboard.KeyboardInputEvent;
import de.lessvoid.nifty.loaderv2.NiftyLoader;
import de.lessvoid.nifty.loaderv2.types.NiftyType;
import de.lessvoid.nifty.screen.KeyInputHandler;
import de.lessvoid.nifty.screen.Screen;
import ihaterobots.game.env.GameEnv;
import ihaterobots.game.map.editor.MapEditor;
import ihaterobots.ui.MapEditorMenuController;
import ihaterobots.ui.MyNiftyOverlayState;
import ihaterobots.ui.TilesSubMenuController;
import ihaterobots.ui.UIConstants;
import ihaterobots.ui.controls.ImageButtonBuilder;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author charly
 */
public class MapEditorState extends MyNiftyOverlayState {

    private final GameEnv env;
    private final MapEditor mapEditor;
    private MapEditorMenuController mapEditorMenuController;
    private TilesSubMenuController rockSubMenuController;
    private final TilesSubMenuController goldSubMenuController;
    private final TilesSubMenuController metalSubMenuController;
    private TilesSubMenuController dirtSubMenuController;
    private TilesSubMenuController brickSubMenuController;
    private TilesSubMenuController sandSubMenuController;
    private int[] placeOverIds;
    private int[] rockPlaceOverIds;
    private int colIdx; //used in loops
    private int tileId; //used in loops
    private String name; //used in loops

    public MapEditorState(GameEnv env) {
        this.env = env;
        mapEditor = new MapEditor(env, this);
        mapEditorMenuController = new MapEditorMenuController(env, mapEditor, this);
        rockSubMenuController = new TilesSubMenuController(Utils.TILE_ID_ROCK, env, mapEditor, this);
        metalSubMenuController = new TilesSubMenuController(Utils.TILE_ID_METAL, env, mapEditor, this);
        goldSubMenuController = new TilesSubMenuController(Utils.TILE_ID_GOLD, env, mapEditor, this);
        dirtSubMenuController = new TilesSubMenuController(Utils.TILE_ID_DIRT, env, mapEditor, this);
        brickSubMenuController = new TilesSubMenuController(Utils.TILE_ID_BRICKS, env, mapEditor, this);
        sandSubMenuController = new TilesSubMenuController(Utils.TILE_ID_SAND, env, mapEditor, this);
    }

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
    }

    public void initNiftyStuff() {
        initNifty();
        loadXml("assets/ui/mapEditor.xml", UIConstants.SCREEN_MAP_EDITOR, mapEditorMenuController);
        fillIdsArrays();
        addSubScreens();
        gotoScreenIfNotActive(UIConstants.SCREEN_MAP_EDITOR);
        initInput();
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        env.getDrawableManager().draw(g);
        super.render(container, game, g);
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        env.getUpdatableManager().update(delta);
        mapEditor.update(delta);
        super.update(container, game, delta);
    }

    @Override
    public boolean processMouseEvent(int mouseX, int mouseY, int mouseWheel, int button, boolean buttonDown) {
        if (isOverlayShown()) {
            return false;
        }
        return mapEditor.processMouseEvent(mouseX, mouseY, mouseWheel, button, buttonDown);
    }

    @Override
    public boolean processKeyboardEvent(KeyboardInputEvent keyEvent) {
        if (keyEvent.isKeyDown() && keyEvent.getKey() == Input.KEY_ESCAPE) {
            showOverlay(!isOverlayShown());
            return true;
        }
        return mapEditor.processKeyboardEvent(keyEvent);
    }

    public void showHideUi() {
        boolean shown = isOverlayShown();
        if (!shown) {
            showOverlay(true);
            gotoScreenIfNotActive(UIConstants.SCREEN_MAP_EDITOR);
            return;
        }
        // go to main scren if not active, otherwise hide ui
        String screenId = getNifty().getCurrentScreen().getScreenId();
        if (!screenId.equals(UIConstants.SCREEN_MAP_EDITOR)) {
            gotoScreenIfNotActive(UIConstants.SCREEN_MAP_EDITOR);
        } else {
            showOverlay(false);
        }
    }

    public void hideUi() {
        showOverlay(false);
    }

    @Override
    public void enter(GameContainer container, StateBasedGame game) throws SlickException {
        super.enter(container, game);
        env.getGameManager().prepareNewGame();
        env.getGameManager().setMapData(mapEditor.getMapData());
        env.getMap().getMapDrawer().setIsInEditState(true);
        mapEditor.register();
    }

    @Override
    public void leave(GameContainer container, StateBasedGame game) throws SlickException {
        super.leave(container, game);
        env.getMap().getMapDrawer().setIsInEditState(false);
        mapEditor.unregister();
    }

    public MapEditorMenuController getMapEditorMenuController() {
        return mapEditorMenuController;
    }

    @Override
    public int getID() {
        return Utils.STATE_ID_MAPEDITOR;
    }

    private void initInput() {
        NiftyInputMapping mapping = new NiftyInputMapping() {

            @Override
            public NiftyInputEvent convert(KeyboardInputEvent inputEvent) {
                if (!inputEvent.isKeyDown()) {
                    return null;
                }
                if (inputEvent.getKey() == KeyboardInputEvent.KEY_ESCAPE) {
                    return NiftyInputEvent.Escape;
                }
                if (inputEvent.getKey() == KeyboardInputEvent.KEY_UP) {
                    return NiftyInputEvent.MoveCursorUp;
                }
                if (inputEvent.getKey() == KeyboardInputEvent.KEY_DOWN) {
                    return NiftyInputEvent.MoveCursorDown;
                }
                return null;
            }
        };

        KeyInputHandler mainMenuHandler = new KeyInputHandler() {

            @Override
            public boolean keyEvent(NiftyInputEvent inputEvent) {
                if (inputEvent == NiftyInputEvent.Escape) {
                    showOverlay(!isOverlayShown());
                    return true;
                }
                return false;
            }
        };

        getNifty().getScreen(UIConstants.SCREEN_MAP_EDITOR).addKeyboardInputHandler(mapping, mainMenuHandler);
    }

    private void fillIdsArrays() {
        placeOverIds = new int[]{
            -1, Utils.TILE_ID_PO_BALL,
            Utils.TILE_ID_PO_WINDOW, Utils.TILE_ID_PO_FRONT,
            Utils.TILE_ID_PO_TOPLEFT, Utils.TILE_ID_PO_TOPRIGHT,
            Utils.TILE_ID_PO_BOTLEFT, Utils.TILE_ID_PO_BOTRIGHT,
            Utils.TILE_ID_PO_TOPLEFTRIGHT, Utils.TILE_ID_PO_RIGHTTOPBOT,
            Utils.TILE_ID_PO_BOTLEFTRIGHT, Utils.TILE_ID_PO_LEFTTOPBOT,
            Utils.TILE_ID_PO_NOTBOTLEFT, Utils.TILE_ID_PO_NOTBOTRIGHT,
            Utils.TILE_ID_PO_NOTTOPLEFT, Utils.TILE_ID_PO_NOTTOPRIGHT,
            Utils.TILE_ID_PO_ROLL_LEFT, Utils.TILE_ID_PO_ROLL_RIGHT,
            Utils.TILE_ID_PO_GRASS, Utils.TILE_ID_PO_ICE
        };
        rockPlaceOverIds = new int[]{
            Utils.TILE_ID_PO_ROCK_TOP, Utils.TILE_ID_PO_ROCK_LEFT,
            Utils.TILE_ID_PO_ROCK_RIGHT, Utils.TILE_ID_PO_ROCK_DOWN,
            Utils.TILE_ID_PO_ROCK_TOPLEFT, Utils.TILE_ID_PO_ROCK_TOPRIGHT,
            Utils.TILE_ID_PO_ROCK_BOTLEFT, Utils.TILE_ID_PO_ROCK_BOTRIGHT,
            Utils.TILE_ID_PO_ROCK_NOTDOWN, Utils.TILE_ID_PO_ROCK_NOTLEFT,
            Utils.TILE_ID_PO_ROCK_NOTRIGHT, Utils.TILE_ID_PO_ROCK_NOTTOP,
            Utils.TILE_ID_PO_ROCK_TOPBOT, Utils.TILE_ID_PO_ROCK_LEFTRIGHT,
            Utils.TILE_ID_PO_ROCK_ALL
        };

        //5*4+4*4 = 9*4 4 lines 9 columns, going vertically
        // normal - ball - window -front // 4 1corners // 4 2 coreners // 4 3coreners // 2ROLLS+ICE+GRASS
        // 4 sieds // 4 corners // 4 3-sides // all
    }

    private void addSubScreens() {
        NiftyType niftyType = new NiftyType();
        try {
            NiftyLoader niftyLoader = getNifty().getLoader();
            niftyLoader.loadStyleFile("nifty-styles.nxs", "nifty-default-styles.xml", niftyType, getNifty());
            niftyLoader.loadStyleFile("nifty-styles.nxs", "assets/ui/style-image-button.xml", niftyType, getNifty());
            niftyLoader.loadControlFile("nifty-controls.nxs",
                    "nifty-default-controls.xml", niftyType);
            niftyLoader.loadControlFile("nifty-controls.nxs",
                    "assets/ui/control-image-button.xml", niftyType);
            niftyType.create(getNifty(), getNifty().getTimeProvider());
            niftyType.output();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Screen rockScreen = buildSubMenuScreen(UIConstants.SCREEN_ROCK_TILES, rockSubMenuController,
                "rock-", 5);
        Screen metalScreen = buildSubMenuScreen(UIConstants.SCREEN_METAL_TILES, metalSubMenuController,
                "metal-", 5);
        Screen goldScreen = buildSubMenuScreen(UIConstants.SCREEN_GOLD_TILES, goldSubMenuController,
                "gold-", 5);
        Screen dirtScreen = buildSubMenuScreen(UIConstants.SCREEN_DIRT_TILES, dirtSubMenuController,
                "dirt-", 9);
        Screen bricksScreen = buildSubMenuScreen(UIConstants.SCREEN_BRICK_TILES, brickSubMenuController,
                "bricks-", 9);
        Screen sandScreen = buildSubMenuScreen(UIConstants.SCREEN_SAND_TILES, sandSubMenuController,
                "sand-", 9);
        getNifty().addScreen(rockScreen.getScreenId(), rockScreen);
        getNifty().addScreen(metalScreen.getScreenId(), metalScreen);
        getNifty().addScreen(goldScreen.getScreenId(), goldScreen);
        getNifty().addScreen(dirtScreen.getScreenId(), dirtScreen);
        getNifty().addScreen(bricksScreen.getScreenId(), bricksScreen);
        getNifty().addScreen(sandScreen.getScreenId(), sandScreen);
    }

    private Screen buildRockMenuScreen() {
        Screen screen = new ScreenBuilder(UIConstants.SCREEN_ROCK_TILES) {

            {
                controller(rockSubMenuController);
                layer(new LayerBuilder() {

                    {
                        childLayoutCenter();
                        panel(new PanelBuilder() { //MAIN PANEL

                            {
                                childLayoutVertical();
                                alignCenter();
                                valignCenter();
                                style("nifty-panel");
                                width(pixels(5 * 40 + 4 * 2 + 32));
//                        height(pixels(4 * 40 + 40));
                                panel(new PanelBuilder() { //VERT PANEL

                                    {
                                        childLayoutHorizontal();
                                        // 5 columns for placeovers
                                        for (colIdx = 0; colIdx < 5; colIdx++) { //FOR COL IDX
                                            panel(new PanelBuilder() {

                                                {
                                                    childLayoutVertical();
                                                    padding(pixels(0));
                                                    // 4 button per column
                                                    for (int j = 0; j < 4; j++) { //FOR ROW IDX
                                                        tileId = placeOverIds[4 * colIdx + j];
                                                        name = "rock-" + tileId;
                                                        control(new ImageButtonBuilder(name) {

                                                            {
                                                                image("assets/img/tile/menu/" + name + ".png");
                                                                width(pixels(40));
                                                                height(pixels(40));
                                                                interactOnClick("onClick(" + tileId + ")");
                                                            }
                                                        });
                                                        panel(new PanelBuilder() {

                                                            {
                                                                height(pixels(2));
                                                            }
                                                        });
                                                    } //END FOR ROW IDX

                                                }
                                            });
                                            panel(new PanelBuilder() {

                                                {
                                                    width(pixels(2));
                                                }
                                            });
                                        } //END FOR COL IDX
                                    }
                                }); //END VERTPANEL
                                control(new ButtonBuilder("backButton") {

                                    {
                                        label("Back");
                                        alignCenter();
                                        interactOnClick("onBackButtonClick()");
                                    }
                                });
                            }
                        }); //END MAIN PANEL
                    }
                });

            }
        }.build(getNifty());
        Element root = screen.getRootElement();
        root.layoutElements();
        return screen;
    }

    private Screen buildSubMenuScreen(String screenId, final TilesSubMenuController controller,
            final String fileSubName, final int columns) {
        Screen screen = new ScreenBuilder(screenId) {

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
                                style("nifty-panel");
                                width(pixels(columns * 40 + 4 * 2 + 32));
//                        height(pixels(4 * 40 + 40));
                                panel(new PanelBuilder() { //VERT PANEL

                                    {
                                        childLayoutHorizontal();
                                        // 5 columns for placeovers
                                        for (colIdx = 0; colIdx < columns; colIdx++) { //FOR COL IDX
                                            panel(new PanelBuilder() {

                                                {
                                                    childLayoutVertical();
                                                    padding(pixels(0));
                                                    // 4 button per column
                                                    for (int j = 0; j < 4; j++) { //FOR ROW IDX
                                                        if (colIdx == 8 && j == 3) {
                                                            break; // only 15 buttons in the last
                                                        }
                                                        int cutileid = -1;
                                                        if (colIdx <= 4) {
                                                            cutileid = placeOverIds[4 * colIdx + j];
                                                        } else {
                                                            cutileid = rockPlaceOverIds[4 * (colIdx - 5) + j];
                                                        }
                                                        final int tileId = cutileid;
                                                        name = fileSubName + tileId;
                                                        control(new ImageButtonBuilder(name) {

                                                            {
                                                                image("assets/img/tile/menu/" + name + ".png");
                                                                width(pixels(40));
                                                                height(pixels(40));
                                                                interactOnClick("onClick(" + tileId + ")");
                                                            }
                                                        });
                                                        panel(new PanelBuilder() {

                                                            {
                                                                height(pixels(2));
                                                            }
                                                        });
                                                    } //END FOR ROW IDX

                                                }
                                            });
                                            panel(new PanelBuilder() {

                                                {
                                                    width(pixels(2));
                                                }
                                            });
                                        } //END FOR COL IDX
                                    }
                                }); //END VERTPANEL
                                control(new ButtonBuilder("backButton") {

                                    {
                                        label("Back");
                                        alignCenter();
                                        interactOnClick("onBackButtonClick()");
                                    }
                                });
                            }
                        }); //END MAIN PANEL
                    }
                });

            }
        }.build(getNifty());
        Element root = screen.getRootElement();
        root.layoutElements();
        return screen;
    }

    private Screen buildDirtMenuScreen() {
        Screen screen = new ScreenBuilder(UIConstants.SCREEN_DIRT_TILES) {

            {
                controller(dirtSubMenuController);
                layer(new LayerBuilder() {

                    {
                        childLayoutCenter();
                        panel(new PanelBuilder() { //MAIN PANEL

                            {
                                childLayoutVertical();
                                alignCenter();
                                valignCenter();
                                style("nifty-panel");
                                width(pixels(9 * 40 + 9 * 2 + 32));
//                        height(pixels(4 * 40 + 40));
                                panel(new PanelBuilder() { //VERT PANEL

                                    {
                                        childLayoutHorizontal();
                                        // 5 columns for placeovers
                                        for (colIdx = 0; colIdx < 9; colIdx++) { //FOR COL IDX PLACEOVERS
                                            panel(new PanelBuilder() {

                                                {
                                                    childLayoutVertical();
                                                    padding(pixels(0));
                                                    // 4 button per column
                                                    for (int j = 0; j < 4; j++) { //FOR ROW IDX
                                                        if (colIdx == 8 && j == 3) {
                                                            break; // only 15 buttons in the last
                                                        }
                                                        if (colIdx <= 4) {
                                                            tileId = placeOverIds[4 * colIdx + j];
                                                        } else {
                                                            tileId = rockPlaceOverIds[4 * (colIdx - 5) + j];
                                                        }
                                                        name = "dirt-" + tileId;
                                                        control(new ImageButtonBuilder(name) {

                                                            {
                                                                image("assets/img/tile/menu/" + name + ".png");
                                                                width(pixels(40));
                                                                height(pixels(40));
                                                                interactOnClick("onClick(" + tileId + ")");
                                                            }
                                                        });
                                                        panel(new PanelBuilder() {

                                                            {
                                                                height(pixels(2));
                                                            }
                                                        });
                                                    } //END FOR ROW IDX

                                                }
                                            });
                                            panel(new PanelBuilder() {

                                                {
                                                    width(pixels(2));
                                                }
                                            });
                                        } //END FOR COL IDX PLACEOVERS
                                    }
                                }); //END VERTPANEL
                                control(new ButtonBuilder("backButton") {

                                    {
                                        label("Back");
                                        alignCenter();
                                        interactOnClick("onBackButtonClick()");
                                    }
                                });
                            }
                        }); //END MAIN PANEL
                    }
                });

            }
        }.build(getNifty());
        Element root = screen.getRootElement();
        root.layoutElements();
        return screen;
    }

    private Screen buildBrickMenuScreen() {
        Screen screen = new ScreenBuilder(UIConstants.SCREEN_BRICK_TILES) {

            {
                controller(brickSubMenuController);
                layer(new LayerBuilder() {

                    {
                        childLayoutCenter();
                        panel(new PanelBuilder() { //MAIN PANEL

                            {
                                childLayoutVertical();
                                alignCenter();
                                valignCenter();
                                style("nifty-panel");
                                width(pixels(9 * 40 + 9 * 2 + 32));
//                        height(pixels(4 * 40 + 40));
                                panel(new PanelBuilder() { //VERT PANEL

                                    {
                                        childLayoutHorizontal();
                                        // 5 columns for placeovers
                                        for (colIdx = 0; colIdx < 9; colIdx++) { //FOR COL IDX PLACEOVERS
                                            panel(new PanelBuilder() {

                                                {
                                                    childLayoutVertical();
                                                    padding(pixels(0));
                                                    // 4 button per column
                                                    for (int j = 0; j < 4; j++) { //FOR ROW IDX
                                                        if (colIdx == 8 && j == 3) {
                                                            break; // only 15 buttons in the last
                                                        }
                                                        if (colIdx <= 4) {
                                                            tileId = placeOverIds[4 * colIdx + j];
                                                        } else {
                                                            tileId = rockPlaceOverIds[4 * (colIdx - 5) + j];
                                                        }
                                                        name = "bricks-" + tileId;
                                                        control(new ImageButtonBuilder(name) {

                                                            {
                                                                image("assets/img/tile/menu/" + name + ".png");
                                                                width(pixels(40));
                                                                height(pixels(40));
                                                                interactOnClick("onClick(" + tileId + ")");
                                                            }
                                                        });
                                                        panel(new PanelBuilder() {

                                                            {
                                                                height(pixels(2));
                                                            }
                                                        });
                                                    } //END FOR ROW IDX

                                                }
                                            });
                                            panel(new PanelBuilder() {

                                                {
                                                    width(pixels(2));
                                                }
                                            });
                                        } //END FOR COL IDX PLACEOVERS
                                    }
                                }); //END VERTPANEL
                                control(new ButtonBuilder("backButton") {

                                    {
                                        label("Back");
                                        alignCenter();
                                        interactOnClick("onBackButtonClick()");
                                    }
                                });
                            }
                        }); //END MAIN PANEL
                    }
                });

            }
        }.build(getNifty());
        Element root = screen.getRootElement();
        root.layoutElements();
        return screen;
    }

    private Screen buildSandMenuScreen() {
        Screen screen = new ScreenBuilder(UIConstants.SCREEN_SAND_TILES) {

            {
                controller(sandSubMenuController);
                layer(new LayerBuilder() {

                    {
                        childLayoutCenter();
                        panel(new PanelBuilder() { //MAIN PANEL

                            {
                                childLayoutVertical();
                                alignCenter();
                                valignCenter();
                                style("nifty-panel");
                                width(pixels(9 * 40 + 9 * 2 + 32));
//                        height(pixels(4 * 40 + 40));
                                panel(new PanelBuilder() { //VERT PANEL

                                    {
                                        childLayoutHorizontal();
                                        // 5 columns for placeovers
                                        for (colIdx = 0; colIdx < 9; colIdx++) { //FOR COL IDX PLACEOVERS
                                            panel(new PanelBuilder() {

                                                {
                                                    childLayoutVertical();
                                                    padding(pixels(0));
                                                    // 4 button per column
                                                    for (int j = 0; j < 4; j++) { //FOR ROW IDX
                                                        if (colIdx == 8 && j == 3) {
                                                            break; // only 15 buttons in the last
                                                        }
                                                        if (colIdx <= 4) {
                                                            tileId = placeOverIds[4 * colIdx + j];
                                                        } else {
                                                            tileId = rockPlaceOverIds[4 * (colIdx - 5) + j];
                                                        }
                                                        name = "sand-" + tileId;
                                                        control(new ImageButtonBuilder(name) {

                                                            {
                                                                image("assets/img/tile/menu/" + name + ".png");
                                                                width(pixels(40));
                                                                height(pixels(40));
                                                                interactOnClick("onClick(" + tileId + ")");
                                                            }
                                                        });
                                                        panel(new PanelBuilder() {

                                                            {
                                                                height(pixels(2));
                                                            }
                                                        });
                                                    } //END FOR ROW IDX

                                                }
                                            });
                                            panel(new PanelBuilder() {

                                                {
                                                    width(pixels(2));
                                                }
                                            });
                                        } //END FOR COL IDX PLACEOVERS
                                    }
                                }); //END VERTPANEL
                                control(new ButtonBuilder("backButton") {

                                    {
                                        label("Back");
                                        alignCenter();
                                        interactOnClick("onBackButtonClick()");
                                    }
                                });
                            }
                        }); //END MAIN PANEL
                    }
                });

            }
        }.build(getNifty());
        Element root = screen.getRootElement();
        root.layoutElements();
        return screen;
    }
}
