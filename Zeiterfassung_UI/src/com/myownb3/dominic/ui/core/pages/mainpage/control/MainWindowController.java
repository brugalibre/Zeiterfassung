/**
 * 
 */
package com.myownb3.dominic.ui.core.pages.mainpage.control;

import com.myownb3.dominic.librarys.pictures.PictureLibrary;
import com.myownb3.dominic.librarys.text.res.TextLabel;
import com.myownb3.dominic.timerecording.app.TimeRecorder;
import com.myownb3.dominic.timerecording.callback.handler.BusinessDayChangedCallbackHandler;
import com.myownb3.dominic.timerecording.work.businessday.BusinessDay;
import com.myownb3.dominic.timerecording.work.businessday.BusinessDayChangedCallbackHandlerImpl;
import com.myownb3.dominic.ui.app.TimeRecordingTray;
import com.myownb3.dominic.ui.core.control.impl.BaseFXController;
import com.myownb3.dominic.ui.core.model.resolver.PageModelResolver;
import com.myownb3.dominic.ui.core.pages.mainpage.model.MainWindowPageModel;
import com.myownb3.dominic.ui.core.pages.overview.control.OverviewController;
import com.myownb3.dominic.ui.core.pages.userinput.control.StopBusinessDayIncrementController;
import com.myownb3.dominic.ui.core.view.Page;
import com.myownb3.dominic.ui.core.view.impl.FXPageContent;

import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
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
    private BorderPane overviewPanel;
    @FXML
    private StopBusinessDayIncrementController stopBusinessDayIncrementPanelController;
    @FXML
    private VBox stopBusinessDayIncrementPanel;

    private TimeRecordingTray timeRecordingTray;

    @Override
    public void initialize(Page<MainWindowPageModel, MainWindowPageModel> mainWindowPage) {

	super.initialize(mainWindowPage);
	overviewPanelController.setMainWindowController(this);
	stopBusinessDayIncrementPanelController.setMainWindowController(this);

	FXPageContent pageContent = (FXPageContent) mainWindowPage.getContent();
	Stage stage = pageContent.getStage().get();
	stage.setTitle(TextLabel.APPLICATION_TITLE + " v" + TimeRecorder.VERSION);
	stage.setIconified(true);
	stage.getIcons().add(PictureLibrary.getClockImageIcon());
    }

    public void showInputMask() {

	overviewPanel.setVisible(false);
	stopBusinessDayIncrementPanel.setVisible(true);
	mainPanel.getChildren().clear();
	mainPanel.getChildren().add(stopBusinessDayIncrementPanel);
	stopBusinessDayIncrementPanelController.show();
	show();
    }

    public void showOverviewView() {
	overviewPanel.setVisible(true);
	stopBusinessDayIncrementPanel.setVisible(false);
	mainPanel.getChildren().clear();
	mainPanel.getChildren().add(overviewPanel);
	show();
    }

    /**
     * Lets the current shown window disappears. If the given boolean is true, the
     * {@link BusinessDay} is checked for redundant entry
     * 
     * @param done
     */
    public void finishOrAbortAndDispose(boolean done) {
	if (done) {
	    TimeRecorder.checkForRedundancy();
	    timeRecordingTray.updateUIStates(false);
	} else {
	    TimeRecorder.resume();
	}
	dispose();
    }

    public void dispose() {
	((FXPageContent) page.getContent()).getStage().get().hide();
    }

    /**
     * 
     */
    public void clearBusinessDayContents() {
	timeRecordingTray.clearBusinessDayContents();
    }

    /**
     * 
     */
    public void export() {
	TimeRecorder.export();
    }

    public void chargeOff() {
	TimeRecorder.book();
	refresh();
    }

    @Override
    protected void refresh() {
	// First check for increments which can be merged (e.g. if there was an charged
	// and a not charged increment which are equal but still handled separately
	// (because the one was already charged and the 2nd not). And as soon as the 2nd
	// one is charged, they can be merged
	TimeRecorder.checkForRedundancy();
	// if (overviewController.page.isVisible()) {
	// showOverviewView(TimeRecorder.getBussinessDay());
	// }
	timeRecordingTray.updateUIStates(false);
    }

    private BusinessDayChangedCallbackHandler getCallbackHandler(BusinessDay bussinessDay) {
	return changeValue -> {
	    new BusinessDayChangedCallbackHandlerImpl(bussinessDay).handleBusinessDayChanged(changeValue);
	    showOverviewView();
	};
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
