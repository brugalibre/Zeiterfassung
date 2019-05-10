/**
 * 
 */
package com.myownb3.dominic.ui.core.pages.mainpage.view;

import com.myownb3.dominic.ui.app.TimeRecordingTray;
import com.myownb3.dominic.ui.core.pages.mainpage.control.MainWindowController;
import com.myownb3.dominic.ui.core.pages.mainpage.model.MainWindowPageModel;
import com.myownb3.dominic.ui.core.view.impl.AbstractFXPage;

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
}
