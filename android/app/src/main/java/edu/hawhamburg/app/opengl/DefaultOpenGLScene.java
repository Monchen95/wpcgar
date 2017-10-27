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
package edu.hawhamburg.app.opengl;

import android.util.Log;

import java.util.List;

import edu.hawhamburg.shared.datastructures.halfEdgeMesh.HalfEdgeTriangleMesh;
import edu.hawhamburg.shared.datastructures.halfEdgeMesh.HalfEdgeUtility;
import edu.hawhamburg.shared.datastructures.mesh.ITriangleMesh;
import edu.hawhamburg.shared.datastructures.mesh.ObjReader;
import edu.hawhamburg.shared.datastructures.mesh.TriangleMesh;
import edu.hawhamburg.shared.datastructures.mesh.TriangleMeshFactory;
import edu.hawhamburg.shared.datastructures.mesh.TriangleMeshTools;
import edu.hawhamburg.shared.misc.Button;
import edu.hawhamburg.shared.misc.ButtonHandler;
import edu.hawhamburg.shared.misc.Constants;
import edu.hawhamburg.shared.misc.Scene;
import edu.hawhamburg.shared.scenegraph.INode;
import edu.hawhamburg.shared.scenegraph.InnerNode;
import edu.hawhamburg.shared.scenegraph.ScaleNode;
import edu.hawhamburg.shared.scenegraph.TriangleMeshNode;

/**
 * Dummy scene with rather simple content.
 *
 * @author Philipp Jenke
 */
public class DefaultOpenGLScene extends Scene {


    public DefaultOpenGLScene() {
        super(100, INode.RenderMode.REGULAR);
    }

    @Override
    public void onSetup(InnerNode rootNode) {

        Button button = new Button("kanone_abfeuern.png",
                -0.7, -0.7, 0.2, new ButtonHandler() {
            @Override
            public void handle() {
                Log.i(Constants.LOGTAG, "Button 1 pressed!");
            }
        });
        //addButton(button);


        ITriangleMesh mesh = new TriangleMesh();
        TriangleMeshFactory.createSphere(mesh, 0.3, 7);
        HalfEdgeTriangleMesh newMesh = HalfEdgeUtility.convert(mesh);
        TriangleMeshNode node = new TriangleMeshNode(newMesh);
        rootNode.addChild(node);
    }

    @Override
    public void onTimerTick(int counter) {
        // Timer tick event
    }

    @Override
    public void onSceneRedraw() {

    }
}
