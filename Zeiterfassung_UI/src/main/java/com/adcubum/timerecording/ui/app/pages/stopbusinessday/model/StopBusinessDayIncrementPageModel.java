/**
 * 
 */
package com.adcubum.timerecording.ui.app.pages.stopbusinessday.model;

import static com.adcubum.timerecording.jira.constants.TicketConst.MULTI_TICKET_DELIMITER;
import static com.adcubum.timerecording.jira.constants.TicketConst.MULTI_TICKET_NO_PATTERN;
import static com.adcubum.timerecording.jira.constants.TicketConst.TICKET_NO_PATTERN;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.adcubum.librarys.text.res.TextLabel;
import com.adcubum.timerecording.core.book.adapter.BookerAdapterFactory;
import com.adcubum.timerecording.core.book.adapter.ServiceCodeAdapter;
import com.adcubum.timerecording.core.work.businessday.BusinessDayIncrement;
import com.adcubum.timerecording.core.work.businessday.TimeSnippet;
import com.adcubum.timerecording.core.work.businessday.TimeSnippetFactory;
import com.adcubum.timerecording.core.work.businessday.update.callback.BusinessDayChangedCallbackHandler;
import com.adcubum.timerecording.core.work.businessday.update.callback.TimeSnippedChangedCallbackHandler;
import com.adcubum.timerecording.core.work.businessday.update.callback.impl.BusinessDayIncrementAdd;
import com.adcubum.timerecording.core.work.businessday.update.callback.impl.BusinessDayIncrementAdd.BusinessDayIncrementAddBuilder;
import com.adcubum.timerecording.core.work.businessday.update.callback.impl.ChangedValue;
import com.adcubum.timerecording.jira.data.TicketComparator;
import com.adcubum.timerecording.jira.data.ticket.Ticket;
import com.adcubum.timerecording.ticketbacklog.TicketBacklogSPI;
import com.adcubum.timerecording.ui.app.inputfield.InputFieldVerifier;
import com.adcubum.timerecording.ui.app.pages.combobox.TicketComboboxItem;
import com.adcubum.timerecording.ui.core.model.PageModel;
import com.adcubum.timerecording.work.date.Time;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
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

   private static final String MNEMONIC_PREFIX = "_";
   private BusinessDayChangedCallbackHandler businessDayChangedCallbackHandler;
   private Property<String> ticketNoProperty;
   private StringProperty multipleTicketsNoFieldProperty;
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
   private StringProperty multipleTicketsNoLabelProperty;
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
   private ObservableValue<Tooltip> finishButtonToolTipTextProperty;
   private TimeSnippet timeSnippet;
   private Time maxEndTime;// defines the max. value for the 'end'-time. 0 means there is no max value
   private BooleanProperty isAbortButtonDisabledProperty;
   private BooleanProperty isBeginTextFieldEnabledProperty;
   private boolean isLastIncrementAmongOthers;

   /**
    * Creates a new {@link StopBusinessDayIncrementPageModel}
    */
   public StopBusinessDayIncrementPageModel(StopBusinessDayIncrementPageModelConstructorInfo pageModelConstructorInfo) {
      this.businessDayChangedCallbackHandler = pageModelConstructorInfo.getBusinessDayChangedCallbackHandler();
      ticketNoLabelProperty = new SimpleStringProperty(TextLabel.TICKET_NUMBER_LABEL);
      multipleTicketsNoLabelProperty = new SimpleStringProperty(TextLabel.MULTIPLE_TICKETS_NUMBER_LABEL);
      descriptionLabelProperty = new SimpleStringProperty(TextLabel.DESCRIPTION_LABEL);
      beginLabelProperty = new SimpleStringProperty(TextLabel.VON_LABEL);
      endLabelProperty = new SimpleStringProperty(TextLabel.BIS_LABEL);
      amountOfHoursLabelProperty = new SimpleStringProperty(TextLabel.AMOUNT_OF_HOURS_LABEL);
      serviceCodesLabelProperty = new SimpleStringProperty(TextLabel.BOOK_TYPE_LABEL);
      finishButtonText = new SimpleStringProperty(getFinishButtonTextValue2Set());
      abortButtonText = new SimpleStringProperty(TextLabel.ABORT_BUTTON_TEXT);
      cancelButtonText = new SimpleStringProperty(getCancelButtonTextValue2Set());
      setAbortButtonToolTipText(new SimpleObjectProperty<>(new Tooltip(TextLabel.ABORT_BUTTON_TOOLTIP_TEXT)));
      setFinishButtonToolTipTextProperty(new SimpleObjectProperty<>(new Tooltip(pageModelConstructorInfo.getFinishContinueButtonToolTipText())));
      setCancelButtonToolTipText(new SimpleObjectProperty<>(new Tooltip(pageModelConstructorInfo.getAbortButtonToolTipText())));

      amountOfHoursTextFieldProperty = new SimpleStringProperty(pageModelConstructorInfo.getTotalDurationRep());
      ticketNoProperty = new SimpleObjectProperty<>(pageModelConstructorInfo.getTicketNumber());
      multipleTicketsNoFieldProperty = new SimpleStringProperty();
      descriptionProperty = new SimpleObjectProperty<>(pageModelConstructorInfo.getDescription());

      serviceCodesFieldProperty = new SimpleListProperty<>(getAllServiceCodeDescriptions());
      serviceCodesSelectedModelProperty = new SimpleObjectProperty<>();
      ticketProperty = new SimpleObjectProperty<>();
      this.timeSnippet = TimeSnippetFactory.createNew(pageModelConstructorInfo.getTimeSnippet());
      if (nonNull(this.timeSnippet)) {
         timeSnippet.setCallbackHandler(this);
      }
      beginTextFieldProperty = new SimpleStringProperty(
            getTimeSnippet() != null ? getTimeSnippet().getBeginTimeStampRep() : "");
      endTextFieldProperty = new SimpleStringProperty(getTimeSnippet() != null ? getTimeSnippet().getEndTimeStampRep() : "");
      ticketComboboxItemsProperty = new SimpleListProperty<>(getTicketComboboxItems());
      ticketsProperty = new SimpleListProperty<>(getTickets());
      isAbortButtonDisabledProperty = new SimpleBooleanProperty(!pageModelConstructorInfo.isAbortEnabled());
      isBeginTextFieldEnabledProperty = new SimpleBooleanProperty(pageModelConstructorInfo.isBeginTextFieldEnabled());
      this.setMaxEndTime(pageModelConstructorInfo.getMaxEndTime());
      this.setIsLastIncrementAmongOthers(pageModelConstructorInfo.isLastIncrementAmongOthers());
      onEndTimeStampChanged(pageModelConstructorInfo.getFinishContinueButtonToolTipText());
   }

   /**
    * Tries to parse a new {@link Date} from the given timestamp value and sets this
    * value as new begin-time stamp
    */
   public void updateAndSetBeginTimeStamp() {
      String newTimeStampValue = beginTextFieldProperty.getValue();
      getTimeSnippet().updateAndSetBeginTimeStamp(newTimeStampValue, false);
   }

   /**
    * Tries to parse a new {@link Date} from the given timestamp value and sets this
    * value as new end-time stamp
    */
   public void updateAndSetEndTimeStamp() {
      String newTimeStampValue = endTextFieldProperty.getValue();
      getTimeSnippet().updateAndSetEndTimeStamp(newTimeStampValue, false);
   }

   /*
    * If we can add more than one BusinessDayIncrement, the Finish-Button is renamed to 'next' instead of 'finished'
    * when ever the entered end time is less then the max value
    * We have to set 'next' in the following situations:
    *    - the end value of the current increment is less than the allowed max value
    *    - the end value is exactly the allowed value but there are other increments
    */
   public void onEndTimeStampChanged() {
      String currentFinishComeAndGoButtonTooltipText =
            isMultipleBDincCreatingEnabled() ? TextLabel.FINISH_COME_AND_GO_BUTTON_TOOLTIP_TEXT : TextLabel.FINISH_BUTTON_TOOLTIP_TEXT;
      onEndTimeStampChanged(currentFinishComeAndGoButtonTooltipText);
   }

   protected void onEndTimeStampChanged(String currentFinishComeAndGoButtonTooltipText) {
      getFinishButtonText().set(getFinishButtonTextValue2Set());
      getFinishButtonToolTipTextProperty().getValue().setText(currentFinishComeAndGoButtonTooltipText);
      if (isEndValueNonNull()) {
         boolean isEndTimeSmallerMaxValue = isEndTimeSmallerMaxValue();
         if (hasNextIncrements()
               && isEndTimeSmallerMaxValue) {
            getFinishButtonText().set(getNextButtonTextValue2Set());
            getFinishButtonToolTipTextProperty().getValue().setText(TextLabel.CONTINUE_COME_AND_GO_BUTTON_TOOLTIP_TEXT);
         } else if (!isEndTimeSmallerMaxValue) {
            timeSnippet.setEndTimeStamp(maxEndTime);
         }
      }
   }

   private boolean hasNextIncrements() {
      return getTimeSnippet().getEndTimeStamp().getMinutes() < maxEndTime.getMinutes()
            || !isLastIncrementAmongOthers();
   }

   private boolean isEndValueNonNull() {
      return nonNull(getTimeSnippet()) && nonNull(getTimeSnippet().getEndTimeStamp());
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
      getTimeSnippet().addAdditionallyTime(newEndAsString);
   }

   /**
    * Updates the value of the given {@link StopBusinessDayIncrementPageModel}
    * 
    * @param inPageModel
    *        the given {@link StopBusinessDayIncrementPageModel}
    * @return the given {@link StopBusinessDayIncrementPageModel}
    */
   public static <T extends StopBusinessDayIncrementPageModel> T of(T inPageModel,
         StopBusinessDayIncrementPageModelConstructorInfo pageModelConstructorInfo) {
      inPageModel.setBusinessDayChangedCallbackHandler(pageModelConstructorInfo.getBusinessDayChangedCallbackHandler());
      inPageModel.getTicketNoLabelProperty().set(TextLabel.TICKET_NUMBER_LABEL);
      inPageModel.getMultipleTicketsNoLabelProperty().set(TextLabel.MULTIPLE_TICKETS_NUMBER_LABEL);
      inPageModel.getDescriptionLabelProperty().set(TextLabel.DESCRIPTION_LABEL);
      inPageModel.getBeginLabelProperty().set(TextLabel.VON_LABEL);
      inPageModel.getEndLabelProperty().set(TextLabel.BIS_LABEL);
      inPageModel.getAmountOfHoursLabelProperty().set(TextLabel.AMOUNT_OF_HOURS_LABEL);
      inPageModel.getServiceCodesLabelProperty().set(TextLabel.BOOK_TYPE_LABEL);
      inPageModel.getFinishButtonText().set(getFinishButtonTextValue2Set());
      inPageModel.getAbortButtonText().set(TextLabel.ABORT_BUTTON_TEXT);
      inPageModel.getCancelButtonText().set(getCancelButtonTextValue2Set());

      inPageModel.getAbortButtonToolTipText().getValue().setText(TextLabel.ABORT_BUTTON_TOOLTIP_TEXT);
      inPageModel.getFinishButtonToolTipTextProperty().getValue().setText(pageModelConstructorInfo.getFinishContinueButtonToolTipText());
      inPageModel.getCancelButtonToolTipText().getValue().setText(pageModelConstructorInfo.getAbortButtonToolTipText());
      inPageModel.getTicketNoProperty().setValue(pageModelConstructorInfo.getTicketNumber());
      inPageModel.getMultipleTicketsNoFieldProperty().setValue("");
      inPageModel.getDescriptionProperty().setValue(pageModelConstructorInfo.getDescription());
      inPageModel.getTicketProperty().setValue(null);

      inPageModel.getAmountOfHoursTextFieldProperty().set(pageModelConstructorInfo.getTotalDurationRep());
      inPageModel.getServiceCodesFieldProperty().setValue(getAllServiceCodeDescriptions());
      inPageModel.getTicketComboboxItemsProperty().setValue(getTicketComboboxItems());
      inPageModel.getTicketsProperty().setValue(getTickets());

      TimeSnippet timeSnippet = TimeSnippetFactory.createNew(pageModelConstructorInfo.getTimeSnippet());
      inPageModel.setTimeSnippet(timeSnippet);
      boolean isTimeSnippetNonNull = nonNull(inPageModel.getTimeSnippet());
      if (isTimeSnippetNonNull) {
         inPageModel.getTimeSnippet().setCallbackHandler(inPageModel);
      }
      inPageModel.getBeginTextFieldProperty().set(isTimeSnippetNonNull ? inPageModel.getTimeSnippet().getBeginTimeStampRep() : "");
      inPageModel.getEndTextFieldProperty().set(isTimeSnippetNonNull ? inPageModel.getTimeSnippet().getEndTimeStampRep() : "");
      inPageModel.getIsAbortButtonDisabledProperty().set(!pageModelConstructorInfo.isAbortEnabled());
      inPageModel.getIsBeginTextFieldEnabledProperty().set(pageModelConstructorInfo.isBeginTextFieldEnabled());
      inPageModel.setMaxEndTime(pageModelConstructorInfo.getMaxEndTime());
      inPageModel.setIsLastIncrementAmongOthers(pageModelConstructorInfo.isLastIncrementAmongOthers());
      inPageModel.onEndTimeStampChanged(pageModelConstructorInfo.getFinishContinueButtonToolTipText());
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
      ServiceCodeAdapter serviceCodeAdapter = BookerAdapterFactory.getServiceCodeAdapter();
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
      ServiceCodeAdapter serviceCodeAdapter = BookerAdapterFactory.getServiceCodeAdapter();
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
            amountOfHoursTextFieldProperty.set(getTimeSnippet().getDurationRep());
            beginTextFieldProperty.set(getTimeSnippet().getBeginTimeStampRep());
            break;
         case END:
            amountOfHoursTextFieldProperty.set(getTimeSnippet().getDurationRep());
            endTextFieldProperty.set(getTimeSnippet().getEndTimeStampRep());
            onEndTimeStampChanged();
            break;
         case AMOUNT_OF_TIME:
            endTextFieldProperty.set(getTimeSnippet().getEndTimeStampRep());
            break;
         default:
            // ignore
            break;
      }
   }

   /**
    * Validates if the entered ticket-nrs are valid
    * 
    * @param multiTicketNumberField
    *        the {@link TextField} which contains the entered ticket-nr
    * @param addErrorStyle
    *        Determines if the style of the given textfield changes if it's valie is invalid
    * @return <code>true</code> if the entered value is valid or <code>false</code> if not
    */
   public boolean isMultiTicketNoValid(TextField multiTicketNumberField, boolean addErrorStyle) {
      return new InputFieldVerifier().isStringMatchingPattern(multiTicketNumberField, MULTI_TICKET_NO_PATTERN, addErrorStyle);
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
   public boolean isTicketNoValid(TextField ticketNumberField, boolean addErrorStyle) {
      return new InputFieldVerifier().isStringMatchingPattern(ticketNumberField, TICKET_NO_PATTERN, addErrorStyle);
   }

   /**
    * Returns <code>true</code> if the entered value for the end-time stamp is smaller then the allowed max. value
    * 
    * @return <code>true</code> if the entered value for the end-time stamp is smaller then the allowed max. value
    */
   private boolean isEndTimeSmallerMaxValue() {
      if (isMultipleBDincCreatingEnabled()) {
         return maxEndTime.getMinutes() >= timeSnippet.getEndTimeStamp().getMinutes();
      }
      return true;
   }

   private boolean isMultipleBDincCreatingEnabled() {
      return maxEndTime.getMinutes() > 0;
   }

   /**
    * Adds the recorded informations as new {@link BusinessDayIncrement}
    * 
    * @param kindOfService
    *        the kind of service
    */
   public void addIncrement2BusinessDay(int kindOfService) {
      if (nonNull(ticketProperty.getValue())) {
         addIncrement2BusinessDayInternal(kindOfService, ticketProperty.getValue(), getTimeSnippet());
      } else {
         addMultipleaIncrement2BusinessDay(kindOfService, multipleTicketsNoFieldProperty.getValue());
      }
   }

   private void addMultipleaIncrement2BusinessDay(int kindOfService, String ticketNoPropValue) {
      String[] ticketNrs = ticketNoPropValue.split(MULTI_TICKET_DELIMITER);
      Time currentBeginTimeStamp = getTimeSnippet().getBeginTimeStamp();
      for (String ticketNr : ticketNrs) {
         TimeSnippet currentTimeSnippet = getTimeSnippet().createTimeStampForIncrement(currentBeginTimeStamp, ticketNrs.length);
         Ticket ticket = TicketBacklogSPI.getTicketBacklog().getTicket4Nr(ticketNr);
         addIncrement2BusinessDayInternal(kindOfService, ticket, currentTimeSnippet);
         currentBeginTimeStamp = currentTimeSnippet.getEndTimeStamp();
      }
   }

   private void addIncrement2BusinessDayInternal(int kindOfService, Ticket ticket, TimeSnippet timeSnippet) {
      BusinessDayIncrementAdd update = new BusinessDayIncrementAddBuilder()
            .withTimeSnippet(timeSnippet)
            .withDescription(descriptionProperty.getValue())
            .withTicket(ticket)
            .withServiceCode(kindOfService)
            .build();
      businessDayChangedCallbackHandler.handleBusinessDayIncrementAdd(update);
   }

   public final Property<String> getTicketNoProperty() {
      return this.ticketNoProperty;
   }

   public final StringProperty getMultipleTicketsNoFieldProperty() {
      return this.multipleTicketsNoFieldProperty;
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

   public final StringProperty getMultipleTicketsNoLabelProperty() {
      return this.multipleTicketsNoLabelProperty;
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

   public ObservableValue<Tooltip> getFinishButtonToolTipTextProperty() {
      return finishButtonToolTipTextProperty;
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

   public void setBusinessDayChangedCallbackHandler(BusinessDayChangedCallbackHandler businessDayChangedCallbackHandler) {
      this.businessDayChangedCallbackHandler = businessDayChangedCallbackHandler;
   }

   public void setTimeSnippet(TimeSnippet timeSnippet) {
      this.timeSnippet = timeSnippet;
   }

   public void setAbortButtonToolTipText(ObservableValue<Tooltip> abortButtonToolTipText) {
      this.abortButtonToolTipText = abortButtonToolTipText;
   }

   public void setFinishButtonToolTipTextProperty(ObservableValue<Tooltip> finishButtonToolTipText) {
      this.finishButtonToolTipTextProperty = finishButtonToolTipText;
   }

   public void setCancelButtonToolTipText(ObservableValue<Tooltip> cancelButtonToolTipText) {
      this.cancelButtonToolTipText = cancelButtonToolTipText;
   }

   public TimeSnippet getTimeSnippet() {
      return timeSnippet;
   }

   public BooleanProperty getIsAbortButtonDisabledProperty() {
      return isAbortButtonDisabledProperty;
   }

   public BooleanProperty getIsBeginTextFieldEnabledProperty() {
      return isBeginTextFieldEnabledProperty;
   }

   public void setMaxEndTime(Time maxEndTime) {
      this.maxEndTime = maxEndTime;
   }

   public void setIsLastIncrementAmongOthers(boolean isLastIncrementAmongOthers) {
      this.isLastIncrementAmongOthers = isLastIncrementAmongOthers;
   }

   public boolean isLastIncrementAmongOthers() {
      return isLastIncrementAmongOthers;
   }

   public BusinessDayChangedCallbackHandler getBusinessDayChangedCallbackHandler() {
      return businessDayChangedCallbackHandler;
   }

   public Time getMaxEndTime() {
      return maxEndTime;
   }

   private String getNextButtonTextValue2Set() {
      return MNEMONIC_PREFIX + TextLabel.NEXT_BUTTON_TEXT;
   }

   private static String getFinishButtonTextValue2Set() {
      return MNEMONIC_PREFIX + TextLabel.FINISH_BUTTON_TEXT;
   }

   private static String getCancelButtonTextValue2Set() {
      return MNEMONIC_PREFIX + TextLabel.CANCEL_BUTTON_TEXT;
   }
}
