package com.adcubum.timerecording.service.timerecorder;

import static java.util.Objects.isNull;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.adcubum.timerecording.app.TimeRecorder;
import com.adcubum.timerecording.app.startstopresult.StartNotPossibleInfo;
import com.adcubum.timerecording.app.startstopresult.UserInteractionResult;
import com.adcubum.timerecording.core.work.businessday.BusinessDay;
import com.adcubum.timerecording.core.work.businessday.BusinessDayIncrement;
import com.adcubum.timerecording.jira.constants.TicketConst;
import com.adcubum.timerecording.message.Message;
import com.adcubum.timerecording.model.businessday.AddNewBusinessDayIncrementDto;
import com.adcubum.timerecording.model.businessday.BusinessDayIncrementDto;
import com.adcubum.timerecording.model.timerecorder.TimeRecorderDto;
import com.adcubum.timerecording.ticketbacklog.TicketBacklogSPI;

@Service
public class TimeRecorderService {

   private TimeRecorder timeRecorder;

   public TimeRecorderService() {
      this.timeRecorder = TimeRecorder.INSTANCE;
   }

   /**
    * Books the content of the current {@link BusinessDay}
    * 
    * @see TimeRecorder#book()
    */
   public int book() {
      timeRecorder.book();
      return HttpStatus.OK.value();
   }

   /**
    * Aborts the current recording and resumes the current {@link BusinessDayIncrement}
    * 
    * @return a integer representing the http status
    */
   public int resume() {
      timeRecorder.resume();
      return HttpStatus.OK.value();
   }

   /**
    * Starts or stops the recording and returns a {@link AddNewBusinessDayIncrementDto} which represents
    * a {@link BusinessDayIncrementDto} to add.
    * The {@link AddNewBusinessDayIncrementDto} is used on the client side to control the process of adding a new
    * {@link BusinessDayIncrement}
    * 
    * @throws a
    *         StartRecordingNotPossibleException if a start was not possible
    * 
    * @return a {@link AddNewBusinessDayIncrementDto}
    */
   public AddNewBusinessDayIncrementDto startStopRecording() {
      UserInteractionResult userInteractionResult = timeRecorder.handleUserInteraction(false);
      checkAndThrowStartRecordingNotPossibleException(userInteractionResult);
      return buildAddNewBusinessDayIncrementDto(timeRecorder.getBussinessDay().getCurrentBussinessDayIncremental());
   }

   private static void checkAndThrowStartRecordingNotPossibleException(UserInteractionResult userInteractionResult) {
      Optional<StartNotPossibleInfo> optionalStartNotPossibleInfo = userInteractionResult.getOptionalStartNotPossibleInfo();
      if (optionalStartNotPossibleInfo.isPresent()) {
         Message message = optionalStartNotPossibleInfo.get().getMessage();
         throw new StartRecordingNotPossibleException(message);
      }
   }

   private static AddNewBusinessDayIncrementDto buildAddNewBusinessDayIncrementDto(BusinessDayIncrement currentBussinessDayIncremental) {
      if (isNull(currentBussinessDayIncremental.getTicket())) {
         currentBussinessDayIncremental.setTicket(TicketBacklogSPI.getTicketBacklog().getTicket4Nr(TicketConst.DEFAULT_TICKET_NAME));
      }
      BusinessDayIncrementDto businessDayIncrementDto = BusinessDayIncrementDto.of(currentBussinessDayIncremental);
      return new AddNewBusinessDayIncrementDto(businessDayIncrementDto);
   }

   /**
    * @return a new {@link TimeRecorderDto}
    */
   public TimeRecorderDto getTimeRecorderDto() {
      return new TimeRecorderDto(timeRecorder);
   }
}
