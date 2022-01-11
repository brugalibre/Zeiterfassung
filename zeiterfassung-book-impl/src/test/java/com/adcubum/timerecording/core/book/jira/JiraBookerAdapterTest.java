package com.adcubum.timerecording.core.book.jira;

import com.adcubum.librarys.text.res.TextLabel;
import com.adcubum.timerecording.core.book.result.BookResultType;
import com.adcubum.timerecording.core.book.result.BookerResult;
import com.adcubum.timerecording.core.work.businessday.*;
import com.adcubum.timerecording.core.work.businessday.update.callback.impl.BusinessDayIncrementAdd;
import com.adcubum.timerecording.jira.data.ticket.IssueType;
import com.adcubum.timerecording.jira.data.ticket.Ticket;
import com.adcubum.timerecording.jira.data.ticket.TicketActivity;
import com.adcubum.timerecording.jira.data.ticket.TicketAttrs;
import com.adcubum.timerecording.jira.jiraapi.configuration.JiraApiConfiguration;
import com.adcubum.timerecording.security.login.auth.AuthenticationService;
import com.adcubum.timerecording.security.login.auth.TestAuthenticationService;
import com.adcubum.timerecording.security.login.auth.usercredentials.UserCredentialsAuthenticator;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static com.adcubum.timerecording.core.work.businessday.update.callback.impl.BusinessDayIncrementAdd.BusinessDayIncrementAddBuilder;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class JiraBookerAdapterTest {
   private static final String USER_PWD = "blabb";
   private static final String USERNAME = "username";

   @Test
   void testBook_TwoIncrements_BothAlreadyBooked() {

      // Given
      TestCaseBuilder tcb = new TestCaseBuilder()
              .withBookedTicketNrs("ABC-123")
              .withJiraServiceCodeAdapter(mock(JiraServiceCodeAdapter.class))
              .withBusinessDayIncrementAdd(new BusinessDayIncrementAddBuilder()
                      .withTicket(mockTicket(true, "ABC-123"))
                      .withDescription("Im booked")
                      .withTimeSnippet(createTimeSnippet(50))
                      .withTicketActivity(createTicketActivity("Im booked", 113))
                      .withId(UUID.randomUUID())
                      .build())
              .withBusinessDayIncrementAdd(new BusinessDayIncrementAddBuilder()
                      .withTicket(mockTicket(true, "ABC-114"))
                      .withDescription("Im booked")
                      .withTimeSnippet(createTimeSnippet(50))
                      .withTicketActivity(createTicketActivity("Im not booked", 111))
                      .withId(UUID.randomUUID())
                      .build())
              .withAlreadyBOokedBusinessDayIncrements("ABC-123", "ABC-114")
              .build();

      // When
      BookerResult actualBookResult = tcb.jiraBookerAdapter.book(tcb.businessDay);

      // Then
      assertThat(actualBookResult.getBookResultType(), is(BookResultType.NOT_BOOKED));
      assertThat(actualBookResult.hasBooked(), is(false));
      verify(tcb.jiraApiWorklogCreator, never()).createWorklog(any());
   }

   @Test
   void testBook_PartialSuccess_AllBookable_OneNotBookedDueToErrorOnApi() {

      // Given
      TestCaseBuilder tcb = new TestCaseBuilder()
              .withBookedTicketNrs("ABC-123")
              .withJiraServiceCodeAdapter(mock(JiraServiceCodeAdapter.class))
              .withBusinessDayIncrementAdd(new BusinessDayIncrementAddBuilder()
                      .withTicket(mockTicket(true, "ABC-123"))
                      .withDescription("Im booked")
                      .withTimeSnippet(createTimeSnippet(50))
                      .withTicketActivity(createTicketActivity("Im booked", 113))
                      .withId(UUID.randomUUID())
                      .build())
              .withBusinessDayIncrementAdd(new BusinessDayIncrementAddBuilder()
                      .withTicket(mockTicket(true, "ABC-114"))
                      .withDescription("Im not booked")
                      .withTimeSnippet(createTimeSnippet(50))
                      .withTicketActivity(createTicketActivity("Im not booked", 111))
                      .withId(UUID.randomUUID())
                      .build())
              .build();

      // When
      BookerResult actualBookResult = tcb.jiraBookerAdapter.book(tcb.businessDay);

      // Then
      assertThat(actualBookResult.getBookResultType(), is(BookResultType.PARTIAL_SUCCESS_WITH_ERROR));
      assertThat(actualBookResult.hasBooked(), is(true));
      verify(tcb.jiraApiWorklogCreator, times(2)).createWorklog(any());
   }

   @Test
   void testBook_PartialSuccess_TwoIncrementsOnlyOneBookable() {

      // Given
      TestCaseBuilder tcb = new TestCaseBuilder()
              .withBookedTicketNrs("ABC-123")
              .withJiraServiceCodeAdapter(mock(JiraServiceCodeAdapter.class))
              .withBusinessDayIncrementAdd(new BusinessDayIncrementAddBuilder()
                      .withTicket(mockTicket(true, "ABC-123"))
                      .withDescription("Im booked")
                      .withTimeSnippet(createTimeSnippet(50))
                      .withTicketActivity(createTicketActivity("Im booked", 113))
                      .withId(UUID.randomUUID())
                      .build())
              .withBusinessDayIncrementAdd(new BusinessDayIncrementAddBuilder()
                      .withTicket(mockTicket(false, "ABC-114"))
                      .withDescription("Im bookable and therefore not booked")
                      .withTimeSnippet(createTimeSnippet(50))
                      .withTicketActivity(createTicketActivity("Im bookable and therefore not booked", 111))
                      .withId(UUID.randomUUID())
                      .build())
              .build();

      // When
      BookerResult actualBookResult = tcb.jiraBookerAdapter.book(tcb.businessDay);

      // Then
      assertThat(actualBookResult.getBookResultType(), is(BookResultType.PARTIAL_SUCCESS_WITH_NON_BOOKABLE));
      assertThat(actualBookResult.hasBooked(), is(true));
      verify(tcb.jiraApiWorklogCreator, times(1)).createWorklog(any());
   }

   @Test
   void testBook_TwoIncrementsNotASingleOneBooked_Failure() {

      // Given
      TestCaseBuilder tcb = new TestCaseBuilder()
              .withBookedTicketNrs("ABC-123")
              .withJiraServiceCodeAdapter(mock(JiraServiceCodeAdapter.class))
              .withBusinessDayIncrementAdd(new BusinessDayIncrementAddBuilder()
                      .withTicket(mockTicket(true, "ABC-113"))
                      .withDescription("Im booked")
                      .withTimeSnippet(createTimeSnippet(50))
                      .withTicketActivity(createTicketActivity("Im booked", 113))
                      .withId(UUID.randomUUID())
                      .build())
              .withBusinessDayIncrementAdd(new BusinessDayIncrementAddBuilder()
                      .withTicket(mockTicket(true, "ABC-114"))
                      .withDescription("Im not booked")
                      .withTimeSnippet(createTimeSnippet(50))
                      .withTicketActivity(createTicketActivity("Im not booked", 111))
                      .withId(UUID.randomUUID())
                      .build())
              .build();

      // When
      BookerResult actualBookResult = tcb.jiraBookerAdapter.book(tcb.businessDay);

      // Then
      assertThat(actualBookResult.getBookResultType(), is(BookResultType.FAILURE));
      assertThat(actualBookResult.hasBooked(), is(true));
      assertThat(actualBookResult.getMessage(), is(TextLabel.BOOKING_FAILED_TEXT));
   }

   @Test
   void testBook_Success() {

      TestCaseBuilder tcb = new TestCaseBuilder()
              .withBookedTicketNrs("ABC-123", "ABC-441")
              .withJiraServiceCodeAdapter(mock(JiraServiceCodeAdapter.class))
              .withBusinessDayIncrementAdd(new BusinessDayIncrementAddBuilder()
                      .withTicket(mockTicket(true, "ABC-123"))
                      .withDescription("Im booked")
                      .withTimeSnippet(createTimeSnippet(50))
                      .withTicketActivity(createTicketActivity("Im booked", 113))
                      .withId(UUID.randomUUID())
                      .build())
              .withBusinessDayIncrementAdd(new BusinessDayIncrementAddBuilder()
                      .withTicket(mockTicket(true, "ABC-441"))
                      .withDescription("Im also booked")
                      .withTimeSnippet(createTimeSnippet(50))
                      .withTicketActivity(createTicketActivity("Im also booked", 111))
                      .withId(UUID.randomUUID())
                      .build())
              .build();

      // When
      tcb.authenticationService.doUserAuthentication(USERNAME, USER_PWD.toCharArray());
      BookerResult actualBookResult = tcb.jiraBookerAdapter.book(tcb.businessDay);

      // Then
      assertThat(actualBookResult.getBookResultType(), is(BookResultType.SUCCESS));
      assertThat(actualBookResult.hasBooked(), is(true));
      assertThat(actualBookResult.getMessage(), is(TextLabel.SUCCESSFULLY_BOOKED_TEXT));
      verify(tcb.jiraApiWorklogCreator, times(2)).createWorklog(any());
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

   private static Ticket mockTicket(boolean isBookable, String ticketNr) {
      Ticket ticket = mock(Ticket.class);
      TicketAttrs attrs = mock(TicketAttrs.class);
      when(attrs.getIssueType()).thenReturn(IssueType.BUG);
      when(attrs.getProjectNr()).thenReturn(1234l);
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
      private JiraBookerAdapter jiraBookerAdapter;
      private TestJiraApiWorklogCreatorImpl jiraApiWorklogCreator;

      private JiraServiceCodeAdapter jiraServiceCodeAdapter;
      private AuthenticationService authenticationService;

      private BusinessDay businessDay;
      private List<BusinessDayIncrementAdd> businessDayIncrementAdds;
      private List<String> bookedTicketNrs;
      private List<String> alreadyBookedBusinessDayIncrements;

      private TestCaseBuilder() {
         this.businessDayIncrementAdds = new ArrayList<>();
         this.businessDay = spy(new BusinessDayImpl());
         this.bookedTicketNrs = new ArrayList<>();
         this.alreadyBookedBusinessDayIncrements = new ArrayList<>();
         createTestAuthenticationService();
      }

      private void createTestAuthenticationService() {
         UserCredentialsAuthenticator userCredentialsAuthenticator = mock(UserCredentialsAuthenticator.class);
         when(userCredentialsAuthenticator.doUserAuthentication(any(), any())).thenReturn(true);
         this.authenticationService = new TestAuthenticationService(userCredentialsAuthenticator);
      }

      public TestCaseBuilder withBusinessDayIncrementAdd(BusinessDayIncrementAdd businessDayIncrementAdd) {
         this.businessDayIncrementAdds.add(businessDayIncrementAdd);
         return this;
      }

      public TestCaseBuilder withJiraServiceCodeAdapter(JiraServiceCodeAdapter jiraServiceCodeAdapter) {
         this.jiraServiceCodeAdapter = jiraServiceCodeAdapter;
         return this;
      }

      private TestCaseBuilder build() {
         addBusinessIncrements();
         JiraApiConfiguration jiraApiConfiguration = mock(JiraApiConfiguration.class);

         this.jiraApiWorklogCreator = spy(new TestJiraApiWorklogCreatorImpl(this.bookedTicketNrs));
         this.jiraBookerAdapter = spy(new JiraBookerAdapter(jiraServiceCodeAdapter, () -> jiraApiConfiguration, (a, b, c) -> jiraApiWorklogCreator));
         authenticationService.registerUserAuthenticatedObservable(jiraBookerAdapter);
         return this;
      }

      private void addBusinessIncrements() {
         for (BusinessDayIncrementAdd businessDayIncrementAdd : businessDayIncrementAdds) {
            this.businessDay = this.businessDay.addBusinessIncrement(businessDayIncrementAdd);
         }
         for (BusinessDayIncrement increment : businessDay.getIncrements()) {
            if (alreadyBookedBusinessDayIncrements.contains(increment.getTicket().getNr())){
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
}