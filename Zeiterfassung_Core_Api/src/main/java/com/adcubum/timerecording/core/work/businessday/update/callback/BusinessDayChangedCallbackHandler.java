package com.adcubum.timerecording.core.work.businessday.update.callback;

import com.adcubum.timerecording.core.work.businessday.BusinessDay;
import com.adcubum.timerecording.core.work.businessday.BusinessDayIncrement;
import com.adcubum.timerecording.core.work.businessday.comeandgo.ComeAndGo;
import com.adcubum.timerecording.core.work.businessday.comeandgo.change.ComeAndGoesUpdater;
import com.adcubum.timerecording.core.work.businessday.update.callback.impl.BusinessDayIncrementAdd;
import com.adcubum.timerecording.core.work.businessday.update.callback.impl.ChangedValue;

public interface BusinessDayChangedCallbackHandler extends ComeAndGoesUpdater {

   /**
    * Informs any listener that the {@link BusinessDayIncrement} for the given
    * order no. has changed
    * 
    * @param changeValue
    *        the changed Value
    */
   public void handleBusinessDayChanged(ChangedValue changeValue);

   /**
    * Removes the {@link BusinessDayIncrement} at the given index
    * 
    * @param index
    *        the given index
    */
   public void handleBusinessDayIncrementDeleted(int index);

   /**
    * Adds the given {@link BusinessDayIncrement} to the current
    * {@link BusinessDay}
    * 
    * @param businessDayIncrementAdd
    *        the {@link BusinessDayIncrement} to add
    */
   public void handleBusinessDayIncrementAdd(BusinessDayIncrementAdd businessDayIncrementAdd);

   /**
    * Flags all {@link ComeAndGo}es of the {@link BusinessDay} as recorded
    */
   public void flagBusinessDayComeAndGoesAsRecorded();

   /**
    * removes all recorded {@link BusinessDayIncrement}
    */
   void clear();
}
