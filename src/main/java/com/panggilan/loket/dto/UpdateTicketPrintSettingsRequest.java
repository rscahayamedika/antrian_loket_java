package com.panggilan.loket.dto;

import javax.validation.constraints.NotBlank;

public class UpdateTicketPrintSettingsRequest {

    @NotBlank(message = "Nama instansi wajib diisi")
    private String institutionName;

    @NotBlank(message = "Alamat instansi wajib diisi")
    private String address;

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
}
