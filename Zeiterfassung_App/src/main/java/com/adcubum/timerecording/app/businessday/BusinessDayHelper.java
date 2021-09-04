package com.adcubum.timerecording.app.businessday;

import java.util.List;

import com.adcubum.timerecording.app.TimeRecorderImpl;
import com.adcubum.timerecording.core.work.businessday.BusinessDay;
import com.adcubum.timerecording.core.work.businessday.BusinessDayIncrement;
import com.adcubum.timerecording.core.work.businessday.history.resolver.BusinessDayHistoryOverviewResolver;

/**
 * The {@link BusinessDayHelper} acts as a helper for the {@link TimeRecorderImpl} in order to retrieve
 * and store a {@link BusinessDay}
 * 
 * @author dstalder
 *
 */
public interface BusinessDayHelper extends BusinessDayHistoryOverviewResolver {

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

   /**
    * Adds the given {@link BusinessDayIncrement} and adds the booked one to the booked {@link BusinessDay}
    * of this {@link BusinessDayHelper}
    * 
    * @param increments
    *        the {@link BusinessDayIncrement} to check and add the booked ones
    */
   void addBookedBusinessDayIncrements(List<BusinessDayIncrement> increments);

   /**
    * Delete all {@link BusinessDay} which are either booked or not booked, depending on the given parameter
    * 
    * @param isBooked
    *        <code>true</code> if only booked ones should be deleted or <code>false</code> if not-booked
    */
   void deleteAll(boolean isBooked);
}
