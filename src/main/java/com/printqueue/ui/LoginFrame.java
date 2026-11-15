
package com.printqueue.ui;

import com.printqueue.service.AccountService;
import com.printqueue.service.PrintQueueService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginFrame extends JFrame {

    private final PrintQueueService queueService;
    private final AccountService accountService = new AccountService();

    private final Color navy = new Color(25, 45, 75);
    private final Color blue = new Color(45, 105, 180);

    public LoginFrame(PrintQueueService queueService) {
        this.queueService = queueService;

        setTitle("PrintQueue - Welcome");
        setSize(500, 480);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        createUI();
        setVisible(true);
    }

    private void createUI() {
        JPanel main = new JPanel(new BorderLayout(20, 25));
        main.setBackground(Color.WHITE);
        main.setBorder(new EmptyBorder(35, 40, 35, 40));

        JLabel title = new JLabel("PrintQueue", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 32));
        title.setForeground(navy);

        JLabel subtitle = new JLabel(
                "Smart Xerox Shop Management System",
                SwingConstants.CENTER
        );
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(Color.GRAY);

        JPanel heading = new JPanel(new GridLayout(2, 1, 5, 5));
        heading.setBackground(Color.WHITE);
        heading.add(title);
        heading.add(subtitle);

        JPanel buttons = new JPanel(new GridLayout(3, 1, 15, 15));
        buttons.setBackground(Color.WHITE);

        JButton customerButton = createButton("Continue as Customer");
        JButton ownerButton = createButton("Shop Owner Login");
        JButton demoButton = createButton("Launch Demo - Both Portals");

        customerButton.addActionListener(e -> showCustomerOptions());
        ownerButton.addActionListener(e -> showOwnerOptions());
        demoButton.addActionListener(e -> openDemoPortals());

        buttons.add(customerButton);
        buttons.add(ownerButton);
        buttons.add(demoButton);

        JLabel footer = new JLabel(
                "Welcome! Please select your account type.",
                SwingConstants.CENTER
        );
        footer.setForeground(Color.GRAY);
        footer.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        main.add(heading, BorderLayout.NORTH);
        main.add(buttons, BorderLayout.CENTER);
        main.add(footer, BorderLayout.SOUTH);

        setContentPane(main);
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setBackground(blue);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(15, 10, 15, 10));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void showCustomerOptions() {
        String[] options = {"Login", "Register", "Back"};

        int choice = JOptionPane.showOptionDialog(
                this,
                "Choose an option to continue.",
                "Customer Portal",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choice == 0) {
            customerLogin();
        } else if (choice == 1) {
            customerRegistration();
        }
    }

    private void customerRegistration() {
        Credentials credentials = getCredentials(
                "Customer Registration", true
        );

        if (credentials == null) return;

        if (!java.util.Arrays.equals(
                credentials.password, credentials.confirmPassword)) {
            clearCredentials(credentials);
            showError("Passwords do not match.");
            return;
        }

        try {
            accountService.registerCustomer(
                    credentials.username, credentials.password
            );

            clearCredentials(credentials);

            JOptionPane.showMessageDialog(
                    this,
                    "Registration successful! Welcome to PrintQueue."
            );

            openCustomerPortal();

        } catch (Exception e) {
            clearCredentials(credentials);
            showError(e.getMessage());
        }
    }

    private void customerLogin() {
        Credentials credentials = getCredentials(
                "Customer Login", false
        );

        if (credentials == null) return;

        boolean valid = accountService.authenticateCustomer(
                credentials.username, credentials.password
        );

        clearCredentials(credentials);

        if (valid) {
            openCustomerPortal();
        } else {
            showError("Invalid username or password.");
        }
    }

    private void showOwnerOptions() {
        if (!accountService.hasOwnerAccount()) {
            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "No Shop Owner account exists.\nWould you like to create one now?",
                    "First-Time Owner Setup",
                    JOptionPane.YES_NO_OPTION
            );

            if (choice == JOptionPane.YES_OPTION) {
                ownerSetup();
            }
        } else {
            ownerLogin();
        }
    }

    private void ownerSetup() {
        Credentials credentials = getCredentials(
                "Shop Owner Account Setup", true
        );

        if (credentials == null) return;

        if (!java.util.Arrays.equals(
                credentials.password, credentials.confirmPassword)) {
            clearCredentials(credentials);
            showError("Passwords do not match.");
            return;
        }

        try {
            accountService.setupOwner(
                    credentials.username, credentials.password
            );

            clearCredentials(credentials);

            JOptionPane.showMessageDialog(
                    this,
                    "Shop Owner account created successfully!"
            );

            openOwnerPortal();

        } catch (Exception e) {
            clearCredentials(credentials);
            showError(e.getMessage());
        }
    }

    private void ownerLogin() {
        Credentials credentials = getCredentials(
                "Shop Owner Login", false
        );

        if (credentials == null) return;

        boolean valid = accountService.authenticateOwner(
                credentials.username, credentials.password
        );

        clearCredentials(credentials);

        if (valid) {
            openOwnerPortal();
        } else {
            showError("Invalid username or password.");
        }
    }

    private Credentials getCredentials(String title, boolean confirmPassword) {
        JTextField usernameField = new JTextField(18);
        JPasswordField passwordField = new JPasswordField(18);
        JPasswordField confirmField = new JPasswordField(18);

        JPanel panel = new JPanel(new GridLayout(
                confirmPassword ? 3 : 2, 2, 8, 10
        ));

        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password (minimum 8 characters):"));
        panel.add(passwordField);

        if (confirmPassword) {
            panel.add(new JLabel("Confirm Password:"));
            panel.add(confirmField);
        }

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                title,
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION) return null;

        Credentials credentials = new Credentials();
        credentials.username = usernameField.getText().trim();
        credentials.password = passwordField.getPassword();

        if (confirmPassword) {
            credentials.confirmPassword = confirmField.getPassword();
        } else {
            credentials.confirmPassword = new char[0];
        }

        if (credentials.username.isEmpty() ||
                credentials.password.length == 0) {
            clearCredentials(credentials);
            showError("Please enter both username and password.");
            return null;
        }

        if (confirmPassword && credentials.password.length < 8) {
            clearCredentials(credentials);
            showError("Password must contain at least 8 characters.");
            return null;
        }

        return credentials;
    }

    private void clearCredentials(Credentials credentials) {
        java.util.Arrays.fill(credentials.password, '\0');
        java.util.Arrays.fill(credentials.confirmPassword, '\0');
    }

    private void openCustomerPortal() {
        new CustomerFrame(queueService);
        dispose();
    }

    private void openOwnerPortal() {
        new OwnerFrame(queueService);
        dispose();
    }

    private void openDemoPortals() {
        new CustomerFrame(queueService);
        new OwnerFrame(queueService);
        dispose();
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                "PrintQueue",
                JOptionPane.WARNING_MESSAGE
        );
    }

    private static class Credentials {
        String username;
        char[] password;
        char[] confirmPassword;
    }
}
