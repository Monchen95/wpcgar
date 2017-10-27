/**
 * Diese Datei gehört zum Android/Java Framework zur Veranstaltung "Computergrafik für
 * Augmented Reality" von Prof. Dr. Philipp Jenke an der Hochschule für Angewandte
 * Wissenschaften (HAW) Hamburg. Weder Teile der Software noch das Framework als Ganzes dürfen
 * ohne die Einwilligung von Philipp Jenke außerhalb von Forschungs- und Lehrprojekten an der HAW
 * Hamburg verwendet werden.
 * <p>
 * This file is part of the Android/Java framework for the course "Computer graphics for augmented
 * reality" by Prof. Dr. Philipp Jenke at the University of Applied (UAS) Sciences Hamburg. Neither
 * parts of the framework nor the complete framework may be used outside of research or student
 * projects at the UAS Hamburg.
 * <p>
 * This software was built upon the Vuforia sample code:
 * || Copyright (c) 2016 PTC Inc. All Rights Reserved.
 * || Copyright (c) 2012-2014 Qualcomm Connected Experiences, Inc. All Rights Reserved.
 * || Vuforia is a trademark of PTC Inc., registered in the United States and other
 * || countries.
 */
package edu.hawhamburg.vuforia;

/**
 * Used to send back to the activity any error during vuforia processes
 */
public class VuforiaException extends Exception {

    private static final long serialVersionUID = 2L;

    public static final int INITIALIZATION_FAILURE = 0;
    public static final int VUFORIA_ALREADY_INITIALIZATED = 1;
    public static final int TRACKERS_INITIALIZATION_FAILURE = 2;
    public static final int LOADING_TRACKERS_FAILURE = 3;
    public static final int UNLOADING_TRACKERS_FAILURE = 4;
    public static final int TRACKERS_DEINITIALIZATION_FAILURE = 5;
    public static final int CAMERA_INITIALIZATION_FAILURE = 6;

    private int mCode = -1;
    private String mString = "";

    public VuforiaException(int code, String description) {
        super(description);
        mCode = code;
        mString = description;
    }


    public int getCode() {
        return mCode;
    }


    public String getString() {
        return mString;
    }
}
