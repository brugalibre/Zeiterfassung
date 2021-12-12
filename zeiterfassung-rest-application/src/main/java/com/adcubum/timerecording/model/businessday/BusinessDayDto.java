/**
 * 
 */
package com.adcubum.timerecording.model.businessday;

import java.util.List;
import java.util.stream.Collectors;

import com.adcubum.timerecording.core.work.businessday.BusinessDay;
import com.adcubum.util.parser.DateParser;
import com.adcubum.util.parser.NumberFormat;

/**
 * The {@link BusinessDayDto} is used to represent a {@link BusinessDay} via api to web services
 * 
 * @author Dominic
 *
 */
public class BusinessDayDto {

   private String dateRepresentation;
   private List<BusinessDayIncrementDto> businessDayIncrementDtos;
   private float totalDuration;

   private BusinessDayDto(BusinessDay businessDay, String dateRepPattern) {
      totalDuration = businessDay.getTotalDuration();
      businessDayIncrementDtos = businessDay.getIncrements()
            .stream()
            .map(BusinessDayIncrementDto::of)
            .collect(Collectors.toList());
      this.dateRepresentation = DateParser.parse2String(businessDay.getDateTime(), dateRepPattern);
   }

   public final List<BusinessDayIncrementDto> getBusinessDayIncrementDtos() {
      return this.businessDayIncrementDtos;
   }

   public boolean getIsDeleteAllPossible() {
      return !businessDayIncrementDtos.isEmpty();
   }

   public String getTotalDurationRep() {
      return NumberFormat.format(totalDuration);
   }

   public float getTotalDuration() {
      return totalDuration;
   }

   public String getDateRepresentation() {
      return dateRepresentation;
   }

   @Override
   public String toString() {
      return "BusinessDayDto [businessDayIncrementDtos=" + businessDayIncrementDtos + ", date=" + dateRepresentation + ", totalDuration="
            + totalDuration + "]";
   }

   /**
    * Creates a new {@link BusinessDay} for the given {@link BusinessDay}
    * 
    * @param businessDay
    *        the given {@link BusinessDay}
    * @return a new {@link BusinessDay}
    */
   public static BusinessDayDto of(BusinessDay businessDay) {
      return new BusinessDayDto(businessDay, DateParser.DD_MM_YYYY);
   }
}
