package com.adcubum.timerecording.core.work.businessday.vo;

import java.util.List;

import com.adcubum.timerecording.core.work.businessday.BusinessDay;
import com.adcubum.timerecording.core.work.businessday.comeandgo.ComeAndGoes;

public interface BusinessDayVO {

   /**
    * Returns the default representation of a date using the pattern 'dd.MM.yyyy'
    * 
    * @return the default representation of a date using the pattern 'dd.MM.yyyy'
    */
   String getDateRep();

   /**
    * Returns the default representation of a date using the given pattern
    * 
    * @param pattern
    *        the pattern to use
    * @return the default representation of a date using the given pattern
    */
   String getDateRep(String pattern);

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
