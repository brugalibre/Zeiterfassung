/**
 * 
 */
package com.myownb3.dominic.ui.app.inputfield;

import java.text.NumberFormat;
import java.text.ParsePosition;

import com.myownb3.dominic.ui.app.styles.Styles;
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
      return verifyTextField(c);
   }

   public boolean verifyNotNull(ComboBox<String> comboBox) {
      return verifyComboBoxValueNotNull(comboBox);
   }

   private boolean verifyTextField(TextField textField) {

      String text = textField.getText().trim();
      if (text.isEmpty()) {
         return false;
      }
      boolean isValidNumber = false;
      try {
         NumberFormat formatter = NumberFormat.getInstance();
         ParsePosition pos = new ParsePosition(0);
         Number number = formatter.parse(text, pos);
         isValidNumber = number.doubleValue() > 0.0;
      } catch (NumberFormatException e) {
         e.printStackTrace();
      }
      addOrRemoveErrorStyle(textField, isValidNumber);
      return isValidNumber;
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
