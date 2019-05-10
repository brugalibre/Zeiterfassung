/**
 * 
 */
package com.myownb3.dominic.ui.core.pages.overview.control;

import java.net.URL;
import java.util.ResourceBundle;

import com.myownb3.dominic.ui.core.control.impl.BaseFXController;
import com.myownb3.dominic.ui.core.model.resolver.PageModelResolver;
import com.myownb3.dominic.ui.core.pages.mainpage.control.MainWindowController;
import com.myownb3.dominic.ui.core.pages.overview.model.OverviewPageModel;
import com.myownb3.dominic.ui.core.pages.overview.model.resolver.OverviewPageModelResolver;
import com.myownb3.dominic.ui.core.pages.overview.model.table.BusinessDayIncTableCellValue;
import com.myownb3.dominic.ui.core.pages.overview.model.table.BusinessDayTableModel;
import com.myownb3.dominic.ui.core.pages.overview.view.OverviewPage;
import com.myownb3.dominic.ui.core.view.Page;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;

/**
 * @author Dominic
 * 
 */
public class OverviewController extends BaseFXController<OverviewPageModel, OverviewPageModel> {

    /**
     * There are five fix headers: Number, Amount of Hours, Ticket, charge-Type &
     * is-charged
     */
    public static final int AMOUNT_OF_FIX_HEADERS = 5;

    private MainWindowController mainWindowController;

    @FXML
    private BorderPane borderPane;

    @FXML
    private TableView<BusinessDayIncTableCellValue> tableView;

    @FXML
    private Button clearButton;
    @FXML
    private Button chargeOffButton;
    @FXML
    private Button exportButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
	initialize(new OverviewPage(this, url));
    }

    @Override
    public void initialize(Page<OverviewPageModel, OverviewPageModel> page) {
	super.initialize(page);
	setBinding(dataModel);
	new BusinessDayTableModel( tableView);
    }

    @FXML
    private void onAction(ActionEvent actionEvent) {

	if (actionEvent.getSource() == clearButton) {
	    mainWindowController.clearBusinessDayContents();
	    mainWindowController.dispose();
	} else if (actionEvent.getSource() == chargeOffButton) {
	    mainWindowController.chargeOff();
	} else if (actionEvent.getSource() == exportButton) {
	    mainWindowController.export();
	}
    }

    @Override
    protected PageModelResolver<OverviewPageModel, OverviewPageModel> createPageModelResolver() {
	return new OverviewPageModelResolver();
    }

    @Override
    protected void setBinding(OverviewPageModel pageVO) {
	// tablePanel.initialize(bussinessDay, handler);
	chargeOffButton.disableProperty().set(getDataModel().isChargeButtonDisabled().getValue());
    }

    public void setMainWindowController(MainWindowController mainWindowController) {
	this.mainWindowController = mainWindowController;
    }
}