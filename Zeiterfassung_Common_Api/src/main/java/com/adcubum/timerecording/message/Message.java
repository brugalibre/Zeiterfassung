package com.adcubum.timerecording.message;

/**
 * The Message defines a message with content, title and type which can be exchanged between the ui and the app layer
 * 
 * @author Dominic
 *
 */
public interface Message {

   /**
    * @return the {@link MessageType} of this {@link Message} instance
    */
   MessageType getMessageType();

   /**
    * @return the title of this {@link Message} instance
    */
   String getMessageTitle();

   /**
    * @return the actual message of this {@link Message} instance
    */
   String getMessage();

}
