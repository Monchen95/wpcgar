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

import edu.hawhamburg.shared.math.AxisAlignedBoundingBox;
import edu.hawhamburg.shared.math.Matrix;

/**
 * Parent class for all scene graph nodes.
 *
 * @author Philipp Jenke
 */
public abstract class INode {

    private INode parentNode = null;

    /**
     * This flag indicates of the node is shown/traversed
     */
    private boolean active = true;

    /**
     * This flag indicates that this node (and its child nodes together) act as a shadowee
     * (a node which casts a shadow).
     */
    protected boolean isShadowee = false;

    /**
     * This enum allows to pass different render mode states to the drawing
     * routines. The default state is REGULAR.
     */
    public enum RenderMode {
        REGULAR, SHADOW_VOLUME, DARK, DEBUG_SHADOW_VOLUME
    }

    /**
     * This method is called to draw the node using OpenGL commands. Override in
     * implementing nodes. Do not forget to call the same method for the children.
     */
    public abstract void traverse(RenderMode mode, Matrix modelMatrix);

    /**
     * Timer tick event.
     */
    public abstract void timerTick(int counter);

    /**
     * Every node must know its root node
     */
    public RootNode getRootNode() {
        return (parentNode == null) ? null : parentNode.getRootNode();
    }

    /**
     * Every node must know its root node
     */
    public void setParentNode(INode parentNode) {
        this.parentNode = parentNode;
    }

    protected INode getParentNode() {
        return parentNode;
    }

    /**
     * Return the combined transformation starting at the root node.
     */
    public abstract Matrix getTransformation();

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    /**
     * Return the bounding
     *
     * @return
     */
    public abstract AxisAlignedBoundingBox getBoundingBox();

    /**
     * Set this flag to indicate that this node (and its child nodes together) act as a shadoee
     * (a node which casts a shadow).
     */
    public void setIsShadowee(boolean isShadowee) {
        isShadowee = true;
    }

    protected void traverseShadowee(RenderMode mode, Matrix modelMatrix) {
        // TODO - if required, create shadow sphere, create shadow volume, draw shadow volume
    }

}
