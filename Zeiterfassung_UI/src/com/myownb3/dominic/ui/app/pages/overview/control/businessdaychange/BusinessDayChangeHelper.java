/**
 * 
 */
package com.myownb3.dominic.ui.app.pages.overview.control.businessdaychange;

import com.myownb3.dominic.timerecording.core.callbackhandler.BusinessDayChangedCallbackHandler;
import com.myownb3.dominic.timerecording.core.callbackhandler.impl.BusinessDayChangedCallbackHandlerImpl;
import com.myownb3.dominic.timerecording.core.callbackhandler.impl.ChangedValue;
import com.myownb3.dominic.timerecording.core.work.businessday.BusinessDay;
import com.myownb3.dominic.timerecording.core.work.businessday.BusinessDayIncrement;
import com.myownb3.dominic.timerecording.core.work.businessday.ValueTypes;
import com.myownb3.dominic.ui.app.pages.overview.control.callback.BDChangeCallbackHandler;
import com.myownb3.dominic.ui.app.pages.overview.model.table.BusinessDayIncTableRowValue;
import com.myownb3.dominic.ui.app.pages.stopbusinessday.control.FinishAction;

import javafx.event.EventHandler;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TablePosition;

/**
 * 
 * The {@link BusinessDayChangeHelper} helps to change a
 * {@link BusinessDayIncrement}. It listens on the table cell and calls the
 * action to change the underlying {@link BusinessDay} if the values
 * of the table-view changes
 * 
 * @author Dominic
 * 
 */
public class BusinessDayChangeHelper implements EventHandler<CellEditEvent<BusinessDayIncTableRowValue, String>> {

   private BDChangeCallbackHandler uiRefresher;
   private BusinessDayChangedCallbackHandler handler;

   public BusinessDayChangeHelper(BDChangeCallbackHandler uiRefresher) {
      this.uiRefresher = uiRefresher;
      this.handler = new BusinessDayChangedCallbackHandlerImpl();
   }

   @Override
   public void handle(CellEditEvent<BusinessDayIncTableRowValue, String> event) {

      BusinessDayIncTableRowValue businessDayIncTableCellValue = event.getRowValue();
      TablePosition<BusinessDayIncTableRowValue, String> tablePosition = event.getTablePosition();
      String newValue = event.getNewValue();

      ValueTypes valueType = businessDayIncTableCellValue.getChangeValueTypeForIndex(tablePosition.getColumn());
      if (isNotValidValueType(valueType)) {
         uiRefresher.onFinish(FinishAction.ABORT);
         return;
      }
      int orderNumber = Integer.parseInt(businessDayIncTableCellValue.getNumber());
      handler.handleBusinessDayChanged(ChangedValue.of(orderNumber, newValue, valueType));
      uiRefresher.onFinish(FinishAction.FINISH);
   }

   private static boolean isNotValidValueType(ValueTypes valueType) {
      // XXX Ugly hack. Since we had to make present values editable
      return valueType == ValueTypes.NONE;
   }
}
