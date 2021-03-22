/*
 * TCSS 305 – Assignment 4.
 * Autumn 2019.
 */
package model;

/**
 * Define the actions for the Messgae class. 
 * 
 * @author Nhan Tran
 * @version Autumn 2019
 */
public abstract class AbstractMessage implements Message {

    /** The timeStamps. */
    private final int myTimeStamps;
    
    /** A constructor for AbstractMessage. 
     *  @param theT the timestamps.
    */
    public AbstractMessage(final int theT) {
        myTimeStamps = theT;
    }
    
    @Override
    public int getTimeStamp() {
        return myTimeStamps;
        
    }
    
    @Override 
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(myTimeStamps);
        return sb.toString();
        
    }
    


}
