package edu.hawhamburg.shared.datastructures.skeleton;

import java.util.ArrayList;
import java.util.List;

import edu.hawhamburg.shared.math.Matrix;
import edu.hawhamburg.shared.math.Vector;

/**
 * Representation of a bone. Its starting position is implicitly given by the parent bone.
 * The orientation of the bone is given by its matrix currentRotationAtStartJoint (combined with its parent orientations).
 * The length of the bone points into its x-direction.
 *
 * @author Philipp Jenke
 */

public class Bone {
    /**
     * Orientation matrix of the start position.
     */
    private Matrix currentRotationAtStartJoint = Matrix.createIdentityMatrix4();

    /**
     * Rest state orientation matrix of the start position.
     */
    private Matrix restStateRotationAtStartJoint = Matrix.createIdentityMatrix4();

    /**
     * Bone length
     */
    private double length = 1;

    /**
     * Reference to the parent bone.
     */
    protected final Bone parent;

    /**
     * List of child bones
     */
    private List<Bone> children = new ArrayList<Bone>();

    public Bone(Bone parent, double length) {
        this.parent = parent;
        if (parent != null) {
            parent.children.add(this);
        }
        this.length = length;

    }

    /**
     * Return iterator for the children.
     */
    public List<Bone> getChildren() {
        List<Bone> children = new ArrayList<>();
        for (Bone child : this.children) {
            children.addAll(child.getChildren());
            children.add(child);
        }
        return children;
    }

    /**
     * Returns the combined transformation for the bone coming from the tree (at the start point).
     */
    public Matrix getTransformationAtStart() {
        return parent.getTransformationAtEnd().multiply(currentRotationAtStartJoint);
    }

    /**
     * Returns the combined transformation for the bone coming from the tree (at the end point).
     */
    public Matrix getTransformationAtEnd() {
        return getTransformationAtStart().multiply(
                Matrix.createTranslationMatrix4(new Vector(length, 0, 0)));
    }

    /***
     * Return the bones start point (where its parent is attached).
     */
    public Vector getStart() {
        return getTransformationAtStart().multiply(new Vector(0, 0, 0, 1)).xyz();
    }

    /***
     * Return the bones end point (where its children are attached).
     */
    public Vector getEnd() {
        return getTransformationAtStart().multiply(new Vector(length, 0, 0, 1)).xyz();
    }

    public double getLength() {
        return length;
    }

    /**
     * Sets the rotation of the bone (at the starting joint).
     */
    public void setRotation(Matrix rotation) {
        this.currentRotationAtStartJoint = rotation;
    }

    /**
     * Preserves the current rotation as the rest state rotation.
     */
    public void setRestState() {
        restStateRotationAtStartJoint = new Matrix(currentRotationAtStartJoint);
    }

    /**
     * [RestState] Returns the combined transformation for the bone coming from the tree (at the start point).
     */
    public Matrix getRestStateTransformationAtStart() {
        return parent.getRestStateTransformationAtEnd().multiply(restStateRotationAtStartJoint);
    }

    /**
     * [RestState] Returns the combined transformation for the bone coming from the tree (at the end point).
     */
    public Matrix getRestStateTransformationAtEnd() {
        return getRestStateTransformationAtStart().multiply(
                Matrix.createTranslationMatrix4(new Vector(length, 0, 0)));
    }

    /***
     * [RestState] Return the bones start point (where its parent is attached).
     */
    public Vector getRestStateStart() {
        return getRestStateTransformationAtStart().multiply(new Vector(0, 0, 0, 1)).xyz();
    }

    /***
     * [RestState] Return the bones end point (where its children are attached).
     */
    public Vector getRestStateEnd() {
        return getRestStateTransformationAtStart().multiply(new Vector(length, 0, 0, 1)).xyz();
    }
}
