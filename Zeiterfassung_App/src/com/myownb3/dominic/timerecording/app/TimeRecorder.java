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
    public static final String VERSION = "1.4.4";
    public static final TIME_TYPE GLOBAL_TIME_TYPE; // application wide use
						    // TIME_TYPE that defines,
						    // in what unit the time is
						    // calculated
    private static WorkStates currentState; // either it is working, or not
					    // working
    public static BusinessDay businessDay;
    private static CallbackHandler callbackHandler;

    static {
	GLOBAL_TIME_TYPE = TIME_TYPE.HOUR;
	currentState = WorkStates.NOT_WORKING;
	businessDay = new BusinessDay();
    }

    public static boolean handleUserInteraction() {
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

    private static void tryStartIfPossible() {
	if (businessDay.hasElementsFromPrecedentDays()) {
	    callbackHandler
		    .showMessage(Message.of(MessageType.ERROR, null, TextLabel.START_NOT_POSSIBLE_PRECEDENT_ELEMENTS));
	} else {
	    start();
	}
    }

    public static void stop() {
	businessDay.stopCurrentIncremental();
	currentState = WorkStates.NOT_WORKING;
	callbackHandler.onStop();
    }

    private static void start() {
	if (currentState == WorkStates.WORKING) {
	    return;
	}
	currentState = WorkStates.WORKING;
	businessDay.startNewIncremental();
	callbackHandler.onStart();
    }

    public static void resume() {

	currentState = WorkStates.WORKING;
	businessDay.resumeLastIncremental();
	callbackHandler.onResume();
    }

    public static void setCallbackHandler(CallbackHandler callbackHandler) {
	TimeRecorder.callbackHandler = callbackHandler;
    }

    public static BusinessDay getBussinessDay() {
	return businessDay;
    }

    /**
     * removes all recorded {@link BusinessDayIncrement}
     */
    public static void clear() {
	businessDay.clearFinishedIncrements();
    }

    /**
    * 
    */
    public static void export() {
	List<String> content = ContentSelector.INSTANCE.collectContent(BusinessDay4Export.of(businessDay));
	FileExporter.INTANCE.export(content);
    }

    /**
     * Return a String, which represents the current state and shows informations
     * according to this
     * 
     * @return a String, which represents the current state and shows informations
     *         according to this
     * @see WorkStates
     */
    public static String getInfoStringForState() {
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
     * Return <code>true</code> if there is any content, <code>false</code> if not
     * 
     * @return <code>true</code> if there is any content, <code>false</code> if not
     */
    public static boolean hasContent() {
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
    public static boolean hasNotChargedElements() {
	return businessDay.hasNotChargedElements();
    }

    /**
     * Collects and export the necessary data which is used by the TurobBucher to
     * charge After the tuber-bucher- app is invoked in order to do actual charge
     * 
     * @return <code>true</code> if there was actually a booking process or <code>false</code> if there wasn't anything to do
     */
    public static boolean book() {
	if (businessDay.hasNotChargedElements()) {
	    BookerHelper helper = new BookerHelper(businessDay);
	    helper.book();
	    return true;
	}
	return false;
    }
}
