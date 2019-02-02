/**
 * 
 */
package com.myownb3.dominic.ui.views.overview;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.myownb3.dominic.librarys.text.res.TextLabel;
import com.myownb3.dominic.timerecording.work.businessday.BusinessDay;
import com.myownb3.dominic.ui.app.MainWindow;
import com.myownb3.dominic.ui.draw.raster.impl.RasterPanel;

/**
 * @author Dominic
 * 
 */
public class OverviewView extends JPanel {
    private static final long serialVersionUID = 1L;

    public static final int AMOUNT_OF_FIX_HEADERS = 4;// 4, number, description,
						      // max amount of hours &
						      // charge-type;

    private static int HEIGHT;
    private static int WIDTH;

    private MainWindow mainView;
    private RasterPanel rasterPanel;

    private JButton clearButton;
    private JButton chargeButton;
    private JButton exportButton;

    static {
	HEIGHT = 140;
	WIDTH = 600;
    }

    public OverviewView(MainWindow mainView) {
	super(new BorderLayout());
	this.mainView = mainView;
	this.rasterPanel = new RasterPanel();
	rasterPanel.setSize(rasterPanel.getSize());

	JScrollPane scrollPane = new JScrollPane(rasterPanel);// ,
							      // JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
							      // JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	scrollPane.setBorder(BorderFactory.createEmptyBorder());
	scrollPane.setPreferredSize(new Dimension((int) (WIDTH * 1.65), HEIGHT));

	add(scrollPane, BorderLayout.CENTER);
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
	panel.add(exportButton);
	return panel;
    }

    /**
     * @param listener
     * 
     */
    private void createChargeButton(ActionListener listener) {
	chargeButton = new JButton(TextLabel.CHARGE_LABEL);
	chargeButton.setEnabled(false);
	chargeButton.addActionListener(listener);
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

    /**
    * 
    */
    private ActionListener createActionListener() {
	return new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		if (e.getSource() == clearButton) {
		    clearBusinessDayContents();
		    mainView.dispose();
		} else if (e.getSource() == chargeButton) {
		    collectTicketData();
		    sendHTTPRequestForCharging();
		} else if (e.getSource() == exportButton) {
		    export();
		}
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
    protected void collectTicketData() {

    }

    /**
    * 
    */
    protected void sendHTTPRequestForCharging() {
	// TODO create Class for HTTP Access to JIRA and transmit the request /
	// send the charging
    }

    /**
     * @return
     */
    public Dimension getDimension() {
	return new Dimension(WIDTH, HEIGHT);
    }

    public void setBussinessDay(BusinessDay bussinessDay) {
	rasterPanel.setBussinessDay(bussinessDay);
	rasterPanel.initialize();
    }
}