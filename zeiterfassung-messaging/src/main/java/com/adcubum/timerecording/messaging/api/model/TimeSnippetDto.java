package com.adcubum.timerecording.messaging.api.model;

import java.util.Objects;

public class TimeSnippetDto {
   private long beginTimeStamp;
   private long endTimeStamp;

   @Override
   public String toString() {
      return "TimeSnippetMessageDto{" +
              "beginTimeStamp=" + beginTimeStamp +
              ", endTimeStamp=" + endTimeStamp +
              '}';
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      TimeSnippetDto that = (TimeSnippetDto) o;
      return beginTimeStamp == that.beginTimeStamp && endTimeStamp == that.endTimeStamp;
   }

   @Override
   public int hashCode() {
      return Objects.hash(beginTimeStamp, endTimeStamp);
   }

   public long getBeginTimeStamp() {
      return beginTimeStamp;
   }

   public void setBeginTimeStamp(long beginTimeStamp) {
      this.beginTimeStamp = beginTimeStamp;
   }

   public long getEndTimeStamp() {
      return endTimeStamp;
   }

   public void setEndTimeStamp(long endTimeStamp) {
      this.endTimeStamp = endTimeStamp;
   }

   public static class TimeSnippetDtoBuilder {
      private final TimeSnippetDto timeSnippetDto;

      private TimeSnippetDtoBuilder() {
         this.timeSnippetDto = new TimeSnippetDto();
      }

      public static TimeSnippetDtoBuilder of() {
         return new TimeSnippetDtoBuilder();
      }

      public TimeSnippetDtoBuilder withBeginTimeStampValue(long beginTimeStamp) {
         this.timeSnippetDto.setBeginTimeStamp(beginTimeStamp);
         return this;
      }

      public TimeSnippetDtoBuilder withEndTimeStampValue(long endTimeStamp) {
         this.timeSnippetDto.setEndTimeStamp(endTimeStamp);
         return this;
      }

      public TimeSnippetDto build() {
         return this.timeSnippetDto;
      }
   }
}
