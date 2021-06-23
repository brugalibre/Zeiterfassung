/**
 * 
 */
package com.adcubum.timerecording.settings.round;

import java.util.Arrays;
import java.util.function.Predicate;

import com.adcubum.timerecording.settings.round.constant.RoundModeConst;
import com.adcubum.timerecording.work.date.TimeType.TIME_TYPE;

/**
 * The {@link RoundMode} defines how the recorded time is rounded as soon as a recording is stopped
 * The recorded time can be rounded as
 * - with a precision of 1 minute
 * - with a precision of 5 minutes
 * - with a precision of 10 minutes
 * - with a precision of 15 minutes
 * 
 * @author Dominic
 *
 */
public enum RoundMode {

   SEC(TIME_TYPE.SEC, 0),
   ONE_MIN(TIME_TYPE.MIN, 1),
   FIVE_MIN(TIME_TYPE.MIN, 5),
   TEN_MIN(TIME_TYPE.MIN, 10),
   FIFTEEN_MIN(TIME_TYPE.MIN, 15);

   /* package */ static final String PROPERTY_KEY = RoundModeConst.SETTINGS_ROUND_KEY;
   private TIME_TYPE timeType;
   private int amount;

   private RoundMode(TIME_TYPE timeType, int amount) {
      this.timeType = timeType;
      this.amount = amount;
   }

   /**
    * Returns the {@link RoundMode} for the given amount
    * 
    * @param roundAmountAsString
    * @return the {@link RoundMode} for the given amount
    */
   public static RoundMode getRoundMode(String roundAmountAsString) {
      return Arrays.asList(RoundMode.values())
            .stream()
            .filter(isSameAmount(roundAmountAsString))
            .findFirst()
            .orElse(RoundMode.ONE_MIN);
   }

   private static Predicate<? super RoundMode> isSameAmount(String roundAmountAsString) {
      int amount = convertToString(roundAmountAsString);
      return roundMode -> amount == roundMode.getAmount();
   }

   private static int convertToString(String roundAmountAsString) {
      return roundAmountAsString == null ? -1 : Integer.valueOf(roundAmountAsString);
   }

   public final TIME_TYPE getTimeType() {
      return this.timeType;
   }

   public final int getAmount() {
      return this.amount;
   }

}
