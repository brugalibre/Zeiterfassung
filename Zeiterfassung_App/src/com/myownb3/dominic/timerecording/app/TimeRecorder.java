/**
 * 
 */
package com.myownb3.dominic.timerecording.app;

import java.io.File;
import java.util.List;

import com.myownb3.dominic.librarys.text.res.TextLabel;
import com.myownb3.dominic.timerecording.core.callbackhandler.CallbackHandler;
import com.myownb3.dominic.timerecording.core.callbackhandler.impl.BusinessDayIncrementAdd;
import com.myownb3.dominic.timerecording.core.callbackhandler.impl.ChangedValue;
import com.myownb3.dominic.timerecording.core.charge.BookerHelper;
import com.myownb3.dominic.timerecording.core.importexport.in.businessday.BusinessDayImporter;
import com.myownb3.dominic.timerecording.core.importexport.in.businessday.exception.BusinessDayImportException;
import com.myownb3.dominic.timerecording.core.importexport.in.file.FileImporter;
import com.myownb3.dominic.timerecording.core.importexport.out.businessday.BusinessDayExporter;
import com.myownb3.dominic.timerecording.core.importexport.out.file.FileExportResult;
import com.myownb3.dominic.timerecording.core.importexport.out.file.FileExporter;
import com.myownb3.dominic.timerecording.core.message.Message;
import com.myownb3.dominic.timerecording.core.message.MessageType;
import com.myownb3.dominic.timerecording.core.work.WorkStates;
import com.myownb3.dominic.timerecording.core.work.businessday.BusinessDay;
import com.myownb3.dominic.timerecording.core.work.businessday.BusinessDayIncrement;
import com.myownb3.dominic.timerecording.core.work.businessday.ValueTypes;
import com.myownb3.dominic.timerecording.core.work.businessday.vo.BusinessDayIncrementVO;
import com.myownb3.dominic.timerecording.core.work.businessday.vo.BusinessDayVO;

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
   public static final String VERSION = "1.6.4";

   private BusinessDay businessDay;
   private CallbackHandler callbackHandler;
   private WorkStates currentState;

   private TimeRecorder() {
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
     * @return <code>true</code> if the {@link TimeRecorder} is working or
     *         <code>false</code> if not
     */
   public boolean handleUserInteraction() {
      switch (currentState) {
         case NOT_WORKING:
            tryStartIfPossible();
            return false;

         case WORKING:
            stop();
            return true;
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

   /*
    * Stops the current recording. This leads the {@link TimeRecorder} to switch into the status {@link WorkStates#NOT_WORKING}.
    * Also the UI is shown in order to enter the Ticket-No
    */
   private void stop() {
      businessDay.stopCurrentIncremental();
      currentState = WorkStates.NOT_WORKING;
      callbackHandler.onStop();
   }

   private void start() {
      if (currentState == WorkStates.WORKING) {
         return;
      }
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
      if (businessDay.hasNotChargedElements()) {
         WorkStates tmpState = currentState;
         currentState = WorkStates.BOOKING;
         try {
            BookerHelper helper = new BookerHelper(businessDay);
            helper.book();
            return true;
         } finally {
            currentState = tmpState;
         }
      }
      return false;
   }

   /**
    * Exports the current {@link BusinessDay} to the file system
    */
   public void export() {
      List<String> content = BusinessDayExporter.INSTANCE.exportBusinessDay(BusinessDayVO.of(businessDay));
      FileExportResult fileExportResult = FileExporter.INTANCE.exportWithResult(content);
      if (fileExportResult.isSuccess()) {
         callbackHandler.displayMessage(Message.of(MessageType.INFORMATION, null, TextLabel.SUCESSFULLY_EXPORTED));
      } else {
         callbackHandler.displayMessage(Message.of(MessageType.ERROR, fileExportResult.getErrorMsg(), TextLabel.EXPORT_FAILED_TITLE));
      }
   }

   /**
    * First the {@link FileImporter} imports the given {@link File} and fills
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
    * @return a {@link BusinessDayVO} for the current {@link BusinessDay}
    */
   public BusinessDayVO getBussinessDayVO() {
      synchronized (businessDay) {
         return BusinessDayVO.of(businessDay);
      }
   }

   public void setCallbackHandler(CallbackHandler callbackHandler) {
      this.callbackHandler = callbackHandler;
   }
}
