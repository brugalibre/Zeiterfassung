/**
 * 
 */
package com.adcubum.timerecording.ui.app.pages.stopbusinessday.model;

import static java.util.Objects.isNull;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.adcubum.librarys.text.res.TextLabel;
import com.adcubum.timerecording.app.TimeRecorder;
import com.adcubum.timerecording.core.book.adapter.ServiceCodeAdapter;
import com.adcubum.timerecording.core.work.businessday.BusinessDayIncrement;
import com.adcubum.timerecording.core.work.businessday.TimeSnippet;
import com.adcubum.timerecording.core.work.businessday.update.callback.TimeSnippedChangedCallbackHandler;
import com.adcubum.timerecording.core.work.businessday.update.callback.impl.BusinessDayChangedCallbackHandlerImpl;
import com.adcubum.timerecording.core.work.businessday.update.callback.impl.BusinessDayIncrementAdd;
import com.adcubum.timerecording.core.work.businessday.update.callback.impl.BusinessDayIncrementAdd.BusinessDayIncrementAddBuilder;
import com.adcubum.timerecording.core.work.businessday.update.callback.impl.ChangedValue;
import com.adcubum.timerecording.core.work.businessday.vo.BusinessDayIncrementVO;
import com.adcubum.timerecording.jira.data.Ticket;
import com.adcubum.timerecording.jira.data.TicketComparator;
import com.adcubum.timerecording.ticketbacklog.TicketBacklogSPI;
import com.adcubum.timerecording.ui.app.inputfield.InputFieldVerifier;
import com.adcubum.timerecording.ui.app.pages.combobox.TicketComboboxItem;
import com.adcubum.timerecording.ui.core.model.PageModel;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;

/**
 * @author Dominic
 *
 */
public class StopBusinessDayIncrementPageModel implements PageModel, TimeSnippedChangedCallbackHandler {

   private static final String TICKET_NO_PATTERN = "(([a-zA-Z0-9-?]+)[-][0-9]+)";

   private Property<String> ticketNoProperty;
   private Property<Ticket> ticketProperty;
   private Property<String> descriptionProperty;
   private StringProperty beginTextFieldProperty;
   private StringProperty endTextFieldProperty;
   private StringProperty amountOfHoursTextFieldProperty;
   private Property<ObservableList<String>> serviceCodesFieldProperty;
   private Property<SingleSelectionModel<String>> serviceCodesSelectedModelProperty;
   private Property<ObservableList<TicketComboboxItem>> ticketComboboxItemsProperty;
   private Property<ObservableList<Ticket>> ticketsProperty;

   private StringProperty ticketNoLabelProperty;
   private StringProperty descriptionLabelProperty;
   private StringProperty beginLabelProperty;
   private StringProperty endLabelProperty;
   private StringProperty amountOfHoursLabelProperty;
   private StringProperty serviceCodesLabelProperty;

   private StringProperty finishButtonText;
   private StringProperty abortButtonText;
   private StringProperty cancelButtonText;
   private ObservableValue<Tooltip> abortButtonToolTipText;
   private ObservableValue<Tooltip> cancelButtonToolTipText;
   private TimeSnippet timeSnippet;

   /**
    * Creates a new {@link StopBusinessDayIncrementPageModel}
    */
   public StopBusinessDayIncrementPageModel(BusinessDayIncrementVO businessDayIncrementVO) {

      ticketNoLabelProperty = new SimpleStringProperty(TextLabel.TICKET_NUMBER_LABEL);
      descriptionLabelProperty = new SimpleStringProperty(TextLabel.DESCRIPTION_LABEL);
      beginLabelProperty = new SimpleStringProperty(TextLabel.VON_LABEL);
      endLabelProperty = new SimpleStringProperty(TextLabel.BIS_LABEL);
      amountOfHoursLabelProperty = new SimpleStringProperty(TextLabel.AMOUNT_OF_HOURS_LABEL);
      serviceCodesLabelProperty = new SimpleStringProperty(TextLabel.BOOK_TYPE_LABEL);
      finishButtonText = new SimpleStringProperty(TextLabel.FINISH_BUTTON_TEXT);
      abortButtonText = new SimpleStringProperty(TextLabel.ABORT_BUTTON_TEXT);
      cancelButtonText = new SimpleStringProperty(TextLabel.CANCEL_BUTTON_TEXT);
      abortButtonToolTipText = new SimpleObjectProperty<>(new Tooltip(TextLabel.ABORT_BUTTON_TOOLTIP_TEXT));
      cancelButtonToolTipText = new SimpleObjectProperty<>(new Tooltip(TextLabel.CANCEL_BUTTON_TOOLTIP_TEXT));

      amountOfHoursTextFieldProperty = new SimpleStringProperty(businessDayIncrementVO.getTotalDurationRep());
      ticketNoProperty = new SimpleObjectProperty<>(businessDayIncrementVO.getTicketNumber());
      descriptionProperty = new SimpleObjectProperty<>(businessDayIncrementVO.getDescription());

      serviceCodesFieldProperty = new SimpleListProperty<>(getAllServiceCodeDescriptions());
      serviceCodesSelectedModelProperty = new SimpleObjectProperty<>();
      ticketProperty = new SimpleObjectProperty<>();
      this.timeSnippet = businessDayIncrementVO.getCurrentTimeSnippet();
      beginTextFieldProperty = new SimpleStringProperty(
            timeSnippet != null ? timeSnippet.getBeginTimeStampRep() : "");
      endTextFieldProperty = new SimpleStringProperty(timeSnippet != null ? timeSnippet.getEndTimeStampRep() : "");
      ticketComboboxItemsProperty = new SimpleListProperty<>(getTicketComboboxItems());
      ticketsProperty = new SimpleListProperty<>(getTickets());
   }

   /**
    * Trys to parse a new {@link Date} from the given timestamp value and sets this
    * value as new begin-time stamp
    */
   public void updateAndSetBeginTimeStamp() {
      String newTimeStampValue = beginTextFieldProperty.getValue();
      timeSnippet.updateAndSetBeginTimeStamp(newTimeStampValue, false);
   }

   /**
    * Trys to parse a new {@link Date} from the given timestamp value and sets this
    * value as new end-time stamp
    */
   public void updateAndSetEndTimeStamp() {
      String newTimeStampValue = endTextFieldProperty.getValue();
      timeSnippet.updateAndSetEndTimeStamp(newTimeStampValue, false);
   }

   /**
    * Trys to parse the given end time stamp (as String) and summs this value up to
    * the given begin Time Stamp
    * 
    * @throws NumberFormatException
    *         if there goes anything wrong while parsing
    */
   public void addAdditionallyTime() {
      String newEndAsString = amountOfHoursTextFieldProperty.getValue();
      timeSnippet.addAdditionallyTime(newEndAsString);
   }

   /**
    * Updates the value of the given {@link StopBusinessDayIncrementPageModel}
    * 
    * @param inPageModel
    *        the given {@link StopBusinessDayIncrementPageModel}
    * @return the given {@link StopBusinessDayIncrementPageModel}
    */
   public static StopBusinessDayIncrementPageModel of(StopBusinessDayIncrementPageModel inPageModel,
         BusinessDayIncrementVO businessDayIncrementVO) {

      inPageModel.timeSnippet = businessDayIncrementVO.getCurrentTimeSnippet();
      inPageModel.getTicketNoLabelProperty().set(TextLabel.TICKET_NUMBER_LABEL);
      inPageModel.getDescriptionLabelProperty().set(TextLabel.DESCRIPTION_LABEL);
      inPageModel.getBeginLabelProperty().set(TextLabel.VON_LABEL);
      inPageModel.getEndLabelProperty().set(TextLabel.BIS_LABEL);
      inPageModel.getAmountOfHoursLabelProperty().set(TextLabel.AMOUNT_OF_HOURS_LABEL);
      inPageModel.getServiceCodesLabelProperty().set(TextLabel.BOOK_TYPE_LABEL);
      inPageModel.getFinishButtonText().set(TextLabel.FINISH_BUTTON_TEXT);
      inPageModel.getAbortButtonText().set(TextLabel.ABORT_BUTTON_TEXT);
      inPageModel.getCancelButtonText().set(TextLabel.CANCEL_BUTTON_TEXT);

      inPageModel.abortButtonToolTipText = new SimpleObjectProperty<>(new Tooltip(TextLabel.ABORT_BUTTON_TOOLTIP_TEXT));
      inPageModel.cancelButtonToolTipText = new SimpleObjectProperty<>(new Tooltip(TextLabel.CANCEL_BUTTON_TOOLTIP_TEXT));
      inPageModel.getTicketNoProperty().setValue(businessDayIncrementVO.getTicketNumber());
      inPageModel.getDescriptionProperty().setValue(businessDayIncrementVO.getDescription());
      inPageModel.getTicketProperty().setValue(null);

      inPageModel.getAmountOfHoursTextFieldProperty().set(businessDayIncrementVO.getTotalDurationRep());
      inPageModel.getServiceCodesFieldProperty().setValue(getAllServiceCodeDescriptions());
      inPageModel.getTicketComboboxItemsProperty().setValue(getTicketComboboxItems());
      inPageModel.getTicketsProperty().setValue(getTickets());

      inPageModel.timeSnippet.setCallbackHandler(inPageModel);
      inPageModel.getBeginTextFieldProperty().set(inPageModel.timeSnippet.getBeginTimeStampRep());
      inPageModel.getEndTextFieldProperty().set(inPageModel.timeSnippet.getEndTimeStampRep());
      return inPageModel;
   }

   private static ObservableList<TicketComboboxItem> getTicketComboboxItems() {
      List<TicketComboboxItem> ticketComboboxItems = getTicketsAndMap2ComboboxItems(TicketBacklogSPI.getTicketBacklog().getTickets());
      return FXCollections.observableList(ticketComboboxItems);
   }

   private static ObservableList<Ticket> getTickets() {
      return FXCollections.observableList(TicketBacklogSPI.getTicketBacklog().getTickets());
   }

   private static ObservableList<String> getAllServiceCodeDescriptions() {
      // Initial we'll show all and as soon as the ticket-nr is knonw we filter
      ServiceCodeAdapter serviceCodeAdapter = TimeRecorder.INSTANCE.getServiceCodeAdapter();
      return FXCollections.observableList(serviceCodeAdapter.getAllServiceCodes());
   }

   private static List<TicketComboboxItem> getTicketsAndMap2ComboboxItems(List<Ticket> tickets) {
      return tickets.stream()
            .sorted(new TicketComparator())
            .map(TicketComboboxItem::of)
            .collect(Collectors.toList());
   }

   public void handleTicketNumberChanged() {
      String ticketNr = ticketNoProperty.getValue();
      Ticket ticket = TicketBacklogSPI.getTicketBacklog().getTicket4Nr(ticketNr);
      ticketProperty.setValue(ticket);
   }

   public void handleTicketChanged() {
      Ticket newTicket = ticketProperty.getValue();
      ServiceCodeAdapter serviceCodeAdapter = TimeRecorder.INSTANCE.getServiceCodeAdapter();
      List<String> fetchServiceCodesForProject = serviceCodeAdapter.fetchServiceCodesForProjectNr(newTicket.getTicketAttrs().getProjectNr());
      serviceCodesFieldProperty.setValue(FXCollections.observableList(fetchServiceCodesForProject));
      if (isNull(serviceCodesSelectedModelProperty.getValue().getSelectedItem())) {
         serviceCodesSelectedModelProperty.getValue().selectFirst();
      }
   }

   @Override
   public void handleTimeSnippedChanged(ChangedValue changeValue) {

      switch (changeValue.getValueTypes()) {

         case BEGIN:
            amountOfHoursTextFieldProperty.set(timeSnippet.getDurationRep());
            beginTextFieldProperty.set(timeSnippet.getBeginTimeStampRep());
            break;
         case END:
            amountOfHoursTextFieldProperty.set(timeSnippet.getDurationRep());
            endTextFieldProperty.set(timeSnippet.getEndTimeStampRep());
            break;
         case AMOUNT_OF_TIME:
            endTextFieldProperty.set(timeSnippet.getEndTimeStampRep());
            break;
         default:
            // ignore
            break;
      }
   }

   /**
    * Validates if the entered ticket-nr is valid
    * 
    * @param ticketNumberField
    *        the {@link TextField} which contains the entered ticket-nr
    * @param addErrorStyle
    *        Determines if the style of the given textfield changes if it's valie is invalid
    * @return <code>true</code> if the entered value is valid or <code>false</code> if not
    */
   public boolean isValid(TextField ticketNumberField, boolean addErrorStyle) {
      return new InputFieldVerifier().isStringMatchingPattern(ticketNumberField, TICKET_NO_PATTERN, addErrorStyle);
   }

   /**
    * Adds the recorded informations as new {@link BusinessDayIncrement}
    * 
    * @param kindOfService
    *        the kind of service
    */
   public void addIncrement2BusinessDay(int kindOfService) {
      addIncrement2BusinessDayInternal(kindOfService, ticketProperty.getValue(), timeSnippet);
   }

   private void addIncrement2BusinessDayInternal(int kindOfService, Ticket ticket, TimeSnippet timeSnippet) {
      BusinessDayIncrementAdd update = new BusinessDayIncrementAddBuilder()
            .withTimeSnippet(timeSnippet)
            .withDescription(descriptionProperty.getValue())
            .withTicket(ticket)
            .withKindOfService(kindOfService)
            .build();
      new BusinessDayChangedCallbackHandlerImpl().handleBusinessDayIncrementAdd(update);
   }

   public final Property<String> getTicketNoProperty() {
      return this.ticketNoProperty;
   }

   public final Property<String> getDescriptionProperty() {
      return this.descriptionProperty;
   }

   public final StringProperty getBeginTextFieldProperty() {
      return this.beginTextFieldProperty;
   }

   public final StringProperty getEndTextFieldProperty() {
      return this.endTextFieldProperty;
   }

   public final Property<ObservableList<String>> getServiceCodesFieldProperty() {
      return this.serviceCodesFieldProperty;
   }

   public final StringProperty getTicketNoLabelProperty() {
      return this.ticketNoLabelProperty;
   }

   public Property<SingleSelectionModel<String>> getServiceCodesSelectedModelProperty() {
      return serviceCodesSelectedModelProperty;
   }

   public void setServiceCodesSelectedModelProperty(ObjectProperty<SingleSelectionModel<String>> selectionModelProperty) {
      this.serviceCodesSelectedModelProperty = selectionModelProperty;
   }

   public final StringProperty getDescriptionLabelProperty() {
      return this.descriptionLabelProperty;
   }

   public final StringProperty getBeginLabelProperty() {
      return this.beginLabelProperty;
   }

   public final StringProperty getEndLabelProperty() {
      return this.endLabelProperty;
   }

   public final StringProperty getServiceCodesLabelProperty() {
      return this.serviceCodesLabelProperty;
   }

   public final StringProperty getAmountOfHoursTextFieldProperty() {
      return this.amountOfHoursTextFieldProperty;
   }

   public final StringProperty getAmountOfHoursLabelProperty() {
      return this.amountOfHoursLabelProperty;
   }

   public final StringProperty getFinishButtonText() {
      return this.finishButtonText;
   }

   public final StringProperty getAbortButtonText() {
      return this.abortButtonText;
   }

   public final StringProperty getCancelButtonText() {
      return this.cancelButtonText;
   }

   public ObservableValue<Tooltip> getAbortButtonToolTipText() {
      return abortButtonToolTipText;
   }

   public ObservableValue<Tooltip> getCancelButtonToolTipText() {
      return cancelButtonToolTipText;
   }

   public Property<ObservableList<TicketComboboxItem>> getTicketComboboxItemsProperty() {
      return ticketComboboxItemsProperty;
   }

   public Property<ObservableList<Ticket>> getTicketsProperty() {
      return ticketsProperty;
   }

   public Property<Ticket> getTicketProperty() {
      return ticketProperty;
   }

   public void setTicketProperty(Property<Ticket> ticketProperty) {
      this.ticketProperty = ticketProperty;
   }
}
