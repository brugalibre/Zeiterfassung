package com.adcubum.timerecording.core.book.jira;

import com.adcubum.timerecording.core.book.adapter.ServiceCodeAdapter;
import com.adcubum.timerecording.core.book.common.PropertyReader;
import com.adcubum.timerecording.core.book.servicecode.ServiceCodeDto;
import com.adcubum.timerecording.core.book.servicecode.ServiceCodeDtoImpl;
import com.adcubum.timerecording.settings.common.Const;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.adcubum.timerecording.settings.common.Const.PROLES_TICKET_ACTIVITIES_PROPERTIES;

public class JiraServiceCodeAdapter implements ServiceCodeAdapter {
    public static final String WHITE_SPACE_REPLACEMENT = "_";
    public static final String WHITE_SPACE = " ";
    private List<ServiceCodeDto> serviceCodeDtos;

    public JiraServiceCodeAdapter() {
        List<ServiceCodeDto> serviceCodeDtosTmp = new ArrayList<>();
        List<SimpleEntry<String, String>> allServiceCodePairs = new PropertyReader(PROLES_TICKET_ACTIVITIES_PROPERTIES).readAllKeyValuePairs();
        for (SimpleEntry<String, String> serviceCodePair : allServiceCodePairs) {
           if (Const.PROLES_PF_TICKET_NAME.equals(serviceCodePair.getKey())) {
              // a little hack since we share the same properties file for both - pf-activity-names and the proles-pf ticket name..
              continue;
           }
            String serviceCodeName = serviceCodePair.getKey()
                    .replace(WHITE_SPACE_REPLACEMENT, WHITE_SPACE);
            serviceCodeDtosTmp.add(new ServiceCodeDtoImpl(Integer.parseInt(serviceCodePair.getValue()), serviceCodeName));
        }
        this.serviceCodeDtos = serviceCodeDtosTmp.stream()
                .sorted(Comparator.comparing(ServiceCodeDto::getServiceCode))
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public List<ServiceCodeDto> fetchServiceCodesForProjectNr(long projectNr) {
        return serviceCodeDtos;
    }

    @Override
    public List<ServiceCodeDto> getAllServiceCodeDescriptions() {
        return serviceCodeDtos;
    }

    @Override
    public ServiceCodeDto getServiceCode4Code(Integer serviceCode) {
        return serviceCodeDtos
                .stream()
                .filter(serviceCodeDto -> serviceCodeDto.getServiceCode() == serviceCode)
                .findFirst()
                .orElse(new ServiceCodeDtoImpl(serviceCode, "unknown"));
    }
}