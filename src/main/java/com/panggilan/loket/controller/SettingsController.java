package com.panggilan.loket.controller;

import com.panggilan.loket.dto.TicketPrintSettingsResponse;
import com.panggilan.loket.dto.UpdateTicketPrintSettingsRequest;
import com.panggilan.loket.service.TicketSettingsService;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/settings")
public class SettingsController {

    private final TicketSettingsService ticketSettingsService;

    public SettingsController(TicketSettingsService ticketSettingsService) {
        this.ticketSettingsService = ticketSettingsService;
    }

    @GetMapping("/ticket-print")
    public TicketPrintSettingsResponse getTicketPrintSettings() {
        return new TicketPrintSettingsResponse(
                ticketSettingsService.getInstitutionName(),
                ticketSettingsService.getAddress()
        );
    }

    @PutMapping("/ticket-print")
    public ResponseEntity<TicketPrintSettingsResponse> updateTicketPrintSettings(
            @Valid @RequestBody UpdateTicketPrintSettingsRequest request) {
        ticketSettingsService.updateSettings(request.getInstitutionName(), request.getAddress());
        return ResponseEntity.ok(new TicketPrintSettingsResponse(
                ticketSettingsService.getInstitutionName(),
                ticketSettingsService.getAddress()
        ));
    }
}
