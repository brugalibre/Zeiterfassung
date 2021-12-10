package com.adcubum.timerecording.core.book.servicecode;

/**
 * The {@link ServiceCodeDto} combines both, the name and the unique code of a service-code
 * @author dstalder
 */
public interface ServiceCodeDto {

    /**
     * Return the description of this {@link ServiceCodeDto}. Note that the description may contain
     * both, its name and code for the representation
     * @return the description of this {@link ServiceCodeDto}
     */
    String getServiceCodeDescription();

    /**
     * @return the name of this {@link ServiceCodeDto}
     */
    String getServiceCodeName();

    /**
     * @return the unique code of this {@link ServiceCodeDto}
     */
    int getServiceCode();
}
