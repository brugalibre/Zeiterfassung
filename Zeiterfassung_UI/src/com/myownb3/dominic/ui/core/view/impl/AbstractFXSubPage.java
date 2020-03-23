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
 */
public abstract class AbstractFXSubPage<IN_VO extends PageModel, OUT_VO extends PageModel>
      extends AbstractFXPage<IN_VO, OUT_VO> {

   @Override
   protected void initializeFXMLoader(FXMLLoader loader) throws IOException {
      // Nothing to do
   }

   @Override
   protected void initializeController(FXMLLoader loader) {
      // Nothing to do
   }

   @Override
   protected void initializeScene(FXMLLoader loader, Optional<Stage> optionalStage) throws IOException {
      // Nothing to do
   }
}
