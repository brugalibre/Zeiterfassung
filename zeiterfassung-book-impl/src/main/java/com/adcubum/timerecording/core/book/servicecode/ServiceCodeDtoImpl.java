package com.adcubum.timerecording.core.book.servicecode;


import static java.util.Objects.requireNonNull;

public class ServiceCodeDtoImpl implements ServiceCodeDto {

    private String description;
    private int code;

    /**
     * Creates a new {@link ServiceCodeDtoImpl} for the given code and description
     *
     * @param code        the code value
     * @param description the description value
     */
    public ServiceCodeDtoImpl(Integer code, String description) {
        this.code = requireNonNull(code);
        this.description = requireNonNull(description);
    }

    @Override
    public String getServiceCodeDescription() {
        return code + " - " + description;
    }

    @Override
    public String getServiceCodeName() {
        return description;
    }

    @Override
    public int getServiceCode() {
        return code;
    }
}
