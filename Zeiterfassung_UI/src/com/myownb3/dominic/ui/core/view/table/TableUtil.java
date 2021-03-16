package com.myownb3.dominic.ui.core.view.table;

import com.myownb3.dominic.ui.app.pages.overview.model.table.BusinessDayTableModelHelper;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumnBase;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;

public class TableUtil {

   private TableUtil() {
      // private 
   }

   /**
    * Resizes the width of all cells and set the sum of all cell-width as the prefered width of the given TableView.
    * By doing so, we avoid that the last column takes up all the space which may be left
    * 
    * <b> Note that somehow it may happen, that nothing has changed, the first time we show the table.. </b>
    * 
    * @param tableView
    *        the table which should be resized
    */
   public static void autoResizeTable(TableView<?> tableView) {
      resize(tableView);
      double currentWidth = getNewWidth(tableView);
      tableView.prefWidthProperty().set(currentWidth);
   }

   private static double getNewWidth(TableView<?> tableView) {
      int margin = 5; // a little margin, otherwise we get a scrollbar when the actual table is the same size than the node containing it
      return tableView.getColumns()
            .stream()
            .mapToDouble(TableColumnBase::getPrefWidth)
            .sum() + margin;
   }

   private static void resize(TableView<?> tableView) {
      tableView.getColumns()
            .stream()
            .forEach(column -> {
               double max = evalPrefWidth4Column(tableView, column);
               column.setPrefWidth(max);
            });
   }

   private static double evalPrefWidth4Column(TableView<?> table, TableColumn<?, ?> column) {
      Text textNode = new Text(column.getText());
      double max = textNode.getLayoutBounds().getWidth();
      for (int i = 0; i < table.getItems().size(); i++) {
         if (isNotEmpty(column, i)) {
            textNode = new Text(column.getCellData(i).toString());
            double calcwidth = textNode.getLayoutBounds().getWidth();
            if (BusinessDayTableModelHelper.TICKET_COLUMN_ID.equals(column.getId())) {// nicht sehr schön, aber irgendwie gibt die Spalte viel zu viel platz an, als sie wirklich benötigt..
               calcwidth = calcwidth / 10;
            }
            max = Math.max(max, calcwidth);
         }
      }
      return max + 15; // A little bit more, give the cell some space to breath..
   }

   private static boolean isNotEmpty(TableColumn<?, ?> column, int i) {
      return column.getCellData(i) != null;
   }
}
