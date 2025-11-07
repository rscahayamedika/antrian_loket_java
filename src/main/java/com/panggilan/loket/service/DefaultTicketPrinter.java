package com.panggilan.loket.service;

import com.panggilan.loket.config.TicketPrintProperties;
import com.panggilan.loket.model.Ticket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class DefaultTicketPrinter implements TicketPrinter {

    private static final Logger log = LoggerFactory.getLogger(DefaultTicketPrinter.class);

    private final TicketPrintProperties properties;
    private final ExecutorService executor = Executors.newSingleThreadExecutor(r -> {
        Thread thread = new Thread(r, "ticket-print-worker");
        thread.setDaemon(true);
        return thread;
    });

    public DefaultTicketPrinter(TicketPrintProperties properties) {
        this.properties = properties;
    }

    @Override
    public void printTicket(Ticket ticket) {
        if (!properties.isEnabled()) {
            return;
        }
        if (ticket == null) {
            return;
        }
        executor.execute(() -> {
            try {
                doPrint(ticket);
            } catch (Exception ex) {
                log.error("Gagal mencetak tiket {}", ticket.getNumber(), ex);
            }
        });
    }

    @PreDestroy
    void shutdown() {
        executor.shutdownNow();
    }

    private void doPrint(Ticket ticket) {
        if (GraphicsEnvironment.isHeadless()) {
            log.error("Lingkungan Java berjalan dalam mode headless, cetak tiket {} dibatalkan. Pastikan -Djava.awt.headless=false.", ticket.getNumber());
            return;
        }
        PrintService printService = PrintServiceLookup.lookupDefaultPrintService();
        if (printService == null) {
            log.warn("Tidak ada printer default yang terdeteksi. Cetak tiket {} dilewati.", ticket.getNumber());
            return;
        }
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setJobName("Tiket " + ticket.getNumber());
        try {
            job.setPrintService(printService);
        } catch (PrinterException ex) {
            log.error("Gagal mengikat printer default untuk tiket {}", ticket.getNumber(), ex);
            return;
        }
        job.setPrintable(new TicketPrintable(ticket));
        try {
            job.print();
        } catch (PrinterException ex) {
            log.error("Gagal mencetak tiket {}", ticket.getNumber(), ex);
        }
    }

    private final class TicketPrintable implements Printable {

        private final Ticket ticket;

        private TicketPrintable(Ticket ticket) {
            this.ticket = ticket;
        }

        @Override
        public int print(java.awt.Graphics graphics, PageFormat pageFormat, int pageIndex) {
            if (pageIndex > 0) {
                return NO_SUCH_PAGE;
            }
            Graphics2D g2 = (Graphics2D) graphics;
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2.setColor(Color.BLACK);
            g2.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

            double width = pageFormat.getImageableWidth();
            double height = pageFormat.getImageableHeight();

            int yTop = 10;
            yTop = drawCenteredLines(g2, new Font("SansSerif", Font.BOLD, properties.getHeaderFontSize()),
                    safeValue(properties.getInstitutionName(), "Instansi Anda"), width, yTop);

            drawCenteredTicketNumber(g2, new Font("SansSerif", Font.BOLD, properties.getTicketFontSize()),
                    ticket.getNumber(), width, height);

            drawFooter(g2, new Font("SansSerif", Font.PLAIN, properties.getFooterFontSize()),
                    safeValue(properties.getAddress(), "Alamat Instansi"), width, height);

            return PAGE_EXISTS;
        }

        private int drawCenteredLines(Graphics2D g2, Font font, String text, double width, int startY) {
            if (text == null || text.isBlank()) {
                return startY;
            }
            g2.setFont(font);
            FontMetrics metrics = g2.getFontMetrics(font);
            List<String> lines = Arrays.asList(text.split("\\R"));
            int y = startY;
            for (String raw : lines) {
                String line = raw.trim();
                if (line.isEmpty()) {
                    y += metrics.getHeight();
                    continue;
                }
                int lineWidth = metrics.stringWidth(line);
                int x = (int) Math.max((width - lineWidth) / 2, 0);
                g2.drawString(line, x, y + metrics.getAscent());
                y += metrics.getHeight();
            }
            return y;
        }

        private void drawCenteredTicketNumber(Graphics2D g2, Font font, String text, double width, double height) {
            if (text == null || text.isBlank()) {
                return;
            }
            g2.setFont(font);
            FontMetrics metrics = g2.getFontMetrics(font);
            int lineWidth = metrics.stringWidth(text);
            int x = (int) Math.max((width - lineWidth) / 2, 0);
            int y = (int) ((height - metrics.getHeight()) / 2) + metrics.getAscent();
            g2.drawString(text, x, y);
        }

        private void drawFooter(Graphics2D g2, Font font, String text, double width, double height) {
            if (text == null || text.isBlank()) {
                return;
            }
            g2.setFont(font);
            FontMetrics metrics = g2.getFontMetrics(font);
            List<String> lines = Arrays.asList(text.split("\\R"));
            int totalHeight = lines.size() * metrics.getHeight();
            int startY = (int) (height - totalHeight);
            int y = startY;
            for (String raw : lines) {
                String line = raw.trim();
                if (line.isEmpty()) {
                    y += metrics.getHeight();
                    continue;
                }
                int lineWidth = metrics.stringWidth(line);
                int x = (int) Math.max((width - lineWidth) / 2, 0);
                g2.drawString(line, x, y + metrics.getAscent());
                y += metrics.getHeight();
            }
        }

        private String safeValue(String value, String fallback) {
            return value == null || value.isBlank() ? fallback : value;
        }
    }
}
