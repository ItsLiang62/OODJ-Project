package gui.manager;

import user.Manager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Month;

public class ManageReportsPage {
    private final Manager managerUser;

    public ManageReportsPage(Manager managerUser) {
        this.managerUser = managerUser;

        JButton januaryButton = new JButton("January");
    }
}
