/*
 * TCSS 305 – Assignment 4.
 * Autumn 2019.
 */
package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;


/**
 * A class for leader board pane.
 * @author Nhan Tran
 * @version Autumn 2019
 */
public class LeaderBoard  extends JPanel implements PropertyChangeListener {
    
    /** The serialization ID. */
    private static final long serialVersionUID = 1L;
    


    /** The size of the leader board panel. */
    private static final Dimension BOARD_SIZE = new Dimension(200, 0);
    
    /** String that hold Participant. */
    private static final String P = "Participant :     ";
    
    /** map that store the racer in the panel. */
    private final Map<Integer, JLabel> myBoard = new HashMap<Integer, JLabel>();
    
    /** map that store the racer name and id. */
    private final Map<Integer, String> myRacer = new HashMap<Integer, String>();
    
    /** The backing model for the track. */
    private final Track myTrack;
    
    /** Array that store the leader board. */
    private Integer [] myLeader;
    
    /** A Map of racers info. */
    private final Map<Integer, String> myRacerInfo = new HashMap<Integer, String>();
    
    /** The status bar label. */
    private final StatusBar myStausBar;
    
    /** The racer id.*/
    private int myID;
    
    /** THe boolean value. */
    private boolean myValue;
    
    
    /** A constructor that setup the layout. 
     *  @param theTrack the track class
     *  @param theStatus the status bar object
    */
    public LeaderBoard(final Track theTrack, final StatusBar theStatus) {
        super();
        myStausBar = theStatus;
        setPreferredSize(BOARD_SIZE);
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
      
 
       
        myTrack = theTrack;
  
       
    }
    
    /** Am method to set up the component. */
    
    public void setupComponents() {
        final int oneNine = 190;
        final int thirty = 30;
        final int five = 5;
        add(Box.createRigidArea(new Dimension(five, five)));
        for (final Integer n : myRacer.keySet()) {
            
            final String name = myRacer.get(n);
            final int racerID = n;
         
        
            final JLabel racer = new JLabel();
           
           
            racer.setMinimumSize(new Dimension(oneNine, thirty));
            racer.setPreferredSize(new Dimension(oneNine, thirty));
            racer.setMaximumSize(new Dimension(oneNine, thirty));
            racer.setOpaque(true);
            racer.setBackground(Color.lightGray);
            racer.setAlignmentX(CENTER_ALIGNMENT);
            racer.setBorder(new BevelBorder(BevelBorder.RAISED));
            
            racer.setForeground(myTrack.getColor().get(racerID));

            racer.setText("      " + racerID + ": " + name);

            racer.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(final MouseEvent theE) { 
                    reduce(racerID);
                }
                @Override
                public void mousePressed(final MouseEvent theE) {     
                }
                @Override
                public void mouseReleased(final MouseEvent theE) {   
                }
                @Override
                public void mouseEntered(final MouseEvent theE) {  
                }
                @Override
                public void mouseExited(final MouseEvent theE) {                    
                }
            });
        
         

            myBoard.put(racerID, racer);
            

            add(myBoard.get(racerID));
            
        }
        
    }
    
    /** Method to reduce inner class. 
     *  @param theID the ID
    */
    private void reduce(final int theID) {
        myTrack.setBoolean();
        myID = theID;
        myValue = true;
        myStausBar.getLabel().setText(P + myRacerInfo.get(theID));
    }
    
    /** Method to set initial array for leaderboard.
     * 
     * @param theL the initial leaderboard 
     */
    public void setInitialLeader(final Integer [] theL) {
        myLeader = theL.clone();
        for (int i = 0; i < myLeader.length; i++) {
            add(myBoard.get(myLeader[i]));
            
            revalidate();

        }
    }
    
    /** Method to set value to false.*/
    public void setBoolean() {
        myValue = false;
    }
    


    
    /** Method to reset everything. */
    public void clear() {
        removeAll();
        myRacer.clear();
        myBoard.clear();
        myValue = false;
       
        revalidate();
        repaint();
        
    }
    

    @Override
    public void propertyChange(final PropertyChangeEvent theEvt) {
        
        if ("telemetry".equals(theEvt.getPropertyName()) && myValue) {           
            myStausBar.getLabel().setText(P + myRacerInfo.get(myID));
            
        }
        
        if ("Racer Info".contentEquals(theEvt.getPropertyName())) {
            myRacerInfo.put((Integer) theEvt.getOldValue(), 
                            (String) theEvt.getNewValue());
        }
       
        if ("leaderboard".contentEquals(theEvt.getPropertyName())) {
            myLeader = (Integer[]) theEvt.getNewValue();
            for (int i = 0; i < myLeader.length; i++) {
                add(myBoard.get(myLeader[i]));
                
                revalidate();

            }
        }
            
        
        if (" Participant".contentEquals(theEvt.getPropertyName())) {
            myRacer.put((Integer) theEvt.getOldValue(), (String) theEvt.getNewValue());

           
            
        }

        
    }

}
