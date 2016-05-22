package main_classes;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * @Class GameRules Class define modal window with game rules 
 */

public class GameRules {
  private static final String rulesText =
      "\n\n\t\t\t\t\t\tИГРА ЖИЗНЬ\n" 
          + "\n\n Жизнь в игре развивается по следующему алгоритму:\n"
          + "\t 1. В клетке зарождается жизнь если у нее 2 или 3 соседа\n"
          + "\t 2. Во всех остальных случаях она умирает\n"
          + "\t 3. За границей полигона жизни нет и она не проникает туда\n"
          + "\t 4. Каждый ход в различных местах рождается новая жизнь\n\n"
          + " Перед началом игры вносятся следующие данные:\n"
          + "\t 1. Количество поколений в секунду\n" 
          + "\t 2. Процент заселенности поля\n"
          + "\n\n\t\t\t\t\t    УДАЧНОЙ ИГРЫ!";
  private static final String FONT_KIND = "Arial";
  private static final int FONT_SIZE = 16;
  private static final int SCENE_WIDTH = 525;
  private static final int SCENE_HEIGHT = 400;
  private static Label lbl;
  
  /**
   * @Method This method made all class functions
   * @param title - string, which set to the window title
   */
  
  public static void showGameRules(String title) {
    Stage window = new Stage();
    window.initModality(Modality.APPLICATION_MODAL);

    Pane pane = new Pane();
    lbl = new Label();
    lbl.setAlignment(Pos.CENTER);
    lbl.setTextAlignment(TextAlignment.LEFT);
    lbl.setTextFill(Color.PURPLE);
    lbl.setText(rulesText);
    lbl.setFont(Font.font(FONT_KIND, FontWeight.BOLD, FONT_SIZE));

    pane.getChildren().add(lbl);
    Scene scene = new Scene(pane, SCENE_WIDTH, SCENE_HEIGHT);
    scene.setFill(Color.WHITESMOKE);
    window.setScene(scene);
    window.setTitle(title);
    window.showAndWait();
  }

}
