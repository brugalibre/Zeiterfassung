/**
 * 
 */
package com.adcubum.timerecording.ui.app.pages.overview.model;

import com.adcubum.librarys.text.res.TextLabel;
import com.adcubum.timerecording.app.TimeRecorder;
import com.adcubum.timerecording.core.work.businessday.vo.BusinessDayVO;
import com.adcubum.timerecording.security.login.auth.AuthenticationService;
import com.adcubum.timerecording.ui.core.model.PageModel;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * @author Dominic
 *
 */
public class OverviewPageModel implements PageModel {

   private BusinessDayVO businessDayVO;
   private BooleanProperty isChargeButtonDisabled;
   private BooleanProperty isClearButtonDisabled;
   private BooleanProperty isExportButtonDisabled;
   private BooleanProperty hasBusinessDayDescriptionProperty;

   private StringProperty bookButtonLabel;
   private StringProperty exportButtonLabel;
   private StringProperty clearButtonLabel;

   private StringProperty totalAmountOfTimeLabel;
   private StringProperty totalAmountOfTimeValue;

   /**
    * Creates a new {@link OverviewPageModel}
    */
   public OverviewPageModel(BusinessDayVO businessDayVO) {
      this.businessDayVO = businessDayVO;
      isChargeButtonDisabled = new SimpleBooleanProperty(isBookButtonDisabled(businessDayVO));
      isClearButtonDisabled = new SimpleBooleanProperty(!businessDayVO.hasNotChargedElements());
      isExportButtonDisabled = new SimpleBooleanProperty(!businessDayVO.hasNotChargedElements());
      hasBusinessDayDescriptionProperty = new SimpleBooleanProperty(businessDayVO.hasIncrementWithDescription());

      bookButtonLabel = new SimpleStringProperty(TextLabel.CHARGE_LABEL);
      exportButtonLabel = new SimpleStringProperty(TextLabel.EXPORT_LABEL);
      clearButtonLabel = new SimpleStringProperty(TextLabel.CLEAR_LABEL);

      totalAmountOfTimeLabel = new SimpleStringProperty(TextLabel.TOTAL_AMOUNT_OF_HOURS_LABEL);
      totalAmountOfTimeValue = new SimpleStringProperty(businessDayVO.getTotalDurationRep());
   }

   /**
    * @param inPageModel
    * @param of
    * @return
    */
   public static OverviewPageModel of(OverviewPageModel inPageModel, BusinessDayVO businessDayVO) {
      inPageModel.businessDayVO = businessDayVO;
      inPageModel.getIsChargeButtonDisabled().setValue(isBookButtonDisabled(businessDayVO));
      inPageModel.getIsClearButtonDisabled().setValue(businessDayVO.getBusinessDayIncrements().isEmpty());
      inPageModel.getIsExportButtonDisabled().setValue(businessDayVO.getBusinessDayIncrements().isEmpty());
      inPageModel.getHasBusinessDayDescription().setValue(businessDayVO.hasIncrementWithDescription());

      inPageModel.getBookButtonLabel().set(TextLabel.CHARGE_LABEL);
      inPageModel.getExportButtonLabel().set(TextLabel.EXPORT_LABEL);
      inPageModel.getClearButtonLabel().set(TextLabel.CLEAR_LABEL);

      inPageModel.getTotalAmountOfTimeLabel().set(TextLabel.TOTAL_AMOUNT_OF_HOURS_LABEL);

      String totalDurationRep = businessDayVO.getTotalDurationRep();
      inPageModel.getTotalAmountOfTimeValue().set(totalDurationRep);
      return inPageModel;
   }

   private static boolean isBookButtonDisabled(BusinessDayVO businessDayVO) {
      boolean isUserNotAuthenticated = !AuthenticationService.INSTANCE.isUserAuthenticated();
      return TimeRecorder.INSTANCE.isBooking() || !businessDayVO.hasNotChargedElements() || isUserNotAuthenticated;
   }

   /**
    * @returns the {@link BusinessDayVO}
    */
   public final BusinessDayVO getBusinessDayVO() {
      return this.businessDayVO;
   }

   /**
    * @return
    */
   public BooleanProperty getIsChargeButtonDisabled() {
      return isChargeButtonDisabled;
   }

   public BooleanProperty getIsClearButtonDisabled() {
      return isClearButtonDisabled;
   }

   public final StringProperty getBookButtonLabel() {
      return this.bookButtonLabel;
   }

   public final StringProperty getExportButtonLabel() {
      return this.exportButtonLabel;
   }

   public final StringProperty getClearButtonLabel() {
      return this.clearButtonLabel;
   }

   public final StringProperty getTotalAmountOfTimeLabel() {
      return this.totalAmountOfTimeLabel;
   }

   public final StringProperty getTotalAmountOfTimeValue() {
      return this.totalAmountOfTimeValue;
   }

   public final BooleanProperty getIsExportButtonDisabled() {
      return this.isExportButtonDisabled;
   }

   public BooleanProperty getHasBusinessDayDescription() {
      return hasBusinessDayDescriptionProperty;
   }
}
