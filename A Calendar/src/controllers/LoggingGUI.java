package controllers;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;


//some calendar code from https://javahungry.blogspot.com/2013/06/calendar-implementation-gui-based.html

public class LoggingGUI {
    JLabel labelUsername, labelPassword;
    JButton logInBut,nextMonth;
    JTable calendarTable;
    JFrame frmCalendar;
    Container pane;
    DefaultTableModel otherCalendarTable;
    JScrollPane scrollCalendar;
    JPanel calendarPane;
    JTextField txt_user;
    JPasswordField txt_pass;
    private LoggingIO logIO;
    String logginUser = null;


    public LoggingGUI() {
        logIO = new LoggingIO();
    }
    public String start(){

        frmCalendar = new JFrame("Calendar");
        frmCalendar.setSize(400,400);
        pane =frmCalendar.getContentPane();
        pane.setLayout(null);
        frmCalendar.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        labelUsername = new JLabel("Username:");
        labelPassword = new JLabel("Password:");
        logInBut = new JButton("Log in");
        nextMonth = new JButton("Create a new account");
        otherCalendarTable = new DefaultTableModel(){public boolean isCellEditable(int rowIndex, int mColIndex){return false;}};
        calendarTable = new JTable(otherCalendarTable);
        scrollCalendar = new JScrollPane(calendarTable);
        calendarPane = new JPanel(null);
        txt_user = new javax.swing.JTextField();
        txt_pass = new javax.swing.JPasswordField();

        calendarPane.setBorder(BorderFactory.createTitledBorder("Calendar"));

        logInBut.addActionListener(new preMonth_Action());
        nextMonth.addActionListener(new nextMonth_Action());

        pane.add(calendarPane);
        calendarPane.add(labelUsername);
        calendarPane.add(labelPassword);
        calendarPane.add(logInBut);
        calendarPane.add(nextMonth);
        calendarPane.add(txt_user);
        calendarPane.add(txt_pass);

        calendarPane.setBounds(0,0,400,400);
        labelUsername.setBounds(60,135,80,25);
        labelPassword.setBounds(60,160,80,25);
        txt_user.setBounds(140,135,100,25);
        txt_pass.setBounds(140,160,100,25);
        logInBut.setBounds(160, 195, 80, 25);
        nextMonth.setBounds(120, 230, 160, 25);

        frmCalendar.setResizable(false);
        frmCalendar.setVisible(true);
        return null;
    }

    class preMonth_Action implements ActionListener{
        public void actionPerformed (ActionEvent e){
            try{logIn();}
            catch(IOException E){}
        }
    }
    class nextMonth_Action implements ActionListener{
        public void actionPerformed (ActionEvent e){
            try{signUp();}
            catch(IOException E){}
        }
    }

    private String logIn() throws IOException {
        String username, password;
        username = txt_user.getText();
        password = txt_pass.getText();

        if (logIO.userExists(username, password)) {
            // Log in successful
            frmCalendar.setVisible(false);
            CalendarGUI calendarGUI = new CalendarGUI(username);
            return username;
        } else {
            System.out.println("No matching userdata found. Please check your both username and password enter correctly.");
            return null;
        }
    }

    private String signUp() throws IOException {
        String username, password;
        username = txt_user.getText();
        password = txt_pass.getText();

        if (logIO.userExists(username, "~~~")) {
            // Log in successful
            System.out.println("There already exists a same username.");
            return null;
        }

        logIO.createUser(username, password);
        System.out.println("User creation successful.");

        return null;
    }
}