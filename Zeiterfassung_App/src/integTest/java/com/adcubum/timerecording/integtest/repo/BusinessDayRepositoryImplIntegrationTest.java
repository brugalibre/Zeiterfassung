package com.adcubum.timerecording.integtest.repo;

import com.adcubum.timerecording.core.businessday.repository.impl.BusinessDayRepositoryImpl;
import com.adcubum.timerecording.core.repository.ObjectNotFoundException;
import com.adcubum.timerecording.core.work.businessday.BusinessDay;
import com.adcubum.timerecording.core.work.businessday.BusinessDayIncrement;
import com.adcubum.timerecording.core.work.businessday.TimeSnippet;
import com.adcubum.timerecording.core.work.businessday.TimeSnippetImpl.TimeSnippetBuilder;
import com.adcubum.timerecording.core.work.businessday.comeandgo.ComeAndGo;
import com.adcubum.timerecording.core.work.businessday.comeandgo.ComeAndGoes;
import com.adcubum.timerecording.core.work.businessday.comeandgo.change.ChangedComeAndGoValue;
import com.adcubum.timerecording.core.work.businessday.repository.BusinessDayRepository;
import com.adcubum.timerecording.core.work.businessday.update.callback.impl.BusinessDayIncrementAdd.BusinessDayIncrementAddBuilder;
import com.adcubum.timerecording.data.ticket.ticketactivity.factor.TicketActivityFactory;
import com.adcubum.timerecording.integtest.TestChangedComeAndGoValueImpl;
import com.adcubum.timerecording.jira.data.ticket.Ticket;
import com.adcubum.timerecording.work.date.DateTime;
import com.adcubum.timerecording.work.date.DateTimeBuilder;
import com.adcubum.timerecording.work.date.DateTimeFactory;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.function.Executable;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

import static com.adcubum.timerecording.integtest.BusinessDayIntegTestUtil.createNewBookedBusinessDayAtDate;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {TestBusinessDayRepoConfig.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BusinessDayRepositoryImplIntegrationTest {

   @Test
   void test_FindBookedBusinessDay_FindNone() {

      // Given
      int year = 2021;
      int month = 11;
      int day = 1;

      DateTime lowerBoundsTime = DateTimeBuilder.of()
            .withYear(year)
            .withMonth(month)
            .withDay(day - 1)
            .withHour(0)
            .withMinute(0)
            .build();

      BusinessDayRepositoryImpl businessDayRepository = new BusinessDayRepositoryImpl();
      createNewBookedBusinessDayAtDate(year, month, day, "Test", businessDayRepository);

      // When
      BusinessDay actualBookedBusinessDay = businessDayRepository.findBookedBusinessDayByDate(lowerBoundsTime);

      // Then
      assertThat(actualBookedBusinessDay, is(nullValue()));
   }

   @Test
   void test_FindBookedBusinessDay_FindExactlyOne() {

      // Given
      int year = 2021;
      int month = 11;
      int day = 1;
      int otherBusinessDaysIncMonth = month - 1;
      String description = "Test";
      String otherBusinessDayIncDescDescription = "MichNIxFindenWollen";

      DateTime lookAtTime = DateTimeBuilder.of()
            .withYear(year)
            .withMonth(month)
            .withDay(day)
            .withHour(4)
            .withMinute(23)
            .build();
      BusinessDayRepositoryImpl businessDayRepository = new BusinessDayRepositoryImpl();

      // The first business-day (the one we want to find)
      createNewBookedBusinessDayAtDate(year, month, day, description, businessDayRepository);
      // Another business-day (the one we dont want to find)
      createNewBookedBusinessDayAtDate(year, otherBusinessDaysIncMonth, day, otherBusinessDayIncDescDescription, businessDayRepository);

      // When
      BusinessDay actualBookedBusinessDay = businessDayRepository.findBookedBusinessDayByDate(lookAtTime);

      // Then
      assertThat(actualBookedBusinessDay, is(notNullValue()));
      assertThat(actualBookedBusinessDay.isBooked(), is(true));
      List<BusinessDayIncrement> increments = actualBookedBusinessDay.getIncrements();
      assertThat(increments.size(), is(1));
      BusinessDayIncrement businessDayIncrement = increments.get(0);
      assertThat(businessDayIncrement.getDescription(), is(description));
      assertThat(businessDayIncrement.getCurrentTimeSnippet().getBeginTimeStamp(), is(notNullValue()));
      assertThat(businessDayIncrement.getCurrentTimeSnippet().getEndTimeStamp(), is(notNullValue()));

      deleteAll(businessDayRepository, false);
   }

   @Test
   @Order(1)
   void test_FindNonAndCreateNew() {
      // Given
      BusinessDayRepositoryImpl businessDayRepository = new BusinessDayRepositoryImpl();

      // When
      BusinessDay businessDay = businessDayRepository.findFirstOrCreateNew();
      TimeSnippet currentTimeSnippet = businessDay.getCurrentTimeSnippet();

      // Then
      assertThat(businessDay, is(notNullValue()));
      assertThat(businessDay.getIncrements().isEmpty(), is(true));
      assertThat(currentTimeSnippet, is(notNullValue()));
      assertThat(currentTimeSnippet.getBeginTimeStamp(), is(nullValue()));
   }

   @Test
   @Order(2)
   void test_FindExistingAndStartNewIncrement() {
      // Given
      BusinessDayRepositoryImpl businessDayRepository = new BusinessDayRepositoryImpl();

      // When
      BusinessDay businessDay = businessDayRepository.findFirstOrCreateNew()
            .startNewIncremental();
      TimeSnippet currentTimeSnippet = businessDay.getCurrentTimeSnippet();

      BusinessDay savedBusinessDay = businessDayRepository.save(businessDay);
      TimeSnippet savedCurrentTimeSnippet = savedBusinessDay.getCurrentTimeSnippet();

      // Then
      assertThat(savedBusinessDay, is(notNullValue()));
      assertThat(savedBusinessDay.getIncrements().isEmpty(), is(true));
      assertThat(savedCurrentTimeSnippet, is(notNullValue()));
      assertThat(savedCurrentTimeSnippet.getBeginTimeStamp(), is(currentTimeSnippet.getBeginTimeStamp()));
      assertThat(savedCurrentTimeSnippet.getEndTimeStamp(), is(nullValue()));
      assertThat(savedBusinessDay.getIncrements().isEmpty(), is(true));
   }

   @Test
   @Order(3)
   void test_FindExistingAndStopNewIncrement() {
      // Given
      BusinessDayRepositoryImpl businessDayRepository = new BusinessDayRepositoryImpl();

      // When
      BusinessDay businessDay = businessDayRepository.findFirstOrCreateNew()
            .stopCurrentIncremental();
      TimeSnippet currentTimeSnippet = businessDay.getCurrentTimeSnippet();

      BusinessDay savedBusinessDay = businessDayRepository.save(businessDay);
      TimeSnippet savedCurrentTimeSnippet = savedBusinessDay.getCurrentTimeSnippet();

      // Then
      assertThat(savedBusinessDay, is(notNullValue()));
      assertThat(savedBusinessDay.getIncrements().isEmpty(), is(true));
      assertThat(savedCurrentTimeSnippet, is(notNullValue()));
      assertThat(savedCurrentTimeSnippet.getBeginTimeStamp(), is(notNullValue()));
      assertThat(savedCurrentTimeSnippet.getEndTimeStamp(), is(notNullValue()));
      assertThat(savedCurrentTimeSnippet.getBeginTimeStamp(), is(currentTimeSnippet.getBeginTimeStamp()));
      assertThat(savedCurrentTimeSnippet.getEndTimeStamp(), is(currentTimeSnippet.getEndTimeStamp()));
      assertThat(savedBusinessDay.getIncrements().isEmpty(), is(true));

      deleteAll(businessDayRepository, false);
   }

   @Test
   @Order(4)
   void test_FindExistingByIdAndAddNewBusinessDayIncrement() {
      // Given
      BusinessDayRepositoryImpl businessDayRepository = new BusinessDayRepositoryImpl();

      // When
      String description = "test";
      int serviceCode = 113;
      String ticketNr = "SYRIUS-123";
      long beginTimeStampValue = System.currentTimeMillis() + 1000;
      long endTimeStampValue = beginTimeStampValue + 1000;
      BusinessDay businessDay = businessDayRepository.findFirstOrCreateNew()
            .addBusinessIncrement(new BusinessDayIncrementAddBuilder()
                  .withAmountOfHours("3")
                  .withDescription(description)
                  .withTicketActivity(TicketActivityFactory.INSTANCE.createNew("test", serviceCode))
                  .withTicket(mockTicket(ticketNr))
                  .withTimeSnippet(TimeSnippetBuilder.of()
                        .withBeginTimeStamp(beginTimeStampValue)
                        .withEndTimeStamp(endTimeStampValue)
                        .build())
                  .build());
      businessDayRepository.save(businessDay);
      BusinessDay changedBusinessDay = businessDayRepository.findById(businessDay.getId());
      TimeSnippet changedCurrentTimeSnippet = changedBusinessDay.getCurrentTimeSnippet();

      // Then
      assertThat(changedBusinessDay, is(notNullValue()));
      assertThat(changedBusinessDay.getIncrements().size(), is(1));
      assertThat(changedCurrentTimeSnippet, is(notNullValue()));
      assertThat(changedCurrentTimeSnippet.getBeginTimeStamp(), is(nullValue()));

      BusinessDayIncrement changedFirstBusinessDayIncrement = changedBusinessDay.getIncrements().get(0);
      TimeSnippet changedFirstBDIncTimeSnippet = changedFirstBusinessDayIncrement.getCurrentTimeSnippet();
      BusinessDayIncrement originFirstBusinessDayIncrement = businessDay.getIncrements().get(0);
      TimeSnippet originFirstBDIncTimeSnippet = originFirstBusinessDayIncrement.getCurrentTimeSnippet();
      assertThat(changedFirstBusinessDayIncrement.getDescription(), is(description));
      assertThat(changedFirstBusinessDayIncrement.getTicket().getNr(), is(ticketNr));
      assertThat(changedFirstBusinessDayIncrement.getTicketActivity().getActivityCode(), is(serviceCode));
      assertThat(changedFirstBDIncTimeSnippet.getBeginTimeStamp(), is(originFirstBDIncTimeSnippet.getBeginTimeStamp()));
      assertThat(changedFirstBDIncTimeSnippet.getEndTimeStamp(), is(originFirstBDIncTimeSnippet.getEndTimeStamp()));
      deleteAll(businessDayRepository, false);
   }

   @Test
   @Order(5)
   void test_CreateNew_Add2BusinessDayIncrements_AndDeleteAgain() {
      // Given
      BusinessDayRepositoryImpl businessDayRepository = new BusinessDayRepositoryImpl();

      // When
      String description = "test";
      int serviceCode = 113;
      String ticketNr = "SYRIUS-123";
      long beginTimeStampValue = System.currentTimeMillis() + 1000;
      long endTimeStampValue = beginTimeStampValue + 1000;
      BusinessDay businessDay = businessDayRepository.findFirstOrCreateNew()
            .addBusinessIncrement(new BusinessDayIncrementAddBuilder()
                  .withAmountOfHours("3")
                  .withDescription(description)
                  .withTicketActivity(TicketActivityFactory.INSTANCE.createNew("test", serviceCode))
                  .withTicket(mockTicket(ticketNr))
                  .withTimeSnippet(TimeSnippetBuilder.of()
                        .withBeginTimeStamp(beginTimeStampValue)
                        .withEndTimeStamp(endTimeStampValue)
                        .build())
                  .build())
            .addBusinessIncrement(new BusinessDayIncrementAddBuilder()
                  .withAmountOfHours("3")
                  .withDescription(description)
                  .withTicketActivity(TicketActivityFactory.INSTANCE.createNew("test", serviceCode))
                  .withTicket(mockTicket(ticketNr))
                  .withTimeSnippet(TimeSnippetBuilder.of()
                        .withBeginTimeStamp(beginTimeStampValue)
                        .withEndTimeStamp(endTimeStampValue)
                        .build())
                  .build());
      businessDay = businessDayRepository.save(businessDay);

      BusinessDay changedBusinessDay = businessDayRepository.findById(businessDay.getId());
      TimeSnippet changedCurrentTimeSnippet = changedBusinessDay.getCurrentTimeSnippet();

      // Then
      assertThat(businessDay, is(notNullValue()));
      assertThat(changedCurrentTimeSnippet, is(notNullValue()));
      assertThat(changedCurrentTimeSnippet.getBeginTimeStamp(), is(nullValue()));
      assertThat(businessDay.getIncrements().size(), is(2));

      BusinessDayIncrement originLastBusinessDayIncrement = businessDay.getIncrements().get(1);
      TimeSnippet originLastBDIncTimeSnippet = originLastBusinessDayIncrement.getCurrentTimeSnippet();
      BusinessDayIncrement changedLastBusinessDayIncrement = changedBusinessDay.getIncrements().get(1);
      TimeSnippet changedLastBDIncTimeSnippet = changedLastBusinessDayIncrement.getCurrentTimeSnippet();
      assertThat(changedLastBDIncTimeSnippet.getBeginTimeStamp(), is(originLastBDIncTimeSnippet.getBeginTimeStamp()));
      assertThat(changedLastBDIncTimeSnippet.getEndTimeStamp(), is(originLastBDIncTimeSnippet.getEndTimeStamp()));

      // When
      changedBusinessDay = changedBusinessDay.clearFinishedIncrements();
      BusinessDay clearedBusinessDay = businessDayRepository.save(changedBusinessDay);

      // Then
      assertThat(clearedBusinessDay.getIncrements().isEmpty(), is(true));

      deleteAll(businessDayRepository, false);
   }

   @Test
   @Order(6)
   void test_FindById_NoExisting() {

      // Given // Given
      BusinessDayRepositoryImpl businessDayRepository = new BusinessDayRepositoryImpl();
      deleteAll(businessDayRepository, false);
      UUID id = UUID.randomUUID();

      // When
      Executable ex = () -> businessDayRepository.findById(id);

      // Then
      assertThrows(ObjectNotFoundException.class, ex);
   }

   @Test
   @Order(7)
   void test_CreateNew_AndCome() {
      // Given
      BusinessDayRepositoryImpl businessDayRepository = new BusinessDayRepositoryImpl();

      // When
      BusinessDay businessDay = businessDayRepository.findFirstOrCreateNew()
            .comeOrGo();
      ComeAndGoes originComeAndGoes = businessDay.getComeAndGoes();
      ComeAndGo originComeAndGo = originComeAndGoes.getComeAndGoEntries().get(0);

      businessDayRepository.save(businessDay);

      BusinessDay changedBusinessDay = businessDayRepository.findById(businessDay.getId());
      ComeAndGoes changedComeAndGoes = changedBusinessDay.getComeAndGoes();

      // Then
      assertThat(changedComeAndGoes.getComeAndGoEntries().size(), is(1));
      ComeAndGo changedComeAndGo = changedComeAndGoes.getComeAndGoEntries().get(0);
      assertThat(originComeAndGo.getComeAndGoTimeStamp().getBeginTimeStamp(), is(changedComeAndGo.getComeAndGoTimeStamp().getBeginTimeStamp()));
      assertThat(changedComeAndGo.isNotDone(), is(true));
   }

   @Test
   @Order(7)
   void test_FindExisting_AndGo() {
      // Given
      BusinessDayRepositoryImpl businessDayRepository = new BusinessDayRepositoryImpl();

      // When
      BusinessDay businessDay = businessDayRepository.findFirstOrCreateNew()
            .comeOrGo();
      ComeAndGoes originComeAndGoes = businessDay.getComeAndGoes();
      ComeAndGo originComeAndGo = originComeAndGoes.getComeAndGoEntries().get(0);

      businessDayRepository.save(businessDay);

      BusinessDay changedBusinessDay = businessDayRepository.findById(businessDay.getId());
      ComeAndGoes changedComeAndGoes = changedBusinessDay.getComeAndGoes();

      // Then
      assertThat(changedComeAndGoes.getComeAndGoEntries().size(), is(1));
      ComeAndGo changedComeAndGo = changedComeAndGoes.getComeAndGoEntries().get(0);
      assertThat(originComeAndGo.getComeAndGoTimeStamp().getBeginTimeStamp(), is(changedComeAndGo.getComeAndGoTimeStamp().getBeginTimeStamp()));
      assertThat(originComeAndGo.getComeAndGoTimeStamp().getEndTimeStamp(), is(changedComeAndGo.getComeAndGoTimeStamp().getEndTimeStamp()));
      assertThat(changedComeAndGo.isNotDone(), is(false));
      assertThat(originComeAndGo.getId(), is(changedComeAndGo.getId()));
   }

   @Test
   @Order(8)
   void test_FindExisting_AndChangeExistingComeAndGo() {
      // Given
      BusinessDayRepositoryImpl businessDayRepository = new BusinessDayRepositoryImpl();

      // When
      BusinessDay businessDay = businessDayRepository.findFirstOrCreateNew();
      ComeAndGoes originComeAndGoes = businessDay.getComeAndGoes();

      // change existing comeandgo
      ComeAndGo originComeAndGo = originComeAndGoes.getComeAndGoEntries().get(0);
      DateTime newComeValue = DateTimeFactory.createNew(originComeAndGo.getComeAndGoTimeStamp().getBeginTimeStamp().getTime() + 90);
      DateTime newGoValue = DateTimeFactory.createNew(originComeAndGo.getComeAndGoTimeStamp().getEndTimeStamp().getTime() + 1000);
      ChangedComeAndGoValue changedComeAndGoValue = new TestChangedComeAndGoValueImpl(originComeAndGo.getId(), newComeValue, newGoValue);
      businessDay = businessDay.changeComeAndGo(changedComeAndGoValue);

      businessDayRepository.save(businessDay);

      BusinessDay changedBusinessDay = businessDayRepository.findById(businessDay.getId());
      ComeAndGoes changedComeAndGoes = changedBusinessDay.getComeAndGoes();

      // Then
      assertThat(changedComeAndGoes.getComeAndGoEntries().size(), is(1));
      ComeAndGo changedComeAndGo = changedComeAndGoes.getComeAndGoEntries().get(0);
      assertThat(originComeAndGo.getComeAndGoTimeStamp().getBeginTimeStamp(), is(changedComeAndGo.getComeAndGoTimeStamp().getBeginTimeStamp()));
      assertThat(originComeAndGo.getComeAndGoTimeStamp().getEndTimeStamp(), is(changedComeAndGo.getComeAndGoTimeStamp().getEndTimeStamp()));
      assertThat(changedComeAndGo.isNotDone(), is(false));
      assertThat(originComeAndGo.getId(), is(changedComeAndGo.getId()));
      assertThat(changedComeAndGo.isNotRecorded(), is(true));
      assertThat(originComeAndGo.isNotRecorded(), is(true));
   }

   @Test
   @Order(9)
   void test_FlagExistingComeAndGoAsRecorded() {
      // Given
      BusinessDayRepositoryImpl businessDayRepository = new BusinessDayRepositoryImpl();

      // When
      BusinessDay businessDay = businessDayRepository.findFirstOrCreateNew()
            .flagComeAndGoesAsRecorded();

      businessDayRepository.save(businessDay);
      BusinessDay changedBusinessDay = businessDayRepository.findById(businessDay.getId());
      ComeAndGoes changedComeAndGoes = changedBusinessDay.getComeAndGoes();

      // Then
      assertThat(changedComeAndGoes.getComeAndGoEntries().size(), is(1));
      ComeAndGo changedComeAndGo = changedComeAndGoes.getComeAndGoEntries().get(0);
      assertThat(changedComeAndGo.isNotDone(), is(false));
      assertThat(changedComeAndGo.isNotRecorded(), is(false));
   }

   @Test
   @Order(10)
   void test_DeleteExistingComeAndGo() {
      // Given
      BusinessDayRepositoryImpl businessDayRepository = new BusinessDayRepositoryImpl();

      // When
      BusinessDay businessDay = businessDayRepository.findFirstOrCreateNew()
            .clearComeAndGoes();

      businessDayRepository.save(businessDay);

      BusinessDay changedBusinessDay = businessDayRepository.findById(businessDay.getId());
      ComeAndGoes changedComeAndGoes = changedBusinessDay.getComeAndGoes();

      // Then
      assertThat(changedComeAndGoes.getComeAndGoEntries().isEmpty(), is(true));
   }

   @Test
   @Order(11)
   void test_DeleteWithinRange() {
      // Given
      BusinessDayRepositoryImpl businessDayRepository = new BusinessDayRepositoryImpl();
      int day = 1;
      int upperBoundsdDay = day + 1;
      int month = 2;

      DateTime lowerBounds = DateTimeBuilder.of()
            .withDay(day)
            .withMonth(month)
            .withYear(2021)
            .withHour(0)
            .build();
      DateTime upperBounds = DateTimeBuilder.of()
            .withDay(upperBoundsdDay)
            .withMonth(month)
            .withYear(2021)
            .withHour(23)
            .build();

      createNewBookedBusinessDayAtDate(2021, month, day, "test", businessDayRepository);
      createNewBookedBusinessDayAtDate(2021, month, upperBoundsdDay, "test", businessDayRepository);
      BusinessDay otherBusinessDay = createNewBookedBusinessDayAtDate(2021, month, day + 2, "test", businessDayRepository);

      // When
      // First make sure all BDays within the range exists 
      List<BusinessDay> bookedBusinessDaysWithinRange = businessDayRepository.findBookedBusinessDaysWithinRange(lowerBounds, upperBounds);
      assertThat(bookedBusinessDaysWithinRange.size(), is(2));

      // Then delete and verify that all BDays within the range are deleted
      businessDayRepository.deleteBookedBusinessDaysWithinRange(lowerBounds, upperBounds);
      bookedBusinessDaysWithinRange = businessDayRepository.findBookedBusinessDaysWithinRange(lowerBounds, upperBounds);
      assertThat(bookedBusinessDaysWithinRange.size(), is(0));

      // Also make sure, that the one out of scope does still exists
      otherBusinessDay = businessDayRepository.findById(otherBusinessDay.getId());
      assertThat(otherBusinessDay, is(notNullValue()));

      // Finally
      deleteAll(businessDayRepository, true);
   }

   private static Ticket mockTicket(String ticketNr) {
      Ticket ticket = mock(Ticket.class);
      when(ticket.getNr()).thenReturn(ticketNr);
      return ticket;
   }

   private void deleteAll(BusinessDayRepository businessDayRepository, boolean isBooked) {
      businessDayRepository.deleteAll(isBooked);
   }
}
