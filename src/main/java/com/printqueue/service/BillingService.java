
package com.printqueue.service;

public class BillingService {

    public double calculateCost(
            String serviceType,
            String paperSize,
            int pages,
            int copies,
            String printMode) {

        double rate = switch (serviceType) {
            case "Color Printout" -> 8.0;
            case "Photocopy" -> 2.0;
            case "Scanning" -> 3.0;
            case "Spiral Binding" -> 40.0;
            default -> 2.0;
        };

        if ("A3".equals(paperSize)) {
            rate *= 2;
        }

        int chargeablePages = pages;

        // Double-sided printing uses two pages per sheet
        if ("Double-sided".equals(printMode)
                && (serviceType.equals("Black & White Printout")
                || serviceType.equals("Color Printout")
                || serviceType.equals("Photocopy"))) {

            chargeablePages = (pages + 1) / 2;
        }

        return chargeablePages * copies * rate;
    }
}
