package main_classes;

import java.util.Random;

import javafx.animation.Animation;
import javafx.animation.FillTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * @Class GameMenu Class draws animated background, main and sub menu,
 *     define button actions. 
 */

@SuppressWarnings("deprecation")
public class GameMenu extends Application implements GameMenuConstants {
  
  private Pane root;
  private StackPane circles;
  private Scene scenario;
  private Circle but_circ;
  private Rectangle rect_background;
  private Thread[] circleThread;
  private FillTransition circAnim;
  
  private Label tipLbl;
  private MenuItem newGame, settings, gameRules, replays, exit, 
    sound, optionBack;
  private SubMenu mainMenu, optionMenu;
  private MenuBox menuBox;

  /**
   * initializate_block define main parameters of class fields
   * 
   */
  
  {
    root = new StackPane();

    circles = new StackPane();
    circles.setStyle(CIRCLE_STYLE);

    rect_background = new Rectangle();
    rect_background.setFill(Color.DARKSLATEBLUE);

    but_circ = new Circle();
    but_circ.setTranslateX(CIRCLE_BUTTON_X);
    but_circ.setTranslateY(CIRCLE_BUTTON_Y);
    but_circ.setRadius(CIRCLE_BUTTON_RADIUS);
    but_circ.setFill(Color.PURPLE);
    but_circ.setOpacity(CIRCLE_BUTTON_OPACITY);
    
    circAnim = new FillTransition(Duration.
        seconds(BUTTON_ANIM_DURACTION), but_circ);
   
    tipLbl = new Label(CIRCLE_BUTTON_TEXT);
    tipLbl.setTranslateX(CIRCLE_BUTTON_X);
    tipLbl.setTranslateY(CIRCLE_BUTTON_Y);
    tipLbl.setTextFill(Color.AQUA);
    tipLbl.setTextAlignment(TextAlignment.CENTER);
    
    circleThread = new Thread[CIRCLES_COUNT];
    
    newGame = new MenuItem(NEW_GAME);
    settings = new MenuItem(SETTINGS);
    gameRules = new MenuItem(GAME_RULES);
    replays = new MenuItem(REPLAYS);
    exit = new MenuItem(EXIT);
    sound = new MenuItem(SOUND);
    optionBack = new MenuItem(BACK);

    mainMenu = new SubMenu(newGame, settings, replays, gameRules, exit);
    optionMenu = new SubMenu(sound, optionBack);

    menuBox = new MenuBox(mainMenu);
    menuBox.setVisible(true);
  }

  {
    newGame.setOnMouseClicked(event -> 
      SetParamsToMainApp.setParams(SET_PARAMS_TITLE));
    settings.setOnMouseClicked(event -> 
      menuBox.setSubMenu(optionMenu));
    gameRules.setOnMouseClicked(event -> 
      GameRules.showGameRules(GAME_RULES_TITLE));
    optionBack.setOnMouseClicked(event -> 
      menuBox.setSubMenu(mainMenu));
    replays.setOnMouseClicked(event -> 
      ReplaysWindow.showReplayList(REPLAYS_TITLE));
    exit.setOnMouseClicked(event -> {
      for (Thread thread : circleThread) {
        try {
          thread.stop();
        }
        catch (IllegalThreadStateException e) {
          e.printStackTrace();
        }
        catch(SecurityException e) {
          e.printStackTrace();
        }
      }
      System.exit(0);
    });
  }

  /**
   * @Method This method determine set scene and stage and show it
   * 
   */
  
  public void start(Stage primaryStage) throws Exception {

    setAnimatedBackground();

    scenario = new Scene(root, SCENE_SIZE, SCENE_SIZE);

    rect_background.widthProperty().bind(this.scenario.widthProperty());
    rect_background.heightProperty().bind(this.scenario.heightProperty());
    
    setCirclesConfiguration();
    
    primaryStage.setTitle(MENU_TITLE);
    primaryStage.setScene(scenario);
    primaryStage.setResizable(false);
    primaryStage.show();
  }

  /**
   * @Method This method set animated background on the scene
   * 
   */
  
  private void setAnimatedBackground() {    
    setCirclesAnimation();

    root.getChildren().addAll(rect_background, circles, menuBox, tipLbl);

    but_circ.setOnMouseClicked(event->{
      setCirclesConfiguration();
    });
    but_circ.setOnMouseEntered(event -> {
      circAnim.setFromValue(Color.WHITE);
      circAnim.setToValue(Color.PURPLE);
      circAnim.setCycleCount(Animation.INDEFINITE);
      circAnim.setAutoReverse(true);
      circAnim.play();
    });
    but_circ.setOnMouseExited(event -> {
      circAnim.stop();
      but_circ.setFill(Color.PURPLE);
    });
    
    root.getChildren().add(but_circ);
  }
  
  /**
   * @Method This method determine algorithm of circles animation,
   * create threads for it
   * 
   */
  
  private void setCirclesAnimation() {
    for (int i = 0; i < CIRCLES_COUNT; i++) {
      Circle circle = new Circle();
      circle.setFill(Color.WHITE);
      circle.setEffect(new GaussianBlur(Math.random() * 8 + 2));
      circle.setOpacity(Math.random());
      circle.setRadius(CIRCLES_RADIUS);
      
      circles.getChildren().add(circle);

      double randScale = (Math.random() * 4) + 1;
      
      try {
        circleThread[i] = new Thread() {
          public void run() {
            KeyValue kValueX = new KeyValue(circle.scaleXProperty(), randScale);
            KeyValue kValueY = new KeyValue(circle.scaleYProperty(), randScale);
            KeyFrame kFrame = new KeyFrame(Duration.millis(CIRCLES_DURATION + 
                  (Math.random() * CIRCLES_DURATION)), kValueX, kValueY);
                        Timeline timeL = new Timeline();
            timeL.getKeyFrames().add(kFrame);
            timeL.setAutoReverse(true);
            timeL.setCycleCount(Animation.INDEFINITE);
            timeL.play();          
          }
        };
        circleThread[i].start();
      }
      catch (IllegalThreadStateException e) {
        e.printStackTrace();
      }
      catch(SecurityException e) {
        e.printStackTrace();
      }
    }
  }
  
  /**
   * @Method This method change circles positions
   * 
   */
  
  private void setCirclesConfiguration()
  {
    Random random = new Random();
    for (Node circle : circles.getChildren()) {
      circle.setTranslateX(random.nextInt((int)
          scenario.getWidth()) - scenario.getWidth() / 2);
      circle.setTranslateY(random.nextInt((int)
          scenario.getHeight()) - scenario.getHeight() / 2);
    }
  }

  /**
   * @Class MenuItem Class create item of the main and sub menus,
   * set animation on buttons and mouse entering/exiting actions 
   */
  
  private static class MenuItem extends StackPane {
    private static final int BUTTON_WIDTH = 400;
    private static final int BUTTON_HEIGHT = 40;
    private static final int FONT_SIZE = 18;
    private static final double BUTTON_OPACITY = 0.5;
    private static final double BUTTON_ANIM_DURACTION = 0.5;

    private static final String FONT = "Arial";

    private Rectangle bd;
    private Text text;
    private FillTransition st;

    {
      bd = new Rectangle(BUTTON_WIDTH, BUTTON_HEIGHT, Color.WHITE);
      bd.setOpacity(BUTTON_OPACITY);

      st = new FillTransition(Duration.seconds(BUTTON_ANIM_DURACTION), bd);
    }

    {
      this.setOnMouseEntered(event -> {
        st.setFromValue(Color.WHITE);
        st.setToValue(Color.PURPLE);
        st.setCycleCount(Animation.INDEFINITE);
        st.setAutoReverse(true);
        st.play();
      });
      this.setOnMouseExited(event -> {
        st.stop();
        bd.setFill(Color.WHITE);
      });
    }

    /**
     * @param name - text on the button
     */
    
    public MenuItem(String name) {
      text = new Text(name);
      text.setFill(Color.WHITE);
      text.setFont(Font.font(FONT, FontWeight.BOLD, FONT_SIZE));

      setAlignment(Pos.CENTER);
      getChildren().addAll(bd, text);
    }
  }

  /**
   * @Class MenuBox Class create MenuItem massive in one block
   */
  
  private static class MenuBox extends Pane {
    private static SubMenu submenu;

    public MenuBox(SubMenu submenu) {
      MenuBox.submenu = submenu;

      setVisible(false);
      getChildren().addAll(submenu);
    }

    public void setSubMenu(SubMenu submenu) {
      getChildren().remove(MenuBox.submenu);
      MenuBox.submenu = submenu;
      getChildren().add(MenuBox.submenu);
    }
  }

  /**
   * @Class MenuBox Class create MenuItem massive in one block to call 
   * from main menu
   */
  
  private static class SubMenu extends VBox {
    public SubMenu(MenuItem... items) {
      setSpacing(STEP_BTWN_BUTTONS);
      setTranslateX(MENU_COORD_X);
      setTranslateY(MENU_COORD_Y);
      for (MenuItem item : items) {
        getChildren().addAll(item);
      }
    }
  }

}


