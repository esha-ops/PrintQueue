
package com.printqueue;

import com.printqueue.service.PrintQueueService;
import com.printqueue.ui.LoginFrame;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PrintQueueService queueService = new PrintQueueService();
            new LoginFrame(queueService);
        });
    }
}
