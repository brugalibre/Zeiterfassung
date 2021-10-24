package com.adcubum.scheduler.helper;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.jupiter.api.Test;

import com.adcubum.timerecording.work.date.DateTime;
import com.adcubum.timerecording.work.date.DateTimeFactory;
import com.adcubum.timerecording.work.date.DateTimeUtil;

class SchedulerTimeHelperTest {

   @Test
   void testGetNextDayOfWeekFromFridayIsMonday() {

      // Given
      GregorianCalendar startDate = new GregorianCalendar(2020, 11, 4, 1, 0, 0);
      DateTime startTime = DateTimeFactory.createNew(startDate.getTimeInMillis());
      DateTime nextDay = SchedulerTimeHelper.getNextDayOfWeek(startTime);

      int dayOfWeekOfStartTime = DateTimeUtil.getCalenderOfTheWeek(startTime);
      assertThat(dayOfWeekOfStartTime, is(Calendar.FRIDAY));

      // When
      int dayOfWeek = DateTimeUtil.getCalenderOfTheWeek(nextDay);

      // Then
      assertThat(dayOfWeek, is(Calendar.MONDAY));
   }
}
