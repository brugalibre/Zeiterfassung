package com.adcubum.timerecording.application;

import org.springframework.boot.web.servlet.context.ServletWebServerInitializedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import com.adcubum.timerecording.settings.Settings;
import com.adcubum.timerecording.ui.app.web.WebUiHelper;

@Service
public class ServerPortService {

   @EventListener
   public void onApplicationEvent(final ServletWebServerInitializedEvent event) {
      int port = event.getWebServer().getPort();
      Settings.INSTANCE.saveValueToProperties(WebUiHelper.SERVER_PORT_KEY, String.valueOf(port));
   }
}
