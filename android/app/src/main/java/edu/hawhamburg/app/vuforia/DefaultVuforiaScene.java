package edu.hawhamburg.app.vuforia;

import edu.hawhamburg.app.opengl.DefaultOpenGLScene;
import edu.hawhamburg.shared.datastructures.mesh.ITriangleMesh;
import edu.hawhamburg.shared.datastructures.mesh.TriangleMesh;
import edu.hawhamburg.shared.datastructures.mesh.TriangleMeshFactory;
import edu.hawhamburg.shared.datastructures.mesh.TriangleMeshTools;
import edu.hawhamburg.shared.misc.Scene;
import edu.hawhamburg.shared.scenegraph.INode;
import edu.hawhamburg.shared.scenegraph.InnerNode;
import edu.hawhamburg.shared.scenegraph.TriangleMeshNode;
import edu.hawhamburg.vuforia.VuforiaMarkerNode;

/**
 * Dummy implementation of a scene with a Vuforia marker
 *
 * @author Philipp Jenke
 */
public class DefaultVuforiaScene extends Scene {

    public DefaultVuforiaScene() {
        super(100, INode.RenderMode.REGULAR);
    }

    @Override
    public void onSetup(InnerNode rootNode) {
        VuforiaMarkerNode marker = new VuforiaMarkerNode("elphi");

        ITriangleMesh mesh = new TriangleMesh();
        TriangleMeshFactory.createSphere(mesh, 0.3, 7);
        TriangleMeshTools.placeOnXZPlane(mesh);
        TriangleMeshNode node = new TriangleMeshNode(mesh);
        marker.addChild(node);

        rootNode.addChild(marker);
    }

    @Override
    public void onTimerTick(int counter) {
        // Timer tick event
    }

    @Override
    public void onSceneRedraw() {

    }
}
