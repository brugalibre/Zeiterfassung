/**
 * 
 */
package com.adcubum.timerecording.ui.app.pages.overview.model.table;

import com.adcubum.timerecording.core.work.businessday.ValueTypes;
import com.adcubum.timerecording.ui.app.pages.overview.view.OverviewPage;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * 
 * A {@link TimeSnippetCellValue} is either a begin-timestamp or an end-timestamp cell on the {@link OverviewPage}-table
 * 
 * @author Dominic
 *
 */
public class TimeSnippetCellValue {

   private StringProperty beginOrEndValueProperty;
   private ObjectProperty<ValueTypes> valueTypeProperty;

   private TimeSnippetCellValue(String beginOrEnd, ValueTypes valueType) {

      this.beginOrEndValueProperty = new SimpleStringProperty(beginOrEnd);
      this.valueTypeProperty = new SimpleObjectProperty<>(valueType);
   }

   public String getBeginOrEnd() {
      return beginOrEndValueProperty.get();
   }

   /**
    * Creates a new {@link TimeSnippetCellValue}
    * 
    * @param beginOrEnd
    *        begin or end value
    * @param valueType
    *        either {@link ValueTypes#BEGIN} or {@link ValueTypes#END}
    * @return a new {@link TimeSnippetCellValue}
    */
   public static TimeSnippetCellValue of(String beginOrEnd, ValueTypes valueType) {
      return new TimeSnippetCellValue(beginOrEnd, valueType);
   }

   public ValueTypes getValueType() {
      return valueTypeProperty.get();
   }
}
