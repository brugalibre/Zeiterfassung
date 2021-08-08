package com.adcubum.timerecording.app.startstopresult;

import java.util.Optional;

public interface UserInteractionResult {

   /**
    * Return <code>true</code> if the user needs to interact, e.g. since a current recording is finished
    * Otherwise it returns <code>false</code>
    * 
    * @return
    */
   public boolean isUserInteractionRequired();

   /**
    * @return an optional {@link StartNotPossibleInfo}
    */
   public Optional<StartNotPossibleInfo> getOptionalStartNotPossibleInfo();
}
