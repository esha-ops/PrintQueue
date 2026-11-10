
package com.printqueue.ui;

import com.printqueue.service.PrintQueueService;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public MainFrame() {

        super("PrintQueue - Smart Xerox Shop Management");

        PrintQueueService queueService = new PrintQueueService();

        NewJobPanel newJobPanel = new NewJobPanel(queueService);
        QueuePanel queuePanel = new QueuePanel(queueService);
        OperatorPanel operatorPanel = new OperatorPanel(queueService);
        TrackJobPanel trackJobPanel = new TrackJobPanel(queueService);

        newJobPanel.setQueuePanel(queuePanel);
        newJobPanel.setOperatorPanel(operatorPanel);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 650);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();

        tabs.addTab("New Print Job", newJobPanel);
        tabs.addTab("Live Queue", queuePanel);
        tabs.addTab("Track My Job", trackJobPanel);
        tabs.addTab("Shop Operator", operatorPanel);

        add(tabs, BorderLayout.CENTER);

        setVisible(true);
    }
}
