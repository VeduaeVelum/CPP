package main_classes;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * @Class ReplayWork Class responds about all replies functions
 * it write and read file information, set name of the file
 */

public class ReplayWork {
  private static final int SQUARE_SIZE = 12;
  private static final int X_MAX = 65;
  private static final int Y_MAX = 65;
  private static final int MSEC_IN_SEC = 1000;  
  private static final double STROKE_SIZE = 0.25;
  private final static String titleTemplate = "Conway's Game of Life | "
      + "Generation: %d";
  private static String FILE_PATH;
  private static final String FILE_FORMAT = ".dat";
    
  private int FPS;
  private long generation;
  private boolean firstRead;
  private boolean[][] nextGeneration = new boolean[X_MAX][Y_MAX];
  private int skipLenght;
  private String nameOfFile;
  
  private static Rectangle[][] content = new Rectangle[X_MAX][Y_MAX];
  private static Rectangle cell;
  private Timeline timeline;
  
  /**
   * @Method This method set one frame to file
   * @param nextGeneration - boolean matrix, which keep info about 
   * all cell of the polygon
   * @param generation - variable, which keep number of the generation 
   */
  
  public void setReplayInfo(boolean[][] nextGeneration, long generation) {
    try (DataOutputStream oStream = new DataOutputStream(
        new BufferedOutputStream(new FileOutputStream(FILE_PATH, true)))) {
      oStream.writeLong(generation);
      for (int i = 0; i < X_MAX; i++) {
        for (int j = 0; j < Y_MAX; j++) {
          oStream.writeBoolean(nextGeneration[i][j]);       
        }
      }
      oStream.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e1) {
      e1.printStackTrace();
    }
  }
  
  public void setFileName(String CurrentDate) {
    FILE_PATH = "Replays/Replay_";
    String buf = CurrentDate.replaceAll(" ", "_");    
    FILE_PATH += buf.replaceAll(":", "_");
    FILE_PATH += FILE_FORMAT;
  }
  
  /**
   * @Method This method set one fps value at first iteration of replay
   * recording
   * @param FPS - count of fps
   */
  
  public void setFpsToReplay(int FPS) {
    try (DataOutputStream oStream = new DataOutputStream(
        new BufferedOutputStream(new FileOutputStream(FILE_PATH)))) {
      oStream.writeInt(FPS); 
      oStream.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e1) {
      e1.printStackTrace();
    }
  }

  /**
   * @Method This is the main replay showing method
   * set parameters of the replay, call necessary methods
   * @param fileName - name of the file with replay info
   */
  
  public void showChanges(String fileName) {
    skipLenght = 0;
    firstRead = true;
    nameOfFile = fileName;
    Stage stage = new Stage();
    stage.initModality(Modality.APPLICATION_MODAL);
    
    Scene scene = new Scene(setField());
    stage.setScene(scene);
    stage.setTitle(String.format(titleTemplate, 0));

    getReplayFrame();
    renderNextGeneration();
    stage.setResizable(false);
    stage.setOnCloseRequest(event->{
      timeline.stop();
    });
    stage.show();
    setAnimation(stage);
  }
  
  /**
   * @Method This method set replay polygon
   */
  
  private HBox setField() {
    HBox hBox = new HBox();
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
    return hBox;
  }

  /**
   * @Method This method set animation of the replay
   * @param stage - stage of the window, which modified by this method
   */
  
  private void setAnimation(Stage stage) {
    final Duration durationOfFps = Duration.millis(MSEC_IN_SEC / FPS);
    final KeyFrame keyFrame = new KeyFrame(durationOfFps, 
        new EventHandler<ActionEvent>() {
     
      @Override
      public void handle(ActionEvent event) {
        if (!getReplayFrame())
        {      
          stage.close();           
          return;
        }
        renderNextGeneration();
        stage.setTitle(String.format(titleTemplate, generation));
      }
    });
    timeline = new Timeline(keyFrame);
    timeline.setCycleCount(Animation.INDEFINITE);
    timeline.play();
  }
  
  /**
   * @Method This method set new generation according to file info
   */
  
  private void renderNextGeneration() {
    for (int i = 0; i < X_MAX; i++) {
      for (int j = 0; j < Y_MAX; j++) {
        cell = content[i][j];
        if (nextGeneration[i][j] == true) {
          cell.setFill(Color.GRAY);
        } else {
          cell.setFill(Color.ANTIQUEWHITE);
        }
      }
    }
  }
  
  /**
   * @Method This method read one frame from file and set skip length
   */
  
  public boolean getReplayFrame() {
    
    try (DataInputStream iStream = new DataInputStream(
        new BufferedInputStream(new FileInputStream(nameOfFile)))) {
      if (firstRead == true) {
        FPS = iStream.readInt(); 
        firstRead = false; 
      }
      else {
        iStream.skip(Integer.BYTES);
        iStream.skip(skipLenght);
      }
      
      if (iStream.available() == 0) {
        iStream.close();
        this.timeline.stop();
        /*new GameStatisticCounter(FPS, countOfAlive, countOfDeath,
            finalGeneration - firstGeneration, nameOfFile).
            showGameStatistic();*/
        return false;
      }    
      generation = iStream.readLong();
      skipLenght += Long.BYTES;
      for (int i = 0; i < X_MAX; i++) {
        for (int j = 0; j < Y_MAX; j++) {
          nextGeneration[i][j] = iStream.readBoolean();
          skipLenght += 1;
        }
      }
      iStream.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e1) {
      e1.printStackTrace();
    }
    return true;
  }  
}
