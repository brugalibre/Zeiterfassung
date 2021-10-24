package com.adcubum.util.parser;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import com.adcubum.timerecording.work.date.DateTime;

class DateParserTest {

   @Test
   void testConvertAndParse2TimeUsingMinutes() throws ParseException {
      // Given
      String input1 = "700";
      String expectedToStringValue = "07:00";

      // When
      DateTime actualConvertedInput = DateParser.convertAndParse2Time(input1, TimeUnit.MINUTES);

      // Then
      assertThat(actualConvertedInput.toString(), is(expectedToStringValue));
   }

   @Test
   void testConvertAndParse2TimeUsingSeconds() throws ParseException {
      // Given
      String input1 = "070015";

      // When
      DateTime actualConvertedInput = DateParser.convertAndParse2Time(input1, TimeUnit.SECONDS);
      Date actualDate = new Date(actualConvertedInput.getTime());

      // Then
      Calendar c = Calendar.getInstance();
      c.setTime(actualDate);
      int actualHour = c.get(Calendar.HOUR);
      int actualMin = c.get(Calendar.MINUTE);
      int actualSec = c.get(Calendar.SECOND);
      assertThat(actualHour, is(7));
      assertThat(actualMin, is(0));
      assertThat(actualSec, is(15));
   }

   @Test
   void testConvertAndParse2TimeUnparsable() throws ParseException {
      // Given
      String input = "unparsable";

      // When
      Executable ex = () -> DateParser.convertAndParse2Time(input, TimeUnit.MINUTES);

      // Then
      assertThrows(IllegalStateException.class, ex);
   }

   @Test
   void testConvertInputWithNoLeadingZero() throws ParseException {
      // Given
      String input1 = "700";
      String expectedConvertedInput = "07:00";
      String input2 = "7:00";

      // When
      String actualConvertedInput1 = DateParser.convertInput(input1);
      String actualConvertedInput2 = DateParser.convertInput(input2);

      // Then
      assertThat(actualConvertedInput1, is(expectedConvertedInput));
      assertThat(actualConvertedInput2, is(expectedConvertedInput));
   }

   @Test
   void testConvertInputWithDoublePoints() throws ParseException {
      // Given
      String input = "15:20";
      String expectedConvertedInput = "15:20";

      // When
      String actualConvertedInput = DateParser.convertInput(input);

      // Then
      assertThat(actualConvertedInput, is(expectedConvertedInput));
   }

   @Test
   void testConvertInputInputWithLotsOfZeros() throws ParseException {
      // Given
      String input = "101000000000000";
      String expectedConvertedInput = "10:10";

      // When
      String actualConvertedInput = DateParser.convertInput(input);

      // Then
      assertThat(actualConvertedInput, is(expectedConvertedInput));
   }

   @Test
   void testConvertInputWithoutDoublePoints() throws ParseException {
      // Given
      String input = "101100";
      String expectedConvertedInput = "10:11";

      // When
      String actualConvertedInput = DateParser.convertInput(input);

      // Then
      assertThat(actualConvertedInput, is(expectedConvertedInput));
   }

   @Test
   void testConvertInputWithoutSec() throws ParseException {
      // Given
      String input = "1010";
      String expectedConvertedInput = "10:10";

      // When
      String actualConvertedInput = DateParser.convertInput(input);

      // Then
      assertThat(actualConvertedInput, is(expectedConvertedInput));
   }

}
