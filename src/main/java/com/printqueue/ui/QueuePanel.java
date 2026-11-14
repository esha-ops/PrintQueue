
package com.printqueue.ui;

import com.printqueue.model.PrintJob;
import com.printqueue.service.PrintQueueService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class QueuePanel extends JPanel {

    private final PrintQueueService queueService;
    private final DefaultTableModel tableModel;
    private final Runnable queueListener;

    public QueuePanel(PrintQueueService queueService) {

        this.queueService = queueService;
        this.queueListener = () ->
            refreshQueue(this.queueService.getActiveJobs());

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        String[] columns = {
                "Token", "Customer", "Phone", "Service",
                "Pages", "Copies", "Paper", "Mode",
                "Cost", "Estimated Time", "Document", "Status"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(tableModel);
        table.setRowHeight(28);
        table.setFillsViewportHeight(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        JLabel title = new JLabel(
                "Live Print Queue",
                SwingConstants.CENTER
        );

        title.setFont(new Font("Segoe UI", Font.BOLD, 20));

        add(title, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        queueService.addListener(queueListener);

        refreshQueue(queueService.getActiveJobs());
    }

    public void refreshQueue(java.util.List<PrintJob> jobs) {

        tableModel.setRowCount(0);

        for (PrintJob job : jobs) {
            tableModel.addRow(job.toTableRow());
        }
    }

    @Override
    public void removeNotify() {
        queueService.removeListener(queueListener);
        super.removeNotify();
    }

    @Override
    public void addNotify() {
        super.addNotify();

        queueService.addListener(queueListener);
        refreshQueue(queueService.getActiveJobs());
    }
}
