/**
 * 
 */
package com.adcubum.timerecording.ui.app.pages.overview.control.rowdeleter;

import java.util.Optional;

import com.adcubum.timerecording.core.work.businessday.update.callback.BusinessDayChangedCallbackHandler;
import com.adcubum.timerecording.core.work.businessday.update.callback.impl.BusinessDayChangedCallbackHandlerImpl;
import com.adcubum.timerecording.ui.app.pages.overview.control.callback.BDChangedUiCallbackHandler;
import com.adcubum.timerecording.ui.app.pages.overview.model.table.BusinessDayIncTableRowValue;
import com.adcubum.timerecording.ui.app.pages.overview.view.OverviewPage;
import com.adcubum.timerecording.ui.app.pages.stopbusinessday.control.FinishAction;

import javafx.event.ActionEvent;
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

   public RowDeleteHelper(BDChangedUiCallbackHandler uiRefresher) {
      this.handler = new BusinessDayChangedCallbackHandlerImpl();
      this.uiRefresher = uiRefresher;
   }

   /**
    * Deletes the row which is currently selected on the given {@link TableView}
    * 
    * @param event
    *        the action event which triggered us
    * @param tableView
    *        the table-view
    */
   public void deleteRow(ActionEvent event, TableView<BusinessDayIncTableRowValue> tableView) {
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
}
