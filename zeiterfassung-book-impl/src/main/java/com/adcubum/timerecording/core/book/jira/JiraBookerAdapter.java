package com.adcubum.timerecording.core.book.jira;

import com.adcubum.timerecording.core.book.common.CommonBookerAdapter;
import com.adcubum.timerecording.core.book.result.BookerResult;
import com.adcubum.timerecording.core.work.businessday.BusinessDay;
import com.adcubum.timerecording.core.work.businessday.BusinessDayIncrement;
import com.adcubum.timerecording.jira.jiraapi.configuration.JiraApiConfiguration;
import com.adcubum.timerecording.jira.jiraapi.configuration.JiraApiConfigurationProvider;
import com.adcubum.timerecording.jira.jiraapi.postrequest.post.data.JiraPostResponse;
import com.adcubum.timerecording.jira.jiraapi.postrequest.post.worklog.JiraApiWorklogCreator;
import com.adcubum.timerecording.jira.jiraapi.postrequest.post.worklog.data.Worklog;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Is responsible for booking the given {@link BusinessDay}.
 *
 * @author Dominic
 */
public class JiraBookerAdapter extends CommonBookerAdapter<JiraServiceCodeAdapter> {

   private Supplier<JiraApiConfiguration> jiraApiConfigurationSupplier;
   private JiraApiWorklogCreatorFactory jiraApiWorklogCreatorFactory;

   public JiraBookerAdapter() {
      this(new JiraServiceCodeAdapter(),
              JiraApiConfigurationProvider.INSTANCE::getJiraApiConfiguration,
              com.adcubum.timerecording.jira.jiraapi.postrequest.post.worklog.JiraApiWorklogCreatorFactory::createNew);
   }

   /**
    * Constructor for testing purpose only
    *
    * @param jiraServiceCodeAdapter        the {@link JiraServiceCodeAdapter}
    * @param jiraApiConfigurationSupplier  the {@link Supplier} for a {@link JiraApiConfiguration}
    * @param jiraApiWorklogCreatorFactory  a {@link JiraApiWorklogCreatorFactory}
    */
   JiraBookerAdapter(JiraServiceCodeAdapter jiraServiceCodeAdapter, Supplier<JiraApiConfiguration>
           jiraApiConfigurationSupplier, JiraApiWorklogCreatorFactory jiraApiWorklogCreatorFactory) {
      super(JiraServiceCodeAdapter.class);
      this.serviceCodeAdapter = jiraServiceCodeAdapter;
      this.jiraApiConfigurationSupplier = jiraApiConfigurationSupplier;
      this.jiraApiWorklogCreatorFactory = jiraApiWorklogCreatorFactory;
   }

   /**
    * Does the actual booking directly via jira website
    *
    * @param businessDay the {@link BusinessDay} to book
    * @return a {@link BookerResult} which defines the booked {@link BusinessDayIncrement}s and error messages, if exists
    */
   @Override
   public BookerResult book(BusinessDay businessDay) {
      JiraApiWorklogCreator jiraApiWorklogCreator = jiraApiWorklogCreatorFactory.createNew(jiraApiConfigurationSupplier.get(), username, userPwdSupplier);
      List<BusinessDayIncrement> increments2Book = evalNotBookedIncrements(businessDay);
      BusinessDay bookedBusinessDay = businessDay;
      for (BusinessDayIncrement businessDayIncrement : increments2Book) {
         if (businessDayIncrement.isBookable()) {
            bookedBusinessDay = bookBusinessDayIncrement(jiraApiWorklogCreator, bookedBusinessDay, businessDayIncrement);
         }
      }
      return createAndReturnBookResult(bookedBusinessDay, businessDay);
   }

   private static BusinessDay bookBusinessDayIncrement(JiraApiWorklogCreator jiraApiWorklogCreator, BusinessDay businessDay, BusinessDayIncrement businessDayIncrement) {
      Worklog worklog = JiraWorklogImpl.of(businessDayIncrement, businessDay.getDateTime());
      JiraPostResponse postResponse = jiraApiWorklogCreator.createWorklog(worklog);
      if (postResponse.isSuccessful()) {
         return businessDay.flagBusinessDayIncrementAsBooked(businessDayIncrement.getId());
      }
      return businessDay;
   }

   private static List<BusinessDayIncrement> evalNotBookedIncrements(BusinessDay businessDay) {
      return businessDay.getIncrements()
              .stream()
              .filter(businessDayIncrement -> !businessDayIncrement.isBooked())
              .collect(Collectors.toList());
   }
}
