/**
 * 
 */
package com.myownb3.dominic.ui.app.pages.overview.control;

/**
 * The {@link UIRefresher} is like a callback handler in order to refresh the UI
 * @author Dominic
 *
 */
@FunctionalInterface
public interface UIRefresher {

    /**
     * Refreshes the UI
     */
    public void refreshUI();
}
