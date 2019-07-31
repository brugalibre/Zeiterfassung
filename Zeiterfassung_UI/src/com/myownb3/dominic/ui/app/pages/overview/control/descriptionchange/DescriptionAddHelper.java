 /**
 * 
 */
package com.myownb3.dominic.ui.app.pages.overview.control.descriptionchange;

import static com.myownb3.dominic.timerecording.core.work.businessday.ValueTypes.DESCRIPTION;

import java.util.Optional;

import com.myownb3.dominic.timerecording.core.callbackhandler.BusinessDayChangedCallbackHandler;
import com.myownb3.dominic.timerecording.core.callbackhandler.impl.BusinessDayChangedCallbackHandlerImpl;
import com.myownb3.dominic.timerecording.core.callbackhandler.impl.ChangedValue;
import com.myownb3.dominic.ui.app.pages.overview.control.UIRefresher;
import com.myownb3.dominic.ui.app.pages.overview.model.table.BusinessDayIncTableRowValue;
import com.myownb3.dominic.ui.app.pages.overview.view.OverviewPage;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * 
 * The {@link DescriptionAddHelper} helps to add a new description to a row on
 * the {@link OverviewPage}
 * 
 * @author Dominic
 * 
 */
public class DescriptionAddHelper {

    private UIRefresher uiRefresher;

    public DescriptionAddHelper(UIRefresher uiRefresher) {
	this.uiRefresher = uiRefresher;
    }

    /**
     * Shows an input field in order to enter the new description
     * 
     * @param event
     *            the {@link ActionEvent}whose triggered this helper
     * @param x
     * @param y
     * @param tableView
     *            the tableview
     */
    public void showInputField(ActionEvent event, double x, double y, TableView<BusinessDayIncTableRowValue> tableView) {
	Optional<BusinessDayIncTableRowValue> optionalBusinessDayIncTableRowValue = Optional.ofNullable(tableView.getSelectionModel().getSelectedItem());
	optionalBusinessDayIncTableRowValue.ifPresent(businessDayIncTableRowValue -> showInputField(businessDayIncTableRowValue, x, y));
	event.consume();
    }

    private void showInputField(BusinessDayIncTableRowValue businessDayIncTableRowValue, double x, double y) {
	
	TextField field = new TextField();
	Stage stage = new Stage();
	EventHandler<? super KeyEvent> keyEventHandler = keyEvent -> handleKeyPressed(keyEvent, field, stage, businessDayIncTableRowValue.getNumberAsInt());
	initContent(stage, field, keyEventHandler, x, y);
	stage.show();
    }

    private void initContent(Stage stage, TextField field, EventHandler<? super KeyEvent> keyEventHandler, double x, double y) {
	Pane borderPane = new Pane();
	borderPane.getChildren().add(field);
	Scene scene = new Scene(borderPane);
	scene.setOnKeyPressed(keyEventHandler);

	stage.setScene(scene);
	stage.initStyle(StageStyle.UNDECORATED);
	stage.setAlwaysOnTop(true);
	stage.setX(x);
	stage.setY(y);
    }

    private void handleKeyPressed(KeyEvent keyEvent, TextField textField, Stage stage, int indexOfChangedEntry) {
	if (keyEvent.getCode() == KeyCode.ESCAPE) {
	    stage.close();
	} else if (keyEvent.getCode() == KeyCode.ENTER) {
	    BusinessDayChangedCallbackHandlerImpl handler = new BusinessDayChangedCallbackHandlerImpl();
	    handler.handleBusinessDayChanged(ChangedValue.of(indexOfChangedEntry, textField.getText(), DESCRIPTION));
	    closeStageAndRefreshUI(stage);
	}
	keyEvent.consume();
    }

    private void closeStageAndRefreshUI(Stage stage) {
	stage.close();
	uiRefresher.refreshUI();
    }
}