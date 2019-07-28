package com.myownb3.dominic.ui.core.dialog;

import java.awt.TextArea;
import java.awt.Toolkit;

import javax.swing.JOptionPane;

import com.myownb3.dominic.librarys.text.res.TextLabel;
import com.myownb3.dominic.timerecording.app.MessageType;
import com.myownb3.dominic.ui.util.SwingUtil;
import com.sun.xml.internal.txw2.IllegalSignatureException;

/**
 * The DialogManager is used to manage all JDialog in the user interface. Most
 * of them are object by the DialoManager.
 * 
 * @author Dominic
 */
public class DialogManager extends JOptionPane {
    /**
    * 
    */
    private static final long serialVersionUID = 1L;

    /**
     * Shows a OptionPane with the message 'messageToDisplay'. This method is
     * called, when a fatal error occurs, such as unable to load the pictures or
     * language files
     * 
     * @param component,
     *            an additionally component such as {@link TextArea}s and so on
     * @param title,
     *            the title of the dialog
     */
    public static void showException(String printStackTrace, String title) {
	Toolkit.getDefaultToolkit().beep();
	showConfirmDialog(null, SwingUtil.getDisplayArea(TextLabel.EXCEPTION_DIALOG_MESSAGE, printStackTrace), title,
		CLOSED_OPTION, ERROR_MESSAGE);
    }
    
    public static void showMessageDialog(String title, String message, MessageType messageType) {
	Toolkit.getDefaultToolkit().beep();
	showMessageDialog(null, message, title, getMessageType(messageType));
    }

    private static int getMessageType(MessageType messageType) {
	switch (messageType) {
	case ERROR:
	    return ERROR_MESSAGE;
	case WARNING:
	    return WARNING_MESSAGE;
	case INFORMATION:
	    return INFORMATION_MESSAGE;
	default:
	    throw new IllegalSignatureException("Unknown messageType '" + messageType + "'");
	}
    }
}
