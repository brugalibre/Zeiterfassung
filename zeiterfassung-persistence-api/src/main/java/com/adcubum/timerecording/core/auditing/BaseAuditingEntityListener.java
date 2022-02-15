package com.adcubum.timerecording.core.auditing;

import com.adcubum.timerecording.core.businessday.common.entity.BaseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Auditable;
import org.springframework.stereotype.Component;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import static java.util.Objects.isNull;

@Component
public class BaseAuditingEntityListener {

   @Autowired
   @Lazy
   private BaseEntityEvaluator baseEntityEvaluator;
//
//   private ObjectFactory<AuditingHandler> handler;
//
//   public TimeRecordingAuditingEntityListener(ObjectFactory<AuditingHandler> auditingHandler) {
//      this.handler = requireNonNull(auditingHandler);
//   }

   @PrePersist
   public void touchForCreate(Object target) {

      if (target instanceof BaseEntity) {
         BaseEntity baseEntity = (BaseEntity) target;
         if (isNull(baseEntity.getCreatedDate())) {
            LocalDateTime now = LocalDateTime.now();
            baseEntity.setCreatedDate(Timestamp.valueOf(now));
            baseEntity.setLastModifiedDate(Timestamp.valueOf(now));
         }
         System.out.println(baseEntity.getLastModifiedDate());
      }
   }

   /**
    * Sets modification and creation date and auditor on the target object in case it implements {@link Auditable} on
    * update events.
    *
    * @param target
    */
   @PreUpdate
   public void touchForUpdate(Object target) {

      if (target instanceof BaseEntity) {
         BaseEntity baseEntity = (BaseEntity) target;
         if (this.baseEntityEvaluator.hasEntityChanged(baseEntity)) {
            baseEntity.setLastModifiedDate(Timestamp.valueOf(LocalDateTime.now()));
         }
         System.out.println(baseEntity.getLastModifiedDate());
      }
   }
}
