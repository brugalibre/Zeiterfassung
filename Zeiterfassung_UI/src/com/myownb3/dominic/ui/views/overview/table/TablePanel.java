/**
 * 
 */
package com.myownb3.dominic.ui.views.overview.table;

import java.awt.Dimension;

import javax.swing.JTable;

import com.myownb3.dominic.timerecording.work.businessday.ext.BusinessDay4Export;

/**
 * @author Dominic
 * 
 */
public class TablePanel {

    private JTable table;

    public TablePanel() {
	table = new JTable();
	table.setVisible(true);
	table.setPreferredScrollableViewportSize(new Dimension(300, 80));
	table.setFillsViewportHeight(true);
	table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    }

    public void initialize(BusinessDay4Export bussinessDay) {
	BusinessDayTableModel businessDayTableModel = new BusinessDayTableModel();
	businessDayTableModel.init(bussinessDay);
	table.setModel(businessDayTableModel);
    }

    public JTable getTable() {
	return table;
    }

    public void setTable(JTable table) {
	this.table = table;
    }
}
