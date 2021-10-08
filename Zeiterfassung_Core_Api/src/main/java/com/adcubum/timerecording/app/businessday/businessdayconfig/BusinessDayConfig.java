package com.adcubum.timerecording.app.businessday.businessdayconfig;

/**
 * The {@link BusinessDayConfig} defines the set amount of hours and knows also the actual working hours
 * It can therefore define the amount of percentage of work which is needs stil to be done
 * 
 * @author dstalder
 *
 */
public interface BusinessDayConfig {

   /**
    * Returns the hours which are left if the given amount of hours are substracted from the total set hours
    * 
    * @param currentHours
    *        the already worked hours
    * @return the hours left to work for today
    */
   float getHoursLeft(float currentHours);

   /**
    * @return the total hours we have to work during a day
    */
   float getSetHours();

}
