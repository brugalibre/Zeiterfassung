/**
 * 
 */
package com.adcubum.timerecording.ui.app.pages.comeandgo.model.table;

import com.adcubum.librarys.text.res.TextLabel;
import com.adcubum.timerecording.core.work.businessday.TimeSnippet;
import com.adcubum.timerecording.core.work.businessday.ValueTypes;
import com.adcubum.timerecording.ui.app.pages.comeandgo.model.ComeAndGoDataModel;
import com.adcubum.timerecording.ui.app.pages.overview.model.table.TimeSnippetCellValue;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.EventHandler;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;

/**
 * The {@link ComeAndGoTableRowValue} represents a single row in a
 * {@link TableView}. There are exactly two {@link TimeSnippet}s - one for the begin and another for the end
 * 
 * @author Dominic
 *
 */
public class ComeAndGoTableRowValue implements EventHandler<CellEditEvent<ComeAndGoTableRowValue, String>> {

   public static final String EXISTING_RECORD = "isTimeRecorded";
   public static final String GO = "goTimeSnipptedValue";
   public static final String COME = "comeTimeSnipptedValue";
   private StringProperty isTimeRecordedProperty;
   private ObjectProperty<TimeSnippetCellValue> comeTimeSnipptedValueProperty;

   private ObjectProperty<TimeSnippetCellValue> goTimeSnipptedValueProperty;

   private ComeAndGoDataModel comeAndGoDataModel;

   /**
    * Creates a new empty {@link ComeAndGoTableRowValue}
    * 
    * @param comeAndGoDataModel
    */
   private ComeAndGoTableRowValue(ComeAndGoDataModel comeAndGoDataModel) {
      TimeSnippet timeSnippet = comeAndGoDataModel.getTimeSnippet();
      this.comeAndGoDataModel = comeAndGoDataModel;
      this.isTimeRecordedProperty = new SimpleStringProperty(comeAndGoDataModel.isNotRecorded() ? TextLabel.NEIN : TextLabel.JA);
      this.comeTimeSnipptedValueProperty = new SimpleObjectProperty<>(TimeSnippetCellValue.of(timeSnippet.getBeginTimeStampRep(), ValueTypes.BEGIN));
      this.goTimeSnipptedValueProperty = new SimpleObjectProperty<>(TimeSnippetCellValue.of(timeSnippet.getEndTimeStampRep(), ValueTypes.END));
   }

   public final String getIsTimeRecorded() {
      return this.isTimeRecordedProperty.get();
   }

   public ObjectProperty<TimeSnippetCellValue> getComeTimeSnipptedValueProperty() {
      return comeTimeSnipptedValueProperty;
   }

   public ObjectProperty<TimeSnippetCellValue> getGoTimeSnipptedValueProperty() {
      return goTimeSnipptedValueProperty;
   }

   /**
    * Creates a new {@link ComeAndGoTableRowValue} for the given {@link ComeAndGoDataModel}
    * 
    * @param comeAndGoDataModel
    *        the given {@link ComeAndGoDataModel}
    * @return a new {@link ComeAndGoTableRowValue}
    */
   public static ComeAndGoTableRowValue of(ComeAndGoDataModel comeAndGoDataModel) {
      return new ComeAndGoTableRowValue(comeAndGoDataModel);
   }

   @Override
   public void handle(CellEditEvent<ComeAndGoTableRowValue, String> event) {
      String newValue = event.getNewValue();
      TablePosition<ComeAndGoTableRowValue, String> tablePosition = event.getTablePosition();
      comeAndGoDataModel.changeComeOrGo(tablePosition.getColumn() == 0, newValue);
   }
}
