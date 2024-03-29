package com.adcubum.timerecording.app.businessday;

import com.adcubum.timerecording.core.businessday.repository.impl.BusinessDayRepositoryImpl;
import com.adcubum.timerecording.core.work.businessday.BusinessDay;
import com.adcubum.timerecording.core.work.businessday.BusinessDayIncrement;
import com.adcubum.timerecording.core.work.businessday.TimeSnippetImpl.TimeSnippetBuilder;
import com.adcubum.timerecording.core.work.businessday.builder.BusinessDayIncrementBuilder;
import com.adcubum.timerecording.data.ticket.ticketactivity.factor.TicketActivityFactory;
import com.adcubum.timerecording.integtest.BaseTestWithSettings;
import com.adcubum.timerecording.integtest.repo.TestBusinessDayRepoConfig;
import com.adcubum.timerecording.jira.data.ticket.factory.TicketFactory;
import com.adcubum.timerecording.ticketbacklog.TicketBacklogSPI;
import com.adcubum.timerecording.work.date.DateTimeFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {TestBusinessDayRepoConfig.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BusinessDayHelperImplIntegrationTest extends BaseTestWithSettings {

   @Test
   void test_AddBookedIncrements_Twice_FindDuplicateAndDontAddAlreadyExisting() {

      // Given
      int timeBetween = 10_000;
      long firstBeginTimestamp = System.currentTimeMillis();
      long firstEndTimestamp = System.currentTimeMillis() + timeBetween;
      String descriptionOfFirst = "Test1";
      String descriptionOfSecond = "Test2";
      TestCaseBuilder tcb = new TestCaseBuilder()
            .addExistingBookedBusinessDayIncrement(BusinessDayIncrementBuilder.of()
                  .withDescription(descriptionOfFirst)
                  .withTicketActivity(TicketActivityFactory.INSTANCE.createNew("test",111))
                  .withTicket(TicketFactory.INSTANCE.dummy("321"))
                  .withId(UUID.randomUUID())
                  .withFlagAsBooked()
                  .withTimeSnippet(TimeSnippetBuilder.of()
                        .withBeginTimeStamp(firstBeginTimestamp)
                        .withEndTimeStamp(firstEndTimestamp)
                        .build())
                  .build())
            .build();

      // When
      // First add a booked increment
      tcb.businessDayHelperImpl.addBookedBusinessDayIncrements(tcb.businessDayIncrements);
      BusinessDay bookedBusinessDay = tcb.businessDayRepository.findBookedBusinessDayByDate(DateTimeFactory.createNew(firstEndTimestamp));

      // Then add a second booked increment, make sure in the end, there are only 2 booked ones
      tcb.businessDayHelperImpl.addBookedBusinessDayIncrements(Arrays.asList(bookedBusinessDay.getIncrements().get(0),
            BusinessDayIncrementBuilder.of()
                  .withDescription(descriptionOfSecond)
                  .withTicketActivity(TicketActivityFactory.INSTANCE.createNew("test",111))
                  .withId(UUID.randomUUID())
                  .withTicket(TicketFactory.INSTANCE.dummy("123"))
                  .withFlagAsBooked()
                  .withTimeSnippet(TimeSnippetBuilder.of()
                        .withBeginTimeStamp(firstEndTimestamp + timeBetween)
                        .withEndTimeStamp(firstEndTimestamp + timeBetween * 2)
                        .build())
                  .build()));

      // Then
      bookedBusinessDay = tcb.businessDayRepository.findById(bookedBusinessDay.getId());
      verify(tcb.bookedBusinessDayDeleter, times(2)).cleanUpBookedBusinessDays();
      assertThat(bookedBusinessDay.getIncrements().size(), is(2));
      assertThat(bookedBusinessDay.getIncrements().get(0).getDescription(), is(descriptionOfFirst));
      assertThat(bookedBusinessDay.getIncrements().get(1).getDescription(), is(descriptionOfSecond));
   }

   private static class TestCaseBuilder {

      private List<BusinessDayIncrement> businessDayIncrements;
      private BusinessDayHelperImpl businessDayHelperImpl;
      private BookedBusinessDayDeleter bookedBusinessDayDeleter;
      private BusinessDayRepositoryImpl businessDayRepository;

      private TestCaseBuilder() {
         this.bookedBusinessDayDeleter = mock(BookedBusinessDayDeleter.class);
         this.businessDayIncrements = new ArrayList<>();
      }

      private TestCaseBuilder addExistingBookedBusinessDayIncrement(BusinessDayIncrement businessDayIncrement) {
         this.businessDayIncrements.add(businessDayIncrement);
         return this;
      }

      private TestCaseBuilder build() {
         this.businessDayRepository = new BusinessDayRepositoryImpl();
         this.businessDayHelperImpl = new BusinessDayHelperImpl(businessDayRepository, bookedBusinessDayDeleter);
         return this;
      }
   }
}
