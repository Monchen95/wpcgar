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
package edu.hawhamburg.shared.scenegraph;

import android.util.Log;

import edu.hawhamburg.shared.math.Matrix;
import edu.hawhamburg.shared.math.Vector;
import edu.hawhamburg.shared.misc.Constants;

/**
 * This node contains a transformation (4x4 matrix) in the scene graph.
 */
public class TransformationNode extends InnerNode {
    /**
     * Transformation matrix (model matrix), 4x4
     */
    private Matrix transformation;

    public TransformationNode(Matrix transformation) {
        this.transformation = transformation;
    }

    public TransformationNode() {
        this.transformation = Matrix.createIdentityMatrix4();
    }

    public void setTransformation(Matrix t) {
        if (t.getNumberOfRows() == 4 && t.getNumberOfColumns() == 4) {
            this.transformation.copy(t);
        } else {
            Log.i(Constants.LOGTAG, "Invalid dimension.");
        }
    }

    public void traverse(RenderMode mode, Matrix modelMatrix) {
        super.traverse(mode, modelMatrix.multiply(transformation));
    }

    public void timerTick(int counter) {
        super.timerTick(counter);
    }

    @Override
    public Matrix getTransformation() {
        return (getParentNode() == null) ? transformation :
                getParentNode().getTransformation().multiply(transformation);
    }
}
