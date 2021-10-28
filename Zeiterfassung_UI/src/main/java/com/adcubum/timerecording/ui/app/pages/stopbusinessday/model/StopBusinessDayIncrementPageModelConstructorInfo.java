/**
 * 
 */
package com.adcubum.timerecording.ui.app.pages.stopbusinessday.model;

import static java.util.Objects.nonNull;

import com.adcubum.librarys.text.res.TextLabel;
import com.adcubum.timerecording.core.work.businessday.BusinessDayIncrement;
import com.adcubum.timerecording.core.work.businessday.TimeSnippet;
import com.adcubum.timerecording.core.work.businessday.TimeSnippetFactory;
import com.adcubum.timerecording.core.work.businessday.comeandgo.ComeAndGo;
import com.adcubum.timerecording.core.work.businessday.update.callback.BusinessDayChangedCallbackHandler;
import com.adcubum.timerecording.core.work.businessday.update.callback.impl.BusinessDayChangedCallbackHandlerFactory;
import com.adcubum.timerecording.jira.constants.TicketConst;
import com.adcubum.timerecording.ui.app.pages.comeandgo.model.ComeAndGoOverviewPageModel;
import com.adcubum.timerecording.ui.app.pages.comeandgo.view.ComeAndGoOverviewPage;
import com.adcubum.timerecording.ui.app.pages.stopbusinessday.view.StopBusinessDayIncrementPage;
import com.adcubum.timerecording.work.date.DateTime;
import com.adcubum.timerecording.work.date.DateTimeFactory;
import com.adcubum.timerecording.work.date.DateTimeUtil;
import com.adcubum.util.parser.NumberFormat;

/**
 * The {@link StopBusinessDayIncrementPageModelConstructorInfo} acts as a container for the {@link StopBusinessDayIncrementPageModel}
 * constructor
 * I apologize for the long name..
 * 
 * @author Dominic
 *
 */
public class StopBusinessDayIncrementPageModelConstructorInfo {

   private static final DateTime ZERO_MAX_TIME = DateTimeFactory.createNew(0);
   private TimeSnippet currentTimeSnippet;
   private String totalDurationRep;
   private String description;
   private String ticketNumber;

   private String abortButtonToolTipText;
   private String finishContinueButtonToolTipText;

   private boolean isLastIncrementAmongOthers;
   private boolean isAbortEnabled;
   private boolean isBeginTextFieldEnabled;
   private DateTime maxEndTime;
   private BusinessDayChangedCallbackHandler businessDayChangedCallbackHandler;

   /**
    * Creates a new {@link StopBusinessDayIncrementPageModelConstructorInfo} for the given {@link BusinessDayIncrement}
    * This method is used from the {@link StopBusinessDayIncrementPage}
    * 
    * @param currentTimeSnippet
    * @return a new {@link StopBusinessDayIncrementPageModelConstructorInfo}
    */
   public static StopBusinessDayIncrementPageModelConstructorInfo of(TimeSnippet currentTimeSnippet) {
      String ticketNr = TicketConst.DEFAULT_TICKET_NAME;
      return new StopBusinessDayIncrementPageModelConstructorInfo(currentTimeSnippet, ZERO_MAX_TIME, ticketNr,
            "", NumberFormat.format(currentTimeSnippet.getDuration()), true, true, true,
            BusinessDayChangedCallbackHandlerFactory.createNew(), TextLabel.CANCEL_BUTTON_TOOLTIP_TEXT, TextLabel.FINISH_BUTTON_TOOLTIP_TEXT);
   }

   /**
    * Creates a new {@link StopBusinessDayIncrementPageModelConstructorInfo} for the given {@link BusinessDayIncrement} and
    * {@link StopBusinessDayIncrementPageModel}
    * This method is used from the {@link ComeAndGoOverviewPage} if we refresh the current displayed {@link StopBusinessDayIncrementPage}.
    * In this case we have to re-use the existing values
    * 
    * @param businessDayIncrement
    *        the given {@link BusinessDayIncrement}
    * @param existingStopBDIncPageModel
    *        the existing {@link StopBusinessDayIncrementPageModel}
    * @param currentTimeSnippet
    * @return a new {@link StopBusinessDayIncrementPageModelConstructorInfo}
    */
   public static StopBusinessDayIncrementPageModelConstructorInfo of(StopBusinessDayIncrementPageModel existingStopBDIncPageModel,
         TimeSnippet currentTimeSnippet) {
      return new StopBusinessDayIncrementPageModelConstructorInfo(currentTimeSnippet, existingStopBDIncPageModel.getMaxEndTime(),
            existingStopBDIncPageModel.getTicketNoProperty().getValue(),
            existingStopBDIncPageModel.getDescriptionProperty().getValue(),
            currentTimeSnippet.getDurationRep(),
            existingStopBDIncPageModel.isLastIncrementAmongOthers(),
            !existingStopBDIncPageModel.getIsAbortButtonDisabledProperty().get(),
            existingStopBDIncPageModel.getIsBeginTextFieldEnabledProperty().get(),
            existingStopBDIncPageModel.getBusinessDayChangedCallbackHandler(),
            existingStopBDIncPageModel.getCancelButtonToolTipText().getValue().getText(),
            existingStopBDIncPageModel.getFinishButtonToolTipTextProperty().getValue().getText());
   }

   /**
    * Creates a new {@link StopBusinessDayIncrementPageModelConstructorInfo} for the given {@link ComeAndGo}
    * This method is used from the {@link ComeAndGoOverviewPage}
    * 
    * @param comeAndGoOverviewPageModel
    *        the given {@link ComeAndGoOverviewPageModel}
    * @return a new {@link StopBusinessDayIncrementPageModelConstructorInfo}
    */
   public static StopBusinessDayIncrementPageModelConstructorInfo of(ComeAndGoOverviewPageModel comeAndGoOverviewPageModel) {
      TimeSnippet timeSnippet = getTimeSnippet(comeAndGoOverviewPageModel);
      boolean isLastIncrementAmongOthers = comeAndGoOverviewPageModel.isLastIncrementAmongOthers();
      String finishContinueComeAndGoButtonTooltipText =
            isLastIncrementAmongOthers ? TextLabel.FINISH_COME_AND_GO_BUTTON_TOOLTIP_TEXT : TextLabel.CONTINUE_COME_AND_GO_BUTTON_TOOLTIP_TEXT;
      String ticketNamePlaceholder = getTicketNrPlaceHolder(comeAndGoOverviewPageModel);
      return new StopBusinessDayIncrementPageModelConstructorInfo(timeSnippet, timeSnippet.getEndTimeStamp(),
            ticketNamePlaceholder, "", timeSnippet.getDurationRep(), isLastIncrementAmongOthers, false, false,
            comeAndGoOverviewPageModel.getBusinessDayChangedCallbackHandler(), TextLabel.ABORT_COME_AND_GO_BUTTON_TOOLTIP_TEXT,
            finishContinueComeAndGoButtonTooltipText);
   }

   /*
    * The first time we start the wizard, we show the default placeholder 
    * For the 2nd and nth time, we show the ticket-nr, the user has entered the last time
    */
   private static String getTicketNrPlaceHolder(ComeAndGoOverviewPageModel comeAndGoOverviewPageModel) {
      return nonNull(comeAndGoOverviewPageModel.getTicketNrFromPrevAddedBDInc()) ? comeAndGoOverviewPageModel.getTicketNrFromPrevAddedBDInc()
            : TicketConst.DEFAULT_TICKET_NAME;
   }

   private StopBusinessDayIncrementPageModelConstructorInfo(TimeSnippet currentTimeSnippet, DateTime maxEndTime, String ticketNumber,
         String description, String totalDurationRep, boolean isLastIncrementAmongOthers, boolean isAbortEnabled, boolean isBeginTextFieldEnabled,
         BusinessDayChangedCallbackHandler businessDayChangedCallbackHandler, String abortButtonToolTipText, String finishContinueButtonToolTipText) {
      this.businessDayChangedCallbackHandler = businessDayChangedCallbackHandler;
      this.finishContinueButtonToolTipText = finishContinueButtonToolTipText;
      this.isBeginTextFieldEnabled = isBeginTextFieldEnabled;
      this.isLastIncrementAmongOthers = isLastIncrementAmongOthers;
      this.abortButtonToolTipText = abortButtonToolTipText;
      this.currentTimeSnippet = currentTimeSnippet;
      this.totalDurationRep = totalDurationRep;
      this.isAbortEnabled = isAbortEnabled;
      this.ticketNumber = ticketNumber;
      this.description = description;
      this.maxEndTime = maxEndTime;
   }

   public String getTicketNumber() {
      return ticketNumber;
   }

   public String getDescription() {
      return description;
   }

   public String getTotalDurationRep() {
      return totalDurationRep;
   }

   public TimeSnippet getTimeSnippet() {
      return currentTimeSnippet;
   }

   private static TimeSnippet getTimeSnippet(ComeAndGoOverviewPageModel comeAndGoOverviewPageModel) {
      DateTime begin = DateTimeFactory.createNew();
      DateTime end = DateTimeFactory.createNew();
      TimeSnippet comeAndGoTimeSnippet = comeAndGoOverviewPageModel.getComeAndGoTimeSnippet();
      if (nonNull(comeAndGoTimeSnippet)) {
         if (nonNull(comeAndGoTimeSnippet.getBeginTimeStamp())) {
            begin = DateTimeUtil.max(comeAndGoTimeSnippet.getBeginTimeStamp(), comeAndGoOverviewPageModel.getPrevComeAndGoEnd());
         }
         if (nonNull(comeAndGoTimeSnippet.getEndTimeStamp())) {
            end = comeAndGoTimeSnippet.getEndTimeStamp();
         }
      }
      return TimeSnippetFactory.createNew()
            .setBeginTimeStamp(begin)
            .setEndTimeStamp(end);
   }

   public boolean isAbortEnabled() {
      return isAbortEnabled;
   }

   public boolean isBeginTextFieldEnabled() {
      return isBeginTextFieldEnabled;
   }

   public DateTime getMaxEndTime() {
      return maxEndTime;
   }

   public BusinessDayChangedCallbackHandler getBusinessDayChangedCallbackHandler() {
      return businessDayChangedCallbackHandler;
   }

   public boolean isLastIncrementAmongOthers() {
      return isLastIncrementAmongOthers;
   }

   public String getAbortButtonToolTipText() {
      return abortButtonToolTipText;
   }

   public String getFinishContinueButtonToolTipText() {
      return finishContinueButtonToolTipText;
   }
}
