package com.adcubum.timerecording.core.businessday.impl.repository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.adcubum.timerecording.core.businessday.comeandgo.dao.ComeAndGoesDao;
import com.adcubum.timerecording.core.businessday.comeandgo.entity.ComeAndGoesEntity;
import com.adcubum.timerecording.core.businessday.dao.BusinessDayDao;
import com.adcubum.timerecording.core.businessday.entity.BusinessDayEntity;
import com.adcubum.timerecording.core.businessday.entity.repository.BusinessDayEntityRepository;
import com.adcubum.timerecording.core.repository.ObjectNotFoundException;

@Repository("business-day-entity-repository")
public class BusinessDayEntityRepositoryImpl implements BusinessDayEntityRepository {

   private static Logger LOG = Logger.getLogger(BusinessDayEntityRepositoryImpl.class);

   private BusinessDayDao businessDayDao;
   private ComeAndGoesDao comeAndGoesDao;

   @Autowired
   public BusinessDayEntityRepositoryImpl(BusinessDayDao businessDayDao, ComeAndGoesDao comeAndGoesDao) {
      this.businessDayDao = businessDayDao;
      this.comeAndGoesDao = comeAndGoesDao;
   }

   @Override
   public BusinessDayEntity findFirstOrCreateNew() {
      List<BusinessDayEntity> allBusinessDayEntities = findAllAsList();
      logAmountOfExistingBusinessDayFound(allBusinessDayEntities);
      if (allBusinessDayEntities.size() >= 1) {
         BusinessDayEntity businessDayEntity = allBusinessDayEntities.get(0);
         ComeAndGoesEntity comeAndGoesEntity = findExistingComesAndGoEntity(businessDayEntity);
         businessDayEntity.setComeAndGoesEntity(comeAndGoesEntity);
         return businessDayEntity;
      }
      BusinessDayEntity businessDayEntity = createNewBusinessDayEntity();
      return save(businessDayEntity);
   }

   private ComeAndGoesEntity findExistingComesAndGoEntity(BusinessDayEntity businessDayEntity) {
      UUID comeAndGoesId = businessDayEntity.getComeAndGoesEntityId();
      return comeAndGoesDao.findById(comeAndGoesId)
            .orElse(new ComeAndGoesEntity());
   }

   @Override
   public BusinessDayEntity findById(UUID businessDayId) throws ObjectNotFoundException {
      BusinessDayEntity businessDayEntity = getBusinessDayEntity(businessDayId);
      ComeAndGoesEntity comeAndGoesEntity = findExistingComesAndGoEntity(businessDayEntity);
      businessDayEntity.setComeAndGoesEntity(comeAndGoesEntity);
      return businessDayEntity;
   }

   @Override
   public BusinessDayEntity save(BusinessDayEntity businessDayEntity) {
      ComeAndGoesEntity comeAndGoesEntity = comeAndGoesDao.save(businessDayEntity.getComeAndGoesEntity());
      businessDayEntity.setComeAndGoesEntity(comeAndGoesEntity);// set, so the BusinessDayEntity::comeandgoes_id is persisted
      BusinessDayEntity savedBusinessDayEntity = businessDayDao.save(businessDayEntity);
      savedBusinessDayEntity.setComeAndGoesEntity(comeAndGoesEntity); // set, so the BusinessDayEntity-value is available
      return savedBusinessDayEntity;
   }

   @Override
   public void deleteAll() {
      businessDayDao.deleteAll();
      comeAndGoesDao.deleteAll();
   }

   private List<BusinessDayEntity> findAllAsList() {
      return StreamSupport.stream(businessDayDao.findAll()
            .spliterator(), false)
            .collect(Collectors.toList());
   }

   private BusinessDayEntity getBusinessDayEntity(UUID businessDayId) {
      return businessDayDao.findById(businessDayId)
            .orElseThrow(() -> new ObjectNotFoundException("No BusinesDay found for id '" + businessDayId + "'"));
   }

   private static BusinessDayEntity createNewBusinessDayEntity() {
      return new BusinessDayEntity();
   }

   private static void logAmountOfExistingBusinessDayFound(List<BusinessDayEntity> allBusinessDayEntities) {
      if (allBusinessDayEntities.size() > 1) {
         LOG.warn("Found total '" + allBusinessDayEntities.size()
               + "' businessdays, select first one. Check current db and remove unnecessary entries!");
      } else if (allBusinessDayEntities.size() == 1) {
         LOG.info("Found one businessdays to select from'");
      } else {
         LOG.info("No existing businessday found, create new one");
      }
   }
}
