/**
 * 
 */
package com.myownb3.dominic.ui.views.userinput;

import java.awt.Color;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JTextField;

import com.myownb3.dominic.ui.styles.color.Colors;
import com.myownb3.dominic.util.parser.NumberFormat;
import com.myownb3.dominic.util.utils.StringUtil;

/**
 * @author Dominic
 *
 */
public class InputFieldVerifier extends InputVerifier {

    private InputMask inputMask;

    /**
     * @param inputMask
     */
    public InputFieldVerifier(InputMask inputMask) {
	this.inputMask = inputMask;
    }

    @Override
    public boolean verify(JComponent c) {
	if (c instanceof JTextField) {
	    return verifyTextField((JTextField) c);
	} else if (c instanceof ComboBox) {
	    return verifyComboBox((ComboBox) c);
	}
	return true;// unknown input
    }

    private boolean verifyComboBox(ComboBox c) {
	boolean isValid = StringUtil.isNotEmptyOrNull(c.getSelectedItem());
	if (!isValid) {
	    c.setForeground(Colors.INVALID_INPUT_COLOR);
	    c.setBackground(Colors.INVALID_INPUT_COLOR);
	} else {
	    c.setForeground(Color.BLACK);
	}
	return isValid;
    }

    private boolean verifyTextField(JTextField c) {
	String text = ((JTextField) c).getText();
	float val = 0;
	try {
	    val = NumberFormat.parseFloat(text);
	} catch (NumberFormatException e) {
	    handleInvalidInput(c);
	    return false;
	}
	if (val != 0) {
	    handleValidInput(c);
	    return true;
	}
	return false;
    }

    public void handleInvalidInput(JComponent c) {
	((JTextField) c).selectAll();
	((JTextField) c).setForeground(Colors.INVALID_INPUT_COLOR);
    }

    public void handleValidInput(JComponent c) {
	((JTextField) c).setForeground(Color.BLACK);
	inputMask.changeStateUpTo();
    }
}
