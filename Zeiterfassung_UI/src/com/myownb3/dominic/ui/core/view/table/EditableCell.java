package com.myownb3.dominic.ui.core.view.table;

import static java.util.Objects.requireNonNull;

import javafx.beans.value.ChangeListener;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import javafx.util.converter.DefaultStringConverter;

/**
 * 
 * It's basically an editable javafx.scene.control.TableCell which starts a commit by pressing the tab key
 * It supports custom TextField instances if desired
 * 
 * Siehe https://gist.github.com/james-d/be5bbd6255a4640a5357
 * 
 * @author james-d
 *
 * @param <S>
 * @param <T>
 */
public class EditableCell<S, T> extends TableCell<S, T> {

   // Text field for editing
   protected TextField textField;

   // Converter for converting the text in the text field to the user type, and vice-versa:
   private final StringConverter<T> converter;

   protected EditableCell(StringConverter<T> converter, TextField textField) {
      this.textField = requireNonNull(textField);
      this.converter = requireNonNull(converter);

      itemProperty().addListener(getItemChangedListener(converter));
      setGraphic(textField);
      enableReadOnlyMode();

      textField.setOnAction(evt -> commitEdit(this.converter.fromString(textField.getText())));
      textField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
         if (!isNowFocused.booleanValue()) {
            commitEdit(this.converter.fromString(textField.getText()));
         }
      });
   }

   protected ChangeListener<T> getItemChangedListener(StringConverter<T> converter) {
      return (obsValue, oldItem, newItem) -> setText(converter.toString(newItem));
   }

   /**
    * Creates a new {@link EditableCell} with a default TextField
    * 
    * @return a new {@link EditableCell} with a default TextField
    */
   public static <S> EditableCell<S, String> createStringEditCell() {
      return new EditableCell<>(new DefaultStringConverter(), new TextField());
   }

   protected void enableReadOnlyMode() {
      setContentDisplay(ContentDisplay.TEXT_ONLY);
   }

   @Override
   public void startEdit() {
      super.startEdit();
      textField.setText(converter.toString(getItem()));
      setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
      textField.requestFocus();
   }

   // revert to text display
   @Override
   public void cancelEdit() {
      super.cancelEdit();
      enableReadOnlyMode();
   }

   @Override
   public void commitEdit(T item) {
      super.commitEdit(item);
      enableReadOnlyMode();
   }
}
