package com.adcubum.timerecording.integtest;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import com.adcubum.timerecording.core.businessday.repository.impl.BusinessDayRepositoryImpl;
import com.adcubum.timerecording.core.work.businessday.BusinessDay;
import com.adcubum.timerecording.core.work.businessday.BusinessDayIncrement;
import com.adcubum.timerecording.core.work.businessday.TimeSnippetImpl.TimeSnippetBuilder;
import com.adcubum.timerecording.core.work.businessday.repository.BusinessDayRepository;
import com.adcubum.timerecording.core.work.businessday.update.callback.impl.BusinessDayIncrementAdd.BusinessDayIncrementAddBuilder;
import com.adcubum.timerecording.data.ticket.ticketactivity.factor.TicketActivityFactory;
import com.adcubum.timerecording.jira.data.ticket.Ticket;
import com.adcubum.timerecording.work.date.DateTimeBuilder;

public class BusinessDayIntegTestUtil {

   private BusinessDayIntegTestUtil() {
      // private 
   }

   /**
    * Creates a new booked {@link BusinessDay} which contains one {@link BusinessDayIncrement} which takes place at the given
    * {@link LocalDate} and has the given description
    * 
    * @param date
    *        the date at which the {@link BusinessDayIncrement} to create takes place
    * @param description
    *        the description of the {@link BusinessDayIncrement}
    * @param businessDayRepository
    *        the {@link BusinessDayRepository} which creates the {@link BusinessDay}
    * @param timeSnippetDurationInHours
    * @return the new created and stored {@link BusinessDay}
    */
   public static BusinessDay createNewBookedBusinessDayAtLocalDate(LocalDate date, String description,
         BusinessDayRepositoryImpl businessDayRepository, int timeSnippetDurationInHours) {
      int hour = 12;
      return createNewBookedBusinessDayAtDateInternal(date.getYear(), date.getMonthValue() - 1, date.getDayOfMonth(), hour, description,
            businessDayRepository, timeSnippetDurationInHours);
   }

   /**
    * Creates a new booked {@link BusinessDay} which contains one {@link BusinessDayIncrement} which takes place at the given year, month
    * and day and has the given description
    * 
    * @param year
    *        the year at which the {@link BusinessDayIncrement} to create takes place
    * @param month
    *        the month at which the {@link BusinessDayIncrement} to create takes place
    * @param day
    *        the day at which the {@link BusinessDayIncrement} to create takes place
    * @param description
    *        the description of the {@link BusinessDayIncrement}
    * @param businessDayRepository
    *        the {@link BusinessDayRepository} which creates the {@link BusinessDay}
    * @return the new created and stored {@link BusinessDay}
    */
   public static BusinessDay createNewBookedBusinessDayAtDate(int year, int month, int day, String description,
         BusinessDayRepositoryImpl businessDayRepository) {
      return createNewBookedBusinessDayAtDateInternal(year, month, day, 12, description, businessDayRepository, 1);
   }

   private static BusinessDay createNewBookedBusinessDayAtDateInternal(int year, int month, int day, int hour, String description,
         BusinessDayRepositoryImpl businessDayRepository, int timeSnippetDurationInHours) {
      BusinessDay businessDay = businessDayRepository.createNew(true)
            .addBusinessIncrement(new BusinessDayIncrementAddBuilder()
                  .withAmountOfHours("3")
                  .withDescription(description)
                  .withTicketActivity(TicketActivityFactory.INSTANCE.createNew("test", 113))
                  .withTicket(mockTicket("ABES-1"))
                  .withTimeSnippet(TimeSnippetBuilder.of()
                        .withBeginTime(DateTimeBuilder.of()
                              .withYear(year)
                              .withMonth(month)
                              .withDay(day)
                              .withHour(hour)
                              .withMinute(45)
                              .build())
                        .withEndTime(DateTimeBuilder.of()
                              .withYear(year)
                              .withMonth(month)
                              .withDay(day)
                              .withHour(hour + timeSnippetDurationInHours)
                              .withMinute(45)
                              .build())
                        .build())
                  .build());
      return businessDayRepository.save(businessDay.flagBusinessDayAsCharged());
   }

   private static Ticket mockTicket(String ticketNr) {
      Ticket ticket = mock(Ticket.class);
      when(ticket.getNr()).thenReturn(ticketNr);
      return ticket;
   }
}
