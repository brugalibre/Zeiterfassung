/**
 * 
 */
package com.myownb3.dominic.ui.core.view.impl;

import com.myownb3.dominic.ui.core.view.RootUIContent;

import javafx.stage.Stage;

/**
 * The FXPageContent implements the PageContent for the Java-FX environment.
 * Therefore it contains the (main)-Stage object of the application
 * 
 * @author Dominic Stalder
 */
public class FXUIContent implements RootUIContent {

   /*
    * this is the main Stage object used by their user interface components. Its a
    * redundant reference to the AbstractFXPage attribute 'stage'
    */
   private Stage stage;

   /**
   * 
   */
   public FXUIContent(Stage stage) {
      this.stage = stage;
   }

   /**
    * @return the stage
    */
   public Stage getStage() {
      return stage;
   }
}
