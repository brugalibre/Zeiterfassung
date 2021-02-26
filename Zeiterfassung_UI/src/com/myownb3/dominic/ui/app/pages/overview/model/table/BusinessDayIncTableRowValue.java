/**
 * 
 */
package com.myownb3.dominic.ui.app.pages.overview.model.table;

import java.util.HashMap;
import java.util.Map;

import com.myownb3.dominic.timerecording.core.work.businessday.TimeSnippet;
import com.myownb3.dominic.timerecording.core.work.businessday.ValueTypes;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TableView;

/**
 * The {@link BusinessDayIncTableRowValue} represents a single row in a
 * {@link TableView}. There are exactly two {@link TimeSnippet}s - one for the begin and another for the end
 * 
 * @author Dominic
 *
 */
public class BusinessDayIncTableRowValue {

   private StringProperty numberProperty;
   private StringProperty totalDurationProperty;
   private StringProperty ticketNumberProperty;
   private StringProperty descriptionProperty;
   private StringProperty chargeTypeProperty;
   private StringProperty isBookedProperty;

   private BeginAndEndCellValue beginAndEndCellValue;
   private Map<Integer, ValueTypes> valueTypesForIndex;

   /**
    * Creates a new empty {@link BusinessDayIncTableRowValue}
    */
   public BusinessDayIncTableRowValue() {
      numberProperty = new SimpleStringProperty();
      totalDurationProperty = new SimpleStringProperty();
      ticketNumberProperty = new SimpleStringProperty();
      descriptionProperty = new SimpleStringProperty();
      chargeTypeProperty = new SimpleStringProperty();
      isBookedProperty = new SimpleStringProperty();
      valueTypesForIndex = new HashMap<>();
   }

   /**
    * @param isDescriptionTitleNecessary
    */
   public void setValueTypes(boolean isDescriptionTitleNecessary) {
      int index = 0;
      valueTypesForIndex.put(index, ValueTypes.NONE);
      index++;
      valueTypesForIndex.put(index, ValueTypes.AMOUNT_OF_TIME);
      index++;
      valueTypesForIndex.put(index, ValueTypes.TICKET_NR);
      index++;
      if (isDescriptionTitleNecessary) {
         valueTypesForIndex.put(index, ValueTypes.DESCRIPTION);
         index++;
      }
      valueTypesForIndex.put(index, beginAndEndCellValue.getBeginCellValue().getValueType());
      index++;
      valueTypesForIndex.put(index, beginAndEndCellValue.getEndCellValue().getValueType());
      index++;
      valueTypesForIndex.put(index, ValueTypes.CHARGE_TYPE);
   }

   /**
    * Returns the type of value for the element at the given index
    * 
    * @param index
    *        the index
    * @return the type of value for the element at the given index
    */
   public ValueTypes getChangeValueTypeForIndex(int index) {
      return valueTypesForIndex.get(index);
   }

   /**
    * @return the begin {@link TimeSnippetCellValue}
    * @see BeginAndEndCellValue#getBeginCellValue()
    */
   public TimeSnippetCellValue getBeginTimeSnippet() {
      return beginAndEndCellValue.getBeginCellValue();
   }

   /**
    * @return the {@link TimeSnippetCellValue} for the given index
    * @see BeginAndEndCellValue#getEndCellValue()
    */
   public TimeSnippetCellValue getEndTimeSnippet() {
      return beginAndEndCellValue.getEndCellValue();
   }

   /**
    * @param timeSnippets
    */
   public void setTimeSnippets(BeginAndEndCellValue beginAndEndCellValue) {
      this.beginAndEndCellValue = beginAndEndCellValue;
   }

   public final String getNumber() {
      return this.numberProperty.get();
   }

   public final String getTotalDuration() {
      return this.totalDurationProperty.get();
   }

   public final String getTicketNumber() {
      return this.ticketNumberProperty.get();
   }

   public final String getDescription() {
      return this.descriptionProperty.get();
   }

   public final String getChargeType() {
      return this.chargeTypeProperty.get();
   }

   public StringProperty chargeTypeProperty() {
      return chargeTypeProperty;
   }

   public final String getIsBooked() {
      return this.isBookedProperty.get();
   }

   public final void setIsBooked(String isCharged) {
      this.isBookedProperty.set(isCharged);
   }

   public final void setNumber(String number) {
      this.numberProperty.set(number);
   }

   public final void setTotalDuration(String totalDuration) {
      this.totalDurationProperty.set(totalDuration);
   }

   public final void setTicketNumber(String ticketNumber) {
      this.ticketNumberProperty.set(ticketNumber);
   }

   public final void setDescription(String description) {
      this.descriptionProperty.set(description);
   }

   public final void setChargeType(String chargeType) {
      this.chargeTypeProperty.set(chargeType);
   }

   public int getNumberAsInt() {
      return Integer.valueOf(getNumber()) - 1;// minus one, since the index start at 1
   }
}
