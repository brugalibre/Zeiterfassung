package com.adcubum.timerecording.core.work.businessday.history;

import com.adcubum.timerecording.core.work.businessday.BusinessDay;
import com.adcubum.timerecording.core.work.businessday.ticketdistribution.TicketDistribution;
import com.adcubum.timerecording.core.work.businessday.ticketdistribution.TicketDistributionEvaluator;
import com.adcubum.timerecording.core.work.businessday.ticketdistribution.TicketDistributionImpl;
import com.adcubum.timerecording.core.work.businessday.util.BusinessDayUtil;
import com.adcubum.timerecording.work.date.DateTime;
import com.adcubum.util.parser.DateParser;

import java.time.LocalDate;
import java.util.Collections;

import static java.util.Objects.requireNonNull;

public class BusinessDayHistoryImpl implements BusinessDayHistory {

   private TicketDistribution ticketDistribution;
   private String dateRepresentation;
   private String bookedHours;
   private LocalDate date;

   private BusinessDayHistoryImpl(String dateRepresentation, String bookedHours, LocalDate date, TicketDistribution ticketDistribution) {
      this.ticketDistribution = requireNonNull(ticketDistribution);
      this.dateRepresentation = requireNonNull(dateRepresentation);
      this.bookedHours = requireNonNull(bookedHours);
      this.date = requireNonNull(date);
   }

   @Override
   public String getDateRepresentation() {
      return dateRepresentation;
   }

   @Override
   public LocalDate getLocalDate() {
      return date;
   }

   @Override
   public String getBookedHours() {
      return bookedHours;
   }

   public TicketDistribution getTicketDistribution() {
      return ticketDistribution;
   }

   @Override
   public String toString() {
      return dateRepresentation + ", booked hours: " + bookedHours;
   }

   /**
    * Creates a {@link BusinessDayHistoryImpl} for the given Time with zero booked hours
    *
    * @param date the {@link LocalDate} when this {@link BusinessDayHistory} took lace
    * @return a {@link BusinessDayHistoryImpl} for the given Time with zero booked hours
    */
   public static BusinessDayHistoryImpl of(LocalDate date) {
      String dateAsString = DateParser.parse2String(date, DateParser.DD_MM_YYYY);
      return new BusinessDayHistoryImpl(dateAsString, "0", date, new TicketDistributionImpl(Collections.emptyList()));
   }

   /**
    * Creates a new {@link BusinessDayHistoryImpl}
    *
    * @param businessDay the {@link BusinessDay} to create a {@link BusinessDayHistoryImpl} from
    * @return a new {@link BusinessDayHistoryImpl}
    */
   public static BusinessDayHistoryImpl of(BusinessDay businessDay) {
      DateTime businessDayDateTime = businessDay.getDateTime();
      String dateAsString = DateParser.parse2String(businessDayDateTime, DateParser.DD_MM_YYYY);
      String bookedHoursAsString = BusinessDayUtil.getTotalDurationRep(businessDay);
      TicketDistribution ticketDistribution = evaluateTicketDistributionFromIncrements(businessDay);
      return new BusinessDayHistoryImpl(dateAsString, bookedHoursAsString, businessDayDateTime.getLocalDate(), ticketDistribution);
   }

   private static TicketDistribution evaluateTicketDistributionFromIncrements(BusinessDay businessDay) {
      TicketDistributionEvaluator ticketDistributionEvaluator = new TicketDistributionEvaluator();
      return ticketDistributionEvaluator.evaluateTicketDistributionFromIncrements(businessDay.getIncrements());
   }
}
