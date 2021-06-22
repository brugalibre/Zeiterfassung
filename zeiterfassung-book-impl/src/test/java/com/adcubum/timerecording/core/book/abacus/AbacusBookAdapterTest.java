package com.adcubum.timerecording.core.book.abacus;

import static java.util.Objects.nonNull;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.adcubum.j2a.abacusconnector.ProjectBookingBean;
import com.adcubum.j2a.zeiterfassung.AbacusBookingConnector;
import com.adcubum.librarys.text.res.TextLabel;
import com.adcubum.timerecording.core.book.result.BookResultType;
import com.adcubum.timerecording.core.book.result.BookerResult;
import com.adcubum.timerecording.core.work.businessday.BusinessDayImpl;
import com.adcubum.timerecording.core.work.businessday.BusinessDayIncrement;
import com.adcubum.timerecording.core.work.businessday.TimeSnippet;
import com.adcubum.timerecording.core.work.businessday.TimeSnippetFactory;
import com.adcubum.timerecording.core.work.businessday.update.callback.impl.BusinessDayIncrementAdd;
import com.adcubum.timerecording.core.work.businessday.update.callback.impl.BusinessDayIncrementAdd.BusinessDayIncrementAddBuilder;
import com.adcubum.timerecording.jira.data.ticket.IssueType;
import com.adcubum.timerecording.jira.data.ticket.Ticket;
import com.adcubum.timerecording.jira.data.ticket.TicketAttrs;
import com.adcubum.timerecording.security.login.auth.AuthenticationService;
import com.adcubum.timerecording.security.login.auth.TestAuthenticationService;
import com.adcubum.timerecording.security.login.auth.usercredentials.UserCredentialsAuthenticator;
import com.adcubum.timerecording.work.date.Time;
import com.adcubum.timerecording.work.date.TimeFactory;

class AbacusBookAdapterTest {

   private static final String USER_PWD = "blabb";
   private static final String USERNAME = "username";

   @Test
   void testInitConnector_Failed() {

      // Given
      AbacusBookingConnector abacusBookingConnector = mock(AbacusBookingConnector.class);
      String username = USERNAME;
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withAbacusBookingConnector(abacusBookingConnector)
            .withAbacusServiceCodeAdapter(mock(AbacusServiceCodeAdapter.class))
            .withUsername(username)
            .withExceptionWhileFetchingEmployeeNumber()
            .build();

      // When
      tcb.authenticationService.doUserAuthentication(username, USER_PWD.toCharArray());

      // Then
      verify(tcb.abacusBookingConnector).fetchEmployeeNumber(eq(username));
   }

   @Test
   void testInitConnector_Success() {

      // Given
      String username = USERNAME;
      AbacusBookingConnector abacusBookingConnector = mock(AbacusBookingConnector.class);
      when(abacusBookingConnector.fetchEmployeeNumber(eq(username))).thenReturn(1324l);
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withAbacusBookingConnector(abacusBookingConnector)
            .withAbacusServiceCodeAdapter(mock(AbacusServiceCodeAdapter.class))
            .withUsername(username)
            .build();

      // When
      tcb.authenticationService.doUserAuthentication(username, USER_PWD.toCharArray());

      // Then
      verify(tcb.abacusBookingConnector).fetchEmployeeNumber(eq(username));
   }

   @Test
   void testBook_PartialSuccess_WithErrorThrown() {

      // Given
      AbacusBookingConnector abacusBookingConnector = mock(AbacusBookingConnector.class);
      String ticketNr = "SYRIUS-654";
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withAbacusBookingConnector(abacusBookingConnector)
            .withAbacusServiceCodeAdapter(mock(AbacusServiceCodeAdapter.class))
            .withBusinessDayIncrementAdd(new BusinessDayIncrementAddBuilder()
                  .withTicket(mockTicket(true))
                  .withTimeSnippet(createTimeSnippet(50))
                  .build())
            .withBusinessDayIncrementAdd(new BusinessDayIncrementAddBuilder()
                  .withTicket(mockTicket(true, ticketNr))
                  .withTimeSnippet(createTimeSnippet(50))
                  .build())
            .withExceptionWhileBooking(ticketNr)
            .build();

      // When
      BookerResult actualBookResult = tcb.abacusBookAdapter.book(tcb.businessDay);

      // Then
      assertThat(actualBookResult.getBookResultType(), is(BookResultType.PARTIAL_SUCCESS_WITH_ERROR));
      verify(tcb.abacusBookingConnector, times(tcb.businessDay.getIncrements().size())).book(any());
   }

   @Test
   void testBook_PartialSuccess_WithBookedAndNonBookeable() {

      // Given
      AbacusBookingConnector abacusBookingConnector = mock(AbacusBookingConnector.class);
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withAbacusBookingConnector(abacusBookingConnector)
            .withAbacusServiceCodeAdapter(mock(AbacusServiceCodeAdapter.class))
            .withBusinessDayIncrementAdd(new BusinessDayIncrementAddBuilder()
                  .withTicket(mockTicket(true))
                  .withTimeSnippet(createTimeSnippet(50))
                  .build())
            .withBusinessDayIncrementAdd(new BusinessDayIncrementAddBuilder()
                  .withTicket(mockTicket(false))
                  .withTimeSnippet(createTimeSnippet(50))
                  .build())
            .build();

      // When
      BookerResult actualBookResult = tcb.abacusBookAdapter.book(tcb.businessDay);

      // Then
      assertThat(actualBookResult.getBookResultType(), is(BookResultType.PARTIAL_SUCCESS_WITH_NON_BOOKABLE));
      verify(tcb.abacusBookingConnector).book(any());
   }

   @Test
   void testBook_Failure() {

      // Given
      AbacusBookingConnector abacusBookingConnector = mock(AbacusBookingConnector.class);
      String ticketNrWhichThrowsException = "65421";
      String ticketNrWhichWasAlreadyBookedBefore = "1324";
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withAbacusBookingConnector(abacusBookingConnector)
            .withAbacusServiceCodeAdapter(mock(AbacusServiceCodeAdapter.class))
            .withBusinessDayIncrementAdd(new BusinessDayIncrementAddBuilder()
                  .withTicket(mockTicket(true, ticketNrWhichThrowsException))
                  .withTimeSnippet(createTimeSnippet(50))
                  .build())
            .withBusinessDayIncrementAdd(new BusinessDayIncrementAddBuilder()
                  .withTicket(mockTicket(true, "1324"))
                  .withTimeSnippet(createTimeSnippet(50))
                  .build())
            .withExceptionWhileBooking(ticketNrWhichThrowsException)
            .withAlreadyChargedTicket(ticketNrWhichWasAlreadyBookedBefore)
            .build();

      // When
      BookerResult actualBookResult = tcb.abacusBookAdapter.book(tcb.businessDay);

      // Then
      assertThat(actualBookResult.getBookResultType(), is(BookResultType.FAILURE));
      assertThat(actualBookResult.hasBooked(), is(true));
      assertThat(actualBookResult.getMessage(), is(TextLabel.BOOKING_FAILED_TEXT));
      verify(tcb.abacusBookingConnector, times(tcb.businessDay.getIncrements().size() - 1)).book(any());
   }

   @Test
   void testBook_Success() {

      // Given
      AbacusBookingConnector abacusBookingConnector = mock(AbacusBookingConnector.class);
      AbacusServiceCodeAdapter abacusServiceCodeAdapter = mock(AbacusServiceCodeAdapter.class);
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withAbacusBookingConnector(abacusBookingConnector)
            .withAbacusServiceCodeAdapter(abacusServiceCodeAdapter)
            .withBusinessDayIncrementAdd(new BusinessDayIncrementAddBuilder()
                  .withTicket(mockTicket(true))
                  .withTimeSnippet(createTimeSnippet(50))
                  .build())
            .build();

      // When
      tcb.authenticationService.doUserAuthentication(USERNAME, USER_PWD.toCharArray());
      BookerResult actualBookResult = tcb.abacusBookAdapter.book(tcb.businessDay);

      // Then
      assertThat(actualBookResult.getBookResultType(), is(BookResultType.SUCCESS));
      assertThat(actualBookResult.hasBooked(), is(true));
      assertThat(actualBookResult.getMessage(), is(TextLabel.SUCCESSFULLY_BOOKED_TEXT));
      verify(tcb.abacusBookingConnector, times(tcb.businessDay.getIncrements().size())).book(any());
      verify(abacusServiceCodeAdapter).init();
   }

   private static Ticket mockTicket(boolean isBookable) {
      return mockTicket(isBookable, "SYRIUS-12345");
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
      Time beginTimeStamp = TimeFactory.createNew(System.currentTimeMillis() + timeBetweenBeginAndEnd);
      TimeSnippet timeSnippet = TimeSnippetFactory.createNew(new Date(beginTimeStamp.getTime()));
      timeSnippet.setBeginTimeStamp(beginTimeStamp);
      timeSnippet.setEndTimeStamp(TimeFactory.createNew(System.currentTimeMillis() + timeBetweenBeginAndEnd + 10));
      return timeSnippet;
   }

   private static class TestCaseBuilder {
      private BusinessDayImpl businessDay;
      private AbacusBookerAdapter abacusBookAdapter;
      private AbacusBookingConnector abacusBookingConnector;
      private List<BusinessDayIncrementAdd> businessDayIncrementAdds;
      private String username;
      private RuntimeException fetchEmployeeException;
      private String chargedTicketNr;
      private String ticketNr2ThrowExceptionDuringBooking;
      private AbacusServiceCodeAdapter abacusServiceCodeAdapter;
      private AuthenticationService authenticationService;

      private TestCaseBuilder() {
         this.businessDayIncrementAdds = new ArrayList<>();
         this.businessDay = spy(new BusinessDayImpl(new Date()));
         createTestAuthenticationService();
      }

      private void createTestAuthenticationService() {
         UserCredentialsAuthenticator userCredentialsAuthenticator = mock(UserCredentialsAuthenticator.class);
         when(userCredentialsAuthenticator.doUserAuthentication(any(), any())).thenReturn(true);
         this.authenticationService = new TestAuthenticationService(userCredentialsAuthenticator);
      }

      public TestCaseBuilder withAlreadyChargedTicket(String chargedTicketNr) {
         this.chargedTicketNr = chargedTicketNr;
         return this;
      }

      public TestCaseBuilder withExceptionWhileBooking(String ticketNr2ThrowExceptionDuringBooking) {
         this.ticketNr2ThrowExceptionDuringBooking = ticketNr2ThrowExceptionDuringBooking;
         return this;
      }

      public TestCaseBuilder withExceptionWhileFetchingEmployeeNumber() {
         this.fetchEmployeeException = new RuntimeException();
         return this;
      }

      public TestCaseBuilder withUsername(String username) {
         this.username = username;
         return this;
      }

      public TestCaseBuilder withBusinessDayIncrementAdd(BusinessDayIncrementAdd businessDayIncrementAdd) {
         this.businessDayIncrementAdds.add(businessDayIncrementAdd);
         return this;
      }

      public TestCaseBuilder withAbacusBookingConnector(AbacusBookingConnector abacusBookingConnector) {
         this.abacusBookingConnector = abacusBookingConnector;
         return this;
      }

      public TestCaseBuilder withAbacusServiceCodeAdapter(AbacusServiceCodeAdapter abacusServiceCodeAdapter) {
         this.abacusServiceCodeAdapter = abacusServiceCodeAdapter;
         return this;
      }

      private TestCaseBuilder build() {
         addBusinessIncrements();
         doThrowWhileFetching();
         this.abacusBookAdapter = spy(new AbacusBookerAdapter(abacusBookingConnector, abacusServiceCodeAdapter));
         authenticationService.registerUserAuthenticatedObservable(abacusBookAdapter);
         doReturn(abacusBookingConnector).when(abacusBookAdapter).createAbacusBookingConnector(any());
         doThrowWhileBooking();
         return this;
      }

      private void doThrowWhileFetching() {
         if (nonNull(fetchEmployeeException)) {
            doThrow(fetchEmployeeException).when(abacusBookingConnector).fetchEmployeeNumber(eq(username));
         }
      }

      private void doThrowWhileBooking() {
         if (nonNull(ticketNr2ThrowExceptionDuringBooking)) {
            for (BusinessDayIncrement businessDayIncrement : businessDay.getIncrements()) {
               String ticketNr = businessDayIncrement.getTicket().getNr();
               if (ticketNr.equals(ticketNr2ThrowExceptionDuringBooking)) {
                  ProjectBookingBean bean = mock(ProjectBookingBean.class);
                  doReturn(bean).when(abacusBookAdapter).map2ProjectBookingBean(eq(businessDayIncrement));
                  doThrow(new RuntimeException()).when(abacusBookingConnector).book(eq(bean));
               }
            }
         }
      }

      private void addBusinessIncrements() {
         for (BusinessDayIncrementAdd businessDayIncrementAdd : businessDayIncrementAdds) {
            this.businessDay.addBusinessIncrement(businessDayIncrementAdd);
         }
         for (BusinessDayIncrement businessDayIncrement : businessDay.getIncrements()) {
            String ticketNr = businessDayIncrement.getTicket().getNr();
            if (ticketNr.equals(chargedTicketNr)) {
               businessDayIncrement.flagAsCharged();
            }
         }
      }
   }
}
