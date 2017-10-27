/**
 * Diese Datei gehört zum Android/Java Framework zur Veranstaltung "Computergrafik für
 * Augmented Reality" von Prof. Dr. Philipp Jenke an der Hochschule für Angewandte
 * Wissenschaften (HAW) Hamburg. Weder Teile der Software noch das Framework als Ganzes dürfen
 * ohne die Einwilligung von Philipp Jenke außerhalb von Forschungs- und Lehrprojekten an der HAW
 * Hamburg verwendet werden.
 *
 * This file is part of the Android/Java framework for the course "Computer graphics for augmented
 * reality" by Prof. Dr. Philipp Jenke at the University of Applied (UAS) Sciences Hamburg. Neither
 * parts of the framework nor the complete framework may be used outside of research or student
 * projects at the UAS Hamburg.
 *
 * This software was built upon the Vuforia sample code:
 * || Copyright (c) 2016 PTC Inc. All Rights Reserved.
 * || Copyright (c) 2012-2014 Qualcomm Connected Experiences, Inc. All Rights Reserved.
 * || Vuforia is a trademark of PTC Inc., registered in the United States and other
 * || countries.
 */
package edu.hawhamburg.vuforia;

import com.vuforia.State;


/**
 * Interface to be implemented by the activity which uses Session
 */
public interface SessionControllable
{
    
    // To be called to initialize the trackers
    boolean doInitTrackers();
    
    
    // To be called to load the trackers' data
    boolean doLoadTrackersData();
    
    
    // To be called to start tracking with the initialized trackers and their
    // loaded data
    boolean doStartTrackers();
    
    
    // To be called to stop the trackers
    boolean doStopTrackers();
    
    
    // To be called to destroy the trackers' data
    boolean doUnloadTrackersData();
    
    
    // To be called to deinitialize the trackers
    boolean doDeinitTrackers();
    
    
    // This callback is called after the Vuforia initialization is complete,
    // the trackers are initialized, their data loaded and
    // tracking is ready to start
    void onInitARDone(VuforiaException e);
    
    
    // This callback is called every cycle
    void onVuforiaUpdate(State state);
    
}
