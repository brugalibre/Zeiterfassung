/**
 * 
 */
package com.myownb3.dominic.ui.app.pages.mainpage.control;

import java.io.File;

import com.myownb3.dominic.librarys.pictures.PictureLibrary;
import com.myownb3.dominic.librarys.text.res.TextLabel;
import com.myownb3.dominic.timerecording.app.TimeRecorder;
import com.myownb3.dominic.timerecording.core.work.businessday.BusinessDay;
import com.myownb3.dominic.ui.app.TimeRecordingTray;
import com.myownb3.dominic.ui.app.pages.mainpage.model.MainWindowPageModel;
import com.myownb3.dominic.ui.app.pages.overview.control.OverviewController;
import com.myownb3.dominic.ui.app.pages.stopbusinessday.control.FinishAction;
import com.myownb3.dominic.ui.app.pages.stopbusinessday.control.StopBusinessDayIncrementController;
import com.myownb3.dominic.ui.app.pages.stopbusinessday.view.StopBusinessDayIncrementPage;
import com.myownb3.dominic.ui.core.control.impl.BaseFXController;
import com.myownb3.dominic.ui.core.dialog.FileImportDialogHelper;
import com.myownb3.dominic.ui.core.model.resolver.PageModelResolver;
import com.myownb3.dominic.ui.core.view.Page;

import javafx.fxml.FXML;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * @author Dominic
 * 
 */
public class MainWindowController extends BaseFXController<MainWindowPageModel, MainWindowPageModel> {

   @FXML
   private StackPane mainPanel;

   @FXML
   private OverviewController overviewPanelController;
   @FXML
   private Region overviewPanel;
   @FXML
   private StopBusinessDayIncrementController stopBusinessDayIncrementPanelController;
   @FXML
   private Region stopBusinessDayIncrementPanel;

   private TimeRecordingTray timeRecordingTray;
   private FileImportDialogHelper fileImportHelper;

   @Override
   public void initialize(Page<MainWindowPageModel, MainWindowPageModel> mainWindowPage) {

      super.initialize(mainWindowPage);
      overviewPanelController.init(this);
      stopBusinessDayIncrementPanelController.setMainWindowController(this);

      Stage stage = getStage();
      stage.setTitle(TextLabel.APPLICATION_TITLE + " v" + TimeRecorder.VERSION);
      stage.getIcons().add(PictureLibrary.getClockImageIcon());
      fileImportHelper = new FileImportDialogHelper();
   }

   /**
    * Refreshes the {@link StopBusinessDayIncrementPage} if the content is currently visible
    */
   public void refreshStopBusinessDayPage() {
      if (stopBusinessDayIncrementPanel.isVisible()) {
         stopBusinessDayIncrementPanelController.refresh();
      }
   }

   public void showInputMask(Stage stage) {

      mainPanel.getChildren().clear();
      mainPanel.getChildren().add(stopBusinessDayIncrementPanel);

      initStage4NewComponent(stage, stopBusinessDayIncrementPanel);
      stopBusinessDayIncrementPanelController.show();

      stage.toFront();
      stage.setOnCloseRequest(stopBusinessDayIncrementPanelController);
      show();
   }

   public void showOverviewView(Stage stage) {

      mainPanel.getChildren().clear();
      mainPanel.getChildren().add(overviewPanel);

      overviewPanelController.show();
      initStage4NewComponent(stage, overviewPanel);

      stage.setOnCloseRequest(overviewPanelController);
      show();
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

   private void initStage4NewComponent(Stage stage, Region region) {
      mainPanel.setPrefWidth(region.getPrefWidth());
      mainPanel.setPrefHeight(region.getPrefHeight());
      stage.setWidth(region.getPrefWidth());
      stage.setHeight(region.getPrefHeight());
      stage.setMinWidth(region.getPrefWidth());
      stage.setMinHeight(region.getPrefHeight());
      stage.setResizable(false);
      stage.sizeToScene();
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

   public void dispose() {
      page.hide();
   }

   /**
    * Clears all entrys in the BusinessDay and updates the system-tray
    */
   public void clearBusinessDayContents() {
      timeRecordingTray.clearBusinessDayContents();
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
      // Nothing to do since this Page only contains two sub pages
   }

   public void setTimeRecordingTray(TimeRecordingTray timeRecordingTray) {
      this.timeRecordingTray = timeRecordingTray;
      overviewPanelController.setTimeRecordingTray(timeRecordingTray);
      Stage stage = getStage();
      overviewPanelController.setMainPanel(stage);
   }
}
