/**
 * 
 */
package com.myownb3.dominic.ui.app.inputfield;

import static com.myownb3.dominic.util.parser.NumberFormat.neutralizeDecimalSeparator;

import java.text.NumberFormat;
import java.text.ParsePosition;

import com.myownb3.dominic.ui.app.styles.Styles;

import javafx.scene.Node;
import javafx.scene.control.TextField;

/**
 * @author Dominic
 *
 */
public class InputFieldVerifier {

   public boolean verify(TextField c) {
      return verifyTextField(c);
   }

   private boolean verifyTextField(TextField textField) {

      NumberFormat formatter = NumberFormat.getInstance();
      String text = neutralizeDecimalSeparator(textField.getText().trim(), formatter);
      if (text.isEmpty()) {
         addOrRemoveErrorStyle(textField, false);
         return false;
      }
      boolean isValidNumber = false;
      try {
         ParsePosition pos = new ParsePosition(0);
         Number number = formatter.parse(text, pos);
         isValidNumber = number.doubleValue() > 0.0;
      } catch (NumberFormatException e) {
         e.printStackTrace();
      }
      addOrRemoveErrorStyle(textField, isValidNumber);
      return isValidNumber;
   }

   private void addOrRemoveErrorStyle(Node node, boolean isValid) {
      if (isValid) {
         node.getStyleClass().removeAll(Styles.INVALID_INPUT_LABEL);
      } else {
         node.getStyleClass().add(Styles.INVALID_INPUT_LABEL);
      }
   }
}
