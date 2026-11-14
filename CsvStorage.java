package com.printqueue.storage;

import com.printqueue.model.PrintJob;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CsvStorage {
    private static final String FILE_PATH = "data/jobs.csv";

    public void saveJobs(List<PrintJob> jobs) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            writer.write("jobId,token,customerName,phoneNumber,serviceType,paperSize,pages,copies,printMode,estimatedCost,estimatedMinutes,status,submittedTime");
            writer.newLine();

            for (PrintJob job : jobs) {
                writer.write(String.join(",",
                        job.getJobId(), job.getToken(), job.getCustomerName(),
                        job.getPhoneNumber(), job.getServiceType(), job.getPaperSize(),
                        String.valueOf(job.getPages()), String.valueOf(job.getCopies()),
                        job.getPrintMode(), String.valueOf(job.getEstimatedCost()),
                        String.valueOf(job.getEstimatedMinutes()), job.getStatus(),
                        job.getSubmittedTime()));
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Could not save jobs: " + e.getMessage());
        }
    }
}
