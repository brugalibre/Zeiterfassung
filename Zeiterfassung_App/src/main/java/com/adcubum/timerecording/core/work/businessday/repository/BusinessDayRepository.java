package com.adcubum.timerecording.core.work.businessday.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import com.adcubum.timerecording.core.businessday.common.repository.CommonBusinessDayRepository;
import com.adcubum.timerecording.core.work.businessday.BusinessDay;
import com.adcubum.timerecording.core.work.businessday.BusinessDayIncrement;
import com.adcubum.timerecording.core.work.businessday.TimeSnippet;
import com.adcubum.timerecording.work.date.DateTime;

public interface BusinessDayRepository extends CommonBusinessDayRepository<BusinessDay, UUID> {

   /**
    * Finds a booked {@link BusinessDay} for the given Date. Note that any {@link BusinessDay} will be returned,
    * whose date of it's {@link BusinessDayIncrement}s has the same year, month and day. Hours or seconds are not taken into consideration
    * Note that a {@link BusinessDay} without any {@link BusinessDayIncrement} will not be found
    * 
    * @param date
    *        the date to check
    * @return the firsst found {@link BusinessDay} for the given Date.
    */
   @Nullable
   BusinessDay findBookedBusinessDayByDate(@NonNull DateTime time);

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
   @NonNull
   List<BusinessDay> findBookedBusinessDaysWithinRange(DateTime lowerBounds, DateTime upperBounds);
}
