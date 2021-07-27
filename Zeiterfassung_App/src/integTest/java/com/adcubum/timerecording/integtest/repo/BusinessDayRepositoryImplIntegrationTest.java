package com.adcubum.timerecording.integtest.repo;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
import com.adcubum.timerecording.core.work.businessday.update.callback.impl.BusinessDayIncrementAdd.BusinessDayIncrementAddBuilder;
import com.adcubum.timerecording.jira.data.ticket.Ticket;

@SpringBootTest(classes = {TestBusinessDayRepoConfig.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BusinessDayRepositoryImplIntegrationTest {

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

      businessDayRepository.deleteAll();
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

      businessDayRepository.deleteAll();
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
            .withKindOfService(kindOfService)
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
      businessDayRepository.deleteAll();
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
            .withKindOfService(kindOfService)
            .withTicket(mockTicket(ticketNr))
            .withTimeSnippet(TimeSnippetBuilder.of()
                  .withBeginTimeStamp(beginTimeStampValue)
                  .withEndTimeStamp(endTimeStampValue)
                  .build())
            .build());
      businessDay.addBusinessIncrement(new BusinessDayIncrementAddBuilder()
            .withAmountOfHours("3")
            .withDescription(description)
            .withKindOfService(kindOfService)
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

      businessDayRepository.deleteAll();
   }

   @Test
   @Order(6)
   void test_FindById_NoExisting() {

      // Given // Given
      BusinessDayRepositoryImpl businessDayRepository = new BusinessDayRepositoryImpl();
      businessDayRepository.deleteAll();
      UUID id = UUID.randomUUID();

      // When
      Executable ex = () -> businessDayRepository.findById(id);

      // Then
      assertThrows(ObjectNotFoundException.class, ex);
   }

   private static Ticket mockTicket(String ticketNr) {
      Ticket ticket = mock(Ticket.class);
      when(ticket.getNr()).thenReturn(ticketNr);
      return ticket;
   }
}
