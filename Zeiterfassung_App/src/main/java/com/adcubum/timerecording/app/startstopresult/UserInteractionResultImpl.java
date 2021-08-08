package com.adcubum.timerecording.app.startstopresult;

import java.util.Optional;

import com.adcubum.timerecording.core.work.WorkStates;

public class UserInteractionResultImpl implements UserInteractionResult {

   private boolean isUserInteractionRequired;
   private WorkStates workState;
   private StartNotPossibleInfo startNotPossibleInfo;

   public UserInteractionResultImpl(boolean isUserInteractionRequired, StartNotPossibleInfo startNotPossibleInfo, WorkStates workState) {
      this.isUserInteractionRequired = isUserInteractionRequired;
      this.workState = workState;
      this.startNotPossibleInfo = startNotPossibleInfo;
   }

   public UserInteractionResultImpl(boolean isUserInteractionRequired, WorkStates workState) {
      this.isUserInteractionRequired = isUserInteractionRequired;
      this.workState = workState;
   }

   @Override
   public boolean isUserInteractionRequired() {
      return isUserInteractionRequired;
   }

   @Override
   public Optional<StartNotPossibleInfo> getOptionalStartNotPossibleInfo() {
      return Optional.ofNullable(startNotPossibleInfo);
   }

   public WorkStates getWorkState() {
      return workState;
   }
}
