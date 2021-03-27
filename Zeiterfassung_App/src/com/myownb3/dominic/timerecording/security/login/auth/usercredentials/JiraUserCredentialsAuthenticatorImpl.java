package com.myownb3.dominic.timerecording.security.login.auth.usercredentials;

import com.adcubum.timerecording.jira.data.Ticket;
import com.adcubum.timerecording.jira.jiraapi.readresponse.read.JiraApiReader;
import com.adcubum.timerecording.security.login.auth.AuthenticationContext;
import com.adcubum.timerecording.security.login.auth.usercredentials.UserCredentialsAuthenticator;
import com.myownb3.dominic.timerecording.settings.Settings;
import com.myownb3.dominic.util.utils.StringUtil;

public class JiraUserCredentialsAuthenticatorImpl implements UserCredentialsAuthenticator {

   private String ticketNr2TestConnection;
   private JiraApiReader jiraApiReader;

   public JiraUserCredentialsAuthenticatorImpl() {
      this(JiraApiReader.INSTANCE, getTicketNr2TestConnection());
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
      String ticketNr2TestConnectionFromSettings = Settings.INSTANCE.getSettingsValue("TicketNr2TestConnection");
      return StringUtil.isEmptyOrNull(ticketNr2TestConnectionFromSettings) ? "INTA-147" : ticketNr2TestConnectionFromSettings;
   }
}
