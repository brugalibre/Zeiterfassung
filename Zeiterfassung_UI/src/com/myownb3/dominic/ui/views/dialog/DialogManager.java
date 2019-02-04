package com.myownb3.dominic.ui.views.dialog;

import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.myownb3.dominic.librarys.text.res.TextLabel;
import com.myownb3.dominic.ui.util.SwingUtil;

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

    /**
     * Shows a OptionPane with the message 'messageToDisplay'. This method is
     * called, when a fatal error occurs, such as unable to load the pictures or
     * language files
     * 
     * @param messageToDisplay
     */
    public static void showFatalError(String messageToDisplay) {
	Toolkit.getDefaultToolkit().beep();
	showConfirmDialog(null, messageToDisplay, "Fatal Error!", CLOSED_OPTION, ERROR_MESSAGE);
    }

    /**
     * Shows a OptionPane as an information-dialog with the message
     * 'messageToDisplay'.
     * 
     * @param parentWindow,
     *            the parent window
     * @param messageToDisplay
     * @param title
     */
    public static void showInformationDialog(JFrame parentWindow, String messageToDisplay, String title) {
	showMessageDialog(parentWindow, messageToDisplay, title, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Shows a dialog which the user has to confirm or abort befor further steps are
     * taken
     * 
     * @param parentWindo
     * @param messageToDisplay
     * @param title
     * @return
     */
    public static int showConfirmationDialg(JFrame parentWindo, String messageToDisplay, String title) {
	return showConfirmDialog(parentWindo, messageToDisplay, title, JOptionPane.YES_NO_OPTION);
    }

    /**
     * Shows a OptionPane as an information-dialog with the message
     * 'messageToDisplay'. This
     * 
     * @param parentWindow,
     *            the parent window
     * @param messageToDisplay
     * @param title
     */
    public static void showWarningDialog(JFrame parentWindow, String messageToDisplay, String title) {
	Toolkit.getDefaultToolkit().beep();
	showMessageDialog(parentWindow, messageToDisplay, title, JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Shows a Dialog with a given message to the user
     * 
     * @param message,
     *            the message to show
     */
    public void showErrorDialog(final String errorMessage, String title) {
	Toolkit.getDefaultToolkit().beep();
	showMessageDialog(null, errorMessage, title, JOptionPane.ERROR_MESSAGE);
    }
}
