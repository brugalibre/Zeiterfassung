/**
 * 
 */
package com.adcubum.timerecording.ui.app.pages.overview.control.businessdaychange;

import com.adcubum.timerecording.core.work.businessday.BusinessDay;
import com.adcubum.timerecording.core.work.businessday.BusinessDayIncrement;
import com.adcubum.timerecording.core.work.businessday.ValueTypes;
import com.adcubum.timerecording.core.work.businessday.update.callback.BusinessDayChangedCallbackHandler;
import com.adcubum.timerecording.core.work.businessday.update.callback.impl.BusinessDayChangedCallbackHandlerFactory;
import com.adcubum.timerecording.core.work.businessday.update.callback.impl.ChangedValue;
import com.adcubum.timerecording.ui.app.pages.overview.control.callback.BDChangedUiCallbackHandler;
import com.adcubum.timerecording.ui.app.pages.overview.model.table.BusinessDayIncTableRowValue;
import com.adcubum.timerecording.ui.app.pages.stopbusinessday.control.FinishAction;

import javafx.event.EventHandler;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TablePosition;

/**
 * 
 * The {@link AbstractBusinessDayChangeHelper} helps to change a
 * {@link BusinessDayIncrement}. It listens on the table cell and calls the
 * action to change the underlying {@link BusinessDay} if the values
 * of the table-view changes
 * 
 * @author Dominic
 * @param <T>
 * 
 */
public abstract class AbstractBusinessDayChangeHelper<T> implements EventHandler<CellEditEvent<BusinessDayIncTableRowValue, T>> {

   private BDChangedUiCallbackHandler uiRefresher;
   private BusinessDayChangedCallbackHandler handler;

   protected AbstractBusinessDayChangeHelper(BDChangedUiCallbackHandler uiRefresher) {
      this.uiRefresher = uiRefresher;
      this.handler = BusinessDayChangedCallbackHandlerFactory.createNew();
   }

   @Override
   public void handle(CellEditEvent<BusinessDayIncTableRowValue, T> event) {

      BusinessDayIncTableRowValue businessDayIncTableCellValue = event.getRowValue();
      TablePosition<BusinessDayIncTableRowValue, T> tablePosition = event.getTablePosition();
      T newValue = event.getNewValue();

      ValueTypes valueType = businessDayIncTableCellValue.getChangeValueTypeForIndex(tablePosition.getColumn());
      if (isNotValidValueType(valueType)) {
         uiRefresher.onFinish(FinishAction.ABORT);
         return;
      }
      int orderNumber = Integer.parseInt(businessDayIncTableCellValue.getNumber()) - 1;// minus one, since we start now counting with 1
      handler.handleBusinessDayChanged(ChangedValue.of(orderNumber, newValue, valueType));
      uiRefresher.onFinish(FinishAction.FINISH);
   }

   private static boolean isNotValidValueType(ValueTypes valueType) {
      // XXX Ugly hack. Since we had to make present values editable
      return valueType == ValueTypes.NONE;
   }
}
