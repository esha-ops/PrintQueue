
package com.printqueue.service;

import com.printqueue.model.PrintJob;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PrintQueueService {

    private final Deque<PrintJob> jobQueue = new ArrayDeque<>();

    private final Map<String, PrintJob> allJobs = new LinkedHashMap<>();

    private final DateTimeFormatter timeFormatter =
            DateTimeFormatter.ofPattern("hh:mm a");

    private int jobCounter = 0;

    public PrintJob submitJob(String customerName, String phoneNumber,
                              String serviceType, String paperSize,
                              int pages, int copies, String printMode,
                              double estimatedCost, String documentPath) {

        jobCounter++;

        String jobId = "JOB-" + String.format("%03d", jobCounter);
        String token = "PRT-" + String.format("%03d", jobCounter);

        String submittedTime = LocalTime.now().format(timeFormatter);

        int estimatedMinutes = Math.max(2, (pages * copies) / 10);

        PrintJob job = new PrintJob(
                jobId,
                token,
                customerName,
                phoneNumber,
                serviceType,
                paperSize,
                pages,
                copies,
                printMode,
                estimatedCost,
                estimatedMinutes,
                submittedTime,
                documentPath
        );

        jobQueue.add(job);
        allJobs.put(token, job);

        return job;
    }

    public PrintJob getJobByToken(String token) {
        if (token == null) {
            return null;
        }

        return allJobs.get(token.trim().toUpperCase());
    }

    public int getQueuePosition(PrintJob job) {
        int position = 1;

        for (PrintJob current : jobQueue) {
            if (current.getJobId().equals(job.getJobId())) {
                return position;
            }

            position++;
        }

        return -1;
    }

    public PrintJob getCurrentJob() {
        return jobQueue.peek();
    }

    public PrintJob startCurrentJob() {
        PrintJob job = getCurrentJob();

        if (job != null) {
            job.setStatus("Printing");
        }

        return job;
    }

    public PrintJob markCurrentJobReady() {
        PrintJob job = getCurrentJob();

        if (job != null) {
            job.setStatus("Ready for Collection");
        }

        return job;
    }

    public PrintJob completeCurrentJob() {
        PrintJob job = getCurrentJob();

        if (job != null) {
            job.setStatus("Completed");
            jobQueue.poll();
        }

        return job;
    }

    public PrintJob cancelCurrentJob() {
        PrintJob job = getCurrentJob();

        if (job != null) {
            job.setStatus("Cancelled");
            jobQueue.poll();
        }

        return job;
    }

    public List<PrintJob> getActiveJobs() {
        return new ArrayList<>(jobQueue);
    }

    public boolean hasJobs() {
        return !jobQueue.isEmpty();
    }
}
