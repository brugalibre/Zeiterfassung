/**
 * 
 */
package com.myownb3.dominic.ui.views.userinput;

import java.awt.Color;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JTextField;

import com.myownb3.dominic.ui.styles.color.Colors;

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
	String text = ((JTextField) c).getText();
	float val = 0;
	try {
	    val = Float.parseFloat(text);
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
