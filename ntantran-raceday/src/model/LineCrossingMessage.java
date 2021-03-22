/*
 * TCSS 305 – Assignment 4.
 * Autumn 2019.
 */
package model;


/** A class that store the finish line crossing message. 
 *  @author Nhan Tran
 *  @version Autumn 2019
*/
public class LineCrossingMessage extends AbstractMessage {
    
    /** The racer ID. */
    private final int myID;
    
    /** The new lap. */
    private final int myNewLap;
    
    /** The boolean if finish. */
    private final boolean myIsFinish;
    
    /**
     * A constructor LineCrossing message.
     * @param theTS the timestamps
     * @param theID the ID number
     * @param theNL the new lap
     * @param theF the boolean value if the finish
     */
    public LineCrossingMessage(final int theTS, final int theID, final int theNL, 
                               final boolean theF) {
        super(theTS);
        myNewLap = theNL;
        myIsFinish = theF;
        myID = theID;
    }
    
    /** A method to get myLeaderBoards. 
     *  @return myTraveledDistance.
    */
    public int getNewLap() {
        return myNewLap;
    }
    
    /** A method to get the boolean for isFinish. 
     *  @return myIsFinish.
    */
    public boolean isFinish() {
        return myIsFinish;
    }
    
    /** A method to get the ID number. 
     *  @return myID.
    */
    public int getID() {
        return myID;
    }

    @Override
    public String toString() {
        final String s = ":";
        final StringBuilder sb = new StringBuilder();
        sb.append("$C:");
        sb.append(getTimeStamp());
        sb.append(s);
        sb.append(myID);
        sb.append(s);
        sb.append(myIsFinish);

        return sb.toString();
    }


}
