
package com.printqueue.ui;

import com.printqueue.service.PrintQueueService;

import javax.swing.*;
import java.awt.*;

public class CustomerFrame extends JFrame {

    public CustomerFrame(PrintQueueService queueService) {

        super("PrintQueue - Customer Portal");

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(900, 650);
        setLocation(30, 50);

        JTabbedPane tabs = new JTabbedPane();

        NewJobPanel newJobPanel = new NewJobPanel(queueService);
        TrackJobPanel trackJobPanel = new TrackJobPanel(queueService);

        tabs.addTab("New Print Job", newJobPanel);
        tabs.addTab("Track My Job", trackJobPanel);

        add(tabs, BorderLayout.CENTER);

        setVisible(true);
    }
}
