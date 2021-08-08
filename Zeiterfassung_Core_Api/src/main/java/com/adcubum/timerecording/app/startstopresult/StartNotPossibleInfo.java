package com.adcubum.timerecording.app.startstopresult;

import com.adcubum.timerecording.message.Message;

/**
 * The {@link StartNotPossibleInfo} is used whenever a start of a recording is not possible
 * It contains detailed information about the cause of the failed start
 * 
 * @author dstalder
 *
 */
public interface StartNotPossibleInfo {

   /**
    * @return a msg about the cause of the failed start
    */
   public Message getMessage();
}
