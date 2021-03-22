/*
 * TCSS 305 – Assignment 4.
 * Autumn 2019.
 */
package model;

import java.beans.PropertyChangeListener;

/**
 * Defines behaviors allowing PropertyChangeListeners to be added or removed from a 
 * RaceControls object. Implementing classes should inform PropertyChangeListeners
 * when methods defined in RaceControls mutate the state of the Race. 
 * 
 * Defines a set of Properties that may be listened too. Implementing class may further define
 * more Properties. 
 * 
 * @author Charles Bryan
 * @version Fall 2018
 *
 */
public interface PropertyChangeEnabledRaceControls extends RaceControls {
   
    
    /*
     * Add your own constant Property values here. 
     */
    
    /**
     * A property name for set up component. 
     */
    String PROPERTY_RACER_INFO = "Racer Info";
    
    
    /**
     * A property name for set up component. 
     */
    String PROPERTY_SET_UP = "Set up";
    
    /**
     * A property name for clear. 
     */
    String PROPERTY_CLEAR = "Clear";
    
    /**
     * A property name for participant. 
     */
    String PROPERTY_PARTICIPANT = " Participant";
    /**
     * A property name for headers. 
     */
    String PROPERTY_HEADER = "header";
    
    /**
    * A property name for the timer.
    */
    String PROPERTY_TIME = " the time";
    
    /**
     * A property name for the message.
     */
    String PROPERTY_MESSAGE = "message";
    
    /**
     * A property name for the message.
     */
    String PROPERTY_LEADERBOARD = "leaderboard";
    
    /**
     * A property name for the max time.
     */
    String PROPERTY_MAX = "max";
    
    /**
     * A property name for the telemetry message.
     */
    String PROPERTY_TELEMETRY = "telemetry";
    
    /**
     * A property name for the painting racer.
     */
    String PROPERTY_PAINT = "paint racer";
    
    /** A property name for the racer id. */
    
    String PROPERTY_TRACK_SIZE = "track size";
    
    /**
     * A property name for set up component. 
     */
    String PROPERTY_OVAL = "Oval";
    
    
                    
    /**
     * Add a PropertyChangeListener to the listener list. The listener is registered for 
     * all properties. The same listener object may be added more than once, and will be 
     * called as many times as it is added. If listener is null, no exception is thrown and 
     * no action is taken.
     * 
     * @param theListener The PropertyChangeListener to be added
     */
    void addPropertyChangeListener(PropertyChangeListener theListener);
    
    
    /**
     * Add a PropertyChangeListener for a specific property. The listener will be invoked only 
     * when a call on firePropertyChange names that specific property. The same listener object
     * may be added more than once. For each property, the listener will be invoked the number 
     * of times it was added for that property. If propertyName or listener is null, no 
     * exception is thrown and no action is taken.
     * 
     * @param thePropertyName The name of the property to listen on.
     * @param theListener The PropertyChangeListener to be added
     */
    void addPropertyChangeListener(String thePropertyName, PropertyChangeListener theListener);

    /**
     * Remove a PropertyChangeListener from the listener list. This removes a 
     * PropertyChangeListener that was registered for all properties. If listener was added 
     * more than once to the same event source, it will be notified one less time after being 
     * removed. If listener is null, or was never added, no exception is thrown and no action 
     * is taken.
     * 
     * @param theListener The PropertyChangeListener to be removed
     */
    void removePropertyChangeListener(PropertyChangeListener theListener);
    
    /**
     * Remove a PropertyChangeListener for a specific property. If listener was added more than
     * once to the same event source for the specified property, it will be notified one less 
     * time after being removed. If propertyName is null, no exception is thrown and no action 
     * is taken. If listener is null, or was never added for the specified property, no 
     * exception is thrown and no action is taken.
     * 
     * @param thePropertyName The name of the property that was listened on.
     * @param theListener The PropertyChangeListener to be removed
     */
    void removePropertyChangeListener(String thePropertyName, 
                                      PropertyChangeListener theListener);
}
