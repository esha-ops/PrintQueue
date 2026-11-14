package com.printqueue.model;

public class Printer {
    private final String printerId;
    private final String name;
    private String status;
    private int paperLevel;
    private int inkLevel;
    private String currentJobId;

    public Printer(String printerId, String name) {
        this.printerId = printerId;
        this.name = name;
        this.status = "Available";
        this.paperLevel = 100;
        this.inkLevel = 100;
        this.currentJobId = "None";
    }

    public String getPrinterId() { return printerId; }
    public String getName() { return name; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public int getPaperLevel() { return paperLevel; }
    public void setPaperLevel(int paperLevel) { this.paperLevel = paperLevel; }
    public int getInkLevel() { return inkLevel; }
    public void setInkLevel(int inkLevel) { this.inkLevel = inkLevel; }
    public String getCurrentJobId() { return currentJobId; }
    public void setCurrentJobId(String currentJobId) { this.currentJobId = currentJobId; }
}
