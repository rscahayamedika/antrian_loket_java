package com.panggilan.loket.service;

import com.panggilan.loket.model.Ticket;

@FunctionalInterface
public interface TicketPrinter {

    void printTicket(Ticket ticket);

    static TicketPrinter noop() {
        return ticket -> {
        };
    }
}
