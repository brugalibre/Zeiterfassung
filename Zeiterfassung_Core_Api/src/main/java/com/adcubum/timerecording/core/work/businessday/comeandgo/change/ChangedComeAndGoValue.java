package com.adcubum.timerecording.core.work.businessday.comeandgo.change;

import com.adcubum.timerecording.core.work.businessday.comeandgo.ComeAndGo;
import com.adcubum.timerecording.work.date.Time;

/**
 * The {@link ChangedComeAndGoValue} defines the new values for a {@link ComeAndGo}
 * 
 * @author dstalder
 *
 */
public interface ChangedComeAndGoValue {

   /**
    * @return the new come value
    */
   Time getNewComeValue();

   /**
    * @return the new go value
    */
   Time getNewGoValue();

   /**
    * @return the id of the {@link ComeAndGo} to change
    */
   String getId();
}
