package com.adcubum.timerecording.settings.round;

import java.util.function.UnaryOperator;

import com.adcubum.timerecording.settings.round.observable.RoundModeChangedListener;

/**
 * The {@link TimeRounder} is responsible for loading and storing of the current {@link RoundMode}
 * 
 * @author Dominic
 *
 */
public interface TimeRounder {

   /** The singleton instance of a {@link TimeRounder} */
   public static final TimeRounder INSTANCE = TimeRounderFactory.createNew();

   /**
    * @return the currently set {@link RoundMode}
    */
   RoundMode getRoundMode();

   /**
    * Adds the given {@link RoundModeChangedListener}
    * 
    * @param roundModeChangedListener
    */
   void addRoundModeChangedListener(RoundModeChangedListener roundModeChangedListener);

   /**
    * Sets a new {@link RoundMode}
    * 
    * @param newRoundMode
    *        the new {@link RoundMode} to set
    */
   void setRoundMode(RoundMode newRoundMode);

   /**
    * Initializes this {@link TimeRounder}
    * 
    * @param settingsValueProvider
    *        the provider of certain stored application values
    */
   void init(UnaryOperator<String> settingsValueProvider);

}
