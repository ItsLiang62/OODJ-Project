package gui.helper;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public final class PageDesigner {
    public static void displayBorderLayoutListPage(JFrame frame, String title, JLabel titleLabel, JButton[] loadPanelButtons, JButton[] operatePanelButtons, JButton backButton, JScrollPane scrollPane) {
        frame.setTitle(title);
        frame.setSize(700, 500);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GridBagConstraints gbc = new GridBagConstraints();

        // Adding panels to ListPage (BorderLayout)
        JPanel northPanel = new JPanel(new GridBagLayout());
        JPanel centerPanel = new JPanel(new GridBagLayout());
        JPanel southPanel = new JPanel(new GridBagLayout());

        frame.add(northPanel, BorderLayout.NORTH);
        frame.add(centerPanel, BorderLayout.CENTER);
        frame.add(southPanel, BorderLayout.SOUTH);

        // Adding panels to northPanel (GridBagLayout)
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 0;

        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel loadPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        gbc.gridy = 0;
        northPanel.add(backPanel, gbc);
        gbc.gridy = 1;
        northPanel.add(titlePanel, gbc);
        gbc.gridy = 2;
        northPanel.add(loadPanel, gbc);

        // Adding panels to centerPanel (GridBagLayout)
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 0;

        gbc.gridy = 0;
        centerPanel.add(scrollPane, gbc);

        // Adding panels to southPanel (GridBagLayout)
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 0;

        JPanel operatePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        JPanel fillerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));

        gbc.gridy = 0;
        southPanel.add(operatePanel, gbc);
        gbc.gridy = 1;
        southPanel.add(fillerPanel, gbc);

        // Add components to backPanel (FlowLayout)
        backPanel.add(backButton);

        // Add components to titlePanel (FlowLayout)
        titlePanel.add(titleLabel);

        // Add components to loadPanel (FlowLayout)
        if (loadPanelButtons != null) {
            Arrays.stream(loadPanelButtons).forEach(loadPanel::add);
        }

        // Add components to operatePanel (FlowLayout)
        if (operatePanelButtons != null) {
            Arrays.stream(operatePanelButtons).forEach(operatePanel::add);
        }


        frame.setVisible(true);
    }

    public static void displayBorderLayoutMainPage(JFrame frame, String title, JLabel titleLabel, JButton[] functionalityButtons, JButton backButton) {
        frame.setTitle(title);
        frame.setSize(700, 500);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GridBagConstraints gbc = new GridBagConstraints();

        // Adding panels to MainPage (BorderLayout)
        JPanel northPanel = new JPanel(new GridBagLayout());
        JPanel centerPanel = new JPanel(new GridBagLayout());

        frame.add(northPanel, BorderLayout.NORTH);
        frame.add(centerPanel, BorderLayout.CENTER);

        // Adding panels to northPanel (GridBagLayout)
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 0;

        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        gbc.gridy = 0;
        northPanel.add(backPanel, gbc);
        gbc.gridy = 1;
        northPanel.add(titlePanel, gbc);

        // Adding components to centerPanel (GridBagLayout)
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 0;

        gbc.gridy = 0;
        for (JButton button: functionalityButtons) {
            centerPanel.add(button, gbc);
            gbc.gridy ++;
        }

        // Add components to backPanel
        backPanel.add(backButton);

        // Add components to titlePanel
        titlePanel.add(titleLabel);

        frame.setVisible(true);
    }

    public static void displayBorderLayoutProfilePage(JFrame frame, String title, JLabel titleLabel, JComponent[] dataFields, JLabel[] labels, JButton saveButton, JButton backButton) {
        frame.setTitle(title);
        frame.setSize(700, 500);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel northPanel = new JPanel(new GridBagLayout());
        JPanel centerPanel = new JPanel(new GridBagLayout());
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.Y_AXIS));
        southPanel.add(Box.createRigidArea(new Dimension(0, 100)));

        GridBagConstraints gbc = new GridBagConstraints();

        // Adding panels to ProfilePage (BorderLayout)
        frame.add(northPanel, BorderLayout.NORTH);
        frame.add(centerPanel, BorderLayout.CENTER);
        frame.add(southPanel, BorderLayout.SOUTH);

        // Adding panels to northPanel (GridBagLayout)
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 0;

        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        gbc.gridy = 0;
        northPanel.add(backPanel);
        gbc.gridy = 1;
        northPanel.add(titlePanel);


        // Adding components to centerPanel (GridBagLayout)
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridx = 0;

        gbc.gridy = 0;
        for (JLabel label: labels) {
            centerPanel.add(label, gbc);
            gbc.gridy ++;
        }

        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 1;

        gbc.gridy = 0;
        for (JComponent dataField: dataFields) {
            centerPanel.add(dataField, gbc);
            gbc.gridy ++;
        }

        // Adding panels to southPanel (BoxLayout)
        JPanel savePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        southPanel.add(savePanel);

        // Adding components to backPanel
        backPanel.add(backButton);

        // Adding components to titlePanel
        titlePanel.add(titleLabel);

        // Adding components to savePanel
        savePanel.add(saveButton);

        frame.setVisible(true);
    }
}
