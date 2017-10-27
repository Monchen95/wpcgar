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
package edu.hawhamburg.app.arcore;

import java.util.List;

import edu.hawhamburg.arcore.ARCoreAnchorNode;
import edu.hawhamburg.arcore.ARCoreScene;
import edu.hawhamburg.shared.datastructures.mesh.ITriangleMesh;
import edu.hawhamburg.shared.datastructures.mesh.ObjReader;
import edu.hawhamburg.shared.math.Vector;
import edu.hawhamburg.shared.scenegraph.INode;
import edu.hawhamburg.shared.scenegraph.InnerNode;
import edu.hawhamburg.shared.scenegraph.ScaleNode;
import edu.hawhamburg.shared.scenegraph.TriangleMeshNode;

/**
 * Dummy implementation of an ARCore scene.
 *
 * @author Philipp Jenke
 */

public class DefaultARCoreScene extends ARCoreScene {

    /**
     * This node is used at the anchor.
     */
    protected INode node = null;

    public DefaultARCoreScene() {
        preloadAnchorNode();
    }

    /**
     * Preload anchor node before it is used.
     */
    protected void preloadAnchorNode() {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // Building
                ObjReader reader = new ObjReader();
                List<ITriangleMesh> meshes = reader.read("meshes/building.obj");
                InnerNode buildingNode = new InnerNode();
                for (ITriangleMesh mesh : meshes) {
                    TriangleMeshNode meshNode = new TriangleMeshNode(mesh);
                    buildingNode.addChild(meshNode);
                }
                ScaleNode scaleNode = new ScaleNode(0.3);
                scaleNode.addChild(buildingNode);
                node = scaleNode;
            }
        };
        new Thread(runnable).start();

    }

    @Override
    public void anchorCreated(ARCoreAnchorNode anchorNode) {
        if (node != null) {
            anchorNode.addChild(node);
            Vector lightPos = anchorNode.getPosition().add(new Vector(0, 1, 0, 0));
            getRoot().setLightPosition(lightPos);
        }
    }

    @Override
    public void onSetup(InnerNode rootNode) {
        super.onSetup(rootNode);
        getRoot().setLightPosition(new Vector(0, 0, 0));
    }

    @Override
    public void onTimerTick(int counter) {
    }

    @Override
    public void onSceneRedraw() {
    }
}
