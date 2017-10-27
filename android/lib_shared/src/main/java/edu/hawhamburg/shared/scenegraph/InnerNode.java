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

import java.util.ArrayList;
import java.util.List;

import edu.hawhamburg.shared.math.AxisAlignedBoundingBox;
import edu.hawhamburg.shared.math.Matrix;

/**
 * Inner node in the scene graph which might have child nodes.
 */
public class InnerNode extends INode {

    /**
     * List of child nodes.
     */
    private List<INode> children = new ArrayList<INode>();

    @Override
    public void traverse(RenderMode mode, Matrix modelMatrix) {
        if (!isActive()) {
            return;
        }

        if (isShadowee && mode == RenderMode.SHADOW_VOLUME && mode == RenderMode.DEBUG_SHADOW_VOLUME) {
            traverseShadowee(mode, modelMatrix);
        } else {
            for (INode child : children) {
                child.traverse(mode, modelMatrix);
            }
        }
    }

    @Override
    public void timerTick(int counter) {
        for (INode child : children) {
            child.timerTick(counter);
        }
    }

    /**
     * Add new child node.
     **/
    public void addChild(INode child) {
        child.setParentNode(this);
        children.add(child);
    }

    @Override
    public Matrix getTransformation() {
        return (getParentNode() == null) ? null : getParentNode().getTransformation();
    }

    /**
     * Return the bounding
     *
     * @return
     */
    public AxisAlignedBoundingBox getBoundingBox() {
        AxisAlignedBoundingBox bbox = null;
        for (INode child : children) {
            if (bbox == null) {
                bbox = new AxisAlignedBoundingBox(child.getBoundingBox());
            } else {
                bbox.add(child.getBoundingBox());
            }
        }
        return bbox;
    }

}
