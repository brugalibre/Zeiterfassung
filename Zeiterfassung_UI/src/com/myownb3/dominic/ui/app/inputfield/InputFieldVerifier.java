/**
 * 
 */
package com.myownb3.dominic.ui.app.inputfield;

import static com.myownb3.dominic.util.parser.NumberFormat.neutralizeDecimalSeparator;
import static java.util.Objects.nonNull;

import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.myownb3.dominic.ui.app.styles.Styles;
import com.myownb3.dominic.util.utils.StringUtil;

import javafx.scene.Node;
import javafx.scene.control.TextField;

/**
 * @author Dominic
 *
 */
public class InputFieldVerifier {

   /**
    * Verifies if the text value of the given {@link TextField} matches the given pattern.
    * If it does not matches the pattern, the {@link Styles#INVALID_INPUT_LABEL} is added to the given TextField
    * 
    * @param textField
    *        the TextLabel which provides the string value to verify
    * @param patternAsString
    *        the pattern
    * @param addErrorStyle
    *        Determines if the style of the given textfield changes if it's value is invalid
    * @return <code>true</code> if the text value from the given TextField matches the pattern or <code>false</code> if not
    */
   public boolean isStringMatchingPattern(TextField textField, String patternAsString, boolean addErrorStyle) {
      Pattern pattern = Pattern.compile(patternAsString);
      Matcher matcher = pattern.matcher(textField.getText());
      boolean matches = matcher.matches();
      if (addErrorStyle) {
         addOrRemoveErrorStyle(textField, matches);
      }
      return matches;
   }

   /**
    * Verifies if the text value of the given {@link TextField} is valid numeric value.
    * If it's not, then the {@link Styles#INVALID_INPUT_LABEL} is added to the given TextField
    * 
    * @param textField
    *        the TextLabel which provides the string value to verify
    * @param addErrorStyle
    *        Determines if the style of the given textfield changes if it's value is invalid
    * @return <code>true</code> if the text value from the given TextField is valid numeric value or <code>false</code> if not
    */
   public boolean verify(TextField c, boolean addErrorStyle) {
      return verifyTextField(c, addErrorStyle);
   }

   public static boolean addOrRemoveErrorStyleAndReturnValidationRes(Node node, boolean isInputValid) {
      addOrRemoveErrorStyle(node, isInputValid);
      return isInputValid;
   }

   public boolean verifyString(Node node, String value) {
      return addOrRemoveErrorStyleAndReturnValidationRes(node, !StringUtil.isEmptyOrNull(value));
   }

   public boolean verifyString(Node node, char[] value) {
      // I don't care about empty passwords so.. feel free
      return addOrRemoveErrorStyleAndReturnValidationRes(node, nonNull(value) && value.length > 0);
   }

   private boolean verifyTextField(TextField textField, boolean addErrorStyle) {
      NumberFormat formatter = NumberFormat.getInstance();
      String text = neutralizeDecimalSeparator(textField.getText().trim(), formatter);
      if (text.isEmpty()) {
         if (addErrorStyle) {
            addOrRemoveErrorStyle(textField, false);
         }
         return false;
      }
      boolean isValidNumber = false;
      try {
         ParsePosition pos = new ParsePosition(0);
         Number number = formatter.parse(text, pos);
         isValidNumber = number.doubleValue() > 0.0;
      } catch (NumberFormatException | NullPointerException e) {
         e.printStackTrace();
      }
      if (addErrorStyle) {
         addOrRemoveErrorStyle(textField, isValidNumber);
      }
      return isValidNumber;
   }

   private static void addOrRemoveErrorStyle(Node node, boolean isValid) {
      if (isValid) {
         node.getStyleClass().removeAll(Styles.INVALID_INPUT_LABEL);
      } else {
         node.getStyleClass().add(Styles.INVALID_INPUT_LABEL);
      }
   }
}
