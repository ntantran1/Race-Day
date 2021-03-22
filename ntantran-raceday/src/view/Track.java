/*
 * TCSS 305 – Assignment 4.
 * Autumn 2019.
 */
package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javax.swing.JPanel;
import track.VisibleRaceTrack;

/**
 * A Class that set up a track view for the race.
 * @author Nhan Tran
 * @version Autumn 2019
 */
public class Track extends JPanel implements PropertyChangeListener, MouseListener {
    
    /** The serialization ID. */
    private static final long serialVersionUID = 8385732728740430466L;
    
    /** The size of the Race Track Panel. */
    private static final Dimension TRACK_SIZE = new Dimension(500, 400);

    
   
    
    /** The x and y location of the Track. */
    private static final int OFF_SET = 40;

    /** The stroke width in pixels. */
    private static final int STROKE_WIDTH = 25;

    /** The size of participants moving around the track. */
    private static final int OVAL_SIZE = 20;
    
    /** Random generator for random number. */
    private static final Random R = new Random();
    
    /** String that hold Participant. */
    private static final String P = "Participant :     ";
    
    /** A variable for a color. */
    private static Color myColor;
    
    /** A variable for a width. */
    private static final double WIDTH = 5;
    
    /** A variable for height. */
    private double myHeight;
    
    /** The visible track. */
    private VisibleRaceTrack myTrack;
    
    /** The racer id.*/
    private int myID;
    
    /** THe boolean value. */
    private boolean myValue;
    
    /** The backing model for the leaderBoard. */
    private LeaderBoard myLeader;
    
    

    
    /** A Map of racers icon. */
    private final Map<Integer, Ellipse2D> myRacers;
    
    /** A Map of racers info. */
    private final Map<Integer, String> myRacerInfo = new HashMap<Integer, String>();
    
    
    /** A Map of racers colors. */
    private final Map<Integer, Color> myColors = new HashMap<Integer, Color>();
    
    
    /** A Map of distances. */
    private final Map<Integer, Double> myDistance = new HashMap<Integer, Double>();
    
    /** The size of the track. */
    private int mySize;
    
    /** The status bar label. */
    private final StatusBar myStausBar;
    
    
    
    /** A constructor for Track. 
     * @param theStatus the status bar
    */
    public Track(final StatusBar theStatus) {
        super();
        myStausBar = theStatus;
        addMouseListener(this);
        setPreferredSize(TRACK_SIZE);
        myRacers = new HashMap<Integer, Ellipse2D>();
 
    }
    
    /**
     * Setup and layout components. 
     */
    public void setupComponents() {
 
        final int width = (int) TRACK_SIZE.getWidth() - (OFF_SET * 2);
        final int height =
                        (int) (((int) TRACK_SIZE.getWidth()  - 2 * OFF_SET) 
                                        / WIDTH * myHeight);
        final int x = OFF_SET;
        final int y = (int) TRACK_SIZE.getHeight()  / 2 - height / 2;

        myTrack = new VisibleRaceTrack(x, y, width, height, mySize);
     
     
        Ellipse2D circle;
        for (final Integer p : myDistance.keySet()) {
            final Point2D start = myTrack.getPointAtDistance(myDistance.get(p));
            circle = new Ellipse2D.Double(start.getX() - OVAL_SIZE / 2,
                                            start.getY() - OVAL_SIZE / 2,
                                            OVAL_SIZE,
                                            OVAL_SIZE);
           
            myRacers.put(p, circle);
            myColors.put(p, getMyColor());
      
        }
    
    }
    
    /**
     * Paints the VisibleTrack with a single ellipse moving around it.
     * 
     * @param theGraphics The graphics context to use for painting.
     */
    @Override
    public void paintComponent(final Graphics theGraphics) {
        super.paintComponent(theGraphics);
        final Graphics2D g2d = (Graphics2D) theGraphics;

        // for better graphics display
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                             RenderingHints.VALUE_ANTIALIAS_ON);

        if (myTrack != null) {
            g2d.setPaint(Color.BLACK);
            g2d.setStroke(new BasicStroke(STROKE_WIDTH));
            g2d.draw(myTrack);
        }
        
        if (!myRacers.isEmpty()) {
            for (final Integer p : myRacers.keySet()) {
                g2d.setPaint(myColors.get(p));
                g2d.setStroke(new BasicStroke(1));
                
                g2d.fill(myRacers.get(p));
                
                
            }
            
        }

    }
    
    /** Method to set value to false.*/
    public void setBoolean() {
        myValue = false;
    }
    
    /** Method to set value to false.
     * @param theL the Leader Board    
    */
    public void addLeader(final LeaderBoard theL) {
        myLeader = theL;
    }
    

    @Override
    public void mouseClicked(final MouseEvent theE) {
        for (final Integer p : myRacers.keySet()) {
            if (myRacers.get(p).contains(theE.getPoint())) {
                myLeader.setBoolean();
                myStausBar.getLabel().setText(P + myRacerInfo.get(p));
                myID = p;
                myValue = true;
            } 
        }
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

    
    
    /**
     * A method that create a random color.
     */
    private void setMyColor() {
        final int red = R.nextInt(256);
        final int blue = R.nextInt(256);
        final int green = R.nextInt(256);
   
        myColor =  new Color(red, blue, green); 
        
    }
    
    
    /**
     * A method that create a random color.
     * @return the color.
     */
    private Color getMyColor() {
        setMyColor();
      
        return myColor;
        
    }
    
    /**
     * Method to get the colors.
     * @return the map of colors
     */
    public Map<Integer, Color> getColor() {
        return  myColors;
    }
    

    @Override
    public void propertyChange(final PropertyChangeEvent theEvt) {        
        if ("Racer Info".contentEquals(theEvt.getPropertyName())) {
            myRacerInfo.put((Integer) theEvt.getOldValue(), 
                            (String) theEvt.getNewValue());
        }
  
        if ("telemetry".equals(theEvt.getPropertyName())) {
            if (myValue) {
                myStausBar.getLabel().setText(P + myRacerInfo.get(myID));
            }
  
            final Point2D traveledDistance = myTrack.getPointAtDistance
                            ((Double) theEvt.getNewValue());
            
            myRacers.get((Integer) theEvt.getOldValue()).
                        setFrame(traveledDistance.getX() -  OVAL_SIZE / 2, 
                                traveledDistance.getY() - OVAL_SIZE / 2, 
                                                          OVAL_SIZE, 
                                                          OVAL_SIZE);    
            repaint();
        }
        
        if ("paint racer".equals(theEvt.getPropertyName())) {
            myDistance.put((Integer) (theEvt.getOldValue()), (Double) (theEvt.getNewValue()));
        }
        
        if ("track size".equals(theEvt.getPropertyName())) {
            mySize = (Integer) theEvt.getNewValue();
        }
        
        if ("Set up".contentEquals(theEvt.getPropertyName())) {
            setupComponents();
        }
        
        if ("Oval".contentEquals(theEvt.getPropertyName())) {
            
            final int ratio = (Integer) theEvt.getOldValue()
                            / (Integer) theEvt.getNewValue();
            checkFormat(ratio);
        }
      
        
    }
    
   
    
    /** A method to check the size ratio of the track.
     *  @param theRatio the size track ratio
    */
    private void checkFormat(final int theRatio) {
        final double four = 4.0;
       
        
        myHeight = WIDTH / theRatio;
        if (myHeight < 2.0) {
            myHeight = 2.0;
        } else if (myHeight > four) {
            myHeight = four;
        }
    }

    

    /** Method to clear out the circle map.  */
    public void clear() {
        myStausBar.getLabel().setText("Participant :");
        myDistance.clear();
        myRacers.clear();
        myValue = false;
    }

}
