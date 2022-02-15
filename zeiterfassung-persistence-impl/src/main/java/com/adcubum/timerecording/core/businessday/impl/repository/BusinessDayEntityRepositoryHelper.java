package com.adcubum.timerecording.core.businessday.impl.repository;

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
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import static java.util.Objects.nonNull;

@Component
public class BusinessDayEntityRepositoryHelper {

   private BusinessDayDao businessDayDao;
   private BusinessDayIncrementDao businessDayIncrementDao;
   private TimeSnippetDao timeSnippetDao;
   private ComeAndGoesDao comeAndGoesDao;
   private ComeAndGoDao comeAndGoDao;

   @Autowired
   public BusinessDayEntityRepositoryHelper(BusinessDayDao businessDayDao, BusinessDayIncrementDao businessDayIncrementDao,
                                            TimeSnippetDao timeSnippetDao, ComeAndGoesDao comeAndGoesDao,
                                            ComeAndGoDao comeAndGoDao) {
      this.businessDayDao = businessDayDao;
      this.businessDayIncrementDao = businessDayIncrementDao;
      this.timeSnippetDao = timeSnippetDao;
      this.comeAndGoesDao = comeAndGoesDao;
      this.comeAndGoDao = comeAndGoDao;
   }

   public void setNonMappedBusinessDayEntityAttrs(BusinessDayEntity businessDayEntity2Change) {
      findAndSetNonMappedEntityAttr4BaseEntity(businessDayEntity2Change);
      setNonMappedBusinessDayIncrementEntityAttrs(businessDayEntity2Change.getBusinessDayIncrementEntities());
   }

   public void setNonMappedComeAndGoesEntityAttrs(ComeAndGoesEntity comeAndGoesEntity) {
      findAndSetNonMappedEntityAttr4BaseEntity(comeAndGoesEntity);
      setNonMappedComeAndGoEntityAttrs(comeAndGoesEntity.getComeAndGoEntriesEntities());
   }

   private void setNonMappedComeAndGoEntityAttrs(List<ComeAndGoEntity> comeAndGoEntities) {
      comeAndGoEntities.stream()
              .filter(comeAndGoEntity -> nonNull(comeAndGoEntity.getId()))
              .forEach(findAndSetNonMappedEntityAttr4BaseEntity()
                      .andThen(baseEntity -> findAndSetNonMappedEntityAttr4BaseEntity(((ComeAndGoEntity) baseEntity).getTimeSnippetEntity())));
   }

   private void setNonMappedBusinessDayIncrementEntityAttrs(List<BusinessDayIncrementEntity> businessDayIncrementEntities2Change) {
      businessDayIncrementEntities2Change.stream()
              .filter(baseEntity -> nonNull(baseEntity.getId()))
              .forEach(findAndSetNonMappedEntityAttr4BaseEntity()
                      .andThen(baseEntity -> findAndSetNonMappedEntityAttr4BaseEntity(((BusinessDayIncrementEntity) baseEntity).getCurrentTimeSnippetEntity())));
   }

   private <T extends BaseEntity> Consumer<T> findAndSetNonMappedEntityAttr4BaseEntity() {
      return this::findAndSetNonMappedEntityAttr4BaseEntity;
   }

   private <T extends BaseEntity> void findAndSetNonMappedEntityAttr4BaseEntity(T changedBaseEntity) {
      CrudRepository<T, UUID> repo = getRepoForEntity(changedBaseEntity);
      repo.findById(changedBaseEntity.getId())
              .ifPresent(persistentBaseEntity -> {
                 changedBaseEntity.setCreatedDate(persistentBaseEntity.getCreatedDate());
                 changedBaseEntity.setLastModifiedDate(persistentBaseEntity.getLastModifiedDate());
              });
   }

   private <T extends BaseEntity, R extends CrudRepository<T, UUID>> R getRepoForEntity(T changedBaseEntity) {
      if (changedBaseEntity instanceof BusinessDayEntity) {
         return (R) businessDayDao;
      } else if (changedBaseEntity instanceof BusinessDayIncrementEntity) {
         return (R) businessDayIncrementDao;
      } else if (changedBaseEntity instanceof TimeSnippetEntity) {
         return (R) timeSnippetDao;
      } else if (changedBaseEntity instanceof ComeAndGoesEntity) {
         return (R) comeAndGoesDao;
      } else if (changedBaseEntity instanceof ComeAndGoEntity) {
         return (R) comeAndGoDao;
      }
      throw new IllegalStateException("No repo found for BaseEntity " + changedBaseEntity + "!");
   }

   public BusinessDayDao getBusinessDayDao() {
      return businessDayDao;
   }

   public ComeAndGoesDao getComeAndGoesDao() {
      return comeAndGoesDao;
   }
}
