package com.adcubum.timerecording.core.book.coolguys;

import com.adcubum.timerecording.core.book.adapter.BookerAdapter;
import com.adcubum.timerecording.core.book.adapter.ServiceCodeAdapter;
import com.adcubum.timerecording.core.book.coolguys.exception.ChargeException;
import com.adcubum.timerecording.core.book.result.BookerResult;
import com.adcubum.timerecording.core.importexport.out.businessday.BusinessDayExporter;
import com.adcubum.timerecording.core.work.businessday.BusinessDay;
import com.adcubum.timerecording.core.work.businessday.BusinessDayIncrement;
import com.adcubum.timerecording.jira.data.ticket.Ticket;
import com.adcubum.timerecording.security.login.auth.AuthenticationContext;
import com.adcubum.timerecording.security.login.auth.AuthenticationService;
import com.adcubum.timerecording.security.login.auth.init.UserAuthenticatedObservable;
import com.coolguys.turbo.Booker;

import java.util.List;
import java.util.function.Supplier;

/**
 * Is responsible for booking the given {@link BusinessDay}.
 * 
 * @author Dominic
 */
public class BookerHelper implements BookerAdapter, UserAuthenticatedObservable {

   private ChargeType chargeType;
   private Supplier<char[]> userPwdSupplier;
   private String username;
   private Object lock;

   public BookerHelper() {
      this.chargeType = new ChargeType();
      this.username = "";
      this.userPwdSupplier = () -> new char[] {};
      this.lock = new Object[] {};
      }

   @Override
   public void init() {
      AuthenticationService.INSTANCE.registerUserAuthenticatedObservable(this);
   }

   @Override
   public void userAuthenticated(AuthenticationContext authenticationContext) {
      this.username = authenticationContext.getUsername();
      this.userPwdSupplier = authenticationContext::getUserPw; // still evil but on the other hand still better than saving it plain text.. 
   }

   @Override
   public ServiceCodeAdapter getServiceCodeAdapter() {
      return chargeType;
   }

   /**
    * Collects from each {@link BusinessDayIncrement} the content to book and calls
    * finally the {@link Booker#bookList(List)}. Additionally all booked
    * {@link BusinessDayIncrement} are flagged as charged
    *
    * @see Booker#bookList(List)
    */
   @Override
   public BookerResult book(BusinessDay businessDay) {
      List<String> content2Charge = createBookContent(businessDay);
      bookInternal(content2Charge);
      BusinessDay bookedBusinessDay = businessDay.flagBusinessDayAsBooked();
      return new BookerHelperResult(bookedBusinessDay);
   }

   /**
    * He {@link BookerHelper} books via the adc-jira plugin and therefore a jira-issue aka {@link com.adcubum.timerecording.jira.data.ticket.Ticket}
    * needs to have project-nr. configured
    *
    * @param ticket the Ticket to verify
    * @return <code>true</code> if this certain {@link Ticket} can be booked by this {@link BookerAdapter} or <code>false</code> if not
    */
   @Override
   public boolean isTicketBookable(Ticket ticket) {
      return ticket.getTicketAttrs().getProjectNr() > 0;
   }

   /*
    * Does the actual charging
    */
   private void bookInternal(List<String> content) {
      try {
         Booker booker = new Booker(username, String.valueOf(userPwdSupplier.get()));
         booker.bookList(content);
      } catch (Exception e) {
         e.printStackTrace();
         throw new ChargeException(e);
      }
   }

   /*
    * Collects the data which has to be charged and exports it into a file which is
    * later used by the Turbo-Bucher
    */
   private List<String> createBookContent(BusinessDay businessDay) {
      synchronized (lock) {
         return BusinessDayExporter.INSTANCE.collectContent4Booking(businessDay);
      }
   }
}
