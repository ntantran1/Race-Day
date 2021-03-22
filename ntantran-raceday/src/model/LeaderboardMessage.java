/*
 * TCSS 305 – Assignment 4.
 * Autumn 2019.
 */
package model;


/** A class that store the leaderboards message. 
 *  @author Nhan Tran
 *  @version Autumn 2019
*/
public class LeaderboardMessage extends AbstractMessage {
      
    /** The String list of the leaderboards. */
    private final Integer [] myLeaderBoards;
    
    /**
     * A constructor Leaderboards message.
     * @param theTS the timestamps
     * @param theLB the string array of the leaders
     */
    public LeaderboardMessage(final int theTS, final Integer [] theLB) {
        super(theTS);
        myLeaderBoards = theLB.clone();
    }
    
    /**
     * Method to get the leaderboard.
     * @return the array of leaderBoard
     */
    public Integer [] getLeaderBoard() {
        return myLeaderBoards.clone();
    }
    
    @Override
    public String toString() {
        final String s = ":";
        int i = 0;
        final StringBuilder sb = new StringBuilder();
        sb.append("$L:");
        sb.append(getTimeStamp());
       
        while (i < myLeaderBoards.length) {
            sb.append(s);
            sb.append(myLeaderBoards [i]);
            
            i++;
        }

        return sb.toString();

    }

}
