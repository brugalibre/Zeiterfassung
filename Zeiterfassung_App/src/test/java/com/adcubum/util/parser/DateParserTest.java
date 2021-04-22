package com.adcubum.util.parser;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.text.ParseException;

import org.junit.jupiter.api.Test;

class DateParserTest {

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
