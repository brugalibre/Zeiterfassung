package com.adcubum.timerecording.api.businessday.history;


import static com.adcubum.timerecording.api.businessday.history.BusinessDayIntegTestUtil.createNewBookedBusinessDayAtLocalDate;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

import com.adcubum.timerecording.application.TimeRecordingApplication;
import com.adcubum.timerecording.application.TimeRecordingRepositoryConfig;
import com.adcubum.timerecording.core.businessday.repository.impl.BusinessDayRepositoryImpl;
import com.adcubum.timerecording.model.businessday.history.BusinessDayHistoryDto;
import com.adcubum.timerecording.model.businessday.history.BusinessDayHistoryOverviewDto;

@SpringBootTest(classes = TimeRecordingApplication.class)
@Import(value = TimeRecordingRepositoryConfig.class)
@TestPropertySource(locations = "classpath:businessday_hist_inmemory_repo_config.properties")
class BusinessDayHistoryControllerIntegrationTest {

   @Autowired
   private BusinessDayHistoryController businessDayHistoryController;

   @Test
   void test_CreateBookedBusinessDay_AndLoadHistory() {
      // Given
      LocalDate now = LocalDate.now();
      int nowTimeSnippetDuration = 1;
      int anotherDateTimeSnippetDuration = 3;
      LocalDate anotherDate = getAnotherDate(now);
      BusinessDayRepositoryImpl businessDayRepository = new BusinessDayRepositoryImpl();
      createNewBookedBusinessDayAtLocalDate(now, "test", businessDayRepository, nowTimeSnippetDuration);
      createNewBookedBusinessDayAtLocalDate(anotherDate, "test", businessDayRepository, anotherDateTimeSnippetDuration);

      // When
      BusinessDayHistoryOverviewDto businessDayHistoryOverviewDto = businessDayHistoryController.getBusinessDayHistoryDto();

      // Then
      List<BusinessDayHistoryDto> businessDayHistoryDtos = businessDayHistoryOverviewDto.getBusinessDayHistoryDtos();
      assertThat(businessDayHistoryDtos.size(), is(now.lengthOfMonth()));
      BusinessDayHistoryDto bDayHistoryOfToday = findBusinessDayHistory4Date(now, businessDayHistoryDtos);
      assertThat(bDayHistoryOfToday.getDate(), is(now));
      assertThat(bDayHistoryOfToday.getBookedHours(), is(String.valueOf(nowTimeSnippetDuration)));
      BusinessDayHistoryDto bDayHistoryOfAnotherDay = findBusinessDayHistory4Date(anotherDate, businessDayHistoryDtos);
      assertThat(bDayHistoryOfAnotherDay.getDate(), is(anotherDate));
      assertThat(bDayHistoryOfAnotherDay.getBookedHours(), is(String.valueOf(anotherDateTimeSnippetDuration)));
   }

   private BusinessDayHistoryDto findBusinessDayHistory4Date(LocalDate date, List<BusinessDayHistoryDto> businessDayHistoryDtos) {
      return businessDayHistoryDtos.stream()
            .filter(businessDayHistory -> date.equals(businessDayHistory.getDate()))
            .findFirst()
            .orElseThrow(IllegalStateException::new);
   }

   private static LocalDate getAnotherDate(LocalDate now) {
      int anothersDateDay = now.getDayOfMonth();
      if (now.getDayOfMonth() == 0) {
         anothersDateDay++;
      } else {
         anothersDateDay--;
      }
      return LocalDate.of(now.getYear(), now.getMonthValue(), anothersDateDay);
   }
}
