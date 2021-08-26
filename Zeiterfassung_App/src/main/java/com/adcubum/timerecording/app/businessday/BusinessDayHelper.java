package com.adcubum.timerecording.app.businessday;

import com.adcubum.timerecording.app.TimeRecorderImpl;
import com.adcubum.timerecording.core.work.businessday.BusinessDay;

/**
 * The {@link BusinessDayHelper} acts as a helper for the {@link TimeRecorderImpl} in order to retrieve
 * and store a {@link BusinessDay}
 * 
 * @author dstalder
 *
 */
public interface BusinessDayHelper {

   /**
    * Returns the current {@link BusinessDay} of this helper or <code>null</code> if there is none
    * 
    * @return the {@link BusinessDay}
    */
   BusinessDay getBusinessDay();

   /**
    * Saves the given {@link BusinessDay}. This replaces all occurence of any other {@link BusinessDay}
    * 
    * @param newBusinessDay
    *        the new {@link BusinessDay}
    * @return a new instance of the saved {@link BusinessDay}
    */
   BusinessDay save(BusinessDay newBusinessDay);

   /**
    * Creates a new {@link BusinessDay} in the underlying persistence
    */
   void createNew();

   /**
    * Initializes this {@link BusinessDayHelper} and it's internal {@link BusinessDay}
    * This will create a new {@link BusinessDay} if there is no existing
    * 
    * @return the already existing or new created {@link BusinessDay}
    */
   BusinessDay loadExistingOrCreateNew();
}
