/**
 * 
 */
package com.myownb3.dominic.ui.views.overview;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.myownb3.dominic.librarys.text.res.TextLabel;
import com.myownb3.dominic.timerecording.app.TimeRecorder;
import com.myownb3.dominic.timerecording.callback.handler.BusinessDayChangedCallbackHandler;
import com.myownb3.dominic.timerecording.work.businessday.ext.BusinessDay4Export;
import com.myownb3.dominic.ui.app.MainWindow;
import com.myownb3.dominic.ui.views.overview.table.TablePanel;

/**
 * @author Dominic
 * 
 */
public class OverviewView extends JPanel{
    private static final long serialVersionUID = 1L;

    /**
     * There are five fix headers: Number, Amount of Hours, Ticket, charge-Type &
     * is-charged
     */
    public static final int AMOUNT_OF_FIX_HEADERS = 5;

    private static int HEIGHT;
    private static int WIDTH;

    private MainWindow mainView;
    private TablePanel tablePanel;

    private JButton clearButton;
    private JButton bookOffButton;
    private JButton exportButton;

    static {
	HEIGHT = 150;
	WIDTH = 600;
    }

    public OverviewView(MainWindow mainView) {
	super(new BorderLayout());
	this.mainView = mainView;
	this.tablePanel = new TablePanel(new Dimension((int) (WIDTH * 1.7), HEIGHT));

	add(tablePanel.getPanel(), BorderLayout.CENTER);
	JPanel controlPanel = createControlPanel();
	add(controlPanel, BorderLayout.PAGE_END);
    }

    /**
     * @return
     */
    private JPanel createControlPanel() {
	JPanel panel = new JPanel(new FlowLayout());

	ActionListener listener = createActionListener();
	createChargeButton(listener);
	createClearButton(listener);
	createExportButton(listener);

	panel.add(clearButton, FlowLayout.LEFT);
	panel.add(exportButton, FlowLayout.CENTER);
	panel.add(bookOffButton, FlowLayout.RIGHT);
	return panel;
    }

    /**
     * @param listener
     * 
     */
    private void createChargeButton(ActionListener listener) {
	bookOffButton = new JButton(TextLabel.CHARGE_LABEL);
	bookOffButton.addActionListener(listener);
    }

    /**
     * @param listener
     * 
     */
    private void createExportButton(ActionListener listener) {
	exportButton = new JButton(TextLabel.EXPORT_LABEL);
	exportButton.addActionListener(listener);
    }

    /**
     * @param listener
     * 
     */
    private void createClearButton(ActionListener listener) {
	clearButton = new JButton(TextLabel.CLEAR_LABEL);
	clearButton.addActionListener(listener);
    }

    private ActionListener createActionListener() {
	return e -> {
	    if (e.getSource() == clearButton) {
		clearBusinessDayContents();
		mainView.dispose();
	    } else if (e.getSource() == bookOffButton) {
		bookTicketData();
	    } else if (e.getSource() == exportButton) {
		export();
	    }
	};
    }

    /**
    * 
    */
    protected void export() {
	mainView.export();
    }

    /**
    * 
    */
    protected void clearBusinessDayContents() {
	mainView.clearBusinessDayContents();
    }

    /**
    * 
    */
    protected void bookTicketData() {
	mainView.book();
    }

    /**
     * @return
     */
    public Dimension getDimension() {
	return new Dimension(WIDTH, HEIGHT);
    }

    public void initialize(BusinessDay4Export bussinessDay, BusinessDayChangedCallbackHandler handler) {
	tablePanel.initialize(bussinessDay, handler);
	bookOffButton.setEnabled(TimeRecorder.hasNotChargedElements());
    }
}