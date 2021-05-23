/**
 * 
 */
package com.adcubum.timerecording.ui.core.control.impl;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import com.adcubum.timerecording.ui.core.control.Controller;
import com.adcubum.timerecording.ui.core.model.PageModel;
import com.adcubum.timerecording.ui.core.view.Page;
import com.adcubum.timerecording.ui.core.view.impl.pagecontent.FXPageContent;
import com.adcubum.timerecording.ui.core.view.pagecontent.PageContent;
import com.adcubum.timerecording.ui.core.view.region.Dimension;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

/**
 * The {@link BaseFXController} provides the most basic features any
 * {@link Controller} should provide such as refreshing the current visible page
 * 
 * @author Dominic Stalder
 */
public abstract class BaseFXController<I extends PageModel, O extends PageModel>
      extends BaseController<I, O> implements Initializable {

   @FXML
   protected Pane rootPane;

   protected BaseFXController() {
      super();
   }

   @Override
   public void initialize(URL arg0, ResourceBundle arg1) {
      // Nothing to do by default
   }

   @Override
   public void show(I dataModelIn) {
      initDataModel(dataModelIn);
      Optional<Stage> optionalStage = getStage(page);
      optionalStage.ifPresent(showStage());
   }

   /**
    * Initializes the given {@link javafx.stage.Stage} for the given {@link Dimension}
    * 
    * @param stage
    *        the Stage which displays the content
    * @param dimension
    *        the Dimension which the new content requires to be displayed
    */
   protected void initStage4NewComponent(Stage stage, Dimension dimension) {
      rootPane.setPrefWidth(dimension.getPrefWidth());
      rootPane.setPrefHeight(dimension.getPrefHeight());
      stage.setWidth(dimension.getPrefWidth());
      stage.setHeight(dimension.getPrefHeight());
      stage.setMinWidth(dimension.getPrefWidth());
      stage.setMinHeight(dimension.getPrefHeight());
      stage.setResizable(false);
      stage.sizeToScene();
      onResizeHandlers.stream()
            .forEach(resizeHandler -> resizeHandler.accept(dimension));
   }

   /**
    * Returns <code>true</code> if the given {@link Region} is contained in the root {@link Pane} of this {@link BaseFXController}
    * and if this very {@link Region} is visible
    * 
    * @param region
    *        the {@link Region} to check
    * @return <code>true</code> if the given {@link Region} is contained in the root {@link Pane} and if this very {@link Region} is
    *         visible. Otherwise it returns <code>false</code>
    */
   protected boolean containsRegionAlready(Region region) {
      return rootPane.getChildren().contains(region) && region.isVisible();
   }

   private Consumer<? super Stage> showStage() {
      return page.isBlocking() ? Stage::showAndWait : Stage::show;
   }

   @Override
   public void hide() {
      Optional<Stage> optionalStage = getStage(page);
      optionalStage.ifPresent(Stage::hide);
   }

   private Optional<Stage> getStage(Page<?, ?> page) {
      PageContent content = page.getContent();
      if (content != null) {
         return ((FXPageContent) content).getStage();
      }
      return Optional.empty();
   }

   protected Stage getStage() {
      FXPageContent pageContent = (FXPageContent) page.getContent();
      return pageContent
            .getStage()
            .orElseThrow(IllegalStateException::new);
   }

   /**
    * @return the root Pane of this {@link BaseFXController}
    */
   public final Pane getRootPane() {
      return rootPane;
   }
}
