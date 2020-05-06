package controllers;

import entities.Alert;
import entities.Event;
import entities.Habit;
import entities.Memo;
import javafx.util.Pair;
import useCases.CalendarManager;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.GregorianCalendar;


//some calendar code from https://javahungry.blogspot.com/2013/06/calendar-implementation-gui-based.html

public class CalendarGUI {
    CalendarManager calendarManager = new CalendarManager();
    JLabel labelMonth,equalBoundary;
    JLabel viewLabel;
    JLabel viewTextLabel1,viewTextLabel2,viewTextLabel3;
    JButton prevMonth,nextMonth;
    JTable calendarTable;
    JComboBox comboYear;
    JComboBox viewBox;
    JComboBox calendarBox;
    JButton createBut,searchBut, createHabitBut;
    JButton createCalendarBut;
    JFrame frmCalendar;
    Container pane;
    DefaultTableModel otherCalendarTable;
    JScrollPane scrollCalendar;
    JPanel calendarPane;

    JButton viewHabitBut;
    JFrame habitListView;

    JFrame frmCreateEvent;
    Container createEventContainer;
    JPanel createEventPane;
    JLabel enterEventName, enterStartTime, enterEndTime, pressSetupOption;
    JButton setUpEventYes, setUpEventNo;
    JButton OKToCreate;
    JTextField textEventName, textStartTime, textEndTime;

    JFrame frmCreateHabit;
    Container createHabitContainer;
    JPanel createHabitPane;
    JLabel enterHabitName, enterHabitDescription;
    JButton OKToCreateHabit;
    JTextField textHabitName, textHabitDescription;

    JList habitList;
    ArrayList<Habit> parallelHabitList;

    JComponent viewHabitContainer;
    DefaultListModel listModel;
    JPanel habitListButtons;
    JButton habitPlus, habitMinus, habitEdit;
    JScrollPane habitScroller;


    JFrame habitEditView;
    Container editHabitContainer;
    JPanel editHabitPane;
    JLabel editHabitName, editHabitDesc;
    JButton editHabitSubmit;
    JTextField editHabitTextName, editHabitTextDesc;


    //Calendar GUI Components
    int theYear, theMonth, theDay, currentYear, currentMonth;

    JFrame frmCreateCalendar;
    Container createCalendarContatiner;
    JPanel createCalendarPane;
    JLabel enterCalendarName;
    JButton OKToCreateCalendar;
    JTextField textCalendarName;

    JFrame frmSearchFunction;
    Container searchFunctionContatiner;
    JPanel searchFunctionPane;
    JLabel enterSourceToSearch;
    JButton OKToSearch, editThisEvent , addNewFeature, postphone, share;
    JComboBox chooseSearchBy;
    JTextField textSourceToSearch;
    JList eventsReturnedBySearch;

    JFrame frmEditFunction;
    Container editFunctionContainer;
    JPanel editFunctionPane;
    JLabel viewAllContent, editNameMessage, editStartTimeMessage,editEndTimeMessage,editFrequencyMessage,editNumberOfEventsMessage;
    JTextField textEditName,textEditStartTime, textEditEndTime, textEditFrequency, textEditNumberOfEvents;
    JComboBox chooseToEdit, chooseToEditMessage;
    JButton OKtoEdit;

    JFrame frmAlertWindow;
    Container alertWindow;
    JPanel alertWindowPane;
    JLabel someAlert;

    JLabel selectedDateDisplay;
    DefaultListModel eventsOnDateList;
    JLabel eventsOnDate;
    JList selectedDateEvents;
    JScrollPane dateEventsScroller;

    private CalendarIO calendarIO;
    private ArrayList<Pair<String, CalendarManager>> calendarManagers = new ArrayList<>();


    public CalendarGUI(String username) throws IOException {
        calendarIO = new CalendarIO();
        this.restoreCalendars(username);
        frmCalendar = new JFrame("Calendar");
        frmCalendar.setSize(800, 800);
        pane = frmCalendar.getContentPane();
        pane.setLayout(null);
        frmCalendar.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frmAlertWindow = new JFrame("Alert");
        frmAlertWindow.setSize(400,400);
        alertWindow = frmAlertWindow.getContentPane();
        alertWindow.setLayout(null);
        frmAlertWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        alertWindowPane = new JPanel(null);
        alertWindow.add(alertWindowPane);
        someAlert = new JLabel();
        alertWindowPane.add(someAlert);
        alertWindowPane.setBounds(0,0,400,400);
        someAlert.setBounds(10,10,380,380);
        frmAlertWindow.setVisible(false);
        frmAlertWindow.setResizable(false);

        selectedDateDisplay = new JLabel();
        eventsOnDate = new JLabel();
        eventsOnDate.setVisible(true);

        LocalDateTime lastLogin = calendarManager.getLastLoginTime();
        if(!calendarManager.getEventManager().alertInTimeInterval(lastLogin).isEmpty()){
            String alertsForUser = "<html>******************************<br>" +
                    "There are alerts for you!!!!!<br>";
            for(Alert c: calendarManager.getEventManager().alertInTimeInterval(lastLogin)){
                alertsForUser += c.toString() + "<br>";
            }
            alertsForUser += alertsForUser + "<br>" + "******************************<html>";
            someAlert.setText(alertsForUser);
            frmAlertWindow.setVisible(true);
        }

        labelMonth = new JLabel();
        comboYear = new JComboBox();
        prevMonth = new JButton("<=");
        nextMonth = new JButton("=>");
        viewLabel = new JLabel("View:");
        searchBut = new JButton("Search");
        viewBox = new JComboBox();
        calendarBox = new JComboBox();
        createCalendarBut = new JButton("Create a new Calendar");
        createBut = new JButton("Create a new Event");
        createHabitBut = new JButton("Create a new Habit");
        viewHabitBut = new JButton("View Habits");
        otherCalendarTable = new DefaultTableModel(){public boolean isCellEditable(int rowIndex, int mColIndex){return false;}};
        calendarTable = new JTable(otherCalendarTable);
        MyRenderer myRenderer = new MyRenderer();
        calendarTable.setDefaultRenderer(Object.class,myRenderer);
        scrollCalendar = new JScrollPane(calendarTable);
        calendarPane = new JPanel(null);
        viewTextLabel1= new JLabel();
        viewTextLabel2 = new JLabel();
        viewTextLabel3 = new JLabel();
        equalBoundary = new JLabel("========================================================================================================");

        frmCreateEvent = new JFrame("Create an event");
        frmCreateEvent.setSize(400,500);
        createEventContainer = frmCreateEvent.getContentPane();
        createEventContainer.setLayout(null);
        frmCreateEvent.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        createEventPane = new JPanel(null);
        enterEventName = new JLabel("Please enter the Event name");
        enterStartTime = new JLabel("<html>Please enter the start time<br/>format\"Year-Month-Day-hour-minute\"<br/>For Example, \"2001-01-01-20-15\"</html>", SwingConstants.CENTER);
        enterEndTime = new JLabel("<html>Please enter the end time<br/>format\"Year-Month-Day-hour-minute\"<br/>For Example, \"2001-01-01-20-15\"</html>", SwingConstants.CENTER);
        pressSetupOption = new JLabel("<html>Do you want to setup the event?<html>", SwingConstants.CENTER);
        setUpEventYes = new JButton("Yes");
        setUpEventNo = new JButton("No");
        textEventName = new javax.swing.JTextField();
        textStartTime = new javax.swing.JTextField();
        textEndTime = new javax.swing.JTextField();
        OKToCreate = new JButton("Okay");

        frmCreateHabit = new JFrame("Create a Habit");
        frmCreateHabit.setSize(400, 500);
        createHabitContainer = frmCreateHabit.getContentPane();
        createHabitContainer.setLayout(null);
        frmCreateHabit.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        createHabitPane = new JPanel(null);
        enterHabitName = new JLabel("Please enter the Habit name");
        enterHabitDescription = new JLabel("Please enter the Habit description");
        textHabitName = new JTextField();
        textHabitDescription = new JTextField();
        OKToCreateHabit = new JButton("Okay");

        habitListView = new JFrame("List of Habits:");
        habitListView.setSize(400, 500);
        habitListView.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        WindowListener hlvWindListener = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                //clear the list
                listModel.removeAllElements();
                parallelHabitList.clear();
                habitListView.dispose();
            }
        };
        habitListView.addWindowListener(hlvWindListener);
        eventsOnDateList = new DefaultListModel();
        calendarTable.setCellSelectionEnabled(true);
        selectedDateEvents = new JList(eventsOnDateList);
        selectedDateEvents.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        selectedDateEvents.setVisibleRowCount(9);
        dateEventsScroller = new JScrollPane(selectedDateEvents);
        System.out.println("got past here");


        calendarTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                eventsOnDateList.clear();
                if(calendarTable.getSelectedRow() != -1 && calendarTable.getSelectedColumn() != -1
                        && calendarTable.getValueAt(calendarTable.getSelectedRow(), calendarTable.getSelectedColumn()) != null) {

                    selectedDateDisplay.setText(labelMonth.getText() + " " + otherCalendarTable.getValueAt(calendarTable.getSelectedRow(),
                            calendarTable.getSelectedColumn()).toString() + " " + comboYear.getSelectedItem().toString());
                    selectedDateDisplay.setVisible(true);

                    int selectedDay = (Integer) calendarTable.getValueAt(calendarTable.getSelectedRow(), calendarTable.getSelectedColumn());
                    String yearString = Integer.toString(currentYear);
                    String monthString = Integer.toString(currentMonth + 1);
                    String dayString = Integer.toString(selectedDay);

                    if(Integer.toString(selectedDay).length() == 1) {
                        dayString = "0" + selectedDay;
                    }
                    if(monthString.length() == 1) {
                        monthString = "0" + monthString;
                    }
                    String dateTimeString = yearString + "-" + monthString + "-" + dayString + "T00:00:00";
                    ArrayList<Event> eventsOnSpecDate = calendarManager.getEventManager().EventsOnSpecificDate(LocalDateTime.parse(dateTimeString));
                    System.out.println(eventsOnSpecDate.size());
                    if(eventsOnSpecDate.size() == 0){
                        eventsOnDate.setText("There are no events on this day.");
                    } else{
                        eventsOnDate.setText("Events: ");
                        for(Event eve : eventsOnSpecDate){
                            eventsOnDateList.addElement(eve);
                        }
                        dateEventsScroller.setVisible(true);
                    }
                } else if (calendarTable.getSelectedRow() != -1 && calendarTable.getSelectedColumn() != -1 &&
                        calendarTable.getValueAt(calendarTable.getSelectedRow(),
                                calendarTable.getSelectedColumn()) == null){
                    selectedDateDisplay.setVisible(false);
                }
            }
        });

        calendarTable.getColumnModel().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                eventsOnDateList.clear();
                if(calendarTable.getSelectedRow() != -1 && calendarTable.getSelectedColumn() != -1
                        && calendarTable.getValueAt(calendarTable.getSelectedRow(), calendarTable.getSelectedColumn()) != null) {

                    selectedDateDisplay.setText(labelMonth.getText() + " " + otherCalendarTable.getValueAt(calendarTable.getSelectedRow(),
                            calendarTable.getSelectedColumn()).toString() + " " + comboYear.getSelectedItem().toString());
                    selectedDateDisplay.setVisible(true);

                    int selectedDay = (Integer) calendarTable.getValueAt(calendarTable.getSelectedRow(), calendarTable.getSelectedColumn());
                    String yearString = Integer.toString(currentYear);
                    String monthString = Integer.toString(currentMonth + 1);
                    String dayString = Integer.toString(selectedDay);

                    if(Integer.toString(selectedDay).length() == 1) {
                        dayString = "0" + selectedDay;
                    }
                    if(monthString.length() == 1) {
                        monthString = "0" + monthString;
                    }
                    String dateTimeString = yearString + "-" + monthString + "-" + dayString + "T00:00:00";
                    ArrayList<Event> eventsOnSpecDate = calendarManager.getEventManager().EventsOnSpecificDate(LocalDateTime.parse(dateTimeString));
                    System.out.println(eventsOnSpecDate.size());
                    if(eventsOnSpecDate.size() == 0){
                        eventsOnDate.setText("There are no events on this day.");
                    } else{
                        eventsOnDate.setText("Events: ");
                        for(Event eve : eventsOnSpecDate){
                            eventsOnDateList.addElement(eve);
                        }
                        dateEventsScroller.setVisible(true);
                    }
                } else if (calendarTable.getSelectedRow() != -1 && calendarTable.getSelectedColumn() != -1 &&
                        calendarTable.getValueAt(calendarTable.getSelectedRow(),
                                calendarTable.getSelectedColumn()) == null){
                    selectedDateDisplay.setVisible(false);
                }
            }
        });

        viewHabitContainer = new JPanel();

        listModel = new DefaultListModel();
        parallelHabitList = calendarManager.getHabitManager().getAllHabits();

        habitList = new JList(listModel);
        habitList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        habitList.setSelectedIndex(0);
        habitList.setVisibleRowCount(5);
        habitScroller = new JScrollPane(habitList);

        habitPlus = new JButton("+");
        PlusListener plusListener = new PlusListener();
        habitPlus.addActionListener(plusListener);

        habitMinus = new JButton("-");
        MinusListener minusListener = new MinusListener();
        habitMinus.addActionListener(minusListener);

        habitEdit = new JButton("Edit!");
        EditListener editListener = new EditListener();
        habitEdit.addActionListener(editListener);

        habitListButtons = new JPanel();
        habitListButtons.add(habitPlus);
        habitListButtons.add(habitMinus);
        habitListButtons.add(habitEdit);

        viewHabitContainer.add(habitScroller);
        viewHabitContainer.add(habitListButtons);

        viewHabitContainer.setOpaque(true);
        habitListView.setContentPane(viewHabitContainer);

        frmCreateCalendar = new JFrame("Create a Calendar");
        frmCreateCalendar.setSize(300,400);
        createCalendarContatiner = frmCreateCalendar.getContentPane();
        frmCreateCalendar.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        createCalendarPane = new JPanel(null);
        enterCalendarName = new JLabel("Please enter the Calendar name");
        OKToCreateCalendar = new JButton("Okay");
        textCalendarName = new javax.swing.JTextField();

        frmSearchFunction = new JFrame("Search");
        frmSearchFunction.setSize(530,400);
        searchFunctionContatiner = frmSearchFunction.getContentPane();
        frmSearchFunction.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        searchFunctionPane = new JPanel(null);
        enterSourceToSearch = new JLabel("Which do you want to search?");
        OKToSearch = new JButton("Okay");
        chooseSearchBy = new JComboBox();
        textSourceToSearch = new javax.swing.JTextField();
        eventsReturnedBySearch = new JList();
        editThisEvent = new JButton("Add/Edit/Delete features");
        addNewFeature = new JButton("Delete");
        postphone = new JButton("Postpone this event");
        share = new JButton("Share this event");


        frmEditFunction = new JFrame("Edit");
        frmEditFunction.setSize(500,700);
        editFunctionContainer = frmEditFunction.getContentPane();
        frmEditFunction.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        editFunctionPane = new JPanel(null);
        viewAllContent = new JLabel();
        editNameMessage = new JLabel();
        editStartTimeMessage = new JLabel("<html>Please enter the start time and day<br>format\"Year-Month-Day-hour-minute\"." +
                "<br>For Example, \"2001-01-01-20-15\"<html>");
        editEndTimeMessage = new JLabel("<html>Please enter the end time and day<br>format\"Year-Month-Day-hour-minute\"." +
                "<br>For Example, \"2001-01-01-20-15\"<html>");
        editFrequencyMessage = new JLabel("<html>Please enter the frequency. <br>For example, 1 represent per day.<html>");
        editNumberOfEventsMessage = new JLabel("Please enter the number of events in the series.");
        chooseToEdit = new JComboBox();
        OKtoEdit = new JButton("Okay");
        chooseToEditMessage = new JComboBox();
        textEditName = new javax.swing.JTextField();
        textEditStartTime = new javax.swing.JTextField();
        textEditEndTime = new javax.swing.JTextField();
        textEditFrequency = new javax.swing.JTextField();
        textEditNumberOfEvents = new javax.swing.JTextField();

        calendarPane.setBorder(BorderFactory.createTitledBorder("Default Calendar"));

        chooseSearchBy.addItem("Date");
        chooseSearchBy.addItem("Event Name");
        chooseSearchBy.addItem("Series Name");
        chooseSearchBy.addItem("Tag Name");

        viewBox.addItem("Events");
        viewBox.addItem("Alerts");
        viewBox.addItem("Memos");

        chooseToEdit.addItem("Series");
        chooseToEdit.addItem("Tag");
        chooseToEdit.addItem("Memo");
        chooseToEdit.addItem("Alert");

        chooseToEditMessage.addItem("Add");
        chooseToEditMessage.addItem("Edit");
        chooseToEditMessage.addItem("Delete");


        for (Pair<String, CalendarManager> pair : this.calendarManagers) {
            calendarBox.addItem(pair.getKey());
        }

        prevMonth.addActionListener(new preMonth_Action());
        nextMonth.addActionListener(new nextMonth_Action());
        comboYear.addActionListener(new comboYear_Action());
        calendarBox.addActionListener(new comboBox_Action());
        viewBox.addActionListener(new viewBox_Action());
        searchBut.addActionListener(new searchBut_Action());
        createBut.addActionListener(new createBut_Action());
        createHabitBut.addActionListener(new createHabitBut_Action());
        viewHabitBut.addActionListener(new viewHabitBut_Action());
        OKToCreate.addActionListener(new OKToCreate_Action());
        OKToCreateHabit.addActionListener(new OKToCreateHabit_Action());
        setUpEventNo.addActionListener(new setUpEventNo_Action());
        setUpEventYes.addActionListener(new setUpEventYes_Action());
        createCalendarBut.addActionListener(new createCalendarBut_Action());
        OKToCreateCalendar.addActionListener(new OKToCreateCalendar_Action());
        OKToSearch.addActionListener(new OKToSearch_Action());
        chooseSearchBy.addActionListener(new chooseSearchBy_Action());
        OKtoEdit.addActionListener(new OKtoEdit_Action());
        chooseToEdit.addActionListener(new chooseToEdit_Action());
        editThisEvent.addActionListener(new editThisEvent_Action());
        addNewFeature.addActionListener(new addNewFeature_Action());
        postphone.addActionListener(new Postone_Action()); //TODO
        share.addActionListener(new share_Action()); //TODO

        frmCalendar.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frmCalendar.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                try {
                    exitProcedure(username);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        pane.add(calendarPane);
        calendarPane.add(labelMonth);
        calendarPane.add(comboYear);
        calendarPane.add(prevMonth);
        calendarPane.add(nextMonth);
        calendarPane.add(scrollCalendar);
        calendarPane.add(viewBox);
        calendarPane.add(createBut);
        calendarPane.add(createHabitBut);
        calendarPane.add(calendarBox);
        calendarPane.add(createCalendarBut);
        calendarPane.add(viewLabel);
        calendarPane.add(searchBut);
        calendarPane.add(viewTextLabel1);
        calendarPane.add(viewTextLabel2);
        calendarPane.add(viewTextLabel3);
        calendarPane.add(equalBoundary);
        calendarPane.add(selectedDateDisplay);

        calendarPane.add(eventsOnDate);
        calendarPane.add(dateEventsScroller);
        calendarPane.add(viewHabitBut);

        calendarPane.setBounds(0,0,800,1000);
        calendarBox.setBounds(24,75,120,30);
        createCalendarBut.setBounds(170,75,170,30);
        comboYear.setBounds(24, 170, 150, 30);
        prevMonth.setBounds(651, 150, 50, 50);
        nextMonth.setBounds(701, 150, 50, 50);
        scrollCalendar.setBounds(24, 200, 727, 251);
        viewLabel.setBounds(401,75,40,40);
        searchBut.setBounds(591,75,80,40);
        viewBox.setBounds(451,75,100,40);
        createBut.setBounds(601,451,150,30);
        createHabitBut.setBounds(400, 451, 150, 30);
        viewHabitBut.setBounds(200, 451, 150, 30);
        equalBoundary.setBounds(24,482,780,10);
        viewTextLabel1.setBounds(24,495,250,300);
        viewTextLabel2.setBounds(284,495,250,300);
        viewTextLabel3.setBounds(544,495,250,300);
        selectedDateDisplay.setBounds(350, 505, 200, 15);
        eventsOnDate.setBounds(275, 530, 200, 15);
        dateEventsScroller.setBounds(250, 580, 330, 100);


        createEventContainer.add(createEventPane);
        createEventPane.add(enterEventName);
        createEventPane.add(enterStartTime);
        createEventPane.add(enterEndTime);
        createEventPane.add(pressSetupOption);
        createEventPane.add(setUpEventNo);
        createEventPane.add(setUpEventYes);
        createEventPane.add(textEventName);
        createEventPane.add(textStartTime);
        createEventPane.add(textEndTime);
        createEventPane.add(OKToCreate);

        createEventPane.setBounds(0,0,400,600);
        enterEventName.setBounds(30,10,300,30);
        textEventName.setBounds(30,50,300,50);
        enterStartTime.setBounds(30,110,300,50);
        textStartTime.setBounds(30,170,300,50);
        enterEndTime.setBounds(30,230,300,50);
        textEndTime.setBounds(30,290,300,50);
        OKToCreate.setBounds(150,350,100,50);
        //pressSetupOption.setBounds(10);
        //setUpEventYes;setUpEventNo;

        createHabitContainer.add(createHabitPane);
        createHabitPane.add(enterHabitName);
        createHabitPane.add(textHabitName);
        createHabitPane.add(textHabitDescription);
        createHabitPane.add(enterHabitDescription);
        createHabitPane.add(OKToCreateHabit);

        createHabitPane.setBounds(0, 0, 400, 600);
        enterHabitName.setBounds(30, 10, 300, 30);
        textHabitName.setBounds(30, 50, 300, 50);
        enterHabitDescription.setBounds(30, 230, 300, 50);
        textHabitDescription.setBounds(30, 290, 300, 50);
        OKToCreateHabit.setBounds(150, 350, 100, 50);

        createCalendarContatiner.add(createCalendarPane);
        createCalendarPane.add(enterCalendarName);
        createCalendarPane.add(OKToCreateCalendar);
        createCalendarPane.add(textCalendarName);

        createCalendarPane.setBounds(0,0,300,400);
        enterCalendarName.setBounds(50,60,200,30);
        textCalendarName.setBounds(75,100,150,30);
        OKToCreateCalendar.setBounds(115,180,70,50);

        searchFunctionContatiner.add(searchFunctionPane);
        searchFunctionPane.add(enterSourceToSearch);
        searchFunctionPane.add(OKToSearch);
        searchFunctionPane.add(chooseSearchBy);
        searchFunctionPane.add(textSourceToSearch);
        searchFunctionPane.add(editThisEvent);
        searchFunctionPane.add(addNewFeature);
        searchFunctionPane.add(postphone);
        searchFunctionPane.add(share);

        searchFunctionPane.setBounds(0,0,530,400);
        enterSourceToSearch.setBounds(30,30,200,40);
        chooseSearchBy.setBounds(360,30,80,40);
        textSourceToSearch.setBounds(150,100,200,30);
        OKToSearch.setBounds(150,160,100,40);


        editFunctionContainer.add(editFunctionPane);
        editFunctionPane.add(viewAllContent);
        editFunctionPane.add(editNameMessage);
        editFunctionPane.add(editStartTimeMessage);
        editFunctionPane.add(editEndTimeMessage);
        editFunctionPane.add(editFrequencyMessage);
        editFunctionPane.add(editNumberOfEventsMessage);
        editFunctionPane.add(chooseToEditMessage);
        editFunctionPane.add(chooseToEdit);
        editFunctionPane.add(textEditName);
        editFunctionPane.add(textEditStartTime);
        editFunctionPane.add(textEditEndTime);
        editFunctionPane.add(textEditFrequency);
        editFunctionPane.add(textEditNumberOfEvents);
        editFunctionPane.add(OKtoEdit);

        editFunctionPane.setBounds(0,0,500,700);
        viewAllContent.setBounds(30,30, 500,100);
        chooseToEditMessage.setBounds(5,140,80,30);
        chooseToEdit.setBounds(90,140,80,30);
        OKtoEdit.setBounds(180,600,70,50);
        editNameMessage.setBounds(200,140,280,50);
        textEditName.setBounds(200,190,280,30);
        editStartTimeMessage.setBounds(200,230,280,50);
        textEditStartTime.setBounds(200,280,280,30);
        editEndTimeMessage.setBounds(200,320,280,50);
        textEditEndTime.setBounds(200,370,280,30);
        editFrequencyMessage.setBounds(200,410,280,50);
        textEditFrequency.setBounds(200,460,280,30);
        editNumberOfEventsMessage.setBounds(200,500,280,50);
        textEditNumberOfEvents.setBounds(200,550,280,30);

        textEditName.setVisible(false);
        editStartTimeMessage.setVisible(false);
        textEditStartTime.setVisible(false);
        editEndTimeMessage.setVisible(false);
        textEditEndTime.setVisible(false);
        editFrequencyMessage.setVisible(false);
        textEditFrequency.setVisible(false);
        editNumberOfEventsMessage.setVisible(false);
        textEditNumberOfEvents.setVisible(false);


        frmEditFunction.setVisible(false);
        frmEditFunction.setResizable(false);

        frmSearchFunction.setVisible(false);
        frmSearchFunction.setResizable(false);

        frmCreateCalendar.setResizable(false);
        frmCreateCalendar.setVisible(false);

        frmCreateEvent.setResizable(false);
        frmCreateEvent.setVisible(false);

        frmCreateHabit.setResizable(false);
        frmCreateHabit.setVisible(false);

        frmCalendar.setResizable(false);
        frmCalendar.setVisible(true);

        GregorianCalendar cal = new GregorianCalendar();
        theDay = cal.get(GregorianCalendar.DAY_OF_MONTH);
        theMonth = cal.get(GregorianCalendar.MONTH);
        theYear = cal.get(GregorianCalendar.YEAR);
        currentMonth = theMonth;
        currentYear = theYear;

        //Add headers
        String[] headers = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"}; //All headers
        for (int i=0; i<7; i++){
            otherCalendarTable.addColumn(headers[i]);
        }

        calendarTable.getParent().setBackground(calendarTable.getBackground());

        calendarTable.getTableHeader().setResizingAllowed(false);
        calendarTable.getTableHeader().setReorderingAllowed(false);

        calendarTable.setColumnSelectionAllowed(true);
        calendarTable.setRowSelectionAllowed(true);
        calendarTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        calendarTable.setRowHeight(38);
        otherCalendarTable.setColumnCount(7);
        otherCalendarTable.setRowCount(6);

        for (int i = theYear - 10; i <= theYear + 10; i++) {
            comboYear.addItem(String.valueOf(i));
        }
        refreshCalendar(theMonth, theYear);
    }

    private void exitProcedure(String username) throws IOException {
        frmCalendar.dispose();
        this.saveCalendars(username);
        System.exit(0);
    }

    private void refreshCalendar(int month, int year) {
        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        int nod, som;

        //Allow/disallow buttons
        prevMonth.setEnabled(true);
        nextMonth.setEnabled(true);
        if (month == 0 && year <= theYear - 10) {
            prevMonth.setEnabled(false);
        }
        if (month == 11 && year >= theYear + 10) {
            nextMonth.setEnabled(false);
        }
        labelMonth.setText(months[month]);
        labelMonth.setBounds(375,150,170,30);
        comboYear.setSelectedItem(String.valueOf(year));

        for (int i=0; i<6; i++){
            for (int j=0; j<7; j++){
                otherCalendarTable.setValueAt(null, i, j);
            }
        }

        GregorianCalendar cal = new GregorianCalendar(year, month, 1);
        nod = cal.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
        som = cal.get(GregorianCalendar.DAY_OF_WEEK);

        for (int i=1; i<=nod; i++){
            int row = new Integer((i+som-2)/7);
            int column  =  (i+som-2)%7;
            otherCalendarTable.setValueAt(i, row, column);
        }
    }

    private void restoreCalendars(String username) throws IOException {
        // Open calendar folder
        // copied from https://stackoverflow.com/questions/1844688/how-to-read-all-files-in-a-folder-from-java
        File folder = new File("phase2\\Database\\" + username);
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles == null) {
            CalendarManager calendarManager = new CalendarManager();
            this.calendarManagers.add(new Pair<>("calendar0", calendarManager));
        } else {
            for (File file : listOfFiles) {
                if (file.isFile()) {
                    CalendarManager calendarManager = new CalendarManager();
                    calendarIO.recreateCalendar(calendarManager, file.getName(), username);
                    this.calendarManagers.add(new Pair<>(file.getName(), calendarManager));
                }
            }
        }
        this.calendarManager = calendarManagers.get(0).getValue();
    }

    private void saveCalendars(String username) throws IOException {
        String path = "/" + username;
        for (Pair calendarPair : this.calendarManagers) {
            this.calendarIO.saveCalendarToFile((CalendarManager) calendarPair.getValue(),
                    (String) calendarPair.getKey(), username);
        }
    }


    public void start(String username) throws IOException {

        this.restoreCalendars(username);


        this.saveCalendars(username);

    }
    class preMonth_Action implements ActionListener{
        public void actionPerformed (ActionEvent e){
            if (currentMonth == 0) { //Back one year
                currentMonth = 11;
                currentYear -= 1;
            } else { //Back one month
                currentMonth -= 1;
            }
            refreshCalendar(currentMonth, currentYear);
        }
    }

    class comboBox_Action implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (calendarBox.getSelectedItem() != null) {
                String chosenCalendar = (String) calendarBox.getSelectedItem();
                for (Pair<String, CalendarManager> pair : calendarManagers) {
                    if (pair.getKey() == chosenCalendar) {
                        calendarManager = pair.getValue();
                    }
                }
            }
        }
    }

    class nextMonth_Action implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (currentMonth == 11) { //Forward one year
                currentMonth = 0;
                currentYear += 1;
            } else { //Foward one month
                currentMonth += 1;
            }
            refreshCalendar(currentMonth, currentYear);
        }
    }
    class comboYear_Action implements ActionListener{
        public void actionPerformed (ActionEvent e){
            if (comboYear.getSelectedItem() != null){
                String b = comboYear.getSelectedItem().toString();
                currentYear = Integer.parseInt(b);
                refreshCalendar(currentMonth, currentYear);
            }
        }
    }

    class viewBox_Action implements ActionListener{
        public void actionPerformed(ActionEvent e){
            if(viewBox.getSelectedItem() != null){
                int NO = viewBox.getSelectedIndex();
                System.out.println(NO+1);
                switch (NO+1){
                    case 1:
                        String eventString1= "",eventString2 = "",eventString3 = "";
                        ArrayList<entities.Event> pastEvents = calendarManager.getEventManager().getPastEvents();
                        if (pastEvents.isEmpty()){
                            eventString1 = "No past events exist.";
                            viewTextLabel1.setText(eventString1);
                        }else{
                            eventString1 = "<html>Past Event:<br>";
                            for (Event c : pastEvents) {
                                eventString1 = eventString1 + c.toString() + "<br>";
                            }
                            eventString1 = eventString1 + "<html>";
                            viewTextLabel1.setText(eventString1);
                        }

                        ArrayList<Event> curEvents = calendarManager.getEventManager().getCurrentEvents();
                        if (curEvents.isEmpty()){
                            eventString2 = "No current events exist.";
                            viewTextLabel2.setText(eventString2);
                        }else{
                            eventString2 = "<html>Current Event:<br>";
                            for (Event c : curEvents) {
                                eventString2 = eventString2 + c.toString() + "<br>";
                            }
                            eventString2 = eventString2 + "<html>";
                            viewTextLabel2.setText(eventString2);
                        }
                        ArrayList<Event> futEvents = calendarManager.getEventManager().getFutureEvents();
                        if (futEvents.isEmpty()){
                            eventString3 = "No future events exist.";
                            viewTextLabel3.setText(eventString3);
                        }else{
                            eventString3 = "<html>Future Event:<br>";
                            for (Event c : futEvents) {
                                eventString3 = eventString3 + c.toString() + "<br>";
                            }
                            eventString3 = eventString3 + "<html>";
                            viewTextLabel3.setText(eventString3);
                        }
                        break;
                    case 2:
                        viewTextLabel1.setText("");
                        viewTextLabel3.setText("");
                        String Alerts = "<html>Alerts: <br>";
                        if (calendarManager.getEventManager().getAlerts().isEmpty()) {
                            Alerts = "<html>No alerts exist.";
                            viewTextLabel2.setText(Alerts);
                        } else{
                            for(Alert c: calendarManager.getEventManager().getAlerts()){
                                Alerts = Alerts + c.viewToString();
                            }
                        }
                        Alerts = Alerts + "<html>";
                        viewTextLabel2.setText(Alerts);
                        break;
                    case 3:
                        viewTextLabel1.setText("");
                        viewTextLabel3.setText("");
                        String Memos = "<html>Memos: <br>";
                        if (calendarManager.getMemoManager().getAllMemos().isEmpty()) {
                            Memos = "<html>No memos exist.";
                            viewTextLabel2.setText(Memos);
                        }else{
                            for(Memo c: calendarManager.getMemoManager().getAllMemos()){
                                if(c != null){
                                    Memos = Memos + c.toString();
                                }
                            }
                        }
                        Memos = Memos + "<html>";
                        viewTextLabel2.setText(Memos);
                        break;
                }
            }
        }
    }

    class searchBut_Action implements ActionListener{
        public void actionPerformed(ActionEvent e){
            if(viewBox.getSelectedItem() != null){
                frmSearchFunction.setVisible(true);
            }
        }
    }
    class createBut_Action implements ActionListener{
        public void actionPerformed(ActionEvent e){
            frmCreateEvent.setVisible(true);
        }
    }

    class createHabitBut_Action implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            frmCreateHabit.setVisible(true);
        }
    }

    class viewHabitBut_Action implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            habitListView.setVisible(true);
            viewHabitContainer.setVisible(true);
            for (Habit h: calendarManager.getHabitManager().getAllHabits()){
                parallelHabitList.add(h);
                String properString;
                properString = h.toString();
                listModel.addElement(properString);
            }
        }
    }

    class PlusListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            int index = habitList.getSelectedIndex();
            Habit selectedHabit = parallelHabitList.get(index);
            selectedHabit.changePoints(1);
            listModel.removeElementAt(index);
            String propString = selectedHabit.getName() + " points: " + selectedHabit.getPoints();
            listModel.insertElementAt(propString, index);
            habitList.setSelectedIndex(index);
        }
    }

    class MinusListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            int index = habitList.getSelectedIndex();
            Habit selectedHabit = parallelHabitList.get(index);
            selectedHabit.changePoints(-1);
            listModel.removeElementAt(index);
            String propString = selectedHabit.getName() + " points: " + selectedHabit.getPoints();
            listModel.insertElementAt(propString, index);
            habitList.setSelectedIndex(index);
        }
    }

    class EditListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            //TODO sort this mess out!!!
            int index = habitList.getSelectedIndex();
            Habit selectedHabit = parallelHabitList.get(index);
            //on edit press, make a new view and then have a submit button.
            // On submit, change the habit, close the window, and revamp listModel
            habitEditView = new JFrame("Edit your Habit!");
            habitEditView.setSize(400, 500);
            editHabitContainer = habitEditView.getContentPane();
            editHabitContainer.setLayout(null);
            WindowListener hevWindListener = new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    super.windowClosing(e);

                }
            };
            habitEditView.addWindowListener(hevWindListener);
            editHabitPane = new JPanel(null);
            editHabitName = new JLabel("Enter the new Habit name");
            editHabitDesc = new JLabel("Please enter the new Habit description");
            editHabitTextName = new JTextField(selectedHabit.getName());
            editHabitTextDesc = new JTextField(selectedHabit.getDescription());
            editHabitSubmit = new JButton("Submit!");

            editHabitContainer.add(editHabitPane);
            editHabitPane.add(editHabitName);
            editHabitPane.add(editHabitTextName);
            editHabitPane.add(editHabitTextDesc);
            editHabitPane.add(editHabitDesc);
            editHabitPane.add(editHabitSubmit);

            EditSubmitListener submitListener = new EditSubmitListener();
            editHabitSubmit.addActionListener(submitListener);

            editHabitPane.setBounds(0, 0, 400, 600);
            editHabitName.setBounds(30, 10, 300, 30);
            editHabitTextName.setBounds(30, 50, 300, 50);
            editHabitDesc.setBounds(30, 230, 300, 50);
            editHabitTextDesc.setBounds(30, 290, 300, 50);
            editHabitSubmit.setBounds(150, 350, 100, 50);

            habitEditView.setVisible(true);
        }
    }
    class EditSubmitListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            int index = habitList.getSelectedIndex();
            Habit selecHab = parallelHabitList.get(index);
            selecHab.setName(editHabitTextName.getText());
            selecHab.setDescription(editHabitTextDesc.getText());
            listModel.removeAllElements();
            for(Habit h : parallelHabitList){
                String properString;
                properString = h.getName() + " points: " + h.getPoints();
                listModel.addElement(properString);
            }
            habitEditView.setVisible(false);
        }
    }
//    class habitBoxAction implements ActionListener{
//        public void valueChanged(ListSelectionEvent e){
//            if (e.getValueIsAdjusting() == false){
//                // Do something with the buttons that we have ie selecte
//            }
//        }
//    }
    private LocalDateTime parse_date_time(String input_time){
        Integer year = Integer.parseInt(input_time.substring(0, 4));
        Integer month = Integer.parseInt(input_time.substring(5, 7));
        Integer day = Integer.parseInt(input_time.substring(8,10));
        Integer hour = Integer.parseInt(input_time.substring(11, 13));
        Integer last = Integer.parseInt(input_time.substring(14));

        return LocalDateTime.of(year, month, day, hour, last);
    }

    class OKToCreate_Action implements ActionListener{
        public void actionPerformed(ActionEvent e){
            LocalDateTime[] duration = new LocalDateTime[2];
            duration[0] = parse_date_time(textStartTime.getText());
            duration[1] = parse_date_time(textEndTime.getText());
            Event newEvent = calendarManager.getEventManager().createEvent(duration,textEventName.getText());
            frmCreateEvent.setVisible(false);
        }
    }

    class OKToCreateHabit_Action implements ActionListener{
        public void actionPerformed(ActionEvent e){
            Habit newHabit = calendarManager.getHabitManager().createHabit(textHabitName.getText(),
                    textHabitDescription.getText());
            frmCreateHabit.setVisible(false);

        }
    }
    class setUpEventNo_Action implements ActionListener{
        public void actionPerformed(ActionEvent e){

        }
    }

    class setUpEventYes_Action implements ActionListener{
        public void actionPerformed(ActionEvent e){
        }
    }

    class createCalendarBut_Action implements ActionListener{
        public void actionPerformed(ActionEvent e){
            frmCreateCalendar.setVisible(true);
        }
    }

    class chooseSearchBy_Action implements ActionListener{
        public void actionPerformed(ActionEvent e){
            int searchNo = chooseSearchBy.getSelectedIndex();
            searchFunctionPane.remove(eventsReturnedBySearch);
            switch (searchNo+1){
                case 1:
                    enterSourceToSearch.setText("<html>Please enter the date to Search<br>Format \"2001-01-01-20-15\"<html>");
                    break;

                case 2:
                    enterSourceToSearch.setText("<html>Please enter the event name to Search<html>");
                    break;

                case 3:
                    enterSourceToSearch.setText("<html>Please enter the series name to Search<html>");
                    break;

                case 4:
                    enterSourceToSearch.setText("<html>Please enter the tag name to Search<html>");
            }
        }
    }

    class OKToCreateCalendar_Action implements ActionListener{
        public void actionPerformed(ActionEvent e){
            calendarBox.addItem(textCalendarName.getText());
            Pair newCalendar = new Pair((String)textCalendarName.getText(),new CalendarManager());
            calendarManagers.add(newCalendar);
        }
    }

    class OKToSearch_Action implements ActionListener{
        public void actionPerformed(ActionEvent e){
            int searchNo = chooseSearchBy.getSelectedIndex();
            switch (searchNo+1){
                case 1:
                    ArrayList<Event> eventsBySearchObject = new ArrayList<Event>();
                    searchFunctionPane.remove(eventsReturnedBySearch);
                    LocalDateTime search_Date = parse_date_time(textSourceToSearch.getText());
                    if (!calendarManager.getEventManager().SearchByDate(search_Date).isEmpty()) {
                        for (Event c : calendarManager.getEventManager().SearchByDate(search_Date)) {
                            eventsBySearchObject.add(c);
                        }
                    }
                    eventsReturnedBySearch = new JList(eventsBySearchObject.toArray());
                    eventsReturnedBySearch.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
                    eventsReturnedBySearch.setLayoutOrientation(JList.HORIZONTAL_WRAP);
                    eventsReturnedBySearch.setVisibleRowCount(-1);JScrollPane listScroller = new JScrollPane(eventsReturnedBySearch);

                    searchFunctionPane.add(eventsReturnedBySearch);
                    //searchFunctionPane.add(listScroller);
                    eventsReturnedBySearch.setBounds(30,210,450,150);
                    editThisEvent.setBounds(280,170,200,30);
                    addNewFeature.setBounds(30,170,80,30);
                    postphone.setBounds(30, 210, 80, 30);//TODO
                    share.setBounds(210,210, 80, 30);
                    //listScroller.setBounds(30,210,300,150);
                    break;

                case 2:
                    eventsBySearchObject = new ArrayList<Event>();
                    searchFunctionPane.remove(eventsReturnedBySearch);
                    String input_String = textSourceToSearch.getText();
                    if (!calendarManager.getEventManager().SearchByName(input_String).isEmpty()) {
                        for (Event c : calendarManager.getEventManager().SearchByName(input_String)) {
                            //eventsBySearch.add(c.toString());
                            eventsBySearchObject.add(c);
                        }
                    }
                    //eventsReturnedBySearch = new JList(eventsBySearch.toArray());
                    eventsReturnedBySearch = new JList(eventsBySearchObject.toArray());
                    eventsReturnedBySearch.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
                    eventsReturnedBySearch.setLayoutOrientation(JList.HORIZONTAL_WRAP);
                    eventsReturnedBySearch.setVisibleRowCount(-1);
                    listScroller = new JScrollPane(eventsReturnedBySearch);
                    searchFunctionPane.add(eventsReturnedBySearch);
                    //searchFunctionPane.add(listScroller);
                    eventsReturnedBySearch.setBounds(30,210,450,150);
                    editThisEvent.setBounds(280,170,200,30);
                    addNewFeature.setBounds(30,170,80,30);
                    //listScroller.setBounds(30,210,300,150);
                    break;

                case 3:
                    eventsBySearchObject = new ArrayList<>();
                    searchFunctionPane.remove(eventsReturnedBySearch);
                    input_String = textSourceToSearch.getText();
                    if (!calendarManager.getEventManager().SearchBySeries(input_String).isEmpty()) {
                        for (Event c : calendarManager.getEventManager().SearchBySeries(input_String)) {
                            eventsBySearchObject.add(c);
                        }
                    }
                    eventsReturnedBySearch = new JList(eventsBySearchObject.toArray());
                    eventsReturnedBySearch.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
                    eventsReturnedBySearch.setLayoutOrientation(JList.HORIZONTAL_WRAP);
                    eventsReturnedBySearch.setVisibleRowCount(-1);
                    listScroller = new JScrollPane(eventsReturnedBySearch);
                    searchFunctionPane.add(eventsReturnedBySearch);
                    //searchFunctionPane.add(listScroller);
                    eventsReturnedBySearch.setBounds(30,210,450,150);
                    editThisEvent.setBounds(280,170,200,30);
                    addNewFeature.setBounds(30,170,80,30);
                    //listScroller.setBounds(30,210,300,150);
                    break;

                case 4:
                    eventsBySearchObject = new ArrayList<>();
                    searchFunctionPane.remove(eventsReturnedBySearch);
                    input_String = textSourceToSearch.getText();
                    if (!calendarManager.getEventManager().SearchByTag(input_String).isEmpty()) {
                        for (Event c : calendarManager.getEventManager().SearchByTag(input_String)) {
                            eventsBySearchObject.add(c);
                        }
                    }
                    eventsReturnedBySearch = new JList(eventsBySearchObject.toArray());
                    eventsReturnedBySearch.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
                    eventsReturnedBySearch.setLayoutOrientation(JList.HORIZONTAL_WRAP);
                    eventsReturnedBySearch.setVisibleRowCount(-1);
                    listScroller = new JScrollPane(eventsReturnedBySearch);
                    searchFunctionPane.add(eventsReturnedBySearch);
                    //searchFunctionPane.add(listScroller);
                    eventsReturnedBySearch.setBounds(30,210,450,150);
                    editThisEvent.setBounds(280,170,200,30);
                    addNewFeature.setBounds(30,170,80,30);
                    //listScroller.setBounds(30,210,300,150);
            }
        }
    }

    class OKtoEdit_Action implements ActionListener{
        public void actionPerformed(ActionEvent e){
            int input_Choose_Functions = chooseToEditMessage.getSelectedIndex();
            switch (input_Choose_Functions + 1){
                case 1:
                    int input_Choose = chooseToEdit.getSelectedIndex();
                    switch (input_Choose+1){
                        case 1:
                            chooseToEdit.removeAllItems();
                            chooseToEdit.addItem("Series");
                            chooseToEdit.addItem("Tag");
                            chooseToEdit.addItem("Memo");
                            chooseToEdit.addItem("Alert");
                            if(textEditName.getText().trim().length() != 0 && textEditNumberOfEvents.getText().trim().length() == 0){
                                ((Event)eventsReturnedBySearch.getSelectedValue()).addSeriesNames(textEditName.getText());
                            } else if(textEditStartTime.getText().trim().length() != 0 && textEditEndTime.getText().trim().length() != 0
                                    && textEditFrequency.getText().trim().length() != 0 && textEditNumberOfEvents.getText().trim().length() != 0 ){
                                LocalDateTime[] durations = {parse_date_time(textEditStartTime.getText()),parse_date_time(textEditEndTime.getText())};
                                calendarManager.getEventManager().createFreqEvent(durations,Integer.parseInt(textEditFrequency.getText()),Integer.parseInt(textEditNumberOfEvents.getText()),textEditName.getText());
                            }
                            textEditNumberOfEvents.setText("");
                            break;
                        case 2:
                            if(textEditName.getText().trim().length() != 0){
                                ((Event)eventsReturnedBySearch.getSelectedValue()).addTag(textEditName.getText());
                            }
                            textEditNumberOfEvents.setText("");
                            break;
                        case 3:
                            if(textEditName.getText().trim().length() != 0){
                                Memo newMemo = calendarManager.getMemoManager().createMemo(textEditName.getText());
                                ((Event)eventsReturnedBySearch.getSelectedValue()).setMemo(newMemo);
                                newMemo.addEvent(((Event)eventsReturnedBySearch.getSelectedValue()));
                                textEditNumberOfEvents.setText("");
                            }
                            break;
                        case 4:
                            if(textEditName.getText().trim().length() != 0 && textEditFrequency.getText().trim().length() == 0){
                                calendarManager.createAlert(((Event)eventsReturnedBySearch.getSelectedValue()),parse_date_time(textEditStartTime.getText()),textEditName.getText());
                            } else if(textEditStartTime.getText().trim().length() != 0 && textEditFrequency.getText().trim().length() != 0){
                                calendarManager.createFreqAlert(((Event)eventsReturnedBySearch.getSelectedValue()),parse_date_time(textEditStartTime.getText()),Integer.parseInt(textEditFrequency.getText()),textEditName.getText());
                                textEditNumberOfEvents.setText("");
                            }
                            break;
                    }
                    break;
                case 2:
                    chooseToEdit.removeAllItems();
                    chooseToEdit.addItem("Edit event's name");
                    chooseToEdit.addItem("Edit event's date and time");
                    chooseToEdit.addItem("Edit alert's message");
                    chooseToEdit.addItem("Edit alert's date and time");
                    input_Choose = chooseToEdit.getSelectedIndex();

                    switch (input_Choose+1){
                        case 1:
                            calendarManager.getEventManager().editName(((Event)eventsReturnedBySearch.getSelectedValue()).getID(),textEditName.getText());
                            break;
                        case 2:
                            calendarManager.getEventManager().editDate(((Event)eventsReturnedBySearch.getSelectedValue()).getID(),parse_date_time(textEditName.getText()),parse_date_time(textEditStartTime.getText()));
                            break;
                        case 3:
                            calendarManager.getEventManager().editAlertMessage(((Event)eventsReturnedBySearch.getSelectedValue()).getID(),textEditName.getText());
                            break;
                        case 4:
                            calendarManager.getEventManager().editTime(((Event)eventsReturnedBySearch.getSelectedValue()).getID(),parse_date_time(textEditName.getText()));
                            break;
                    }
                    break;
                case 3:
                    chooseToEdit.removeAllItems();
                    chooseToEdit.addItem("Series");
                    chooseToEdit.addItem("Tag");
                    chooseToEdit.addItem("Memo");
                    chooseToEdit.addItem("Alert");
                    input_Choose = chooseToEdit.getSelectedIndex();

                    switch (input_Choose+1){
                        case 1:
                            ((Event)eventsReturnedBySearch.getSelectedValue()).deleteSeries(textEditName.getText());
                            break;
                        case 2:
                            ((Event)eventsReturnedBySearch.getSelectedValue()).deleteTag(textEditName.getText());
                            break;
                        case 3:
                            ((Event)eventsReturnedBySearch.getSelectedValue()).removeMemo();
                            break;
                        case 4:
                            calendarManager.getEventManager().deleteAlert(parse_date_time(textEditName.getText()));
                            break;
                    }
                    break;
            }
        }
    }

    class chooseToEdit_Action implements ActionListener{
        public void actionPerformed(ActionEvent e){
            int input_Choose_Functions = chooseToEditMessage.getSelectedIndex();
            switch (input_Choose_Functions + 1){
                case 1:int input_Choose = chooseToEdit.getSelectedIndex();
                    switch (input_Choose+1){
                        case 1:
                            textEditName.setVisible(true);
                            editStartTimeMessage.setVisible(true);
                            textEditStartTime.setVisible(true);
                            editEndTimeMessage.setVisible(true);
                            textEditEndTime.setVisible(true);
                            editFrequencyMessage.setVisible(true);
                            textEditFrequency.setVisible(true);
                            editNumberOfEventsMessage.setVisible(true);
                            textEditNumberOfEvents.setVisible(true);
                            editNameMessage.setText("Enter the series name:");
                            break;
                        case 2:
                            textEditName.setVisible(true);
                            editStartTimeMessage.setVisible(false);
                            textEditStartTime.setVisible(false);
                            editEndTimeMessage.setVisible(false);
                            textEditEndTime.setVisible(false);
                            editFrequencyMessage.setVisible(false);
                            textEditFrequency.setVisible(false);
                            editNumberOfEventsMessage.setVisible(false);
                            textEditNumberOfEvents.setVisible(false);
                            editNameMessage.setText("Enter the tag name:");
                            break;
                        case 3:
                            textEditName.setVisible(true);
                            editStartTimeMessage.setVisible(false);
                            textEditStartTime.setVisible(false);
                            editEndTimeMessage.setVisible(false);
                            textEditEndTime.setVisible(false);
                            editFrequencyMessage.setVisible(false);
                            textEditFrequency.setVisible(false);
                            editNumberOfEventsMessage.setVisible(false);
                            textEditNumberOfEvents.setVisible(false);
                            editNameMessage.setText("Enter the memo:");
                            break;
                        case 4:
                            textEditName.setVisible(true);
                            editStartTimeMessage.setVisible(true);
                            textEditStartTime.setVisible(true);
                            editEndTimeMessage.setVisible(false);
                            textEditEndTime.setVisible(false);
                            editFrequencyMessage.setVisible(true);
                            textEditFrequency.setVisible(true);
                            editNumberOfEventsMessage.setVisible(false);
                            textEditNumberOfEvents.setVisible(false);
                            editStartTimeMessage.setText("<html>Please enter the time and day,<br>format\"Year-Month-Day-hour-minute\"." +
                                    "<br>For Example, \"2001-01-01-20-15\"<html>");
                            editNameMessage.setText("Enter the alert message:");
                            editFrequencyMessage.setText("<html>Please enter the frequency of the Alert.<br> For example, 1 means there will be an alert every 1 hour.<html>");
                    }
                    break;
                case 2:
                    input_Choose = chooseToEdit.getSelectedIndex();
                    switch (input_Choose+1){
                        case 1:
                            textEditName.setVisible(true);
                            editStartTimeMessage.setVisible(false);
                            textEditStartTime.setVisible(false);
                            editEndTimeMessage.setVisible(false);
                            textEditEndTime.setVisible(false);
                            editFrequencyMessage.setVisible(false);
                            textEditFrequency.setVisible(false);
                            editNumberOfEventsMessage.setVisible(false);
                            textEditNumberOfEvents.setVisible(false);
                            editNameMessage.setText("Enter the event name you want to change:");
                            break;
                        case 2:
                            textEditName.setVisible(true);
                            editStartTimeMessage.setVisible(true);
                            textEditStartTime.setVisible(true);
                            editEndTimeMessage.setVisible(false);
                            textEditEndTime.setVisible(false);
                            editFrequencyMessage.setVisible(false);
                            textEditFrequency.setVisible(false);
                            editNumberOfEventsMessage.setVisible(false);
                            textEditNumberOfEvents.setVisible(false);
                            editNameMessage.setText("<html>Please enter the start time you want to change to event,format\"Year-Month-Day-hour-minute\"." +
                                    "<br>For Example, \"2001-01-01-20-15\"<html>");
                            editStartTimeMessage.setText("<html>Please enter the end time you want to change to event, format\"Year-Month-Day-hour-minute\"." +
                                    "<br>For Example, \"2001-01-01-20-15\"<html>");
                            break;
                        case 3:
                            textEditName.setVisible(true);
                            editStartTimeMessage.setVisible(false);
                            textEditStartTime.setVisible(false);
                            editEndTimeMessage.setVisible(false);
                            textEditEndTime.setVisible(false);
                            editFrequencyMessage.setVisible(false);
                            textEditFrequency.setVisible(false);
                            editNumberOfEventsMessage.setVisible(false);
                            textEditNumberOfEvents.setVisible(false);
                            editNameMessage.setText("Enter the message you want to change to:");
                            break;
                        case 4:
                            textEditName.setVisible(true);
                            editStartTimeMessage.setVisible(false);
                            textEditStartTime.setVisible(false);
                            editEndTimeMessage.setVisible(false);
                            textEditEndTime.setVisible(false);
                            editFrequencyMessage.setVisible(false);
                            textEditFrequency.setVisible(false);
                            editNumberOfEventsMessage.setVisible(false);
                            textEditNumberOfEvents.setVisible(false);
                            editNameMessage.setText("<html>Please enter the date time you want to change to alert,format\"Year-Month-Day-hour-minute\"." +
                                    "<br>For Example, \"2001-01-01-20-15\"<html>");
                    }
                    break;
                case 3:
                    input_Choose = chooseToEdit.getSelectedIndex();
                    switch (input_Choose+1){
                        case 1:
                            textEditName.setVisible(true);
                            editStartTimeMessage.setVisible(false);
                            textEditStartTime.setVisible(false);
                            editEndTimeMessage.setVisible(false);
                            textEditEndTime.setVisible(false);
                            editFrequencyMessage.setVisible(false);
                            textEditFrequency.setVisible(false);
                            editNumberOfEventsMessage.setVisible(false);
                            textEditNumberOfEvents.setVisible(false);
                            editNameMessage.setText("Enter the series name to delete:");
                            break;
                        case 2:
                            textEditName.setVisible(true);
                            editStartTimeMessage.setVisible(false);
                            textEditStartTime.setVisible(false);
                            editEndTimeMessage.setVisible(false);
                            textEditEndTime.setVisible(false);
                            editFrequencyMessage.setVisible(false);
                            textEditFrequency.setVisible(false);
                            editNumberOfEventsMessage.setVisible(false);
                            textEditNumberOfEvents.setVisible(false);
                            editNameMessage.setText("Enter the tag name to delete:");
                            break;
                        case 3:
                            textEditName.setVisible(false);
                            editStartTimeMessage.setVisible(false);
                            textEditStartTime.setVisible(false);
                            editEndTimeMessage.setVisible(false);
                            textEditEndTime.setVisible(false);
                            editFrequencyMessage.setVisible(false);
                            textEditFrequency.setVisible(false);
                            editNumberOfEventsMessage.setVisible(false);
                            textEditNumberOfEvents.setVisible(false);
                            editNameMessage.setText("");
                            break;
                        case 4:
                            textEditName.setVisible(true);
                            editStartTimeMessage.setVisible(false);
                            textEditStartTime.setVisible(false);
                            editEndTimeMessage.setVisible(false);
                            textEditEndTime.setVisible(false);
                            editFrequencyMessage.setVisible(false);
                            textEditFrequency.setVisible(false);
                            editNumberOfEventsMessage.setVisible(false);
                            textEditNumberOfEvents.setVisible(false);
                            editNameMessage.setText("Enter the alert time to delete:");
                            break;
                    }
                    break;
            }
        }
    }

    class editThisEvent_Action implements ActionListener{
        public void actionPerformed(ActionEvent e){
            if(!eventsReturnedBySearch.isSelectionEmpty()){
                frmEditFunction.setVisible(true);
                viewAllContent.setText("<html>Event's name: " +((Event)eventsReturnedBySearch.getSelectedValue()).getName() + "<html>");
            }
        }
    }

    class addNewFeature_Action implements ActionListener{
        public void actionPerformed(ActionEvent e){
            if(!eventsReturnedBySearch.isSelectionEmpty()){
                calendarManager.deleteEvent(((Event)eventsReturnedBySearch.getSelectedValue()));
            }
        }
    }
    class Postone_Action implements ActionListener{
        public void actionPerformed(ActionEvent e){
            if(!eventsReturnedBySearch.isSelectionEmpty()){
                System.out.println("Sorry, did not implement...");//TODO
            }
        }
    }

    class share_Action implements ActionListener{
        public void actionPerformed(ActionEvent e){
            if(!eventsReturnedBySearch.isSelectionEmpty()){
                System.out.println("Sorry, did not implement..."); //TODO
            }
        }
    }

    class MyRenderer extends DefaultTableCellRenderer
    {
        public Component getTableCellRendererComponent(JTable table, Object value, boolean   isSelected, boolean hasFocus, int row, int column)
        {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if(row == 0 && column == 0)
                    c.setBackground(new java.awt.Color(0, 0, 255));
                else
                    c.setBackground(table.getBackground());

            return c;
        }
    }
}