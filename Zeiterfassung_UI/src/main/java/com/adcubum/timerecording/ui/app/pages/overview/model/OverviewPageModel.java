/**
 * 
 */
package com.adcubum.timerecording.ui.app.pages.overview.model;

import com.adcubum.librarys.text.res.TextLabel;
import com.adcubum.timerecording.app.TimeRecorder;
import com.adcubum.timerecording.core.work.businessday.BusinessDay;
import com.adcubum.timerecording.core.work.businessday.util.BusinessDayUtil;
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

   private BusinessDay businessDay;
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
   public OverviewPageModel(BusinessDay businessDay) {
      this.businessDay = businessDay;
      isChargeButtonDisabled = new SimpleBooleanProperty(isBookButtonDisabled(businessDay));
      isClearButtonDisabled = new SimpleBooleanProperty(!businessDay.hasNotChargedElements());
      isExportButtonDisabled = new SimpleBooleanProperty(!businessDay.hasNotChargedElements());
      hasBusinessDayDescriptionProperty = new SimpleBooleanProperty(businessDay.hasDescription());

      bookButtonLabel = new SimpleStringProperty(TextLabel.CHARGE_LABEL);
      exportButtonLabel = new SimpleStringProperty(TextLabel.EXPORT_LABEL);
      clearButtonLabel = new SimpleStringProperty(TextLabel.CLEAR_LABEL);

      totalAmountOfTimeLabel = new SimpleStringProperty(TextLabel.TOTAL_AMOUNT_OF_HOURS_LABEL);
      totalAmountOfTimeValue = new SimpleStringProperty(BusinessDayUtil.getTotalDurationRep(businessDay));
   }

   /**
    * @param inPageModel
    * @param of
    * @return
    */
   public static OverviewPageModel of(OverviewPageModel inPageModel, BusinessDay businessDay) {
      inPageModel.businessDay = businessDay;
      inPageModel.getIsChargeButtonDisabled().setValue(isBookButtonDisabled(businessDay));
      inPageModel.getIsClearButtonDisabled().setValue(businessDay.getIncrements().isEmpty());
      inPageModel.getIsExportButtonDisabled().setValue(businessDay.getIncrements().isEmpty());
      inPageModel.getHasBusinessDayDescription().setValue(businessDay.hasDescription());

      inPageModel.getBookButtonLabel().set(TextLabel.CHARGE_LABEL);
      inPageModel.getExportButtonLabel().set(TextLabel.EXPORT_LABEL);
      inPageModel.getClearButtonLabel().set(TextLabel.CLEAR_LABEL);

      inPageModel.getTotalAmountOfTimeLabel().set(TextLabel.TOTAL_AMOUNT_OF_HOURS_LABEL);

      String totalDurationRep = BusinessDayUtil.getTotalDurationRep(businessDay);
      inPageModel.getTotalAmountOfTimeValue().set(totalDurationRep);
      return inPageModel;
   }

   private static boolean isBookButtonDisabled(BusinessDay businessDay) {
      boolean isUserNotAuthenticated = !AuthenticationService.INSTANCE.isUserAuthenticated();
      return TimeRecorder.INSTANCE.isBooking() || !businessDay.hasNotChargedElements() || isUserNotAuthenticated;
   }

   /**
    * @returns the {@link BusinessDay}
    */
   public final BusinessDay getBusinessDay() {
      return this.businessDay;
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
