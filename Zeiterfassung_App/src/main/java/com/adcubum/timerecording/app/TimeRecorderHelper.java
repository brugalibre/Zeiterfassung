package com.adcubum.timerecording.app;

import com.adcubum.librarys.text.res.TextLabel;
import com.adcubum.timerecording.core.work.WorkStates;
import com.adcubum.timerecording.core.work.businessday.BusinessDay;
import com.adcubum.timerecording.core.work.businessday.BusinessDayIncrement;
import com.adcubum.timerecording.core.work.businessday.TimeSnippet;
import com.adcubum.timerecording.settings.Settings;
import com.adcubum.timerecording.settings.key.ValueKey;
import com.adcubum.timerecording.settings.key.ValueKeyFactory;

import static java.util.Objects.isNull;

public class TimeRecorderHelper {

   private static final ValueKey<String> APPLICATION_TITLE_KEY = ValueKeyFactory.createNew("AppTitle", String.class);

   private TimeRecorderHelper() {
      // private 
   }

   /**
    * Evaluates the {@link WorkStates} of the {@link TimeRecorder} depending on the {@link BusinessDay#getCurrentTimeSnippet()}
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

   /**
    * @return the applications title
    */
   public static String getApplicationTitle() {
      String appTitle = Settings.INSTANCE.getSettingsValue(APPLICATION_TITLE_KEY);
      return isNull(appTitle) ? TextLabel.ADC_APPLICATION_TITLE : String.format(TextLabel.TEMPLATE_APPLICATION_TITLE, appTitle);
   }
}
