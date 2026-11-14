
package com.printqueue.ui;

import com.printqueue.service.PrintQueueService;

import javax.swing.*;
import java.awt.*;

public class OwnerFrame extends JFrame {

    public OwnerFrame(PrintQueueService queueService) {

        super("PrintQueue - Shop Owner Portal");

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(950, 650);
        setLocation(650, 50);

        JTabbedPane tabs = new JTabbedPane();

        QueuePanel queuePanel = new QueuePanel(queueService);
        OperatorPanel operatorPanel = new OperatorPanel(queueService);

        tabs.addTab("Live Queue", queuePanel);
        tabs.addTab("Shop Operator", operatorPanel);

        add(tabs, BorderLayout.CENTER);

        setVisible(true);
    }
}
