package com.adcubum.timerecording.core.businessday.dao;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.adcubum.timerecording.core.businessday.dao.config.TestTimeSnippetDaoConfig;
import com.adcubum.timerecording.core.businessday.entity.BusinessDayEntity;
import com.adcubum.timerecording.core.businessday.entity.BusinessDayIncrementEntity;
import com.adcubum.timerecording.core.businessday.entity.TimeSnippetEntity;
import com.adcubum.timerecording.work.date.DateTime;
import com.adcubum.timerecording.work.date.DateTimeBuilder;

@SpringBootTest(classes = {TestTimeSnippetDaoConfig.class})
class BusinessDayDaoTest {
   @Autowired
   private BusinessDayDao businessDayDao;

   @Test
   void testFindTimeSnippetsWithinRange_ThereAreTwo_ButFindOnlyOne() {

      // Given
      int year = 2021;
      int month = 12;
      int day = 1;
      DateTime firstBeginTime = DateTimeBuilder.of()
            .withYear(year)
            .withMonth(month)
            .withDay(day)
            .withHour(12)
            .withMinute(45)
            .build();
      Timestamp firstBegin = new Timestamp(firstBeginTime.getTime());
      Timestamp firstEnd = new Timestamp(firstBegin.getTime() + 1000);

      DateTime secondBeginTime = DateTimeBuilder.of()
            .withYear(year)
            .withMonth(month)
            .withDay(day - 1)
            .withHour(12)
            .withMinute(45)
            .build();
      Timestamp secondBegin = new Timestamp(secondBeginTime.getTime());
      Timestamp secondEnd = new Timestamp(firstBegin.getTime());
      DateTime lowerBoundsTime = DateTimeBuilder.of()
            .withYear(year)
            .withMonth(month)
            .withDay(day)
            .withHour(0)
            .withMinute(0)
            .build();
      DateTime upperBoundsTime = DateTimeBuilder.of()
            .withYear(year)
            .withMonth(month)
            .withDay(day)
            .withHour(23)
            .withMinute(59)
            .build();
      Timestamp lowerBounds = new Timestamp(lowerBoundsTime.getTime());
      Timestamp upperBounds = new Timestamp(upperBoundsTime.getTime());
      TestCaseBuilder tcb = new TestCaseBuilder(businessDayDao)
            .addPersistentBusinessDayEntity("test", "ABES-123", new TimeSnippetEntity(null, firstBegin, firstEnd))
            .addPersistentBusinessDayEntity("test", "ABES-123", new TimeSnippetEntity(null, secondBegin, secondEnd))
            .build();

      // When
      List<BusinessDayEntity> allBusinessDaysWithinRange = tcb.businessDayDao.findAllBusinessDayEntitiesWithinRange(lowerBounds, upperBounds);

      // Then
      assertThat(allBusinessDaysWithinRange.size(), is(1));
      TimeSnippetEntity timeSnippetEntity = allBusinessDaysWithinRange.get(0).getBusinessDayIncrementEntities().get(0).getCurrentTimeSnippetEntity();
      assertThat(timeSnippetEntity.getBeginTimestamp(), is(firstBegin));
      assertThat(timeSnippetEntity.getEndTimestamp(), is(firstEnd));
      tcb.businessDayDao.deleteAll();
   }

   @Test
   void testFindTimeSnippetsWithinRange_None_TimeSnippetOutOfRange() {

      // Given
      int year = 2021;
      int month = 12;
      int day = 1;
      DateTime beginTime = DateTimeBuilder.of()
            .withYear(year)
            .withMonth(month)
            .withDay(day)
            .withHour(12)
            .withMinute(45)
            .build();
      Timestamp begin = new Timestamp(beginTime.getTime());
      Timestamp end = new Timestamp(begin.getTime() + 1000);

      DateTime lowerBoundsTime = DateTimeBuilder.of()
            .withYear(year)
            .withMonth(month)
            .withDay(day - 1)
            .withHour(0)
            .withMinute(0)
            .build();
      DateTime upperBoundsTime = DateTimeBuilder.of()
            .withYear(year)
            .withMonth(month)
            .withDay(day - 1)
            .withHour(23)
            .withMinute(59)
            .build();
      Timestamp lowerBounds = new Timestamp(lowerBoundsTime.getTime());
      Timestamp upperBounds = new Timestamp(upperBoundsTime.getTime());
      TestCaseBuilder tcb = new TestCaseBuilder(businessDayDao)
            .addPersistentBusinessDayEntity("test", "ABES-123", new TimeSnippetEntity(null, begin, end))
            .build();

      // When
      List<BusinessDayEntity> allBusinessDaysWithinRange = tcb.businessDayDao.findAllBusinessDayEntitiesWithinRange(lowerBounds, upperBounds);

      // Then
      assertThat(allBusinessDaysWithinRange.isEmpty(), is(true));
      tcb.businessDayDao.deleteAll();
   }

   private static class TestCaseBuilder {
      private BusinessDayDao businessDayDao;
      private List<BusinessDayEntity> businessDayEntities;

      private TestCaseBuilder(BusinessDayDao businessDayDao) {
         this.businessDayDao = businessDayDao;
         this.businessDayEntities = new ArrayList<>();
      }

      private static BusinessDayEntity buildBusinessDayEntity(String description, String ticketNr) {
         BusinessDayEntity businessDayEntity = new BusinessDayEntity();
         businessDayEntity.setComeAndGoesEntity(null);
         BusinessDayIncrementEntity businessDayIncrementEntity =
               new BusinessDayIncrementEntity(null, businessDayEntity, description, ticketNr, 113, false, false);
         businessDayEntity.setBusinessDayIncrementEntities(Collections.singletonList(businessDayIncrementEntity));
         return businessDayEntity;
      }

      private TestCaseBuilder addPersistentBusinessDayEntity(String description, String ticketNr, TimeSnippetEntity timeSnippetEntity) {
         BusinessDayEntity businessDayEntity = buildBusinessDayEntity(description, ticketNr);
         BusinessDayIncrementEntity businessDayIncrementEntity = businessDayEntity.getBusinessDayIncrementEntities().get(0);
         businessDayIncrementEntity.setCurrentTimeSnippetEntity(timeSnippetEntity);
         this.businessDayEntities.add(businessDayEntity);
         return this;
      }

      private TestCaseBuilder build() {
         saveAllTimeSnippetEntities();
         return this;
      }

      private void saveAllTimeSnippetEntities() {
         for (BusinessDayEntity businessDayEntity : businessDayEntities) {
            businessDayDao.save(businessDayEntity);
         }
      }
   }
}
