package com.adcubum.timerecording.core.work.businessday.mapper;

import com.adcubum.timerecording.core.businessday.comeandgo.entity.ComeAndGoesEntity;
import com.adcubum.timerecording.core.businessday.entity.BusinessDayEntity;
import com.adcubum.timerecording.core.businessday.entity.BusinessDayIncrementEntity;
import com.adcubum.timerecording.core.businessday.entity.TimeSnippetEntity;
import com.adcubum.timerecording.core.work.businessday.BusinessDay;
import com.adcubum.timerecording.core.work.businessday.BusinessDayIncrement;
import com.adcubum.timerecording.core.work.businessday.TimeSnippet;
import com.adcubum.timerecording.core.work.businessday.comeandgo.ComeAndGoes;
import com.adcubum.timerecording.core.work.businessday.comeandgo.impl.mapper.ComeAndGoesEntityMapper;
import com.adcubum.timerecording.core.work.businessday.factory.BusinessDayFactory;
import com.adcubum.timerecording.core.work.businessday.factory.BusinessDayIncrementFactory;
import com.adcubum.timerecording.jira.data.ticket.Ticket;
import com.adcubum.timerecording.jira.data.ticket.TicketActivity;
import com.adcubum.timerecording.ticketbacklog.TicketBacklogSPI;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.*;

/**
 * The {@link BusinessDayEntityMapper} is used as a Mapper in order to map from a {@link BusinessDay} into a {@link BusinessDayEntity}
 * and vice versa
 * 
 * @author dstalder
 *
 */
public class BusinessDayEntityMapper {

   /** Singleton instance of the {@link BusinessDayEntityMapper} */
   public static final BusinessDayEntityMapper INSTANCE = new BusinessDayEntityMapper();

   private BusinessDayEntityMapper() {
      // private
   }

   /**
    * Maps the given {@link BusinessDayEntity} into a {@link BusinessDay}
    * 
    * @param businessDayEntity
    *        the {@link BusinessDayEntity} to map from
    * @return a transient {@link BusinessDay}
    */
   public BusinessDay map2BusinessDay(BusinessDayEntity businessDayEntity) {
      requireNonNull(businessDayEntity);
      List<BusinessDayIncrement> increments = map2BusinessDayIncrements(businessDayEntity);
      TimeSnippet currentTimeSnippet = TimeSnippetEntityMapper.INSTANCE.map2TimeSnippet(businessDayEntity.getCurrentBusinessDayIncrementEntity());
      ComeAndGoes comeAndGoes = ComeAndGoesEntityMapper.INSTANCE.map2ComeAndGoes(businessDayEntity.getComeAndGoesEntity());
      return BusinessDayFactory.createNew(businessDayEntity.getId(), businessDayEntity.isBooked(), increments, currentTimeSnippet,
            comeAndGoes);
   }

   /**
    * Maps the given {@link BusinessDay} into a {@link BusinessDayEntity}
    * 
    * @param businessDay
    *        the {@link BusinessDay} to map from
    * @return a new {@link BusinessDayEntity}
    */
   public BusinessDayEntity map2BusinessDayEntity(BusinessDay businessDay) {
      requireNonNull(businessDay);
      BusinessDayEntity businessDayEntity = new BusinessDayEntity(businessDay.getId(), businessDay.isBooked());
      TimeSnippetEntity currentTimeSnippetEntity =
            TimeSnippetEntityMapper.INSTANCE.map2TimeSnippetEntity(businessDay.getCurrentTimeSnippet());
      List<BusinessDayIncrementEntity> businessDayIncrementEntities =
            map2BusinessDayIncrementEntities(businessDay.getIncrements(), businessDayEntity);
      ComeAndGoesEntity comeAndGoesEntity = ComeAndGoesEntityMapper.INSTANCE.map2ComeAndGoesEntity(businessDay.getComeAndGoes());
      businessDayEntity.setBusinessDayIncrementEntities(businessDayIncrementEntities);
      businessDayEntity.setCurrentTimeSnippetEntity(currentTimeSnippetEntity);
      businessDayEntity.setComeAndGoesEntity(comeAndGoesEntity);
      return businessDayEntity;
   }

   private static List<BusinessDayIncrementEntity> map2BusinessDayIncrementEntities(List<BusinessDayIncrement> bDayIncrements,
         BusinessDayEntity businessDayEntity) {
      return bDayIncrements
            .stream()
            .map(bDayInc -> map2CompleteBusinessDayIncrementEntity(bDayInc, businessDayEntity))
            .collect(Collectors.toList());
   }

   private static List<BusinessDayIncrement> map2BusinessDayIncrements(BusinessDayEntity businessDayEntity) {
      return businessDayEntity.getBusinessDayIncrementEntities()
            .stream()
            .map(BusinessDayEntityMapper::map2BusinessDayIncrement)
            .collect(Collectors.toList());
   }

   /**
    * Creates a new {@link BusinessDayIncrementEntity} for the given {@link BusinessDayIncrement} and id
    * 
    * @param businessDayIncrement
    *        the {@link BusinessDayIncrement}
    * @return a new {@link BusinessDayIncrementEntity} for the given {@link BusinessDayIncrement}
    */
   private static BusinessDayIncrementEntity map2CompleteBusinessDayIncrementEntity(BusinessDayIncrement businessDayIncrement,
         BusinessDayEntity businessDayEntity) {
      BusinessDayIncrementEntity businessDayIncrementEntity = map2BusinessDayIncrementEntity(businessDayIncrement, businessDayEntity);
      TimeSnippetEntity timeSnippetEntity = TimeSnippetEntityMapper.INSTANCE.map2TimeSnippetEntity(businessDayIncrement.getCurrentTimeSnippet());
      businessDayIncrementEntity.setCurrentTimeSnippetEntity(timeSnippetEntity);
      return businessDayIncrementEntity;
   }

   private static BusinessDayIncrementEntity map2BusinessDayIncrementEntity(BusinessDayIncrement businessDayIncrement,
         BusinessDayEntity businessDayEntity) {
      Ticket ticket = businessDayIncrement.getTicket();
      return new BusinessDayIncrementEntity(businessDayIncrement.getId(), businessDayEntity, businessDayIncrement.getDescription(),
            isNull(ticket) ? null : ticket.getNr(), businessDayIncrement.getTicketActivity().getActivityCode(), businessDayIncrement.isBooked());
   }

   /**
    * Creates a new {@link BusinessDayIncrement} for the given {@link BusinessDayIncrementEntity}
    * 
    * @param businessDayIncrementEntity
    *        the given {@link BusinessDayIncrementEntity}
    * @return a new {@link BusinessDayIncrement} for the given {@link BusinessDayIncrementEntity}
    */
   private static BusinessDayIncrement map2BusinessDayIncrement(BusinessDayIncrementEntity businessDayIncrementEntity) {
      TimeSnippet timeSnippet = TimeSnippetEntityMapper.INSTANCE.map2TimeSnippet(businessDayIncrementEntity.getCurrentTimeSnippetEntity());
      Ticket ticket = map2Ticket(businessDayIncrementEntity);
      TicketActivity ticketActivity = map2TicketActivity(businessDayIncrementEntity);
      return BusinessDayIncrementFactory.createNew(timeSnippet, businessDayIncrementEntity.getId(), businessDayIncrementEntity.getDescription(),
            ticket, ticketActivity, businessDayIncrementEntity.isBooked());
   }

   private static Ticket map2Ticket(BusinessDayIncrementEntity businessDayIncrementEntity) {
      String ticketNr = businessDayIncrementEntity.getTicketNr();
      if (nonNull(ticketNr)) {
         return TicketBacklogSPI.getTicketBacklog().getTicket4Nr(ticketNr);
      }
      return null;
   }

   private static TicketActivity map2TicketActivity(BusinessDayIncrementEntity businessDayIncrementEntity) {
      Integer serviceCode = businessDayIncrementEntity.getServiceCode();
      if (nonNull(serviceCode)) {
         return TicketBacklogSPI.getTicketBacklog().getTicketActivity4ServiceCode(serviceCode);
      }
      throw new IllegalStateException("A BusinessDayIncrementEntity must have a service code set!");
   }
}
