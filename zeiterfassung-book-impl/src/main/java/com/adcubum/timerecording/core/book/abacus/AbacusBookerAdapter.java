package com.adcubum.timerecording.core.book.abacus;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import com.adcubum.j2a.abacusconnector.ProjectBookingBean;
import com.adcubum.j2a.zeiterfassung.AbacusBookingConnector;
import com.adcubum.librarys.text.res.TextLabel;
import com.adcubum.timerecording.core.book.adapter.BookerAdapter;
import com.adcubum.timerecording.core.book.adapter.ServiceCodeAdapter;
import com.adcubum.timerecording.core.book.result.BookResultType;
import com.adcubum.timerecording.core.book.result.BookerResult;
import com.adcubum.timerecording.core.work.businessday.BusinessDay;
import com.adcubum.timerecording.core.work.businessday.BusinessDayIncrement;
import com.adcubum.timerecording.core.work.businessday.TimeSnippet;
import com.adcubum.timerecording.jira.data.ticket.Ticket;
import com.adcubum.timerecording.jira.data.ticket.TicketAttrs;
import com.adcubum.timerecording.security.login.auth.AuthenticationContext;
import com.adcubum.timerecording.security.login.auth.AuthenticationService;
import com.adcubum.timerecording.security.login.auth.init.UserAuthenticatedObservable;

/**
 * The {@link AbacusBookerAdapter} books directly into the abacus interface
 * 
 * @author Dominic
 *
 */
public class AbacusBookerAdapter implements BookerAdapter, UserAuthenticatedObservable {

   private static final Logger LOG = Logger.getLogger(AbacusBookerAdapter.class);
   private AbacusBookingConnector abacusBookingConnector;
   private AbacusServiceCodeAdapter serviceCodeAdapter;
   private long employeeNumber;

   public AbacusBookerAdapter() {
      AuthenticationService.INSTANCE.registerUserAuthenticatedObservable(this);
      this.abacusBookingConnector = new AbacusBookingConnector("", "", "http://", -1);// explizit invalid arguments
      this.serviceCodeAdapter = new AbacusServiceCodeAdapter(abacusBookingConnector);
   }

   /**
    * Constructor for testing purpose only!
    * 
    * @param abacusBookingConnector
    *        the {@link AbacusBookingConnector}*
    */
   AbacusBookerAdapter(AbacusBookingConnector abacusBookingConnector) {
      this(abacusBookingConnector, new AbacusServiceCodeAdapter(abacusBookingConnector));
   }

   /**
    * Package private constructor for testing purpose only
    * 
    * @param abacusBookingConnector
    *        the {@link AbacusBookingConnector}
    * @param serviceCodeAdapter
    *        the {@link AbacusServiceCodeAdapter}
    */
   AbacusBookerAdapter(AbacusBookingConnector abacusBookingConnector, AbacusServiceCodeAdapter serviceCodeAdapter) {
      this.abacusBookingConnector = abacusBookingConnector;
      this.serviceCodeAdapter = serviceCodeAdapter;
   }

   AbacusBookingConnector createAbacusBookingConnector(AuthenticationContext authenticationContext) {
      return new AbacusBookingConnector(authenticationContext.getUsername(), String.valueOf(authenticationContext.getUserPw()),
            AbacusConst.DEFAULT_END_POINT, AbacusConst.DEFAULT_MANDANT);
   }

   private void init(String username) {
      serviceCodeAdapter.init();
      fetchEmployeeNumber(username);
   }

   @Override
   public void userAuthenticated(AuthenticationContext authenticationContext) {
      abacusBookingConnector = createAbacusBookingConnector(authenticationContext);
      init(authenticationContext.getUsername());
   }

   @Override
   public ServiceCodeAdapter getServiceCodeAdapter() {
      return serviceCodeAdapter;
   }

   @Override
   public BookerResult book(BusinessDay businessDay) {
      List<BusinessDayIncrement> currentIncrements2Book = getNotBookedIncrements(businessDay); // We need the increments, which were not booked at the very begining in order to determine the success of the current booking
      bookBusinessDay(businessDay);
      return createBookResult(currentIncrements2Book);
   }

   private void bookBusinessDay(BusinessDay businessDay) {
      getRelevantIncrements(businessDay).stream()
            .forEach(this::bookBusinessDayInc);
   }

   private static BookerResult createBookResult(List<BusinessDayIncrement> currentIncrements2Book) {
      BookResultType bookResultType = evalBookResultType(currentIncrements2Book);
      String message = evalResultMessage(currentIncrements2Book, bookResultType);
      return new AbacusBookerAdapterResult(true, bookResultType, message);
   }

   private void bookBusinessDayInc(BusinessDayIncrement businessDayIncrement) {
      try {
         ProjectBookingBean projectBookingBean = map2ProjectBookingBean(businessDayIncrement);
         abacusBookingConnector.book(projectBookingBean);
         businessDayIncrement.flagAsCharged();
      } catch (Exception e) {
         LOG.error("Error occurred during booking of ticket '" + businessDayIncrement.getTicket() + "!'", e);
      }
   }

   private static String evalResultMessage(List<BusinessDayIncrement> businessDayIncrements, BookResultType bookResultType) {
      StringBuilder stringBuilder = new StringBuilder();
      switch (bookResultType) {
         case SUCCESS:
            stringBuilder.append(TextLabel.SUCCESSFULLY_BOOKED_TEXT);
            break;
         case PARTIAL_SUCCESS_WITH_ERROR:
            stringBuilder.append(TextLabel.PARTIAL_SUCCESSFULLY_BOOKED_TEXT);
            break;
         case PARTIAL_SUCCESS_WITH_NON_BOOKABLE:
            stringBuilder.append(TextLabel.PARTIAL_SUCCESSFULLY_BOOKED_TEXT);
            for (BusinessDayIncrement businessDayIncrement : getNotBookableIncrements(businessDayIncrements)) {
               stringBuilder.append("\n");
               stringBuilder.append(String.format(TextLabel.NOT_BOOKABLE_TICKETS_FOUND_TEXT, businessDayIncrement.getTicket().getNr()));
            }
            break;
         case FAILURE:
            stringBuilder.append(TextLabel.BOOKING_FAILED_TEXT);
            break;
         default:
            throw new IllegalStateException("Unknown state '" + bookResultType + "'");
      }
      return stringBuilder.toString();
   }

   private static BookResultType evalBookResultType(List<BusinessDayIncrement> currentIncrements2Book) {
      BookResultType bookResultType;
      if (wasBookingCompleteSuccessfull(currentIncrements2Book)) {
         bookResultType = BookResultType.SUCCESS;
      } else {
         List<BusinessDayIncrement> notBookableIncrements = getNotBookableIncrements(currentIncrements2Book);
         if (wasBookingFailure(currentIncrements2Book)) {
            bookResultType = BookResultType.FAILURE;
         } else if (hasNotBookeableInc(notBookableIncrements)) {
            bookResultType = BookResultType.PARTIAL_SUCCESS_WITH_NON_BOOKABLE;
         } else {
            bookResultType = BookResultType.PARTIAL_SUCCESS_WITH_ERROR;
         }
      }
      return bookResultType;
   }

   private static boolean hasNotBookeableInc(List<BusinessDayIncrement> notBookableIncrements) {
      return !notBookableIncrements.isEmpty();// we implicitely have booked increments, otherwise it would be a full 'failure'
   }

   ProjectBookingBean map2ProjectBookingBean(BusinessDayIncrement businessDayIncrement) {
      Ticket ticket = businessDayIncrement.getTicket();
      TicketAttrs attrs = ticket.getTicketAttrs();

      TimeSnippet timeSnippet = businessDayIncrement.getCurrentTimeSnippet();
      Calendar timeFrom = new GregorianCalendar();
      timeFrom.setTime(new Date(timeSnippet.getBeginTimeStamp().getTime()));
      Calendar timeTo = new GregorianCalendar();
      timeTo.setTime(new Date(timeSnippet.getEndTimeStamp().getTime()));

      String issueType = attrs.getIssueType().name();
      return new ProjectBookingBean(employeeNumber, businessDayIncrement.getDate(), attrs.getProjectNr(), businessDayIncrement.getChargeType(),
            timeFrom, timeTo, null, businessDayIncrement.getDescription(), attrs.getExternalNr(), attrs.getNr(), attrs.getProjectCostUnit(),
            issueType, attrs.getId(), attrs.getThema(), attrs.getSubthema(), attrs.getBusinessTeamPlaning(), attrs.getPlaningId(),
            attrs.getImplementationPackage(), attrs.getSyriusExtension(), attrs.getSyriusRelease(), attrs.getEpicNr());
   }

   private void fetchEmployeeNumber(String username) {
      try {
         this.employeeNumber = abacusBookingConnector.fetchEmployeeNumber(username);
      } catch (Exception e) {
         LOG.error("Error occurred during evaluating the employe-number for name '" + username + "!'", e);
      }
   }

   private static boolean wasBookingCompleteSuccessfull(List<BusinessDayIncrement> notBookedIncrements) {
      return wasBookingSuccessOrFailure(notBookedIncrements, BusinessDayIncrement::isCharged);
   }

   private static boolean wasBookingFailure(List<BusinessDayIncrement> notBookedIncrements) {
      Predicate<BusinessDayIncrement> isCharged = BusinessDayIncrement::isCharged;
      return wasBookingSuccessOrFailure(notBookedIncrements, isCharged.negate());
   }

   private static boolean wasBookingSuccessOrFailure(List<BusinessDayIncrement> notBookedIncrements,
         Predicate<BusinessDayIncrement> isChargedOrNotCharged) {
      return notBookedIncrements.stream()
            .allMatch(isChargedOrNotCharged);
   }

   private static List<BusinessDayIncrement> getRelevantIncrements(BusinessDay businessDay) {
      Predicate<BusinessDayIncrement> isBookable = BusinessDayIncrement::isBookable;
      Predicate<BusinessDayIncrement> isCharged = BusinessDayIncrement::isCharged;
      return filterBDIncrements(businessDay.getIncrements(), isBookable
            .and(isCharged.negate()));
   }

   private static List<BusinessDayIncrement> getNotBookedIncrements(BusinessDay businessDay) {
      Predicate<BusinessDayIncrement> isCharged = BusinessDayIncrement::isCharged;
      return filterBDIncrements(businessDay.getIncrements(), isCharged.negate());
   }

   private static List<BusinessDayIncrement> getNotBookableIncrements(List<BusinessDayIncrement> businessDayIncrements) {
      Predicate<BusinessDayIncrement> isBookable = BusinessDayIncrement::isBookable;
      return filterBDIncrements(businessDayIncrements, isBookable.negate());
   }

   private static List<BusinessDayIncrement> filterBDIncrements(List<BusinessDayIncrement> businessDayIncrements,
         Predicate<BusinessDayIncrement> predicate) {
      return businessDayIncrements.stream()
            .filter(predicate)
            .collect(Collectors.toList());
   }
}
