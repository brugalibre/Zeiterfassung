/**
 * 
 */
package com.myownb3.dominic.ui.views.overview.table;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import com.myownb3.dominic.librarys.text.res.TextLabel;
import com.myownb3.dominic.timerecording.callback.handler.BusinessDayChangedCallbackHandler;
import com.myownb3.dominic.timerecording.callback.handler.impl.ChangedValue;
import com.myownb3.dominic.timerecording.work.businessday.ext.BusinessDay4Export;

/**
 * @author Dominic
 * 
 */
public class TablePanel implements TableModelListener {

    private JTable table;
    private BusinessDayChangedCallbackHandler handler;
    private JPanel panel;
    private JLabel totalAmountOfHoursLabel;
    private JLabel totalAmountOfHoursValue;
    
    public TablePanel(Dimension dimension) {
	table = new JTable();
	table.setVisible(true);
	table.setFillsViewportHeight(true);
	
	
	JScrollPane scrollPane = new JScrollPane(table);
	scrollPane.setBorder(BorderFactory.createEmptyBorder());
	scrollPane.setPreferredSize(dimension);
	
	JPanel totalAmountOfHoursPanel = new JPanel (new FlowLayout());
	totalAmountOfHoursLabel = new JLabel(TextLabel.TOTAL_AMOUNT_OF_HOURS_LABEL);
	totalAmountOfHoursValue = new JLabel();
	totalAmountOfHoursPanel.add(totalAmountOfHoursLabel, FlowLayout.LEFT);
	totalAmountOfHoursPanel.add(totalAmountOfHoursValue, FlowLayout.CENTER);
	
	panel = new JPanel (new BorderLayout());
	panel.add(scrollPane, BorderLayout.CENTER);
	panel.add(totalAmountOfHoursPanel, BorderLayout.PAGE_END);
    }

    public void initialize(BusinessDay4Export bussinessDay, BusinessDayChangedCallbackHandler handler) {
	this.handler = handler;
	BusinessDayTableModel businessDayTableModel = new BusinessDayTableModel(this);
	businessDayTableModel.init(bussinessDay);
	table.setModel(businessDayTableModel);
	totalAmountOfHoursValue.setText(bussinessDay.getTotalDurationRep());
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

    @Override
    public void tableChanged(TableModelEvent e) {

	if (e.getType() == TableModelEvent.UPDATE) {
	    BusinessDayTableModel businessDayTableModel = (BusinessDayTableModel) e.getSource();
	    TableCellValue tableCellValue = businessDayTableModel.getCellAt(e.getFirstRow(), e.getColumn());
	    TableCellValue noTableCellValue = businessDayTableModel.getCellAt(e.getFirstRow(), 0);
	    handler.handleBusinessDayChanged(ChangedValue.of(Integer.valueOf(noTableCellValue.getValue()), tableCellValue.getValue(), tableCellValue.getValueType(),
		    getIndexForFromUpto(tableCellValue)));
	}
    }

    private int getIndexForFromUpto(TableCellValue tableCellValue) {
	if (tableCellValue instanceof TimeSnippetCellValue) {
	    return ((TimeSnippetCellValue) tableCellValue).getSequence();
	}
	return -1;
    }

    public Component getPanel() {
	return panel;
    }
}
