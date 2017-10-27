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

import edu.hawhamburg.shared.math.Matrix;
import edu.hawhamburg.shared.math.Vector;

/**
 * Scene graph node which scales all its child nodes.
 *
 * @author Philipp Jenke
 */
public class ScaleNode extends InnerNode {

    /**
     * Scaling factors in x-, y- and z-direction.
     */
    private Matrix scaleMatrix;

    /**
     * Constructor.
     */
    public ScaleNode(Vector scale) {
        scaleMatrix = Matrix.createScaleMatrix4(scale);
    }

    /**
     * Constructor.
     */
    public ScaleNode(double scale) {
        scaleMatrix = Matrix.createScaleMatrix4(new Vector(scale, scale, scale));
    }

    public void traverse(RenderMode mode, Matrix modelMatrix) {
        super.traverse(mode, modelMatrix.multiply(scaleMatrix));
    }

    public void timerTick(int counter) {
        super.timerTick(counter);
    }

    @Override
    public Matrix getTransformation() {
        return (getParentNode() == null) ? scaleMatrix :
                getParentNode().getTransformation().multiply(scaleMatrix);
    }

    public void setScale(Vector scale) {
        scaleMatrix = Matrix.createScaleMatrix4(scale);
    }
}
