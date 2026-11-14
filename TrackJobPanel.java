
package com.printqueue.ui;

import com.printqueue.model.PrintJob;
import com.printqueue.service.PrintQueueService;

import javax.swing.*;
import java.awt.*;

public class TrackJobPanel extends JPanel {

    private final PrintQueueService queueService;

    private final JTextField tokenField = new JTextField(15);

    private final JLabel statusLabel = new JLabel("Enter your token to track your job.");
    private final JTextArea detailsArea = new JTextArea();

    private final JButton trackButton = new JButton("Track Job");

    private String trackedToken;

    private final Timer refreshTimer;

    public TrackJobPanel(PrintQueueService queueService) {

        this.queueService = queueService;

        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(25, 40, 25, 40));

        JLabel title = new JLabel(
                "Track Your Print Job",
                SwingConstants.CENTER
        );

        title.setFont(new Font("Segoe UI", Font.BOLD, 24));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 15));

        searchPanel.add(new JLabel("Enter Job Token:"));
        searchPanel.add(tokenField);
        searchPanel.add(trackButton);

        detailsArea.setEditable(false);
        detailsArea.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        detailsArea.setLineWrap(true);
        detailsArea.setWrapStyleWord(true);

        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel resultPanel = new JPanel(new BorderLayout(10, 20));
        resultPanel.setBorder(
                BorderFactory.createTitledBorder("Job Information")
        );

        resultPanel.add(statusLabel, BorderLayout.NORTH);
        resultPanel.add(new JScrollPane(detailsArea), BorderLayout.CENTER);

        add(title, BorderLayout.NORTH);
        add(searchPanel, BorderLayout.CENTER);
        add(resultPanel, BorderLayout.SOUTH);

        trackButton.addActionListener(e -> trackJob());
        tokenField.addActionListener(e -> trackJob());

        refreshTimer = new Timer(2000, e -> refreshStatus());
        refreshTimer.start();
    }

    private void trackJob() {

        String token = tokenField.getText().trim();

        if (token.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please enter your job token."
            );
            return;
        }

        PrintJob job = queueService.getJobByToken(token);

        if (job == null) {
            trackedToken = null;
            statusLabel.setText("Job Not Found");
            statusLabel.setForeground(Color.RED);
            detailsArea.setText(
                    "Please check your token and try again."
            );
            return;
        }

        trackedToken = token.toUpperCase();

        refreshStatus();
    }

    private void refreshStatus() {

        if (trackedToken == null) {
            return;
        }

        PrintJob job = queueService.getJobByToken(trackedToken);

        if (job == null) {
            return;
        }

        statusLabel.setText("Current Status: " + job.getStatus());

        switch (job.getStatus()) {
            case "Queued":
                statusLabel.setForeground(new Color(200, 140, 0));
                break;

            case "Printing":
                statusLabel.setForeground(new Color(30, 100, 200));
                break;

            case "Ready for Collection":
                statusLabel.setForeground(new Color(0, 140, 70));
                break;

            case "Completed":
                statusLabel.setForeground(new Color(0, 140, 70));
                break;

            case "Cancelled":
                statusLabel.setForeground(Color.RED);
                break;

            default:
                statusLabel.setForeground(Color.BLACK);
        }

        int position = queueService.getQueuePosition(job);

        String queueInfo;

        if (position == -1) {
            queueInfo = "No longer in the active queue";
        } else {
            queueInfo = String.valueOf(position);
        }

        detailsArea.setText(
                "Token: " + job.getToken() + "\n\n" +
                "Customer Name: " + job.getCustomerName() + "\n" +
                "Document: " + job.getDocumentName() + "\n" +
                "Service: " + job.getServiceType() + "\n" +
                "Pages: " + job.getPages() + "\n" +
                "Copies: " + job.getCopies() + "\n" +
                "Paper Size: " + job.getPaperSize() + "\n" +
                "Print Mode: " + job.getPrintMode() + "\n" +
                "Estimated Cost: Rs. " + job.getEstimatedCost() + "\n" +
                "Estimated Time: " + job.getEstimatedMinutes() + " minutes\n" +
                "Queue Position: " + queueInfo + "\n" +
                "Submitted At: " + job.getSubmittedTime()
        );
    }
}
