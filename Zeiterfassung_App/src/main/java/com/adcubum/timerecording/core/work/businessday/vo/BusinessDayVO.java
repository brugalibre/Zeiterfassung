/**
 * 
 */
package com.adcubum.timerecording.core.work.businessday.vo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.adcubum.timerecording.core.work.businessday.BusinessDay;

/**
 * The {@link BusinessDayVO} is used whenever a we need
 * {@link BusinessDay} for displaying or exporting. The {@link BusinessDayVO} is read only
 * 
 * @author Dominic
 *
 */
public class BusinessDayVO {

   private List<BusinessDayIncrementVO> businessDayIncrements;
   private float totalDuration;
   private Date date;
   private boolean hasNotChargedElements;
   private boolean hasIncrementWithDescription;

   /**
    * Returns the default representation of a date using the pattern 'dd.MM.yyyy'
    * 
    * @return the default representation of a date using the pattern 'dd.MM.yyyy'
    */
   public String getDateRep() {
      return getDateRep("dd.MM.yyyy");
   }

   /**
    * Returns the default representation of a date using the given pattern
    * 
    * @param pattern
    *        the pattern to use
    * @return the default representation of a date using the given pattern
    */
   public String getDateRep(String pattern) {
      SimpleDateFormat df = (SimpleDateFormat) DateFormat.getTimeInstance(DateFormat.SHORT);
      df.applyPattern(pattern);
      return df.format(date);
   }

   private BusinessDayVO(BusinessDay businessDay) {

      totalDuration = businessDay.getTotalDuration();
      date = businessDay.getDate();

      businessDayIncrements = businessDay.getIncrements()//
            .stream()//
            .map(BusinessDayIncrementVO::of)//
            .collect(Collectors.toList());
      hasNotChargedElements = businessDay.hasNotChargedElements();
      hasIncrementWithDescription = businessDay.hasDescription();
   }

   public final List<BusinessDayIncrementVO> getBusinessDayIncrements() {
      return this.businessDayIncrements;
   }

   /**
    * Returns <code>true</code> if this {@link BusinessDay} has at least one
    * element with a description <code>false</code> if not
    * 
    * @return <code>true</code> if this {@link BusinessDay} has at least one
    *         element with a description <code>false</code> if not
    */
   public boolean hasIncrementWithDescription() {
      return hasIncrementWithDescription;
   }

   /**
    * @return <code>true</code> if this {@link BusinessDayVO} has at least one
    *         {@link BusinessDayIncrementVO} which is not charged yet otherwise
    *         <code>false</code>
    */
   public boolean hasNotChargedElements() {
      return hasNotChargedElements;
   }

   public String getTotalDurationRep() {
      return String.valueOf(totalDuration);
   }

   /**
    * Creates a new {@link BusinessDayVO} for the given {@link BusinessDay}
    * 
    * @param businessDay
    *        the given {@link BusinessDay}
    * @return a new {@link BusinessDayVO}
    */
   public static BusinessDayVO of(BusinessDay businessDay) {
      return new BusinessDayVO(businessDay);
   }
}
