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

    /**
     * Sets the new value
     * @param value the new value
     */
    public void setValue(String value) {
	this.value = value;
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
