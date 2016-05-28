package main_classes;

import javafx.animation.Animation;
import javafx.animation.FillTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * @Class SetParamsToMainApp Class define modal window, which called 
 * before starting of the game. Offers to user input mane game parameters
 */

public class SetParamsToMainApp {
  private static final int SCENE_SIZE_X = 250;
  private static final int SCENE_SIZE_Y = 250;
  private static final int PADD_BTWN_ELEMENTS = 10;
  private static final int VERT_GAP = 30;
  private static final int FPS_LIMIT_MIN = 0;
  private static final int FPS_LIMIT_MAX = 30;
  private static final double ALIVE_LIMIT_MIN = 0.0;
  private static final double ALIVE_LIMIT_MAX = 100.0;
  private static final double PERSENT_TO_PARTS = 0.01;
  
  private static final String FPS_PROMT_TEXT = "Count of FPS(1-30)";
  private static final String ALIVE_PROMT_TEXT = 
      "Population percentage(0-100)";
  private static final String ADD_ALIVE_TEXT = "Add life supporting(Y/N)";
  private static final String BUTTON_TEXT = "Submit parametres";
  
  private static final int[] FPS_TXTFIELD_POS = {1, 0};
  private static final int[] ALIVE_TXTFIELD_POS = {1, 1};
  private static final int[] ADD_ALIVE_FLAG_POS = {1, 2};
  private static final int[] SUBMIT_TXTFIELD_POS = {1, 3};  
  
  private static double AliveProbability;
  private static int FPS;
  
  private static TextField fps_mean;
  private static TextField alive_mean;
  private static CheckBox add_alive;
  private static MenuItem submit;
  
  /**
   * @Method This method set all window components
   * @param title - string, which set to the window title
   */
  
  public static void setParams(String title) {
    Stage window = new Stage();
    window.initModality(Modality.APPLICATION_MODAL);

    GridPane root = new GridPane();
    root.setPadding(new Insets(PADD_BTWN_ELEMENTS, 
        PADD_BTWN_ELEMENTS, PADD_BTWN_ELEMENTS, PADD_BTWN_ELEMENTS));
    root.setAlignment(Pos.CENTER);
    root.setVgap(VERT_GAP);

    fps_mean = new TextField();
    fps_mean.setPromptText(FPS_PROMT_TEXT);
    GridPane.setConstraints(fps_mean, FPS_TXTFIELD_POS[0],
        FPS_TXTFIELD_POS[1]);
    root.getChildren().add(fps_mean);

    alive_mean = new TextField();
    alive_mean.setPromptText(ALIVE_PROMT_TEXT);
    GridPane.setConstraints(alive_mean, ALIVE_TXTFIELD_POS[0],
        ALIVE_TXTFIELD_POS[1]);
    root.getChildren().add(alive_mean);

    add_alive = new CheckBox();
    add_alive.setText(ADD_ALIVE_TEXT);
    add_alive.setSelected(false);
    GridPane.setConstraints(add_alive, ADD_ALIVE_FLAG_POS[0], 
        ADD_ALIVE_FLAG_POS[1]);
    root.getChildren().add(add_alive);
    
    submit = new MenuItem(BUTTON_TEXT);
    submit.setOnMouseClicked(event -> {
      String buf;
      buf = fps_mean.getText();
      FPS = Integer.parseInt(buf);
      buf = alive_mean.getText();
      AliveProbability = Double.parseDouble(buf);
      if(checkParametres(FPS, AliveProbability))
      {
        window.close();
        new MainApplication().start(FPS, PERSENT_TO_PARTS * AliveProbability, 
            add_alive.selectedProperty().get());
      }
      else
      {
        fps_mean.clear();
        alive_mean.clear();
        fps_mean.setPromptText(FPS_PROMT_TEXT);
        alive_mean.setPromptText(ALIVE_PROMT_TEXT);        
      }
    });
    GridPane.setConstraints(submit, SUBMIT_TXTFIELD_POS[0],
        SUBMIT_TXTFIELD_POS[1]);

    root.getChildren().add(submit);
    Scene scene = new Scene(root, SCENE_SIZE_X, SCENE_SIZE_Y);
    scene.setFill(Color.WHITESMOKE);
    window.setScene(scene);
    window.setTitle(title);
    window.setResizable(false);
    window.showAndWait();
  }
  
  /**
   * @Method This method check inputed by user parameters 
   * @param fps - count of fps
   * @param aliveProb - percentage of the alive cells
   */
  
  public static boolean checkParametres(int fps, double aliveProp)
  {
    return (fps > FPS_LIMIT_MAX || fps < FPS_LIMIT_MIN 
        || aliveProp > ALIVE_LIMIT_MAX || aliveProp < ALIVE_LIMIT_MIN)
        ? false : true;
  }
  
  /**
   * @Class MenuItem Class create button in style of the main menu,
   * but some settings are differ 
   */
  
  private static class MenuItem extends StackPane {
    private static final int BUTTON_WIDTH = 175;
    private static final int BUTTON_HEIGHT = 40;
    private static final int FONT_SIZE = 18;
    private static final double BUTTON_OPACITY = 0.5;
    private static final double BUTTON_ANIM_DURACTION = 0.5;
        
    private static final String FONT = "Arial";
        
    public MenuItem(String name) {
      Rectangle bd = new Rectangle(BUTTON_WIDTH, 
          BUTTON_HEIGHT, Color.GREENYELLOW);
      bd.setOpacity(BUTTON_OPACITY);

      Text text = new Text(name);
      text.setFill(Color.GREEN);
      text.setFont(Font.font(FONT, FontWeight.BOLD, FONT_SIZE));

      setAlignment(Pos.CENTER);
      getChildren().addAll(bd, text);
      FillTransition st = new FillTransition(Duration.
          seconds(BUTTON_ANIM_DURACTION), bd);

      setOnMouseEntered(event -> {
        st.setFromValue(Color.GREENYELLOW);
        st.setToValue(Color.LIGHTSKYBLUE);
        st.setCycleCount(Animation.INDEFINITE);
        st.setAutoReverse(true);
        st.play();
      });
      setOnMouseExited(event -> {
        st.stop();
        bd.setFill(Color.GREENYELLOW);
      });
    }
  }
}
