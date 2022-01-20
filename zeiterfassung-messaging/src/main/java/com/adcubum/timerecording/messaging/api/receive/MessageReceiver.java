package com.adcubum.timerecording.messaging.api.receive;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Marker annotation which has to be annotated by all {@link BookBusinessDayMessageReceiver} so that they can be found
 * using the {@link com.adcubum.timerecording.messaging.util.ReflectionUtil}
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface MessageReceiver {
}
