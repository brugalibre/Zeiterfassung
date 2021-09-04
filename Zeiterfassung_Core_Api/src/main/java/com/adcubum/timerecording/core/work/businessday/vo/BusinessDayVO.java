package com.adcubum.timerecording.core.work.businessday.vo;

import java.util.List;

import com.adcubum.timerecording.core.work.businessday.BusinessDay;
import com.adcubum.timerecording.core.work.businessday.comeandgo.ComeAndGoes;

public interface BusinessDayVO {

   List<BusinessDayIncrementVO> getBusinessDayIncrements();

   /**
    * Returns <code>true</code> if this {@link BusinessDay} has at least one
    * element with a description <code>false</code> if not
    * 
    * @return <code>true</code> if this {@link BusinessDay} has at least one
    *         element with a description <code>false</code> if not
    */
   boolean hasIncrementWithDescription();

   /**
    * @return <code>true</code> if this {@link BusinessDayVO} has at least one
    *         {@link BusinessDayIncrementVO} which is not charged yet otherwise
    *         <code>false</code>
    */
   boolean hasNotChargedElements();

   /**
    * @return the string representation of the total duration
    */
   String getTotalDurationRep();

   /**
    * @return {@link ComeAndGoes}
    */
   ComeAndGoes getComeAndGoes();

}
