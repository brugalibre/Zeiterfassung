/**
 * 
 */
package com.adcubum.timerecording.app;

import java.io.File;
import java.util.List;

import com.adcubum.librarys.text.res.TextLabel;
import com.adcubum.timerecording.core.book.adapter.BookerAdapter;
import com.adcubum.timerecording.core.book.adapter.BookerAdapterFactory;
import com.adcubum.timerecording.core.book.adapter.ServiceCodeAdapter;
import com.adcubum.timerecording.core.book.result.BookerResult;
import com.adcubum.timerecording.core.callbackhandler.UiCallbackHandler;
import com.adcubum.timerecording.core.importexport.in.businessday.BusinessDayImporter;
import com.adcubum.timerecording.core.importexport.in.businessday.exception.BusinessDayImportException;
import com.adcubum.timerecording.core.importexport.out.businessday.BusinessDayExporter;
import com.adcubum.timerecording.core.work.WorkStates;
import com.adcubum.timerecording.core.work.businessday.BusinessDay;
import com.adcubum.timerecording.core.work.businessday.BusinessDayIncrement;
import com.adcubum.timerecording.core.work.businessday.ValueTypes;
import com.adcubum.timerecording.core.work.businessday.comeandgo.ComeAndGo;
import com.adcubum.timerecording.core.work.businessday.comeandgo.ComeAndGoes;
import com.adcubum.timerecording.core.work.businessday.comeandgo.change.ChangedComeAndGoValue;
import com.adcubum.timerecording.core.work.businessday.update.callback.impl.BusinessDayIncrementAdd;
import com.adcubum.timerecording.core.work.businessday.update.callback.impl.ChangedValue;
import com.adcubum.timerecording.core.work.businessday.vo.BusinessDayIncrementVO;
import com.adcubum.timerecording.core.work.businessday.vo.BusinessDayVO;
import com.adcubum.timerecording.importexport.in.file.FileImporter;
import com.adcubum.timerecording.importexport.in.file.FileImporterImpl;
import com.adcubum.timerecording.importexport.out.file.FileExportResult;
import com.adcubum.timerecording.importexport.out.file.FileExporter;
import com.adcubum.timerecording.message.Message;
import com.adcubum.timerecording.message.MessageType;
import com.adcubum.util.utils.FileSystemUtil;

/**
 * Responsible for recording the time. The {@link TimeRecorder} consist of one
 * object, that represent a business day. When the user clicks on the tray-icon,
 * a current {@link BusinessDayIncrement} is either started or terminated -
 * depending if the user was working before or not
 * 
 * @author Dominic
 */
public class TimeRecorder {

   /**
    * The singleton instance of this class
    */
   public static final TimeRecorder INSTANCE = new TimeRecorder();

   /**
    * The version of the application
    */
   public static final String VERSION = "1.8.0";

   private BusinessDay businessDay;
   private UiCallbackHandler callbackHandler;
   private WorkStates currentState;
   private BookerAdapter bookAdapter;

   /**
    * Constructor for testing purpose only!
    */
   TimeRecorder(BookerAdapter bookAdapter, BusinessDay businessDay) {
      this.bookAdapter = bookAdapter;
      this.businessDay = businessDay;
      currentState = WorkStates.NOT_WORKING;
   }

   TimeRecorder(BookerAdapter bookAdapter) {
      this.bookAdapter = bookAdapter;
      init();
   }

   private TimeRecorder() {
      bookAdapter = BookerAdapterFactory.getAdapter();
      init();
   }

   void init() {
      currentState = WorkStates.NOT_WORKING;
      businessDay = new BusinessDay();
   }

   /**
     * Either starts a new or stops the current recording.
     * 
     * @formatter:off
     *	<ul>
     *  	<li>Start: Starts a new recording. This leads the {@link TimeRecorder} to switch into the status
     *                	{@link WorkStates#WORKING}.
     * 		</li>
     *          <li>Stop: Stops the current recording. This This leads the {@link TimeRecorder} to switch into
     *          	the status {@link WorkStates#NOT_WORKING}. Also the UI is shown in 
     *          	order to enter the Ticket-No
     *          </li>
     *	</ul>
     * @formatter:on
    * @param comeOrGo <code>true</code> if the user started/stopped a come or go or <code>false</code> if the user started/stopped a recording
     * @return <code>true</code> if the {@link TimeRecorder} is working or
     *         <code>false</code> if not
     */
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
         callbackHandler.displayMessage(Message.of(MessageType.ERROR,
               TextLabel.START_NOT_POSSIBLE_PRECEDENT_ELEMENTS, TextLabel.START_NOT_POSSIBLE_PRECEDENT_ELEMENTS_TITLE));
      } else {
         start();
      }
   }

   private void tryComeIfPossible() {
      if (businessDay.hasComeAndGoesFromPrecedentDays()) {
         callbackHandler.displayMessage(Message.of(MessageType.ERROR,
               TextLabel.COME_NOT_POSSIBLE_PRECEDENT_ELEMENTS, TextLabel.COME_NOT_POSSIBLE_PRECEDENT_ELEMENTS_TITLE));
      } else if (businessDay.hasElementsFromPrecedentDays()) {
         callbackHandler.displayMessage(Message.of(MessageType.ERROR,
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

   /**
    * Resumes a previously stopped recording
    */
   public void resume() {
      currentState = WorkStates.WORKING;
      businessDay.resumeLastIncremental();
      callbackHandler.onResume();
   }

   /////////////////////////////////////////////////////////////////////////////////////////////
   // Change the Business-Day (add, change, remove and so on)
   /////////////////////////////////////////////////////////////////////////////////////////////

   /**
    * removes all recorded {@link BusinessDayIncrement}
    */
   public void clear() {
      businessDay.clearFinishedIncrements();
   }

   /**
    * removes all recorded {@link BusinessDayIncrement}
    */
   public void clearComeAndGoes() {
      businessDay.clearComeAndGoes();
   }

   /**
    * Creates and adds a new {@link BusinessDayIncrement} for the given
    * {@link BusinessDayIncrementAdd}
    * 
    * @param update
    *        the {@link BusinessDayIncrementAdd} which defines the new
    *        {@link BusinessDayIncrement}
    */
   public void addBusinessIncrement(BusinessDayIncrementAdd businessDayIncrementAdd) {
      businessDay.addBusinessIncrement(businessDayIncrementAdd);
   }

   /**
    * Removes the {@link BusinessDayIncrement} at the given index. If there is no
    * {@link BusinessDayIncrement} for this index nothing is done
    * 
    * @param index
    *        the given index
    */
   public void removeIncrementAtIndex(int index) {
      businessDay.removeIncrementAtIndex(index);
   }

   /**
    * According to the given {@link ChangedValue} the corresponding
    * {@link BusinessDayIncrement} evaluated. If there is one then the value is
    * changed
    * 
    * @param changeValue
    *        the param which defines what value is changed
    * @see ValueTypes
    */
   public void changeBusinesDayIncrement(ChangedValue changeValue) {
      businessDay.changeBusinesDayIncrement(changeValue);
   }

   /**
    * Updates a {@link ComeAndGo} for the given changed values
    * 
    * @param changedComeAndGoValue
    *        the {@link ChangedComeAndGoValue} define the changed values
    * @return the changed {@link ComeAndGoes}
    */
   public ComeAndGoes changeComeAndGo(ChangedComeAndGoValue changedComeAndGoValue) {
      return businessDay.changeComeAndGo(changedComeAndGoValue);
   }

   /**
    * Flags all {@link ComeAndGo}es of the {@link BusinessDay} as recorded
    */
   public void flagBusinessDayComeAndGoesAsRecorded() {
      businessDay.flagComeAndGoesAsRecorded();
   }

   /////////////////////////////////////////////////////////////////////////////////////////////
   // Import, Export & Booking
   /////////////////////////////////////////////////////////////////////////////////////////////

   /**
    * Collects and export the necessary data which is used by the TurobBucher to
    * charge After the tuber-bucher- app is invoked in order to do actual charge
    * 
    * @return <code>true</code> if there was actually a booking process or
    *         <code>false</code> if there wasn't anything to do
    */
   public boolean book() {
      boolean hasBooked = false;
      if (businessDay.hasNotChargedElements()) {
         WorkStates tmpState = currentState;
         currentState = WorkStates.BOOKING;
         try {
            BookerResult bookResult = bookAdapter.book(businessDay);
            if (bookResult.hasBooked()) {
               callbackHandler.displayMessage(Message.of(map2MessageType(bookResult), null, bookResult.getMessage()));
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

   /**
    * Exports the current {@link BusinessDay} to the users home-directory
    * 
    * @return the result of the export
    */
   public FileExportResult export() {
      return export(FileSystemUtil.getHomeDir());
   }

   private FileExportResult export(String path2Export) {
      FileExportResult fileExportResult = exportSilently(path2Export);
      if (fileExportResult.isSuccess()) {
         callbackHandler.displayMessage(Message.of(MessageType.INFORMATION, null, TextLabel.SUCESSFULLY_EXPORTED));
      } else {
         callbackHandler.displayMessage(Message.of(MessageType.ERROR, fileExportResult.getErrorMsg(), TextLabel.EXPORT_FAILED_TITLE));
      }
      return fileExportResult;
   }

   /**
    * Exports the current {@link BusinessDay} to the given path and ignores any result
    * 
    * @param path2Export
    *        the path
    * @return the result of the export
    */
   public FileExportResult exportSilently(String path2Export) {
      List<String> content = BusinessDayExporter.INSTANCE.exportBusinessDay(BusinessDayVO.of(businessDay));
      return FileExporter.INTANCE.exportWithResult(content, path2Export);
   }

   /**
    * Is called after the user was successfully loged-in
    */
   public void onSuccessfullyLogin() {
      // This is necessary if the user startet the recording while he/she was offline
      businessDay.refreshDummyTickets();
   }

   /**
    * First the {@link FileImporterImpl} imports the given {@link File} and fills
    * it's content into a list. Later the {@link BusinessDayImporter} uses this
    * lists in order to import a a new {@link BusinessDay}
    * 
    * @param file
    *        the file to import
    * @return <code>true</code> if the new {@link BusinessDay} was successfully
    *         imported or <code>false</code> if not
    */
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
      List<String> fileContent = FileImporter.INTANCE.importFile(file);
      this.businessDay = BusinessDayImporter.INTANCE.importBusinessDay(fileContent);
   }

   /////////////////////////////////////////////////////////////////////////////////////////////
   // Getter & Setter
   /////////////////////////////////////////////////////////////////////////////////////////////

   /**
    * Return a String, which represents the current state and shows informations
    * according to this
    * 
    * @return a String, which represents the current state and shows informations
    *         according to this
    * @see WorkStates
    */
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

   /**
    * Return <code>true</code> if there is any content, <code>false</code> if not
    * 
    * @return <code>true</code> if there is any content, <code>false</code> if not
    */
   public boolean hasContent() {
      return businessDay.getTotalDuration() > 0f;
   }

   /**
    * Returns <code>true</code> if this {@link BusinessDay} has at least one
    * element which is not yed charged. Otherwise returns <code>false</code>
    * 
    * @return <code>true</code> if this {@link BusinessDay} has at least one
    *         element which is not yed charged. Otherwise returns
    *         <code>false</code>
    */
   public boolean hasNotChargedElements() {
      return businessDay.hasNotChargedElements();
   }

   /**
    * @return <code>true</code> if the {@link TimeRecorder} is currently recording
    *         and <code>false</code> if not
    */
   public boolean isRecordindg() {
      return currentState == WorkStates.WORKING;
   }

   /**
    * returns <code>true</code> if currently a booking is running and
    * <code>false</code> if not
    */
   public boolean isBooking() {
      return currentState == WorkStates.BOOKING;
   }

   /**
    * @return a {@link BusinessDayIncrementVO} for the current {@link BusinessDayIncrement} of the {@link BusinessDay}
    */
   public BusinessDayIncrementVO getCurrentBussinessDayIncrement() {
      return BusinessDayIncrementVO.of(businessDay.getCurrentBussinessDayIncremental());
   }

   /**
    * @return <code>true</code> if the current {@link BusinessDay} has at least
    *         on {@link BusinessDayIncrement} with a description otherwise
    *         <code>false</code>
    */
   public boolean hasBusinessDayDescription() {
      return businessDay.hasDescription();
   }

   /**
    * @return the {@link ServiceCodeAdapter} of this {@link TimeRecorder}
    */
   public ServiceCodeAdapter getServiceCodeAdapter() {
      return bookAdapter.getServiceCodeAdapter();
   }

   /**
    * @return a {@link BusinessDayVO} for the current {@link BusinessDay}
    */
   public BusinessDayVO getBussinessDayVO() {
      synchronized (businessDay) {
         return BusinessDayVO.of(businessDay);
      }
   }

   public void setCallbackHandler(UiCallbackHandler callbackHandler) {
      this.callbackHandler = callbackHandler;
   }
}
