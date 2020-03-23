/**
 * 
 */
package com.myownb3.dominic.ui.core.control.impl;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import com.myownb3.dominic.ui.core.control.Controller;
import com.myownb3.dominic.ui.core.model.PageModel;
import com.myownb3.dominic.ui.core.view.Page;
import com.myownb3.dominic.ui.core.view.PageContent;
import com.myownb3.dominic.ui.core.view.impl.AbstractFXPage;
import com.myownb3.dominic.ui.core.view.impl.FXPageContent;

import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.stage.Stage;

/**
 * The {@link BaseFXController} provides the most basic features any
 * {@link Controller} should provide such as refreshing the current visible page
 * 
 * @author Dominic Stalder
 */
public abstract class BaseFXController<IN_VO extends PageModel, OUT_VO extends PageModel>
      extends BaseController<IN_VO, OUT_VO> implements Initializable {

   @Override
   public void initialize(Page<IN_VO, OUT_VO> page) {
      super.initialize(page);
   }

   @Override
   public void initialize(URL arg0, ResourceBundle arg1) {
      // Nothing to do by default
   }

   /**
    * Shows the content of this controller. For the most common case we cast the
    * 'incoming' datamodl to the 'outgoing' in order to initialize it. If this
    * behavior is unwanted, override this method
    */
   @SuppressWarnings("unchecked")
   @Override
   public void show() {
      initDataModel((IN_VO) dataModel);
      super.show();
      Optional<Stage> optionalStage = getStage(page);
      optionalStage.ifPresent(stage -> stage.show());
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
}
