/*
 * TCSS 305 – Assignment 4.
 * Autumn 2019.
 */
package controller; 

import static model.PropertyChangeEnabledRaceControls.PROPERTY_TIME;
import static model.PropertyChangeEnabledRaceControls.PROPERTY_MESSAGE;
import static model.PropertyChangeEnabledRaceControls.PROPERTY_MAX; 
import static model.PropertyChangeEnabledRaceControls.PROPERTY_HEADER;


import application.Main;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.DefaultCaret;
import model.PropertyChangeEnabledRaceControls;
import model.RaceModel;
import view.LeaderBoard;
import view.StatusBar;
import view.Track;
import view.Utilities;

/**
 * The Controller that control the race.
 * 
 * @author Charles Bryan
 * @author Nhan Tran
 * @version Autumn 2019
 */
public class ControllerPanel extends JPanel implements PropertyChangeListener {

    /** A text for the start/stop button. */
    private static final String BUTTON_TEXT_PLAY = "Play";

    /** A text for the clear button. */
    private static final String BUTTON_TEXT_CLEAR = "Clears";
    
    /** A text for the clear property. */
    private static final String PROPERTY_CLEAR = "Clear";
    
    /** A text for the participant property. */
    private static final String PROPERTY_PARTICIPANT = " Participant";
   
    /** A text for the telemetry property. */
    private static final String PROPERTY_TELEMETRY = "telemetry";
    
    /** A text for the initial property. */
    private static final String PROPERTY_INITIAL = "initial";
    
    /** A text for the race info property. */
    private static final String PROPERTY_RACE_INFO = "Racer Info";

    /** A text for the restart button. */
    private static final String BUTTON_TEXT_RESTART = "Restart";

    /** A text for the Times one. */
    private static final String BUTTON_TEXT_TIMESONE = "Times One";
    
    /** A text for the Times one icon. */
    private static final String BUTTON_TEXT_TIMESONEICON = "/ic_one_times.png";
    
    /** A text for the Repeat icon. */
    private static final String BUTTON_TEXT_REPEATICON = "/ic_repeat.png";

    /** A text for the Repeat button. */
    private static final String BUTTON_TEXT_REPEAT = "Single Race";
    
    /** A text for the Race Icon. */
    private static final String ICON_NAME = "/raceIcon.png";
    
    /** A text for new line. */
    private static final String NEW_LINE = "\n";
    
    /** A text for error loading file. */
    private static final String ERROR_MESSAGE = "Error loading file.";
    
    /** A text for error. */
    private static final String ERROR = "Error!";
    
    /** Value for number 10. */
    private static final int TEN = 10;
    
    /** Value for number 50. */
    private static final int FIFTY = 50;
    
    /** 60000. */
    private static final int BIG = 60000;
    
    /** 10000. */
    private static final int SMALL = 10000;
    /** Value for regular multiplier. */
    private static final int SPEED_REGULAR = 1;

    /** Value for fast multiplier. */
    private static final int SPEED_FAST = 4;

    /** The backing model for the track. */
    private static Track track;
    
    /** The backing model for the leaderBoard. */
    private static LeaderBoard leader;
    
    /** The backing model for the status bar. */
    private static StatusBar status;
        
    /** The frame for the race track. */
    private static final JFrame TRACK_VIEW = new JFrame("Race Day");

    /** The serialization ID. */
    private static final long serialVersionUID = -6759410572845422202L;

    /** A reference to the backing Race Model. */
    private final PropertyChangeEnabledRaceControls myRace;

    /** Display of messages coming from the Race Model. */
    private final JTextArea myOutputArea;

    /** Panel to display CheckBoxs for each race Participant. */
    private final JPanel myParticipantPanel;

    /** A view on the race model  that displays the current race time. */
    private final JLabel myTimeLabel;

    /** A controller and view of the Race Model. */
    private final JSlider myTimeSlider;

    /** The list of javax.swing.Actions that make up the ToolBar (Controls) buttons. */
    private final List<Action> myControlActions;

    /** The timer that advances the Race Model. */
    private final Timer myTimer;

    /** Container to hold the different output areas. */
    private final JTabbedPane myTabbedPane;

    /** The Time frequency. */
    private int myTimerFrequency;
    
    /** The race information menu. */
    private final JMenuItem myInfoItem = new JMenuItem("Race Info...");
    
    /** The about information menu. */
    private final JMenuItem myAboutItem = new JMenuItem("About...");
    
    /** The load race menu. */
    private final JMenuItem myLoad = new JMenuItem("Load Race...");

    /** A list of participant. */
    private final Map<Integer, String> myParticipant = new HashMap<Integer, String>();
    
    /** Initial leaderboard. */
    private Integer [] myLeader;
    
    /** The value to check if the race is repeating. */
    private boolean myIsRepeating;
    
    /** The time multiplier. */
    private int myMultiplier;
    
    /** Race info. */
    private String myRaceInfo;
    

    

    /**
     * Construct a ControllerPanel.
     * 
     * @param theRace the backing race model
     */
    public ControllerPanel(final PropertyChangeEnabledRaceControls theRace) {
        super();     
        myOutputArea = new JTextArea(TEN, FIFTY);  
        
       
        myTimeLabel = new JLabel(Utilities.formatTime(0));

        myRace = theRace;
        myTimeSlider = new JSlider(0, 0, 0);
        myControlActions = new ArrayList<>();

        myTabbedPane = new JTabbedPane();
        myParticipantPanel = new JPanel();
        
        reduce();

      
        //TODO This component require Event Handlers 
        myTimer = new Timer(myTimerFrequency, this::handleTimer);

       
    }
    
    /**
     * Method to help reduce the constructor.
     */
    private void reduce() {
        status =  new StatusBar(myTimeSlider);
        track = new Track(status);
        leader = new LeaderBoard(track, status);
        track.addLeader(leader);
        final DefaultCaret c = (DefaultCaret) myOutputArea.getCaret();
        c.setUpdatePolicy(DefaultCaret.OUT_BOTTOM);
        final int thirtyOne = 31;
        myTimerFrequency = thirtyOne;
        buildActions();
        setUpComponents();
        addListeners();
        myMultiplier = 1;
    }
    
    /**
     * Method that advance that time in the race model.
     * @param theEvent the fired event.
     */
    private void handleTimer(final ActionEvent theEvent) { //NOPMD
        myRace.advance(myTimerFrequency * myMultiplier);
    }
    /**
     * Displays a simple JFrame.
     */
    private void setUpComponents() {
        final int ten = 10;
        setLayout(new BorderLayout());

        // JPanel is a useful container for organizing components inside a JFrame
        final JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(ten, ten, ten, ten));

        mainPanel.add(buildSliderPanel(), BorderLayout.NORTH);

        myOutputArea.setEditable(false);


        final JScrollPane scrollPane = new JScrollPane(myOutputArea);
        scrollPane.
        setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);


        final JScrollPane participantScrollPane = new JScrollPane(myParticipantPanel);
        participantScrollPane.setPreferredSize(scrollPane.getSize());



        myTabbedPane.addTab("Data Output Stream", scrollPane);
        myTabbedPane.addTab("Race Participants", participantScrollPane);
        myTabbedPane.setEnabledAt(1, false);



        mainPanel.add(myTabbedPane, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);

        add(buildToolBar(), BorderLayout.SOUTH);

    }

    /**
     * Builds the panel with the time slider and time label.
     * 
     * @return the panel
     */
    private JPanel buildSliderPanel() {
        final int five = 5;
        final int twentyFive = 25;
        //TODO These components require Event Handlers
        final JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(five, five, twentyFive, five));

        myTimeSlider.setBorder(BorderFactory.createEmptyBorder(five, five, five, five));
        myTimeSlider.setEnabled(false);

        panel.add(myTimeSlider, BorderLayout.CENTER);

        myTimeLabel.setBorder(BorderFactory.
                              createCompoundBorder(BorderFactory.createEtchedBorder(),
                             BorderFactory.createEmptyBorder(five, five, five, five)));


        final JPanel padding = new JPanel();
        padding.add(myTimeLabel);
        panel.add(padding, BorderLayout.EAST);

        return panel;
    }



    /**
     * Constructs a JMenuBar for the Frame.
     * @return the Menu Bar
     */
    private JMenuBar buildMenuBar() {
        final JMenuBar bar = new JMenuBar();
        bar.add(buildFileMenu());
        bar.add(buildControlsMenu(myControlActions));
        bar.add(buildHelpMenu());
        return bar;
    }

    /**
     * Builds the file menu for the menu bar.
     * 
     * @return the File menu
     */
    private JMenu buildFileMenu() {
        //TODO These components require Event Handlers

        final JMenu fileMenu = new JMenu("File");


        
        myLoad.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent theE) {
           
                new FileLoader().execute();

            }

        });

        fileMenu.add(myLoad);

        fileMenu.addSeparator();

        final JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent theE) {
                System.exit(0);

            }

        });

        fileMenu.add(exitItem);
        return fileMenu;
    }

    /**
     * Build the Controls JMenu.
     * 
     * @param theActions the Actions needed to add/create the items in this menu
     * @return the Controls JMenu
     */
    private JMenu buildControlsMenu(final List<Action> theActions) {
        final JMenu controlsMenu = new JMenu("Controls");

        for (final Action a : theActions) {
            a.setEnabled(false);
            controlsMenu.add(a);
        }

        return controlsMenu;
    }

    /**
     * Build the Help JMenu.
     * 
     * @return the Help JMenu
     */
    private JMenu buildHelpMenu() {
        //TODO These components require Event Handlers
        final JMenu helpMenu = new JMenu("Help");


        
        myInfoItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent theE) {
                JOptionPane.showMessageDialog(ControllerPanel.this, 
                                     myRaceInfo);

            }

        });
        myInfoItem.setEnabled(false);
        helpMenu.add(myInfoItem);

        myAboutItem.setEnabled(true);
        
        
        myAboutItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent theE) {
                final JOptionPane jop = new JOptionPane("Nhan Tran\nFall 2019\nTCSS 305");
                final JDialog d = jop.createDialog("About Information");
                d.setIconImage(Toolkit.getDefaultToolkit().getImage(getRes(ICON_NAME)));
                d.setVisible(true);
           

            }

        });
        
        helpMenu.add(myAboutItem);
        return helpMenu;
    }

    /**
     * Build the toolbar from the Actions list.
     * 
     * @return the toolbar with buttons for all of the Actions in the list
     */
    private JToolBar buildToolBar() {
        final JToolBar toolBar = new JToolBar();
        for (final Action a : myControlActions) {
            final JButton b = new JButton(a);

            b.setHideActionText(true);

            toolBar.add(b);
        }
        return toolBar;
    }


    /**
     * Add actionListeners to the buttons. 
     */
    private void addListeners() {

        myTimeSlider.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(final ChangeEvent theE) {
                // TODO Auto-generated method stub\
                final JSlider source = (JSlider) theE.getSource();
                if (source.getValueIsAdjusting() && !myTimer.isRunning()) {
                    myRace.moveTo(source.getValue());                    
                    
                } 
                
            }
            
        });

    }

    /**
     * Instantiate and add the Actions.
     */
    private void buildActions() {
        //TODO These components require Event Handlers
        myControlActions.add(new SimpleAction(BUTTON_TEXT_RESTART, "/ic_restart.png"));

        myControlActions.add(new SimpleAction(BUTTON_TEXT_PLAY, "/ic_play.png"));

        myControlActions.add(new SimpleAction(BUTTON_TEXT_TIMESONE,  
                                              BUTTON_TEXT_TIMESONEICON));
        myControlActions.add(new SimpleAction(BUTTON_TEXT_REPEAT, BUTTON_TEXT_REPEATICON));
        myControlActions.add(new SimpleAction(BUTTON_TEXT_CLEAR, "/ic_clear.png"));

    }
    
    /**
     * Wrapper method to get a system resource.
     * 
     * @param theResource the name of the resource to retrieve
     * @return the resource
     */
    private static URL getRes(final String theResource) {
        return Main.class.getResource(theResource);
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    public static void createAndShowGUI() {
        //Create and set up the window.
        final JFrame frame = new JFrame("Race Day!");
        frame.setIconImage(Toolkit.getDefaultToolkit().getImage(getRes(ICON_NAME)));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //TODO instantiate your model here. 
        final PropertyChangeEnabledRaceControls race = new RaceModel();

        //Create and set up the content pane.
        final ControllerPanel pane = new ControllerPanel(race);

        //Add the JMenuBar to the frame:
        frame.setJMenuBar(pane.buildMenuBar());

        pane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(pane);
        
        race.addPropertyChangeListener("leaderboard", leader);
        race.addPropertyChangeListener(PROPERTY_PARTICIPANT, leader);
        race.addPropertyChangeListener(PROPERTY_CLEAR, leader);
        race.addPropertyChangeListener("Oval", track);
        race.addPropertyChangeListener(PROPERTY_TELEMETRY, track);
        race.addPropertyChangeListener(PROPERTY_TELEMETRY, leader);
        race.addPropertyChangeListener("paint racer", track);
        race.addPropertyChangeListener("track size", track);
        race.addPropertyChangeListener("Set up", track);
        race.addPropertyChangeListener(PROPERTY_CLEAR, track);
        race.addPropertyChangeListener(PROPERTY_RACE_INFO, track);
        race.addPropertyChangeListener(PROPERTY_RACE_INFO, leader);
        race.addPropertyChangeListener(PROPERTY_TIME, pane);
        race.addPropertyChangeListener(PROPERTY_TIME, status);
        race.addPropertyChangeListener(PROPERTY_INITIAL, pane);
        race.addPropertyChangeListener("message", pane);
        
        race.addPropertyChangeListener("max", pane);
        
        race.addPropertyChangeListener(PROPERTY_PARTICIPANT, pane);
        race.addPropertyChangeListener("header", pane);
        
        leader.setBackground(Color.WHITE);
        track.setBackground(Color.WHITE);
        track.setBorder(BorderFactory.createTitledBorder("Race Track!"));
        TRACK_VIEW.setIconImage(Toolkit.getDefaultToolkit().getImage(getRes(ICON_NAME)));
        TRACK_VIEW.setResizable(false);
        TRACK_VIEW.add(track, BorderLayout.WEST);
        TRACK_VIEW.add(leader, BorderLayout.EAST);
        TRACK_VIEW.add(status, BorderLayout.SOUTH);
        TRACK_VIEW.pack();
        TRACK_VIEW.setVisible(true);
        

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Method that reset everything.
     */
    private void reset() {
        myRace.moveTo(0);
        myParticipant.clear();
        track.clear();
        leader.clear();
        myTimeLabel.setText(Utilities.formatTime(0));
        myTabbedPane.setEnabledAt(1, false);
        myTimeSlider.setEnabled(false);
        for (final Action a : myControlActions) {

            a.setEnabled(false);

        }
        myOutputArea.setText("");
        myParticipantPanel.removeAll();


    }
    
    @Override
    public void propertyChange(final PropertyChangeEvent theEvt) {
        if (PROPERTY_INITIAL.contentEquals(theEvt.getPropertyName())) {
            myLeader = (Integer[]) theEvt.getNewValue();
        }
        if (PROPERTY_HEADER.equals(theEvt.getPropertyName())) {
            myRaceInfo = (String) theEvt.getNewValue();
            
        }
        if (PROPERTY_PARTICIPANT.equals(theEvt.getPropertyName())) {
            myParticipant.put((Integer) theEvt.getOldValue(), (String) theEvt.getNewValue());
            
        }
        if (PROPERTY_MESSAGE.equals(theEvt.getPropertyName())) {
            myOutputArea.append((String) theEvt.getNewValue() + NEW_LINE);
        }
        
        if (PROPERTY_MAX.equals(theEvt.getPropertyName())) {
            myTimeSlider.setMaximum((Integer) theEvt.getNewValue());
        }
       
        if (PROPERTY_TIME.equals(theEvt.getPropertyName())) {
            myTimeSlider.setValue((Integer) theEvt.getNewValue());
            myTimeLabel.setText(Utilities.formatTime(myTimeSlider.getValue()));
   
            
            int c = myTimerFrequency * myMultiplier;
            while (c > 0) {
                if (((Integer) theEvt.getNewValue()) - c 
                                >= myTimeSlider.getMaximum()) {
                    final String s = "stop";
                    if (myIsRepeating) {
                        myOutputArea.setText("");
                        final ActionEvent stop = new ActionEvent(myTimer, TEN, s);
                        myControlActions.get(0).actionPerformed(stop);

                    } else {                       
                        final ActionEvent stop = new ActionEvent(myTimer, TEN, s);
                        myControlActions.get(1).actionPerformed(stop);
                   
                    }
           
                }
                c = c - 1;
            }

        } 
    }
    

    /**
     * This is a simple implementation of an Action.
     * You will most likely not use this implementation in your final solution. Either
     * create your own Actions or alter this to suit the requirements for this assignment. 
     * 
     * @author Charles Bryan
     * @version Autumn 2019
     */
    private class SimpleAction extends AbstractAction {

        /** The serialization ID. */
        private static final long serialVersionUID = -3160383376683650991L;

        /** The button's name. */
        private final String myT;

        /** The button's Icon. */
        private final String myIC;

        /**
         * Constructs a SimpleAction.
         * 
         * @param theText the text to display on this Action
         * @param theIcon the icon to display on this Action
         */
        SimpleAction(final String theText, final String theIcon) {
            super(theText);
            myT = theText;
            myIC = theIcon;
            setIcon(myIC);

        }



        @Override
        public void actionPerformed(final ActionEvent theEvent) {
            if (myT.contentEquals(BUTTON_TEXT_PLAY)) {
                checkTimer();
            } else if (myT.contentEquals(BUTTON_TEXT_CLEAR)) {
                myOutputArea.setText("");
            } else if (myT.contentEquals(BUTTON_TEXT_RESTART)) {
                myTimeLabel.setText(Utilities.formatTime(0));
                myRace.moveTo(0);
               
            } else if (myT.contentEquals(BUTTON_TEXT_TIMESONE) 
                            && myMultiplier == SPEED_REGULAR) {
               
                myMultiplier = SPEED_FAST; 
                setIcon("/ic_four_times.png");              
                putValue(NAME, "Times Four");

            } else if (myT.contentEquals(BUTTON_TEXT_TIMESONE) 
                            && myMultiplier == SPEED_FAST)  {
               
                myMultiplier = SPEED_REGULAR;
                setIcon(BUTTON_TEXT_TIMESONEICON);
                putValue(NAME, BUTTON_TEXT_TIMESONE);
                
            } else {
                checkRepeating();  
            } 
        }
        
        /** Method to check if the timer is running or not. */
        private void checkRepeating() {
            if (myT.contentEquals(BUTTON_TEXT_REPEAT) && !myIsRepeating) {
                setIcon("/ic_repeat_color.png");
                myIsRepeating = true;
                putValue(NAME, "Loop Race");
              
            } else if (myT.contentEquals(BUTTON_TEXT_REPEAT) && myIsRepeating) {
                myIsRepeating = false;
                setIcon(BUTTON_TEXT_REPEATICON);
                putValue(NAME, BUTTON_TEXT_REPEAT);
                
            }
        }

        /** Method to check if the timer is running or not. */
      
        @SuppressWarnings("deprecation")
        private void checkTimer() {
            if (myTimer.isRunning()) {
                myTimer.stop();
                myLoad.setEnabled(true);
                myTimeSlider.enable(true);
                setIcon(myIC);
                putValue(NAME, "Start");
                return;
            } 
            
            if (!myTimer.isRunning() && myTimeSlider.getValue() 
                            < myTimeSlider.getMaximum()) {
                myTimer.start();
                myLoad.setEnabled(false);
                myTimeSlider.enable(false);
                setIcon("/ic_pause.png");
                putValue(NAME, "Pause");
            }
        }


        /**
         * Helper to size and set the Icon for the button. 
         * @param theFile the icon file
         */
        private void setIcon(final String theFile) {
            // small icons are usually assigned to the menu
            ImageIcon icon = (ImageIcon) new ImageIcon(getRes(theFile));
            final Image smallImage = icon.getImage().
                            getScaledInstance(16, -1, java.awt.Image.SCALE_SMOOTH);
            final ImageIcon smallIcon = new ImageIcon(smallImage);
            putValue(Action.SMALL_ICON, smallIcon);

            // Here is how to assign a larger icon to the tool bar.
            icon = (ImageIcon) new ImageIcon(getRes(theFile));
            final Image largeImage = icon.getImage().
                            getScaledInstance(24, -1, java.awt.Image.SCALE_SMOOTH);
            final ImageIcon largeIcon = new ImageIcon(largeImage);
            putValue(Action.LARGE_ICON_KEY, largeIcon);
            
        }


    }


    /**
     * A worker thread to load the files.
     * 
     * @author Charles Bryan
     * @version Autumn 2019
     */
    private class FileLoader extends SwingWorker<Boolean, Void> {

        @Override
        public Boolean doInBackground() {
            boolean result = true;
            ControllerPanel.this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            final JFileChooser chooser = new JFileChooser(".");
            final int choice = chooser.showOpenDialog(ControllerPanel.this);

            if (choice == JFileChooser.APPROVE_OPTION) {
                try {
                   
                    reset();
                    myInfoItem.setEnabled(false);
                    myAboutItem.setEnabled(false);

                    myOutputArea.setText("Loading file: Start - this may take a while "
                                    + "please be paitient.");

                    myRace.loadRace(chooser.getSelectedFile());
                    for (final Integer a :  myParticipant.keySet()) {
                        
                        final JCheckBox enableEditBtton;
                        
                        enableEditBtton = new JCheckBox(myParticipant.get(a));
                        enableEditBtton.setSelected(true);
                        
                        enableEditBtton.addActionListener(new ActionListener() {

                            @Override
                            public void actionPerformed(final ActionEvent theE) {
                                // TODO Auto-generated method stub
                       
                                    if (myParticipant.get(a).equals
                                                    (enableEditBtton.getText())) {
                                        if (enableEditBtton.isSelected()) {
                                            myRace.toggleParticipant(a, true);
                                            
                                        } else {
                                            myRace.toggleParticipant(a, false);   
                                        }
                                    }
                                
                            }
                            
                        });
                        myParticipantPanel.setLayout(new BoxLayout(myParticipantPanel, 
                                                                   BoxLayout.PAGE_AXIS));
                        myParticipantPanel.add(enableEditBtton);
                    }
                    
                    for (final Action b : myControlActions) {

                        b.setEnabled(true);

                    }
                    if (!myTimer.isRunning()) {
                        myTimeSlider.setEnabled(true);
                    }
                   
                    myTabbedPane.setEnabledAt(1, true);
                    
                    myInfoItem.setEnabled(true);

                    
                    myAboutItem.setEnabled(true);
                    myTimeSlider.setMinimum(0);
            
                    
                    myTimeSlider.setMajorTickSpacing(BIG);
                    myTimeSlider.setMajorTickSpacing(SMALL);
                    myTimeSlider.setPaintTicks(true);
                    
                    myOutputArea.append("\nLoad file Complete!\n");
                    
                    track.setupComponents();
                    leader.setupComponents();
                    leader.setInitialLeader(myLeader);
                    TRACK_VIEW.repaint();
                    TRACK_VIEW.revalidate();
                    myRace.addPropertyChangeListener(PROPERTY_TIME,
                                                   track);

                 
                   
                } catch (final IOException exception) {
                    myTimeSlider.setPaintTicks(false);
                                 
                    JOptionPane.showMessageDialog(ControllerPanel.this, ERROR_MESSAGE,
                                                  ERROR, JOptionPane.ERROR_MESSAGE);
                    result = false;
                }
            }
            return result;
        }

        @Override
        public void done() { 
            
           
        
            ControllerPanel.this.setCursor(Cursor.getDefaultCursor());
            try {
                final boolean resultOfFileLoad = get();

                if (!resultOfFileLoad) {
                    get();
                }


                /*
                 * Do something with the result of reading the file. 
                 */

            } catch (final InterruptedException ex1) {
                                                  
            } catch (final ExecutionException ex2) {
                
                myTimeSlider.setPaintTicks(false);
                
                JOptionPane.showMessageDialog(ControllerPanel.this, 
                                              ERROR_MESSAGE,
                                              ERROR, JOptionPane.ERROR_MESSAGE);
            }

        }

    }

}
