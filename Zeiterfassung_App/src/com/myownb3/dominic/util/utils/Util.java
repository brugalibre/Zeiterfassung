/**
 * 
 */
package com.myownb3.dominic.util.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import com.myownb3.dominic.librarys.text.res.TextLabel;

/**
 * @author Dominic
 * 
 */
public class Util {
    /**
     * Creates and returns a {@link String} from a given Stack traces
     * 
     * @param thread,
     *            the {@link Thread} which caused the {@link Throwable}
     * @param thrown,
     *            the thrown {@link Throwable}
     * @return a string which contain the stack trace
     * @return a {@link String} that includes the print-stack-trace
     */
    public static String getStackTracesAsString(String thread, Throwable thrown) {
	StringBuilder stackTrace = new StringBuilder("Exception in Thread \"" + thread + "\" ");
	StringWriter ws = new StringWriter();
	thrown.printStackTrace(new PrintWriter(ws));

	stackTrace.append(ws.toString());
	return stackTrace.toString();
    }

    /**
     * @param value
     * @return {@value TextLabel#NEIN} if value is true and {@value TextLabel#NEIN}
     *         if not
     */
    public static String booleanValueMapper(boolean value) {
	return value == true ? TextLabel.JA : TextLabel.NEIN;
    }

    /**
     * @param <T>
     * @param listToInvert
     * @return a new list, which is an inverted version of the given one
     */
    public static <T> List<T> getInvertedList(List<T> listToInvert) {
	List<T> invertedList = new ArrayList<T>();

	for (int i = (listToInvert.size() - 1); i >= 0; i--) {
	    invertedList.add(listToInvert.get(i));
	}
	return invertedList;
    }
}
