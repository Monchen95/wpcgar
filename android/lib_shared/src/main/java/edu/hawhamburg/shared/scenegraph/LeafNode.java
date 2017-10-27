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
import edu.hawhamburg.shared.rendering.ShaderAttributes;

/**
 * A leaf node allows to draw OpenGl content.
 */
public abstract class LeafNode extends INode {

    public static final String LOGTAG = "WP Computer Graphics AR";

    @Override
    public void traverse(RenderMode mode, Matrix modelMatrix) {

        if (!isActive()) {
            return;
        }

        if (isShadowee && mode == RenderMode.SHADOW_VOLUME && mode == RenderMode.DEBUG_SHADOW_VOLUME) {
            traverseShadowee(mode, modelMatrix);
        } else {

            // Model matrix
            Matrix openGLModelMatrix = modelMatrix.getTransposed();
            ShaderAttributes.getInstance().setModelMatrixParameter(openGLModelMatrix);
            //Log.i(LOGTAG, "modelview (cg):\n " + modelMatrix.toString());

            // Projection matrix
            ShaderAttributes.getInstance().setProjectionMatrixParameter(
                    Camera.getInstance().getProjectionMatrix());
            //Log.i(LOGTAG, "projection (cg):\n " + Camera.getInstance().getProjectionMatrix().toString());

            // View matrix
            Matrix viewMatrix = Camera.getInstance().getViewMatrix();
            ShaderAttributes.getInstance().setViewMatrixParameter(viewMatrix);

            drawGL(mode, openGLModelMatrix);
        }
    }

    @Override
    public void timerTick(int counter) {
    }

    /**
     * Draw GL content.
     */
    public abstract void drawGL(RenderMode mode, Matrix modelMatrix);

    @Override
    public Matrix getTransformation() {
        return (getParentNode() == null) ? null : getParentNode().getTransformation();
    }

}
