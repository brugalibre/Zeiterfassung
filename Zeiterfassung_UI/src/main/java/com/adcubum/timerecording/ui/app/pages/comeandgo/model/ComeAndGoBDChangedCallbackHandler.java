package com.adcubum.timerecording.ui.app.pages.comeandgo.model;

import java.util.LinkedList;

import com.adcubum.timerecording.core.work.businessday.BusinessDay;
import com.adcubum.timerecording.core.work.businessday.TimeSnippet;
import com.adcubum.timerecording.core.work.businessday.comeandgo.ComeAndGo;
import com.adcubum.timerecording.core.work.businessday.comeandgo.ComeAndGoes;
import com.adcubum.timerecording.core.work.businessday.comeandgo.change.ChangedComeAndGoValue;
import com.adcubum.timerecording.core.work.businessday.update.callback.BusinessDayChangedCallbackHandler;
import com.adcubum.timerecording.core.work.businessday.update.callback.impl.BusinessDayChangedCallbackHandlerFactory;
import com.adcubum.timerecording.core.work.businessday.update.callback.impl.BusinessDayIncrementAdd;
import com.adcubum.timerecording.core.work.businessday.update.callback.impl.ChangedValue;

public class ComeAndGoBDChangedCallbackHandler implements BusinessDayChangedCallbackHandler {

   private BusinessDayChangedCallbackHandler defaultBusinessDayChangedCallbackHandler;
   private LinkedList<BusinessDayIncrementAdd> businessDayIncrementAdds;

   public ComeAndGoBDChangedCallbackHandler() {
      this.defaultBusinessDayChangedCallbackHandler = BusinessDayChangedCallbackHandlerFactory.createNew();
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
    * @return the ticket nr which was entered for the previous {@link ComeAndGo}
    */
   public String getTicketNrFromPrevAddedBDInc() {
      if (!businessDayIncrementAdds.isEmpty()) {
         BusinessDayIncrementAdd lastBusinessDayIncrementAdd = businessDayIncrementAdds.getLast();
         return lastBusinessDayIncrementAdd.getTicket().getNr();
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

   @Override
   public ComeAndGoes changeComeAndGo(ChangedComeAndGoValue changedComeAndGoValue) {
      return defaultBusinessDayChangedCallbackHandler.changeComeAndGo(changedComeAndGoValue);
   }

   @Override
   public void clear() {
      defaultBusinessDayChangedCallbackHandler.clear();
   }

   @Override
   public void clearComeAndGoes() {
      defaultBusinessDayChangedCallbackHandler.clearComeAndGoes();
   }
}
