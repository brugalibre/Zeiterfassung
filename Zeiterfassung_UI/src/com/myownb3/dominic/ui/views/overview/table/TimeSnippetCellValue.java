/**
 * 
 */
package com.myownb3.dominic.ui.views.overview.table;

import com.myownb3.dominic.timerecording.work.businessday.ValueTypes;

/**
 * 
 * @author Dominic
 *
 */
public class TimeSnippetCellValue extends TableCellValue {

    private int sequence;

    public TimeSnippetCellValue(String value, int sequence, boolean isEditable, ValueTypes valueType) {
	super(value, isEditable, valueType);
	this.sequence = sequence;
    }

    public static TimeSnippetCellValue of(String value, int sequence, ValueTypes valueTypes) {
	return new TimeSnippetCellValue(value, sequence, true, valueTypes);
    }
    
    public static TimeSnippetCellValue of(String value, int sequence, boolean isEditable, ValueTypes valueTypes) {
	return new TimeSnippetCellValue(value, sequence, isEditable, valueTypes);
    }

    public int getSequence() {
	return sequence;
    }

}
