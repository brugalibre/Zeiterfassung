package com.adcubum.timerecording.app.businessday;

import static java.util.Objects.requireNonNull;

import com.adcubum.timerecording.core.work.businessday.BusinessDay;
import com.adcubum.timerecording.core.work.businessday.repository.BusinessDayRepository;
import com.adcubum.timerecording.work.date.DateTime;
import com.adcubum.timerecording.work.date.DateTimeFactory;
import com.adcubum.timerecording.work.date.DateTimeUtil;

public class BookedBusinessDayDeleterImpl implements BookedBusinessDayDeleter {

   private DateTime lowerBounds;
   private DateTime thresholdTime;
   private BusinessDayRepository businessDayRepository;

   private BookedBusinessDayDeleterImpl(DateTime lowerBounds, DateTime thresholdTime, BusinessDayRepository businessDayRepository) {
      this.lowerBounds = requireNonNull(lowerBounds);
      this.thresholdTime = requireNonNull(thresholdTime);
      this.businessDayRepository = requireNonNull(businessDayRepository);
   }

   @Override
   public void cleanUpBookedBusinessDays() {
      businessDayRepository.deleteBookedBusinessDaysWithinRange(lowerBounds, thresholdTime);
   }

   /**
    * Creates as default implementation which deletes all booked {@link BusinessDay}s which where booked
    * before the first of the current month (exclusively the first)
    */
   public static BookedBusinessDayDeleterImpl of(BusinessDayRepository businessDayRepository) {
      DateTime lastOfPrevMonth = DateTimeUtil.getLastOfPrevMonth();
      DateTime zeroTime = DateTimeFactory.createNew(0);
      return new BookedBusinessDayDeleterImpl(zeroTime, lastOfPrevMonth, businessDayRepository);
   }
}
