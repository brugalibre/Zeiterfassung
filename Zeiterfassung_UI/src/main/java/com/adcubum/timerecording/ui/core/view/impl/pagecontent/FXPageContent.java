/**
 * 
 */
package com.adcubum.timerecording.ui.core.view.impl.pagecontent;

import java.util.Optional;

import com.adcubum.timerecording.ui.core.view.region.Region;

import javafx.stage.Stage;

/**
 * The FXPageContent implements the PageContent for the Java-FX environment.
 * Therefore it contains the (main)-Stage object of the application
 * 
 * @author Dominic Stalder
 */
public class FXPageContent extends PageContentImpl {
   private Optional<Stage> stage;

   /**
   * 
   */
   public FXPageContent(Optional<Stage> stage, Region rootRegion) {
      super(rootRegion);
      this.stage = stage;
   }

   /**
    * @return the stage
    */
   public Optional<Stage> getStage() {
      return stage;
   }
}
