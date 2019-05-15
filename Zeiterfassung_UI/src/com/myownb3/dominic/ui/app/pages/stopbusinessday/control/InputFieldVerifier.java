/**
 * 
 */
package com.myownb3.dominic.ui.app.pages.stopbusinessday.control;

import com.myownb3.dominic.util.parser.NumberFormat;

import javafx.scene.control.TextField;

/**
 * @author Dominic
 *
 */
public class InputFieldVerifier {

    public boolean verify(TextField c) {
	return verifyTextField((TextField) c);
    }

    private boolean verifyTextField(TextField textField) {

	String text = textField.getText();
	float val = -1;
	try {
	    val = NumberFormat.parseFloat(text.trim());
	} catch (NumberFormatException e) {
	    System.err.println(e);
	}
	return val > 0;
    }
}
