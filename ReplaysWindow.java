package main_classes;

import java.util.ArrayList;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * @Class ReplaysWindow Class show list of available replies
 */

public class ReplaysWindow {
  private static final int SCENE_WIDTH = 200;
  private static final int SCENE_HEIGHT = 200;  
    
  ArrayList<String> fileNames = new ArrayList<String>();  
  
  /**
   * @Method This method made all class functions
   * @param title - string, which set to the window title
   */
  
  public static void showReplayList(String title) {
    Stage window = new Stage();
    window.initModality(Modality.APPLICATION_MODAL);
    
    Pane pane = new Pane();
    ComboBox<String> fileNames = new ComboBox<String>();
    
    fileNames.setItems(FXCollections.observableArrayList(
        "Replay.dat"
    ));
    fileNames.getSelectionModel().selectedIndexProperty().addListener(
        (ObservableValue<? extends Number> ov, Number old_val, Number new_val)->{
          ReplayWork obj = new ReplayWork();
          obj.showChanges("Replay.dat");
          window.close();
        }
    );
    pane.getChildren().add(fileNames);
    Scene scene = new Scene(pane, SCENE_WIDTH, SCENE_HEIGHT);
    scene.setFill(Color.WHITESMOKE);
    window.setScene(scene);
    window.setTitle(title);
    window.showAndWait();
  }
}
