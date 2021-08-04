package com.adcubum.timerecording.app;

import com.adcubum.timerecording.core.work.WorkStates;
import com.adcubum.timerecording.core.work.businessday.BusinessDay;
import com.adcubum.timerecording.core.work.businessday.BusinessDayIncrement;
import com.adcubum.timerecording.core.work.businessday.TimeSnippet;

public class TimeRecorderHelper {

   private TimeRecorderHelper() {
      // private 
   }

   /**
    * Evaluates the {@link WorkStates} of the {@link TimeRecorder} depending on the {@link BusinessDay#getCurrentBussinessDayIncremental()}
    * That means, if there is a non finished {@link TimeSnippet} on the current {@link BusinessDayIncrement}, {@link WorkStates#WORKING} is
    * returned.
    * 
    * @param businessDay
    *        the {@link BusinessDay}
    * @return the initial {@link WorkStates} depending on the state of the given {@link BusinessDay}
    */
   public static WorkStates evalWorkingState4BusinessDay(BusinessDay businessDay) {
      WorkStates currentState = WorkStates.NOT_WORKING;
      if (businessDay.hasUnfinishedBusinessDayIncrement()) {
         currentState = WorkStates.WORKING;
      } else if (businessDay.getComeAndGoes().hasUnfinishedComeAndGo()) {
         currentState = WorkStates.COME_AND_GO;
      }
      return currentState;
   }

}
