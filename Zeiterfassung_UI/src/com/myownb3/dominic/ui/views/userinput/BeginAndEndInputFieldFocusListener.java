/**
 * 
 */
package com.myownb3.dominic.ui.views.userinput;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Date;

import com.myownb3.dominic.timerecording.work.businessday.TimeSnippet;
import com.myownb3.dominic.util.parser.DateParser;

/**
 * @author Dominic
 *
 */
public class BeginAndEndInputFieldFocusListener implements FocusListener {

    private InputMask inputMask;

    public BeginAndEndInputFieldFocusListener(InputMask inputMask) {
	this.inputMask = inputMask;
    }

    @Override
    public void focusLost(FocusEvent e) {

	TimeSnippet currentTimeSnippet = inputMask.getCurrentTimeSnippet();
	if (e.getSource() == inputMask.getVonField()) {

	    Date date = DateParser.getDate(inputMask.getVonField().getText(), currentTimeSnippet.getDate());
	    inputMask.handleBeginChanged(date);

	} else {
	    Date date = DateParser.getDate(inputMask.getBisField().getText(), currentTimeSnippet.getDate());
	    inputMask.handleEndChanged(date);
	}
    }

    @Override
    public void focusGained(FocusEvent e) {
	// Nothing to do
    }
}
