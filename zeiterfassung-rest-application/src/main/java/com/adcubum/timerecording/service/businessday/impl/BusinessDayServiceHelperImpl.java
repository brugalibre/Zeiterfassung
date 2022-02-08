package com.adcubum.timerecording.service.businessday.impl;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import com.adcubum.timerecording.data.ticket.ticketactivity.factor.TicketActivityFactory;
import com.adcubum.timerecording.jira.data.ticket.TicketActivity;
import com.adcubum.timerecording.model.businessday.ticketdistribution.TicketDistributionDto;
import com.adcubum.timerecording.model.ticketbacklog.ServiceCodeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.adcubum.timerecording.app.TimeRecorder;
import com.adcubum.timerecording.core.businessday.repository.factory.BusinessDayEntityRepositoryHolder;
import com.adcubum.timerecording.core.work.businessday.TimeSnippet;
import com.adcubum.timerecording.core.work.businessday.TimeSnippetFactory;
import com.adcubum.timerecording.core.work.businessday.ValueTypes;
import com.adcubum.timerecording.core.work.businessday.history.BusinessDayHistoryOverview;
import com.adcubum.timerecording.core.work.businessday.update.callback.BusinessDayChangedCallbackHandler;
import com.adcubum.timerecording.core.work.businessday.update.callback.impl.BusinessDayChangedCallbackHandlerFactory;
import com.adcubum.timerecording.core.work.businessday.update.callback.impl.BusinessDayIncrementAdd;
import com.adcubum.timerecording.core.work.businessday.update.callback.impl.BusinessDayIncrementAdd.BusinessDayIncrementAddBuilder;
import com.adcubum.timerecording.core.work.businessday.update.callback.impl.ChangedValue;
import com.adcubum.timerecording.jira.data.ticket.Ticket;
import com.adcubum.timerecording.model.businessday.BusinessDayDto;
import com.adcubum.timerecording.model.businessday.BusinessDayIncrementDto;
import com.adcubum.timerecording.model.businessday.TimeSnippetDto;
import com.adcubum.timerecording.model.businessday.history.BusinessDayHistoryOverviewDto;
import com.adcubum.timerecording.service.businessday.BusinessDayServiceHelper;
import com.adcubum.timerecording.service.exception.BeginOrEndTimeParseException;
import com.adcubum.timerecording.service.exception.NoPersistentBusinessDayIncrementToChangeFoundException;
import com.adcubum.timerecording.ticketbacklog.TicketBacklogSPI;
import com.adcubum.timerecording.work.date.DateTime;
import com.adcubum.timerecording.work.date.DateTimeUtil;

@Component("BusinessDayServiceHelper")
@DependsOn(BusinessDayEntityRepositoryHolder.BUSINESS_DAY_ENTITY_REPOSITORY_FACTORY)
public class BusinessDayServiceHelperImpl implements BusinessDayServiceHelper {

   private BusinessDayChangedCallbackHandler businessDayChangedCallbackHandler;

   @Autowired
   public BusinessDayServiceHelperImpl() {
      this.businessDayChangedCallbackHandler = BusinessDayChangedCallbackHandlerFactory.createNew();
   }

   @Override
   public int deleteBusinessDayIncrementDto(UUID id) {
      businessDayChangedCallbackHandler.handleBusinessDayIncrementDeleted(id);
      return HttpStatus.OK.value();
   }

   @Override
   public BusinessDayIncrementDto addBusinessDayIncrement(BusinessDayIncrementDto businessDayIncrementDto) {
      BusinessDayIncrementAdd businessDayIncrementAdd = createBusinessDayIncrementAddOf(businessDayIncrementDto);
      businessDayChangedCallbackHandler.handleBusinessDayIncrementAdd(businessDayIncrementAdd);
      return businessDayIncrementDto;//TODO resolve exactly this one! we need an id
   }

   private BusinessDayIncrementAdd createBusinessDayIncrementAddOf(@Valid BusinessDayIncrementDto businessDayIncrementDto) {
      ServiceCodeDto serviceCodeDto = businessDayIncrementDto.getServiceCodeDto();
      TicketActivity ticketActivity = TicketActivityFactory.INSTANCE.createNew(serviceCodeDto.getRepresentation(), serviceCodeDto.getValue());
      Ticket ticket = TicketBacklogSPI.getTicketBacklog().getTicket4Nr(businessDayIncrementDto.getTicketDto().getTicketNr());
      TimeSnippet timeSnippet = createTimeSnippet(businessDayIncrementDto);
      return new BusinessDayIncrementAddBuilder()
            .withAmountOfHours(businessDayIncrementDto.getTimeSnippetDto().getDurationRep())
            .withDescription(businessDayIncrementDto.getDescription())
            .withTicketActivity(ticketActivity)
            .withTicket(ticket)
            .withTimeSnippet(timeSnippet)
            .build();
   }

   private static TimeSnippet createTimeSnippet(BusinessDayIncrementDto businessDayIncrementDto) {
      TimeSnippetDto timeSnippetDto = businessDayIncrementDto.getTimeSnippetDto();
      try {
         return TimeSnippetFactory.createNew(new Date(), // this should be fine, since any user should not be able to add new from any other day than today 
               timeSnippetDto.getBeginTimeStampRepresentation(),
               timeSnippetDto.getEndTimeStampRepresentation());
      } catch (ParseException e) {
         throw new BeginOrEndTimeParseException(e);
      }
   }

   @Override
   public BusinessDayIncrementDto changeBusinessDayIncrementDto(BusinessDayIncrementDto changedBusinessDayIncrementDto) {
      List<ValueTypes> changedValues = evalChangedValues(changedBusinessDayIncrementDto);
      for (ValueTypes changedValueType : changedValues) {
         ChangedValue changeValue = createChangedValueFromBDIncDto(changedBusinessDayIncrementDto, changedValueType);
         businessDayChangedCallbackHandler.handleBusinessDayChanged(changeValue);
      }
      return evalPersistentBusinessDayIncrementDto(changedBusinessDayIncrementDto.getId());
   }

   private List<ValueTypes> evalChangedValues(BusinessDayIncrementDto changedBusinessDayIncrementDto) {
      BusinessDayIncrementDto persistentBusinessDayIncrementDto =
            evalPersistentBusinessDayIncrementDto(changedBusinessDayIncrementDto.getId());
      return persistentBusinessDayIncrementDto.compareAndEvalChangedValues(changedBusinessDayIncrementDto);
   }

   private BusinessDayIncrementDto evalPersistentBusinessDayIncrementDto(UUID id) {
      return getBusinessDayDto().getBusinessDayIncrementDtos()
            .stream()
            .filter(businessDayIncDto -> businessDayIncDto.getId().equals(id))
            .findFirst()
            .orElseThrow(() -> new NoPersistentBusinessDayIncrementToChangeFoundException(
                  "No persistent business-day-increment found for id '" + id + "'!"));
   }

   private ChangedValue createChangedValueFromBDIncDto(BusinessDayIncrementDto changedBusinessDayIncrementDto, ValueTypes changedValueType) {
      Object changedValue = changedBusinessDayIncrementDto.getChangedValue(changedValueType);
      return ChangedValue.of(changedBusinessDayIncrementDto.getId(), changedValue, changedValueType);
   }

   @Override
   public int deleteAllBusinessDayIncrements() {
      businessDayChangedCallbackHandler.clear();
      return HttpStatus.OK.value();
   }

   @Override
   public BusinessDayHistoryOverviewDto getBusinessDayHistoryDto() {
      DateTime firstDayOfCurrentMonth = DateTimeUtil.getFirstDayOfCurrentMonth();
      DateTime lastDayOfCurrentMonth = DateTimeUtil.getLastDayOfCurrentMonth();
      BusinessDayHistoryOverview businessDayHistoryOverview =
            TimeRecorder.INSTANCE.getBusinessDayHistoryOverview(firstDayOfCurrentMonth, lastDayOfCurrentMonth);
      return BusinessDayHistoryOverviewDto.of(businessDayHistoryOverview);
   }

   @Override
   public BusinessDayDto getBusinessDayDto() {
      return BusinessDayDto.of(TimeRecorder.INSTANCE.getBusinessDay());
   }
}
