package com.adcubum.timerecording.core.work.businessday.history;

import java.util.List;

import com.adcubum.timerecording.core.work.businessday.BusinessDay;

/**
 * The {@link BusinessDayHistoryOverview} serves as an overview for booked {@link BusinessDay}s over a specific amount of time
 * 
 * @author dstalder
 *
 */
public interface BusinessDayHistoryOverview {

   /**
    * @return all {@link BusinessDayHistory} objects of this overview
    */
   List<BusinessDayHistory> getBusinessDayHistoryEntries();

}
