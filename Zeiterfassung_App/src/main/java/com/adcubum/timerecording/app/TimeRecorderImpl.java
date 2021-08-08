/**
 * 
 */
package com.adcubum.timerecording.app;

import java.io.File;
import java.util.List;
import java.util.UUID;

import com.adcubum.librarys.text.res.TextLabel;
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
import com.adcubum.timerecording.core.work.businessday.comeandgo.ComeAndGoes;
import com.adcubum.timerecording.core.work.businessday.comeandgo.change.ChangedComeAndGoValue;
import com.adcubum.timerecording.core.work.businessday.repository.BusinessDayRepository;
import com.adcubum.timerecording.core.work.businessday.update.callback.impl.BusinessDayIncrementAdd;
import com.adcubum.timerecording.core.work.businessday.update.callback.impl.ChangedValue;
import com.adcubum.timerecording.core.work.businessday.vo.BusinessDayIncrementVO;
import com.adcubum.timerecording.core.work.businessday.vo.BusinessDayIncrementVOImpl;
import com.adcubum.timerecording.core.work.businessday.vo.BusinessDayVO;
import com.adcubum.timerecording.core.work.businessday.vo.BusinessDayVOImpl;
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
import com.adcubum.util.utils.FileSystemUtil;

/**
 * Responsible for recording the time. The {@link TimeRecorder} consist of one
 * object, that represent a business day. When the user clicks on the tray-icon,
 * a current {@link BusinessDayIncrement} is either started or terminated -
 * depending if the user was working before or not
 * 
 * @author Dominic
 */
public class TimeRecorderImpl implements TimeRecorder {

   private BusinessDayRepository businessDayRepository;
   private BusinessDay businessDay;
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
      this.businessDayRepository = businessDayRepository;
      this.bookAdapter = bookAdapter;
      this.settings = settings;
   }

   @Override
   public void init() {
      this.businessDay = businessDayRepository.findFirstOrCreateNew();
      this.currentState = TimeRecorderHelper.evalWorkingState4BusinessDay(businessDay);
      settings.init();
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
      businessDay.stopCurrentIncremental();
      currentState = WorkStates.NOT_WORKING;
      callbackHandler.onStop();
      saveBusinessDay();
   }

   private void go() {
      currentState = WorkStates.NOT_WORKING;
      businessDay.comeOrGo();
      callbackHandler.onGo();
      saveBusinessDay();
   }

   private void come() {
      currentState = WorkStates.COME_AND_GO;
      businessDay.comeOrGo();
      callbackHandler.onCome();
      saveBusinessDay();
   }

   private void start() {
      currentState = WorkStates.WORKING;
      businessDay.startNewIncremental();
      callbackHandler.onStart();
      saveBusinessDay();
   }

   @Override
   public void resume() {
      currentState = WorkStates.WORKING;
      businessDay.resumeLastIncremental();
      callbackHandler.onResume();
      saveBusinessDay();
   }

   /////////////////////////////////////////////////////////////////////////////////////////////
   // Change the Business-Day (add, change, remove and so on)
   /////////////////////////////////////////////////////////////////////////////////////////////

   @Override
   public void clear() {
      businessDay.clearFinishedIncrements();
      saveBusinessDay();
   }

   @Override
   public void clearComeAndGoes() {
      businessDay.clearComeAndGoes();
      saveBusinessDay();
   }

   @Override
   public void addBusinessIncrement(BusinessDayIncrementAdd businessDayIncrementAdd) {
      businessDay.addBusinessIncrement(businessDayIncrementAdd);
      saveBusinessDay();
   }

   @Override
   public void removeIncrement4Id(UUID id) {
      businessDay.removeIncrement4Id(id);
      saveBusinessDay();
   }

   @Override
   public void changeBusinesDayIncrement(ChangedValue changeValue) {
      businessDay.changeBusinesDayIncrement(changeValue);
      saveBusinessDay();
   }

   @Override
   public ComeAndGoes changeComeAndGo(ChangedComeAndGoValue changedComeAndGoValue) {
      ComeAndGoes changeComeAndGo = businessDay.changeComeAndGo(changedComeAndGoValue);
      saveBusinessDay();
      return changeComeAndGo;
   }

   @Override
   public void flagBusinessDayComeAndGoesAsRecorded() {
      businessDay.flagComeAndGoesAsRecorded();
      saveBusinessDay();
   }

   /////////////////////////////////////////////////////////////////////////////////////////////
   // Import, Export & Booking
   /////////////////////////////////////////////////////////////////////////////////////////////

   @Override
   public boolean book() {
      boolean hasBooked = false;
      if (businessDay.hasNotChargedElements()) {
         WorkStates tmpState = currentState;
         currentState = WorkStates.BOOKING;
         try {
            BookerResult bookResult = bookAdapter.book(businessDay);
            businessDay = businessDayRepository.save(businessDay);
            if (bookResult.hasBooked()) {
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
      List<String> content = BusinessDayExporter.INSTANCE.exportBusinessDay(businessDay);
      return FileExporter.INTANCE.exportWithResult(content, path2Export);
   }

   @Override
   public void onSuccessfullyLogin() {
      // This is necessary if the user started the recording while he/she was offline
      businessDay.refreshDummyTickets();
   }

   @Override
   public boolean importBusinessDayFromFile(File file) {
      try {
         importBusinessDayInternal(file);
         return true;
      } catch (BusinessDayImportException e) {
         e.printStackTrace();
         // since the import failed, we need to create manually a new one
         this.businessDay = businessDayRepository.findFirstOrCreateNew();
      }
      return false;
   }

   private void importBusinessDayInternal(File file) {
      FileImporter fileImporter = FileImporterFactory.createNew();
      List<String> fileContent = fileImporter.importFile(file);
      BusinessDay importedBusinessDay = BusinessDayImporter.INTANCE.importBusinessDay(fileContent);
      // First delete all existing entries, since we may have already a persistent (but so far empty) businessDay
      businessDayRepository.deleteAll();
      this.businessDay = businessDayRepository.save(importedBusinessDay);
   }

   /////////////////////////////////////////////////////////////////////////////////////////////
   // Getter & Setter
   /////////////////////////////////////////////////////////////////////////////////////////////

   @Override
   public String getInfoStringForState() {
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
      return businessDay.getTotalDuration() > 0f;
   }

   @Override
   public boolean hasNotChargedElements() {
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
      return businessDay.hasNotChargedElements() || businessDay.hasNotRecordedComeAndGoContent();
   }

   @Override
   public BusinessDayIncrementVO getCurrentBussinessDayIncrement() {
      return BusinessDayIncrementVOImpl.of(businessDay.getCurrentBussinessDayIncremental());
   }

   /**
    * @return a {@link BusinessDayVO} for the current {@link BusinessDay}
    */
   @Override
   public BusinessDayVO getBussinessDayVO() {
      synchronized (businessDay) {
         return BusinessDayVOImpl.of(businessDay);
      }
   }

   @Override
   public BusinessDay getBussinessDay() {
      synchronized (businessDay) {
         return businessDay;
      }
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
   private void saveBusinessDay() {
      this.businessDay = businessDayRepository.save(businessDay);
   }
}
