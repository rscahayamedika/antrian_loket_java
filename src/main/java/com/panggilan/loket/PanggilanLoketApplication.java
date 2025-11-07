package com.panggilan.loket;

import com.panggilan.loket.config.CounterProperties;
import com.panggilan.loket.config.TicketPrintProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({CounterProperties.class, TicketPrintProperties.class})
public class PanggilanLoketApplication {

    public static void main(String[] args) {
        System.setProperty("java.awt.headless", "false");
        SpringApplication.run(PanggilanLoketApplication.class, args);
    }
}
