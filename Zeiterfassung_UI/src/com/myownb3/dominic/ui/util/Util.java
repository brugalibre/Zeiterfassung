/**
 * 
 */
package com.myownb3.dominic.ui.util;

import java.awt.FontMetrics;
import java.io.PrintWriter;
import java.io.StringWriter;

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

    public static int getTextLength(String text, FontMetrics fm) {
	return fm.stringWidth(text);
    }

    public static int getTextHight(FontMetrics fm) {
	return fm.getHeight();
    }
}
