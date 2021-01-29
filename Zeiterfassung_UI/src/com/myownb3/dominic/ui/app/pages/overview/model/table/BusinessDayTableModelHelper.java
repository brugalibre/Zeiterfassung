package com.myownb3.dominic.ui.app.pages.overview.model.table;

import static javafx.collections.FXCollections.observableList;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.myownb3.dominic.librarys.text.res.TextLabel;
import com.myownb3.dominic.timerecording.core.charge.ChargeType;
import com.myownb3.dominic.timerecording.core.work.businessday.TimeSnippet;
import com.myownb3.dominic.timerecording.core.work.businessday.ValueTypes;
import com.myownb3.dominic.timerecording.core.work.businessday.vo.BusinessDayIncrementVO;
import com.myownb3.dominic.timerecording.core.work.businessday.vo.BusinessDayVO;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import javafx.util.converter.DefaultStringConverter;

public class BusinessDayTableModelHelper {

   private List<BusinessDayIncTableRowValue> colmnValues;
   private List<TableColumn<BusinessDayIncTableRowValue, String>> columnNames;
   private EventHandler<CellEditEvent<BusinessDayIncTableRowValue, String>> changeListener;

   public BusinessDayTableModelHelper(EventHandler<CellEditEvent<BusinessDayIncTableRowValue, String>> changeListener) {
      columnNames = new ArrayList<>();
      colmnValues = new ArrayList<>();
      this.changeListener = changeListener;
   }

   public void init(BusinessDayVO bussinessDay, TableView<BusinessDayIncTableRowValue> tableView) {
      Objects.requireNonNull(bussinessDay);
      this.colmnValues = getBusinessDayCells(bussinessDay);
      this.columnNames = getTableHeaders(bussinessDay);
      tableView.setEditable(true);
      tableView.getSelectionModel().cellSelectionEnabledProperty().set(true);
      tableView.getColumns().setAll(columnNames);
      ObservableList<BusinessDayIncTableRowValue> observableList = observableList(colmnValues);
      tableView.setItems(observableList);
   }

   private List<TableColumn<BusinessDayIncTableRowValue, String>> getTableHeaders(BusinessDayVO bussinessDay) {
      List<TableColumn<BusinessDayIncTableRowValue, String>> titleHeaders = new ArrayList<>();
      TableColumn<BusinessDayIncTableRowValue, String> numberTableColumn = new TableColumn<>(
            TextLabel.NUMMER_LABEL);
      setNonEditableCellValueFactory(numberTableColumn, "number");
      titleHeaders.add(numberTableColumn);
      TableColumn<BusinessDayIncTableRowValue, String> amountOfHoursTableColumn = new TableColumn<>(TextLabel.AMOUNT_OF_HOURS_LABEL);
      amountOfHoursTableColumn.setId(TextLabel.AMOUNT_OF_HOURS_LABEL);
      titleHeaders.add(amountOfHoursTableColumn);
      setEditableCellValueFactory(amountOfHoursTableColumn, "totalDuration", changeListener);
      TableColumn<BusinessDayIncTableRowValue, String> ticketTableColumn = new TableColumn<>(TextLabel.TICKET);
      ticketTableColumn.setId(TextLabel.TICKET);
      titleHeaders.add(ticketTableColumn);
      setEditableCellValueFactory(ticketTableColumn, "ticketNumber", changeListener);

      boolean isDescriptionTitleNecessary = bussinessDay.hasIncrementWithDescription();
      if (isDescriptionTitleNecessary) {
         TableColumn<BusinessDayIncTableRowValue, String> descriptionTableColumn = new TableColumn<>(TextLabel.DESCRIPTION_LABEL);
         setEditableCellValueFactory(descriptionTableColumn, "description", changeListener);
         titleHeaders.add(descriptionTableColumn);
      }

      TableColumn<BusinessDayIncTableRowValue, String> beginTableColumn = new TableColumn<>(TextLabel.VON_LABEL);
      beginTableColumn.setId(TextLabel.VON_LABEL);
      TableColumn<BusinessDayIncTableRowValue, String> endTableColumn = new TableColumn<>(TextLabel.BIS_LABEL);
      endTableColumn.setId(TextLabel.BIS_LABEL);
      beginTableColumn.setCellValueFactory(getTimeSnippetBeginCellValueFactory());
      beginTableColumn.setCellFactory(getTimeSnippetBeginOrEndCellFactory(beginTableColumn));
      endTableColumn.setCellValueFactory(getTimeSnippetEndCellValueFactory());
      endTableColumn.setCellFactory(getTimeSnippetBeginOrEndCellFactory(endTableColumn));

      titleHeaders.add(beginTableColumn);
      titleHeaders.add(endTableColumn);

      TableColumn<BusinessDayIncTableRowValue, String> chargeTypeTableColumn = new TableColumn<>(TextLabel.CHARGE_TYPE_LABEL);
      titleHeaders.add(chargeTypeTableColumn);
      setEditableColumBoxCellFactory(chargeTypeTableColumn);
      TableColumn<BusinessDayIncTableRowValue, String> isChargedTableColumn = new TableColumn<>(TextLabel.CHARGED);
      titleHeaders.add(isChargedTableColumn);
      setNonEditableCellValueFactory(isChargedTableColumn, "isCharged");
      return titleHeaders;
   }

   private void setEditableColumBoxCellFactory(
         TableColumn<BusinessDayIncTableRowValue, String> chargeTypeTableColumn) {
      chargeTypeTableColumn.setCellValueFactory(cellData -> cellData.getValue().chargeTypeProperty());
      chargeTypeTableColumn
            .setCellFactory(ComboBoxTableCell.forTableColumn(ChargeType.getLeistungsartenRepresentation()));
      chargeTypeTableColumn.editableProperty().set(true);
      chargeTypeTableColumn.setOnEditCommit(changeListener);
   }

   private Callback<CellDataFeatures<BusinessDayIncTableRowValue, String>, ObservableValue<String>> getTimeSnippetBeginCellValueFactory() {
      return cellData -> {
         BusinessDayIncTableRowValue businessDayIncTableCellValue = cellData.getValue();
         TimeSnippetCellValue beginTimeSnippet = businessDayIncTableCellValue.getBeginTimeSnippet();
         return new SimpleStringProperty(beginTimeSnippet.getBeginOrEnd());
      };
   }

   private Callback<CellDataFeatures<BusinessDayIncTableRowValue, String>, ObservableValue<String>> getTimeSnippetEndCellValueFactory() {
      return cellData -> {
         BusinessDayIncTableRowValue businessDayIncTableCellValue = cellData.getValue();
         TimeSnippetCellValue beginTimeSnippet = businessDayIncTableCellValue.getEndTimeSnippet();
         return new SimpleStringProperty(beginTimeSnippet.getBeginOrEnd());
      };
   }

   private Callback<TableColumn<BusinessDayIncTableRowValue, String>, TableCell<BusinessDayIncTableRowValue, String>> getTimeSnippetBeginOrEndCellFactory(
         TableColumn<BusinessDayIncTableRowValue, String> tableColumn) {
      return cellData -> {
         tableColumn.editableProperty().set(true);
         tableColumn.setOnEditCommit(changeListener);
         tableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
         return new TextFieldTableCell<>(new DefaultStringConverter());
      };
   }

   private void setNonEditableCellValueFactory(TableColumn<BusinessDayIncTableRowValue, String> tableColumn, String paramName) {
      tableColumn.setCellValueFactory(new PropertyValueFactory<>(paramName));
   }

   private void setEditableCellValueFactory(TableColumn<BusinessDayIncTableRowValue, String> tableColumn,
         String paramName, EventHandler<CellEditEvent<BusinessDayIncTableRowValue, String>> changeListener) {
      tableColumn.setEditable(true);
      tableColumn.setOnEditCommit(changeListener);
      Callback<TableColumn<BusinessDayIncTableRowValue, String>, TableCell<BusinessDayIncTableRowValue, String>> callback = TextFieldTableCell
            .forTableColumn();
      tableColumn.setCellFactory(callback);
      setNonEditableCellValueFactory(tableColumn, paramName);// For diplaying the value read only
   }

   private List<BusinessDayIncTableRowValue> getBusinessDayCells(BusinessDayVO businessDay) {
      List<BusinessDayIncTableRowValue> businessDayCells = new ArrayList<>();
      int counter = 0;
      for (BusinessDayIncrementVO bussinessDayIncremental : businessDay.getBusinessDayIncrements()) {
         BusinessDayIncTableRowValue businessDayIncrementalCell = getBusinessDayIncrementalCell(
               bussinessDayIncremental, businessDay.hasIncrementWithDescription(), counter);
         businessDayCells.add(businessDayIncrementalCell);
         counter++;
      }
      return businessDayCells;
   }

   /*
    * Creates a list which contains all Cells that are required to paint a
    * BusinessDayIncremental
    */
   private BusinessDayIncTableRowValue getBusinessDayIncrementalCell(BusinessDayIncrementVO bussinessDayIncremental,
         boolean isDescriptionTitleNecessary, int no) {
      // create Cells for the introduction of a BD-inc.

      BusinessDayIncTableRowValue businessDayIncTableCellValue = new BusinessDayIncTableRowValue();

      businessDayIncTableCellValue.setNumber(String.valueOf(no));
      businessDayIncTableCellValue.setTotalDuration(bussinessDayIncremental.getTotalDurationRep());
      businessDayIncTableCellValue.setTicketNumber(bussinessDayIncremental.getTicketNumber());

      if (isDescriptionTitleNecessary) {
         String cellValue = bussinessDayIncremental.hasDescription() ? bussinessDayIncremental.getDescription() : "";
         businessDayIncTableCellValue.setDescription(cellValue);
      }

      // create Cells for all TimeSnippet's
      businessDayIncTableCellValue.setTimeSnippets(getTimeSnippets(bussinessDayIncremental));
      businessDayIncTableCellValue
            .setChargeType(ChargeType.getRepresentation(bussinessDayIncremental.getChargeType()));
      businessDayIncTableCellValue.setIsCharged(bussinessDayIncremental.isCharged() ? TextLabel.YES : TextLabel.NO);
      businessDayIncTableCellValue.setValueTypes(isDescriptionTitleNecessary);
      return businessDayIncTableCellValue;
   }

   private BeginAndEndCellValue getTimeSnippets(BusinessDayIncrementVO bussinessDayIncremental) {
      TimeSnippet snippet = bussinessDayIncremental.getCurrentTimeSnippet();
      String begin = String.valueOf(snippet.getBeginTimeStampRep());
      String end = String.valueOf(snippet.getEndTimeStamp());
      return new BeginAndEndCellValue(TimeSnippetCellValue.of(begin, ValueTypes.BEGIN), TimeSnippetCellValue.of(end, ValueTypes.END));
   }
}
