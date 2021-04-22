package com.adcubum.timerecording.ui.app.pages.comeandgo.model;

import java.util.LinkedList;

import com.adcubum.timerecording.core.work.businessday.BusinessDay;
import com.adcubum.timerecording.core.work.businessday.TimeSnippet;
import com.adcubum.timerecording.core.work.businessday.update.callback.BusinessDayChangedCallbackHandler;
import com.adcubum.timerecording.core.work.businessday.update.callback.impl.BusinessDayChangedCallbackHandlerImpl;
import com.adcubum.timerecording.core.work.businessday.update.callback.impl.BusinessDayIncrementAdd;
import com.adcubum.timerecording.core.work.businessday.update.callback.impl.ChangedValue;

public class ComeAndGoBDChangedCallbackHandler implements BusinessDayChangedCallbackHandler {

   private BusinessDayChangedCallbackHandler defaultBusinessDayChangedCallbackHandler;
   private LinkedList<BusinessDayIncrementAdd> businessDayIncrementAdds;

   public ComeAndGoBDChangedCallbackHandler() {
      this.defaultBusinessDayChangedCallbackHandler = new BusinessDayChangedCallbackHandlerImpl();
      this.businessDayIncrementAdds = new LinkedList<>();
   }

   @Override
   public void handleBusinessDayChanged(ChangedValue changeValue) {
      defaultBusinessDayChangedCallbackHandler.handleBusinessDayChanged(changeValue);
   }

   @Override
   public void handleBusinessDayIncrementDeleted(int index) {
      defaultBusinessDayChangedCallbackHandler.handleBusinessDayIncrementDeleted(index);
   }

   /**
    * Adds the given {@link BusinessDayIncrementAdd} but does not add it already to a {@link BusinessDay}
    */
   @Override
   public void handleBusinessDayIncrementAdd(BusinessDayIncrementAdd businessDayIncrementAdd) {
      businessDayIncrementAdds.add(businessDayIncrementAdd);
   }

   /**
    * @return the {@link TimeSnippet} from the last added {@link BusinessDayIncrementAdd} or <code>null</code> if there is none
    */
   public TimeSnippet getCurrentTimeSnippet() {
      if (!businessDayIncrementAdds.isEmpty()) {
         return businessDayIncrementAdds.getLast().getTimeSnippet();
      }
      return null;
   }

   /**
    * Finally adds all collected {@link BusinessDayIncrementAdd} to a {@link BusinessDay}
    */
   public void addAllBusinessDayIncrements() {
      for (BusinessDayIncrementAdd businessDayIncrementAdd : businessDayIncrementAdds) {
         defaultBusinessDayChangedCallbackHandler.handleBusinessDayIncrementAdd(businessDayIncrementAdd);
      }
      businessDayIncrementAdds.clear();
      flagBusinessDayComeAndGoesAsRecorded();
   }

   @Override
   public void flagBusinessDayComeAndGoesAsRecorded() {
      defaultBusinessDayChangedCallbackHandler.flagBusinessDayComeAndGoesAsRecorded();
   }
}
