package main_classes;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
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
import javafx.util.Duration;

/**
 * @Class GameStatisticCounter Class show common game statistic
 */

public class GameStatisticCounter {
  private static final int SCENE_SIZE_X = 300;
  private static final int SCENE_SIZE_Y = 300;
  private static final int X_MAX = 65;
  private static final int Y_MAX = 65;
  private static final int THREAD_SLEEP = 100;
  private static final int DOUBLE_ACCURACY = 1000;
  private static final String STATISTIC_TITLE = "Game Statistic";
  private static final String FONT_KIND = "Arial";
  private static final String COMMON_FILE_PATH = "Replays/ReplaysCommonInfo." 
      + "dat";
  private static final int FONT_SIZE = 16;
  private static final int PADD_BTWN_ELEMENTS = 10;
  private static final int VERT_GAP = 30;
  private static final int[] STAT_STRING_LABEL_POS = {1, 0};
  private static final int[] GENERATIONS_COUNT_POS = {1, 2};
  private static final int[] AVERAGE_PERCENRAGE_POS = {1, 3};
  private static final int[] MAX_LIFE_POS = {1, 4};

  private static final String GAME_RESULTS = "  --COMMON GAMES STATISTIC--";
  private static final String FILE_PREFIX = "Replays/Replay_";
  private static final String NOTATION_PREFIX = "Replays/Notations/Not_";
  private static final String BIN_FILE_FORMAT = ".dat";
  private static final String TXT_FILE_FORMAT = ".txt";
  private static String GENERATION_NUM = "Number of generations: ";
  private static String AVER_POPULATION = "Average population: ";
  private static String MAX_LIFE = "The more lively cell in: ";
  private String REP_FILE_PATH;
  private final int[] maxLifeCoord = new int[2];
  private long[][] polygonLifeStat = new long[X_MAX][Y_MAX];

  private Double averagePercentage = 0.0;
  private long numberOfGenerations = 0;
  private ArrayList<Double> populations = new ArrayList<Double>();
  private ArrayList<String> replayFileNames = new ArrayList<String>();

  private Label stat;
  private Label statGenerationNumber;
  private Label statAveragePercentage;
  private Label maxLifeLabel;
  private GridPane root;
  private Thread notationThread;

  private ScalaFunctions obj = new ScalaFunctions();

  {
    stat = new Label(GAME_RESULTS);
    stat.setAlignment(Pos.CENTER);
    stat.setTextFill(Color.PURPLE);
    stat.setFont(Font.font(FONT_KIND, FontWeight.BOLD, FONT_SIZE));

    statGenerationNumber = new Label();
    statGenerationNumber.setTextFill(Color.PURPLE);
    statGenerationNumber.setFont(Font.font(FONT_KIND, FontWeight.BOLD, 
        FONT_SIZE));

    statAveragePercentage = new Label();
    statAveragePercentage.setTextFill(Color.PURPLE);
    statAveragePercentage.setFont(Font.font(FONT_KIND, FontWeight.BOLD, 
        FONT_SIZE));

    maxLifeLabel = new Label();
    maxLifeLabel.setTextFill(Color.PURPLE);
    maxLifeLabel.setFont(Font.font(FONT_KIND, FontWeight.BOLD, FONT_SIZE));

    GridPane.setConstraints(this.stat, STAT_STRING_LABEL_POS[0], 
        STAT_STRING_LABEL_POS[1]);
    GridPane.setConstraints(this.statGenerationNumber, GENERATIONS_COUNT_POS[0],
        GENERATIONS_COUNT_POS[1]);
    GridPane.setConstraints(this.statAveragePercentage, AVERAGE_PERCENRAGE_POS[0],
        AVERAGE_PERCENRAGE_POS[1]);
    GridPane.setConstraints(this.maxLifeLabel, MAX_LIFE_POS[0], MAX_LIFE_POS[1]);
  }

  /**
   * @Method This method read replays information from file 
   * and count common generations number
   */

  public void readReplaysInfo() {
    try (DataInputStream iStream =
        new DataInputStream(new BufferedInputStream(new 
            FileInputStream(COMMON_FILE_PATH)))) {
      while (true) {
        if (iStream.available() == 0) {
          iStream.close();
          break;
        }

        countPolygonLifeStat(iStream.readUTF());
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
   * @Method This method count write notation and count more lively polygon's cell
   * 
   */

  public void countPolygonLifeStat(String fileName) {
    replayFileNames.add(fileName);
    REP_FILE_PATH = FILE_PREFIX + fileName.replace(" ", "_").
        replace(":", "_") + BIN_FILE_FORMAT;
    try (DataInputStream iStream =
        new DataInputStream(new BufferedInputStream(new 
            FileInputStream(REP_FILE_PATH)))) {
      iStream.skipBytes(Integer.BYTES);
      while (true) {
        if (iStream.available() == 0) {
          iStream.close();
          break;
        }
        
        iStream.skipBytes(Long.BYTES);
        
        for (int i = 0; i < X_MAX; i++) {
          for (int j = 0; j < Y_MAX; j++) {
            if (iStream.readBoolean()) {
              polygonLifeStat[i][j]++;
            }
          }
        }
        
        if (iStream.available() == 0) {
          iStream.close();
          break;
        }
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

  public long statisticCounter() {
    long max = 0;
    int k = 0;
    double[] array = new double[populations.size()];

    for (Double popul : populations) {
      array[k++] = popul;
    }

    averagePercentage = obj.count(array);
    averagePercentage /= populations.size();
    averagePercentage = Math.ceil(averagePercentage * DOUBLE_ACCURACY) 
        / DOUBLE_ACCURACY;

    for (int i = 0; i < X_MAX; i++) {
      for (int j = 0; j < Y_MAX; j++) {
        if (polygonLifeStat[i][j] > max) {
          max = polygonLifeStat[i][j];
          maxLifeCoord[0] = i;
          maxLifeCoord[1] = j;
        }
      }
    }
    return max;
  }

  private void writeNotation() {
    for (String fileName : replayFileNames) {
      REP_FILE_PATH = FILE_PREFIX + fileName.replace(" ", "_").
          replace(":", "_") + BIN_FILE_FORMAT;
      boolean temp;
      try (DataInputStream iStream =
          new DataInputStream(new BufferedInputStream(new 
              FileInputStream(REP_FILE_PATH)))) {
        REP_FILE_PATH = NOTATION_PREFIX + fileName.replace(" ", "_").replace(":", "_")
            + TXT_FILE_FORMAT;
        FileOutputStream oStream = new FileOutputStream(REP_FILE_PATH);

        oStream.write(obj.chooser("Head").getBytes());
        oStream.write(obj.chooser(iStream.readInt()).getBytes());

        while (true) {
          if (iStream.available() == 0) {
            iStream.close();
            oStream.close();
            break;
          }

          oStream.write(obj.chooser(iStream.readLong()).getBytes());

          for (int i = 0; i < X_MAX; i++) {
            for (int j = 0; j < Y_MAX; j++) {
              temp = iStream.readBoolean();
              oStream.write(obj.chooser(temp).getBytes());
              if (temp == true) {
                polygonLifeStat[i][j]++;
              }
            }
            oStream.write("\r\n".getBytes());
          }
          oStream.write("\r\n\r\n".getBytes());
        }
        
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      } catch (IOException e1) {
        e1.printStackTrace();
      } 
    }
    try {
      notationThread.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  /**
   * @Method This is the main method of the class. Show window 
   * with counted common statistic
   * 
   */

  public void showGameStatistic() {
    Stage window = new Stage();
    window.initModality(Modality.APPLICATION_MODAL);

    readReplaysInfo();
    statisticCounter();
    try {
      notationThread = new Thread() {
        public void run() {
          writeNotation();
        }
      };
      notationThread.start();
      notationThread.sleep(THREAD_SLEEP);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }    

    GENERATION_NUM = "Number of generations: " + this.numberOfGenerations;
    AVER_POPULATION = "Average population: " + this.averagePercentage;
    MAX_LIFE = "The more lively cell in: (X: " + this.maxLifeCoord[0] + ", Y: "
        + this.maxLifeCoord[1] + ")";

    this.numberOfGenerations = 0;
    this.averagePercentage = 0.0;
    this.populations.clear();

    this.statGenerationNumber.setText(GENERATION_NUM);
    this.statAveragePercentage.setText(AVER_POPULATION);
    this.maxLifeLabel.setText(MAX_LIFE);

    root = new GridPane();
    root.setPadding(
        new Insets(PADD_BTWN_ELEMENTS, PADD_BTWN_ELEMENTS, 
            PADD_BTWN_ELEMENTS, PADD_BTWN_ELEMENTS));
    root.setAlignment(Pos.CENTER);
    root.setVgap(VERT_GAP);

    root.getChildren().addAll(this.stat, this.statGenerationNumber, 
        this.statAveragePercentage, this.maxLifeLabel);

    Scene scene = new Scene(root, SCENE_SIZE_X, SCENE_SIZE_Y);
    scene.setFill(Color.WHITESMOKE);
    window.setScene(scene);
    window.setTitle(STATISTIC_TITLE);
    window.setResizable(false);
    window.showAndWait();
  }
}
