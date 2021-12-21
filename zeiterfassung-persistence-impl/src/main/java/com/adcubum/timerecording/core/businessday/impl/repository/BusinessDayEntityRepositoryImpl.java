package com.adcubum.timerecording.core.businessday.impl.repository;

import com.adcubum.timerecording.core.businessday.comeandgo.dao.ComeAndGoesDao;
import com.adcubum.timerecording.core.businessday.comeandgo.entity.ComeAndGoesEntity;
import com.adcubum.timerecording.core.businessday.dao.BusinessDayDao;
import com.adcubum.timerecording.core.businessday.entity.BusinessDayEntity;
import com.adcubum.timerecording.core.businessday.entity.repository.BusinessDayEntityRepository;
import com.adcubum.timerecording.core.repository.ObjectNotFoundException;
import com.adcubum.timerecording.work.date.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository("business-day-entity-repository")
public class BusinessDayEntityRepositoryImpl implements BusinessDayEntityRepository {

   private static final Logger LOG = LoggerFactory.getLogger(BusinessDayEntityRepositoryImpl.class);

   private BusinessDayDao businessDayDao;
   private ComeAndGoesDao comeAndGoesDao;

   @Autowired
   public BusinessDayEntityRepositoryImpl(BusinessDayDao businessDayDao, ComeAndGoesDao comeAndGoesDao) {
      this.businessDayDao = businessDayDao;
      this.comeAndGoesDao = comeAndGoesDao;
   }

   @Override
   public List<BusinessDayEntity> findAllBookedBusinessDayEntitiesWithinRange(DateTime lowerBounds, DateTime upperBounds) {
      LOG.info("Find all booked business-days within range '{}' upto '{}'", lowerBounds, upperBounds);
      Timestamp lowerBoundsTimestamp = new Timestamp(lowerBounds.getTime());
      Timestamp upperBoundsTimestamp = new Timestamp(upperBounds.getTime());
      List<BusinessDayEntity> allBusinessDayEntitiesWithinRange =
            businessDayDao.findAllBookedBusinessDayEntitiesWithinRange(lowerBoundsTimestamp, upperBoundsTimestamp);
      return setAdditionalComesAndGoes(allBusinessDayEntitiesWithinRange);
   }

   private List<BusinessDayEntity> setAdditionalComesAndGoes(List<BusinessDayEntity> businessDayEntities) {
      return businessDayEntities.stream()
            .map(findAndSetComesAndGoes())
            .collect(Collectors.toList());
   }

   @Override
   public Optional<BusinessDayEntity> findBookedBusinessDayEntityWithinRange(DateTime lowerBounds, DateTime upperBounds) {
      LOG.info("Find a single booked business-days within range '{}' upto '{}'", lowerBounds, upperBounds);
      Timestamp lowerBoundsTimestamp = new Timestamp(lowerBounds.getTime());
      Timestamp upperBoundsTimestamp = new Timestamp(upperBounds.getTime());
      return businessDayDao.findAllBookedBusinessDayEntitiesWithinRange(lowerBoundsTimestamp, upperBoundsTimestamp)
            .stream()
            .findFirst()
            .map(findAndSetComesAndGoes());
   }

   private Function<BusinessDayEntity, BusinessDayEntity> findAndSetComesAndGoes() {
      return businessDayEntity -> {
         ComeAndGoesEntity comeAndGoesEntity = findExistingOrCreateEmptyComesAndGoes(businessDayEntity.getComeAndGoesEntityId());
         businessDayEntity.setComeAndGoesEntity(comeAndGoesEntity);
         return businessDayEntity;
      };
   }

   private ComeAndGoesEntity findExistingOrCreateEmptyComesAndGoes(UUID comesAndGoesId) {
      Optional<ComeAndGoesEntity> comesAndGoesEntityOpt = comeAndGoesDao.findById(comesAndGoesId);
      return comesAndGoesEntityOpt
            .orElse(new ComeAndGoesEntity());
   }

   @Override
   public BusinessDayEntity findFirstOrCreateNew() {
      LOG.info("Looking for an existing business-day or create a new one");
      List<UUID> allBusinessDayEntityIds = businessDayDao.findAllBusinessDayIds4BookingStatus(false);
      logAmountOfExistingBusinessDayFound(allBusinessDayEntityIds);
      if (!allBusinessDayEntityIds.isEmpty()) {
         return findById(allBusinessDayEntityIds.get(0));
      }
      return createNew(false);
   }

   @Override
   public BusinessDayEntity createNew(boolean isBooked) {
      LOG.info("Create new business-day, isBooked={}", isBooked);
      BusinessDayEntity businessDayEntity = createNewBusinessDayEntity(isBooked);
      LOG.info("Created new business-day'{}'", businessDayEntity);
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
      LOG.info("Found a single business-day '{}'", businessDayEntity);
      return businessDayEntity;
   }

   @Override
   public BusinessDayEntity save(BusinessDayEntity businessDayEntity) {
      LOG.info("Save business-day '{}'", businessDayEntity);
      ComeAndGoesEntity comeAndGoesEntity = comeAndGoesDao.save(businessDayEntity.getComeAndGoesEntity());
      businessDayEntity.setComeAndGoesEntity(comeAndGoesEntity);// set, so the BusinessDayEntity::comeandgoes_id is persisted
      BusinessDayEntity savedBusinessDayEntity = businessDayDao.save(businessDayEntity);
      savedBusinessDayEntity.setComeAndGoesEntity(comeAndGoesEntity); // set, so the BusinessDayEntity-value is available
      return savedBusinessDayEntity;
   }

   @Override
   public void deleteAll(boolean isBooked) {
      LOG.info("Delete all business-days, isBooked={}", isBooked);
      businessDayDao.findAllBusinessDayIds4BookingStatus(isBooked)
            .stream()
            .map(this::getBusinessDayEntity)
            .forEach(this::deleteCompleteBusinessDayEntity);
   }

   @Override
   public void deleteBookedBusinessDaysWithinRange(DateTime lowerBounds, DateTime upperBounds) {
      LOG.info("Delete all booked business-days within range '{}' upto '{}'", lowerBounds, upperBounds);
      findAllBookedBusinessDayEntitiesWithinRange(lowerBounds, upperBounds)
            .stream()
            .forEach(this::deleteCompleteBusinessDayEntity);
   }

   private void deleteCompleteBusinessDayEntity(BusinessDayEntity businessDayEntity) {
      businessDayDao.delete(businessDayEntity);
      comeAndGoesDao.delete(businessDayEntity.getComeAndGoesEntity());
   }

   private BusinessDayEntity getBusinessDayEntity(UUID businessDayId) {
      return businessDayDao.findById(businessDayId)
            .orElseThrow(() -> new ObjectNotFoundException("No business-day found for id '" + businessDayId + "'"));
   }

   private static BusinessDayEntity createNewBusinessDayEntity(boolean isBooked) {
      return new BusinessDayEntity(isBooked);
   }

   private static void logAmountOfExistingBusinessDayFound(List<UUID> allBusinessDayEntityIds) {
      if (allBusinessDayEntityIds.size() > 1) {
         LOG.warn("Found total '{}' business-days, select first one. Check current db and remove unnecessary entries!", allBusinessDayEntityIds.size());
      } else if (allBusinessDayEntityIds.size() == 1) {
         LOG.info("Found one business-day to select from");
      } else {
         LOG.info("No existing business-day found, going to create a new one..");
      }
   }
}
