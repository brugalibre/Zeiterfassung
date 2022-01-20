package com.adcubum.timerecording.messaging.api.receive;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Marker annotation for all {@link BookBusinessDayMessageReceiver}
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface MessageReceiver {
}
