package com.panggilan.loket.dto;

public class TicketPrintSettingsResponse {

    private final String institutionName;
    private final String address;

    public TicketPrintSettingsResponse(String institutionName, String address) {
        this.institutionName = institutionName;
        this.address = address;
    }

    public String getInstitutionName() {
        return institutionName;
    }

    public String getAddress() {
        return address;
    }
}
