/**
 * 
 */
package com.myownb3.dominic.timerecording.app;

import java.util.List;

import com.myownb3.dominic.export.ContentSelector;
import com.myownb3.dominic.export.FileExporter;
import com.myownb3.dominic.librarys.text.res.TextLabel;
import com.myownb3.dominic.timerecording.callback.handler.CallbackHandler;
import com.myownb3.dominic.timerecording.charge.BookerHelper;
import com.myownb3.dominic.timerecording.work.WorkStates;
import com.myownb3.dominic.timerecording.work.businessday.BusinessDay;
import com.myownb3.dominic.timerecording.work.businessday.BusinessDayIncrement;
import com.myownb3.dominic.timerecording.work.businessday.ext.BusinessDay4Export;
import com.myownb3.dominic.timerecording.work.date.TimeType.TIME_TYPE;

/**
 * Responsible for recording the time. The {@link TimeRecorder} consist of one
 * object, that represent a business day. When the user clicks on the tray-icon,
 * a current {@link BusinessDayIncrement} is either started or terminated -
 * depending if the user was working before or not
 * 
 * @author Dominic
 */
public class TimeRecorder {

    /**
     * The singleton instance of this class
     */
    public static final TimeRecorder INSTANCE = new TimeRecorder();

    /**
     * The version of the application
     */
    public static final String VERSION = "1.5.0";

    private BusinessDay businessDay;
    private CallbackHandler callbackHandler;
    private WorkStates currentState;
    private TIME_TYPE timeType;

    private TimeRecorder() {
	timeType = TIME_TYPE.HOUR;
	currentState = WorkStates.NOT_WORKING;
	businessDay = new BusinessDay();
    }

    public boolean handleUserInteraction() {
	switch (currentState) {
	case NOT_WORKING:
	    tryStartIfPossible();
	    return false;

	case WORKING:
	    stop();
	    return true;
	default:
	    throw new RuntimeException("Unknowing working state '" + currentState + "'!");
	}
    }

    private void tryStartIfPossible() {
	if (businessDay.hasElementsFromPrecedentDays()) {
	    callbackHandler
		    .showMessage(Message.of(MessageType.ERROR, null, TextLabel.START_NOT_POSSIBLE_PRECEDENT_ELEMENTS));
	} else {
	    start();
	}
    }

    public void stop() {
	businessDay.stopCurrentIncremental();
	currentState = WorkStates.NOT_WORKING;
	callbackHandler.onStop();
    }

    private void start() {
	if (currentState == WorkStates.WORKING) {
	    return;
	}
	currentState = WorkStates.WORKING;
	businessDay.startNewIncremental();
	callbackHandler.onStart();
    }

    public void resume() {

	currentState = WorkStates.WORKING;
	businessDay.resumeLastIncremental();
	callbackHandler.onResume();
    }

    public void setCallbackHandler(CallbackHandler callbackHandler) {
	this.callbackHandler = callbackHandler;
    }

    public BusinessDay getBussinessDay() {
	return businessDay;
    }

    /**
     * removes all recorded {@link BusinessDayIncrement}
     */
    public void clear() {
	businessDay.clearFinishedIncrements();
    }

    /**
    * 
    */
    public void export() {
	List<String> content = ContentSelector.INSTANCE.collectContent(BusinessDay4Export.of(businessDay));
	FileExporter.INTANCE.export(content);
    }

    /**
     * Return a String, which represents the current state and shows
     * informations according to this
     * 
     * @return a String, which represents the current state and shows
     *         informations according to this
     * @see WorkStates
     */
    public String getInfoStringForState() {
	switch (currentState) {
	case NOT_WORKING:
	    return businessDay.getCapturingInactiveSinceMsg();
	case WORKING:
	    return businessDay.getCapturingActiveSinceMsg();
	default:
	    throw new RuntimeException("Unknowing working state '" + currentState + "'!");
	}
    }

    /**
     * Return <code>true</code> if there is any content, <code>false</code> if
     * not
     * 
     * @return <code>true</code> if there is any content, <code>false</code> if
     *         not
     */
    public boolean hasContent() {
	return businessDay.getTotalDuration() > 0f;
    }

    /**
     * Returns <code>true</code> if this {@link BusinessDay} has at least one
     * element which is not yed charged. Otherwise returns <code>false</code>
     * 
     * @return <code>true</code> if this {@link BusinessDay} has at least one
     *         element which is not yed charged. Otherwise returns
     *         <code>false</code>
     */
    public boolean hasNotChargedElements() {
	return businessDay.hasNotChargedElements();
    }

    /**
     * Collects and export the necessary data which is used by the TurobBucher
     * to charge After the tuber-bucher- app is invoked in order to do actual
     * charge
     * 
     * @return <code>true</code> if there was actually a booking process or
     *         <code>false</code> if there wasn't anything to do
     */
    public boolean book() {
	if (businessDay.hasNotChargedElements()) {
	    BookerHelper helper = new BookerHelper(businessDay);
	    helper.book();
	    return true;
	}
	return false;
    }

    /**
     * @return the current {@link TIME_TYPE} of this {@link TimeRecorder}
     */
    public TIME_TYPE getTimeType() {
	return timeType;
    }
}
