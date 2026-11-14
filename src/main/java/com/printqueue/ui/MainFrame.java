package com.printqueue.ui;

import com.printqueue.service.PrintQueueService;

public class MainFrame {
    public MainFrame() {
        PrintQueueService queueService = new PrintQueueService();

        new CustomerFrame(queueService);
        new OwnerFrame(queueService);
    }
}