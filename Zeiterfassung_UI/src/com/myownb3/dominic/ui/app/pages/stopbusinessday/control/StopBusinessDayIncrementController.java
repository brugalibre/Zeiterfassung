/**
 * 
 */
package com.myownb3.dominic.ui.app.pages.stopbusinessday.control;

import static java.util.Objects.nonNull;

import java.awt.Toolkit;
import java.net.URL;
import java.util.ResourceBundle;

import com.myownb3.dominic.timerecording.core.charge.ChargeType;
import com.myownb3.dominic.timerecording.core.charge.exception.InvalidChargeTypeRepresentationException;
import com.myownb3.dominic.ui.app.pages.mainpage.control.MainWindowController;
import com.myownb3.dominic.ui.app.pages.stopbusinessday.control.combobox.ComboBoxHelper;
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
    private ComboBox<String> ticketNumberComboBox;

    @FXML
    private Label descriptionLabel;
    @FXML
    private ComboBox<String> descriptionComboBox;

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
    private Button finishButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Button abortButton;

    private ComboBoxHelper ticketNumberComboBoxHelper;
    private ComboBoxHelper descriptionComboBoxHelper;
    private MainWindowController mainWindowController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
	initialize(new StopBusinessDayIncrementPage(this, url));
	kindOfServiceComboBox.getSelectionModel().selectFirst();
	ticketNumberComboBoxHelper = new ComboBoxHelper(5, ticketNumberComboBox);
	descriptionComboBoxHelper = new ComboBoxHelper(5, descriptionComboBox);
    }

    @Override
    public void show() {
	super.show();
	ticketNumberComboBoxHelper.setFocusToFirstElement();
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
	    abort();
	} else if (actionEvent.getSource() == cancelButton) {
	    cancel();
	}
    }

    private boolean isFinish(ActionEvent actionEvent) {
	return actionEvent.getSource() == finishButton || actionEvent.getSource() == amountOfHoursTextField;
    }

    private void abort() {
	dispose(FinishAction.ABORT);
    }

    private void cancel() {
	dispose(FinishAction.RESUME);
    }

    @Override
    protected PageModelResolver<StopBusinessDayIncrementPageModel, StopBusinessDayIncrementPageModel> createPageModelResolver() {
	return new StopBusinessDayIncrementPageModelResolver();
    }

    private void dispose(FinishAction finishAction) {
	mainWindowController.finishOrAbortAndDispose(finishAction);
	amountOfHoursTextField.getStyleClass().remove(Styles.INVALID_INPUT_LABEL);
    }

    private void submit() {
	if (isInputValid()) {
	    getDataModel().addIncrement2BusinessDay(getSelectedLeistungsart());
	    updateCombobo();
	    dispose(FinishAction.FINISH);
	} else {
	    Toolkit.getDefaultToolkit().beep();
	}
    }

    private void updateCombobo() {
	String selectedItem = ticketNumberComboBox.getSelectionModel().getSelectedItem();
	ticketNumberComboBoxHelper.addNewItem(selectedItem);
	String selectedDescItem = descriptionComboBox.getSelectionModel().getSelectedItem();
	descriptionComboBoxHelper.addNewItem(selectedDescItem);
    }

    private int getSelectedLeistungsart() {
	String selectedItem = kindOfServiceComboBox.getSelectionModel().getSelectedItem();
	try {
	    return ChargeType.getLeistungsartForRep(selectedItem);
	} catch (InvalidChargeTypeRepresentationException e) {
	    // This should never happen here, therefore we throw a RuntimeException
	    e.printStackTrace();
	    throw new RuntimeException(e);
	}
    }

    private boolean isInputValid() {
	return new InputFieldVerifier().verify(amountOfHoursTextField)
		&& nonNull(kindOfServiceComboBox.getSelectionModel().getSelectedItem());
    }

    @Override
    protected void setBinding(StopBusinessDayIncrementPageModel pageModel) {
	descriptionComboBox.valueProperty().bindBidirectional(pageModel.getDescriptionProperty());
	ticketNumberComboBox.valueProperty().bindBidirectional(pageModel.getTicketNoProperty());

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

	finishButton.textProperty().bindBidirectional(pageModel.getFinishButtonText());
	abortButton.textProperty().bindBidirectional(pageModel.getAbortButtonText());
	cancelButton.textProperty().bindBidirectional(pageModel.getCancelButtonText());
	abortButton.tooltipProperty().bind(pageModel.getAbortButtonToolTipText());
	cancelButton.tooltipProperty().bind(pageModel.getCancelButtonToolTipText());

	beginTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
	    if (oldValue && !newValue) {
		getDataModel().updateAndSetBeginTimeStamp(beginTextField.getText());
	    }
	});
	endTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
	    if (oldValue && !newValue) {
		 getDataModel().updateAndSetEndTimeStamp(endTextField.getText());
	    }
	});
	amountOfHoursTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
	    if (hasAmountOfHoursChanged(oldValue, newValue)) {
		getDataModel().addAdditionallyTime(amountOfHoursTextField.getText());
	    }
	});
    }

    private boolean hasAmountOfHoursChanged(Boolean oldValue, Boolean newValue) {
	return oldValue && !newValue && new InputFieldVerifier().verify(amountOfHoursTextField);
    }

    /**
     * @param mainWindowController;
     */
    public void setMainWindowController(MainWindowController mainWindowController) {
	this.mainWindowController = mainWindowController;
    }
}
