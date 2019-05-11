/**
 * 
 */
package com.myownb3.dominic.ui.core.pages.mainpage.view;

import java.io.IOException;
import java.util.Optional;

import com.myownb3.dominic.librarys.text.res.TextLabel;
import com.myownb3.dominic.timerecording.app.TimeRecorder;
import com.myownb3.dominic.ui.app.TimeRecordingTray;
import com.myownb3.dominic.ui.core.pages.mainpage.control.MainWindowController;
import com.myownb3.dominic.ui.core.pages.mainpage.model.MainWindowPageModel;
import com.myownb3.dominic.ui.core.view.impl.AbstractFXPage;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author Dominic
 *
 */
public class MainWindowPage extends AbstractFXPage<MainWindowPageModel, MainWindowPageModel> {

    private ViewList currentView = ViewList.NONE;
    
    /**
     * @param timeRecordingTray
     * @param stage
     */
    public MainWindowPage(TimeRecordingTray timeRecordingTray, Stage stage) {
	super(stage);
	((MainWindowController) getController()).setTimeRecordingTray(timeRecordingTray);
    }
    
    @Override
    protected void initializeScene(FXMLLoader loader, Optional<Stage> optionalStage) throws IOException {
        super.initializeScene(loader, optionalStage);
        Stage stage = optionalStage.get();
        stage.setScene(new Scene(loader.getRoot()));
    }
    
    @Override
    public void show() {
        super.show();
	showInternal();
    }

    @Override
    public void refresh() {
        super.refresh();
        showInternal();
    }
    
    private void showInternal() {
	Stage stage = getStage().get();
	stage.setScene(new Scene(getRootParent()));
	stage.sizeToScene();
	stage.setTitle(TextLabel.APPLICATION_TITLE + " v" + TimeRecorder.VERSION);
	
	switch (currentView) {
	case INPUT_MASK:
	    showInputMask();
	    break;
	case OVERVIEW_VIEW:
	    showOverviewView();
	    break;
	default:
	    break;
	}
    }

    public void showOverviewView() {
	currentView = ViewList.OVERVIEW_VIEW;
	((MainWindowController) getController()).showOverviewView(getStage().get());
    }

    public void showInputMask() {
	currentView = ViewList.INPUT_MASK;
	((MainWindowController) getController()).showInputMask(getStage().get());
    }
}
