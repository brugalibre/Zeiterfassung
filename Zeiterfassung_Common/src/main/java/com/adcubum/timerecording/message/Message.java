package com.adcubum.timerecording.message;

/**
 * A {@link Message} is used to transfer message between the
 * {@link TimeRecorder} and the corresponding Tray interface
 * 
 * @author Dominic
 *
 */
public class Message {

   private String msgAsString;
   private String messageTitle;
   private MessageType messageType;

   private Message(String msgAsString, String messageTitle, MessageType messageType) {
      this.msgAsString = msgAsString;
      this.messageTitle = messageTitle;
      this.messageType = messageType;
   }

   public String getMessage() {
      return msgAsString;
   }

   public String getMessageTitle() {
      return messageTitle;
   }

   public MessageType getMessageType() {
      return messageType;
   }

   /**
    * Creates a new message
    * 
    * @param messageType
    *        the {@link MessageType}
    * @param title
    *        the title
    * @param msg
    *        the message itself
    * @return
    */
   public static Message of(MessageType messageType, String msg, String title) {
      return new Message(msg, title, messageType);
   }
}
