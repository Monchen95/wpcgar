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
 */
package edu.hawhamburg.shared.rendering;

import edu.hawhamburg.shared.math.Matrix;
import edu.hawhamburg.shared.math.Vector;
import edu.hawhamburg.shared.scenegraph.Camera;

/**
 * Control the camera using touch gestures
 */
public class ObserverInteractionController implements InteractionController {
    @Override
    public void init() {
        Camera.getInstance().setup(new Vector(-2, 0, 0), new Vector(0,0,0), new Vector(0,1,0));
        Camera.getInstance().setViewMatrixFromEyeRefUp();
    }

    @Override
    public void touchMoved(int dx, int dy) {
        float alpha = - dx / 200.0f;
        float beta = - dy / 200.0f;
        Camera cam = Camera.getInstance();
        Vector eye = cam.getEye();
        Vector ref = cam.getRef();
        Vector up = cam.getUp();
        Vector dir = eye.subtract(ref);
        // Rotate around up-vector
        eye = Matrix.createRotationMatrix3(up, alpha).multiply(dir).add(ref);
        // Rotate around side-vector
        dir = eye.subtract(ref);
        Vector side = dir.cross(up);
        side.normalize();
        eye = Matrix.createRotationMatrix3(side, -beta).multiply(dir).add(ref);
        // Fix up-vector
        dir = ref.subtract(eye);
        side = dir.cross(up);
        side.normalize();
        up = side.cross(dir);
        up.normalize();
        cam.setup(eye, ref, up);
        cam.setViewMatrixFromEyeRefUp();
    }
}
