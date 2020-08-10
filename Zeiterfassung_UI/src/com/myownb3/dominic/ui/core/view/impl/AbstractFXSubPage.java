/**
 * 
 */
package com.myownb3.dominic.ui.core.view.impl;

import java.io.IOException;
import java.util.Optional;

import com.myownb3.dominic.ui.core.model.PageModel;

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
