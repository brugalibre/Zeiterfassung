package com.myownb3.dominic.ui.app.pages.stopbusinessday.control.combobox;

public class Entry {
    private String value;
    private long lastUsage; // defines when this entry was used the last time

    public Entry(String value) {
	this.value = value;
    }

    public long getLastUsage() {
	return lastUsage;
    }

    public void setLastUsage() {
	this.lastUsage = System.currentTimeMillis();
    }

    public String getValue() {
	return value;
    }

    @Override
    public String toString() {
	return getValue();
    }
}
