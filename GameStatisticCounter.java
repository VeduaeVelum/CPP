package main_classes;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * @Class GameStatisticCounter Class show common game statistic
 */

public class GameStatisticCounter {
  private static final int SCENE_SIZE_X = 280;
  private static final int SCENE_SIZE_Y = 250;
  private static final int DOUBLE_ACCURACY = 1000;
  private static final String STATISTIC_TITLE = "Game Statistic";
  private static final String FONT_KIND = "Arial";
  private static final String COMMON_FILE_PATH = 
      "Replays/ReplaysCommonInfo.dat";
  private static final int FONT_SIZE = 16;
  private static final int PADD_BTWN_ELEMENTS = 10;
  private static final int VERT_GAP = 30;
  private static final int[] STAT_STRING_LABEL_POS = {1, 0};  
  private static final int[] GENERATIONS_COUNT_POS = {1, 2};
  private static final int[] AVERAGE_PERCENRAGE_POS = {1, 3};
  
  private static final String GAME_RESULTS = "  --COMMON GAMES STATISTIC--";
  private static String GENERATION_NUM = "Number of generations: ";
  private static String AVER_POPULATION = "Average population: ";
      
  private Double averagePercentage = 0.0;
  private long numberOfGenerations = 0;
  private ArrayList<Double> populations = new ArrayList<Double>();
  
  private Label stat;
  private Label statGenerationNumber;
  private Label statAveragePercentage;
  private GridPane root;
  
  {
    stat = new Label(GAME_RESULTS);
    stat.setAlignment(Pos.CENTER);
    stat.setTextFill(Color.PURPLE);
    stat.setFont(Font.font(FONT_KIND, FontWeight.BOLD, FONT_SIZE));
    
    statGenerationNumber = new Label();
    statGenerationNumber.setTextFill(Color.PURPLE);
    statGenerationNumber.setFont(Font.font(FONT_KIND, 
        FontWeight.BOLD, FONT_SIZE));
    
    statAveragePercentage = new Label();
    statAveragePercentage.setTextFill(Color.PURPLE);
    statAveragePercentage.setFont(Font.font(FONT_KIND, 
        FontWeight.BOLD, FONT_SIZE));
    
    
    GridPane.setConstraints(stat, STAT_STRING_LABEL_POS[0],
        STAT_STRING_LABEL_POS[1]); 
  }
  
  /**
   * @Method This method read replays information from file
   * and count common generations number
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
        
        iStream.readUTF();
        iStream.readInt();
        numberOfGenerations += iStream.readLong();
        populations.add(iStream.readDouble());       
             
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e1) {
      e1.printStackTrace();
    } 
  }
  
  /**
   * @Method This method count average population of the polygon
   * 
   */
  
  public void statisticCounter() {
    for (Double popul : populations) {
      averagePercentage += popul;
    }
    averagePercentage /= populations.size();
    averagePercentage = Math.ceil(averagePercentage * 
        DOUBLE_ACCURACY) / DOUBLE_ACCURACY;
  }
  
  /**
   * @Method This is the main method of the class.
   * Show window with counted common statistic
   * 
   */
  
  public void showGameStatistic() {
    Stage window = new Stage();
    window.initModality(Modality.APPLICATION_MODAL);  
    
    readReplaysInfo();
    statisticCounter();
   
    GENERATION_NUM = "Number of generations: " + this.numberOfGenerations;
    AVER_POPULATION = "Average population: " + this.averagePercentage;
    this.numberOfGenerations = 0;
    this.averagePercentage = 0.0;
    this.populations.clear();
    
    this.statGenerationNumber.setText(GENERATION_NUM);
    this.statAveragePercentage.setText(AVER_POPULATION);
    
    GridPane.setConstraints(this.statGenerationNumber, 
        GENERATIONS_COUNT_POS[0], GENERATIONS_COUNT_POS[1]);
    
    GridPane.setConstraints(this.statAveragePercentage, 
        AVERAGE_PERCENRAGE_POS[0], AVERAGE_PERCENRAGE_POS[1]);
    
    root = new GridPane();
    root.setPadding(new Insets(PADD_BTWN_ELEMENTS, 
        PADD_BTWN_ELEMENTS, PADD_BTWN_ELEMENTS, PADD_BTWN_ELEMENTS));
    root.setAlignment(Pos.CENTER);
    root.setVgap(VERT_GAP);
    
    root.getChildren().addAll(this.stat, this.statGenerationNumber, 
        this.statAveragePercentage);   
    
    Scene scene = new Scene(root, SCENE_SIZE_X, SCENE_SIZE_Y);
    scene.setFill(Color.WHITESMOKE);
    window.setScene(scene);
    window.setTitle(STATISTIC_TITLE);
    window.setResizable(false);
    window.showAndWait();
  }  
}
