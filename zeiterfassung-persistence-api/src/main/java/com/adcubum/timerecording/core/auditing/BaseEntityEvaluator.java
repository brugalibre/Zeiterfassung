package com.adcubum.timerecording.core.auditing;

import com.adcubum.timerecording.core.businessday.comeandgo.dao.ComeAndGoDao;
import com.adcubum.timerecording.core.businessday.comeandgo.dao.ComeAndGoesDao;
import com.adcubum.timerecording.core.businessday.comeandgo.entity.ComeAndGoEntity;
import com.adcubum.timerecording.core.businessday.comeandgo.entity.ComeAndGoesEntity;
import com.adcubum.timerecording.core.businessday.common.entity.BaseEntity;
import com.adcubum.timerecording.core.businessday.dao.BusinessDayDao;
import com.adcubum.timerecording.core.businessday.dao.BusinessDayIncrementDao;
import com.adcubum.timerecording.core.businessday.dao.TimeSnippetDao;
import com.adcubum.timerecording.core.businessday.entity.BusinessDayEntity;
import com.adcubum.timerecording.core.businessday.entity.BusinessDayIncrementEntity;
import com.adcubum.timerecording.core.businessday.entity.TimeSnippetEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

@Component
public class BaseEntityEvaluator {
   @Autowired
   @Lazy
   private BusinessDayDao businessDayDao;

   @Autowired
   @Lazy
   private BusinessDayIncrementDao businessDayIncrementDao;

   @Autowired
   @Lazy
   private TimeSnippetDao timeSnippetDao;

   @Autowired
   @Lazy
   private ComeAndGoesDao comeAndGoesDao;

   @Autowired
   @Lazy
   private ComeAndGoDao comeAndGoDao;

   /**
    * @param changedBaseEntity the changed {@link BaseEntity}
    * @param <T>
    * @return <code>true</code> if the given {@link BaseEntity} has  changed, compared to its persistent pendant
    * or <code>false</code> if there are no changes,
    * the entity is not persistent or has no last modified date
    */
   public <T extends BaseEntity> boolean hasEntityChanged(T changedBaseEntity) {
      return findEntityById(changedBaseEntity)
              .map(persistentBaseEntity -> changedBaseEntity.hasChangesForLastModified(persistentBaseEntity))
              .orElse(false);
   }

   private <T extends BaseEntity> Optional<T> findEntityById(T changedOther) {
      CrudRepository<T, UUID> repo = getRepoForEntity(changedOther);
      return repo.findById(changedOther.getId());
   }

   private <T extends BaseEntity, V extends CrudRepository<T, UUID>> V getRepoForEntity(T baseEntity) {
      if (baseEntity instanceof BusinessDayEntity) {
         return (V) businessDayDao;
      } else if (baseEntity instanceof ComeAndGoEntity) {
         return (V) comeAndGoDao;
      } else if (baseEntity instanceof ComeAndGoesEntity) {
         return (V) comeAndGoesDao;
      } else if (baseEntity instanceof BusinessDayIncrementEntity) {
         return (V) this.businessDayIncrementDao;
      } else if (baseEntity instanceof TimeSnippetEntity) {
         return (V) this.timeSnippetDao;
      }
      throw new IllegalStateException("No repository found for BaseEntity '" + baseEntity + "'. This Entity may not be annotated with the " + BaseAuditingEntityListener.class + "' class");
   }

   private static <T extends BaseEntity> Supplier<IllegalStateException> throwException(T baseEntity) {
      return () -> new IllegalStateException("No entity found for '" + baseEntity + "'");
   }
}
