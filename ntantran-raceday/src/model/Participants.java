/*
 * TCSS 305 – Assignment 4.
 * Autumn 2019.
 */
package model;


/**
 * A class that store Participant informations.
 * @author Nhan Tran
 * @version Autumn 2019
 */
public class Participants {
    
    
    /** The name of the racer. */
    private final String myName;
    
    /** The ID of the racer. */
    private final int myID;
    
    /** The participant starting distance. */
    private double myDistance;
    
    /** The participant starting distance. */
    private int myLap;
    
    /** Checking if the racer is finish. */
    private boolean myIsFinish;
    
    
    
    /**
     * Constructor for participant class.
     * @param theID the ID Number of the participant.
     * @param theName the name of the participant.
     *  @param theDistance the starting distance.
     */
    public Participants(final int theID, final String theName,
                        final double theDistance) {
        myName = theName;
        myID = theID;
        myDistance = theDistance;

        
    }
    
    /** 
     * Method to get starting distance.
     * @return myStartingDistance
     */
    public double getMyStartingDistance() {
        return myDistance;
    }
    
    /** 
     * Method to set distance.
     * @param theD the current distance
     */
    public void setMyDistance(final double theD) {
        myDistance = theD;
    }
    
    /** 
     * Method to get id number.
     * @return myID
     */
    public int getMyID() {
        return myID;
    }
    
    /** 
     * Method to get the name of the racer.
     * @return myName
     */
    public String getMyName() {
        return myName;
    }
    
    /** 
     * Method to get current lap.
     * @return myLap
     */
    public int getMyLap() {
        return myLap;
    }
    
    /** 
     * Method to set the lap of the racer.
     * @param theLap the lap number
     */
    public void setMyLap(final int theLap) {
        this.myLap = theLap;
    }
    
    /**
     * Method getting finish race condition.
     * @return the finish race condition
     */
    public boolean isMyIsFinish() {
        return myIsFinish;
    }

    /**
     * Method setting finish race condition.
     * @param theIsFinish the finish race condition
     */
    public void setMyIsFinish(final boolean theIsFinish) {
        this.myIsFinish = theIsFinish;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        final String s = "- - - - - - -";
        final String a = "#" + myID + "  ";
        final String b = s + myName + s;
        final String c = " Lap: " + myLap;
        final String d = " - Distance: " + myDistance;
        sb.append(a);
        sb.append(b);
        sb.append(c);
        sb.append(d);
 
        return sb.toString();
    }

}
