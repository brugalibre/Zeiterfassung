package com.myownb3.dominic.ui.app.pages.stopbusinessday.control.combobox;

import java.util.Comparator;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

public class EntrySorter implements Comparator<Entry> {
    /**
     * Return 0 if both {@link Entry} where used the last time at the same time
     * Return 1 if the firstEntry was used more recently then the second one Return
     * -1 if the secondEntry was used more recently then the first one
     * 
     * @param firstEntry  the first {@link Entry} to compare
     * @param secondEntry the second {@link Entry} to compare
     */
    @Override
    public int compare(Entry firstEntry, Entry secondEntry) {
	if (firstEntry.getLastUsage() > secondEntry.getLastUsage()) {
	    return 1;
	} else if (firstEntry.getLastUsage() < secondEntry.getLastUsage()) {
	    return -1;
	} else {
	    return 0;
	}
    }

    @Override
    public Comparator<Entry> reversed() {
	return null;
    }

    @Override
    public Comparator<Entry> thenComparing(Comparator<? super Entry> comparator) {
	return null;
    }

    @Override
    public <U extends Comparable<? super U>> Comparator<Entry> thenComparing(
	    Function<? super Entry, ? extends U> arg0) {
	return null;
    }

    @Override
    public <U> Comparator<Entry> thenComparing(Function<? super Entry, ? extends U> arg0, Comparator<? super U> arg1) {
	return null;
    }

    @Override
    public Comparator<Entry> thenComparingDouble(ToDoubleFunction<? super Entry> arg0) {
	return null;
    }

    @Override
    public Comparator<Entry> thenComparingInt(ToIntFunction<? super Entry> arg0) {
	return null;
    }

    @Override
    public Comparator<Entry> thenComparingLong(ToLongFunction<? super Entry> arg0) {
	return null;
    }
}
