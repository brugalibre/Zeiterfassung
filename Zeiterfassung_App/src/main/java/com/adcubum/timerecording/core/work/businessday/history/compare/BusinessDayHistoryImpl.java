package com.adcubum.timerecording.core.work.businessday.history.compare;

import static java.util.Objects.requireNonNull;

import java.time.LocalDate;

import com.adcubum.timerecording.core.work.businessday.BusinessDay;
import com.adcubum.timerecording.core.work.businessday.history.BusinessDayHistory;
import com.adcubum.timerecording.work.date.Time;
import com.adcubum.timerecording.work.date.TimeFactory;
import com.adcubum.util.parser.DateParser;
import com.adcubum.util.parser.NumberFormat;

public class BusinessDayHistoryImpl implements BusinessDayHistory {

   private String dateRepresentation;
   private String bookedHours;
   private LocalDate date;

   private BusinessDayHistoryImpl(String dateRepresentation, String bookedHours, LocalDate date) {
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

   @Override
   public String toString() {
      return dateRepresentation + ", booked hours: " + bookedHours;
   }

   /**
    * Creates a {@link BusinessDayHistoryImpl} for the given Time with zero booked hours
    * 
    * @param date
    *        the {@link LocalDate} when this {@link BusinessDayHistory} took lace
    * @return a {@link BusinessDayHistoryImpl} for the given Time with zero booked hours
    */
   public static BusinessDayHistoryImpl of(LocalDate date) {
      String dateAsString = DateParser.parse2String(date, DateParser.DD_MM_YYYY);
      return new BusinessDayHistoryImpl(dateAsString, "0", date);
   }

   /**
    * Creates a new {@link BusinessDayHistoryImpl}
    *
    * @param businessDay
    *        the {@link BusinessDay} to create a {@link BusinessDayHistoryImpl} from
    * @return a new {@link BusinessDayHistoryImpl}
    */
   public static BusinessDayHistoryImpl of(BusinessDay businessDay) {
      String dateAsString = DateParser.parse2String(businessDay.getDate(), DateParser.DD_MM_YYYY);
      String bookedHoursAsString = NumberFormat.format(businessDay.getTotalDuration());
      Time time = TimeFactory.createNew(businessDay.getDate());
      return new BusinessDayHistoryImpl(dateAsString, bookedHoursAsString, time.getLocalDate());
   }
}
