package com.myownb3.dominic.ui.views.overview.table;

import com.myownb3.dominic.timerecording.work.businessday.ValueTypes;

public class TableCellValue {

    private String value;
    private ValueTypes valueType;

    private boolean isEditable;

    protected TableCellValue(String newValue, boolean isEditable, ValueTypes valueType) {
	this.value = newValue;
	this.isEditable = isEditable;
	this.valueType = valueType;
    }

    @Override
    public String toString() {
	return value;
    }

    public boolean isEditable() {
	return isEditable;
    }

    /**
     * Returns the value of this cell
     * @return the value of this cell
     */
    public String getValue() {
        return value;
    }
    
    public ValueTypes getValueType() {
        return valueType;
    }
    
    /**
     * Creates a new non editable {@link TableCellValue} with the given String
     * value
     * 
     * @param value
     *            the value of this cell
     * @return a new non editable {@link TableCellValue} with the given String
     *         value
     */
    public static TableCellValue of(String value) {
	return new TableCellValue(value, false, ValueTypes.NONE);
    }

    /**
     * Creates a new non editable {@link TableCellValue} with the given String
     * value
     * 
     * @param value
     *            the value of this cell
     * @param isEditable
     *            <code>true</code> if this cell is editable or
     *            <code>false</code> if not
     * @param valueTypes
     *            the type of value
     * @return a new non editable {@link TableCellValue} with the given String
     *         value
     */
    public static TableCellValue of(String value, boolean isEditable, ValueTypes valueTypes) {
	return new TableCellValue(value, isEditable, valueTypes);
    }
    
    /**
     * Creates a new {@link TableCellValue} for the given {@link TableCellValue}
     * and the new value
     * 
     * @param value
     *            the value of this cell
     * @param tableCellValue
     *            the existing {@link TableCellValue} whose values are used for
     *            the new one
     * 
     * @return a new non editable {@link TableCellValue} with the given String
     *         value
     */
    public static TableCellValue of(String newValue, TableCellValue tableCellValue) {
	if (tableCellValue instanceof TimeSnippetCellValue) {
	    return new TimeSnippetCellValue(newValue, ((TimeSnippetCellValue) tableCellValue).getSequence(),
		    tableCellValue.isEditable(), tableCellValue.getValueType());
	}
	return new TableCellValue(newValue, tableCellValue.isEditable(), tableCellValue.getValueType());
    }
    
    /**
     * Creates a new non editable {@link TableCellValue} with the given int
     * value
     * 
     * @param value
     *            the value of this cell
     * @return a new non editable {@link TableCellValue} with the given int
     *         value
     */
    public static TableCellValue of(int value) {
	return new TableCellValue(String.valueOf(value), false, ValueTypes.NONE);
    }
}
