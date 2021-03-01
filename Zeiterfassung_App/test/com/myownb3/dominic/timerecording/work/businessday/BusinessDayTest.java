package com.myownb3.dominic.timerecording.work.businessday;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Date;

import org.junit.Test;

import com.myownb3.dominic.timerecording.core.work.businessday.BusinessDay;
import com.myownb3.dominic.timerecording.core.work.businessday.BusinessDayIncrement;
import com.myownb3.dominic.timerecording.core.work.businessday.TimeSnippet;
import com.myownb3.dominic.timerecording.core.work.businessday.ValueTypes;
import com.myownb3.dominic.timerecording.core.work.businessday.update.callback.impl.BusinessDayIncrementAdd;
import com.myownb3.dominic.timerecording.core.work.businessday.update.callback.impl.BusinessDayIncrementAdd.BusinessDayIncrementAddBuilder;
import com.myownb3.dominic.timerecording.core.work.businessday.update.callback.impl.ChangedValue;
import com.myownb3.dominic.timerecording.core.work.date.Time;

public class BusinessDayTest {

   @Test
   public void testChangeDBIncDurationIncreaseDuration() {

      // Given
      String newTotalBDDuration = "2";
      long firstTimeStampStart = System.currentTimeMillis();
      int firstTimeBetweenStartAndStop = 3600 * 1000;
      TimeSnippet firstSnippet = createTimeSnippet(firstTimeStampStart, firstTimeStampStart + firstTimeBetweenStartAndStop);
      BusinessDayIncrementAdd firstInc = createUpdate(firstSnippet, 113, "SYRIUS-1324");

      BusinessDay businessDay = new BusinessDay();
      businessDay.addBusinessIncrement(firstInc);

      ChangedValue changeValue = ChangedValue.of(0, newTotalBDDuration, ValueTypes.AMOUNT_OF_TIME);

      // When
      businessDay.changeBusinesDayIncrement(changeValue);

      // Then
      BusinessDayIncrement businessDayIncrement = businessDay.getIncrements().get(0);
      TimeSnippet lastTimeSnippet = businessDayIncrement.getCurrentTimeSnippet();
      assertThat(businessDayIncrement.getTotalDuration(), is(Float.parseFloat(newTotalBDDuration)));
      assertThat(lastTimeSnippet.getDuration(), is(2.0f));
   }

   @Test
   public void testChangeDBIncDurationSnippets_DecreaseDuration() {

      // Given
      String newTotalBDDuration = "1.4";
      long firstTimeStampStart = System.currentTimeMillis();
      int firstTimeBetweenStartAndStop = 3600 * 1000;
      TimeSnippet firstSnippet = createTimeSnippet(firstTimeStampStart, firstTimeStampStart + firstTimeBetweenStartAndStop);
      BusinessDayIncrementAdd firstInc = createUpdate(firstSnippet, 113, "SYRIUS-1324");

      BusinessDay businessDay = new BusinessDay();
      businessDay.addBusinessIncrement(firstInc);

      ChangedValue changeValue = ChangedValue.of(0, newTotalBDDuration, ValueTypes.AMOUNT_OF_TIME);

      // When
      businessDay.changeBusinesDayIncrement(changeValue);

      // Then
      BusinessDayIncrement businessDayIncrement = businessDay.getIncrements().get(0);
      TimeSnippet lastTimeSnippet = businessDayIncrement.getCurrentTimeSnippet();
      assertThat(businessDayIncrement.getTotalDuration(), is(Float.parseFloat(newTotalBDDuration)));
      assertThat(lastTimeSnippet.getDuration(), is(1.4f));
   }

   @Test
   public void testChangeDBIncDurationWith2TimeSnippets_DecreaseNotPossibleSinceItsToMuch() {

      // Given
      // This is not possible, since we would have to drop BusinessDayIncrements and time-stamps in order to achive this duration
      String newTotalBDDuration = "-1.0";
      int firstTimeBetweenStartAndStop = 3600 * 1000 * 2;
      long firstTimeStampStart = System.currentTimeMillis() + firstTimeBetweenStartAndStop;
      TimeSnippet secondSnippet = createTimeSnippet(firstTimeStampStart, firstTimeStampStart + firstTimeBetweenStartAndStop);
      BusinessDayIncrementAdd secondInc = createUpdate(secondSnippet, 113, "SYRIUS-1324");

      BusinessDay businessDay = new BusinessDay();
      businessDay.addBusinessIncrement(secondInc);

      ChangedValue changeValue = ChangedValue.of(0, newTotalBDDuration, ValueTypes.AMOUNT_OF_TIME);

      // When
      businessDay.changeBusinesDayIncrement(changeValue);

      // Then - everything remains as it is
      BusinessDayIncrement businessDayIncrement = businessDay.getIncrements().get(0);
      TimeSnippet lastTimeSnippet = businessDayIncrement.getCurrentTimeSnippet();
      assertThat(businessDayIncrement.getTotalDuration(), is(2.0f));
      assertThat(lastTimeSnippet.getDuration(), is(2.0f));
   }

   @Test
   public void testHasNotChargedElements() {

      // Given
      boolean expectedHasNotChargedElements = true;
      BusinessDay businessDay = new BusinessDay();

      TimeSnippet timeSnippet = createTimeSnippet(0);
      BusinessDayIncrementAdd updateWithTimeSnippet = createUpdate(timeSnippet, 113, "SYRIUS-1324");

      TimeSnippet timeSnippetYesterday = createTimeSnippet(0);
      BusinessDayIncrementAdd updateWithTimeSnippetTomorrow = createUpdate(timeSnippetYesterday, 114, "SYRIUS-1324");

      businessDay.addBusinessIncrement(updateWithTimeSnippet);
      businessDay.flagBusinessDayAsCharged();
      businessDay.addBusinessIncrement(updateWithTimeSnippetTomorrow);

      // When
      boolean actualHasNotChargedElements = businessDay.hasNotChargedElements();

      // Then
      assertThat(actualHasNotChargedElements, is(expectedHasNotChargedElements));
   }

   @Test
   public void testHasNoNotChargedElements() {

      // Given
      boolean expectedHasNotChargedElements = false;
      BusinessDay businessDay = new BusinessDay();

      TimeSnippet timeSnippet = createTimeSnippet(0);
      BusinessDayIncrementAdd updateWithTimeSnippet = createUpdate(timeSnippet, 113, "SYRIUS-1324");

      TimeSnippet timeSnippetYesterday = createTimeSnippet(0);
      BusinessDayIncrementAdd updateWithTimeSnippetTomorrow = createUpdate(timeSnippetYesterday, 114, "SYRIUS-1324");

      businessDay.addBusinessIncrement(updateWithTimeSnippet);
      businessDay.addBusinessIncrement(updateWithTimeSnippetTomorrow);
      businessDay.flagBusinessDayAsCharged();

      // When
      boolean actualHasNotChargedElements = businessDay.hasNotChargedElements();

      // Then
      assertThat(actualHasNotChargedElements, is(expectedHasNotChargedElements));
   }

   @Test
   public void testHasElementsFromPrecedentDays_PrecedentElements() {

      // Given
      boolean expectedHasElementsFromPrecedentDays = true;
      BusinessDay businessDay = new BusinessDay();

      TimeSnippet timeSnippet = createTimeSnippet(0);
      BusinessDayIncrementAdd updateWithTimeSnippet = createUpdate(timeSnippet, 113, "SYRIUS-1324");

      TimeSnippet timeSnippetYesterday = createTimeSnippet(-24 * 60 * 3600 * 1000);// One day in Miliseconds
      BusinessDayIncrementAdd updateWithTimeSnippetTomorrow = createUpdate(timeSnippetYesterday, 114, "SYRIUS-1324");

      businessDay.addBusinessIncrement(updateWithTimeSnippet);
      businessDay.addBusinessIncrement(updateWithTimeSnippetTomorrow);

      // When
      boolean actualHasElementsFromPrecedentDays = businessDay.hasElementsFromPrecedentDays();

      // Then
      assertThat(actualHasElementsFromPrecedentDays, is(expectedHasElementsFromPrecedentDays));
   }

   @Test
   public void testHasElementsFromPrecedentDays_NoPrecedentElements() {

      // Given
      boolean expectedHasElementsFromPrecedentDays = false;
      BusinessDay businessDay = new BusinessDay();

      // When
      boolean actualHasElementsFromPrecedentDays = businessDay.hasElementsFromPrecedentDays();

      // Then
      assertThat(actualHasElementsFromPrecedentDays, is(expectedHasElementsFromPrecedentDays));
   }

   private BusinessDayIncrementAdd createUpdate(TimeSnippet timeSnippet, int kindOfService, String ticketNo) {
      return new BusinessDayIncrementAddBuilder()
            .withTimeSnippet(timeSnippet)
            .withTicketNo(ticketNo)
            .withKindOfService(kindOfService)
            .build();
   }

   private TimeSnippet createTimeSnippet(int additionalTime) {
      Time beginTimeStamp = new Time(System.currentTimeMillis() + additionalTime);
      TimeSnippet timeSnippet = new TimeSnippet(new Date(beginTimeStamp.getTime()));
      timeSnippet.setBeginTimeStamp(beginTimeStamp);
      timeSnippet.setEndTimeStamp(new Time(System.currentTimeMillis() + additionalTime + 10));
      return timeSnippet;
   }

   private TimeSnippet createTimeSnippet(long startTime, long stopTime) {
      Time beginTimeStamp = new Time(startTime);
      Time endTimeStamp = new Time(stopTime);
      TimeSnippet timeSnippet = new TimeSnippet(new Date(beginTimeStamp.getTime()));
      timeSnippet.setBeginTimeStamp(beginTimeStamp);
      timeSnippet.setEndTimeStamp(endTimeStamp);
      return timeSnippet;
   }
}
