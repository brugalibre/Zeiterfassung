package com.adcubum.timerecording.core.work.businessday.history;

import java.time.LocalDate;

import com.adcubum.timerecording.core.work.businessday.BusinessDay;

/**
 * The {@link BusinessDayHistory} represents a booked {@link BusinessDay} and is used in the {@link BusinessDayHistoryOverview}
 * It therefore contains only the date when it took place as well as the amount of working hours of that specific day
 * 
 * @author dstalder
 *
 */
public interface BusinessDayHistory {

   /**
    * @return the date of this {@link BusinessDayHistory} as String
    */
   String getDateRepresentation();

   /**
    * @return the {@link LocalDate} this {@link BusinessDayHistory} took place
    */
   LocalDate getLocalDate();

   /**
    * @return the amount of booked hours as String
    * 
    */
   String getBookedHours();

}
