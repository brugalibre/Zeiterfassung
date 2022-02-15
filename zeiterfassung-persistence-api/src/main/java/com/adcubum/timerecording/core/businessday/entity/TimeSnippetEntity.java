package com.adcubum.timerecording.core.businessday.entity;

import com.adcubum.timerecording.core.businessday.common.entity.BaseEntity;
import org.springframework.lang.NonNull;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.UUID;

import static java.util.Objects.nonNull;

@Entity
@Table(name = "timesnippet")
public class TimeSnippetEntity extends BaseEntity {

   @NonNull
   private Timestamp beginTimestamp;
   private Timestamp endTimestamp;

   /*
    * Private constructor used by jpa
    */
   @SuppressWarnings("unused")
   private TimeSnippetEntity() {
      this(null, new Timestamp(System.currentTimeMillis()), null);
   }

   /**
    * Creates a new {@link TimeSnippetEntity}
    *
    * @param id        the id
    * @param beginTime the {@link Timestamp} of the begin time stamp
    * @param endTime   the {@link Timestamp} of the end time stamp
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
   public <T extends BaseEntity> boolean hasChangesForLastModified(T persistentOther) {
      if (persistentOther instanceof TimeSnippetEntity) {
         TimeSnippetEntity persistentTimeSnippetEntity = (TimeSnippetEntity) persistentOther;
         return nonNull(this.getBeginTimestamp()) && this.getBeginTimestamp().equals(persistentTimeSnippetEntity.getBeginTimestamp())
                 || nonNull(this.getEndTimestamp()) && nonNull(persistentTimeSnippetEntity.getEndTimestamp())
                 && this.getEndTimestamp().equals(persistentTimeSnippetEntity.getEndTimestamp());
      }
      return false;
   }

   @Override
   public String toString() {
      return String.format(
              "TimeSnippetEntity[id=%s, beginTimestamp='%s', endTimestamp='%s']",
              id, beginTimestamp, endTimestamp);
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = super.hashCode();
      result = prime * result + ((beginTimestamp == null) ? 0 : beginTimestamp.hashCode());
      result = prime * result + ((endTimestamp == null) ? 0 : endTimestamp.hashCode());
      return result;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (!super.equals(obj))
         return false;
      if (getClass() != obj.getClass())
         return false;
      TimeSnippetEntity other = (TimeSnippetEntity) obj;
      if (beginTimestamp == null) {
         if (other.beginTimestamp != null)
            return false;
      } else if (!beginTimestamp.equals(other.beginTimestamp))
         return false;
      if (endTimestamp == null) {
         if (other.endTimestamp != null)
            return false;
      } else if (!endTimestamp.equals(other.endTimestamp))
         return false;
      return true;
   }


}
