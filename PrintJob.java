
package com.printqueue.model;

import java.io.File;

public class PrintJob {
    private final String jobId;
    private final String token;
    private final String customerName;
    private final String phoneNumber;
    private final String serviceType;
    private final String paperSize;
    private final int pages;
    private final int copies;
    private final String printMode;
    private final double estimatedCost;
    private final int estimatedMinutes;
    private final String submittedTime;
    private final String documentPath;

    private String status;

    public PrintJob(String jobId, String token, String customerName,
                    String phoneNumber, String serviceType, String paperSize,
                    int pages, int copies, String printMode,
                    double estimatedCost, int estimatedMinutes,
                    String submittedTime, String documentPath) {

        this.jobId = jobId;
        this.token = token;
        this.customerName = customerName;
        this.phoneNumber = phoneNumber;
        this.serviceType = serviceType;
        this.paperSize = paperSize;
        this.pages = pages;
        this.copies = copies;
        this.printMode = printMode;
        this.estimatedCost = estimatedCost;
        this.estimatedMinutes = estimatedMinutes;
        this.submittedTime = submittedTime;
        this.documentPath = documentPath;

        this.status = "Queued";
    }

    public String getJobId() {
        return jobId;
    }

    public String getToken() {
        return token;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getServiceType() {
        return serviceType;
    }

    public String getPaperSize() {
        return paperSize;
    }

    public int getPages() {
        return pages;
    }

    public int getCopies() {
        return copies;
    }

    public String getPrintMode() {
        return printMode;
    }

    public double getEstimatedCost() {
        return estimatedCost;
    }

    public int getEstimatedMinutes() {
        return estimatedMinutes;
    }

    public String getSubmittedTime() {
        return submittedTime;
    }

    public String getDocumentPath() {
        return documentPath;
    }

    public String getDocumentName() {
        return new File(documentPath).getName();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object[] toTableRow() {
        return new Object[]{
                token,
                customerName,
                phoneNumber,
                serviceType,
                pages,
                copies,
                paperSize,
                printMode,
                estimatedCost,
                estimatedMinutes + " min",
                getDocumentName(),
                status
        };
    }
}
