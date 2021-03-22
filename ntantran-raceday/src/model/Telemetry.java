/*
 * TCSS 305 – Assignment 4.
 * Autumn 2019.
 */
package model;

/** A class that store the telemetry message. 
 *  @author Nhan Tran
 *  @version Autumn 2019
*/
public class Telemetry extends AbstractMessage {
      
    /** The Distance traveled. */
    private final double myTraveledDistance;
    
    /** The lab number. */
    private final int myLapNumber;
    
    /** The ID number of the participant. */
    private final int myID;
    
    /**
     * A constructor Telemetry message.
     * @param theTS the timestamps
     * @param theID the participant ID number
     * @param theDT the distance traveled
     * @param theL the lap number
     */
    public Telemetry(final int theTS, final int theID, final double theDT,
                     final int theL) {
        super(theTS);
        myID = theID;
        myTraveledDistance = theDT;
        myLapNumber = theL;
    }
    
    /** A method to get myTraveledDistance. 
     *  @return myTraveledDistance.
    */
    public double getMyTraveledDistance() {
        return myTraveledDistance;
    }
    
    /** A method to get myLapNumber. 
     *  @return myLapNumber.
    */
    public int getMyLapNumber() {
        return myLapNumber;
    }
    
    /** A method to get Id number. 
     *  @return myID.
    */
    public int getID() {
        return myID;
    }
    
    @Override
    public String toString() {
        final String s = ":";
        final StringBuilder sb = new StringBuilder();
        sb.append("$T:");
        sb.append(getTimeStamp());
        sb.append(s);
        sb.append(myID);
        sb.append(s);
        sb.append(myTraveledDistance);
        sb.append(s);
        sb.append(myLapNumber);
        return sb.toString();
    }


}
