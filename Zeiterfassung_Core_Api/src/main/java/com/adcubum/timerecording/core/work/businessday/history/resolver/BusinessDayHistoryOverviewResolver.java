package com.adcubum.timerecording.core.work.businessday.history.resolver;

import com.adcubum.timerecording.core.work.businessday.BusinessDay;
import com.adcubum.timerecording.core.work.businessday.BusinessDayIncrement;
import com.adcubum.timerecording.core.work.businessday.TimeSnippet;
import com.adcubum.timerecording.core.work.businessday.history.BusinessDayHistoryOverview;
import com.adcubum.timerecording.work.date.DateTime;

/**
 * The {@link BusinessDayHistoryOverviewResolver} serves as an entry point in order to retrieve an {@link BusinessDayHistoryOverview}
 * 
 * @author dstalder
 *
 */
public interface BusinessDayHistoryOverviewResolver {

   /**
    * Resolves all booked {@link BusinessDay}s within the given range
    * Only {@link BusinessDay}s with finished {@link BusinessDayIncrement}s, whose {@link TimeSnippet}
    * are within the given time range, are considered
    * 
    * @param lowerBounds
    *        the lower time bound
    * @param upperBounds
    *        the upper time bound
    * @return all booked {@link BusinessDay}s within the given range
    */
   BusinessDayHistoryOverview getBusinessDayHistoryOverview(DateTime lowerBounds, DateTime upperBounds);

}
