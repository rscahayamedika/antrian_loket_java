package com.panggilan.loket.service;

import com.panggilan.loket.config.TicketPrintProperties;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class TicketSettingsService {

    private final TicketPrintProperties properties;

    public TicketSettingsService(TicketPrintProperties properties) {
        this.properties = properties;
    }

    public synchronized String getInstitutionName() {
        return properties.getInstitutionName();
    }

    public synchronized String getAddress() {
        return properties.getAddress();
    }

    public synchronized void updateSettings(String institutionName, String address) {
        properties.setInstitutionName(normalize(institutionName));
        properties.setAddress(normalize(address));
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim();
    }
}
