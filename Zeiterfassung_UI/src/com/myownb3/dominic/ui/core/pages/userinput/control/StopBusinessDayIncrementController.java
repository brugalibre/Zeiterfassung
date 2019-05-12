/**
 * 
 */
package com.myownb3.dominic.ui.core.pages.userinput.control;

import java.net.URL;
import java.util.ResourceBundle;

import com.myownb3.dominic.timerecording.app.TimeRecorder;
import com.myownb3.dominic.ui.core.control.impl.BaseFXController;
import com.myownb3.dominic.ui.core.model.resolver.PageModelResolver;
import com.myownb3.dominic.ui.core.pages.mainpage.control.MainWindowController;
import com.myownb3.dominic.ui.core.pages.userinput.model.StopBusinessDayIncrementPageModel;
import com.myownb3.dominic.ui.core.pages.userinput.model.resolver.StopBusinessDayIncrementPageModelResolver;
import com.myownb3.dominic.ui.core.pages.userinput.view.StopBusinessDayIncrementPage;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * @author Dominic
 *
 */
public class StopBusinessDayIncrementController
	extends BaseFXController<StopBusinessDayIncrementPageModel, StopBusinessDayIncrementPageModel> {

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
    protected PageModelResolver<StopBusinessDayIncrementPageModel, StopBusinessDayIncrementPageModel> createPageModelResolver() {
	return new StopBusinessDayIncrementPageModelResolver();
    }

    @FXML
    private void onAction(ActionEvent actionEvent) {
	if (actionEvent.getSource() == okButton) {
	    submit();
	} else if (actionEvent.getSource() == abortButton) {
	    cancel();
	}
    }

    private void cancel() {
	mainWindowController.finishOrAbortAndDispose(false);
    }

    private void submit() {
	if (isInputValid()) {
	    getDataModel().addIncrement2BusinessDay(TimeRecorder.getBussinessDay(),
		    Integer.valueOf(kindOfServiceComboBox.getSelectionModel().getSelectedItem().substring(0, 3)));
	    mainWindowController.finishOrAbortAndDispose(true);
	}
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
	    getDataModel().addAdditionallyTime(changedTextField.getText());
	}
    }

    /**
     * @param mainWindowController;
     */
    public void setMainWindowController(MainWindowController mainWindowController) {
	this.mainWindowController = mainWindowController;
    }
}
