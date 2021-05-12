package com.adcubum.timerecording.ui.app.inputfield;

import static java.util.Objects.isNull;

import java.text.ParseException;

import com.adcubum.timerecording.core.work.businessday.TimeSnippet;
import com.adcubum.util.parser.DateParser;
import com.adcubum.util.utils.StringUtil;

import javafx.util.StringConverter;

public class TimeStampStringConverter extends StringConverter<String> {

   @Override
   public String toString(String object) {
      if (isNull(object) || StringUtil.isEqual("null", object)) {
         return TimeSnippet.NULL_TIME_REP;
      }
      return convertInput(object);
   }

   @Override
   public String fromString(String string) {
      return convertInput(string);
   }


   private String convertInput(String newTimeStampValue) {
      try {
         return DateParser.convertInput(newTimeStampValue);
      } catch (ParseException e) {
         // ignore
      }
      return newTimeStampValue;
   }

}
