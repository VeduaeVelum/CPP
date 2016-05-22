package main_classes;

import javafx.beans.property.SimpleStringProperty;

/**
 * @Class CommonFileInfo Class contain information for replays table
 * and set/get method for all fields
 */

public class CommonFileInfo {

  private final SimpleStringProperty firstName;
  private final SimpleStringProperty lastName;
  private final SimpleStringProperty email;
  private final SimpleStringProperty popul;

  /**
   * @Constructor Constructor set class fields
   * @param gameTime - time of the game, contain date, week day and time
   * @param gameFPS - variable, which keep count of FPS
   * @param gameGenerations - variable, which keep count of generations
   * @param gamePopulation - variable, which keep proportion alive/dead 
   * polygon's cells
   */
  
  public CommonFileInfo(String gameTime, String gameFPS, 
      String gameGenerations, String gamePopulation) {
    this.firstName = new SimpleStringProperty(gameTime);
    this.lastName = new SimpleStringProperty(gameFPS);
    this.email = new SimpleStringProperty(gameGenerations);
    this.popul = new SimpleStringProperty(gamePopulation);
  }

  public String getFirstName() {
    return firstName.get();
  }

  public void setFirstName(String gameTime) {
    firstName.set(gameTime);
  }

  public String getLastName() {
    return lastName.get();
  }

  public void setLastName(String gameFPS) {
    lastName.set(gameFPS);
  }

  public String getEmail() {
    return email.get();
  }

  public void setEmail(String gameGenerations) {
    email.set(gameGenerations);
  }
  
  public String getPopul() {
    return popul.get();
  }
  
  public void setPopul(String gamePopulation) {
    popul.set(gamePopulation);
  }
}


