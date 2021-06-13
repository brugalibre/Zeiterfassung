package com.adcubum.scheduler.helper;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.jupiter.api.Test;

import com.adcubum.timerecording.work.date.Time;
import com.adcubum.timerecording.work.date.TimeFactory;
import com.adcubum.timerecording.work.date.TimeUtil;

class SchedulerTimeHelperTest {

   @Test
   void testGetNextDayOfWeekFromFridayIsMonday() {

      // Given
      GregorianCalendar startDate = new GregorianCalendar(2020, 11, 4, 1, 0, 0);
      Time startTime = TimeFactory.createNew(startDate.getTimeInMillis());
      Time nextDay = SchedulerTimeHelper.getNextDayOfWeek(startTime);

      int dayOfWeekOfStartTime = TimeUtil.getCalenderOfTheWeek(startTime);
      assertThat(dayOfWeekOfStartTime, is(Calendar.FRIDAY));

      // When
      int dayOfWeek = TimeUtil.getCalenderOfTheWeek(nextDay);

      // Then
      assertThat(dayOfWeek, is(Calendar.MONDAY));
   }
}
