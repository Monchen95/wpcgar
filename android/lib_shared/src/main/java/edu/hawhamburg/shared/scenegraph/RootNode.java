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
import edu.hawhamburg.shared.rendering.Shader;

/**
 * The root node exists only once and is used as the scene graph root. It
 * contains scene information.
 *
 * @author Philipp Jenke
 */
public class RootNode extends InnerNode {
    /**
     * Currently used shader
     */
    private Shader shader;

    /**
     * This flags indicates that the scene should be animated
     */
    private boolean animated;

    /**
     * Position of the light source
     */
    private Vector lightPosition;

    /**
     * Background color
     */
    private Vector backGroundColor;

    public RootNode(Shader shader) {
        this.shader = shader;
        lightPosition = new Vector(1, 1, 0);
        backGroundColor = new Vector(0.95, 0.95, 0.95);
        animated = true;
    }

    @Override
    public void traverse(RenderMode mode, Matrix modelMatrix) {
        super.traverse(mode, modelMatrix);
    }

    @Override
    public void timerTick(int counter) {
        super.timerTick(counter);
    }

    public RootNode getRootNode() {
        return this;
    }

    public Shader getShader() {
        return shader;
    }

    public boolean isAnimated() {
        return animated;
    }

    public void setAnimated(boolean animated) {
        this.animated = animated;
    }

    public Vector getLightPosition() {
        return lightPosition;
    }

    public Vector getBackgroundColor() {
        return backGroundColor;
    }

    public void setLightPosition(Vector lightPosition) {
        this.lightPosition = lightPosition;
    }

    public void setBackgroundColor(Vector backGroundColor) {
        this.backGroundColor = backGroundColor;
    }

    @Override
    public Matrix getTransformation() {
        return Matrix.createIdentityMatrix4();
    }
}
