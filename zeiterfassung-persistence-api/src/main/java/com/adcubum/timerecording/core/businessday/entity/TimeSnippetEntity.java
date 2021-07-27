package com.adcubum.timerecording.core.businessday.entity;

import java.sql.Timestamp;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.lang.NonNull;

import com.adcubum.timerecording.core.businessday.common.entity.BaseEntity;

@Entity
@Table(name = "timesnippet")
public class TimeSnippetEntity extends BaseEntity {

   @NonNull
   private Timestamp beginTimestamp;
   private Timestamp endTimestamp;

   private TimeSnippetEntity() {
      super(null);
   }

   /**
    * Creates a new {@link TimeSnippetEntity}
    * 
    * @param id
    *        the id
    * @param beginTime
    *        the {@link Timestamp} of the begin time stamp
    * @param endTime
    *        the {@link Timestamp} of the end time stamp
    */
   public TimeSnippetEntity(UUID id, Timestamp beginTime, Timestamp endTime) {
      super(id);
      this.beginTimestamp = beginTime;
      this.endTimestamp = endTime;
   }

   public Timestamp getBeginTimestamp() {
      return beginTimestamp;
   }

   public Timestamp getEndTimestamp() {
      return endTimestamp;
   }

   @Override
   public String toString() {
      return String.format(
            "TimeSnippetEntity[id=%s, beginTimestamp='%s', endTimestamp='%s']",
            id, beginTimestamp, endTimestamp);
   }
}
