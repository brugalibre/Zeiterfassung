package com.adcubum.timerecording.message;

import com.adcubum.timerecording.core.factory.AbstractFactory;

/**
 * The {@link MessageFactory} is used in order to create a {@link Message}
 * 
 * @author Dominic
 *
 */
public class MessageFactory extends AbstractFactory {
   private static final MessageFactory INSTANCE = new MessageFactory();
   private static final String BEAN_NAME = "message";

   private MessageFactory() {
      super("spring.xml");
   }

   /**
    * Creates a new {@link Message} instance
    * 
    * @return a new {@link Message} instance
    */
   public static Message createNew(MessageType messageType, String msg, String title) {
      return INSTANCE.createNewWithAgruments(BEAN_NAME, msg, title, messageType);
   }
}
