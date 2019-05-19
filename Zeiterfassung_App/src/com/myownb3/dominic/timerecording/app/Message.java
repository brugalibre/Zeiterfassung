package com.myownb3.dominic.timerecording.app;

/**
 * A {@link Message} is used to transfer message between the {@link TimeRecorder} and the 
 * corresponding Tray interface
 * @author Dominic
 *
 */
public class Message {

    private String message;
    private String messageTitle;
    private MessageType messageType;

    private Message(String message, String messageTitle, MessageType messageType) {
	this.message = message;
	this.messageTitle = messageTitle;
	this.messageType = messageType;
    }

    public String getMessage() {
	return message;
    }

    public String getMessageTitle() {
	return messageTitle;
    }

    public MessageType getMessageType() {
	return messageType;
    }

    /**
     * Creates a new message
     * @param messageType the {@link MessageType}
     * @param title the title
     * @param msg the message itself
     * @return
     */
    public static Message of(MessageType messageType, String title, String msg) {
	return new Message(title, msg, messageType);
    }
}
