package gui.manager;

import gui.helper.ListenerHelper;
import operation.Invoice;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import user.Manager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Month;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.List;

public class ManageReportsPage extends JFrame {
    private final Manager managerUser;

    public ManageReportsPage(Manager managerUser) {
        this.managerUser = managerUser;

        setTitle("View Reports Page");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        JLabel titleLabel = new JLabel("Manage Reports");
        JComboBox<Month> monthComboBox = new JComboBox<>(Month.values());
        JButton generateReportButton = new JButton("Generate Report");
        JButton viewRevenueTrendButton = new JButton("View Revenue Trend");
        JButton backButton = new JButton("Back");
        JPanel reportPanel = new JPanel();

        reportPanel.setForeground(Color.WHITE);
        generateReportButton.addActionListener(this.new GenerateReportButtonListener(monthComboBox, reportPanel));
        viewRevenueTrendButton.addActionListener(this.new ViewRevenueTrendButtonListener());
        backButton.addActionListener(this.new BackButtonListener());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 0;

        // Adding panels to ManageReportsPage (BorderLayout)
        JPanel northPanel = new JPanel(new GridBagLayout());
        JPanel centerPanel = new JPanel(new GridBagLayout());
        JPanel southPanel = new JPanel(new GridBagLayout());

        add(northPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);

        // Adding panels to northPanel (GridBagLayout)
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel generateReportPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        gbc.gridy = 0;
        northPanel.add(backPanel, gbc);
        gbc.gridy = 1;
        northPanel.add(titlePanel, gbc);
        gbc.gridy = 2;
        northPanel.add(generateReportPanel, gbc);

        // Adding panels to centerPanel (GridBagLayout)
        gbc.gridy = 0;
        centerPanel.add(reportPanel);

        // Adding panels to southPanel
        JPanel viewRevenueTrendPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        gbc.gridy = 0;
        southPanel.add(viewRevenueTrendPanel);

        // Adding components to backPanel (FlowLayout)
        backPanel.add(backButton);

        // Adding components to titlePanel (FlowLayout)
        titlePanel.add(titleLabel);

        // Adding components to generateReportPanel (FlowLayout)
        generateReportPanel.add(monthComboBox);
        generateReportPanel.add(generateReportButton);

        // Adding components to viewRevenueTrendPanel
        viewRevenueTrendPanel.add(viewRevenueTrendButton);

        setVisible(true);
    }

    private class GenerateReportButtonListener implements ActionListener {
        JComboBox<Month> monthComboBox;
        JPanel reportPanel;

        public GenerateReportButtonListener(JComboBox<Month> monthComboBox, JPanel reportPanel) {
            this.monthComboBox = monthComboBox;
            this.reportPanel = reportPanel;
            this.reportPanel.setLayout(new GridBagLayout());
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            // Filter invoice records to this month only
            Month month = (Month) monthComboBox.getSelectedItem();
            java.util.List<Invoice> allInvoicesOfMonth = managerUser.getAllInvoicesOfMonth(month);
            java.util.List<Double> allAppointmentRevenueOfMonth = allInvoicesOfMonth.stream().map(Invoice::getTotalAmount).toList();
            DoubleSummaryStatistics appointmentRevenueOfMonthStats = allAppointmentRevenueOfMonth.stream().mapToDouble(Double::doubleValue).summaryStatistics();

            // Calculate statistics
            String totalAppointmentRevenue = String.valueOf(appointmentRevenueOfMonthStats.getSum());
            String numAppointments = String.valueOf(appointmentRevenueOfMonthStats.getCount());
            String avgAppointmentRevenue = String.valueOf(appointmentRevenueOfMonthStats.getAverage());
            String stdDevAppointmentRevenue = String.valueOf(getStandardDeviation(allAppointmentRevenueOfMonth, Double.parseDouble(avgAppointmentRevenue)));
            String maxAppointmentRevenue = String.valueOf(appointmentRevenueOfMonthStats.getMax());
            String minAppointmentRevenue = String.valueOf(appointmentRevenueOfMonthStats.getMin());
            String q1AppointmentRevenue;
            String q3AppointmentRevenue;
            try {
                q1AppointmentRevenue = String.valueOf(getQ1Q3(allAppointmentRevenueOfMonth)[0]);
                q3AppointmentRevenue = String.valueOf(getQ1Q3(allAppointmentRevenueOfMonth)[1]);
            } catch (IllegalArgumentException exception) {
                q1AppointmentRevenue = "Not Applicable";
                q3AppointmentRevenue = "Not Applicable";
            }
            if (allInvoicesOfMonth.isEmpty()) {
                stdDevAppointmentRevenue = "Not Applicable";
                maxAppointmentRevenue = "Not Applicable";
                minAppointmentRevenue = "Not Applicable";
            }

            // Fill reportPanel with labels and statistics
            reportPanel.removeAll();

            JLabel[] labels = {new JLabel("Total Appointment Revenue:"), new JLabel("Number of Appointments:"), new JLabel("Average Revenue per Appointment:"), new JLabel("Standard Deviation of Appointment Revenues:"), new JLabel("Highest Appointment Revenue:"), new JLabel("Lowest Appointment Revenue:"), new JLabel("Middle 50% of Appointment Revenues:")};
            JLabel[] stats = {new JLabel(totalAppointmentRevenue), new JLabel(numAppointments), new JLabel(avgAppointmentRevenue), new JLabel(stdDevAppointmentRevenue), new JLabel(maxAppointmentRevenue), new JLabel(minAppointmentRevenue), new JLabel(String.format("%s - %s", q1AppointmentRevenue, q3AppointmentRevenue))};

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);

            gbc.gridx = 0;
            gbc.anchor = GridBagConstraints.EAST;

            gbc.gridy = 0;
            for (JLabel label: labels) {
                reportPanel.add(label, gbc);
                gbc.gridy ++;
            }

            gbc.gridx = 1;
            gbc.anchor = GridBagConstraints.CENTER;

            gbc.gridy = 0;
            for (JLabel stat: stats) {
                reportPanel.add(stat, gbc);
                gbc.gridy ++;
            }

            reportPanel.revalidate();
            reportPanel.repaint();
        }

        private double getStandardDeviation(java.util.List<Double> dataset, double mean) {
            double sumSq = 0;
            for (double data: dataset) {
                sumSq += Math.pow(data - mean, 2);
            }
            return Math.sqrt(sumSq / dataset.size());
        }

        public double[] getQ1Q3(java.util.List<Double> dataset) {
            if (dataset == null || dataset.size() < 4) {
                throw new IllegalArgumentException("Dataset must contain at least 4 elements to compute Q1 and Q3.");
            }

            double middleIndex = (dataset.size()-1)/2.0;

            java.util.List<Double> datasetCopy = new ArrayList<>(dataset);
            datasetCopy.sort(Comparator.comparingDouble(data -> data));
            java.util.List<Double> firstHalfDataset;
            List<Double> secondHalfDataset;
            if (Math.floor(middleIndex) != middleIndex) {
                firstHalfDataset = datasetCopy.subList(0, (int) Math.ceil(middleIndex));
                secondHalfDataset = datasetCopy.subList((int) Math.ceil(middleIndex), datasetCopy.size());
            } else {
                firstHalfDataset = datasetCopy.subList(0, (int) middleIndex);
                secondHalfDataset = datasetCopy.subList((int) middleIndex + 1, datasetCopy.size());
            }
            double q1, q3;
            double halfDatasetMiddleIndex = (firstHalfDataset.size()-1)/2.0;
            if (Math.floor(halfDatasetMiddleIndex) == halfDatasetMiddleIndex) {
                q1 = firstHalfDataset.get((int) halfDatasetMiddleIndex);
                q3 = secondHalfDataset.get((int) halfDatasetMiddleIndex);
            } else {
                int lowerIndex = (int) Math.floor(halfDatasetMiddleIndex);
                int upperIndex = (int) Math.ceil(halfDatasetMiddleIndex);
                q1 = (firstHalfDataset.get(lowerIndex) + firstHalfDataset.get(upperIndex))/2;
                q3 = (secondHalfDataset.get(lowerIndex) + secondHalfDataset.get(upperIndex))/2;
            }
            return new double[] {Math.round(q1*100)/100.0, Math.round(q3*100)/100.0};
        }
    }

    private class ViewRevenueTrendButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            List<Double> monthlyRevenues = new ArrayList<>();
            for (Month month: Month.values()) {
                double thisMonthRevenue = 0;
                for (Invoice invoice: managerUser.getAllInvoicesOfMonth(month)) {
                    thisMonthRevenue += invoice.getTotalAmount();
                }
                monthlyRevenues.add(thisMonthRevenue);
            }

            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            for (int i=0; i<monthlyRevenues.size(); i++) {
                dataset.addValue(monthlyRevenues.get(i), "Total Revenue", Month.values()[i].toString().substring(0, 3));
            }

            JFreeChart chart = ChartFactory.createBarChart("Monthly Revenue", "Month", "Revenue (RM)", dataset);

            ChartPanel chartPanel = new ChartPanel(chart);

            JFrame popUpChartWindow = new JFrame("Revenue Trend 2025");
            popUpChartWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            popUpChartWindow.setContentPane(chartPanel);
            popUpChartWindow.pack();
            popUpChartWindow.setLocationRelativeTo(null);
            popUpChartWindow.setVisible(true);
        }
    }

    private class BackButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(() -> new ManagerMainPage(managerUser));
            dispose();
        }
    }
}
