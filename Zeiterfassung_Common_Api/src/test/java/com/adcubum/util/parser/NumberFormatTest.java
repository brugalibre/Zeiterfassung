package com.adcubum.util.parser;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

class NumberFormatTest {

   @Test
   void testInvalidValue2ParseGetDefault() {
      // Given
      String input = "abc";
      float defaultValue = 349f;

      // When
      float actualValue = com.adcubum.util.parser.NumberFormat.parseFloatOrDefault(input, defaultValue);

      // Then
      assertThat(actualValue, is(defaultValue));
   }

   @Test
   void testInvalidValue2ParseThrowNumberFormatException() {
      // Given
      String input = "abc";

      // When
      Executable exe = () -> com.adcubum.util.parser.NumberFormat.parseFloat(input);

      // Then
      assertThrows(NumberFormatException.class, exe);
   }

   @Test
   void testNeutralizeDecimalSeparator_SwissLocale() {
      // Given
      String input = ",15";
      String expectedInput = ".15";

      // When
      NumberFormat numberFormat = NumberFormat.getInstance(Locale.forLanguageTag("de_CH"));
      String neutralizedInput = com.adcubum.util.parser.NumberFormat.neutralizeDecimalSeparator(input, numberFormat);

      // Then
      assertThat(neutralizedInput, is(expectedInput));
   }

   @Test
   void testNeutralizeDecimalSeparator_DecimalFormatWithCommaAsSepparator() {
      // Given
      String input = ".15";
      String expectedInput = ",15";

      // When
      DecimalFormat numberFormat = mockDecimalFormat();
      String neutralizedInput = com.adcubum.util.parser.NumberFormat.neutralizeDecimalSeparator(input, numberFormat);

      // Then
      assertThat(neutralizedInput, is(expectedInput));
   }

   @Test
   void testNeutralizeDecimalSeparator_NullInput() {
      // Given
      String input = null;
      String expectedInput = "";

      // When
      DecimalFormat numberFormat = mock(DecimalFormat.class);
      String neutralizedInput = com.adcubum.util.parser.NumberFormat.neutralizeDecimalSeparator(input, numberFormat);

      // Then
      assertThat(neutralizedInput, is(expectedInput));
   }

   private static DecimalFormat mockDecimalFormat() {
      DecimalFormat numberFormat = mock(DecimalFormat.class);
      DecimalFormatSymbols decimalFormatSymbols = mock(DecimalFormatSymbols.class);
      when(decimalFormatSymbols.getDecimalSeparator()).thenReturn(',');
      when(numberFormat.getDecimalFormatSymbols()).thenReturn(decimalFormatSymbols);
      return numberFormat;
   }
}
