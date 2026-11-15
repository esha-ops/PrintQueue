
package com.printqueue.ui;

import com.printqueue.model.PrintJob;
import com.printqueue.service.PrintQueueService;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TrackJobPanel extends JPanel {

    private final PrintQueueService queueService;

    private final JTextField tokenField = new JTextField(15);

    private final JLabel statusLabel =
            new JLabel("Enter your token to track your job.");

    private final JTextArea detailsArea = new JTextArea();

    private final JButton trackButton = new JButton("Track Job");
    private final JButton billButton = new JButton("View / Print Bill");

    private String trackedToken;
    private String lastKnownStatus;

    private final Runnable trackingListener;

    public TrackJobPanel(PrintQueueService queueService) {

        this.queueService = queueService;
        trackingListener = this::refreshStatus;

        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(25, 40, 25, 40));

        JLabel title = new JLabel(
                "Track Your Print Job",
                SwingConstants.CENTER
        );
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));

        JPanel searchPanel = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 10, 15)
        );

        searchPanel.add(new JLabel("Enter Job Token:"));
        searchPanel.add(tokenField);
        searchPanel.add(trackButton);

        detailsArea.setEditable(false);
        detailsArea.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        detailsArea.setLineWrap(true);
        detailsArea.setWrapStyleWord(true);

        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);

        billButton.setEnabled(false);
        billButton.addActionListener(e -> showBill());

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        actionPanel.add(billButton);

        JPanel resultPanel = new JPanel(new BorderLayout(10, 15));
        resultPanel.setBorder(
                BorderFactory.createTitledBorder("Job Information")
        );

        resultPanel.add(statusLabel, BorderLayout.NORTH);
        resultPanel.add(new JScrollPane(detailsArea), BorderLayout.CENTER);
        resultPanel.add(actionPanel, BorderLayout.SOUTH);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(title, BorderLayout.NORTH);
        topPanel.add(searchPanel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);
        add(resultPanel, BorderLayout.CENTER);

        trackButton.addActionListener(e -> trackJob());
        tokenField.addActionListener(e -> trackJob());
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
            lastKnownStatus = null;
            billButton.setEnabled(false);

            statusLabel.setText("Job Not Found");
            statusLabel.setForeground(Color.RED);

            detailsArea.setText(
                    "Please check your token and try again."
            );
            return;
        }

        trackedToken = token.toUpperCase();

        // Remember the current status so an old completed job
        // does not trigger a new notification.
        lastKnownStatus = job.getStatus();

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

        String currentStatus = job.getStatus();

        // Notify only when this tracked job changes to Completed.
        if (lastKnownStatus != null
                && !lastKnownStatus.equals("Completed")
                && currentStatus.equals("Completed")) {

            JOptionPane.showMessageDialog(
                    this,
                    "Your print job is ready!\n\n"
                            + "Please come and collect your documents.\n"
                            + "Job Token: " + job.getToken(),
                    "PrintQueue - Job Completed",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }

        lastKnownStatus = currentStatus;

        statusLabel.setText("Current Status: " + currentStatus);

        switch (currentStatus) {

            case "Queued":
                statusLabel.setForeground(new Color(200, 140, 0));
                break;

            case "Printing":
                statusLabel.setForeground(new Color(30, 100, 200));
                break;

            case "Ready for Collection":
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

        String queueInfo = position == -1
                ? "No longer in the active queue"
                : String.valueOf(position);

        detailsArea.setText(
                "Token: " + job.getToken() + "\n\n"
                        + "Customer Name: " + job.getCustomerName() + "\n"
                        + "Document: " + job.getDocumentName() + "\n"
                        + "Service: " + job.getServiceType() + "\n"
                        + "Pages: " + job.getPages() + "\n"
                        + "Copies: " + job.getCopies() + "\n"
                        + "Paper Size: " + job.getPaperSize() + "\n"
                        + "Print Mode: " + job.getPrintMode() + "\n"
                        + "Estimated Cost: Rs. "
                        + String.format("%.2f", job.getEstimatedCost()) + "\n"
                        + "Estimated Time: " + job.getEstimatedMinutes()
                        + " minutes\n"
                        + "Queue Position: " + queueInfo + "\n"
                        + "Submitted At: " + job.getSubmittedTime()
        );

        billButton.setEnabled(currentStatus.equals("Completed"));
    }

    private void showBill() {

        if (trackedToken == null) {
            return;
        }

        PrintJob job = queueService.getJobByToken(trackedToken);

        if (job == null || !job.getStatus().equals("Completed")) {
            return;
        }

        String date = LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm a")
        );

        String bill =
                "              PRINTQUEUE\n"
                + "       SMART XEROX SHOP\n"
                + "----------------------------------------\n"
                + "                 INVOICE\n"
                + "----------------------------------------\n"
                + "Invoice / Job ID : " + job.getJobId() + "\n"
                + "Job Token        : " + job.getToken() + "\n"
                + "Date             : " + date + "\n"
                + "Customer         : " + job.getCustomerName() + "\n"
                + "Phone            : " + job.getPhoneNumber() + "\n"
                + "----------------------------------------\n"
                + "Service          : " + job.getServiceType() + "\n"
                + "Document         : " + job.getDocumentName() + "\n"
                + "Paper Size       : " + job.getPaperSize() + "\n"
                + "Pages            : " + job.getPages() + "\n"
                + "Copies           : " + job.getCopies() + "\n"
                + "Print Mode       : " + job.getPrintMode() + "\n"
                + "----------------------------------------\n"
                + "TOTAL AMOUNT     : Rs. "
                + String.format("%.2f", job.getEstimatedCost()) + "\n"
                + "Payment Status   : To be collected\n"
                + "Job Status       : " + job.getStatus() + "\n"
                + "----------------------------------------\n"
                + "       Thank you for choosing us!\n"
                + "----------------------------------------\n";

        JTextArea billArea = new JTextArea(bill);
        billArea.setEditable(false);
        billArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        billArea.setMargin(new Insets(15, 15, 15, 15));

        JScrollPane scrollPane = new JScrollPane(billArea);
        scrollPane.setPreferredSize(new Dimension(450, 480));

        JButton printButton = new JButton("Print / Save as PDF");
        JButton saveButton = new JButton("Save Bill");

        JPanel buttons = new JPanel(new FlowLayout());
        buttons.add(printButton);
        buttons.add(saveButton);

        JDialog dialog = new JDialog(
                (Frame) SwingUtilities.getWindowAncestor(this),
                "PrintQueue - Customer Bill",
                true
        );

        dialog.setLayout(new BorderLayout(10, 10));
        dialog.add(scrollPane, BorderLayout.CENTER);
        dialog.add(buttons, BorderLayout.SOUTH);

        printButton.addActionListener(e -> {
            try {
                billArea.print();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        dialog,
                        "Unable to print bill: " + ex.getMessage(),
                        "Print Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        saveButton.addActionListener(e -> saveBill(bill));

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void saveBill(String bill) {

        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Save Customer Bill");
        chooser.setSelectedFile(new File("PrintQueue_Bill.txt"));

        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {

            File file = chooser.getSelectedFile();

            try (FileWriter writer = new FileWriter(file)) {

                writer.write(bill);

                JOptionPane.showMessageDialog(
                        this,
                        "Bill saved successfully!"
                );

            } catch (IOException ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "Unable to save bill: " + ex.getMessage(),
                        "Save Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    @Override
    public void addNotify() {
        super.addNotify();

        queueService.addListener(trackingListener);
        refreshStatus();
    }

    @Override
    public void removeNotify() {
        queueService.removeListener(trackingListener);
        super.removeNotify();
    }
}
