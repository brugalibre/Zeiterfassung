/**
 * 
 */
package com.adcubum.timerecording.core.work.businessday.vo;

import java.util.List;
import java.util.stream.Collectors;

import com.adcubum.timerecording.core.work.businessday.BusinessDay;
import com.adcubum.timerecording.core.work.businessday.comeandgo.ComeAndGoes;

/**
 * The {@link BusinessDayVO} is used whenever a we need
 * {@link BusinessDay} for displaying or exporting. The {@link BusinessDayVO} is read only
 * 
 * @author Dominic
 *
 */
public class BusinessDayVOImpl implements BusinessDayVO {

   private List<BusinessDayIncrementVO> businessDayIncrements;
   private float totalDuration;
   private boolean hasNotChargedElements;
   private boolean hasIncrementWithDescription;
   private ComeAndGoes comeAndGoes;

   private BusinessDayVOImpl(BusinessDay businessDay) {

      totalDuration = businessDay.getTotalDuration();
      businessDayIncrements = businessDay.getIncrements()//
            .stream()//
            .map(BusinessDayIncrementVOImpl::of)//
            .collect(Collectors.toList());
      hasNotChargedElements = businessDay.hasNotChargedElements();
      hasIncrementWithDescription = businessDay.hasDescription();
      this.comeAndGoes = businessDay.getComeAndGoes();
   }

   @Override
   public final List<BusinessDayIncrementVO> getBusinessDayIncrements() {
      return this.businessDayIncrements;
   }

   @Override
   public boolean hasIncrementWithDescription() {
      return hasIncrementWithDescription;
   }

   @Override
   public boolean hasNotChargedElements() {
      return hasNotChargedElements;
   }

   @Override
   public String getTotalDurationRep() {
      return String.valueOf(totalDuration);
   }

   @Override
   public ComeAndGoes getComeAndGoes() {
      return comeAndGoes;
   }

   /**
    * Creates a new {@link BusinessDayVO} for the given {@link BusinessDay}
    * 
    * @param businessDay
    *        the given {@link BusinessDay}
    * @return a new {@link BusinessDayVO}
    */
   public static BusinessDayVO of(BusinessDay businessDay) {
      return new BusinessDayVOImpl(businessDay);
   }
}
