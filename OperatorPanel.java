
package com.printqueue.ui;

import com.printqueue.model.PrintJob;
import com.printqueue.service.PrintQueueService;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class OperatorPanel extends JPanel {
    private final PrintQueueService queueService;

    private final JProgressBar progressBar = new JProgressBar(0, 100);
    private final JTextArea currentJobArea = new JTextArea();

    private final JButton openDocumentButton =
            new JButton("Open Document");

    private PrintJob currentJob;

    public OperatorPanel(PrintQueueService queueService) {
        this.queueService = queueService;

        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        progressBar.setStringPainted(true);
        progressBar.setString("No active job");

        currentJobArea.setEditable(false);
        currentJobArea.setFont(new Font("Monospaced", Font.PLAIN, 14));

        add(createControls(), BorderLayout.NORTH);
        add(new JScrollPane(currentJobArea), BorderLayout.CENTER);
        add(createDocumentPanel(), BorderLayout.SOUTH);

        openDocumentButton.addActionListener(e -> openDocument());

        new javax.swing.Timer(2000, e ->
                refreshCurrentJob(queueService.getCurrentJob())).start();

        refreshCurrentJob(queueService.getCurrentJob());
    }

    private JPanel createControls() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 12, 12));
        panel.setBorder(BorderFactory.createTitledBorder("Operator Controls"));

        JButton start = new JButton("Start Current Job");
        JButton ready = new JButton("Mark Ready for Collection");
        JButton complete = new JButton("Mark Completed");
        JButton cancel = new JButton("Cancel Current Job");

        panel.add(start);
        panel.add(ready);
        panel.add(complete);
        panel.add(cancel);
        panel.add(new JLabel("Current Job Progress:"));
        panel.add(progressBar);

        start.addActionListener(e -> startJob());
        ready.addActionListener(e -> markReady());
        complete.addActionListener(e -> completeJob());
        cancel.addActionListener(e -> cancelJob());

        return panel;
    }

    private JPanel createDocumentPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        panel.setBorder(
                BorderFactory.createTitledBorder("Uploaded Document")
        );

        panel.add(openDocumentButton);
        openDocumentButton.setEnabled(false);

        return panel;
    }

    private void startJob() {
        PrintJob job = queueService.startCurrentJob();

        if (job == null) {
            showMessage("No jobs in the queue.");
            return;
        }

        refreshCurrentJob(job);
    }

    private void markReady() {
        PrintJob job = queueService.markCurrentJobReady();

        if (job == null) {
            showMessage("No jobs in the queue.");
            return;
        }

        refreshCurrentJob(job);
    }

    private void completeJob() {
        PrintJob job = queueService.completeCurrentJob();

        if (job == null) {
            showMessage("No jobs in the queue.");
            return;
        }

        refreshCurrentJob(queueService.getCurrentJob());

        JOptionPane.showMessageDialog(
                this,
                "Job " + job.getToken() + " completed successfully."
        );
    }

    private void cancelJob() {
        PrintJob job = queueService.cancelCurrentJob();

        if (job == null) {
            showMessage("No jobs in the queue.");
            return;
        }

        refreshCurrentJob(queueService.getCurrentJob());

        JOptionPane.showMessageDialog(
                this,
                "Job " + job.getToken() + " cancelled."
        );
    }

    public void refreshCurrentJob(PrintJob job) {
        currentJob = job;

        if (job == null) {
            currentJobArea.setText("No active print job.");
            openDocumentButton.setEnabled(false);

            progressBar.setValue(0);
            progressBar.setString("No active job");

            return;
        }

        String documentName = job.getDocumentName();

        currentJobArea.setText(
                "CURRENT PRINT JOB\n"
                        + "-------------------------------\n"
                        + "Token: " + job.getToken() + "\n"
                        + "Customer: " + job.getCustomerName() + "\n"
                        + "Phone: " + job.getPhoneNumber() + "\n"
                        + "Service: " + job.getServiceType() + "\n"
                        + "Paper: " + job.getPaperSize() + "\n"
                        + "Pages: " + job.getPages() + "\n"
                        + "Copies: " + job.getCopies() + "\n"
                        + "Mode: " + job.getPrintMode() + "\n"
                        + "Cost: Rs. "
                        + String.format("%.2f", job.getEstimatedCost()) + "\n"
                        + "Document: " + documentName + "\n"
                        + "Status: " + job.getStatus() + "\n"
        );

        File file = new File(job.getDocumentPath());

        openDocumentButton.setEnabled(file.isFile() && file.canRead());

        switch (job.getStatus()) {
            case "Queued":
                progressBar.setValue(0);
                progressBar.setString("Waiting in queue");
                break;

            case "Printing":
                progressBar.setValue(35);
                progressBar.setString("Printing job " + job.getToken());
                break;

            case "Ready for Collection":
                progressBar.setValue(80);
                progressBar.setString("Ready for collection");
                break;

            default:
                progressBar.setValue(0);
                progressBar.setString(job.getStatus());
        }
    }

    private void openDocument() {
        if (currentJob == null) {
            showMessage("No active job.");
            return;
        }

        File file = new File(currentJob.getDocumentPath());

        if (!file.exists() || !file.isFile()) {
            JOptionPane.showMessageDialog(
                    this,
                    "The uploaded document could not be found.",
                    "File Not Found",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        if (!Desktop.isDesktopSupported()
                || !Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
            JOptionPane.showMessageDialog(
                    this,
                    "Opening documents is not supported on this computer.",
                    "Unsupported Operation",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        try {
            Desktop.getDesktop().open(file);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Unable to open the document: " + ex.getMessage(),
                    "Document Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                "Queue Empty",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}
