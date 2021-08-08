package com.adcubum.timerecording.app.startstopresult;

import com.adcubum.timerecording.message.Message;

public class StartNotPossibleInfoImpl implements StartNotPossibleInfo {

   private Message message;

   public StartNotPossibleInfoImpl(Message message) {
      this.message = message;
   }

   @Override
   public Message getMessage() {
      return message;
   }
}
