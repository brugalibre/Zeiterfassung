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
public class TimeSnippetCellValue  {

    private IntegerProperty sequenceValueProperty;
    private StringProperty vonValueProperty;
    private StringProperty bisValueProperty;

    public TimeSnippetCellValue(int sequence, String von, String bis) {

	this.vonValueProperty = new SimpleStringProperty(von); 
	this.bisValueProperty = new SimpleStringProperty(bis); 
	this.sequenceValueProperty = new SimpleIntegerProperty(sequence); 
    }
    
    public int getSequence() {
        return sequenceValueProperty.get();
    }
    public String getVon() {
        return vonValueProperty.get();
    }
    public String getBis() {
        return bisValueProperty.get();
    }
}
