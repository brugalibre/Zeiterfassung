/**
 * 
 */
package com.myownb3.dominic.ui.app.pages.mainpage.view;

import java.io.IOException;
import java.util.Optional;

import com.myownb3.dominic.ui.app.TimeRecordingTray;
import com.myownb3.dominic.ui.app.pages.mainpage.control.MainWindowController;
import com.myownb3.dominic.ui.app.pages.mainpage.model.MainWindowPageModel;
import com.myownb3.dominic.ui.core.view.impl.AbstractFXPage;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
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
	super(stage);
	getMainWindowPageController().setTimeRecordingTray(timeRecordingTray);
    }

    @Override
    protected void initializeScene(FXMLLoader loader, Optional<Stage> optionalStage) throws IOException {
	super.initializeScene(loader, optionalStage);
	Stage stage = optionalStage.get();
	stage.setScene(new Scene(loader.getRoot()));
    }
    
    public void showOverviewView() {
	getMainWindowPageController().showOverviewView(getStage().get());
    }

    public void showInputMask() {
	getMainWindowPageController().showInputMask(getStage().get());
    }

    private MainWindowController getMainWindowPageController() {
	return (MainWindowController) getController();
    }

}
