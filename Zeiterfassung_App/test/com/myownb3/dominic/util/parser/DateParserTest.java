package com.myownb3.dominic.util.parser;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.text.ParseException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

class DateParserTest {

   @Test
   void testConvertInputInvalidInput() {
      // Given
      String input = "00:0";

      // When
      Executable exec = () -> DateParser.convertInput(input);

      // Then
      assertThrows(ParseException.class, exec);
   }

   @Test
   void testConvertInputWithDoublePoints() throws ParseException {
      // Given
      String input = "15:20:11";
      String expectedConvertedInput = "15:20:11";

      // When
      String actualConvertedInput = DateParser.convertInput(input);

      // Then
      assertThat(actualConvertedInput, is(expectedConvertedInput));
   }

   @Test
   void testConvertInputInputWithLotsOfZeros() throws ParseException {
      // Given
      String input = "101000000000000";
      String expectedConvertedInput = "10:10:00";

      // When
      String actualConvertedInput = DateParser.convertInput(input);

      // Then
      assertThat(actualConvertedInput, is(expectedConvertedInput));
   }

   @Test
   void testConvertInputWithoutDoublePoints() throws ParseException {
      // Given
      String input = "101010";
      String expectedConvertedInput = "10:10:10";

      // When
      String actualConvertedInput = DateParser.convertInput(input);

      // Then
      assertThat(actualConvertedInput, is(expectedConvertedInput));
   }

   @Test
   void testConvertInputWithoutSec() throws ParseException {
      // Given
      String input = "1010";
      String expectedConvertedInput = "10:10:00";

      // When
      String actualConvertedInput = DateParser.convertInput(input);

      // Then
      assertThat(actualConvertedInput, is(expectedConvertedInput));
   }

}
