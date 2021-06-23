/**
 * 
 */
package com.adcubum.timerecording.settings.round;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

import com.adcubum.timerecording.settings.round.observable.RoundModeChangedListener;

/**
 * The {@link TimeRounderImpl} is responsible for loading and storing of the current {@link RoundMode}
 * 
 * @author Dominic
 *
 */
public class TimeRounderImpl implements TimeRounder {

   private static final String TIME_ROUNDER_KEY = RoundMode.PROPERTY_KEY;
   private UnaryOperator<String> settingsValueProvider;
   private List<RoundModeChangedListener> changedListeners;
   private RoundMode roundMode;

   /**
    * Package private default Constructor used by the {@link TimeRounderFactory}
    * Initializes the {@link RoundMode} of this {@link TimeRounder} to {@link RoundMode#ONE_MIN}
    * 
    */
   TimeRounderImpl() {
      this.roundMode = RoundMode.ONE_MIN;
   }

   @Override
   public void init(UnaryOperator<String> settingsValueProvider) {
      this.settingsValueProvider = requireNonNull(settingsValueProvider);
      this.changedListeners = new ArrayList<>();
      RoundMode newRoundMode = evalRoundMode();
      setRoundMode(newRoundMode);
   }

   private RoundMode evalRoundMode() {
      String roundModeAsString = settingsValueProvider.apply(TIME_ROUNDER_KEY);
      return RoundMode.getRoundMode(roundModeAsString);
   }

   @Override
   public void setRoundMode(RoundMode newRoundMode) {
      RoundMode oldRoundMode = this.roundMode;
      this.roundMode = newRoundMode;
      if (oldRoundMode != newRoundMode && nonNull(oldRoundMode)) {
         changedListeners.forEach(changedListener -> changedListener.onRoundModeChanged(oldRoundMode, newRoundMode));
      }
   }

   @Override
   public final RoundMode getRoundMode() {
      return this.roundMode;
   }

   @Override
   public void addRoundModeChangedListener(RoundModeChangedListener roundModeChangedListener) {
      changedListeners.add(requireNonNull(roundModeChangedListener));
   }
}
