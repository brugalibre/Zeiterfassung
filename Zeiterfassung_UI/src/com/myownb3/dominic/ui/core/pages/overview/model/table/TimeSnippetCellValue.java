/**
 * 
 */
package com.myownb3.dominic.ui.core.pages.overview.model.table;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * 
 * @author Dominic
 *
 */
public class TimeSnippetCellValue {

    private IntegerProperty sequenceValueProperty;
    private StringProperty beginValueProperty;
    private StringProperty endValueProperty;

    private TimeSnippetCellValue(int sequence, String begin, String end) {

	this.beginValueProperty = new SimpleStringProperty(begin);
	this.endValueProperty = new SimpleStringProperty(end);
	this.sequenceValueProperty = new SimpleIntegerProperty(sequence);
    }

    public int getSequence() {
	return sequenceValueProperty.get();
    }

    public String getBegin() {
	return beginValueProperty.get();
    }

    public String getEnd() {
	return endValueProperty.get();
    }

    /**
     * @param value
     * @param sequence
     * @param begin
     * @return
     */
    public static TimeSnippetCellValue of(String begin, String end, int sequence) {
	return new TimeSnippetCellValue(sequence, begin, end);
    }
}
