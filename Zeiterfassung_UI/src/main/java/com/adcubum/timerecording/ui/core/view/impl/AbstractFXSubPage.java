/**
 * 
 */
package com.adcubum.timerecording.ui.core.view.impl;

import java.io.IOException;
import java.util.Optional;

import com.adcubum.timerecording.ui.core.control.Controller;
import com.adcubum.timerecording.ui.core.control.impl.BaseFXController;
import com.adcubum.timerecording.ui.core.model.PageModel;
import com.adcubum.timerecording.ui.core.view.impl.pagecontent.FXPageContent;
import com.adcubum.timerecording.ui.core.view.impl.region.FxRegionImpl;
import com.adcubum.timerecording.ui.core.view.region.Region;

import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

/**
 * @author Dominic Stalder
 * @param <O>
 *        - the outgoing data-model
 * @param <I>
 *        - the incoming data-model
 */
public abstract class AbstractFXSubPage<I extends PageModel, O extends PageModel>
      extends AbstractFXPage<I, O> {

   @Override
   protected void setController(Controller<I, O> controller) {
      super.setController(controller);
      setFxContent((BaseFXController<I, O>) controller);
   }

   private <T extends BaseFXController<I, O>> void setFxContent(T controller) {
      Region rootRegion = new FxRegionImpl(controller.getRootPane());
      setContent(new FXPageContent(Optional.empty(), rootRegion));
   }

   @Override
   protected void initializeFXMLoader(FXMLLoader loader) throws IOException {
      // Nothing to do
   }

   @Override
   protected void initializeController(FXMLLoader loader) {
      // Nothing to do
   }

   @Override
   protected void initializeScene(FXMLLoader loader, Optional<Stage> optionalStage) {
      // Nothing to do
   }
}
