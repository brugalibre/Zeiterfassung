/**
 * 
 */
package com.myownb3.dominic.ui.app.pages.stopbusinessday.control;

import java.awt.Toolkit;
import java.net.URL;
import java.util.ResourceBundle;

import com.myownb3.dominic.timerecording.charge.ChargeType;
import com.myownb3.dominic.ui.app.pages.mainpage.control.MainWindowController;
import com.myownb3.dominic.ui.app.pages.stopbusinessday.model.StopBusinessDayIncrementPageModel;
import com.myownb3.dominic.ui.app.pages.stopbusinessday.model.resolver.StopBusinessDayIncrementPageModelResolver;
import com.myownb3.dominic.ui.app.pages.stopbusinessday.view.StopBusinessDayIncrementPage;
import com.myownb3.dominic.ui.app.styles.Styles;
import com.myownb3.dominic.ui.core.control.impl.BaseFXController;
import com.myownb3.dominic.ui.core.model.resolver.PageModelResolver;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.WindowEvent;

/**
 * @author Dominic
 *
 */
public class StopBusinessDayIncrementController
	extends BaseFXController<StopBusinessDayIncrementPageModel, StopBusinessDayIncrementPageModel>
	implements EventHandler<WindowEvent> {

    @FXML
    private Label ticketNumberLabel;
    @FXML
    private TextField ticketNumberTextField;

    @FXML
    private Label descriptionLabel;
    @FXML
    private TextField descriptionTextField;

    @FXML
    private Label beginLabel;
    @FXML
    private TextField beginTextField;

    @FXML
    private Label endLabel;
    @FXML
    private TextField endTextField;

    @FXML
    private Label amountOfHoursLabel;
    @FXML
    private TextField amountOfHoursTextField;

    @FXML
    private Label kindOfServiceLabel;
    @FXML
    private ComboBox<String> kindOfServiceComboBox;

    @FXML
    private Button okButton;
    @FXML
    private Button abortButton;

    private MainWindowController mainWindowController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
	initialize(new StopBusinessDayIncrementPage(this, url));
	kindOfServiceComboBox.getSelectionModel().selectFirst();
    }

    @Override
    public void show() {
	super.show();
	ticketNumberTextField.requestFocus();
	ticketNumberTextField.selectAll();
    }

    @Override
    public void handle(WindowEvent event) {
	if (event.getEventType() == WindowEvent.WINDOW_CLOSE_REQUEST) {
	    cancel();
	}
    }

    @FXML
    private void onAction(ActionEvent actionEvent) {
	if (isFinish(actionEvent)) {
	    submit();
	} else if (actionEvent.getSource() == abortButton) {
	    cancel();
	}
    }

    private boolean isFinish(ActionEvent actionEvent) {
	return actionEvent.getSource() == okButton || actionEvent.getSource() == amountOfHoursTextField;
    }

    private void cancel() {
	dispose(false);
    }

    @Override
    protected PageModelResolver<StopBusinessDayIncrementPageModel, StopBusinessDayIncrementPageModel> createPageModelResolver() {
	return new StopBusinessDayIncrementPageModelResolver();
    }

    private void dispose(boolean success) {
	mainWindowController.finishOrAbortAndDispose(success);
	amountOfHoursTextField.getStyleClass().remove(Styles.INVALID_INPUT_LABEL);
	amountOfHoursTextField.getStyleClass().remove(Styles.INVALID_INPUT_HOVER_LABEL);
    }

    private void submit() {
	if (isInputValid()) {
	    getDataModel().addIncrement2BusinessDay(getSelectedLeistungsart());
	    dispose(true);
	} else {
	    Toolkit.getDefaultToolkit().beep();
	    amountOfHoursTextField.getStyleClass().add(Styles.INVALID_INPUT_LABEL);
	    amountOfHoursTextField.getStyleClass().add(Styles.INVALID_INPUT_HOVER_LABEL);
	    amountOfHoursTextField.requestFocus();
	}
    }

    private int getSelectedLeistungsart() {
	String selectedItem = kindOfServiceComboBox.getSelectionModel().getSelectedItem();
	return ChargeType.getLeistungsartForRep(selectedItem);
    }

    private boolean isInputValid() {
	return new InputFieldVerifier().verify(amountOfHoursTextField)
		&& kindOfServiceComboBox.getSelectionModel().getSelectedItem() != null;
    }

    @Override
    protected void setBinding(StopBusinessDayIncrementPageModel pageModel) {
	ticketNumberTextField.textProperty().bindBidirectional(pageModel.getTicketNoTextFieldProperty());
	descriptionTextField.textProperty().bindBidirectional(pageModel.getDescriptionTextFieldProperty());

	beginTextField.textProperty().bindBidirectional(pageModel.getBeginTextFieldProperty());
	endTextField.textProperty().bindBidirectional(pageModel.getEndTextFieldProperty());
	amountOfHoursTextField.textProperty().bindBidirectional(pageModel.getAmountOfHoursTextFieldProperty());
	kindOfServiceComboBox.itemsProperty().bindBidirectional(pageModel.getKindOfServiceTextFieldProperty());

	ticketNumberLabel.textProperty().bindBidirectional(pageModel.getTicketNoLabelProperty());
	descriptionLabel.textProperty().bindBidirectional(pageModel.getDescriptionLabelProperty());
	beginLabel.textProperty().bindBidirectional(pageModel.getBeginLabelProperty());
	endLabel.textProperty().bindBidirectional(pageModel.getEndLabelProperty());
	amountOfHoursLabel.textProperty().bindBidirectional(pageModel.getAmountOfHoursLabelProperty());
	kindOfServiceLabel.textProperty().bindBidirectional(pageModel.getKindOfServiceLabelProperty());

	okButton.textProperty().bindBidirectional(pageModel.getOkButtonText());
	abortButton.textProperty().bindBidirectional(pageModel.getAbortButtonText());

	beginTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
	    if (oldValue && !newValue) {
		updateValue(beginTextField);
	    }
	});
	endTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
	    if (oldValue && !newValue) {
		updateValue(endTextField);
	    }
	});
	amountOfHoursTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
	    if (oldValue && !newValue) {
		updateValue(amountOfHoursTextField);
	    }
	});
    }

    private void updateValue(TextField changedTextField) {
	if (beginTextField == changedTextField) {
	    getDataModel().updateAndSetBeginTimeStamp(changedTextField.getText());
	} else if (endTextField == changedTextField) {
	    getDataModel().updateAndSetEndTimeStamp(changedTextField.getText());
	} else if (amountOfHoursTextField == changedTextField) {
	    if (new InputFieldVerifier().verify(changedTextField)){
		getDataModel().addAdditionallyTime(changedTextField.getText());
	    }
	}
    }

    /**
     * @param mainWindowController;
     */
    public void setMainWindowController(MainWindowController mainWindowController) {
	this.mainWindowController = mainWindowController;
    }
}
