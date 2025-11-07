package com.panggilan.loket.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "printer.ticket")
public class TicketPrintProperties {

    private boolean enabled = true;
    private String institutionName = "Instansi Anda";
    private String address = "Alamat Instansi";
    private int headerFontSize = 16;
    private int ticketFontSize = 48;
    private int footerFontSize = 12;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getInstitutionName() {
        return institutionName;
    }

    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getHeaderFontSize() {
        return headerFontSize;
    }

    public void setHeaderFontSize(int headerFontSize) {
        this.headerFontSize = headerFontSize;
    }

    public int getTicketFontSize() {
        return ticketFontSize;
    }

    public void setTicketFontSize(int ticketFontSize) {
        this.ticketFontSize = ticketFontSize;
    }

    public int getFooterFontSize() {
        return footerFontSize;
    }

    public void setFooterFontSize(int footerFontSize) {
        this.footerFontSize = footerFontSize;
    }
}
