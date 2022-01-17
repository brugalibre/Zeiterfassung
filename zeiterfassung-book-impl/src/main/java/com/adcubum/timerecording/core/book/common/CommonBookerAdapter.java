package com.adcubum.timerecording.core.book.common;

import com.adcubum.librarys.text.res.TextLabel;
import com.adcubum.timerecording.core.book.adapter.BookerAdapter;
import com.adcubum.timerecording.core.book.adapter.ServiceCodeAdapter;
import com.adcubum.timerecording.core.book.result.BookResultType;
import com.adcubum.timerecording.core.book.result.BookerResult;
import com.adcubum.timerecording.core.work.businessday.BusinessDay;
import com.adcubum.timerecording.core.work.businessday.BusinessDayIncrement;
import com.adcubum.timerecording.jira.data.ticket.Ticket;
import com.adcubum.timerecording.security.login.auth.AuthenticationContext;
import com.adcubum.timerecording.security.login.auth.AuthenticationService;
import com.adcubum.timerecording.security.login.auth.init.UserAuthenticatedObservable;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public abstract class CommonBookerAdapter<T extends ServiceCodeAdapter> implements BookerAdapter, UserAuthenticatedObservable {

   protected Supplier<char[]> userPwdSupplier;
   protected String username;
   protected T serviceCodeAdapter;

   protected CommonBookerAdapter(Class<T> serviceCodeAdapterClass) {
      this.username = "";
      this.userPwdSupplier = () -> new char[]{};
      this.serviceCodeAdapter = createServiceCodeAdapter(serviceCodeAdapterClass);
   }

   private T createServiceCodeAdapter(Class<T> serviceCodeAdapterClass) {
      try {
         return serviceCodeAdapterClass.getDeclaredConstructor().newInstance();
      } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
         throw new IllegalStateException(e);
      }
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
   public boolean isTicketBookable(Ticket ticket) {
      return true; // per default there are no mandatory attributes
   }

   @Override
   public ServiceCodeAdapter getServiceCodeAdapter() {
      return serviceCodeAdapter;
   }

   protected BookerResult createAndReturnBookResult(BusinessDay bookedBusinessDay, BusinessDay initialBusinessDay) {
      List<BusinessDayIncrement> allHandledBusinessDayIncrements = evalHandledBusinessDayIncrements(bookedBusinessDay, initialBusinessDay);
      BookResultType bookResultType = evalBookResultType(allHandledBusinessDayIncrements);
      String message = evalResultMessage(allHandledBusinessDayIncrements, bookResultType);
      return new CommonBookResult(bookedBusinessDay, bookResultType, message);
   }

   /*
    * Evaluates all BusinessDayIncrements which where 'touched' during the current booking progress. This means,
    * basically all BusinessDayIncrements which weren't booked initially
    *
    */
   private List<BusinessDayIncrement> evalHandledBusinessDayIncrements(BusinessDay bookedBusinessDay, BusinessDay initialBusinessDay) {
      Predicate<BusinessDayIncrement> isBooked = BusinessDayIncrement::isBooked;
      return initialBusinessDay.getIncrements()
              .stream()
              .filter(isBooked.negate())
              .map(getIncrementFromBookedBusinessDayByIncrementId(bookedBusinessDay))
              .collect(Collectors.toList());
   }

   private static Function<BusinessDayIncrement, BusinessDayIncrement> getIncrementFromBookedBusinessDayByIncrementId(BusinessDay bookedBusinessDay) {
      return notBookedBusinessDayIncrement -> bookedBusinessDay.getBusinessDayIncrementById(notBookedBusinessDayIncrement.getId());
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
         case NOT_BOOKED:
            stringBuilder.append(TextLabel.NOT_BOOKED);
            break;
         default:
            throw new IllegalStateException("Unknown state '" + bookResultType + "'");
      }
      return stringBuilder.toString();
   }

   private static BookResultType evalBookResultType(List<BusinessDayIncrement> currentIncrements2Book) {
      if (wasBookingCompleteSuccessfull(currentIncrements2Book)) {
         return BookResultType.SUCCESS;
      } else if (!currentIncrements2Book.isEmpty()) {
         List<BusinessDayIncrement> notBookableIncrements = getNotBookableIncrements(currentIncrements2Book);
         if (wasBookingFailure(currentIncrements2Book)) {
            return BookResultType.FAILURE;
         } else if (hasNotBookeableInc(notBookableIncrements)) {
            return BookResultType.PARTIAL_SUCCESS_WITH_NON_BOOKABLE;
         } else {
            return BookResultType.PARTIAL_SUCCESS_WITH_ERROR;
         }
      }
      return BookResultType.NOT_BOOKED;
   }

   private static boolean hasNotBookeableInc(List<BusinessDayIncrement> notBookableIncrements) {
      // we implicitly have booked increments, otherwise it would be a full 'failure'
      return !notBookableIncrements.isEmpty();
   }

   private static boolean wasBookingCompleteSuccessfull(List<BusinessDayIncrement> currentIncrements2Book) {
      return wasBookingSuccessOrFailure(currentIncrements2Book, BusinessDayIncrement::isBooked);
   }

   private static boolean wasBookingFailure(List<BusinessDayIncrement> currentIncrements2Book) {
      Predicate<BusinessDayIncrement> isBooked = BusinessDayIncrement::isBooked;
      return wasBookingSuccessOrFailure(currentIncrements2Book, isBooked.negate());
   }

   private static boolean wasBookingSuccessOrFailure(List<BusinessDayIncrement> currentIncrements2Book,
                                                     Predicate<BusinessDayIncrement> isBookedOrNotCharged) {
      return !currentIncrements2Book.isEmpty()
              & currentIncrements2Book.stream()
              .allMatch(isBookedOrNotCharged);
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
