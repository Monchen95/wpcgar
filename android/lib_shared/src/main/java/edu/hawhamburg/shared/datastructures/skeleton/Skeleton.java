package edu.hawhamburg.shared.datastructures.skeleton;

import java.util.Iterator;
import java.util.List;

import edu.hawhamburg.shared.math.Matrix;
import edu.hawhamburg.shared.math.Vector;

/**
 * A skeleton represents the root bone in a bone tree.
 *
 * @author Philipp Jenke
 */

public class Skeleton extends Bone {

    /**
     * Origin of the bone tree.
     */
    private Vector origin = new Vector(0, 0, 0);

    public Skeleton() {
        // root has no length (virtual bone)
        super(null, 0);
    }

    @Override
    public Matrix getTransformationAtStart() {
        return Matrix.createTranslationMatrix4(origin);
    }

    @Override
    public Matrix getRestStateTransformationAtStart() {
        return getTransformationAtStart();
    }

    /**
     * Returns an iterator to all bones in the skeleton.
     */
    public Iterator<Bone> getBonesIterator() {
        List<Bone> bones = getChildren();
        return bones.iterator();
    }

    /**
     * Sets the current rotations for all bones as rest state.
     */
    public void setRestState() {
        for (Iterator<Bone> it = getBonesIterator(); it.hasNext(); ) {
            Bone bone = it.next();
            bone.setRestState();
        }
    }
}
