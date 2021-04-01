/**
 * 
 */
package com.adcubum.timerecording.ui.app.pages.stopbusinessday.control;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import java.awt.Toolkit;
import java.net.URL;
import java.util.ResourceBundle;

import com.adcubum.timerecording.jira.data.Ticket;
import com.adcubum.timerecording.ui.app.inputfield.AutoCompleteTextField;
import com.adcubum.timerecording.ui.app.inputfield.InputFieldVerifier;
import com.adcubum.timerecording.ui.app.pages.combobox.TicketComboboxItem;
import com.adcubum.timerecording.ui.app.pages.mainpage.control.MainWindowController;
import com.adcubum.timerecording.ui.app.pages.stopbusinessday.model.StopBusinessDayIncrementPageModel;
import com.adcubum.timerecording.ui.app.pages.stopbusinessday.model.resolver.StopBusinessDayIncrementPageModelResolver;
import com.adcubum.timerecording.ui.app.pages.stopbusinessday.view.StopBusinessDayIncrementPage;
import com.adcubum.timerecording.ui.app.styles.Styles;
import com.adcubum.timerecording.ui.core.control.impl.BaseFXController;
import com.adcubum.timerecording.ui.core.model.resolver.PageModelResolver;

import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import src.com.myownb3.dominic.timerecording.app.TimeRecorder;
import src.com.myownb3.dominic.timerecording.core.book.adapter.ServiceCodeAdapter;

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
   private ComboBox<String> serviceCodesComboBox;

   @FXML
   private Button finishButton;
   @FXML
   private Button cancelButton;
   @FXML
   private Button abortButton;

   private MainWindowController mainWindowController;

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
   public void show() {
      super.show();
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
   private void onAction(ActionEvent actionEvent) {
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
         getDataModel().handleTicketNumberChanged();
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
      serviceCodesComboBox.getStyleClass().removeAll(Styles.INVALID_INPUT_LABEL);
      ticketNumberField.getStyleClass().removeAll(Styles.INVALID_INPUT_LABEL);
      amountOfHoursTextField.getStyleClass().removeAll(Styles.INVALID_INPUT_LABEL);
      mainWindowController.finishOrAbortAndDispose(finishAction);
      ticketNumberField.onDispose();
   }

   private void submit() {
      if (isInputValid()) {
         getDataModel().addIncrement2BusinessDay(getSelectedLeistungsart());
         dispose(FinishAction.FINISH);
      } else {
         Toolkit.getDefaultToolkit().beep();
      }
   }

   private int getSelectedLeistungsart() {
      String selectedItem = serviceCodesComboBox.getSelectionModel().getSelectedItem();
      ServiceCodeAdapter serviceCodeAdapter = TimeRecorder.INSTANCE.getServiceCodeAdapter();
      return serviceCodeAdapter.getServiceCode4Description(selectedItem);
   }

   private boolean isInputValid() {
      return new InputFieldVerifier().verify(amountOfHoursTextField, true)
            & validateSelectedServiceCode()
            & dataModel.isValid(ticketNumberField, true);
   }

   private boolean validateSelectedServiceCode() {
      boolean isNonNull = nonNull(serviceCodesComboBox.getSelectionModel().getSelectedItem());
      return InputFieldVerifier.addOrRemoveErrorStyleAndReturnValidationRes(serviceCodesComboBox, isNonNull);
   }

   @Override
   protected void setBinding(StopBusinessDayIncrementPageModel pageModel) {
      descriptionTextField.textProperty().bindBidirectional(pageModel.getDescriptionProperty());
      ticketNumberField.textProperty().bindBidirectional(pageModel.getTicketNoProperty());
      ticketNumberComboBox.itemsProperty().bindBidirectional(pageModel.getTicketComboboxItemsProperty());
      ticketNumberField.keyWordsProperty().bindBidirectional(pageModel.getTicketsProperty());

      beginTextField.textProperty().bindBidirectional(pageModel.getBeginTextFieldProperty());
      endTextField.textProperty().bindBidirectional(pageModel.getEndTextFieldProperty());
      amountOfHoursTextField.textProperty().bindBidirectional(pageModel.getAmountOfHoursTextFieldProperty());
      serviceCodesComboBox.itemsProperty().bindBidirectional(pageModel.getServiceCodesFieldProperty());
      pageModel.setServiceCodesSelectedModelProperty(serviceCodesComboBox.selectionModelProperty());
      serviceCodesComboBox.itemsProperty().addListener(serviceCodeSelectionChangedListener());

      ticketNumberLabel.textProperty().bindBidirectional(pageModel.getTicketNoLabelProperty());
      ticketNumberField.focusedProperty().addListener(ticketNrChangedListener());
      getDataModel().getTicketProperty().addListener(ticketChangedListener());
      descriptionLabel.textProperty().bindBidirectional(pageModel.getDescriptionLabelProperty());
      beginLabel.textProperty().bindBidirectional(pageModel.getBeginLabelProperty());
      endLabel.textProperty().bindBidirectional(pageModel.getEndLabelProperty());
      amountOfHoursLabel.textProperty().bindBidirectional(pageModel.getAmountOfHoursLabelProperty());
      serviceCodesLabel.textProperty().bindBidirectional(pageModel.getServiceCodesLabelProperty());

      finishButton.textProperty().bindBidirectional(pageModel.getFinishButtonText());
      abortButton.textProperty().bindBidirectional(pageModel.getAbortButtonText());
      cancelButton.textProperty().bindBidirectional(pageModel.getCancelButtonText());
      abortButton.tooltipProperty().bind(pageModel.getAbortButtonToolTipText());
      cancelButton.tooltipProperty().bind(pageModel.getCancelButtonToolTipText());

      beginTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
         if (oldValue.booleanValue() && !newValue.booleanValue()) {
            getDataModel().updateAndSetBeginTimeStamp();
         }
      });
      endTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
         if (oldValue.booleanValue() && !newValue.booleanValue()) {
            getDataModel().updateAndSetEndTimeStamp();
         }
      });
      amountOfHoursTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
         if (hasAmountOfHoursChanged(oldValue, newValue)) {
            getDataModel().addAdditionallyTime();
         }
      });
   }

   private ChangeListener<ObservableList<String>> serviceCodeSelectionChangedListener() {
      return (observable, oldValue, newValue) -> {
         if (!oldValue.equals(newValue)) {
            serviceCodesComboBox.getSelectionModel().selectFirst();
         }
      };
   }

   private ChangeListener<Ticket> ticketChangedListener() {
      return (observable, oldTicket, newTicket) -> {
         if (hasTicketChangedAndIsValid(oldTicket, newTicket)) {
            getDataModel().handleTicketChanged();
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
         if (oldValue.booleanValue() && !newValue.booleanValue()
               && dataModel.isValid(ticketNumberField, false)) {
            getDataModel().handleTicketNumberChanged();
         }
      };
   }

   private boolean hasAmountOfHoursChanged(Boolean oldValue, Boolean newValue) {
      return oldValue && !newValue && new InputFieldVerifier().verify(amountOfHoursTextField, false);
   }

   /**
    * @param mainWindowController;
    */
   public void setMainWindowController(MainWindowController mainWindowController) {
      this.mainWindowController = mainWindowController;
   }
}
