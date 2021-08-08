/**
 * 
 */
package com.adcubum.timerecording.model.businessday;

import java.util.List;
import java.util.stream.Collectors;

import com.adcubum.timerecording.core.work.businessday.BusinessDay;
import com.adcubum.timerecording.core.work.businessday.vo.BusinessDayVO;
import com.adcubum.util.parser.NumberFormat;

/**
 * The {@link BusinessDayDto} is used to represent a {@link BusinessDay} via api to web services
 * 
 * @author Dominic
 *
 */
public class BusinessDayDto {

   private List<BusinessDayIncrementDto> businessDayIncrementDtos;
   private float totalDuration;

   private BusinessDayDto(BusinessDay businessDay) {
      totalDuration = businessDay.getTotalDuration();
      businessDayIncrementDtos = businessDay.getIncrements()
            .stream()
            .map(BusinessDayIncrementDto::of)
            .collect(Collectors.toList());
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

   @Override
   public String toString() {
      return "BusinessDayDto [businessDayIncrementDtos=" + businessDayIncrementDtos + ", totalDuration=" + totalDuration + "]";
   }

   /**
    * Creates a new {@link BusinessDayVO} for the given {@link BusinessDay}
    * 
    * @param businessDay
    *        the given {@link BusinessDay}
    * @return a new {@link BusinessDayVO}
    */
   public static BusinessDayDto of(BusinessDay businessDay) {
      return new BusinessDayDto(businessDay);
   }
}
