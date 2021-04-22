/**
 * 
 */
package com.adcubum.timerecording.ui.app.pages.comeandgo.control;

import static java.util.Objects.requireNonNull;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import com.adcubum.timerecording.app.TimeRecorder;
import com.adcubum.timerecording.ui.app.pages.comeandgo.model.ComeAndGoOverviewPageModel;
import com.adcubum.timerecording.ui.app.pages.comeandgo.model.resolver.ComeAndGoPageModelResolver;
import com.adcubum.timerecording.ui.app.pages.comeandgo.view.ComeAndGoOverviewPage;
import com.adcubum.timerecording.ui.app.pages.stopbusinessday.control.FinishAction;
import com.adcubum.timerecording.ui.app.pages.stopbusinessday.control.StopBusinessDayIncrementController;
import com.adcubum.timerecording.ui.app.styles.Styles;
import com.adcubum.timerecording.ui.core.control.impl.BaseFXController;
import com.adcubum.timerecording.ui.core.model.PageModel;
import com.adcubum.timerecording.ui.core.model.resolver.PageModelResolver;
import com.adcubum.timerecording.ui.core.view.impl.region.DimensionImpl;
import com.adcubum.timerecording.ui.core.view.region.Dimension;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * @author Dominic
 *
 */
public class ComeAndGoOverviewController extends BaseFXController<PageModel, ComeAndGoOverviewPageModel>
      implements EventHandler<WindowEvent> {

   private Consumer<FinishAction> onFinishHandler;
   @FXML
   private Button startAddBDIncrementButton;
   @FXML
   private Button clearAllComeAndGoes;
   @FXML
   private Label comeAndGoesLabel;

   @FXML
   private Region comeAndGoOverviewPane;
   @FXML
   private Region addBDIncrementContentPane;
   @FXML
   private VBox comeAndGoesContent;

   @FXML
   private StopBusinessDayIncrementController addBDIncrementContentPaneController;
   private Stage stage;
   private ComeAndGoShowMode showMode;

   @Override
   public void initialize(URL url, ResourceBundle resourceBundle) {
      initialize(new ComeAndGoOverviewPage(this));
      addBDIncrementContentPaneController.setOnFinishHandler(this::onFinishFromSubpage);
      rootPane.getChildren().remove(addBDIncrementContentPane);
      comeAndGoesLabel.getStyleClass().add(Styles.TITLE);
      showMode = ComeAndGoShowMode.COME_AND_GO_PAGE;
   }

   @Override
   public void show(PageModel dataModelIn) {
      super.show(dataModelIn);
      if (showMode == ComeAndGoShowMode.COME_AND_GO_PAGE) {
         addOverviewPaneAndShowComeAndGoes();
      } else {
         addBDIncrementContentPaneController.refresh();
      }
   }

   private void addOverviewPaneAndShowComeAndGoes() {
      rootPane.getChildren().clear();
      rootPane.getChildren().add(comeAndGoOverviewPane);
      startAddBDIncrementButton.setDisable(dataModel.isStartAddBDIncrementButtonDisabled());
      clearAllComeAndGoes.setDisable(dataModel.isClearAllComeAndGoesButtonDisabled());
      comeAndGoOverviewPane.requestFocus();
      showComeAndGoes();
   }

   @Override
   public void handle(WindowEvent event) {
      addBDIncrementContentPaneController.handle(event);
      if (event.getEventType() == WindowEvent.WINDOW_CLOSE_REQUEST) {
         rootPane.getChildren().remove(addBDIncrementContentPane);
      }
   }

   private void showComeAndGoes() {
      comeAndGoesContent.getChildren().clear();
      for (String comeAndGoRepresentation : dataModel.getComeAndGoesRepresentations()) {
         comeAndGoesContent.getChildren().add(new Label(comeAndGoRepresentation));
      }
   }

   @Override
   protected void setBinding(ComeAndGoOverviewPageModel pageModel) {
      startAddBDIncrementButton.textProperty().bindBidirectional(pageModel.getStartAddBDIncrementButtonProperty());
      clearAllComeAndGoes.textProperty().bindBidirectional(pageModel.getClearAllComeAndGoesButtonProperty());
      comeAndGoesLabel.textProperty().bindBidirectional(pageModel.getComeAndGoesLabelProperty());
   }

   @FXML
   private void onAction(ActionEvent actionEvent) {
      if (actionEvent.getSource() == startAddBDIncrementButton) {
         startAddBDIncrements();
      } else if (actionEvent.getSource() == clearAllComeAndGoes) {
         TimeRecorder.INSTANCE.clearComeAndGoes();
         onFinishHandler.accept(FinishAction.ABORT);
      }
   }

   private void startAddBDIncrements() {
      showMode = ComeAndGoShowMode.STOP_BD_INC_PAGE;
      rootPane.getChildren().clear();
      rootPane.getChildren().add(addBDIncrementContentPane);
      addBDIncrementContentPaneController.show(dataModel);
      initStage4NewComponent(stage, addBDIncrementContentPaneController.getDimension());
   }

   private void onFinishFromSubpage(FinishAction finishAction) {
      showMode = ComeAndGoShowMode.COME_AND_GO_PAGE;
      switch (finishAction) {
         case ABORT:
            handleAbortOrResume();
            break;
         case FINISH:
            handleFinish(finishAction);
            break;
         case RESUME:
            handleAbortOrResume();
            break;
         default:
            break;
      }
   }

   private void handleAbortOrResume() {
      show(dataModel);
      initStage4NewComponent(stage, getDimension());
   }

   private void handleFinish(FinishAction finishAction) {
      requireNonNull(onFinishHandler, "No OnFinishHandler, call setOnFinishHandler first!");
      if (dataModel.hasUnfinishedComeAndGoes()) {
         dataModel.prepareForNextIteration(dataModel.getCurrentBDIncTimeSnippet());
         addBDIncrementContentPaneController.show(dataModel);
      } else {
         dataModel.addAllBusinessDayIncrements2BusinessDay();
         onFinishHandler.accept(finishAction);
      }
   }

   public void setOnFinishHandler(Consumer<FinishAction> onFinishHandler) {
      this.onFinishHandler = requireNonNull(onFinishHandler);
   }

   public void init(Stage stage) {
      this.stage = requireNonNull(stage);
   }

   @Override
   public Dimension getDimension() {
      if (showMode == ComeAndGoShowMode.COME_AND_GO_PAGE) {
         return new DimensionImpl(comeAndGoOverviewPane.getPrefWidth(), comeAndGoOverviewPane.getPrefHeight());
      }
      return addBDIncrementContentPaneController.getDimension();
   }

   @Override
   protected PageModelResolver<PageModel, ComeAndGoOverviewPageModel> createPageModelResolver() {
      return new ComeAndGoPageModelResolver();
   }
}
