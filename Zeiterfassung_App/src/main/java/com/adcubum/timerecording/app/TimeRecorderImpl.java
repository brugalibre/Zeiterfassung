/**
 * 
 */
package com.adcubum.timerecording.app;

import java.io.File;
import java.util.List;

import com.adcubum.librarys.text.res.TextLabel;
import com.adcubum.timerecording.core.book.adapter.BookerAdapter;
import com.adcubum.timerecording.core.book.adapter.BookerAdapterFactory;
import com.adcubum.timerecording.core.book.result.BookerResult;
import com.adcubum.timerecording.core.callbackhandler.UiCallbackHandler;
import com.adcubum.timerecording.core.importexport.in.businessday.BusinessDayImporter;
import com.adcubum.timerecording.core.importexport.in.businessday.exception.BusinessDayImportException;
import com.adcubum.timerecording.core.importexport.out.businessday.BusinessDayExporter;
import com.adcubum.timerecording.core.work.WorkStates;
import com.adcubum.timerecording.core.work.businessday.BusinessDay;
import com.adcubum.timerecording.core.work.businessday.BusinessDayImpl;
import com.adcubum.timerecording.core.work.businessday.BusinessDayIncrement;
import com.adcubum.timerecording.core.work.businessday.comeandgo.ComeAndGoes;
import com.adcubum.timerecording.core.work.businessday.comeandgo.change.ChangedComeAndGoValue;
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

   private BusinessDay businessDay;
   private Settings settings;
   private UiCallbackHandler callbackHandler;
   private WorkStates currentState;
   private BookerAdapter bookAdapter;

   /**
    * Constructor for testing purpose only!
    */
   TimeRecorderImpl(BookerAdapter bookAdapter, BusinessDay businessDay) {
      this.bookAdapter = bookAdapter;
      this.businessDay = businessDay;
      this.settings = Settings.INSTANCE;
      currentState = WorkStates.NOT_WORKING;
   }

   TimeRecorderImpl(BookerAdapter bookAdapter) {
      this(bookAdapter, Settings.INSTANCE);
   }

   /**
    * Default constructor used by Spring
    */
   protected TimeRecorderImpl() {
      this(BookerAdapterFactory.getAdapter(), Settings.INSTANCE);
   }

   TimeRecorderImpl(BookerAdapter bookAdapter, Settings settings) {
      this.bookAdapter = bookAdapter;
      this.settings = settings;
      init();
   }

   @Override
   public void init() {
      currentState = WorkStates.NOT_WORKING;
      businessDay = new BusinessDayImpl();
      settings.init();
   }

   @Override
   public boolean handleUserInteraction(boolean comeOrGo) {
      switch (currentState) {
         case NOT_WORKING:
            if (comeOrGo) {
               tryComeIfPossible();
            } else {
               tryStartIfPossible();
            }
            return false;
         case WORKING:
            stop();
            return true;
         case COME_AND_GO:
            if (comeOrGo) {
               go();
            }
            return false;
         case BOOKING:
            return false;
         default:
            throw new IllegalStateException("Unknowing working state '" + currentState + "'!");
      }
   }

   private void tryStartIfPossible() {
      if (businessDay.hasElementsFromPrecedentDays()) {
         callbackHandler.displayMessage(MessageFactory.createNew(MessageType.ERROR,
               TextLabel.START_NOT_POSSIBLE_PRECEDENT_ELEMENTS, TextLabel.START_NOT_POSSIBLE_PRECEDENT_ELEMENTS_TITLE));
      } else {
         start();
      }
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
   }

   private void go() {
      currentState = WorkStates.NOT_WORKING;
      businessDay.comeOrGo();
      callbackHandler.onGo();
   }

   private void come() {
      currentState = WorkStates.COME_AND_GO;
      businessDay.comeOrGo();
      callbackHandler.onCome();
   }

   private void start() {
      currentState = WorkStates.WORKING;
      businessDay.startNewIncremental();
      callbackHandler.onStart();
   }

   @Override
   public void resume() {
      currentState = WorkStates.WORKING;
      businessDay.resumeLastIncremental();
      callbackHandler.onResume();
   }

   /////////////////////////////////////////////////////////////////////////////////////////////
   // Change the Business-Day (add, change, remove and so on)
   /////////////////////////////////////////////////////////////////////////////////////////////

   @Override
   public void clear() {
      businessDay.clearFinishedIncrements();
   }

   @Override
   public void clearComeAndGoes() {
      businessDay.clearComeAndGoes();
   }

   @Override
   public void addBusinessIncrement(BusinessDayIncrementAdd businessDayIncrementAdd) {
      businessDay.addBusinessIncrement(businessDayIncrementAdd);
   }

   @Override
   public void removeIncrementAtIndex(int index) {
      businessDay.removeIncrementAtIndex(index);
   }

   @Override
   public void changeBusinesDayIncrement(ChangedValue changeValue) {
      businessDay.changeBusinesDayIncrement(changeValue);
   }

   @Override
   public ComeAndGoes changeComeAndGo(ChangedComeAndGoValue changedComeAndGoValue) {
      return businessDay.changeComeAndGo(changedComeAndGoValue);
   }

   @Override
   public void flagBusinessDayComeAndGoesAsRecorded() {
      businessDay.flagComeAndGoesAsRecorded();
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
      // This is necessary if the user startet the recording while he/she was offline
      businessDay.refreshDummyTickets();
   }

   @Override
   public boolean importBusinessDayFromFile(File file) {
      try {
         importBusinessDayInternal(file);
         return true;
      } catch (BusinessDayImportException e) {
         e.printStackTrace();
         // Nothing more to do
      }
      return false;
   }

   private void importBusinessDayInternal(File file) {
      FileImporter fileImporter = FileImporterFactory.createNew();
      List<String> fileContent = fileImporter.importFile(file);
      this.businessDay = BusinessDayImporter.INTANCE.importBusinessDay(fileContent);
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
   public boolean isRecordindg() {
      return currentState == WorkStates.WORKING;
   }

   @Override
   public boolean isBooking() {
      return currentState == WorkStates.BOOKING;
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
   public void setCallbackHandler(UiCallbackHandler callbackHandler) {
      this.callbackHandler = callbackHandler;
   }
}
