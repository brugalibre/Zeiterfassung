/**
 * 
 */
package com.myownb3.dominic.timerecording.app;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import com.myownb3.dominic.export.ContentSelector;
import com.myownb3.dominic.export.FileExporter;
import com.myownb3.dominic.librarys.text.res.TextLabel;
import com.myownb3.dominic.timerecording.callback.handler.CallbackHandler;
import com.myownb3.dominic.timerecording.charge.Charger;
import com.myownb3.dominic.timerecording.work.WorkStates;
import com.myownb3.dominic.timerecording.work.businessday.BusinessDay;
import com.myownb3.dominic.timerecording.work.businessday.BusinessDayIncremental;
import com.myownb3.dominic.timerecording.work.businessday.TimeSnippet;
import com.myownb3.dominic.timerecording.work.businessday.ext.BusinessDay4Export;
import com.myownb3.dominic.timerecording.work.date.TimeType.TIME_TYPE;

/**
 * Responsible for recording the time. The {@link TimeRecorder} consist of one
 * object, that represent a business day. When the user clicks on the tray-icon,
 * a current {@link BusinessDayIncremental} is either started or terminated -
 * depending if the user was working before or not
 * 
 * @author Dominic
 */
public class TimeRecorder {
    public static final String VERSION = "1.3.0";
    public static final TIME_TYPE GLOBAL_TIME_TYPE; // application wide use
						    // TIME_TYPE that defines,
						    // in what unit the time is
						    // calculated
    public static final NumberFormat formater;
    private static WorkStates currentState; // either it is working, or not
					    // working
    private static BusinessDay businessDay;
    private static CallbackHandler callbackHandler;

    static {
	formater = new DecimalFormat("0.00");
	GLOBAL_TIME_TYPE = TIME_TYPE.HOUR;
	currentState = WorkStates.NOT_WORKING;
	businessDay = new BusinessDay();
    }

    public static boolean handleUserInteraction() {
	switch (currentState) {
	case NOT_WORKING:
	    start();
	    return false;

	case WORKING:
	    stop();
	    return true;
	default:
	    throw new RuntimeException("Unknowing working state '" + currentState + "'!");
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

    /**
     * @return
     */
    public static String getTotalAmountOfWork(TIME_TYPE type) {
	return String.valueOf(businessDay.getTotalDuration(type));
    }

    public static BusinessDay getBussinessDay() {
	return businessDay;
    }

    /**
    * 
    */
    public static void checkForRedundancy() {
	businessDay.checkForRedundancys();
    }

    /**
     * removes all recorded {@link BusinessDayIncremental}
     */
    public static void clear() {
	businessDay.clearFinishedIncrements();
    }

    /**
    * 
    */
    public static void export() {
	List<String> content = ContentSelector.collectContent(new BusinessDay4Export(businessDay));
	FileExporter.export(content);
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
	BusinessDayIncremental currentIncrement = null;
	switch (currentState) {
	case NOT_WORKING:
	    currentIncrement = businessDay.getCurrentBussinessDayIncremental();
	    TimeSnippet endPoint = currentIncrement.getCurrentTimeSnippet();
	    if (endPoint != null) {
		return TextLabel.APPLICATION_TITLE + ": " + TextLabel.CAPTURING_INCTIVE_SINCE + " "
			+ endPoint.getEndTimeStamp();
	    }
	    return TextLabel.APPLICATION_TITLE + ": " + TextLabel.CAPTURING_INACTIVE;

	case WORKING:
	    currentIncrement = businessDay.getCurrentBussinessDayIncremental();
	    TimeSnippet startPoint = currentIncrement.getCurrentTimeSnippet();
	    String time = startPoint.getDuration() > 0 ? " (" + startPoint.getDuration() + "h)" : "";
	    return TextLabel.CAPTURING_ACTIVE_SINCE + " " + startPoint.getBeginTimeStamp() + time;
	default:
	    throw new RuntimeException("Unknowing working state '" + currentState + "'!");
	}
    }

    /**
     * Return true if the {@link TimeRecorder} is currently working, otherwise false
     * 
     * @return true if the {@link TimeRecorder} is currently working, otherwise
     *         false
     * @see WorkStates
     */
    public static boolean isRecording() {
	return currentState == WorkStates.WORKING;
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
     * Returns <code>true</code> if there is at least one
     * {@link BusinessDayIncremental} which is finished. Otherwise returns
     * <code>false</code>
     * 
     * @return <code>true</code> if there is at least one
     *         {@link BusinessDayIncremental} which is finished. Otherwise returns
     *         <code>false</code>
     */
    public static boolean hasFinishedContent() {
	return businessDay.hasFinishedContent();
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
     * @return the path of the new created file
     */
    public static void charge() {
	if (businessDay.hasNotChargedElements()) {
	    Charger charger = new Charger(businessDay);
	    charger.charge();
	}
    }
}
