package com.myownb3.dominic.ui.app.pages.stopbusinessday.control.combobox;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.myownb3.dominic.util.utils.StringUtil;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

public class ComboBoxHelper {

   private int counter;
   private int maxAmountOfEntries;
   // Map to store the actual value to its entry (which has information about
   // usage) since the ComboBoxes works with Strings!
   private Map<String, Entry> value2EntryMap;
   private ComboBox<String> comboBox;

   public ComboBoxHelper(int amountOfEntries, ComboBox<String> comboBox) {
      super();

      value2EntryMap = new HashMap<>();
      this.maxAmountOfEntries = amountOfEntries;
      counter = 0;
      this.comboBox = comboBox;
   }

   /**
    * Adds the given String to this ComboBox. If there is any value which LRU value
    * is below the threshold, this value is removed
    * 
    * @param itemAsString
    *        - the new value to add
    */
   public void addNewItem(String itemAsString) {
      if (StringUtil.isEmptyOrNull(itemAsString)) {
         return;
      }
      Entry item = new Entry(itemAsString);
      // Item does not exist at the moment --> add it, and set the amount of
      // calls to 1
      if (!listContain(item)) {
         addItem(item);
         counter++; // since a new Item was added, increment the counter
      }
      // entry already exist, increment the amount of calls
      else {
         item = getExistingItemFromList(item);
      }
      item.setLastUsage();

      comboBox.setItems(FXCollections.observableList(getSortedEntries()));
      cleanUp();
   }

   private List<String> getSortedEntries() {
      return getElementsAsList().stream()
            .sorted(Comparator.comparing(Entry::getLastUsage).reversed())
            .map(Entry::getValue)
            .collect(Collectors.toList());
   }

   /*
    * Returns the (existing) Item within the list, for the given one. The given one
    * may have been created recently but there exist already an identical one
    */
   private Entry getExistingItemFromList(Entry item) {
      // = evaluate the index of the existing one in the list
      int index = 0;
      for (int i = 0; i < getItemSize(); i++) {
         if (getItemAt(i).getValue().equals(item.getValue())) {
            index = i;
            break;
         }
      }
      // = return the existing one for the found index
      return getItemAt(index);
   }

   private Entry getItemAt(int i) {

      for (int j = 0; j < getItemSize(); j++) {
         String entryValue = comboBox.getItems().get(j);
         if (i == j) {
            return value2EntryMap.get(entryValue);
         }
      }
      return null;
   }

   private void cleanUp() {
      if (counter > maxAmountOfEntries) {
         removeLastRecentlyUsed();
         counter--; // since an element was removed, decrement the counter
      }
   }

   private boolean listContain(Entry description) {
      for (int i = 0; i < getItemSize(); i++) {
         if (getItemAt(i).getValue().equals(description.getValue())) {
            return true;
         }
      }
      return false;
   }

   /*
    * Removes the element which was used the fewest. Therefore each entry is
    * compared against the next one. The one which was used at a time, which is the
    * longest away from now, is remembered until the end - and then it's deleted!
    */
   private void removeLastRecentlyUsed() {
      // = sort the list, so the entry which was used most recently is at the
      // end, and the one which was NOT used for longest time is at the
      // beginning
      List<Entry> currentSnapShotList = getElementsAsList();
      Collections.sort(currentSnapShotList, new EntrySorter());

      // = delete the first entry (was not used for the longest time)
      removeItem(currentSnapShotList);
   }

   private List<Entry> getElementsAsList() {
      return comboBox.getItems()
            .stream()
            .map(this::getEntryValue)
            .collect(Collectors.toList());
   }

   private Entry getEntryValue(String value) {
      return value2EntryMap.get(value);
   }

   private void removeItem(List<Entry> currentSnapShotList) {
      Entry firstEntry = currentSnapShotList.get(0);
      comboBox.getItems().remove(firstEntry.getValue());
   }

   private int getItemSize() {
      return comboBox.getItems().size();
   }

   private void addItem(Entry item) {
      ObservableList<String> items = comboBox.getItems();
      if (items.isEmpty()) {
         items.add(item.getValue());
         value2EntryMap.put(item.getValue(), item);
      } else {
         int size = items.size();
         items.add(size - 1, item.getValue());
         value2EntryMap.put(item.getValue(), item);
      }
   }

   /**
    * Sets the focus to the first element
    */
   public void setFocusToFirstElement() {
      comboBox.requestFocus();
      comboBox.getSelectionModel().clearAndSelect(0);
   }
}
