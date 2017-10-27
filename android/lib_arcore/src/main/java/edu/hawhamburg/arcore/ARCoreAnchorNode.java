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

import edu.hawhamburg.shared.math.Matrix;
import edu.hawhamburg.shared.math.Vector;
import edu.hawhamburg.shared.scenegraph.InnerNode;
import edu.hawhamburg.arcore.rendering.PlaneAttachment;

/**
 * Represents an anchor position in a ARCore scene
 *
 * @author Philipp Jenke
 */
public class ARCoreAnchorNode extends InnerNode {

    /**
     * Plane attachment reference
     */
    private PlaneAttachment planeAttachment;

    /**
     * ARCore matrix
     */
    private final float[] mAnchorMatrix = new float[16];

    /**
     * CG framework matrix
     */
    private Matrix modelMatrix;

    public ARCoreAnchorNode(PlaneAttachment planeAttachment) {
        this.planeAttachment = planeAttachment;
    }

    @Override
    public void traverse(RenderMode mode, Matrix modelMatrix) {
        if (planeAttachment == null) {
            return;
        }
        planeAttachment.getPose().toMatrix(mAnchorMatrix, 0);
        modelMatrix.set(
                mAnchorMatrix[0], mAnchorMatrix[4], mAnchorMatrix[8], mAnchorMatrix[12],
                mAnchorMatrix[1], mAnchorMatrix[5], mAnchorMatrix[9], mAnchorMatrix[13],
                mAnchorMatrix[2], mAnchorMatrix[6], mAnchorMatrix[10], mAnchorMatrix[14],
                mAnchorMatrix[3], mAnchorMatrix[7], mAnchorMatrix[11], mAnchorMatrix[15]
        );
        super.traverse(mode, modelMatrix);
    }

    /**
     * Return the anchor position.
     */
    public Vector getPosition() {
        planeAttachment.getPose().toMatrix(mAnchorMatrix, 0);
        Matrix M = new Matrix(
                mAnchorMatrix[0], mAnchorMatrix[4], mAnchorMatrix[8], mAnchorMatrix[12],
                mAnchorMatrix[1], mAnchorMatrix[5], mAnchorMatrix[9], mAnchorMatrix[13],
                mAnchorMatrix[2], mAnchorMatrix[6], mAnchorMatrix[10], mAnchorMatrix[14],
                mAnchorMatrix[3], mAnchorMatrix[7], mAnchorMatrix[11], mAnchorMatrix[15]
        );
        Vector pos = M.multiply(new Vector(0, 0, 0, 1));
        return pos;
    }
}
