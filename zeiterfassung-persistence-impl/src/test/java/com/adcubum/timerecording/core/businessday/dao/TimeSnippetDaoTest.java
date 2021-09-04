package com.adcubum.timerecording.core.businessday.dao;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.adcubum.timerecording.core.businessday.dao.config.TestTimeSnippetDaoConfig;
import com.adcubum.timerecording.core.businessday.entity.TimeSnippetEntity;
import com.adcubum.timerecording.work.date.Time;
import com.adcubum.timerecording.work.date.TimeBuilder;

@SpringBootTest(classes = {TestTimeSnippetDaoConfig.class})
class TimeSnippetDaoTest {
   @Autowired
   private TimeSnippetDao timeSnippetDao;

   @Test
   void testFindTimeSnippetsWithinRange_ThereAreTwo_ButFindOnlyOne() {

      // Given
      int year = 2021;
      int month = 12;
      int day = 1;
      Time firstBeginTime = TimeBuilder.of()
            .withYear(year)
            .withMonth(month)
            .withDay(day)
            .withHour(12)
            .withMinute(45)
            .build();
      Timestamp firstBegin = new Timestamp(firstBeginTime.getTime());
      Timestamp firstEnd = new Timestamp(firstBegin.getTime() + 1000);

      Time secondBeginTime = TimeBuilder.of()
            .withYear(year)
            .withMonth(month)
            .withDay(day - 1)
            .withHour(12)
            .withMinute(45)
            .build();
      Timestamp secondBegin = new Timestamp(secondBeginTime.getTime());
      Timestamp secondEnd = new Timestamp(firstBegin.getTime());
      Time lowerBoundsTime = TimeBuilder.of()
            .withYear(year)
            .withMonth(month)
            .withDay(day)
            .withHour(0)
            .withMinute(0)
            .build();
      Time upperBoundsTime = TimeBuilder.of()
            .withYear(year)
            .withMonth(month)
            .withDay(day)
            .withHour(23)
            .withMinute(59)
            .build();
      Timestamp lowerBounds = new Timestamp(lowerBoundsTime.getTime());
      Timestamp upperBounds = new Timestamp(upperBoundsTime.getTime());
      TestCaseBuilder tcb = new TestCaseBuilder(timeSnippetDao)
            .withPersistentTimeSnippet(new TimeSnippetEntity(UUID.randomUUID(), firstBegin, firstEnd))
            .withPersistentTimeSnippet(new TimeSnippetEntity(UUID.randomUUID(), secondBegin, secondEnd))
            .build();

      // When
      List<TimeSnippetEntity> allTimeSnippetsWithinRange = tcb.timeSnippetDao.findAllTimeSnippetsWithinRange(lowerBounds, upperBounds);

      // Then
      assertThat(allTimeSnippetsWithinRange.size(), is(1));
      TimeSnippetEntity timeSnippetEntity = allTimeSnippetsWithinRange.get(0);
      assertThat(timeSnippetEntity.getBeginTimestamp(), is(firstBegin));
      assertThat(timeSnippetEntity.getEndTimestamp(), is(firstEnd));
      tcb.timeSnippetDao.deleteAll();
   }

   @Test
   void testFindTimeSnippetsWithinRange_None_TimeSnippetOutOfRange() {

      // Given
      int year = 2021;
      int month = 12;
      int day = 1;
      Time beginTime = TimeBuilder.of()
            .withYear(year)
            .withMonth(month)
            .withDay(day)
            .withHour(12)
            .withMinute(45)
            .build();
      Timestamp begin = new Timestamp(beginTime.getTime());
      Timestamp end = new Timestamp(begin.getTime() + 1000);

      Time lowerBoundsTime = TimeBuilder.of()
            .withYear(year)
            .withMonth(month)
            .withDay(day - 1)
            .withHour(0)
            .withMinute(0)
            .build();
      Time upperBoundsTime = TimeBuilder.of()
            .withYear(year)
            .withMonth(month)
            .withDay(day - 1)
            .withHour(23)
            .withMinute(59)
            .build();
      Timestamp lowerBounds = new Timestamp(lowerBoundsTime.getTime());
      Timestamp upperBounds = new Timestamp(upperBoundsTime.getTime());
      TestCaseBuilder tcb = new TestCaseBuilder(timeSnippetDao)
            .withPersistentTimeSnippet(new TimeSnippetEntity(UUID.randomUUID(), begin, end))
            .build();

      // When
      List<TimeSnippetEntity> allTimeSnippetsWithinRange = tcb.timeSnippetDao.findAllTimeSnippetsWithinRange(lowerBounds, upperBounds);

      // Then
      assertThat(allTimeSnippetsWithinRange.isEmpty(), is(true));
      tcb.timeSnippetDao.deleteAll();
   }

   private static class TestCaseBuilder {
      private List<TimeSnippetEntity> timeSnippetEntities;
      private TimeSnippetDao timeSnippetDao;

      private TestCaseBuilder(TimeSnippetDao timeSnippetDao) {
         this.timeSnippetDao = timeSnippetDao;
         this.timeSnippetEntities = new ArrayList<>();
      }

      private TestCaseBuilder withPersistentTimeSnippet(TimeSnippetEntity timeSnippetEntity) {
         timeSnippetEntities.add(timeSnippetEntity);
         return this;
      }

      private TestCaseBuilder build() {
         saveAllTimeSnippetEntities();
         return this;
      }

      private void saveAllTimeSnippetEntities() {
         for (TimeSnippetEntity timeSnippetEntity : timeSnippetEntities) {
            timeSnippetDao.save(timeSnippetEntity);
         }
      }
   }
}
