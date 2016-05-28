package main_classes;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * @Class ReplaysWindow Class show table of replays
 */

public class ReplaysWindow {
  private TableView<CommonFileInfo> table = new TableView<CommonFileInfo>();
  private static final int SCENE_WIDTH = 605;
  private static final int SCENE_HEIGHT = 515;
  private static final int DOUBLE_ACCURACY = 100_000;
  private static final int DATE_COLUMN_WIDTH = 240;
  private static final int FPS_COLUMN_WIDTH = 100;
  private static final int GENER_NUM_COLUMN_WIDTH = 100;
  private static final int POPULATION_COLUMN_WIDTH = 140;
  private static final int BUTTON_HEIGHT = 10;
  private static final int BUTTON_WIDTH = 100;
  private static final String REPLAY_FOLDER = "Replays/Replay_";
  private static final String FILE_FORMAT = ".dat";
  private static final String FONT_KIND = "Arial";
  private static final int FONT_SIZE = 20;
  private static final String WINDOW_TITLE = "Replays table window";
  private static final String COMMON_FILE_PATH = 
      "Replays/ReplaysCommonInfo.dat";
  
  private String gameTime;
  private int gameFPS;
  private long gameGenerations;
  private double gamePopulation;
  
  private ArrayList<CommonFileInfo> replaysInfoList =
      new ArrayList<CommonFileInfo>();
  
  private TableColumn<CommonFileInfo, String> gameTimeCol = 
      new TableColumn<CommonFileInfo, String>("Game time");
  private TableColumn<CommonFileInfo, String> FPSNumCol = 
      new TableColumn<CommonFileInfo, String>("Game FPS");
  private TableColumn<CommonFileInfo, String> generCol = 
      new TableColumn<CommonFileInfo, String>("Generations");
  private TableColumn<CommonFileInfo, String> populCol = 
      new TableColumn<CommonFileInfo, String>("Populations");
  
  private Button playButton = new Button("Play");
  private final Label label = new Label("Replays INFO");
  private final VBox vBox = new VBox();  
  
  {
    gameTimeCol.setMinWidth(DATE_COLUMN_WIDTH);
    gameTimeCol.setCellValueFactory(
        new PropertyValueFactory<CommonFileInfo, String>("firstName"));
    FPSNumCol.setMinWidth(FPS_COLUMN_WIDTH);
    FPSNumCol.setCellValueFactory(
        new PropertyValueFactory<CommonFileInfo, String>("lastName"));
    generCol.setMinWidth(GENER_NUM_COLUMN_WIDTH);
    generCol.setCellValueFactory(
        new PropertyValueFactory<CommonFileInfo, String>("email"));
    
    populCol.setMinWidth(POPULATION_COLUMN_WIDTH);
    populCol.setCellValueFactory(
        new PropertyValueFactory<CommonFileInfo, String>("popul"));  
    
    table.getColumns().addAll(gameTimeCol, FPSNumCol, generCol, populCol);
    table.setEditable(true);
    
    playButton.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);
    playButton.setDisable(true);  
    
    label.setFont(Font.font(FONT_KIND, FontWeight.BOLD, FONT_SIZE));
    label.setTextFill(Color.PURPLE);
    
    vBox.setSpacing(10);
    vBox.setPadding(new Insets(10, 0, 0, 10));
    vBox.setAlignment(Pos.CENTER);
    vBox.getChildren().addAll(label, table, playButton);
    
  }
  
  {
    playButton.setOnMouseClicked(event -> {
      String replayFileName = REPLAY_FOLDER + table.getSelectionModel().
          getSelectedItem().getFirstName().replace(" ", "_").replace(":", "_")
          + FILE_FORMAT;
      ReplayWork obj = new ReplayWork();
      obj.showChanges(replayFileName);
      playButton.setDisable(true);
    });
    table.setOnMouseClicked(event -> {
      playButton.setDisable(false);
    });
  }
  
  /**
   * @Method This method read statistic information
   * from file
   *    
   */
  
  public void readReplaysInfo() {
    try (DataInputStream iStream =
        new DataInputStream(new BufferedInputStream
            (new FileInputStream(COMMON_FILE_PATH)))) {
      while (true) { 
        if (iStream.available() == 0) {
          iStream.close();
          break;
        }
        
        gameTime = iStream.readUTF();
        gameFPS = iStream.readInt();
        gameGenerations = iStream.readLong();
        gamePopulation = iStream.readDouble();       
        gamePopulation = Math.ceil(gamePopulation * 
            DOUBLE_ACCURACY) / DOUBLE_ACCURACY;
        replaysInfoList.add(new CommonFileInfo(gameTime, 
            String.valueOf(gameFPS), String.valueOf(gameGenerations), 
            String.valueOf(gamePopulation)));       
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e1) {
      e1.printStackTrace();
    } 
  }

  /**
   * @Method This is the main method of the class. 
   * It drown table, add replays information to it.
   * 
   */
  
  public void showReplayList(String title) {
    Stage stage = new Stage();
    stage.initModality(Modality.APPLICATION_MODAL);
    stage.setResizable(false);
    stage.setTitle(WINDOW_TITLE);
    stage.setWidth(SCENE_WIDTH);
    stage.setHeight(SCENE_HEIGHT);
    
    readReplaysInfo();
    
    ObservableList<CommonFileInfo> observableList = 
        FXCollections.observableList(replaysInfoList); 
    
    table.setItems(observableList);
    
    Scene scene = new Scene(new Group());
    scene.setFill(Color.WHITESMOKE);   

    ((Group) scene.getRoot()).getChildren().addAll(vBox);

    stage.setScene(scene);
    stage.showAndWait();
  }
  
  /**
   * @Method This method sort replays info and determine 
   * time of the sorting
   * 
   */
  
  public void sortItems(ArrayList<CommonFileInfo> data) {
    long start, end;
    int size;

    ArrayList<CommonFileInfo> data_scale = new ArrayList<>();
    for (int i = 0; i < 20; i++) {
      data_scale.addAll(data);
    }
    
    size = data_scale.size();
    
    System.out.println(size);
    
    long[] arrayJavaSorted = new long[size];
    for (int j = 0; j < size; j++) {
      arrayJavaSorted[j] = Long.decode(data_scale.get(j).getEmail());
    }
    
    long[] arrayScalaSorted = arrayJavaSorted.clone();

    JavaSort javaSort = new JavaSort();
    start = System.nanoTime();
    javaSort.sort(arrayJavaSorted);
    end = System.nanoTime();
    System.out.println("Java:  " + (end - start));

    ScalaFunctions scalaSort = new ScalaFunctions();
    start = System.nanoTime();
    scalaSort.qsort(arrayScalaSorted, 0, size - 1);    
    end = System.nanoTime();
    System.out.println("Scala: " + (end - start));
  }
}
