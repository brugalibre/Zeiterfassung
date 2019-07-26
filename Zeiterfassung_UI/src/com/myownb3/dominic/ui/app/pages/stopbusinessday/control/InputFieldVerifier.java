/**
 * 
 */
package com.myownb3.dominic.ui.app.pages.stopbusinessday.control;

import com.myownb3.dominic.ui.app.styles.Styles;
import com.myownb3.dominic.util.parser.NumberFormat;
import com.myownb3.dominic.util.utils.StringUtil;

import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

/**
 * @author Dominic
 *
 */
public class InputFieldVerifier {

    public boolean verify(TextField c) {
	return verifyTextField((TextField) c);
    }
    
    public boolean verifyNotNull(ComboBox<String> comboBox) {
	return verifyComboBoxValueNotNull( comboBox);
    }

    private boolean verifyTextField(TextField textField) {

	String text = textField.getText();
	float val = -1;
	try {
	    val = NumberFormat.parseFloat(text.trim());
	} catch (NumberFormatException e) {
	    System.err.println(e);
	}
	addOrRemoveErrorStyle(textField, val > 0);
	return val > 0;
    }
    
    private boolean verifyComboBoxValueNotNull(ComboBox<String> comboBox) {

	String text = comboBox.getSelectionModel().getSelectedItem();
	boolean isValid = !StringUtil.isEmptyOrNull(text);

	addOrRemoveErrorStyle(comboBox, isValid);
	return isValid;
    }

    private void addOrRemoveErrorStyle(Node node, boolean isValid) {
	if (isValid) {
	    node.getStyleClass().removeAll(Styles.INVALID_INPUT_LABEL);
	} else {
	    node.getStyleClass().add(Styles.INVALID_INPUT_LABEL);
	}
    }
}
