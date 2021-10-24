package com.adcubum.timerecording.core.work.businessday.comeandgo.change;

import java.util.UUID;

import com.adcubum.timerecording.core.work.businessday.comeandgo.ComeAndGo;
import com.adcubum.timerecording.work.date.DateTime;

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
   DateTime getNewComeValue();

   /**
    * @return the new go value
    */
   DateTime getNewGoValue();

   /**
    * @return the id of the {@link ComeAndGo} to change
    */
   UUID getId();
}
