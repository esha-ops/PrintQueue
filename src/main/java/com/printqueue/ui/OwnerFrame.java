
package com.printqueue.ui;

import com.printqueue.service.PrintQueueService;

import javax.swing.*;
import java.awt.*;

public class OwnerFrame extends JFrame {

    private final PrintQueueService queueService;

    public OwnerFrame(PrintQueueService queueService) {

        super("PrintQueue - Shop Owner Portal");

        this.queueService = queueService;

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Position owner portal on the right half
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int halfWidth = screen.width / 2;
        int height = screen.height - 40;

        setBounds(halfWidth, 0, halfWidth, height);

        JTabbedPane tabs = new JTabbedPane();

        QueuePanel queuePanel = new QueuePanel(queueService);
        OperatorPanel operatorPanel = new OperatorPanel(queueService);

        tabs.addTab("Live Queue", queuePanel);
        tabs.addTab("Shop Operator", operatorPanel);

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
