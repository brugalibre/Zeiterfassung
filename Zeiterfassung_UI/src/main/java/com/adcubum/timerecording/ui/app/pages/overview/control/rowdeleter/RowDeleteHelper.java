/**
 * 
 */
package com.adcubum.timerecording.ui.app.pages.overview.control.rowdeleter;

import java.util.Optional;

import com.adcubum.timerecording.ui.app.pages.overview.control.callback.BDChangeCallbackHandler;
import com.adcubum.timerecording.ui.app.pages.overview.model.table.BusinessDayIncTableRowValue;
import com.adcubum.timerecording.ui.app.pages.overview.view.OverviewPage;
import com.adcubum.timerecording.ui.app.pages.stopbusinessday.control.FinishAction;

import javafx.event.ActionEvent;
import javafx.scene.control.TableView;
import src.com.myownb3.dominic.timerecording.core.work.businessday.update.callback.BusinessDayChangedCallbackHandler;
import src.com.myownb3.dominic.timerecording.core.work.businessday.update.callback.impl.BusinessDayChangedCallbackHandlerImpl;

/**
 * The {@link RowDeleteHelper} helps deleting a single row from the
 * {@link OverviewPage}
 * 
 * @author Dominic
 * 
 */
public class RowDeleteHelper {

   private BDChangeCallbackHandler uiRefresher;

   public RowDeleteHelper(BDChangeCallbackHandler uiRefresher) {
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

      BusinessDayChangedCallbackHandler handler = new BusinessDayChangedCallbackHandlerImpl();
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
