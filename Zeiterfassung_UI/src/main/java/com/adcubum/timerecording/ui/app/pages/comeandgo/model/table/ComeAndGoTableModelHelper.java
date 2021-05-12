package com.adcubum.timerecording.ui.app.pages.comeandgo.model.table;


import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import org.apache.batik.ext.awt.image.PadMode;

import com.adcubum.librarys.text.res.TextLabel;
import com.adcubum.timerecording.core.work.businessday.TimeSnippet;
import com.adcubum.timerecording.ui.app.inputfield.TimeStampStringConverter;
import com.adcubum.timerecording.ui.app.pages.comeandgo.model.ComeAndGoDataModel;
import com.adcubum.timerecording.ui.app.pages.comeandgo.model.ComeAndGoOverviewPageModel;
import com.adcubum.timerecording.ui.app.pages.overview.model.table.TimeSnippetCellValue;
import com.adcubum.timerecording.ui.core.view.table.EditableCell;
import com.adcubum.util.utils.StringUtil;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

public class ComeAndGoTableModelHelper {

   private TableView<ComeAndGoTableRowValue> comeAndGoTableView;

   public ComeAndGoTableModelHelper(TableView<ComeAndGoTableRowValue> comeAndGoTableView) {
      this.comeAndGoTableView = comeAndGoTableView;
   }

   /**
    * Creates a new {@link ComeAndGoTableModelHelper} which is responsible for creating the necessary columns and binds the necessary
    * TableView-properties to the given {@link PadMode}
    * 
    * @param comeAndGoDataModels
    *        the {@link ComeAndGoDataModel}s
    * @param changeListener
    *        the listener whenever the cell values change
    */
   public void bindTable2DataModel(ComeAndGoOverviewPageModel dataModel) {
      List<TableColumn<ComeAndGoTableRowValue, String>> columnNames = getTableHeaders(dataModel);
      comeAndGoTableView.setEditable(true);
      comeAndGoTableView.getSelectionModel().cellSelectionEnabledProperty().set(true);
      comeAndGoTableView.getColumns().setAll(columnNames);
      comeAndGoTableView.itemsProperty().bindBidirectional(dataModel.getComeAndGoTableRowValuesProperty());
   }

   /**
    * Resizes the TableView so that all columns have the proper size and fill out the given width of the TableView
    */
   public void fitTableSize() {
      TableColumn<ComeAndGoTableRowValue, ?> comeColumn = getColumnById(TextLabel.COME_OR_GO_COME);
      comeColumn.setPrefWidth(Math.floor(comeAndGoTableView.getPrefWidth() / 4));
      TableColumn<ComeAndGoTableRowValue, ?> goColumn = getColumnById(TextLabel.COME_OR_GO_GO);
      goColumn.setPrefWidth(Math.floor(comeAndGoTableView.getPrefWidth() / 4));
      TableColumn<ComeAndGoTableRowValue, ?> existsRecordTableColumn = getColumnById(TextLabel.EXISTS_RECORD_FOR_COME_AND_GO);
      existsRecordTableColumn.setPrefWidth(Math.floor(comeAndGoTableView.getPrefWidth() / 2));
   }

   private List<TableColumn<ComeAndGoTableRowValue, String>> getTableHeaders(
         EventHandler<CellEditEvent<ComeAndGoTableRowValue, String>> changeListener) {
      List<TableColumn<ComeAndGoTableRowValue, String>> titleHeaders = new ArrayList<>();
      TableColumn<ComeAndGoTableRowValue, String> comeTableColumn = new TableColumn<>(TextLabel.COME_OR_GO_COME);
      comeTableColumn.setId(TextLabel.COME_OR_GO_COME);
      setEditableCellValueFactory(comeTableColumn, ComeAndGoTableRowValue.COME, changeListener);
      comeTableColumn
            .setCellValueFactory(getTimeSnippetCellValueFactory(tableRowValue -> tableRowValue.getComeTimeSnipptedValueProperty().getValue()));
      titleHeaders.add(comeTableColumn);

      TableColumn<ComeAndGoTableRowValue, String> goTableColumn = new TableColumn<>(TextLabel.COME_OR_GO_GO);
      goTableColumn.setId(TextLabel.COME_OR_GO_GO);
      setEditableCellValueFactory(goTableColumn, ComeAndGoTableRowValue.GO, changeListener);
      goTableColumn.setCellValueFactory(getTimeSnippetCellValueFactory(tableRowValue -> tableRowValue.getGoTimeSnipptedValueProperty().getValue()));
      titleHeaders.add(goTableColumn);

      TableColumn<ComeAndGoTableRowValue, String> existsRecordTableColumn = new TableColumn<>(TextLabel.EXISTS_RECORD_FOR_COME_AND_GO);
      existsRecordTableColumn.setId(TextLabel.EXISTS_RECORD_FOR_COME_AND_GO);
      setNonEditableCellValueFactory(existsRecordTableColumn, ComeAndGoTableRowValue.EXISTING_RECORD);
      titleHeaders.add(existsRecordTableColumn);
      return titleHeaders;
   }

   private Callback<CellDataFeatures<ComeAndGoTableRowValue, String>, ObservableValue<String>> getTimeSnippetCellValueFactory(
         Function<ComeAndGoTableRowValue, TimeSnippetCellValue> timeStampBeginOrEndRep) {
      return cellData -> {
         TimeSnippetCellValue timeSnippetCellValue = timeStampBeginOrEndRep.apply(cellData.getValue());
         return new SimpleStringProperty(timeSnippetCellValue.getBeginOrEnd());
      };
   }

   private void setNonEditableCellValueFactory(TableColumn<ComeAndGoTableRowValue, String> tableColumn, String paramName) {
      tableColumn.setCellValueFactory(new PropertyValueFactory<>(paramName));
   }

   private void setEditableCellValueFactory(TableColumn<ComeAndGoTableRowValue, String> tableColumn, String paramName,
         EventHandler<CellEditEvent<ComeAndGoTableRowValue, String>> changeListener) {
      tableColumn.setEditable(true);
      tableColumn.setOnEditCommit(changeListener);
      tableColumn.setCellFactory(column -> EditableCell.createStringEditCell(new TimeStampStringConverter(), isNotNullValue()));
      setNonEditableCellValueFactory(tableColumn, paramName);// For diplaying the value read only
   }

   private static Predicate<String> isNotNullValue() {
      return currVal -> !StringUtil.isEqual(currVal, TimeSnippet.NULL_TIME_REP);
   }

   private TableColumn<ComeAndGoTableRowValue, ?> getColumnById(String id) {
      return comeAndGoTableView.getColumns()
            .stream()
            .filter(tableColumn -> StringUtil.isEqual(tableColumn.getId(), id))
            .findFirst()
            .orElseThrow(IllegalStateException::new);
   }
}
