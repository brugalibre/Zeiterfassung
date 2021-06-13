/**
 * 
 */
package com.adcubum.timerecording.ui.app.pages.stopbusinessday.model;

import static java.util.Objects.nonNull;

import com.adcubum.librarys.text.res.TextLabel;
import com.adcubum.timerecording.core.work.businessday.TimeSnippet;
import com.adcubum.timerecording.core.work.businessday.comeandgo.ComeAndGo;
import com.adcubum.timerecording.core.work.businessday.update.callback.BusinessDayChangedCallbackHandler;
import com.adcubum.timerecording.core.work.businessday.update.callback.impl.BusinessDayChangedCallbackHandlerImpl;
import com.adcubum.timerecording.core.work.businessday.vo.BusinessDayIncrementVO;
import com.adcubum.timerecording.jira.constants.TicketConst;
import com.adcubum.timerecording.ui.app.pages.comeandgo.model.ComeAndGoOverviewPageModel;
import com.adcubum.timerecording.ui.app.pages.comeandgo.view.ComeAndGoOverviewPage;
import com.adcubum.timerecording.ui.app.pages.stopbusinessday.view.StopBusinessDayIncrementPage;
import com.adcubum.timerecording.work.date.Time;
import com.adcubum.timerecording.work.date.TimeFactory;
import com.adcubum.timerecording.work.date.TimeUtil;

/**
 * The {@link StopBusinessDayIncrementPageModelConstructorInfo} acts as a container for the {@link StopBusinessDayIncrementPageModel}
 * constructor
 * I apologize for the long name..
 * 
 * @author Dominic
 *
 */
public class StopBusinessDayIncrementPageModelConstructorInfo {

   private static final Time ZERO_MAX_TIME = TimeFactory.createNew(0);
   private TimeSnippet currentTimeSnippet;
   private String totalDurationRep;
   private String description;
   private String ticketNumber;

   private String abortButtonToolTipText;
   private String finishContinueButtonToolTipText;

   private boolean isLastIncrementAmongOthers;
   private boolean isAbortEnabled;
   private boolean isBeginTextFieldEnabled;
   private Time maxEndTime;
   private BusinessDayChangedCallbackHandler businessDayChangedCallbackHandler;

   /**
    * Creates a new {@link StopBusinessDayIncrementPageModelConstructorInfo} for the given {@link BusinessDayIncrementVO}
    * This method is used from the {@link StopBusinessDayIncrementPage}
    * 
    * @param businessDayIncrementVO
    *        the given {@link BusinessDayIncrementVO}
    * @param currentTimeSnippet
    * @return a new {@link StopBusinessDayIncrementPageModelConstructorInfo}
    */
   public static StopBusinessDayIncrementPageModelConstructorInfo of(BusinessDayIncrementVO businessDayIncrementVO, TimeSnippet currentTimeSnippet) {
      return new StopBusinessDayIncrementPageModelConstructorInfo(currentTimeSnippet, ZERO_MAX_TIME, businessDayIncrementVO.getTicketNumber(),
            businessDayIncrementVO.getDescription(), businessDayIncrementVO.getTotalDurationRep(), true, true, true,
            new BusinessDayChangedCallbackHandlerImpl(), TextLabel.CANCEL_BUTTON_TOOLTIP_TEXT, TextLabel.FINISH_BUTTON_TOOLTIP_TEXT);
   }

   /**
    * Creates a new {@link StopBusinessDayIncrementPageModelConstructorInfo} for the given {@link BusinessDayIncrementVO} and
    * {@link StopBusinessDayIncrementPageModel}
    * This method is used from the {@link ComeAndGoOverviewPage} if we refresh the current displayed {@link StopBusinessDayIncrementPage}.
    * In this case we have to re-use the existing values
    * 
    * @param businessDayIncrementVO
    *        the given {@link BusinessDayIncrementVO}
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
      boolean isLastIncrementAmongOthers =
            comeAndGoOverviewPageModel.getAmountOfComeAndGoes() == comeAndGoOverviewPageModel.getCurrentComeAndGoIndex();
      String finishContinueComeAndGoButtonTooltipText =
            isLastIncrementAmongOthers ? TextLabel.FINISH_COME_AND_GO_BUTTON_TOOLTIP_TEXT : TextLabel.CONTINUE_COME_AND_GO_BUTTON_TOOLTIP_TEXT;
      return new StopBusinessDayIncrementPageModelConstructorInfo(timeSnippet, timeSnippet.getEndTimeStamp(),
            TicketConst.DEFAULT_TICKET_NAME, "", timeSnippet.getDurationRep(), isLastIncrementAmongOthers, false, false,
            comeAndGoOverviewPageModel.getBusinessDayChangedCallbackHandler(), TextLabel.ABORT_COME_AND_GO_BUTTON_TOOLTIP_TEXT,
            finishContinueComeAndGoButtonTooltipText);
   }


   private StopBusinessDayIncrementPageModelConstructorInfo(TimeSnippet currentTimeSnippet, Time maxEndTime, String ticketNumber, String description,
         String totalDurationRep, boolean isLastIncrementAmongOthers, boolean isAbortEnabled, boolean isBeginTextFieldEnabled,
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
      Time begin =TimeFactory.createNew();
      Time end =TimeFactory.createNew();
      TimeSnippet comeAndGoTimeSnippet = comeAndGoOverviewPageModel.getComeAndGoTimeSnippet();
      if (nonNull(comeAndGoTimeSnippet)) {
         if (nonNull(comeAndGoTimeSnippet.getBeginTimeStamp())) {
            begin = TimeUtil.max(comeAndGoTimeSnippet.getBeginTimeStamp(), comeAndGoOverviewPageModel.getPrevComeAndGoEnd());
         }
         if (nonNull(comeAndGoTimeSnippet.getEndTimeStamp())) {
            end = comeAndGoTimeSnippet.getEndTimeStamp();
         }
      }
      TimeSnippet timeSnippet = new TimeSnippet(begin);
      timeSnippet.setBeginTimeStamp(begin);
      timeSnippet.setEndTimeStamp(end);
      return timeSnippet;
   }

   public boolean isAbortEnabled() {
      return isAbortEnabled;
   }

   public boolean isBeginTextFieldEnabled() {
      return isBeginTextFieldEnabled;
   }

   public Time getMaxEndTime() {
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
