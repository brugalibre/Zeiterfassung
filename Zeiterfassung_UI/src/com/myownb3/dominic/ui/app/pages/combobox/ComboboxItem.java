package com.myownb3.dominic.ui.app.pages.combobox;

public class ComboboxItem {
   private String key;
   private String description;

   private ComboboxItem(String key, String value) {
      this.key = key;
      this.description = value;
   }

   @Override
   public String toString() {
      if (key == null || key.length() == 0) {
         return "";
      }
      return key + " (" + description + ")";
   }

   public String getKey() {
      return key;
   }

   public String getValue() {
      return getKey();
   }

   public static ComboboxItem of(String ticketNumber) {
      return new ComboboxItem(ticketNumber, "-");
   }

   public static ComboboxItem of(String key, String value) {
      return new ComboboxItem(key, value);
   }
}
