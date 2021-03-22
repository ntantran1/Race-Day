/*
 * TCSS 305 – Assignment 4.
 * Autumn 2019.
 */
package model;


import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import view.Utilities;




/**
 * A model of the race that set up then race.
 * @author Nhan Tran
 * @version Autumn 2019
 */
public class RaceModel implements PropertyChangeEnabledRaceControls {
    
    /** An error message for illegal arguments. */
    private static final String ERROR_MESSAGE = "Time may not be less than 0.";
    
    
    /** An error message for illegal arguments. */
    private static final String SEMI = ":";
    
    /** Stores this objects time. */
    private int myTime;
    
    /** Change support. */
    private final PropertyChangeSupport myPcs;
    
    /** The number of Participants. */
    private int myNumOfParticipants;
    
    /** A list that stores all the participants. */
    private final List<Participants> myParticipant = new ArrayList<Participants>();
    
    /** A list that stores all the participants. */
    private final Map<Integer, Boolean> myRacer = new HashMap<Integer, Boolean>();
    
    /** A Map that store the messages. */
    private final List<List<Message>> myMessage = new ArrayList<List<Message>>();
    
    /** The headers. */
    private final List<String> myHeader = new ArrayList<String>();
    
    /** Race Size. */
    private final List<Integer> myRaceSize = new ArrayList<Integer>();
    
    /** Race distance. */
    private int myDistance;
    
    /** Race info. */
    private String myRaceInfo;

    /**
     * Construct an Observable time object with a start time of 0.  
     */
    public RaceModel() {
        
        final int h = 7;
        myNumOfParticipants = h;
        myPcs = new PropertyChangeSupport(this);
        myTime = 0;
    }
    
    

    @Override
    public void advance() {
        // TODO Auto-generated method stub
        advance(1);
        
    }

    @Override
    public void advance(final int theMillisecond) {
        // TODO Auto-generated method stub
        if (theMillisecond < 0) {
            throw new IllegalArgumentException(ERROR_MESSAGE);
        }
        checkMessgae(myTime + theMillisecond, theMillisecond); 
        changeTime(myTime + theMillisecond);
     
    }
        
    

    @Override
    public void moveTo(final int theMillisecond) {
        // TODO Auto-generated method stub
        final int thirtyOne = 31;
        if (theMillisecond < 0) {
            throw new IllegalArgumentException(ERROR_MESSAGE);
        }
        
        if (!myMessage.isEmpty()) {
            
            checkMessgae(theMillisecond, thirtyOne);
        

            changeTime(theMillisecond);
        }
    }
        
    

    @Override
    public void toggleParticipant(final int theParticpantID, final boolean theToggle) {
        // TODO Auto-generated method stub
        if (myRacer.containsKey(theParticpantID)) {
            myRacer.put(theParticpantID, theToggle);
        }
    }
    
    /**
     * Helper method to change the value of time and notify observers. 
     * Functional decomposition. 
     * @param theMillisecond the time to change to
     */
    private void changeTime(final int theMillisecond) {
        final int old = myTime;
        myTime = theMillisecond;
        myPcs.firePropertyChange(PROPERTY_TIME, old, myTime);
    }

    @Override
    public void loadRace(final File theRaceFile) throws IOException {
        myPcs.firePropertyChange(PROPERTY_CLEAR, 1, 1);
        myParticipant.clear();
        myMessage.clear();
        myHeader.clear();
        myRaceSize.clear();
        this.moveTo(0);
        final Scanner input = new Scanner(theRaceFile);
        try {
            checkValid(input);
            int index = 1;
            final int l = 7;
            while (input.hasNextLine()) {
                final String line = input.nextLine();
                final String[] parts = line.split(SEMI);
                
                if (index == l) {

                    setNumOfParticipant(l + Integer.parseInt(parts[1]));
       
                }
                if (line.charAt(0) == '$' || line.charAt(0) == '#')  {
                    parseLine(parts, index, line);
                } else {
                    throw new IOException();
                }
                
                index++;
              
            }
        } finally {
            
            input.close();
            storeRaceSize();
            getHeaders();
            getDistance();
            myPcs.firePropertyChange(PROPERTY_MAX, 1, myMessage.size());
            myPcs.firePropertyChange(PROPERTY_TRACK_SIZE, 1, myDistance);
            myPcs.firePropertyChange(PROPERTY_HEADER, 1, myRaceInfo);
            myPcs.firePropertyChange(PROPERTY_OVAL, myRaceSize.get(0), myRaceSize.get(1));
           
            updateTrack();
            myPcs.firePropertyChange("initial", 1, 
                         ((LeaderboardMessage) myMessage.get(0).get(0)).getLeaderBoard());
            
          
        }
        
    }
    
    /** Method to update the track. */
    private void updateTrack() {

        for (final Participants p : myParticipant) { 
            myPcs.firePropertyChange(PROPERTY_PARTICIPANT, p.getMyID(), p.getMyName());
            myPcs.firePropertyChange(PROPERTY_RACER_INFO, p.getMyID(), p.toString());
            myPcs.firePropertyChange(PROPERTY_PAINT, p.getMyID(), p.getMyStartingDistance());
            
        }
      
       
        
    }
    
    
    /**
     * Helper to parse individual lines. 
     * @param theLine the line to parse
     * @param theIndex the line index
     * @param theL the current line in the file
     * @throws IOException to stop running the file
     */
    private void parseLine(final String [] theLine, final int theIndex, 
                           final String theL) throws IOException {
        final int tL = 5;
        if (theIndex > numOfParticipant()) {
            if (theLine[0].equals("$L") &&  (theLine.length == numOfParticipant() - tL))  {
                readLMessage(theLine);
    
            } else if (theLine[0].equals("$T") &&  theLine.length == tL) {
                readTMessage(theLine);
    
            } else if (theLine[0].equals("$C") && theLine.length == tL) {
                readCMessage(theLine);
    
       
            } else {                
                throw new IOException();
                
                
            }
        } else {
            checkHeader(theIndex, theLine, theL);
        }
                    
    }
    
    /**
     * Method to read the lap line.
     * @param theLine the line in the file
     * @throws IOException to stop running the file
     */
    private void readLMessage(final String [] theLine) 
                    throws IOException {
        final ArrayList<Integer> lB = new ArrayList<Integer>();
        for (int i = 1; i < theLine.length; i++) {
         
            checkForInt(theLine [i]);
            
            if (i > 1) {
                checkRange(Integer.parseInt(theLine [i]));
                lB.add(Integer.parseInt(theLine[i]));
            }
            if (i == theLine.length - 1) {               
                int ind = 0;
                final Integer [] array = new Integer [theLine.length - 2];
                while (ind < array.length) {
                    array [ind] = lB.get(ind);
                    ind++;
                }
                final Message lb = 
                                new LeaderboardMessage(Integer.parseInt(theLine [1]),
                                                                      array);
                
                while (Integer.parseInt(theLine [1]) >= myMessage.size()) {
                    myMessage.add(new ArrayList<Message>());
                } 

                myMessage.get(Integer.parseInt(theLine [1])).add(lb);

                //System.out.println(myMessage.get(2).size());
               
            }

     
                
        }
        
    }
    
    /**
     * Method to read the telemetry line.
     * @param theLine the line in the file
     * @throws IOException to stop running the file
     */
    private void readTMessage(final String [] theLine) 
                    throws IOException {
        final int n = 4;
        final int m = 3;
        for (int i = 1; i < theLine.length; i++) {
            
            if (i == 1 || i == 2 || i == n) {
                checkForInt(theLine [i]);
                checkRange(Integer.parseInt(theLine [2]));
            } 
            
            if (i == m) {
                checkForDouble(theLine [i]);
            }

            if (i == theLine.length - 1) {

                final Telemetry t = new Telemetry(Integer.parseInt(theLine [1]),
                                               Integer.parseInt(theLine [2]),
                                               Double.parseDouble(theLine [m]),
                                               Integer.parseInt(theLine [n]));
               
                
                while (Integer.parseInt(theLine [1]) >= myMessage.size()) {
                    myMessage.add(new ArrayList<Message>());
                } 

                myMessage.get(Integer.parseInt(theLine [1])).add(t);
    

                
               
            }


                
        }
        
    }
    
    /**
     * Method to read the crossing line.
     * @param theLine the line in the file

     * @throws IOException to stop running the file
     */
    private void readCMessage(final String [] theLine) 
                    throws IOException {
        final int size = 3;
        final int n = 4;
        for (int i = 1; i < theLine.length; i++) {
            if (i == 1 || i == 2 || i == size) {
                checkForInt(theLine [i]);
                checkRange(Integer.parseInt(theLine [2]));
            } 
            
            if (i == n) {
                checkForBoolean(theLine [i]);
            }
            if (i == theLine.length - 1) {
     
                final LineCrossingMessage lc = 
                                new LineCrossingMessage(Integer.parseInt(theLine [1]), 
                                                Integer.parseInt(theLine [2]),
                                               Integer.parseInt(theLine [size]),
                                               Boolean.parseBoolean(theLine [n]));
               
              
                while (Integer.parseInt(theLine [1]) >= myMessage.size()) {
                    myMessage.add(new ArrayList<Message>());
                    
                } 
                myMessage.get(Integer.parseInt(theLine [1])).add(lc);


                
            }
      
        }
        
    }
    
    /**
     * A method that check if the file is a readable text file or not.
     * @param theInput the Scanner for the file
     * @throws IOException to stop running the file
     */
    private void checkValid(final Scanner theInput) throws IOException {
        if (!theInput.hasNextLine()) {
            throw new IOException();
        }
    }
    
    /**
     * Method that check for the format of the header.
     * @param theIndex the index of the line in the file
     * @param theParts the string array of each word in the string
     * @param theL the current line in the file
     * @throws IOException to stop running the file
     */
    private void checkHeader(final int theIndex, final String [] theParts, final String theL) 
                                             throws IOException {
       
        final int n = 3;
        final int m = 7;
        if (theIndex == 1 || theIndex == 2
                        && theParts.length == 2) {
            checkForString(theParts [0]);
            checkForString(theParts [1]);
            myHeader.add(theL.substring(1));
          

        } else if (theIndex >= n && theIndex <= m
                        && theParts.length == 2) {
            checkForString(theParts [0]);
            checkForInt(theParts [1]);
            myHeader.add(theL.substring(1));
            
            
      
            
        } else if (theIndex > m && theIndex <= numOfParticipant()) { // Parsing participant
            checkForInt(theParts [0].substring(1));
            checkRange(Integer.parseInt(theParts [0].substring(1)));
            checkForString(theParts [1]);
            checkForDouble(theParts [2]);
            myParticipant.add(new Participants(Integer.parseInt
                                               (theParts [0].substring(1)),
                                                theParts [1],
                                                Double.parseDouble(theParts [2])));
            myRacer.put(Integer.parseInt(theParts [0].substring(1)), true);
            
        }
    }
    
    /** Method to check for range of ID from 0 to 99.
     * @param theID the Id number as string
     * @throws IOException throw exception if out of range
     */
    private void checkRange(final int theID) throws IOException {
        final int max = 100;
        if (theID > max || theID < 0) {
            throw new IOException();
        }
    }
    
    /**
     * The Method that check if the string can be convert to integer or double. 
     * @param theParts the string array of each element in the string
     * @throws IOException to stop running the file
     */
    private void checkForString(final String theParts) throws IOException {
        try {
            Double.parseDouble(theParts);

            throw new IOException();
        } catch (final NumberFormatException e) {

        }
        
    }
    
    /**
     * The Method that check if the string can be convert to integer. 
     * @param theParts the string array of each element in the string
     * @throws IOException to stop running the file
     */
    private void checkForInt(final String theParts) throws IOException {
        try {
            Integer.parseInt(theParts);
        } catch (final NumberFormatException e) {
            
            throw new IOException(e); 
           
        }
       
    }
    
    /**
     * The Method that check if the string can be convert to double. 
     * @param theParts the string array of each element in the string
     * @throws IOException to stop running the file
     */
    private void checkForDouble(final String theParts) throws IOException {
        try {
            Double.parseDouble(theParts);
        } catch (final NumberFormatException e) {
           
            throw new IOException(e);          
        }
    }
    
    /**
     * The Method that check if the string is a boolean. 
     * @param theParts the string array of each element in the string
     * @throws IOException to stop running the file
     */
    private void checkForBoolean(final String theParts) throws IOException {
        if (!"true".equals(theParts) && !"false".equals(theParts)) {
           
            throw new IOException();
        }
    }
    
    /**
     * Method that store the number of participants.
     * @param theP the number that store
     */
    private void setNumOfParticipant(final int theP) {
        myNumOfParticipants = theP;
    }
    
    /**
     * Method that return the number of participants.
     * @return return the number of participants
     */
    private int numOfParticipant() {
        return myNumOfParticipants;
    }

    @Override
    public void addPropertyChangeListener(final PropertyChangeListener theListener) {
        myPcs.addPropertyChangeListener(theListener);
        
    }

    @Override
    public void addPropertyChangeListener(final String thePropertyName,
                                          final PropertyChangeListener theListener) {
        myPcs.addPropertyChangeListener(thePropertyName, theListener);
        
    }

    @Override
    public void removePropertyChangeListener(final PropertyChangeListener theListener) {
        myPcs.removePropertyChangeListener(theListener);
        
    }

    @Override
    public void removePropertyChangeListener(final String thePropertyName,
                                             final PropertyChangeListener theListener) {
        myPcs.removePropertyChangeListener(thePropertyName, theListener);
        
    }
    
    /**
     * Store race size.
     */
    private void storeRaceSize() {
        final int three = 3;
        final String [] width = myHeader.get(2).split(SEMI);
        final String [] height = myHeader.get(three).split(SEMI);
        myRaceSize.add(Integer.parseInt(width[1]));
        myRaceSize.add(Integer.parseInt(height[1]));
    }
    



    
    /**
     * Get the Header.
     */
    private void getHeaders() {
        final String tp = "Track type: ";
        final String time = "Total time: ";
        final String lD = "Lap distance: ";
        final String n = "\n";
        final String space = " ";
        final int five = 5;
        final int four = 4;
    
        final StringBuilder sb = new StringBuilder();
       
        final String [] raceName = myHeader.get(0).split(SEMI);
        final String [] trackType = myHeader.get(1).split(SEMI);
        final String [] distance = myHeader.get(four).split(SEMI);
        final String [] timeInfo = myHeader.get(five).split(SEMI);
        
        sb.append(raceName[1].substring(2, five));
        sb.append(space);
        sb.append(raceName[1].substring(five));
        sb.append(n);
        
        sb.append(tp);
        sb.append(trackType[1]);
        sb.append(n);
        
        sb.append(time);
        sb.append(Utilities.formatTime(Integer.parseInt(timeInfo[1])));
        sb.append(n);
        
        sb.append(lD);
        sb.append(distance[1]);
           
  
        myRaceInfo = sb.toString();
    }
    
    
    /**
     * Method to get the race distance.
    */
    private void getDistance() {
        final int four = 4;
        final String [] distance = myHeader.get(four).split(SEMI);
        myDistance = Integer.parseInt(distance[1]);
    }
    
    /** Method to check the message.
     * 
     * @param theTime the current time
     * @param theFrequency the frequency
     */
    private void checkMessgae(final int theTime, final int theFrequency) {
        if (theTime == 0) {
            for (final Message ms : myMessage.get(0)) {
                updateDistance(ms);
            }
        }
        int c = theFrequency;
        while (c > 0) {
            if (theTime - c >= 0 && theTime - c 
                            < myMessage.size()
                && !myMessage.get
                            (theTime - c).isEmpty()) {
               
                for (final Message m : myMessage.get(theTime - c)) {
                    
                    updateDistance(m);

                }
                
            }  
            c = c - 1;
        }

    } 
    
    /** 
     * Method to update the distance of the racer. 
     * @param theM the current message
    */
    private void updateDistance(final Message theM) {
        if (theM instanceof Telemetry) {       
            final int iD = ((Telemetry) theM).getID();
            if (myRacer.containsKey(iD) && myRacer.get(iD)) {
                for (final Participants p: myParticipant) {
                    if (p.getMyID() == iD) {
                        p.setMyDistance(((Telemetry) theM).getMyTraveledDistance());
                        p.setMyLap(((Telemetry) theM).getMyLapNumber());
                        
                        myPcs.firePropertyChange(PROPERTY_TELEMETRY,
                                                 p.getMyID(), p.getMyStartingDistance());
                        
                       
                        myPcs.firePropertyChange(PROPERTY_MESSAGE, 1, theM.toString());
                        myPcs.firePropertyChange(PROPERTY_RACER_INFO, 
                                                 p.getMyID(), p.toString());
                     
                    }    
                } 
            }
        } else if (theM instanceof LineCrossingMessage) {
            
            updateFinishStatus(theM);
        } else  if (theM instanceof LeaderboardMessage) {
          
            myPcs.firePropertyChange(PROPERTY_LEADERBOARD, 1, 
                                     ((LeaderboardMessage) theM).getLeaderBoard());
            myPcs.firePropertyChange(PROPERTY_MESSAGE, 1, theM.toString());
        }
    }
    
    /** 
     * Method to update finish condition of the racer. 
     * @param theM the current message
    */
    private void updateFinishStatus(final Message theM) {
        final int iD = ((LineCrossingMessage) theM).getID();
        if (myRacer.containsKey(iD) && myRacer.get(iD)) {
            for (final Participants p:  myParticipant) {
                if (p.getMyID() == iD) {
                    p.setMyIsFinish(((LineCrossingMessage) theM).isFinish());
                    p.setMyLap(((LineCrossingMessage) theM).getNewLap());
                    
                    myPcs.firePropertyChange(PROPERTY_MESSAGE, 1, theM.toString());
                    myPcs.firePropertyChange(PROPERTY_RACER_INFO, p.getMyID(), p.toString());
                }
            }
        }
        
    }
    

    
}






