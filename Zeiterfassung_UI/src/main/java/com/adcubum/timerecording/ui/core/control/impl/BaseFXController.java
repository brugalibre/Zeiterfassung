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
import com.adcubum.timerecording.ui.core.view.PageContent;
import com.adcubum.timerecording.ui.core.view.impl.AbstractFXPage;
import com.adcubum.timerecording.ui.core.view.impl.FXPageContent;

import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.stage.Stage;

/**
 * The {@link BaseFXController} provides the most basic features any
 * {@link Controller} should provide such as refreshing the current visible page
 * 
 * @author Dominic Stalder
 */
public abstract class BaseFXController<I extends PageModel, O extends PageModel>
      extends BaseController<I, O> implements Initializable {

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

   @SuppressWarnings("rawtypes")
   public Node getPageContent() {
      return ((AbstractFXPage) this.page).getRootParent();
   }

   protected Stage getStage() {
      FXPageContent pageContent = (FXPageContent) page.getContent();
      return pageContent
            .getStage()
            .orElseThrow(IllegalStateException::new);
   }
}
