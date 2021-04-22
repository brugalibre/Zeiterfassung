/**
 * 
 */
package com.adcubum.timerecording.ui.app.pages.mainpage.view;

import com.adcubum.timerecording.ui.app.TimeRecordingTray;
import com.adcubum.timerecording.ui.app.pages.mainpage.control.MainWindowController;
import com.adcubum.timerecording.ui.app.pages.mainpage.model.MainWindowPageModel;
import com.adcubum.timerecording.ui.app.pages.stopbusinessday.view.StopBusinessDayIncrementPage;
import com.adcubum.timerecording.ui.core.view.impl.AbstractFXPage;

import javafx.stage.Stage;

/**
 * @author Dominic
 *
 */
public class MainWindowPage extends AbstractFXPage<MainWindowPageModel, MainWindowPageModel> {

   /**
    * @param timeRecordingTray
    * @param stage
    */
   public MainWindowPage(TimeRecordingTray timeRecordingTray, Stage stage) {
      super(stage, false);
      getMainWindowPageController().setTimeRecordingTray(timeRecordingTray);
   }

   public void showOverviewView() {
      getMainWindowPageController().showOverviewView(getStage());
   }

   public void showComeAndGoOverview() {
      getMainWindowPageController().showComeAndGoOverview(getStage());
   }

   public void showInputMask() {
      getMainWindowPageController().showInputMask(getStage());
   }

   /**
    * Refreshes the {@link StopBusinessDayIncrementPage} if the content is currently visible
    */
   public void refreshStopBusinessDayPage() {
      if (getStage().isShowing()) {
         getMainWindowPageController().refreshStopBusinessDayPage();
      }
   }

   private MainWindowController getMainWindowPageController() {
      return (MainWindowController) getController();
   }

   /**
    * Opens a dialog in order to choose a file to import
    */
   public void showImportDialog() {
      getMainWindowPageController().showImportDialog(getStage());
   }

   private Stage getStage() {
      return getStageOptional()
            .orElseThrow(IllegalStateException::new);
   }
}
