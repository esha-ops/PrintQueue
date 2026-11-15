
package com.printqueue.ui;

import com.printqueue.service.PrintQueueService;

import javax.swing.*;
import java.awt.*;

public class CustomerFrame extends JFrame {

    private final PrintQueueService queueService;

    public CustomerFrame(PrintQueueService queueService) {

        super("PrintQueue - Customer Portal");

        this.queueService = queueService;

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Position customer portal on the left half
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int halfWidth = screen.width / 2;
        int height = screen.height - 40;

        setBounds(0, 0, halfWidth, height);

        JTabbedPane tabs = new JTabbedPane();

        NewJobPanel newJobPanel = new NewJobPanel(queueService);
        TrackJobPanel trackJobPanel = new TrackJobPanel(queueService);

        tabs.addTab("New Print Job", newJobPanel);
        tabs.addTab("Track My Job", trackJobPanel);

        // Logout button
        JButton logoutButton = new JButton("Logout");

        logoutButton.addActionListener(e -> logout());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.add(logoutButton);

        add(topPanel, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);

        setVisible(true);
    }

    private void logout() {

        for (Window window : Window.getWindows()) {
            if (window instanceof CustomerFrame ||
                    window instanceof OwnerFrame) {
                window.dispose();
            }
        }

        new LoginFrame(queueService);
    }
}
