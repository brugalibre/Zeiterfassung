/**
 * 
 */
package com.myownb3.dominic.ui.app.pages.overview.model.table;

import com.myownb3.dominic.timerecording.work.businessday.ValueTypes;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * 
 * @author Dominic
 *
 */
public class TimeSnippetCellValue {

    private IntegerProperty sequenceValueProperty;
    private StringProperty beginOrEndValueProperty;
    private ObjectProperty<ValueTypes> valueTypeProperty;

    private TimeSnippetCellValue(int sequence, String beginOrEnd, ValueTypes valueType) {

	this.beginOrEndValueProperty = new SimpleStringProperty(beginOrEnd);
	this.sequenceValueProperty = new SimpleIntegerProperty(sequence);
	this.valueTypeProperty = new SimpleObjectProperty<ValueTypes>(valueType);
    }

    public int getSequence() {
	return sequenceValueProperty.get();
    }

    public String getBeginOrEnd() {
	return beginOrEndValueProperty.get();
    }

    /**
     * Creates a new {@link TimeSnippetCellValue}
     * 
     * @param beginOrEnd
     *            begin or end value
     * @param sequence
     *            the sequence
     * @param valueType
     *            either {@link ValueTypes#BEGIN} or {@link ValueTypes#END}
     * @return a new {@link TimeSnippetCellValue}
     */
    public static TimeSnippetCellValue of(String beginOrEnd, int sequence, ValueTypes valueType) {
	return new TimeSnippetCellValue(sequence, beginOrEnd, valueType);
    }

    public ValueTypes getValueType() {
	return valueTypeProperty.get();
    }
}
