/**
 * 
 */
package com.adcubum.timerecording.ui.app.pages.stopbusinessday.control;

import com.adcubum.timerecording.jira.data.ticket.Ticket;
import com.adcubum.timerecording.jira.data.ticket.TicketActivity;
import com.adcubum.timerecording.ui.app.inputfield.AutoCompleteTextField;
import com.adcubum.timerecording.ui.app.inputfield.InputFieldVerifier;
import com.adcubum.timerecording.ui.app.pages.combobox.TicketComboboxItem;
import com.adcubum.timerecording.ui.app.pages.stopbusinessday.model.StopBusinessDayIncrementPageModel;
import com.adcubum.timerecording.ui.app.pages.stopbusinessday.model.resolver.StopBusinessDayIncrementPageModelResolver;
import com.adcubum.timerecording.ui.app.pages.stopbusinessday.view.StopBusinessDayIncrementPage;
import com.adcubum.timerecording.ui.app.styles.Styles;
import com.adcubum.timerecording.ui.core.control.impl.BaseFXController;
import com.adcubum.timerecording.ui.core.model.PageModel;
import com.adcubum.timerecording.ui.core.model.resolver.PageModelResolver;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.stage.WindowEvent;
import javafx.util.Callback;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import static com.adcubum.util.utils.StringUtil.isEmptyOrNull;
import static java.util.Objects.*;

/**
 * @author Dominic
 *
 */
public class StopBusinessDayIncrementController extends BaseFXController<PageModel, StopBusinessDayIncrementPageModel>
      implements EventHandler<WindowEvent> {

   @FXML
   private Label ticketNumberLabel;

   @FXML
   private Label multipleTicketsNumberLabel;

   @FXML
   private TextField multipleTicketsNumberField;

   @FXML
   private ComboBox<TicketComboboxItem> ticketNumberComboBox;

   @FXML
   private AutoCompleteTextField ticketNumberField;

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
   private Label serviceCodesLabel;
   @FXML
   private ComboBox<TicketActivity> serviceCodesComboBox;

   @FXML
   private Button finishButton;
   @FXML
   private Button cancelButton;
   @FXML
   private Button abortButton;

   private Consumer<FinishAction> onFinishHandler;

   @Override
   public void initialize(URL url, ResourceBundle resourceBundle) {
      initialize(new StopBusinessDayIncrementPage(this));
      ticketNumberComboBox.setCellFactory(createCellFactory());
   }

   private static Callback<ListView<TicketComboboxItem>, ListCell<TicketComboboxItem>> createCellFactory() {
      return listView -> new ListCell<TicketComboboxItem>() {
         @Override
         protected void updateItem(TicketComboboxItem item, boolean isEmpty) {
            super.updateItem(item, isEmpty);
            if (item == null || isEmpty) {
               setGraphic(null);
            } else {
               setText(item.toString());
               if (item.getTicket().isCurrentUserAssigned()) {
                  getStyleClass().add(Styles.CURRENT_USER_HAS_TICKET_ASSIGNED);
               } else {
                  getStyleClass().remove(Styles.CURRENT_USER_HAS_TICKET_ASSIGNED);
               }
            }
         }
      };
   }

   @Override
   public void show(PageModel dataModelIn) {
      super.show(dataModelIn);
      disableOrEnable(multipleTicketsNumberField, true);
      disableOrEnable(ticketNumberField, false);
      ticketNumberComboBox.getSelectionModel().clearSelection();
      ticketNumberField.requestFocus();// sometimes this also 'selects all'. But not somehow not the first time
      ticketNumberField.selectAll();
   }

   @Override
   public void handle(WindowEvent event) {
      if (event.getEventType() == WindowEvent.WINDOW_CLOSE_REQUEST) {
         cancel();
      }
   }

   @FXML
   protected void onAction(ActionEvent actionEvent) {
      if (isFinish(actionEvent)) {
         submit();
      } else if (actionEvent.getSource() == abortButton) {
         abort();
      } else if (actionEvent.getSource() == cancelButton) {
         cancel();
      } else if (actionEvent.getSource() == ticketNumberComboBox) {
         setSelectedTicket();
      }
   }

   private void setSelectedTicket() {
      if (nonNull(ticketNumberComboBox.getSelectionModel().getSelectedItem())) {
         ticketNumberField.setText(ticketNumberComboBox.getSelectionModel().getSelectedItem().getKey());
         dataModel.handleTicketNumberChanged();
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
   protected PageModelResolver<PageModel, StopBusinessDayIncrementPageModel> createPageModelResolver() {
      return new StopBusinessDayIncrementPageModelResolver();
   }

   private void dispose(FinishAction finishAction) {
      serviceCodesComboBox.getStyleClass().removeAll(Styles.INVALID_INPUT_LABEL);
      ticketNumberField.getStyleClass().removeAll(Styles.INVALID_INPUT_LABEL);
      amountOfHoursTextField.getStyleClass().removeAll(Styles.INVALID_INPUT_LABEL);
      endTextField.getStyleClass().removeAll(Styles.INVALID_INPUT_LABEL);
      ticketNumberField.onDispose();
      onFinishHandler.accept(finishAction);
   }

   private void submit() {
      if (isInputValid()) {
         dataModel.addIncrement2BusinessDay(serviceCodesComboBox.getSelectionModel().getSelectedItem());
         dispose(FinishAction.FINISH);
      } else {
         Toolkit.getDefaultToolkit().beep();
      }
   }

   private boolean isInputValid() {
      InputFieldVerifier.removeErrorStyle(multipleTicketsNumberField, ticketNumberField);
      boolean isValidSelectedServiceCode = validateSelectedServiceCode();
      boolean isTicketNoInputValid = isTicketNoInputValid();
      boolean isMultiTicketNoInputValid = isMultiTicketNoInputValid();
      boolean isDescriptionValid = dataModel.isDescriptionValid(descriptionTextField);
      return new InputFieldVerifier().verify(amountOfHoursTextField, true)
            && isValidSelectedServiceCode
            && isDescriptionValid
            && isTicketNoInputValid
            && isMultiTicketNoInputValid;
   }

   private boolean isMultiTicketNoInputValid() {
      return multipleTicketsNumberField.isDisable()
            || isEmptyOrNull(multipleTicketsNumberField.getText())
            || dataModel.isMultiTicketNoValid(multipleTicketsNumberField, true);
   }

   private boolean isTicketNoInputValid() {
      return ticketNumberField.isDisable()
            || dataModel.isTicketNoValid(ticketNumberField, true);
   }

   private boolean validateSelectedServiceCode() {
      boolean isNonNull = nonNull(serviceCodesComboBox.getSelectionModel().getSelectedItem());
      return InputFieldVerifier.addOrRemoveErrorStyleAndReturnValidationRes(serviceCodesComboBox, isNonNull);
   }

   @Override
   protected void setBinding(StopBusinessDayIncrementPageModel pageModel) {
      descriptionTextField.textProperty().bindBidirectional(pageModel.getDescriptionProperty());
      ticketNumberComboBox.itemsProperty().bindBidirectional(pageModel.getTicketComboboxItemsProperty());
      ticketNumberField.textProperty().bindBidirectional(pageModel.getTicketNoProperty());
      ticketNumberField.keyWordsProperty().bindBidirectional(pageModel.getTicketsProperty());
      multipleTicketsNumberField.textProperty().bindBidirectional(pageModel.getMultipleTicketsNoFieldProperty());
      multipleTicketsNumberField.focusedProperty().addListener(multiTicketNrsChangedListener());

      beginTextField.textProperty().bindBidirectional(pageModel.getBeginTextFieldProperty());
      beginTextField.editableProperty().bind(pageModel.getIsBeginTextFieldEnabledProperty());
      endTextField.textProperty().bindBidirectional(pageModel.getEndTextFieldProperty());
      amountOfHoursTextField.textProperty().bindBidirectional(pageModel.getAmountOfHoursTextFieldProperty());
      serviceCodesComboBox.itemsProperty().bindBidirectional(pageModel.getServiceCodesFieldProperty());
      pageModel.setServiceCodesSelectedModelProperty(serviceCodesComboBox.selectionModelProperty());
      serviceCodesComboBox.itemsProperty().addListener(serviceCodeSelectionChangedListener());

      multipleTicketsNumberLabel.textProperty().bindBidirectional(pageModel.getMultipleTicketsNoLabelProperty());
      ticketNumberLabel.textProperty().bindBidirectional(pageModel.getTicketNoLabelProperty());
      ticketNumberField.focusedProperty().addListener(ticketNrChangedListener());
      dataModel.getTicketProperty().addListener(ticketChangedListener());
      descriptionLabel.textProperty().bindBidirectional(pageModel.getDescriptionLabelProperty());
      beginLabel.textProperty().bindBidirectional(pageModel.getBeginLabelProperty());
      endLabel.textProperty().bindBidirectional(pageModel.getEndLabelProperty());
      amountOfHoursLabel.textProperty().bindBidirectional(pageModel.getAmountOfHoursLabelProperty());
      serviceCodesLabel.textProperty().bindBidirectional(pageModel.getServiceCodesLabelProperty());

      finishButton.textProperty().bindBidirectional(pageModel.getFinishButtonText());
      finishButton.tooltipProperty().bind(pageModel.getFinishButtonToolTipTextProperty());
      abortButton.textProperty().bindBidirectional(pageModel.getAbortButtonText());
      abortButton.disableProperty().bind(pageModel.getIsAbortButtonDisabledProperty());
      cancelButton.textProperty().bindBidirectional(pageModel.getCancelButtonText());
      abortButton.tooltipProperty().bind(pageModel.getAbortButtonToolTipText());
      cancelButton.tooltipProperty().bind(pageModel.getCancelButtonToolTipText());

      beginTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
         if (oldValue.booleanValue() && !newValue.booleanValue()) {
            dataModel.updateAndSetBeginTimeStamp();
         }
      });
      endTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
         if (oldValue.booleanValue() && !newValue.booleanValue()) {
            dataModel.updateAndSetEndTimeStamp();
         }
      });
      amountOfHoursTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
         if (hasAmountOfHoursChanged(oldValue, newValue)) {
            dataModel.addAdditionallyTime();
         }
      });
   }

   private ChangeListener<ObservableList<TicketActivity>> serviceCodeSelectionChangedListener() {
      return (observable, oldValue, newValue) -> {
         if (!oldValue.equals(newValue)) {
            serviceCodesComboBox.getSelectionModel().selectFirst();
         }
      };
   }

   private ChangeListener<Ticket> ticketChangedListener() {
      return (observable, oldTicket, newTicket) -> {
         if (hasTicketChangedAndIsValid(oldTicket, newTicket)) {
            ticketNumberField.setDisable(false);
            dataModel.handleTicketChanged();
         }
      };
   }

   private static boolean hasTicketChangedAndIsValid(Ticket oldTicket, Ticket newTicket) {
      return hasTicketChanged(oldTicket, newTicket) && !newTicket.isDummyTicket();
   }

   private static boolean hasTicketChanged(Ticket oldTicket, Ticket newTicket) {
      return (isNull(oldTicket) || !oldTicket.equals(newTicket)) && nonNull(newTicket);
   }

   private ChangeListener<Boolean> ticketNrChangedListener() {
      return (observable, oldValue, newValue) -> {
         if (oldValue.booleanValue() && !newValue.booleanValue()) {
            if (dataModel.isTicketNoValid(ticketNumberField, false)) {
               dataModel.handleTicketNumberChanged();
               disableOrEnable(multipleTicketsNumberField, true);
            } else {
               disableOrEnable(multipleTicketsNumberField, !isEmptyOrNull(ticketNumberField.getText()));
            }
         } else if (isEmptyOrNull(ticketNumberField.getText())) {
            enableMultipleTicketsNumberField();
         }
      };
   }

   private void disableOrEnable(TextField textField, boolean disable) {
      textField.setDisable(disable);
      if (disable) {
         InputFieldVerifier.removeErrorStyle(textField);
      }
   }

   private ChangeListener<Boolean> multiTicketNrsChangedListener() {
      return (observable, oldValue, newValue) -> disableOrEnable(ticketNumberField, !isEmptyOrNull(multipleTicketsNumberField.getText()));
   }

   private void enableMultipleTicketsNumberField() {
      disableOrEnable(multipleTicketsNumberField, false);
      dataModel.getTicketProperty().setValue(null);
   }

   private boolean hasAmountOfHoursChanged(Boolean oldValue, Boolean newValue) {
      return oldValue && !newValue && new InputFieldVerifier().verify(amountOfHoursTextField, false);
   }

   public void setOnFinishHandler(Consumer<FinishAction> onFinishHandler) {
      this.onFinishHandler = requireNonNull(onFinishHandler);
   }
}
