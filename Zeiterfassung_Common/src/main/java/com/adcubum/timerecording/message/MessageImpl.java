package com.adcubum.timerecording.message;

public class MessageImpl implements Message {

   private String msgAsString;
   private String messageTitle;
   private MessageType messageType;

   /**
    * Default Constructor
    * 
    * @param msgAsString
    *        the message
    * @param messageTitle
    *        the title
    * @param messageType
    *        the {@link MessageType}
    */
   MessageImpl(String msgAsString, String messageTitle, MessageType messageType) {
      this.msgAsString = msgAsString;
      this.messageTitle = messageTitle;
      this.messageType = messageType;
   }

   @Override
   public String getMessage() {
      return msgAsString;
   }

   @Override
   public String getMessageTitle() {
      return messageTitle;
   }

   @Override
   public MessageType getMessageType() {
      return messageType;
   }
}
