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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.adcubum.timerecording.core.repository.ObjectNotFoundException;
import com.adcubum.timerecording.core.work.businessday.BusinessDay;
import com.adcubum.timerecording.core.work.businessday.BusinessDayImpl;
import com.adcubum.timerecording.core.work.businessday.BusinessDayIncrement;
import com.adcubum.timerecording.core.work.businessday.BusinessDayIncrementImpl;
import com.adcubum.timerecording.core.work.businessday.TimeSnippetImpl.TimeSnippetBuilder;
import com.adcubum.timerecording.core.work.businessday.builder.BusinessDayIncrementBuilder;
import com.adcubum.timerecording.core.work.businessday.comeandgo.impl.ComeAndGoesImpl;
import com.adcubum.timerecording.core.work.businessday.repository.BusinessDayRepository;
import com.adcubum.timerecording.jira.data.ticket.factory.TicketFactory;
import com.adcubum.timerecording.work.date.DateTime;

class BusinessDayHelperImplTest {

   @Test
   void testDeleteAll() {

      // Given
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withBusinessDayRepository(mock(TestBusinessDayRepository.class))
            .build();

      // When
      tcb.businessDayHelperImpl.deleteAll(false);

      // Then
      verify(tcb.businessDayRepository).deleteAll(eq(false));
   }

   @Test
   void testAddBookedIncrements_NoBookedBusinessDay() {

      // Given
      BookedBusinessDayDeleter bookedBusinessDayDeleter = mock(BookedBusinessDayDeleter.class);
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withBusinessDayRepository(spy(new TestBusinessDayRepository()))
            .withBookedBusinessDayDeleter(bookedBusinessDayDeleter)
            .addBusinessDayIncrement(BusinessDayIncrementBuilder.of()
                  .withDescription("Test")
                  .withServiceCode(113)
                  .withTicket(TicketFactory.INSTANCE.dummy("123"))
                  .withTimeSnippet(TimeSnippetBuilder.of()
                        .build())
                  .build())
            .build();

      // When
      BusinessDay actualBookedBusinessDay = tcb.businessDayHelperImpl.addBookedBusinessDayIncrements(tcb.businessDayIncrements);

      // Then
      verify(tcb.businessDayRepository, never()).createNew(eq(false));
      verify(bookedBusinessDayDeleter).cleanUpBookedBusinessDays();
      assertThat(actualBookedBusinessDay, is(nullValue()));
   }

   @Test
   void testAddBookedIncrements_OneBookedBusinessDay() {

      // Given
      BookedBusinessDayDeleter bookedBusinessDayDeleter = mock(BookedBusinessDayDeleter.class);
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withBusinessDayRepository(spy(new TestBusinessDayRepository()))
            .withBookedBusinessDayDeleter(bookedBusinessDayDeleter)
            .addBusinessDayIncrement(BusinessDayIncrementBuilder.of()
                  .withDescription("Test")
                  .withServiceCode(113)
                  .withTicket(TicketFactory.INSTANCE.dummy("123"))
                  .withId(UUID.randomUUID())
                  .withTimeSnippet(TimeSnippetBuilder.of()
                        .withBeginTimeStamp(10)
                        .withEndTimeStamp(20)
                        .build())
                  .build())
            .addBusinessDayIncrement(BusinessDayIncrementBuilder.of()
                  .withDescription("Test3")
                  .withServiceCode(111)
                  .withTicket(TicketFactory.INSTANCE.dummy("321"))
                  .withId(UUID.randomUUID())
                  .withFlagAsBooked()
                  .withTimeSnippet(TimeSnippetBuilder.of()
                        .withBeginTimeStamp(110)
                        .withEndTimeStamp(120)
                        .build())
                  .build())
            .build();

      // When
      BusinessDay actualBookedBusinessDay = tcb.businessDayHelperImpl.addBookedBusinessDayIncrements(tcb.businessDayIncrements);

      // Then
      verify(tcb.businessDayRepository).createNew(eq(true));
      assertThat(actualBookedBusinessDay.getIncrements().size(), is(1));
      BusinessDayIncrement firstBookedBDInc = actualBookedBusinessDay.getIncrements().get(0);
      assertThat(firstBookedBDInc.getDescription(), is(tcb.businessDayIncrements.get(1).getDescription()));
      assertThat(firstBookedBDInc.getId(), is(nullValue()));
      verify(tcb.businessDayRepository).save(eq(actualBookedBusinessDay));
      verify(bookedBusinessDayDeleter).cleanUpBookedBusinessDays();
   }

   @Test
   void testAddBookedIncrements_CallTwice_ButCreateBDayOnlyOnce() {

      // Given
      BookedBusinessDayDeleter bookedBusinessDayDeleter = mock(BookedBusinessDayDeleter.class);
      BusinessDayIncrement thirdBDIncrement = BusinessDayIncrementBuilder.of()
            .withDescription("ThirdIncrement")
            .withServiceCode(111)
            .withTicket(TicketFactory.INSTANCE.dummy("321"))
            .withId(UUID.randomUUID())
            .withFlagAsBooked()
            .withTimeSnippet(TimeSnippetBuilder.of()
                  .withBeginTimeStamp(100)
                  .withEndTimeStamp(200)
                  .build())
            .build();
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withBusinessDayRepository(spy(new TestBusinessDayRepository()))
            .addBusinessDayIncrement(BusinessDayIncrementBuilder.of()
                  .withDescription("Test")
                  .withServiceCode(113)
                  .withTicket(TicketFactory.INSTANCE.dummy("123"))
                  .withId(UUID.randomUUID())
                  .withTimeSnippet(TimeSnippetBuilder.of()
                        .withBeginTimeStamp(100)
                        .withEndTimeStamp(200)
                        .build())
                  .build())
            .addBusinessDayIncrement(BusinessDayIncrementBuilder.of()
                  .withDescription("Test2")
                  .withServiceCode(111)
                  .withTicket(TicketFactory.INSTANCE.dummy("321"))
                  .withId(UUID.randomUUID())
                  .withFlagAsBooked()
                  .withTimeSnippet(TimeSnippetBuilder.of()
                        .withBeginTimeStamp(300)
                        .withEndTimeStamp(400)
                        .build())
                  .build())
            .withBookedBusinessDayDeleter(bookedBusinessDayDeleter)
            .build();

      // When
      BusinessDay actualBookedBusinessDay = tcb.businessDayHelperImpl.addBookedBusinessDayIncrements(tcb.businessDayIncrements);
      tcb.withExistingBookedBusinessDay();
      actualBookedBusinessDay = tcb.businessDayHelperImpl.addBookedBusinessDayIncrements(Collections.singletonList(thirdBDIncrement));

      // Then
      verify(tcb.businessDayRepository).createNew(eq(true));
      assertThat(actualBookedBusinessDay.getIncrements().size(), is(2));
      verify(tcb.businessDayRepository, times(2)).save(any());
      verify(bookedBusinessDayDeleter, times(2)).cleanUpBookedBusinessDays();
   }

   @Test
   void testGetBusinessDay_GetNull() {

      // Given
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withBusinessDayRepository(new TestBusinessDayRepository())
            .build();

      // When
      BusinessDay actualBusinessDay = tcb.businessDayHelperImpl.getBusinessDay();

      // Then
      assertThat(actualBusinessDay, is(nullValue()));
   }

   private static class TestBusinessDayRepository implements BusinessDayRepository {

      private BusinessDay bookedBusinessDay;
      private boolean withExistingBookedBusinessDay;

      public TestBusinessDayRepository() {
         bookedBusinessDay = new BusinessDayImpl(UUID.randomUUID(), true, new ArrayList<>(), new BusinessDayIncrementImpl(), ComeAndGoesImpl.of());
      }

      @Override
      public BusinessDay findById(UUID objectId) throws ObjectNotFoundException {
         return bookedBusinessDay;
      }

      @Override
      public BusinessDay findFirstOrCreateNew() {
         return bookedBusinessDay;
      }

      @Override
      public BusinessDay createNew(boolean isBooked) {
         return bookedBusinessDay;
      }

      @Override
      public BusinessDay save(BusinessDay businessDay2Save) {
         this.bookedBusinessDay = businessDay2Save;
         return businessDay2Save;
      }

      @Override
      public void deleteAll(boolean isBooked) {
         this.bookedBusinessDay = null;
      }

      @Override
      public void deleteBookedBusinessDaysWithinRange(DateTime lowerBounds, DateTime upperBounds) {

      }

      @Override
      public BusinessDay findBookedBusinessDayByDate(DateTime time) {
         return withExistingBookedBusinessDay ? bookedBusinessDay : null;
      }

      @Override
      public List<BusinessDay> findBookedBussinessDaysWithinRange(DateTime lowerBounds, DateTime upperBounds) {
         return Collections.emptyList();
      }

      public void setWithExistingBookedBusinessDay() {
         withExistingBookedBusinessDay = true;
      }

   }

   private static class TestCaseBuilder {

      private List<BusinessDayIncrement> businessDayIncrements;
      private BusinessDayHelperImpl businessDayHelperImpl;
      private TestBusinessDayRepository businessDayRepository;
      private BookedBusinessDayDeleter bookedBusinessDayDeleter;

      private TestCaseBuilder() {
         this.bookedBusinessDayDeleter = mock(BookedBusinessDayDeleter.class);
         this.businessDayIncrements = new ArrayList<>();
      }

      public TestCaseBuilder withExistingBookedBusinessDay() {
         businessDayRepository.setWithExistingBookedBusinessDay();
         return this;
      }

      private TestCaseBuilder addBusinessDayIncrement(BusinessDayIncrement businessDayIncrement) {
         this.businessDayIncrements.add(businessDayIncrement);
         return this;
      }

      private TestCaseBuilder withBusinessDayRepository(TestBusinessDayRepository businessDayRepository) {
         this.businessDayRepository = businessDayRepository;
         return this;
      }

      private TestCaseBuilder withBookedBusinessDayDeleter(BookedBusinessDayDeleter bookedBusinessDayDeleter) {
         this.bookedBusinessDayDeleter = bookedBusinessDayDeleter;
         return this;
      }

      private TestCaseBuilder build() {
         this.businessDayHelperImpl = new BusinessDayHelperImpl(businessDayRepository, bookedBusinessDayDeleter);
         return this;
      }
   }
}
