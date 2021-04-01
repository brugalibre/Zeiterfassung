package com.adcubum.timerecording.ui.core.dialog;

import java.awt.Component;
import java.awt.TextArea;
import java.awt.Toolkit;
import java.awt.TrayIcon.MessageType;

import javax.swing.JOptionPane;

import com.adcubum.librarys.text.res.TextLabel;
import com.adcubum.timerecording.ui.util.SwingUtil;

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
    *        an additionally component such as {@link TextArea}s and so
    *        on
    * @param title,
    *        the title of the dialog
    */
   public static void showException(String printStackTrace, String title) {
      Toolkit.getDefaultToolkit().beep();
      showConfirmDialog(null, SwingUtil.getDisplayArea(TextLabel.EXCEPTION_DIALOG_MESSAGE, printStackTrace), title,
            CLOSED_OPTION, ERROR_MESSAGE);
   }

   public static void showDialog(Component parent, String messageTitle, String message, MessageType messageType) {
      showConfirmDialog(parent, message, messageTitle, CLOSED_OPTION, map2Int(messageType));
   }

   private static int map2Int(MessageType messageType) {
      int messageTypoe;
      switch (messageType) {
         case ERROR:
            messageTypoe = 0;
            break;
         case WARNING:
            messageTypoe = 2;
            break;
         case INFO:
            messageTypoe = 1;
            break;
         default:
            messageTypoe = -1;
            break;
      }
      return messageTypoe;
   }
}
