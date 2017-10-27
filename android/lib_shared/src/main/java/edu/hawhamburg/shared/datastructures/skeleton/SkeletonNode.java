package edu.hawhamburg.shared.datastructures.skeleton;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import edu.hawhamburg.shared.datastructures.mesh.TriangleMesh;
import edu.hawhamburg.shared.datastructures.mesh.TriangleMeshFactory;
import edu.hawhamburg.shared.math.Matrix;
import edu.hawhamburg.shared.scenegraph.InnerNode;
import edu.hawhamburg.shared.scenegraph.ScaleNode;
import edu.hawhamburg.shared.scenegraph.TransformationNode;
import edu.hawhamburg.shared.scenegraph.TriangleMeshNode;

/**
 * Render a skeleton and all its bones
 *
 * @author Philipp Jenke
 */
public class SkeletonNode extends InnerNode {

    /**
     * Reference to the skeleton
     */
    private final Skeleton skeleton;

    /**
     * The bone mesh is only created once.
     */
    private TriangleMesh boneMesh = new TriangleMesh();

    /**
     * Remember the transformation node for each bone.
     */
    private Map<Bone, TransformationNode> transformationNodeMap = new HashMap<Bone, TransformationNode>();

    public SkeletonNode(Skeleton skeleton) {
        TriangleMeshFactory.createBone(boneMesh);
        this.skeleton = skeleton;
        for (Iterator<Bone> it = skeleton.getBonesIterator(); it.hasNext(); ) {
            Bone bone = it.next();
            TransformationNode transformationNode = new TransformationNode(bone.getTransformationAtStart());
            transformationNodeMap.put(bone, transformationNode);
            ScaleNode scaleNode = new ScaleNode(bone.getLength());
            scaleNode.addChild(new TriangleMeshNode(boneMesh));
            transformationNode.addChild(scaleNode);
            addChild(transformationNode);
        }
    }

    @Override
    public void traverse(RenderMode mode, Matrix modelMatrix) {
        super.traverse(mode, modelMatrix);

        // Update transformations
        for (Iterator<Bone> it = skeleton.getBonesIterator(); it.hasNext(); ) {
            Bone bone = it.next();
            transformationNodeMap.get(bone).setTransformation(bone.getTransformationAtStart());
        }
    }
}
