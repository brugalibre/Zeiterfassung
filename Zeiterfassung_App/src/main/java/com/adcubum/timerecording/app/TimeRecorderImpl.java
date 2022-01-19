/**
 * 
 */
package com.adcubum.timerecording.app;

import com.adcubum.librarys.text.res.TextLabel;
import com.adcubum.timerecording.app.businessday.BusinessDayHelper;
import com.adcubum.timerecording.app.businessday.BusinessDayHelperImpl;
import com.adcubum.timerecording.app.startstopresult.StartNotPossibleInfo;
import com.adcubum.timerecording.app.startstopresult.StartNotPossibleInfoImpl;
import com.adcubum.timerecording.app.startstopresult.UserInteractionResult;
import com.adcubum.timerecording.app.startstopresult.UserInteractionResultImpl;
import com.adcubum.timerecording.core.book.adapter.BookerAdapter;
import com.adcubum.timerecording.core.book.adapter.BookerAdapterFactory;
import com.adcubum.timerecording.core.book.result.BookerResult;
import com.adcubum.timerecording.core.businessday.repository.impl.BusinessDayRepositoryImpl;
import com.adcubum.timerecording.core.callbackhandler.UiCallbackHandler;
import com.adcubum.timerecording.core.importexport.in.businessday.BusinessDayImporter;
import com.adcubum.timerecording.core.importexport.in.businessday.exception.BusinessDayImportException;
import com.adcubum.timerecording.core.importexport.out.businessday.BusinessDayExporter;
import com.adcubum.timerecording.core.work.WorkStates;
import com.adcubum.timerecording.core.work.businessday.BusinessDay;
import com.adcubum.timerecording.core.work.businessday.BusinessDayIncrement;
import com.adcubum.timerecording.core.work.businessday.TimeSnippet;
import com.adcubum.timerecording.core.work.businessday.comeandgo.ComeAndGoes;
import com.adcubum.timerecording.core.work.businessday.comeandgo.change.ChangedComeAndGoValue;
import com.adcubum.timerecording.core.work.businessday.history.BusinessDayHistoryOverview;
import com.adcubum.timerecording.core.work.businessday.repository.BusinessDayRepository;
import com.adcubum.timerecording.core.work.businessday.update.callback.impl.BusinessDayIncrementAdd;
import com.adcubum.timerecording.core.work.businessday.update.callback.impl.ChangedValue;
import com.adcubum.timerecording.importexport.in.file.FileImporter;
import com.adcubum.timerecording.importexport.in.file.FileImporterFactory;
import com.adcubum.timerecording.importexport.out.file.FileExportResult;
import com.adcubum.timerecording.importexport.out.file.FileExporter;
import com.adcubum.timerecording.message.Message;
import com.adcubum.timerecording.message.MessageFactory;
import com.adcubum.timerecording.message.MessageType;
import com.adcubum.timerecording.settings.Settings;
import com.adcubum.timerecording.settings.key.ValueKey;
import com.adcubum.timerecording.settings.key.ValueKeyFactory;
import com.adcubum.timerecording.work.date.DateTime;
import com.adcubum.util.utils.FileSystemUtil;

import java.io.File;
import java.util.List;
import java.util.UUID;

/**
 * Responsible for recording the time. The {@link TimeRecorder} consist of one
 * object, that represent a business day. When the user clicks on the tray-icon,
 * a current {@link BusinessDayIncrement} is either started or terminated -
 * depending if the user was working before or not
 * 
 * @author Dominic
 */
public class TimeRecorderImpl implements TimeRecorder {

   private BusinessDayHelper businessDayHelper;
   private Settings settings;
   private UiCallbackHandler callbackHandler;
   private WorkStates currentState;
   private BookerAdapter bookAdapter;

   /**
    * Constructor for testing purpose only!
    */
   TimeRecorderImpl(BookerAdapter bookAdapter, BusinessDayRepository businessDayRepository) {
      this(bookAdapter, Settings.INSTANCE, businessDayRepository);
   }

   /**
    * Default constructor used by Spring
    */
   protected TimeRecorderImpl() {
      this(BookerAdapterFactory.getAdapter(), Settings.INSTANCE, new BusinessDayRepositoryImpl());
   }

   TimeRecorderImpl(BookerAdapter bookAdapter, Settings settings, BusinessDayRepository businessDayRepository) {
      this.businessDayHelper = new BusinessDayHelperImpl(businessDayRepository);
      this.bookAdapter = bookAdapter;
      this.settings = settings;
   }

   @Override
   public void init() {
      BusinessDay businessDay = businessDayHelper.loadExistingOrCreateNew();
      this.currentState = TimeRecorderHelper.evalWorkingState4BusinessDay(businessDay);
      this.settings.init();
      this.bookAdapter.init();
   }

   @Override
   public UserInteractionResult handleUserInteraction(boolean comeOrGo) {
      switch (currentState) {
         case NOT_WORKING:
            StartNotPossibleInfo wasStartSucessful = null;
            if (comeOrGo) {
               tryComeIfPossible();
            } else {
               wasStartSucessful = tryStartIfPossible();
            }
            return new UserInteractionResultImpl(false, wasStartSucessful, currentState);
         case WORKING:
            stop();
            return new UserInteractionResultImpl(true, currentState);
         case COME_AND_GO:
            if (comeOrGo) {
               go();
            }
            return new UserInteractionResultImpl(false, currentState);
         case BOOKING:
            return new UserInteractionResultImpl(false, currentState);
         default:
            throw new IllegalStateException("Unknowing working state '" + currentState + "'!");
      }
   }

   private StartNotPossibleInfo tryStartIfPossible() {
      BusinessDay businessDay = businessDayHelper.getBusinessDay();
      StartNotPossibleInfo startNotPossibleInfo = null;
      if (businessDay.hasElementsFromPrecedentDays()) {
         Message message = MessageFactory.createNew(MessageType.ERROR,
               TextLabel.START_NOT_POSSIBLE_PRECEDENT_ELEMENTS, TextLabel.START_NOT_POSSIBLE_PRECEDENT_ELEMENTS_TITLE);
         callbackHandler.displayMessage(message);
         startNotPossibleInfo = new StartNotPossibleInfoImpl(message);
      } else {
         start();
      }
      return startNotPossibleInfo;
   }

   private void tryComeIfPossible() {
      BusinessDay businessDay = businessDayHelper.getBusinessDay();
      if (businessDay.hasComeAndGoesFromPrecedentDays()) {
         callbackHandler.displayMessage(MessageFactory.createNew(MessageType.ERROR,
               TextLabel.COME_NOT_POSSIBLE_PRECEDENT_ELEMENTS, TextLabel.COME_NOT_POSSIBLE_PRECEDENT_ELEMENTS_TITLE));
      } else if (businessDay.hasElementsFromPrecedentDays()) {
         callbackHandler.displayMessage(MessageFactory.createNew(MessageType.ERROR,
               TextLabel.COME_NOT_POSSIBLE_PRECEDENT_ELEMENTS, TextLabel.COME_NOT_POSSIBLE_PRECEDENT_BDINCREMENTS_TITLE));
      } else {
         come();
      }
   }

   /*
    * Stops the current recording. This leads the {@link TimeRecorder} to switch into the status {@link WorkStates#NOT_WORKING}.
    * Also the UI is shown in order to enter the Ticket-No
    */
   private void stop() {
      currentState = WorkStates.NOT_WORKING;
      BusinessDay businessDay = businessDayHelper.getBusinessDay()
            .stopCurrentIncremental();
      saveBusinessDay(businessDay);
      callbackHandler.onStop();
   }

   private void go() {
      currentState = WorkStates.NOT_WORKING;
      BusinessDay businessDay = businessDayHelper.getBusinessDay()
            .comeOrGo();
      saveBusinessDay(businessDay);
      callbackHandler.onGo();
   }

   private void come() {
      currentState = WorkStates.COME_AND_GO;
      BusinessDay businessDay = businessDayHelper.getBusinessDay()
            .comeOrGo();
      saveBusinessDay(businessDay);
      callbackHandler.onCome();
   }

   private void start() {
      currentState = WorkStates.WORKING;
      BusinessDay businessDay = businessDayHelper.getBusinessDay()
            .startNewIncremental();
      saveBusinessDay(businessDay);
      callbackHandler.onStart();
   }

   @Override
   public void resume() {
      currentState = WorkStates.WORKING;
      BusinessDay businessDay = businessDayHelper.getBusinessDay()
            .resumeLastIncremental();
      saveBusinessDay(businessDay);
      callbackHandler.onResume();
   }

   /////////////////////////////////////////////////////////////////////////////////////////////
   // Change the Business-Day (add, change, remove and so on)
   /////////////////////////////////////////////////////////////////////////////////////////////

   @Override
   public void clear() {
      BusinessDay businessDay = businessDayHelper.getBusinessDay()
            .clearFinishedIncrements();
      saveBusinessDay(businessDay);
   }

   @Override
   public void clearComeAndGoes() {
      BusinessDay businessDay = businessDayHelper.getBusinessDay()
            .clearComeAndGoes();
      saveBusinessDay(businessDay);
   }

   @Override
   public void addBusinessIncrement(BusinessDayIncrementAdd businessDayIncrementAdd) {
      BusinessDay businessDay = businessDayHelper.getBusinessDay()
            .addBusinessIncrement(businessDayIncrementAdd);
      saveBusinessDay(businessDay);
   }

   @Override
   public void removeIncrement4Id(UUID id) {
      BusinessDay businessDay = businessDayHelper.getBusinessDay()
            .removeIncrement4Id(id);
      saveBusinessDay(businessDay);
   }

   @Override
   public void changeBusinesDayIncrement(ChangedValue changeValue) {
      BusinessDay businessDay = businessDayHelper.getBusinessDay()
            .changeBusinesDayIncrement(changeValue);
      saveBusinessDay(businessDay);
   }

   @Override
   public ComeAndGoes changeComeAndGo(ChangedComeAndGoValue changedComeAndGoValue) {
      BusinessDay businessDay = businessDayHelper.getBusinessDay()
            .changeComeAndGo(changedComeAndGoValue);
      saveBusinessDay(businessDay);
      return businessDay.getComeAndGoes();
   }

   @Override
   public void flagBusinessDayComeAndGoesAsRecorded() {
      BusinessDay businessDay = businessDayHelper.getBusinessDay()
            .flagComeAndGoesAsRecorded();
      saveBusinessDay(businessDay);
   }

   /////////////////////////////////////////////////////////////////////////////////////////////
   // Import, Export & Booking
   /////////////////////////////////////////////////////////////////////////////////////////////

   @Override
   public boolean book() {
      boolean hasBooked = false;
      BusinessDay businessDay = businessDayHelper.getBusinessDay();
      if (businessDay.hasNotChargedElements()) {
         WorkStates tmpState = currentState;
         currentState = WorkStates.BOOKING;
         try {
            BookerResult bookResult = bookAdapter.book(businessDay);
            businessDay = businessDayHelper.save(bookResult.getBookedBusinessDay());
            if (bookResult.hasBooked()) {
               businessDayHelper.addBookedBusinessDayIncrements(businessDay.getIncrements());
               callbackHandler.displayMessage(MessageFactory.createNew(map2MessageType(bookResult), null, bookResult.getMessage()));
               hasBooked = true;
            }
         } finally {
            currentState = tmpState;
         }
      }
      return hasBooked;
   }

   private static MessageType map2MessageType(BookerResult bookResult) {
      switch (bookResult.getBookResultType()) {
         case SUCCESS:
            return MessageType.INFORMATION;
         case PARTIAL_SUCCESS_WITH_ERROR:// fall through
         case PARTIAL_SUCCESS_WITH_NON_BOOKABLE:
            return MessageType.WARNING;
         case FAILURE:
         default:// fall through
            return MessageType.ERROR;
      }
   }

   @Override
   public FileExportResult export() {
      return export(FileSystemUtil.getHomeDir());
   }

   private FileExportResult export(String path2Export) {
      FileExportResult fileExportResult = exportSilently(path2Export);
      if (fileExportResult.isSuccess()) {
         callbackHandler.displayMessage(MessageFactory.createNew(MessageType.INFORMATION, null, TextLabel.SUCESSFULLY_EXPORTED));
      } else {
         callbackHandler.displayMessage(MessageFactory.createNew(MessageType.ERROR, fileExportResult.getErrorMsg(), TextLabel.EXPORT_FAILED_TITLE));
      }
      return fileExportResult;
   }

   @Override
   public FileExportResult exportSilently(String path2Export) {

      BusinessDay businessDay = businessDayHelper.getBusinessDay();
      List<String> content = BusinessDayExporter.INSTANCE.exportBusinessDay(businessDay);
      return FileExporter.INTANCE.exportWithResult(content, path2Export);
   }

   @Override
   public void onTicketBacklogInitialized() {
      // This is necessary if the user started the recording while he/she was offline
      BusinessDay businessDay = businessDayHelper.getBusinessDay();
      businessDayHelper.save(businessDay.refreshDummyTickets());
   }

   @Override
   public boolean importBusinessDayFromFile(File file) {
      try {
         importBusinessDayInternal(file);
         return true;
      } catch (BusinessDayImportException e) {
         e.printStackTrace();
         // since the import failed, we need to create manually a new one
         businessDayHelper.createNew();
      }
      return false;
   }

   private void importBusinessDayInternal(File file) {
      FileImporter fileImporter = FileImporterFactory.createNew();
      List<String> fileContent = fileImporter.importFile(file);
      BusinessDay importedBusinessDay = BusinessDayImporter.INTANCE.importBusinessDay(fileContent);
      // First delete all existing entries, since we may have already a persistent (but so far empty) businessDay
      businessDayHelper.deleteAll(false);
      businessDayHelper.save(importedBusinessDay);
   }

   /////////////////////////////////////////////////////////////////////////////////////////////
   // Getter & Setter
   /////////////////////////////////////////////////////////////////////////////////////////////

   @Override
   public String getInfoStringForState() {
      BusinessDay businessDay = businessDayHelper.getBusinessDay();
      switch (currentState) {
         case NOT_WORKING:
            return businessDay.getCapturingInactiveSinceMsg();
         case WORKING:
            return businessDay.getCapturingActiveSinceMsg();
         case COME_AND_GO:
            return businessDay.getComeAndGoMsg();
         case BOOKING:
            return TextLabel.BOOKING_RUNNING;
         default:
            throw new IllegalStateException("Unknowing working state '" + currentState + "'!");
      }
   }

   @Override
   public String getSettingsValue(String key) {
      ValueKey<String> valueKey = ValueKeyFactory.createNew(key, String.class);
      return settings.getSettingsValue(valueKey);
   }

   @Override
   public void saveSettingValue(String value, String key) {
      ValueKey<String> valueKey = ValueKeyFactory.createNew(key, String.class);
      settings.saveValueToProperties(valueKey, value);
   }

   @Override
   public boolean hasContent() {
      BusinessDay businessDay = businessDayHelper.getBusinessDay();
      return businessDay.getTotalDuration() > 0f;
   }

   @Override
   public boolean hasNotChargedElements() {
      BusinessDay businessDay = businessDayHelper.getBusinessDay();
      return businessDay.hasNotChargedElements();
   }

   @Override
   public boolean isRecording() {
      return currentState == WorkStates.WORKING;
   }

   @Override
   public boolean isComeAndGoActive() {
      return currentState == WorkStates.COME_AND_GO;
   }

   @Override
   public boolean isBooking() {
      return currentState == WorkStates.BOOKING;
   }

   @Override
   public boolean needsStartRecordingReminder() {
      return !isRecording()
            && !isComeAndGoActive()
            && !hasContent();
   }

   @Override
   public boolean needsStartBookingReminder() {
      BusinessDay businessDay = businessDayHelper.getBusinessDay();
      return businessDay.hasNotChargedElements() || businessDay.hasNotRecordedComeAndGoContent();
   }

   @Override
   public TimeSnippet getCurrentBussinessDayIncrement() {
      BusinessDay businessDay = businessDayHelper.getBusinessDay();
      return businessDay.getCurrentTimeSnippet();
   }

   @Override
   public String getApplicationTitle() {
      return TimeRecorderHelper.getApplicationTitle();
   }

   @Override
   public BusinessDay getBussinessDay() {
      synchronized (businessDayHelper) {
         return businessDayHelper.getBusinessDay();
      }
   }

   @Override
   public BusinessDayHistoryOverview getBusinessDayHistoryOverview(DateTime lowerBounds, DateTime upperBounds) {
      return businessDayHelper.getBusinessDayHistoryOverview(lowerBounds, upperBounds);
   }

   @Override
   public void setCallbackHandler(UiCallbackHandler callbackHandler) {
      this.callbackHandler = callbackHandler;
   }

   /*
    * Stores the current changes on the BusinessDay to the repository.
    * Also, since we map from a BusinessDay to a BusinessDayEntity and back, we need to assign
    * the new re-mapped BusinessDayEntity to the existing BusinessDay instance
    */
   private void saveBusinessDay(BusinessDay businessDay) {
      businessDayHelper.save(businessDay);
   }
}
