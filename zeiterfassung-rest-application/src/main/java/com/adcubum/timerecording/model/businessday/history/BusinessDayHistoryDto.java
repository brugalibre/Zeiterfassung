package com.adcubum.timerecording.model.businessday.history;

import static java.util.Objects.requireNonNull;

import java.time.LocalDate;

import com.adcubum.timerecording.core.work.businessday.history.BusinessDayHistory;

public class BusinessDayHistoryDto {

   private String dateRepresentation;
   private String bookedHours;
   private LocalDate date;

   private BusinessDayHistoryDto(LocalDate date, String dateRepresentation, String bookedHours) {
      this.dateRepresentation = requireNonNull(dateRepresentation);
      this.bookedHours = requireNonNull(bookedHours);
      this.date = requireNonNull(date);
   }

   public String getDateRepresentation() {
      return dateRepresentation;
   }

   public String getBookedHours() {
      return bookedHours;
   }

   public LocalDate getDate() {
      return date;
   }

   /**
    * Creates a new {@link BusinessDayHistoryDto}
    *
    * @param businessDayHistory
    *        the {@link BusinessDayHistory} to create a {@link BusinessDayHistoryDto} from
    * @return a new {@link BusinessDayHistoryDto}
    */
   public static BusinessDayHistoryDto of(BusinessDayHistory businessDayHistory) {
      return new BusinessDayHistoryDto(businessDayHistory.getLocalDate(), businessDayHistory.getDateRepresentation(),
            businessDayHistory.getBookedHours());
   }
}
