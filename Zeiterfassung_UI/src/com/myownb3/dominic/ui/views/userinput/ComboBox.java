/**
 * 
 */
package com.myownb3.dominic.ui.views.userinput;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JComboBox;

import com.myownb3.dominic.ui.views.userinput.entry.Entry;
import com.myownb3.dominic.ui.views.userinput.entry.EntrySorter;

/**
 * @author Dominic
 * 
 */
public class ComboBox extends JComboBox<Entry> {
    private int maxAmountOfEntries;
    private int counter;

    public ComboBox(int amountOfEntries) {
	super();
	this.maxAmountOfEntries = amountOfEntries;
	counter = 0;
    }

    /**
    * 
    */
    private static final long serialVersionUID = 1L;

    /**
     * @param description
     */
    public void addNewItem(Entry item) {
	// Item does not exist at the moment --> add it, and set the amount of
	// calls to 1
	if (!listContain(item)) {
	    super.addItem(item);
	    counter++; // since a new Item was added, increment the counter
	}
	// entry already exist, increment the amount of calls
	else {
	    item = getExistingItemFromList(item);
	}
	item.setLastUsage();
	cleanUp();
    }

    /*
     * Returns the (existing) Item within the list, for the given one. The given
     * one may have been created recently but there exist already an identical
     * one
     */
    private Entry getExistingItemFromList(Entry item) {
	// = evaluate the index of the existing one in the list
	int index = 0;
	for (int i = 0; i < getItemCount(); i++) {
	    if (getItemAt(i).getValue().equals(item.getValue())) {
		index = i;
		break;
	    }
	}
	// = return the existing one for the found index
	return getItemAt(index);
    }

    @Override
    public String getSelectedItem() {
	if (super.getSelectedItem() instanceof Entry) {
	    return ((Entry) super.getSelectedItem()).getValue();
	}
	return (String) super.getSelectedItem();
    }

    @Override
    public void addItem(Entry item) {
	super.addItem(item);
    }

    /**
    * 
    */
    private void cleanUp() {
	if (counter > maxAmountOfEntries) {
	    removeLastRecentlyUsed();
	    counter--; // since an element was removed, decrement the counter
	}
    }

    /**
     * @param description
     * @return
     */
    private boolean listContain(Entry description) {
	for (int i = 0; i < getItemCount(); i++) {
	    if (getItemAt(i).getValue().equals(description.getValue())) {
		return true;
	    }
	}
	return false;
    }

    /*
     * Removes the element which was used the fewest. Therefore each entry is
     * compared against the next one. The one which was used at a time, which is
     * the longest away from now, is remembered until the end - and then it's
     * deleted!
     */
    private void removeLastRecentlyUsed() {
	// = sort the list, so the entry which was used most recently is at the
	// end, and the one which was NOT used for longest time is at the
	// beginning
	List<Entry> currentSnapShotList = getElementsAsList();
	Collections.sort(currentSnapShotList, new EntrySorter());

	// = delete the first entry (was not used for the longest time)
	removeItem(currentSnapShotList.get(0));
    }

    /**
     * @return
     */
    private List<Entry> getElementsAsList() {
	List<Entry> list = new ArrayList<>(getItemCount());
	for (int i = 0; i < getItemCount(); i++) {
	    list.add(getItemAt(i));
	}
	return list;
    }
}
