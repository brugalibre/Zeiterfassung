/**
 * 
 */
package com.adcubum.timerecording.ui.app.pages.mainpage.control;

import java.io.File;

import com.adcubum.librarys.text.res.TextLabel;
import com.adcubum.timerecording.app.TimeRecorder;
import com.adcubum.timerecording.core.work.businessday.BusinessDay;
import com.adcubum.timerecording.librarys.pictures.PictureLibrary;
import com.adcubum.timerecording.ui.app.TimeRecordingTray;
import com.adcubum.timerecording.ui.app.pages.comeandgo.control.ComeAndGoOverviewController;
import com.adcubum.timerecording.ui.app.pages.mainpage.control.callback.MainWindowCallbackHandler;
import com.adcubum.timerecording.ui.app.pages.mainpage.model.MainWindowPageModel;
import com.adcubum.timerecording.ui.app.pages.overview.control.OverviewController;
import com.adcubum.timerecording.ui.app.pages.stopbusinessday.control.FinishAction;
import com.adcubum.timerecording.ui.app.pages.stopbusinessday.control.StopBusinessDayIncrementController;
import com.adcubum.timerecording.ui.app.pages.stopbusinessday.view.StopBusinessDayIncrementPage;
import com.adcubum.timerecording.ui.core.control.Controller;
import com.adcubum.timerecording.ui.core.control.impl.BaseFXController;
import com.adcubum.timerecording.ui.core.dialog.FileImportDialogHelper;
import com.adcubum.timerecording.ui.core.model.PageModel;
import com.adcubum.timerecording.ui.core.model.resolver.PageModelResolver;
import com.adcubum.timerecording.ui.core.view.Page;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * @author Dominic
 * 
 */
public class MainWindowController extends BaseFXController<MainWindowPageModel, MainWindowPageModel> {

   @FXML
   private OverviewController overviewPanelController;
   @FXML
   private Region overviewPanel;

   @FXML
   private ComeAndGoOverviewController comeAndGoOverviewPanelController;
   @FXML
   private Region comeAndGoOverviewPanel;

   @FXML
   private StopBusinessDayIncrementController stopBusinessDayIncrementPanelController;
   @FXML
   private Region stopBusinessDayIncrementPanel;

   private TimeRecordingTray timeRecordingTray;
   private FileImportDialogHelper fileImportHelper;

   @Override
   public void initialize(Page<MainWindowPageModel, MainWindowPageModel> mainWindowPage) {
      super.initialize(mainWindowPage);
      comeAndGoOverviewPanelController.setOnFinishHandler(this::finishOrAbortAndDispose);
      comeAndGoOverviewPanelController.addOnResizeHandler(dimension -> initStage4NewComponent(getStage(), dimension));
      overviewPanelController.addOnResizeHandler(dimension -> initStage4NewComponent(getStage(), dimension));
      stopBusinessDayIncrementPanelController.setOnFinishHandler(this::finishOrAbortAndDispose);
      Stage stage = getStage();
      stage.setTitle(TextLabel.APPLICATION_TITLE + " v" + TimeRecorder.VERSION);
      stage.getIcons().add(PictureLibrary.getClockImageIcon());
      fileImportHelper = new FileImportDialogHelper();
   }

   /**
    * Refreshes the {@link StopBusinessDayIncrementPage} if the content is currently visible
    */
   public void refreshStopBusinessDayPage() {
      if (containsRegionAlready(stopBusinessDayIncrementPanel)) {
         stopBusinessDayIncrementPanelController.refresh();
      } else if (containsRegionAlready(comeAndGoOverviewPanel)) {
         comeAndGoOverviewPanelController.refresh();
      }
   }

   public void showInputMask(Stage stage) {
      if (containsRegionAlready(stopBusinessDayIncrementPanel)) {
         stopBusinessDayIncrementPanelController.refresh();
      }
      showPane(stage, stopBusinessDayIncrementPanel, stopBusinessDayIncrementPanelController, stopBusinessDayIncrementPanelController);
   }

   public void showOverviewView(Stage stage) {
      if (containsRegionAlready(overviewPanel)) {
         overviewPanelController.refresh();
      }
      showPane(stage, overviewPanel, overviewPanelController, overviewPanelController);
   }

   public void showComeAndGoOverview(Stage stage) {
      if (containsRegionAlready(comeAndGoOverviewPanel)) {
         comeAndGoOverviewPanelController.refresh();
      }
      showPane(stage, comeAndGoOverviewPanel, comeAndGoOverviewPanelController, comeAndGoOverviewPanelController);
   }

   private boolean containsRegionAlready(Region region) {
      return rootPane.getChildren().contains(region) && region.isVisible();
   }

   private void showPane(Stage stage, Region content4Controller, Controller<PageModel, ?> controller, EventHandler<WindowEvent> eventHandler) {
      rootPane.getChildren().clear();
      rootPane.getChildren().add(content4Controller);

      controller.show(dataModel);
      initStage4NewComponent(stage, controller.getDimension());

      stage.setOnCloseRequest(eventHandler);
      show(dataModel);
   }

   /**
    * Opens a dialog in order to choose a file to import. If there was any file
    * selected this file is passed to the {@link TimeRecorder} in order to
    * import a new {@link BusinessDay}
    * 
    * @param stage
    *        the current stage
    */
   public void showImportDialog(Stage stage) {
      File selectedFile = fileImportHelper.showImportDialogAndReturnFile(stage);
      if (selectedFile != null) {
         timeRecordingTray.importBusinessDayFromFile(selectedFile);
      }
   }

   /**
    * Lets the current shown window disappears. If the given boolean is true, the
    * {@link BusinessDay} is checked for redundant entry
    * 
    * @param finishAction
    */
   public void finishOrAbortAndDispose(FinishAction finishAction) {

      switch (finishAction) {
         case ABORT:
         case FINISH:// Fall through
            timeRecordingTray.updateUIStates();
            dispose();
            break;
         case RESUME:
            timeRecordingTray.resume();
            dispose();
            break;
         default:
      }
   }

   private void dispose() {
      page.hide();
   }

   @Override
   public void refresh() {
      timeRecordingTray.updateUIStates();
   }

   @Override
   protected PageModelResolver<MainWindowPageModel, MainWindowPageModel> createPageModelResolver() {
      return oldPageModel -> oldPageModel == null ? new MainWindowPageModel() : oldPageModel;
   }

   @Override
   protected void setBinding(MainWindowPageModel pageVO) {
      // Nothing to do since this Page contains only sub pages
   }

   public void setTimeRecordingTray(TimeRecordingTray timeRecordingTray) {
      Stage stage = getStage();
      this.timeRecordingTray = timeRecordingTray;
      overviewPanelController.init(new MainWindowCallbackHandlerImpl(this, timeRecordingTray), stage);
      comeAndGoOverviewPanelController.init(stage);
   }

   public static class MainWindowCallbackHandlerImpl implements MainWindowCallbackHandler {

      private MainWindowController mainWindowController;
      private TimeRecordingTray timeRecordingTray;

      public MainWindowCallbackHandlerImpl(MainWindowController mainWindowController, TimeRecordingTray timeRecordingTray) {
         this.mainWindowController = mainWindowController;
         this.timeRecordingTray = timeRecordingTray;
      }

      @Override
      public void updateUIStates() {
         timeRecordingTray.updateUIStates();
      }

      @Override
      public void export() {
         timeRecordingTray.export();
      }

      @Override
      public void clearBusinessDayContentsAndDispose() {
         timeRecordingTray.clearBusinessDayContents();
         mainWindowController.dispose();
      }
   }
}
