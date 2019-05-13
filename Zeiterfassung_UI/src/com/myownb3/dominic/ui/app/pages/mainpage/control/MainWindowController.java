/**
 * 
 */
package com.myownb3.dominic.ui.app.pages.mainpage.control;

import com.myownb3.dominic.librarys.pictures.PictureLibrary;
import com.myownb3.dominic.librarys.text.res.TextLabel;
import com.myownb3.dominic.timerecording.app.TimeRecorder;
import com.myownb3.dominic.timerecording.work.businessday.BusinessDay;
import com.myownb3.dominic.ui.app.TimeRecordingTray;
import com.myownb3.dominic.ui.app.pages.mainpage.model.MainWindowPageModel;
import com.myownb3.dominic.ui.app.pages.overview.control.OverviewController;
import com.myownb3.dominic.ui.app.pages.stopbusinessday.control.StopBusinessDayIncrementController;
import com.myownb3.dominic.ui.core.control.impl.BaseFXController;
import com.myownb3.dominic.ui.core.model.resolver.PageModelResolver;
import com.myownb3.dominic.ui.core.view.Page;
import com.myownb3.dominic.ui.core.view.impl.FXPageContent;

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

    @Override
    public void initialize(Page<MainWindowPageModel, MainWindowPageModel> mainWindowPage) {

	super.initialize(mainWindowPage);
	overviewPanelController.init(this);
	stopBusinessDayIncrementPanelController.setMainWindowController(this);

	FXPageContent pageContent = (FXPageContent) mainWindowPage.getContent();
	Stage stage = pageContent.getStage().get();
	stage.setTitle(TextLabel.APPLICATION_TITLE + " v" + TimeRecorder.VERSION);
	stage.setIconified(true);
	stage.getIcons().add(PictureLibrary.getClockImageIcon());
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

	initStage4NewComponent(stage, overviewPanel);

	overviewPanelController.show();
	stage.setOnCloseRequest(null);
	show();
    }

    private void initStage4NewComponent(Stage stage, Region region) {
	mainPanel.setPrefWidth(region.getPrefWidth());
	mainPanel.setPrefHeight(region.getPrefHeight());
	stage.setWidth(region.getPrefWidth());
	stage.setHeight(region.getPrefHeight());
	stage.setResizable(false);
    }

    /**
     * Lets the current shown window disappears. If the given boolean is true, the
     * {@link BusinessDay} is checked for redundant entry
     * 
     * @param done
     */
    public void finishOrAbortAndDispose(boolean done) {
	if (done) {
	    timeRecordingTray.updateUIStates();
	} else {
	    TimeRecorder.resume();
	}
	dispose();
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
    protected void refresh() {
	timeRecordingTray.updateUIStates();
    }

    @Override
    protected PageModelResolver<MainWindowPageModel, MainWindowPageModel> createPageModelResolver() {
	return oldPageModel -> oldPageModel == null ? new MainWindowPageModel() : oldPageModel;
    }

    @Override
    protected void setBinding(MainWindowPageModel pageVO) {
	// Nothing to do since this Page only contains to sub pages
    }

    public void setTimeRecordingTray(TimeRecordingTray timeRecordingTray) {
	this.timeRecordingTray = timeRecordingTray;
    }

}
