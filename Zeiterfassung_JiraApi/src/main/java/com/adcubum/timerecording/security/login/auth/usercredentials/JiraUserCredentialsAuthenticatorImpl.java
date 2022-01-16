package com.adcubum.timerecording.security.login.auth.usercredentials;


import com.adcubum.timerecording.jira.data.ticket.Ticket;
import com.adcubum.timerecording.jira.jiraapi.configuration.JiraApiConfigurationProvider;
import com.adcubum.timerecording.jira.jiraapi.readresponse.read.JiraApiReader;
import com.adcubum.timerecording.jira.jiraapi.readresponse.read.JiraApiReaderBuilder;
import com.adcubum.timerecording.security.login.auth.AuthenticationContext;
import com.adcubum.timerecording.settings.Settings;
import com.adcubum.timerecording.settings.key.ValueKey;
import com.adcubum.timerecording.settings.key.ValueKeyFactory;
import com.adcubum.util.utils.StringUtil;

@Authenticator(authenticatorNames = {"JIRA_API", "ADC_JIRA_WEB"})
public class JiraUserCredentialsAuthenticatorImpl implements UserCredentialsAuthenticator {

   private String ticketNr2TestConnection;
   private JiraApiReader jiraApiReader;

   public JiraUserCredentialsAuthenticatorImpl() {
      this(JiraApiReaderBuilder.of()
            .withJiraApiConfiguration(JiraApiConfigurationProvider.INSTANCE.getJiraApiConfiguration())
            .build(), getTicketNr2TestConnection());
   }

   JiraUserCredentialsAuthenticatorImpl(JiraApiReader jiraApiReader, String ticketNr2TestConnection) {
      this.ticketNr2TestConnection = ticketNr2TestConnection;
      this.jiraApiReader = jiraApiReader;
   }

   @Override
   public boolean doUserAuthentication(String username, char[] userPwd) {
      jiraApiReader.userAuthenticated(new AuthenticationContext(username, () -> userPwd));
      return jiraApiReader.readTicket4Nr(ticketNr2TestConnection)
            .map(Ticket::isDummyTicket)
            .map(isDummyTicket -> !isDummyTicket)
            .orElse(false);
   }

   private static String getTicketNr2TestConnection() {
      ValueKey<String> key = ValueKeyFactory.createNew("TicketNr2TestConnection", String.class);
      String ticketNr2TestConnectionFromSettings = Settings.INSTANCE.getSettingsValue(key);
      return StringUtil.isEmptyOrNull(ticketNr2TestConnectionFromSettings) ? "INTA-147" : ticketNr2TestConnectionFromSettings;
   }
}
