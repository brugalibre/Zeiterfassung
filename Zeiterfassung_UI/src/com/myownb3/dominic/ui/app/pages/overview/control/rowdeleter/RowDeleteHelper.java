/**
 * 
 */
package com.myownb3.dominic.ui.app.pages.overview.control.rowdeleter;

import java.util.Optional;

import com.myownb3.dominic.timerecording.core.callbackhandler.BusinessDayChangedCallbackHandler;
import com.myownb3.dominic.ui.app.TimeRecordingTray;
import com.myownb3.dominic.ui.app.pages.overview.control.OverviewController;
import com.myownb3.dominic.ui.app.pages.overview.model.table.BusinessDayIncTableRowValue;
import com.myownb3.dominic.ui.app.pages.overview.view.OverviewPage;

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

    private BusinessDayChangedCallbackHandler handler;
    private OverviewController overviewController;
    private TimeRecordingTray timeRecordingTray;

    public RowDeleteHelper(OverviewController overviewController, BusinessDayChangedCallbackHandler handler, TimeRecordingTray timeRecordingTray) {
	this.overviewController = overviewController;
	this.handler = handler;
	this.timeRecordingTray = timeRecordingTray;
    }

    /**
     * Deletes the row which is currently selected on the given {@link TableView}
     * 
     * @param event
     *            the action event which triggered us
     * @param tableView
     *            the table-view
     */
    public void deleteRow(ActionEvent event, TableView<BusinessDayIncTableRowValue> tableView) {

	Optional<BusinessDayIncTableRowValue> optionalBusinessDayIncTableRowValue = Optional.ofNullable(tableView.getSelectionModel().getSelectedItem());
	optionalBusinessDayIncTableRowValue.ifPresent(businessDayIncTableRowValue -> {
	    handler.handleBusinessDayIncrementDeleted(businessDayIncTableRowValue.getNumberAsInt());
	    afterDelete(event);
	});
    }

    private void afterDelete(ActionEvent event) {
	consumeEventAndRefresh(event);
	timeRecordingTray.updateUIStates();
    }

    private void consumeEventAndRefresh(ActionEvent event) {
	event.consume();
	overviewController.show();
    }
}