/**
 * 
 */
package com.myownb3.dominic.ui.app.pages.stopbusinessday.control;

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
      return verifyTextField((TextField) c);
   }

   public boolean verifyNotNull(ComboBox<String> comboBox) {
      return verifyComboBoxValueNotNull(comboBox);
   }

   private boolean verifyTextField(TextField textField) {

      String text = textField.getText().trim();
      boolean isNumber = false;
      try {
         NumberFormat formatter = NumberFormat.getInstance();
         ParsePosition pos = new ParsePosition(0);
         formatter.parse(text, pos);
         isNumber = text.length() == pos.getIndex();
      } catch (NumberFormatException e) {
         System.err.println(e);
      }
      addOrRemoveErrorStyle(textField, isNumber);
      return isNumber;
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
