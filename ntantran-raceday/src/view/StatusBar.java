/*
 * TCSS 305 – Assignment 4.
 * Autumn 2019.
 */
package view;



import java.awt.BorderLayout;
import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

/**
 * A class that create a status bar panel.
 * @author Nhan Tran
 * @version Autumm 2019
 */
public class StatusBar extends JPanel implements PropertyChangeListener {
    
    /** The serialization ID. */
    private static final long serialVersionUID = 1L;
    
    /** The string for Time: .*/
    private  static final String TIME = "Time: ";
    
    /** The label to display the time. */
    private final JLabel myTimeLabel;
    
    /** A time slider. */
    private final JSlider myTimeSlider;
    
    /** A participant label. */
    private final JLabel myParticipantLabel;
    
    
   
    
    /** A Constructor for the status bar to set the initial display. 
     *  @param theSlider the slider from the controller
    */
    public StatusBar(final JSlider theSlider) {
        super();
        myParticipantLabel = new JLabel("Participants: ");
        myTimeSlider = theSlider;
        myTimeLabel = new JLabel(TIME + Utilities.formatTime(0));
        setBackground(Color.LIGHT_GRAY);
        setUpComponent();
        
    }
    
    /**
     * A method to set up the components for the label.
     */
    private void setUpComponent() {

        
        setLayout(new BorderLayout());
        
        myParticipantLabel.setForeground(Color.BLACK);
        myTimeLabel.setForeground(Color.BLACK);
        add(myTimeLabel, BorderLayout.EAST);
        add(myParticipantLabel, BorderLayout.WEST);
    }
    
    /** Method to get the participant label.
     *  @return return the label of participant 
    */
    public JLabel getLabel() {
        return myParticipantLabel;
    }
  

    @Override
    public void propertyChange(final PropertyChangeEvent theE) {
        // TODO Auto-generated method stub
        if (" the time".equals(theE.getPropertyName())) {
            myTimeLabel.setText(TIME + Utilities.formatTime(
                                      (Integer) myTimeSlider.getValue()));
        }
        
    }

}
