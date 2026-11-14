
package com.printqueue.ui;

import com.printqueue.model.PrintJob;
import com.printqueue.service.BillingService;
import com.printqueue.service.PrintQueueService;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.io.File;

public class NewJobPanel extends JPanel {

    private final PrintQueueService queueService;
    private final BillingService billingService = new BillingService();

    private final JTextField customerNameField = new JTextField();
    private final JTextField phoneNumberField = new JTextField();

    private final JComboBox<String> serviceTypeBox = new JComboBox<>(new String[]{
            "Black & White Printout", "Color Printout", "Photocopy",
            "Scanning", "Spiral Binding"
    });

    private final JComboBox<String> paperSizeBox =
            new JComboBox<>(new String[]{"A4", "A3", "Legal"});

    private final JComboBox<String> printModeBox =
            new JComboBox<>(new String[]{"Single-sided", "Double-sided"});

    private final JSpinner pagesSpinner =
            new JSpinner(new SpinnerNumberModel(1, 1, 1000, 1));

    private final JSpinner copiesSpinner =
            new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));

    private final JLabel costLabel = new JLabel("Estimated Cost: Rs. 0.00");
    private final JLabel statusLabel =
            new JLabel("No active print job.", SwingConstants.CENTER);

    private final JLabel fileLabel = new JLabel("No file selected");
    private final JButton uploadButton = new JButton("Upload Document");

    private File selectedFile;

    public NewJobPanel(PrintQueueService queueService) {
        this.queueService = queueService;

        // Restrict phone number input
        restrictPhoneInput();

        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        add(createForm(), BorderLayout.NORTH);
        add(createStatusPanel(), BorderLayout.CENTER);
    }

    // Accept only digits and a maximum of 10 characters
    private void restrictPhoneInput() {

        AbstractDocument document =
                (AbstractDocument) phoneNumberField.getDocument();

        document.setDocumentFilter(new DocumentFilter() {

            @Override
            public void replace(FilterBypass fb, int offset,
                                int length, String text,
                                AttributeSet attrs)
                    throws BadLocationException {

                String current = fb.getDocument().getText(
                        0, fb.getDocument().getLength());

                String replacement = text == null ? "" : text;

                String updated = current.substring(0, offset)
                        + replacement
                        + current.substring(offset + length);

                if (updated.matches("[0-9]{0,10}")) {
                    fb.replace(offset, length, replacement, attrs);
                }
            }

            @Override
            public void insertString(FilterBypass fb, int offset,
                                     String text, AttributeSet attrs)
                    throws BadLocationException {

                replace(fb, offset, 0, text, attrs);
            }
        });
    }

    private JPanel createForm() {
        JPanel form = new JPanel(new GridLayout(6, 4, 12, 12));
        form.setBorder(BorderFactory.createTitledBorder("Submit Print / Xerox Job"));

        JButton calculateButton = new JButton("Calculate Cost");
        JButton submitButton = new JButton("Submit Job");
        JButton clearButton = new JButton("Clear Form");

        form.add(new JLabel("Customer Name:"));
        form.add(customerNameField);
        form.add(new JLabel("Phone Number:"));
        form.add(phoneNumberField);

        form.add(new JLabel("Service Type:"));
        form.add(serviceTypeBox);
        form.add(new JLabel("Paper Size:"));
        form.add(paperSizeBox);

        form.add(new JLabel("Number of Pages:"));
        form.add(pagesSpinner);
        form.add(new JLabel("Number of Copies:"));
        form.add(copiesSpinner);

        form.add(new JLabel("Print Mode:"));
        form.add(printModeBox);
        form.add(calculateButton);
        form.add(submitButton);

        form.add(new JLabel("Upload Document:"));
        form.add(uploadButton);
        form.add(fileLabel);
        form.add(new JLabel(""));

        form.add(costLabel);
        form.add(new JLabel(""));
        form.add(new JLabel(""));
        form.add(clearButton);

        calculateButton.addActionListener(e -> calculateCost());
        uploadButton.addActionListener(e -> uploadDocument());
        submitButton.addActionListener(e -> submitJob());
        clearButton.addActionListener(e -> clearForm());

        return form;
    }

    private JPanel createStatusPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Your Job Status"),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        panel.add(statusLabel, BorderLayout.CENTER);

        return panel;
    }

    private void uploadDocument() {
        JFileChooser fileChooser = new JFileChooser();

        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Supported Documents (PDF, JPG, JPEG, PNG)",
                "pdf", "jpg", "jpeg", "png"
        );

        fileChooser.setFileFilter(filter);

        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();

            String fileName = file.getName().toLowerCase();

            if (!(fileName.endsWith(".pdf")
                    || fileName.endsWith(".jpg")
                    || fileName.endsWith(".jpeg")
                    || fileName.endsWith(".png"))) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please select a PDF, JPG, JPEG or PNG file.",
                        "Unsupported File",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            if (!file.isFile() || !file.canRead()) {
                JOptionPane.showMessageDialog(
                        this,
                        "The selected file cannot be accessed.",
                        "File Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            selectedFile = file;
            fileLabel.setText(file.getName());
        }
    }

    private void calculateCost() {
        double cost = billingService.calculateCost(
                (String) serviceTypeBox.getSelectedItem(),
                (String) paperSizeBox.getSelectedItem(),
                getPages(),
                getCopies()
        );

        costLabel.setText("Estimated Cost: Rs. " + String.format("%.2f", cost));
    }

    private void submitJob() {
        String name = customerNameField.getText().trim();
        String phone = phoneNumberField.getText().trim();

        if (name.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please enter customer name and phone number.",
                    "Missing Details",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // Validate exactly 10 digits
        if (!phone.matches("[0-9]{10}")) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a valid 10-digit phone number.",
                    "Invalid Phone Number",
                    JOptionPane.WARNING_MESSAGE
            );
            phoneNumberField.requestFocusInWindow();
            return;
        }

        if (selectedFile == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please upload a document before submitting the job.",
                    "Document Required",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        double cost = billingService.calculateCost(
                (String) serviceTypeBox.getSelectedItem(),
                (String) paperSizeBox.getSelectedItem(),
                getPages(),
                getCopies()
        );

        PrintJob job = queueService.submitJob(
                name,
                phone,
                (String) serviceTypeBox.getSelectedItem(),
                (String) paperSizeBox.getSelectedItem(),
                getPages(),
                getCopies(),
                (String) printModeBox.getSelectedItem(),
                cost,
                selectedFile.getAbsolutePath()
        );

        int position = queueService.getQueuePosition(job);

        statusLabel.setText(
                "<html><center>Print job submitted successfully.<br><br>"
                        + "Token: <b>" + job.getToken() + "</b><br>"
                        + "Document: " + job.getDocumentName() + "<br>"
                        + "Service: " + job.getServiceType() + "<br>"
                        + "Estimated cost: <b>Rs. "
                        + String.format("%.2f", cost) + "</b><br>"
                        + "Jobs ahead: <b>" + (position - 1) + "</b><br>"
                        + "Estimated ready in: <b>"
                        + job.getEstimatedMinutes() + " minutes</b></center></html>"
        );

        clearForm();
    }

    private void clearForm() {
        customerNameField.setText("");
        phoneNumberField.setText("");

        pagesSpinner.setValue(1);
        copiesSpinner.setValue(1);

        serviceTypeBox.setSelectedIndex(0);
        paperSizeBox.setSelectedIndex(0);
        printModeBox.setSelectedIndex(0);

        selectedFile = null;
        fileLabel.setText("No file selected");

        costLabel.setText("Estimated Cost: Rs. 0.00");
    }

    private int getPages() {
        return (int) pagesSpinner.getValue();
    }

    private int getCopies() {
        return (int) copiesSpinner.getValue();
    }
}
