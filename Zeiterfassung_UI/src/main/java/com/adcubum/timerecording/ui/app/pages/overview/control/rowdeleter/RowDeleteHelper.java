/**
 * 
 */
package com.adcubum.timerecording.ui.app.pages.overview.control.rowdeleter;

import java.util.Optional;

import com.adcubum.librarys.text.res.TextLabel;
import com.adcubum.timerecording.core.work.businessday.BusinessDayIncrement;
import com.adcubum.timerecording.core.work.businessday.update.callback.BusinessDayChangedCallbackHandler;
import com.adcubum.timerecording.core.work.businessday.update.callback.impl.BusinessDayChangedCallbackHandlerFactory;
import com.adcubum.timerecording.ui.app.pages.overview.control.callback.BDChangedUiCallbackHandler;
import com.adcubum.timerecording.ui.app.pages.overview.model.table.BusinessDayIncTableRowValue;
import com.adcubum.timerecording.ui.app.pages.overview.view.OverviewPage;
import com.adcubum.timerecording.ui.app.pages.stopbusinessday.control.FinishAction;

import javafx.event.ActionEvent;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;

/**
 * The {@link RowDeleteHelper} helps deleting a single row from the
 * {@link OverviewPage}
 * 
 * @author Dominic
 * 
 */
public class RowDeleteHelper {

   private BDChangedUiCallbackHandler uiRefresher;
   private BusinessDayChangedCallbackHandler handler;
   private MenuItem deleteMenuItem;

   public RowDeleteHelper(BDChangedUiCallbackHandler uiRefresher, TableView<BusinessDayIncTableRowValue> tableView) {
      this.handler = BusinessDayChangedCallbackHandlerFactory.createNew();
      this.uiRefresher = uiRefresher;
      buildDeleteMenuItem(tableView);
   }

   private void buildDeleteMenuItem(TableView<BusinessDayIncTableRowValue> tableView) {
      this.deleteMenuItem = new MenuItem(TextLabel.DELETE_ROW);
      deleteMenuItem.setOnAction(event -> deleteRow(event, tableView));
   }

   /**
    * Deletes the row which is currently selected on the given {@link TableView}
    * 
    * @param event
    *        the action event which triggered us
    * @param tableView
    *        the table-view
    */
   private void deleteRow(ActionEvent event, TableView<BusinessDayIncTableRowValue> tableView) {
      Optional<BusinessDayIncTableRowValue> optionalBusinessDayIncTableRowValue =
            Optional.ofNullable(tableView.getSelectionModel().getSelectedItem());
      optionalBusinessDayIncTableRowValue.ifPresent(businessDayIncTableRowValue -> {
         handler.handleBusinessDayIncrementDeleted(businessDayIncTableRowValue.getNumberAsInt());
         afterDelete(event);
      });
   }

   private void afterDelete(ActionEvent event) {
      event.consume();
      uiRefresher.onFinish(FinishAction.FINISH);
   }

   /**
    * @return the menu item in order to delete a {@link BusinessDayIncrement}
    */
   public MenuItem getDeleteMenuItem() {
      return deleteMenuItem;
   }
}
