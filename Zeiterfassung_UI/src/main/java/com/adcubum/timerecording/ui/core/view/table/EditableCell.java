package com.adcubum.timerecording.ui.core.view.table;

import static java.util.Objects.requireNonNull;

import java.util.function.BooleanSupplier;
import java.util.function.Predicate;

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
   private Predicate<String> isEditablePredicate;

   // Converter for converting the text in the text field to the user type, and vice-versa:
   private final StringConverter<T> converter;

   protected EditableCell(StringConverter<T> converter, TextField textField, Predicate<String> isEditablePredicate) {
      this.textField = requireNonNull(textField);
      this.converter = requireNonNull(converter);
      this.isEditablePredicate = requireNonNull(isEditablePredicate);

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
      return (obsValue, oldItem, newItem) -> {
         setText(converter.toString(newItem));
         setCellEditable();
      };
   }

   /**
    * Creates a new {@link EditableCell} with the given StringConverter and a default TextField
    * The given {@link BooleanSupplier} is used to controll weather or not this {@link EditableCell} is editable or not
    * This may be important if certain cells are not editable although the entire column is. I didn't know how to do this better..
    * 
    * @param converter
    *        the used StringConverter
    * @param isEditablePredicate
    *        the {@link Predicate} to determine if this {@link EditableCell} should be able to start a commit
    * @return a new {@link EditableCell} with a default TextField
    */
   public static <S> EditableCell<S, String> createStringEditCell(StringConverter<String> converter, Predicate<String> isEditablePredicate) {
      return new EditableCell<>(converter, new TextField(), isEditablePredicate);
   }

   /**
    * Creates a new {@link EditableCell} with a default TextField
    * 
    * @return a new {@link EditableCell} with a default TextField
    */
   public static <S> EditableCell<S, String> createStringEditCell() {
      return new EditableCell<>(new DefaultStringConverter(), new TextField(), text -> true);
   }

   protected void enableReadOnlyMode() {
      setContentDisplay(ContentDisplay.TEXT_ONLY);
   }

   @Override
   public void startEdit() {
      if (isEditable()) {
         startEditIfEditable();
      }
   }

   private void startEditIfEditable() {
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

   private void setCellEditable() {
      String currentText = converter.toString(getItem());
      boolean isCellEditable = isEditablePredicate.test(currentText);
      setEditable(isCellEditable);
   }
}
