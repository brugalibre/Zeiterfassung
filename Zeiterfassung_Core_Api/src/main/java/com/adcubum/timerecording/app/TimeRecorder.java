package com.adcubum.timerecording.app;

import com.adcubum.timerecording.app.book.TimeRecorderBookResult;
import com.adcubum.timerecording.app.startstopresult.UserInteractionResult;
import com.adcubum.timerecording.core.callbackhandler.UiCallbackHandler;
import com.adcubum.timerecording.core.work.businessday.BusinessDay;
import com.adcubum.timerecording.core.work.businessday.BusinessDayIncrement;
import com.adcubum.timerecording.core.work.businessday.ValueTypes;
import com.adcubum.timerecording.core.work.businessday.comeandgo.ComeAndGo;
import com.adcubum.timerecording.core.work.businessday.comeandgo.ComeAndGoes;
import com.adcubum.timerecording.core.work.businessday.comeandgo.change.ChangedComeAndGoValue;
import com.adcubum.timerecording.core.work.businessday.history.resolver.BusinessDayHistoryOverviewResolver;
import com.adcubum.timerecording.core.work.businessday.update.callback.impl.BusinessDayIncrementAdd;
import com.adcubum.timerecording.core.work.businessday.update.callback.impl.ChangedValue;
import com.adcubum.timerecording.importexport.in.file.FileImporter;
import com.adcubum.timerecording.importexport.out.file.FileExportResult;

import java.io.File;
import java.util.UUID;

public interface TimeRecorder extends BusinessDayHistoryOverviewResolver {

   /**
    * The version of the application
    */
   String VERSION = "2.0.0";

   /**
    * The singleton instance of the {@link TimeRecorder}
    */
   TimeRecorder INSTANCE = TimeRecorderFactory.createNew();

   /**
    * Initializes this {@link TimeRecorder}
    */
   void init();

   /**
    * Either starts a new or stops the current recording.
    *
    * @param comeOrGo <code>true</code> if the user started/stopped a come or go or <code>false</code> if the user started/stopped a recording
    * @return a {@link UserInteractionResult}
    * @formatter:off    <ul>
    * <li>Start: Starts a new recording. This leads the {@link TimeRecorder} to switch into the status
    * {@link WorkStates#WORKING}.
    * </li>
    * <li>Stop: Stops the current recording. This This leads the {@link TimeRecorder} to switch into
    * the status {@link WorkStates#NOT_WORKING}. Also the UI is shown in
    * order to enter the Ticket-No
    * </li>
    * </ul>
    * @formatter:on
    */
   UserInteractionResult handleUserInteraction(boolean comeOrGo);

   /**
    * Resumes a previously stopped recording
    */
   void resume();

   /**
    * removes all recorded {@link BusinessDayIncrement}
    */
   void clear();

   /**
    * removes all recorded {@link BusinessDayIncrement}
    */
   void clearComeAndGoes();

   /**
    * Creates and adds a new {@link BusinessDayIncrement} for the given
    * {@link BusinessDayIncrementAdd}
    *
    * @param businessDayIncrementAdd the {@link BusinessDayIncrementAdd} which defines the new
    *                                {@link BusinessDayIncrement}
    */
   void addBusinessIncrement(BusinessDayIncrementAdd businessDayIncrementAdd);

   /**
    * Removes the {@link BusinessDayIncrement} at the given index. If there is no
    * {@link BusinessDayIncrement} for this index nothing is done
    *
    * @param id the given index
    */
   void removeIncrement4Id(UUID id);

   /**
    * According to the given {@link ChangedValue} the corresponding
    * {@link BusinessDayIncrement} evaluated. If there is one then the value is
    * changed
    *
    * @param changeValue the param which defines what value is changed
    * @see ValueTypes
    */
   void changeBusinesDayIncrement(ChangedValue changeValue);

   /**
    * Updates a {@link ComeAndGo} for the given changed values
    *
    * @param changedComeAndGoValue the {@link ChangedComeAndGoValue} define the changed values
    * @return the changed {@link ComeAndGoes}
    */
   ComeAndGoes changeComeAndGo(ChangedComeAndGoValue changedComeAndGoValue);

   /**
    * Flags all {@link ComeAndGo}es of the {@link BusinessDay} as recorded
    */
   void flagBusinessDayComeAndGoesAsRecorded();

   /**
    * Flags the {@link BusinessDay} as booked without do any of the actual booking
    */
   void flagBusinessDayAsBooked();

   /**
    *
    * Calls the booking-adapter which does the actual booking - depending on the configured booking-system
    *
    * @return a {@link com.adcubum.timerecording.app.book.TimeRecorderBookResult} indicating the success of this booking
    */
   TimeRecorderBookResult book();

   /**
    * Exports the current {@link BusinessDay} to the users home-directory
    *
    * @return the result of the export
    */
   FileExportResult export();

   /**
    * Exports the current {@link BusinessDay} to the given path and ignores any result
    *
    * @param path2Export the path
    * @return the result of the export
    */
   FileExportResult exportSilently(String path2Export);

   /**
    * Is called after the {@link com.adcubum.timerecording.ticketbacklog.TicketBacklog} was initialized sucessfully
    */
   void onTicketBacklogInitialized();

   /**
    * First the {@link FileImporter} imports the given {@link File} and fills
    * it's content into a list. Later the a BusinessDayImporter uses this
    * lists in order to import a a new {@link BusinessDay}
    *
    * @param file the file to import
    * @return <code>true</code> if the new {@link BusinessDay} was successfully
    * imported or <code>false</code> if not
    */
   boolean importBusinessDayFromFile(File file);

   /**
    * Return a String, which represents the current state and shows informations
    * according to this
    *
    * @return a String, which represents the current state and shows informations
    * according to this
    */
   String getInfoStringForState();

   /**
    * Returns a value stored in the Settings for this {@link TimeRecorder} application
    *
    * @param key the key
    * @return a value stored in the Settings for this {@link TimeRecorder} application
    */
   String getSettingsValue(String key);

   /**
    * Saves the given value for this application
    *
    * @param value the value
    * @param key   the key associated with
    */
   void saveSettingValue(String value, String key);

   /**
    * Return <code>true</code> if there is any content, <code>false</code> if not
    *
    * @return <code>true</code> if there is any content, <code>false</code> if not
    */
   boolean hasContent();

   /**
    * Returns <code>true</code> if this {@link BusinessDay} has at least one
    * element which is not yed charged. Otherwise returns <code>false</code>
    *
    * @return <code>true</code> if this {@link BusinessDay} has at least one
    * element which is not yed charged. Otherwise returns
    * <code>false</code>
    */
   boolean hasNotChargedElements();

   /**
    * @return <code>true</code> if the {@link TimeRecorder} is currently recording
    * and <code>false</code> if not
    */
   boolean isRecording();

   /**
    * @return <code>true</code> if the {@link TimeRecorder} has currently an active {@link ComeAndGo}
    * and <code>false</code> if not
    */
   boolean isComeAndGoActive();

   /**
    * returns <code>true</code> if currently a booking is running and
    * <code>false</code> if not
    */
   boolean isBooking();

   /**
    * @return <code>true</code> if this {@link TimeRecorder} needs a reminder to start recording or <code>false</code> if not
    */
   boolean needsStartRecordingReminder();

   /**
    * @return <code>true</code> if this {@link TimeRecorder} needs a reminder to book the recorded content or <code>false</code> if not
    */
   boolean needsStartBookingReminder();

   /**
    * @return a {@link BusinessDay} for the current {@link BusinessDay}
    */
   BusinessDay getBusinessDay();

   void setCallbackHandler(UiCallbackHandler callbackHandler);

   /**
    * @return the title of this application
    */
   String getApplicationTitle();
}
