package com.adcubum.timerecording.settings.round;

import com.adcubum.timerecording.settings.Settings;

/**
 * The {@link TimeRounder} is responsible for loading and storing of the current {@link RoundMode}
 * 
 * @author Dominic
 *
 */
public interface TimeRounder {

   /** The singleton instance of a {@link TimeRounder} */
   public static final TimeRounder INSTANCE = TimeRounderFactory.createNew(Settings.INSTANCE);

   /**
    * @return the currently set {@link RoundMode}
    */
   RoundMode getRoundMode();

   /**
    * Sets a new {@link RoundMode}
    * 
    * @param newRoundMode
    *        the new {@link RoundMode} to set
    */
   void setRoundMode(RoundMode newRoundMode);

}
