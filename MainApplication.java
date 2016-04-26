package main_classes;

import java.util.concurrent.ThreadLocalRandom;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * @author mizhevich.e
 * @Class MainApplication Class draws animated background, main and sub menu,
 *     define button actions. 
 */

/**
 * Model of the "Game of Life";\n
 * Model of field: boarded polygon;\n
 * Life cannot penetrate through the board;\n
 * In the title of the window generations is counting;\n
 * Any generation life born in random cells of polygon.
 */

public class MainApplication {
  private static double Alive_Probability;  
  private static int FPS;

  private static final int SQUARE_SIZE = 12;
  private static final int X_MAX = 65;
  private static final int Y_MAX = 65;
  private static final int NUM_OF_SUPPORT = 70;
  private static final int MSEC_IN_SEC = 1000;  
  private static final double STROKE_SIZE = 0.25;
  
  private boolean[][] currentGeneration = new boolean[X_MAX][Y_MAX];
  private boolean[][] nextGeneration = new boolean[X_MAX][Y_MAX];
  private boolean[][] temporaryReference;
  private boolean replayFlag = false;
  private int[] lifeSupporting = new int[NUM_OF_SUPPORT];

  private final String titleTemplate = "Conway's Game of Life | Generation: %d";
  
  private Rectangle[][] content = new Rectangle[X_MAX][Y_MAX];
  private Rectangle cell;
  
  private ReplayWork obj = new ReplayWork();
  
  /**
   * @Method This method seed start life
   * @param aliveProbability - percent of alive cells on the polygon
   */
  
  private void seed(double aliveProbability) {
    for (int i = 0; i < X_MAX; i++) {
      for (int j = 0; j < Y_MAX; j++) {
        nextGeneration[i][j] = Math.random() < aliveProbability;
      }
    }
  }

  /**
   * @Method This method create new generation according to algorithm
   */
  
  private void createNewGeneration() {
    for (int i = 0; i < NUM_OF_SUPPORT; i += 2) {
      lifeSupporting[i] = ThreadLocalRandom.current().nextInt(0, X_MAX + 1);
      lifeSupporting[i + 1] = ThreadLocalRandom.current().nextInt(0, Y_MAX + 1);
    }
    for (int i = 0; i < X_MAX; i++) {
      for (int j = 0; j < Y_MAX; j++) {
        nextGeneration[i][j] = getRuleSetResult(i, j);
      }
    }
    for (int i = 0; i < X_MAX; i++) {
      for (int j = 0; j < Y_MAX; j++) {
        for (int k = 0; k < NUM_OF_SUPPORT; k += 2) {
          if (lifeSupporting[k] == i && lifeSupporting[k + 1] == j) {
            nextGeneration[i][j] = true;
          }
        }
      }
    }
  }

  /**
   * @Method This method determine will be life in cell or not
   * @param x, y - coordinates of the poligon's cell
   */
  
  private boolean getRuleSetResult(int x, int y) {
    return (currentGeneration[x][y]) ? aliveCellRules(x, y) 
        : deadCellRules(x, y);
  }

  /**
   * @Method This method determine status of the cell(alive)
   * @param x, y - coordinates of the poligon's cell
   */
  
  private boolean aliveCellRules(int x, int y) {
    switch (countAliveNeighbors(x, y)) {
      case 2:
      case 3:
        return true;
      default:
        return false;
    }
  }

  /**
   * @Method This method determine status of the cell(dead)
   * @param x, y - coordinates of the poligon's cell
   */
  
  private boolean deadCellRules(int x, int y) {
    return (countAliveNeighbors(x, y)) == 3 ? true : false;
  }
    
  /**
   * @Method This method count number of neighbors of the cell
   * @param x, y - coordinates of the poligon's cell
   */
  
  private int countAliveNeighbors(int x, int y) {
    int count = 0;
    for (int i = x - 1; i <= x + 1 && i < X_MAX; i++) {
      for (int j = y - 1; j <= y + 1 && j < Y_MAX; j++) {
        if (i >= 0 && j >= 0 && !(i == x && j == y) && currentGeneration[i][j]) {
          count++;
        }
        if (count == 4) {
          return count;
        }
      }
    }
    return count;
  }

  /**
   * @Method This method render new generation
   */
  
  private void renderNextGeneration() {
    for (int i = 0; i < X_MAX; i++) {
      for (int j = 0; j < Y_MAX; j++) {
        cell = content[i][j];
        if (nextGeneration[i][j] && !currentGeneration[i][j]) {
          cell.setFill(Color.GRAY);
        } else if (!nextGeneration[i][j] && currentGeneration[i][j]) {
          cell.setFill(Color.ANTIQUEWHITE);
        }
      }
    }
    temporaryReference = currentGeneration;
    currentGeneration = nextGeneration;
    nextGeneration = temporaryReference;
  }

  /**
   * @Method This method set animation of the game
   * @param stage - stage of the window, which modified by this method
   */
  
  private void setAnimation(Stage stage) {
    final Duration durationOfFps = Duration.millis(MSEC_IN_SEC / FPS);
    final KeyFrame keyFrame = new KeyFrame(durationOfFps, 
        new EventHandler<ActionEvent>() {
      private long generation = 1;

      @Override
      public void handle(ActionEvent event) {
        createNewGeneration();
        renderNextGeneration();
        stage.setTitle(String.format(titleTemplate, generation));
        generation++;
        if (replayFlag == true)
        {
          obj.setReplayInfo(nextGeneration, generation);
        }
      }
    });
    Timeline timeline = new Timeline(keyFrame);
    timeline.setCycleCount(Animation.INDEFINITE);
    timeline.play();
  }

  /**
   * @Method This method set polygon with cells and add buttons actions
   */
  
  private HBox setField() {
    HBox hBox = new HBox();
    VBox btnsBox = new VBox(30);
    Button replayStart = new Button("Start replay");
    Button replayStop = new Button("Stop replay");
    replayStop.setDisable(true);
    for (int i = 0; i < X_MAX; i++) {
      VBox vBox = new VBox();
      hBox.getChildren().add(vBox);
      for (int j = 0; j < Y_MAX; j++) {
        cell = new Rectangle(SQUARE_SIZE, SQUARE_SIZE, 
            Color.ANTIQUEWHITE);
        cell.setStroke(Color.BLACK);
        cell.setStrokeWidth(STROKE_SIZE);
        vBox.getChildren().add(cell);
        content[i][j] = cell;
      }
    }
    replayStart.setOnMouseClicked(event->{
      replayFlag = true;
      obj.setFpsToReplay(FPS);
      replayStart.setDisable(true);
      replayStop.setDisable(false);
    });
    replayStop.setOnMouseClicked(event->{
      replayFlag = false;
      replayStart.setDisable(false);
      replayStop.setDisable(true);
    }); 
    btnsBox.getChildren().addAll(replayStart, replayStop);
    btnsBox.setPadding(new Insets(15, 30, 0, 30));
    hBox.getChildren().add(btnsBox);
    return hBox;
  }

  /**
   * @Method The main method of the class. Show modal window and call 
   * another method
   */
  
  public void start(int fps, double aliveProb) {
    Alive_Probability = aliveProb;
    FPS = fps;
    Stage stage = new Stage();
    stage.initModality(Modality.APPLICATION_MODAL);

    Scene scene = new Scene(setField());
    stage.setScene(scene);
    stage.setTitle(String.format(titleTemplate, 0));

    seed(Alive_Probability);
    renderNextGeneration();
    stage.setResizable(false);
    stage.show();
    setAnimation(stage);
  }  
}
