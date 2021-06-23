package com.adcubum.timerecording.settings.round.observable;

import com.adcubum.timerecording.settings.round.RoundMode;
import com.adcubum.timerecording.settings.round.TimeRounder;

/**
 * The {@link RoundModeChangedListener} is a listener in order to react on changed {@link RoundMode} of a {@link TimeRounder}
 * 
 * @author DStalder
 *
 */
public interface RoundModeChangedListener {

   /**
    * Is called as soon as a {@link TimeRounder} has changed it's internal {@link RoundMode}
    * 
    * @param oldRoundMode
    *        the old value
    * @param newRoundMode
    *        the new value
    */
   void onRoundModeChanged(RoundMode oldRoundMode, RoundMode newRoundMode);

}
