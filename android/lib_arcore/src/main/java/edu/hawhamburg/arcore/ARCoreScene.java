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
 */
package edu.hawhamburg.arcore;

import edu.hawhamburg.shared.misc.Button;
import edu.hawhamburg.shared.misc.ButtonHandler;
import edu.hawhamburg.shared.misc.Scene;
import edu.hawhamburg.shared.scenegraph.InnerNode;

/**
 * Base scene for ARCore apps.
 *
 * @author Philipp Jenke
 */
public abstract class ARCoreScene extends Scene {

    /**
     * This flag is used to show/hide the detected planes.
     */
    private boolean showDetectedPlanes = true;

    /**
     * A new anchor node was created in the scene graph.
     */
    public abstract void anchorCreated(ARCoreAnchorNode anchorNode);

    @Override
    public void onSetup(InnerNode rootNode) {
        // Load resources, add buttons
        Button button = new Button("show_detected_planes",
                -0.8, -0.8, 0.3, new ButtonHandler() {
            @Override
            public void handle() {
                toggleShowDetectedPlanes();
            }
        });
        addButton(button);
    }

    public void toggleShowDetectedPlanes() {
        showDetectedPlanes = !showDetectedPlanes;
    }

    public boolean showDetectedPlanes() {
        return showDetectedPlanes;
    }
}
