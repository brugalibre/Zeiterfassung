/**
 * 
 */
package com.myownb3.dominic.ui.app.pages.stopbusinessday.model;

import static com.myownb3.dominic.ui.app.pages.stopbusinessday.util.StopBusinessDayUtil.areMultipleTicketsEntered;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.myownb3.dominic.librarys.text.res.TextLabel;
import com.myownb3.dominic.timerecording.core.charge.ChargeType;
import com.myownb3.dominic.timerecording.core.work.businessday.BusinessDayIncrement;
import com.myownb3.dominic.timerecording.core.work.businessday.TimeSnippet;
import com.myownb3.dominic.timerecording.core.work.businessday.update.callback.TimeSnippedChangedCallbackHandler;
import com.myownb3.dominic.timerecording.core.work.businessday.update.callback.impl.BusinessDayChangedCallbackHandlerImpl;
import com.myownb3.dominic.timerecording.core.work.businessday.update.callback.impl.BusinessDayIncrementAdd;
import com.myownb3.dominic.timerecording.core.work.businessday.update.callback.impl.BusinessDayIncrementAdd.BusinessDayIncrementAddBuilder;
import com.myownb3.dominic.timerecording.core.work.businessday.update.callback.impl.ChangedValue;
import com.myownb3.dominic.timerecording.core.work.businessday.vo.BusinessDayIncrementVO;
import com.myownb3.dominic.timerecording.core.work.date.Time;
import com.myownb3.dominic.timerecording.ticketbacklog.TicketBacklog;
import com.myownb3.dominic.timerecording.ticketbacklog.TicketBacklogSPI;
import com.myownb3.dominic.timerecording.ticketbacklog.data.Ticket;
import com.myownb3.dominic.timerecording.ticketbacklog.data.TicketComparator;
import com.myownb3.dominic.ui.app.pages.combobox.TicketComboboxItem;
import com.myownb3.dominic.ui.core.model.PageModel;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Tooltip;

/**
 * @author Dominic
 *
 */
public class StopBusinessDayIncrementPageModel implements PageModel, TimeSnippedChangedCallbackHandler {

   private Property<String> ticketNoProperty;
   private Property<String> descriptionProperty;
   private StringProperty beginTextFieldProperty;
   private StringProperty endTextFieldProperty;
   private StringProperty amountOfHoursTextFieldProperty;
   private Property<ObservableList<String>> kindOfServiceTextFieldProperty;
   private Property<ObservableList<TicketComboboxItem>> ticketComboboxItemsProperty;
   private Property<ObservableList<Ticket>> ticketsProperty;

   private StringProperty ticketNoLabelProperty;
   private StringProperty descriptionLabelProperty;
   private StringProperty beginLabelProperty;
   private StringProperty endLabelProperty;
   private StringProperty amountOfHoursLabelProperty;
   private StringProperty kindOfServiceLabelProperty;

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
      kindOfServiceLabelProperty = new SimpleStringProperty(TextLabel.CHARGE_TYPE_LABEL);
      finishButtonText = new SimpleStringProperty(TextLabel.FINISH_BUTTON_TEXT);
      abortButtonText = new SimpleStringProperty(TextLabel.ABORT_BUTTON_TEXT);
      cancelButtonText = new SimpleStringProperty(TextLabel.CANCEL_BUTTON_TEXT);
      abortButtonToolTipText = new SimpleObjectProperty<>(new Tooltip(TextLabel.ABORT_BUTTON_TOOLTIP_TEXT));
      cancelButtonToolTipText = new SimpleObjectProperty<>(new Tooltip(TextLabel.CANCEL_BUTTON_TOOLTIP_TEXT));

      amountOfHoursTextFieldProperty = new SimpleStringProperty(businessDayIncrementVO.getTotalDurationRep());
      ticketNoProperty = new SimpleObjectProperty<>(businessDayIncrementVO.getTicketNumber());
      descriptionProperty = new SimpleObjectProperty<>(businessDayIncrementVO.getDescription());

      kindOfServiceTextFieldProperty = new SimpleListProperty<>(
            FXCollections.observableArrayList(ChargeType.getLeistungsartenRepresentation()));

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
    * 
    * @param newTimeStampValue
    *        the new begin-time-stamp as String
    */
   public void updateAndSetBeginTimeStamp(String newTimeStampValue) {
      timeSnippet.updateAndSetBeginTimeStamp(newTimeStampValue, false);
   }

   /**
    * Trys to parse a new {@link Date} from the given timestamp value and sets this
    * value as new end-time stamp
    * 
    * @param newTimeStampValue
    *        the new begin-time-stamp as String
    */
   public void updateAndSetEndTimeStamp(String newTimeStampValue) {
      timeSnippet.updateAndSetEndTimeStamp(newTimeStampValue, false);
   }

   /**
    * Trys to parse the given end time stamp (as String) and summs this value up to
    * the given begin Time Stamp
    * 
    * @param newEndAsString
    *        the new End-time stamp as String
    * @throws NumberFormatException
    *         if there goes anything wrong while parsing
    */
   public void addAdditionallyTime(String newEndAsString) {
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
      inPageModel.getKindOfServiceLabelProperty().set(TextLabel.CHARGE_TYPE_LABEL);
      inPageModel.getFinishButtonText().set(TextLabel.FINISH_BUTTON_TEXT);
      inPageModel.getAbortButtonText().set(TextLabel.ABORT_BUTTON_TEXT);
      inPageModel.getCancelButtonText().set(TextLabel.CANCEL_BUTTON_TEXT);

      inPageModel.abortButtonToolTipText = new SimpleObjectProperty<>(new Tooltip(TextLabel.ABORT_BUTTON_TOOLTIP_TEXT));
      inPageModel.cancelButtonToolTipText = new SimpleObjectProperty<>(new Tooltip(TextLabel.CANCEL_BUTTON_TOOLTIP_TEXT));
      inPageModel.getTicketNoProperty().setValue(businessDayIncrementVO.getTicketNumber());
      inPageModel.getDescriptionProperty().setValue(businessDayIncrementVO.getDescription());

      inPageModel.getAmountOfHoursTextFieldProperty().set(businessDayIncrementVO.getTotalDurationRep());

      inPageModel.getKindOfServiceTextFieldProperty()
            .setValue(FXCollections.observableArrayList(ChargeType.getLeistungsartenRepresentation()));
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

   private static List<TicketComboboxItem> getTicketsAndMap2ComboboxItems(List<Ticket> tickets) {
      return tickets.stream()
            .sorted(new TicketComparator())
            .map(TicketComboboxItem::of)
            .collect(Collectors.toList());
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
    * Adds the recorded informations as new {@link BusinessDayIncrement}
    * 
    * @param kindOfService
    *        the kind of service
    */
   public void addIncrement2BusinessDay(int kindOfService) {

      String ticketNoPropValue = ticketNoProperty.getValue();
      boolean multipleTicketsEntered = areMultipleTicketsEntered(ticketNoPropValue);
      if (multipleTicketsEntered) {
         addMultipleaIncrement2BusinessDay(kindOfService, ticketNoPropValue);
      } else {
         addIncrement2BusinessDayInternal(kindOfService, ticketNoProperty.getValue(), timeSnippet);
      }
   }

   private void addMultipleaIncrement2BusinessDay(int kindOfService, String ticketNoPropValue) {

      String[] tickets = ticketNoPropValue.split(";");
      Time currentBeginTimeStamp = timeSnippet.getBeginTimeStamp();

      for (String ticket : tickets) {
         TimeSnippet currentTimeSnippet = timeSnippet.createTimeStampForIncrement(currentBeginTimeStamp,
               tickets.length);
         addIncrement2BusinessDayInternal(kindOfService, ticket, currentTimeSnippet);

         currentBeginTimeStamp = currentTimeSnippet.getEndTimeStamp();
      }
   }

   private void addIncrement2BusinessDayInternal(int kindOfService, String ticketNr, TimeSnippet timeSnippet) {
      BusinessDayIncrementAdd update = new BusinessDayIncrementAddBuilder()
            .withTimeSnippet(timeSnippet)
            .withDescription(descriptionProperty.getValue())
            .withTicketNo(ticketNr)
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

   public final Property<ObservableList<String>> getKindOfServiceTextFieldProperty() {
      return this.kindOfServiceTextFieldProperty;
   }

   public final StringProperty getTicketNoLabelProperty() {
      return this.ticketNoLabelProperty;
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

   public final StringProperty getKindOfServiceLabelProperty() {
      return this.kindOfServiceLabelProperty;
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
}
