package com.adcubum.timerecording.app.businessday;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.adcubum.timerecording.core.work.businessday.BusinessDay;
import com.adcubum.timerecording.core.work.businessday.BusinessDayImpl;
import com.adcubum.timerecording.core.work.businessday.BusinessDayIncrement;
import com.adcubum.timerecording.core.work.businessday.BusinessDayIncrementBuilder;
import com.adcubum.timerecording.core.work.businessday.BusinessDayIncrementImpl;
import com.adcubum.timerecording.core.work.businessday.TimeSnippetImpl.TimeSnippetBuilder;
import com.adcubum.timerecording.core.work.businessday.comeandgo.impl.ComeAndGoesImpl;
import com.adcubum.timerecording.core.work.businessday.repository.BusinessDayRepository;
import com.adcubum.timerecording.jira.data.ticket.factory.TicketFactory;

class BusinessDayHelperImplTest {

   @Test
   void testDeleteAll() {

      // Given
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withBusinessDayRepository(mock(BusinessDayRepository.class))
            .build();

      // When
      tcb.businessDayHelperImpl.deleteAll(false);

      // Then
      verify(tcb.businessDayRepository).deleteAll(eq(false));
   }

   @Test
   void testAddBookedIncrements_NoBookedOnes() {

      // Given
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withBusinessDayRepository(mock(BusinessDayRepository.class))
            .addBusinessDayIncrement(BusinessDayIncrementBuilder.of()
                  .withDescription("Test")
                  .withServiceCode(113)
                  .withTicket(TicketFactory.INSTANCE.dummy("123"))
                  .withTimeSnippet(TimeSnippetBuilder.of()
                        .build())
                  .build())
            .build();

      // When
      tcb.businessDayHelperImpl.addBookedBusinessDayIncrements(tcb.businessDayIncrements);

      // Then
      verify(tcb.businessDayRepository, never()).createNew(eq(false));
      assertThat(tcb.bookedBusinessDay.getIncrements().isEmpty(), is(true));
   }

   @Test
   void testAddBookedIncrements_OneBookedOne() {

      // Given
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withBusinessDayRepository(mock(BusinessDayRepository.class))
            .addBusinessDayIncrement(BusinessDayIncrementBuilder.of()
                  .withDescription("Test")
                  .withServiceCode(113)
                  .withTicket(TicketFactory.INSTANCE.dummy("123"))
                  .withId(UUID.randomUUID())
                  .withTimeSnippet(TimeSnippetBuilder.of()
                        .build())
                  .build())
            .addBusinessDayIncrement(BusinessDayIncrementBuilder.of()
                  .withDescription("Test3")
                  .withServiceCode(111)
                  .withTicket(TicketFactory.INSTANCE.dummy("321"))
                  .withId(UUID.randomUUID())
                  .withFlagAsBooked()
                  .withTimeSnippet(TimeSnippetBuilder.of()
                        .build())
                  .build())
            .build();

      // When
      tcb.businessDayHelperImpl.addBookedBusinessDayIncrements(tcb.businessDayIncrements);

      // Then
      verify(tcb.businessDayRepository).createNew(eq(true));
      assertThat(tcb.bookedBusinessDay.getIncrements().size(), is(1));
      BusinessDayIncrement firstBookedBDInc = tcb.bookedBusinessDay.getIncrements().get(0);
      assertThat(firstBookedBDInc.getDescription(), is(tcb.businessDayIncrements.get(1).getDescription()));
      assertThat(firstBookedBDInc.getId(), is(nullValue()));
      verify(tcb.businessDayRepository).save(eq(tcb.bookedBusinessDay));
   }

   @Test
   void testAddBookedIncrements_CallTwice_ButCreateBDayOnlyOnce() {

      // Given
      BusinessDayIncrement thirdBDIncrement = BusinessDayIncrementBuilder.of()
            .withDescription("ThirdIncrement")
            .withServiceCode(111)
            .withTicket(TicketFactory.INSTANCE.dummy("321"))
            .withId(UUID.randomUUID())
            .withFlagAsBooked()
            .withTimeSnippet(TimeSnippetBuilder.of()
                  .build())
            .build();
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withBusinessDayRepository(mock(BusinessDayRepository.class))
            .addBusinessDayIncrement(BusinessDayIncrementBuilder.of()
                  .withDescription("Test")
                  .withServiceCode(113)
                  .withTicket(TicketFactory.INSTANCE.dummy("123"))
                  .withId(UUID.randomUUID())
                  .withTimeSnippet(TimeSnippetBuilder.of()
                        .build())
                  .build())
            .addBusinessDayIncrement(BusinessDayIncrementBuilder.of()
                  .withDescription("Test2")
                  .withServiceCode(111)
                  .withTicket(TicketFactory.INSTANCE.dummy("321"))
                  .withId(UUID.randomUUID())
                  .withFlagAsBooked()
                  .withTimeSnippet(TimeSnippetBuilder.of()
                        .build())
                  .build())
            .build();

      // When
      tcb.businessDayHelperImpl.addBookedBusinessDayIncrements(tcb.businessDayIncrements);
      tcb.withExistingBookedBusinessDay();
      tcb.businessDayHelperImpl.addBookedBusinessDayIncrements(Collections.singletonList(thirdBDIncrement));

      // Then
      verify(tcb.businessDayRepository).createNew(eq(true));
      assertThat(tcb.bookedBusinessDay.getIncrements().size(), is(2));
      verify(tcb.businessDayRepository, times(2)).save(eq(tcb.bookedBusinessDay));
   }

   @Test
   void testGetBusinessDay_GetNull() {

      // Given
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withBusinessDayRepository(mock(BusinessDayRepository.class))
            .build();

      // When
      BusinessDay actualBusinessDay = tcb.businessDayHelperImpl.getBusinessDay();

      // Then
      assertThat(actualBusinessDay, is(nullValue()));
   }

   private static class TestCaseBuilder {

      private List<BusinessDayIncrement> businessDayIncrements;
      private BusinessDayHelperImpl businessDayHelperImpl;
      private BusinessDayRepository businessDayRepository;
      private BusinessDayImpl bookedBusinessDay;

      private TestCaseBuilder() {
         this.bookedBusinessDay =
               spy(new BusinessDayImpl(UUID.randomUUID(), true, Collections.emptyList(), new BusinessDayIncrementImpl(), ComeAndGoesImpl.of()));
         this.businessDayIncrements = new ArrayList<>();
      }

      public TestCaseBuilder withExistingBookedBusinessDay() {
         when(businessDayRepository.findBookedBusinessDayByDate(any())).thenReturn(bookedBusinessDay);
         return this;
      }

      private TestCaseBuilder addBusinessDayIncrement(BusinessDayIncrement businessDayIncrement) {
         this.businessDayIncrements.add(businessDayIncrement);
         return this;
      }

      private TestCaseBuilder withBusinessDayRepository(BusinessDayRepository businessDayRepository) {
         this.businessDayRepository = businessDayRepository;
         return this;
      }

      private TestCaseBuilder build() {
         this.businessDayHelperImpl = new BusinessDayHelperImpl(businessDayRepository);
         when(businessDayRepository.createNew(eq(true))).thenReturn(bookedBusinessDay);
         return this;
      }
   }
}
