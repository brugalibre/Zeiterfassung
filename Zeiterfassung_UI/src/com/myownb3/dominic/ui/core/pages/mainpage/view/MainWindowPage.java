/**
 * 
 */
package com.myownb3.dominic.ui.core.pages.mainpage.view;

import java.io.IOException;
import java.util.Optional;

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

    public void showOverviewView() {
	((MainWindowController) getController()).showOverviewView(getStage().get());
    }

    public void showInputMask() {
	((MainWindowController) getController()).showInputMask(getStage().get());
    }
}
