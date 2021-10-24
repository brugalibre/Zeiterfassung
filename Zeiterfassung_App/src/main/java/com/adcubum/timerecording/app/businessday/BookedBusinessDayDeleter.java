package com.adcubum.timerecording.app.businessday;

import com.adcubum.timerecording.core.work.businessday.BusinessDay;
import com.adcubum.timerecording.core.work.businessday.BusinessDayIncrement;
import com.adcubum.timerecording.core.work.businessday.TimeSnippet;
import com.adcubum.timerecording.core.work.businessday.comeandgo.ComeAndGoes;
import com.adcubum.timerecording.work.date.DateTime;

/**
 * The {@link BookedBusinessDayDeleter} deletes booked {@link BusinessDay}s including their {@link BusinessDayIncrement},
 * {@link TimeSnippet}s and {@link ComeAndGoes} which are booked previous a date
 * 
 * @author dstalder
 *
 */
public interface BookedBusinessDayDeleter {

   /**
    * Removes all booked {@link BusinessDay}s which are older than a specific {@link DateTime}
    */
   void cleanUpBookedBusinessDays();
}
