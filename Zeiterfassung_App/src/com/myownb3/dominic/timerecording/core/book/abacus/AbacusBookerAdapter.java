package com.myownb3.dominic.timerecording.core.book.abacus;

import static com.myownb3.dominic.timerecording.settings.common.Const.USER_NAME_PW_VALUE_KEY;
import static com.myownb3.dominic.timerecording.settings.common.Const.USER_NAME_VALUE_KEY;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import com.adcubum.j2a.abacusconnector.ProjectBookingBean;
import com.adcubum.j2a.zeiterfassung.AbacusBookingConnector;
import com.myownb3.dominic.librarys.text.res.TextLabel;
import com.myownb3.dominic.timerecording.core.book.adapter.BookerAdapter;
import com.myownb3.dominic.timerecording.core.book.adapter.ServiceCodeAdapter;
import com.myownb3.dominic.timerecording.core.book.result.BookResultType;
import com.myownb3.dominic.timerecording.core.book.result.BookerResult;
import com.myownb3.dominic.timerecording.core.work.businessday.BusinessDay;
import com.myownb3.dominic.timerecording.core.work.businessday.BusinessDayIncrement;
import com.myownb3.dominic.timerecording.core.work.businessday.TimeSnippet;
import com.myownb3.dominic.timerecording.settings.Settings;
import com.myownb3.dominic.timerecording.ticketbacklog.data.Ticket;
import com.myownb3.dominic.timerecording.ticketbacklog.data.ticket.TicketAttrs;

/**
 * The {@link AbacusBookerAdapter} books directly into the abacus interface
 * 
 * @author Dominic
 *
 */
public class AbacusBookerAdapter implements BookerAdapter {

   private static final Logger LOG = Logger.getLogger(AbacusBookerAdapter.class);
   private AbacusBookingConnector abacusBookingConnector;
   private AbacusServiceCodeAdapter serviceCodeAdapter;
   private long employeeNumber;
   private boolean isInitialized;

   public AbacusBookerAdapter() {
      String username = Settings.INSTANCE.getSettingsValue(USER_NAME_VALUE_KEY);
      createAbacusBookingConnector(username);
      this.serviceCodeAdapter = new AbacusServiceCodeAdapter(abacusBookingConnector);
      init(username);
   }

   private void createAbacusBookingConnector(String username) {
      String pw = Settings.INSTANCE.getSettingsValue(USER_NAME_PW_VALUE_KEY);
      this.abacusBookingConnector = new AbacusBookingConnector(username, pw, AbacusConst.DEFAULT_END_POINT, AbacusConst.DEFAULT_MANDANT);
   }

   /**
    * Constructor for testing purpose only!
    * 
    * @param abacusBookingConnector
    *        the {@link AbacusBookingConnector}
    * @param username
    *        the username
    */
   AbacusBookerAdapter(AbacusBookingConnector abacusBookingConnector, String username) {
      this(abacusBookingConnector, new AbacusServiceCodeAdapter(abacusBookingConnector), username);
   }

   /**
    * Package private constructor for testing purpose only
    * 
    * @param abacusBookingConnector
    *        the {@link AbacusBookingConnector}
    * @param serviceCodeAdapter
    *        the {@link AbacusServiceCodeAdapter}
    * @param username
    *        the username
    */
   AbacusBookerAdapter(AbacusBookingConnector abacusBookingConnector, AbacusServiceCodeAdapter serviceCodeAdapter, String username) {
      this.abacusBookingConnector = abacusBookingConnector;
      this.serviceCodeAdapter = serviceCodeAdapter;
      init(username);
   }

   private void init(String username) {
      serviceCodeAdapter.init();
      fetchEmployeeNumber(username);
      this.isInitialized = employeeNumber > 0;
   }

   public boolean isInitialized() {
      return isInitialized;
   }

   @Override
   public ServiceCodeAdapter getServiceCodeAdapter() {
      return serviceCodeAdapter;
   }

   @Override
   public BookerResult book(BusinessDay businessDay) {
      businessDay.refreshDummyTickets();
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
               stringBuilder.append(String.format(TextLabel.NOT_BOOKABLE_TICKETS_FOUND_TEXT, businessDayIncrement.getTicketNumber()));
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
