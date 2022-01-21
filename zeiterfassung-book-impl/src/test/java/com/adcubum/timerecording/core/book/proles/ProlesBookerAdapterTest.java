package com.adcubum.timerecording.core.book.proles;

import com.adcubum.librarys.text.res.TextLabel;
import com.adcubum.timerecording.core.book.result.BookResultType;
import com.adcubum.timerecording.core.book.result.BookerResult;
import com.adcubum.timerecording.core.work.businessday.*;
import com.adcubum.timerecording.core.work.businessday.update.callback.impl.BusinessDayIncrementAdd;
import com.adcubum.timerecording.core.work.businessday.update.callback.impl.BusinessDayIncrementAdd.BusinessDayIncrementAddBuilder;
import com.adcubum.timerecording.jira.data.ticket.IssueType;
import com.adcubum.timerecording.jira.data.ticket.Ticket;
import com.adcubum.timerecording.jira.data.ticket.TicketActivity;
import com.adcubum.timerecording.jira.data.ticket.TicketAttrs;
import com.zeiterfassung.web.common.book.record.BookRecord;
import com.zeiterfassung.web.common.book.record.BookRecordEntry;
import com.zeiterfassung.web.common.book.record.errorhandling.ExceptionEntry;
import com.zeiterfassung.web.proles.book.ProlesBooker;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

class ProlesBookerAdapterTest {

   @Test
   void testBookHappyFlow() {
      // Given
     TestCaseBuilder tcb = new TestCaseBuilder()
             .withBookedTicketNrs("ABC-123", "ABC-441")
             .withBusinessDayIncrementAdd(new BusinessDayIncrementAddBuilder()
                     .withTicket(mockTicket(true, "ABC-123", "nag", "nxt"))
                     .withDescription("Im booked")
                     .withTimeSnippet(createTimeSnippet(50))
                     .withTicketActivity(createTicketActivity("Im booked", 113))
                     .withId(UUID.randomUUID())
                     .build())
             .withBusinessDayIncrementAdd(new BusinessDayIncrementAddBuilder()
                     .withTicket(mockTicket(true, "ABC-441", "nag", "nxt"))
                     .withDescription("Im also booked")
                     .withTimeSnippet(createTimeSnippet(50))
                     .withTicketActivity(createTicketActivity("Im also booked", 111))
                     .withId(UUID.randomUUID())
                     .build())
             .build();

      // When
      BookerResult actualBookerResult = tcb.prolesBookerAdapter.book(tcb.businessDay);

      // Then
      assertThat(actualBookerResult.getBookResultType(), is(BookResultType.SUCCESS));
      assertThat(actualBookerResult.hasBooked(), is(true));
      assertThat(actualBookerResult.getMessage(), is(TextLabel.SUCCESSFULLY_BOOKED_TEXT));
   }

   @Test
   void testTwoIncrementsOneBooked() {
      // Given
     TestCaseBuilder tcb = new TestCaseBuilder()
             .withBookedTicketNrs("ABC-441")
             .withBusinessDayIncrementAdd(new BusinessDayIncrementAddBuilder()
                     .withTicket(mockTicket(false, "ABC-123", "nag", "nxt"))
                     .withDescription("Im booked")
                     .withTimeSnippet(createTimeSnippet(50))
                     .withTicketActivity(createTicketActivity("Im booked", 113))
                     .withId(UUID.randomUUID())
                     .build())
             .withBusinessDayIncrementAdd(new BusinessDayIncrementAddBuilder()
                     .withTicket(mockTicket(true, "ABC-441", "nag", "nxt"))
                     .withDescription("Im also booked")
                     .withTimeSnippet(createTimeSnippet(50))
                     .withTicketActivity(createTicketActivity("Im also booked", 111))
                     .withId(UUID.randomUUID())
                     .build())
             .build();

      // When
      BookerResult actualBookerResult = tcb.prolesBookerAdapter.book(tcb.businessDay);

      // Then
      assertThat(actualBookerResult.getBookResultType(), is(BookResultType.PARTIAL_SUCCESS_WITH_NON_BOOKABLE));
      assertThat(actualBookerResult.hasBooked(), is(true));
      assertThat(actualBookerResult.getMessage().startsWith(TextLabel.PARTIAL_SUCCESSFULLY_BOOKED_TEXT), is(true));
   }

   private TicketActivity createTicketActivity(String activityName, int activityCode) {
      return new TicketActivity() {
         @Override
         public String getActivityName() {
            return activityName;
         }

         @Override
         public int getActivityCode() {
            return activityCode;
         }

         @Override
         public boolean isDummy() {
            return false;
         }
      };
   }

   private static Ticket mockTicket(boolean isBookable, String ticketNr, String project, String customer) {
      Ticket ticket = mock(Ticket.class);
      TicketAttrs attrs = mock(TicketAttrs.class);
      when(attrs.getIssueType()).thenReturn(IssueType.BUG);
      when(attrs.getProjectDesc()).thenReturn(project);
      when(attrs.getThema()).thenReturn(customer);
      when(attrs.getNr()).thenReturn(ticketNr);
      when(ticket.getTicketAttrs()).thenReturn(attrs);
      when(ticket.getNr()).thenReturn(ticketNr);
      when(ticket.isBookable()).thenReturn(isBookable);
      return ticket;
   }

   private TimeSnippet createTimeSnippet(int timeBetweenBeginAndEnd) {
      return TimeSnippetImpl.TimeSnippetBuilder
              .of()
              .withBeginTimeStamp(timeBetweenBeginAndEnd)
              .withEndTimeStamp(System.currentTimeMillis() + timeBetweenBeginAndEnd + 10)
              .build();
   }

   private static class TestCaseBuilder {
      private ProlesBookerAdapter prolesBookerAdapter;

      private ProlesServiceCodeAdapter jiraServiceCodeAdapter;

      private BusinessDay businessDay;
      private List<BusinessDayIncrementAdd> businessDayIncrementAdds;
      private List<String> bookedTicketNrs;
      private List<String> alreadyBookedBusinessDayIncrements;
      private ProlesBooker prolesBooker;

      private TestCaseBuilder() {
         this.businessDayIncrementAdds = new ArrayList<>();
         this.businessDay = spy(new BusinessDayImpl());
         this.bookedTicketNrs = new ArrayList<>();
         this.alreadyBookedBusinessDayIncrements = new ArrayList<>();
      }

      public TestCaseBuilder withBusinessDayIncrementAdd(BusinessDayIncrementAdd businessDayIncrementAdd) {
         this.businessDayIncrementAdds.add(businessDayIncrementAdd);
         return this;
      }

      public TestCaseBuilder withProlesServiceCodeAdapter(ProlesServiceCodeAdapter prolesServiceCodeAdapter) {
         this.jiraServiceCodeAdapter = prolesServiceCodeAdapter;
         return this;
      }

      private TestCaseBuilder build() {
         addBusinessIncrements();

         this.prolesBooker = mock(ProlesBooker.class);

         List<BookRecordEntry> bookRecordEntries = new ArrayList<>();
         for (String bookedTicketNr : bookedTicketNrs) {
            BusinessDayIncrement businessDayIncrement1 = businessDay.getIncrements().stream().filter(businessDayIncrement -> businessDayIncrement.getTicket().getNr().equals(bookedTicketNr)).findFirst().orElse(null);
            if (businessDayIncrement1 != null){
               bookRecordEntries.add(new TestBookRecordEntry(true, businessDayIncrement1.getId().toString()));
            }
         }

         BookRecord bookRecordResult = new TestBookerRecord(bookRecordEntries);
         when(prolesBooker.bookRecords(any())).thenReturn(bookRecordResult);
         this.prolesBookerAdapter = spy(new ProlesBookerAdapter(prolesBooker));
         return this;
      }

      private void addBusinessIncrements() {
         for (BusinessDayIncrementAdd businessDayIncrementAdd : businessDayIncrementAdds) {
            this.businessDay = this.businessDay.addBusinessIncrement(businessDayIncrementAdd);
         }
         for (BusinessDayIncrement increment : businessDay.getIncrements()) {
            if (alreadyBookedBusinessDayIncrements.contains(increment.getTicket().getNr())) {
               businessDay = businessDay.flagBusinessDayIncrementAsBooked(increment.getId());
            }
         }
      }

      public TestCaseBuilder withBookedTicketNrs(String... bookedTicketNrs) {
         this.bookedTicketNrs = Arrays.asList(bookedTicketNrs);
         return this;
      }

      public TestCaseBuilder withAlreadyBOokedBusinessDayIncrements(String... bookedTicketNrs) {
         this.alreadyBookedBusinessDayIncrements = Arrays.asList(bookedTicketNrs);
         return this;
      }
   }

   private static class TestBookRecordEntry implements BookRecordEntry {

      private boolean isBooked;
      private String externalId;

      public TestBookRecordEntry(boolean isBooked, String externalId) {
         this.isBooked = isBooked;
         this.externalId = externalId;
      }

      @Override
      public String getActivity() {
         return null;
      }

      @Override
      public String getAmountOfHours() {
         return null;
      }

      @Override
      public String getDescription() {
         return null;
      }

      @Override
      public String getDate() {
         return null;
      }

      @Override
      public Optional<ExceptionEntry> getExceptionEntry() {
         return Optional.empty();
      }

      @Override
      public boolean getIsBooked() {
         return isBooked;
      }

      @Override
      public String getExternalId() {
         return externalId;
      }

      @Override
      public String getDescriptionShort() {
         return null;
      }

      @Override
      public void flagAsBooked() {
         this.isBooked = true;
      }

      @Override
      public void setErrorEntry(ExceptionEntry exceptionEntry) {

      }
   }

   private static class TestBookerRecord implements BookRecord {

      private List<BookRecordEntry> bookerRecordEntries;

      public TestBookerRecord(List<BookRecordEntry> bookerRecordEntries) {
         this.bookerRecordEntries = bookerRecordEntries;
      }

      @Override
      public List<BookRecordEntry> getBookerRecordEntries() {
         return bookerRecordEntries;
      }
   }

}