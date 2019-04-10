/**
 * 
 */
package com.myownb3.dominic.ui.views.overview.table;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

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
	table.setFillsViewportHeight(true);
    }

    public void initialize(BusinessDay4Export bussinessDay) {
	BusinessDayTableModel businessDayTableModel = new BusinessDayTableModel();
	businessDayTableModel.init(bussinessDay);
	table.setModel(businessDayTableModel);
	resizeColumnWidth();
    }

    private void resizeColumnWidth() {
	for (int column = 0; column < table.getColumnCount(); column++) {
	    TableColumn tableColumn = table.getColumnModel().getColumn(column);
	    int preferredWidth = tableColumn.getMinWidth();
	    int maxWidth = tableColumn.getMaxWidth();

	    for (int row = 0; row < table.getRowCount(); row++) {
		TableCellRenderer cellRenderer = table.getCellRenderer(row, column);
		Component c = table.prepareRenderer(cellRenderer, row, column);
		int width = c.getPreferredSize().width + table.getIntercellSpacing().width;
		preferredWidth = Math.max(preferredWidth, width);

		// We've exceeded the maximum width, no need to check other rows
		if (preferredWidth >= maxWidth) {
		    preferredWidth = maxWidth;
		    break;
		}
	    }
	    tableColumn.setPreferredWidth(preferredWidth);
	    tableColumn.setWidth(preferredWidth);
	}
    }

    public JTable getTable() {
	return table;
    }
}
