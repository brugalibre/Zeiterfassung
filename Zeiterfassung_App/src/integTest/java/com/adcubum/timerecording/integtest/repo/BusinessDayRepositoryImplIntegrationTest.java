package com.adcubum.timerecording.integtest.repo;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.function.Executable;
import org.springframework.boot.test.context.SpringBootTest;

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
import com.adcubum.timerecording.integtest.TestChangedComeAndGoValueImpl;
import com.adcubum.timerecording.jira.data.ticket.Ticket;
import com.adcubum.timerecording.jira.data.ticket.factory.TicketFactory;
import com.adcubum.timerecording.work.date.Time;
import com.adcubum.timerecording.work.date.TimeBuilder;
import com.adcubum.timerecording.work.date.TimeFactory;

@SpringBootTest(classes = {TestBusinessDayRepoConfig.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BusinessDayRepositoryImplIntegrationTest {

   @Test
   void test_FindBookedBusinessDay_FindNone() {

      // Given
      int year = 2021;
      int month = 11;
      int day = 1;

      Time lowerBoundsTime = TimeBuilder.of()
            .withYear(year)
            .withMonth(month)
            .withDay(day - 1)
            .withHour(0)
            .withMinute(0)
            .build();

      BusinessDayRepositoryImpl businessDayRepository = new BusinessDayRepositoryImpl();
      createAndChangeBusinessDay(year, month, day, "Test", businessDayRepository);

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

      Time lookAtTime = TimeBuilder.of()
            .withYear(year)
            .withMonth(month)
            .withDay(day)
            .withHour(4)
            .withMinute(23)
            .build();
      BusinessDayRepositoryImpl businessDayRepository = new BusinessDayRepositoryImpl();

      // The first business-day (the one we want to find)
      createAndChangeBusinessDay(year, month, day, description, businessDayRepository);
      // Another business-day (the one we dont want to find)
      createAndChangeBusinessDay(year, otherBusinessDaysIncMonth, day, otherBusinessDayIncDescDescription, businessDayRepository);

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

   private void createAndChangeBusinessDay(int year, int month, int day, String description, BusinessDayRepositoryImpl businessDayRepository) {
      BusinessDay businessDay = businessDayRepository.createNew(true);
      businessDay.addBusinessIncrement(new BusinessDayIncrementAddBuilder()
            .withDescription(description)
            .withServiceCode(113)
            .withTicket(TicketFactory.INSTANCE.dummy("123"))
            .withTimeSnippet(TimeSnippetBuilder.of()
                  .withBeginTime(TimeBuilder.of()
                        .withYear(year)
                        .withMonth(month)
                        .withDay(day)
                        .withHour(12)
                        .withMinute(45)
                        .build())
                  .withEndTime(TimeBuilder.of()
                        .withYear(year)
                        .withMonth(month)
                        .withDay(day)
                        .withHour(13)
                        .withMinute(45)
                        .build())
                  .build())
            .build());
      businessDay.flagBusinessDayAsCharged();
      businessDayRepository.save(businessDay);
   }

   @Test
   @Order(1)
   void test_FindNonAndCreateNew() {
      // Given
      BusinessDayRepositoryImpl businessDayRepository = new BusinessDayRepositoryImpl();

      // When
      BusinessDay businessDay = businessDayRepository.findFirstOrCreateNew();
      BusinessDayIncrement currentBussinessDayIncremental = businessDay.getCurrentBussinessDayIncremental();

      // Then
      assertThat(businessDay, is(notNullValue()));
      assertThat(businessDay.getIncrements().isEmpty(), is(true));
      assertThat(currentBussinessDayIncremental, is(notNullValue()));
      assertThat(currentBussinessDayIncremental.getCurrentTimeSnippet(), is(nullValue()));
   }

   @Test
   @Order(2)
   void test_FindExistingAndStartNewIncrement() {
      // Given
      BusinessDayRepositoryImpl businessDayRepository = new BusinessDayRepositoryImpl();

      // When
      BusinessDay businessDay = businessDayRepository.findFirstOrCreateNew();
      businessDay.startNewIncremental();
      BusinessDayIncrement currentBussinessDayIncremental = businessDay.getCurrentBussinessDayIncremental();
      TimeSnippet currentTimeSnippet = currentBussinessDayIncremental.getCurrentTimeSnippet();

      BusinessDay savedBusinessDay = businessDayRepository.save(businessDay);
      BusinessDayIncrement savedCurrentBussinessDayIncremental = savedBusinessDay.getCurrentBussinessDayIncremental();
      TimeSnippet savedCurrentTimeSnippet = savedCurrentBussinessDayIncremental.getCurrentTimeSnippet();

      // Then
      assertThat(savedBusinessDay, is(notNullValue()));
      assertThat(savedBusinessDay.getIncrements().isEmpty(), is(true));
      assertThat(savedCurrentBussinessDayIncremental, is(notNullValue()));
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
      BusinessDay businessDay = businessDayRepository.findFirstOrCreateNew();
      businessDay.stopCurrentIncremental();
      BusinessDayIncrement currentBussinessDayIncremental = businessDay.getCurrentBussinessDayIncremental();
      TimeSnippet currentTimeSnippet = currentBussinessDayIncremental.getCurrentTimeSnippet();

      BusinessDay savedBusinessDay = businessDayRepository.save(businessDay);
      BusinessDayIncrement savedCurrentBussinessDayIncremental = savedBusinessDay.getCurrentBussinessDayIncremental();
      TimeSnippet savedCurrentTimeSnippet = savedCurrentBussinessDayIncremental.getCurrentTimeSnippet();

      // Then
      assertThat(savedBusinessDay, is(notNullValue()));
      assertThat(savedBusinessDay.getIncrements().isEmpty(), is(true));
      assertThat(savedCurrentBussinessDayIncremental, is(notNullValue()));
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
      BusinessDay businessDay = businessDayRepository.findFirstOrCreateNew();
      String description = "test";
      int kindOfService = 113;
      String ticketNr = "SYRIUS-123";
      long beginTimeStampValue = System.currentTimeMillis() + 1000;
      long endTimeStampValue = beginTimeStampValue + 1000;
      businessDay.addBusinessIncrement(new BusinessDayIncrementAddBuilder()
            .withAmountOfHours("3")
            .withDescription(description)
            .withServiceCode(kindOfService)
            .withTicket(mockTicket(ticketNr))
            .withTimeSnippet(TimeSnippetBuilder.of()
                  .withBeginTimeStamp(beginTimeStampValue)
                  .withEndTimeStamp(endTimeStampValue)
                  .build())
            .build());
      businessDayRepository.save(businessDay);
      BusinessDay changedBusinessDay = businessDayRepository.findById(businessDay.getId());
      BusinessDayIncrement changedCurrentBussinessDayIncremental = changedBusinessDay.getCurrentBussinessDayIncremental();
      TimeSnippet changedCurrentTimeSnippet = changedCurrentBussinessDayIncremental.getCurrentTimeSnippet();

      // Then
      assertThat(changedBusinessDay, is(notNullValue()));
      assertThat(changedBusinessDay.getIncrements().size(), is(1));
      assertThat(changedCurrentBussinessDayIncremental, is(notNullValue()));
      assertThat(changedCurrentTimeSnippet, is(nullValue()));

      BusinessDayIncrement changedFirstBusinessDayIncrement = changedBusinessDay.getIncrements().get(0);
      TimeSnippet changedFirstBDIncTimeSnippet = changedFirstBusinessDayIncrement.getCurrentTimeSnippet();
      BusinessDayIncrement originFirstBusinessDayIncrement = businessDay.getIncrements().get(0);
      TimeSnippet originFirstBDIncTimeSnippet = originFirstBusinessDayIncrement.getCurrentTimeSnippet();
      assertThat(changedFirstBusinessDayIncrement.getDescription(), is(description));
      assertThat(changedFirstBusinessDayIncrement.getTicket().getNr(), is(ticketNr));
      assertThat(changedFirstBusinessDayIncrement.getChargeType(), is(kindOfService));
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
      BusinessDay businessDay = businessDayRepository.findFirstOrCreateNew();
      String description = "test";
      int kindOfService = 113;
      String ticketNr = "SYRIUS-123";
      long beginTimeStampValue = System.currentTimeMillis() + 1000;
      long endTimeStampValue = beginTimeStampValue + 1000;
      businessDay.addBusinessIncrement(new BusinessDayIncrementAddBuilder()
            .withAmountOfHours("3")
            .withDescription(description)
            .withServiceCode(kindOfService)
            .withTicket(mockTicket(ticketNr))
            .withTimeSnippet(TimeSnippetBuilder.of()
                  .withBeginTimeStamp(beginTimeStampValue)
                  .withEndTimeStamp(endTimeStampValue)
                  .build())
            .build());
      businessDay.addBusinessIncrement(new BusinessDayIncrementAddBuilder()
            .withAmountOfHours("3")
            .withDescription(description)
            .withServiceCode(kindOfService)
            .withTicket(mockTicket(ticketNr))
            .withTimeSnippet(TimeSnippetBuilder.of()
                  .withBeginTimeStamp(beginTimeStampValue)
                  .withBeginTimeStamp(endTimeStampValue)
                  .build())
            .build());
      businessDayRepository.save(businessDay);

      BusinessDay changedBusinessDay = businessDayRepository.findById(businessDay.getId());
      BusinessDayIncrement changedCurrentBussinessDayIncremental = changedBusinessDay.getCurrentBussinessDayIncremental();
      TimeSnippet currentTimeSnippet = changedCurrentBussinessDayIncremental.getCurrentTimeSnippet();

      // Then
      assertThat(businessDay, is(notNullValue()));
      assertThat(changedCurrentBussinessDayIncremental, is(notNullValue()));
      assertThat(currentTimeSnippet, is(nullValue()));
      assertThat(businessDay.getIncrements().size(), is(2));

      BusinessDayIncrement originLastBusinessDayIncrement = businessDay.getIncrements().get(1);
      TimeSnippet originLastBDIncTimeSnippet = originLastBusinessDayIncrement.getCurrentTimeSnippet();
      BusinessDayIncrement changedLastBusinessDayIncrement = changedBusinessDay.getIncrements().get(1);
      TimeSnippet changedLastBDIncTimeSnippet = changedLastBusinessDayIncrement.getCurrentTimeSnippet();
      assertThat(changedLastBDIncTimeSnippet.getBeginTimeStamp(), is(originLastBDIncTimeSnippet.getBeginTimeStamp()));
      assertThat(changedLastBDIncTimeSnippet.getEndTimeStamp(), is(originLastBDIncTimeSnippet.getEndTimeStamp()));

      // When
      changedBusinessDay.clearFinishedIncrements();
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
      BusinessDay businessDay = businessDayRepository.findFirstOrCreateNew();
      businessDay.comeOrGo();
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
      BusinessDay businessDay = businessDayRepository.findFirstOrCreateNew();
      businessDay.comeOrGo();
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
      Time newComeValue = TimeFactory.createNew(originComeAndGo.getComeAndGoTimeStamp().getBeginTimeStamp().getTime() + 90);
      Time newGoValue = TimeFactory.createNew(originComeAndGo.getComeAndGoTimeStamp().getEndTimeStamp().getTime() + 1000);
      ChangedComeAndGoValue changedComeAndGoValue = new TestChangedComeAndGoValueImpl(originComeAndGo.getId(), newComeValue, newGoValue);
      businessDay.changeComeAndGo(changedComeAndGoValue);

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
      BusinessDay businessDay = businessDayRepository.findFirstOrCreateNew();
      businessDay.flagComeAndGoesAsRecorded();

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
      BusinessDay businessDay = businessDayRepository.findFirstOrCreateNew();
      businessDay.clearComeAndGoes();

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
      int month = 2;

      Time lowerBounds = TimeBuilder.of()
            .withDay(day)
            .withMonth(month)
            .withYear(2021)
            .withHour(0)
            .build();
      Time upperBounds = TimeBuilder.of()
            .withDay(day + 1)
            .withMonth(month)
            .withYear(2021)
            .withHour(23)
            .build();

      createNewBookedBusinessDayAtDate(businessDayRepository, day, month).getId();
      createNewBookedBusinessDayAtDate(businessDayRepository, day + 1, month).getId();
      BusinessDay otherBusinessDay = createNewBookedBusinessDayAtDate(businessDayRepository, day + 2, month);

      // When
      // First make sure all BDays within the range exists 
      List<BusinessDay> bookedBussinessDaysWithinRange = businessDayRepository.findBookedBussinessDaysWithinRange(lowerBounds, upperBounds);
      assertThat(bookedBussinessDaysWithinRange.size(), is(2));

      // Then delete and verify that all BDays within the range are deleted
      businessDayRepository.deleteBookedBusinessDaysWithinRange(lowerBounds, upperBounds);
      bookedBussinessDaysWithinRange = businessDayRepository.findBookedBussinessDaysWithinRange(lowerBounds, upperBounds);
      assertThat(bookedBussinessDaysWithinRange.size(), is(0));

      // Also make sure, that the one out of scope does still exists
      otherBusinessDay = businessDayRepository.findById(otherBusinessDay.getId());
      assertThat(otherBusinessDay, is(notNullValue()));

      // Finally
      deleteAll(businessDayRepository, true);
   }

   private static BusinessDay createNewBookedBusinessDayAtDate(BusinessDayRepositoryImpl businessDayRepository, int day, int month) {
      BusinessDay businessDay = businessDayRepository.createNew(true);
      businessDay.addBusinessIncrement(new BusinessDayIncrementAddBuilder()
            .withAmountOfHours("3")
            .withDescription("test")
            .withServiceCode(113)
            .withTicket(mockTicket("ABES-1"))
            .withTimeSnippet(TimeSnippetBuilder.of()
                  .withBeginTime(TimeBuilder.of()
                        .withDay(day)
                        .withMonth(month)
                        .withYear(2021)
                        .withHour(1)
                        .build())
                  .withEndTime(TimeBuilder.of()
                        .withDay(day)
                        .withMonth(month)
                        .withYear(2021)
                        .withHour(1)
                        .build())
                  .build())
            .build());
      businessDay.flagBusinessDayAsCharged();
      return businessDayRepository.save(businessDay);
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
