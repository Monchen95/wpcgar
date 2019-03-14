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

import org.w3c.dom.Document;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import edu.hawhamburg.shared.datastructures.halfEdgeMesh.HalfEdgeTriangleMesh;
import edu.hawhamburg.shared.datastructures.halfEdgeMesh.HalfEdgeUtility;
import edu.hawhamburg.shared.datastructures.mesh.ITriangleMesh;
import edu.hawhamburg.shared.datastructures.mesh.ObjReader;
import edu.hawhamburg.shared.datastructures.mesh.TriangleMesh;
import edu.hawhamburg.shared.datastructures.mesh.TriangleMeshFactory;
import edu.hawhamburg.shared.datastructures.mesh.TriangleMeshTools;
import edu.hawhamburg.shared.importer.skeleton.SimpleTestFactory;
import edu.hawhamburg.shared.importer.skeleton.SkeletalAnimatedMesh;
import edu.hawhamburg.shared.importer.util.ColladaImporter;
import edu.hawhamburg.shared.math.Vector;
import edu.hawhamburg.shared.misc.AssetPath;
import edu.hawhamburg.shared.misc.Button;
import edu.hawhamburg.shared.misc.ButtonHandler;
import edu.hawhamburg.shared.misc.Constants;
import edu.hawhamburg.shared.misc.Scene;
import edu.hawhamburg.shared.scenegraph.INode;
import edu.hawhamburg.shared.scenegraph.InnerNode;
import edu.hawhamburg.shared.scenegraph.ScaleNode;
import edu.hawhamburg.shared.scenegraph.TransformationNode;
import edu.hawhamburg.shared.scenegraph.TriangleMeshNode;

/**
 * Dummy scene with rather simple content.
 *
 * @author Philipp Jenke
 */
public class DefaultOpenGLScene extends Scene {

    TransformationNode meshTransformationNode;
    TriangleMesh mesh = null;
    TriangleMeshNode mNode = null;
    boolean b = true;
    InnerNode node = new InnerNode();
    ScaleNode scaleNode = new ScaleNode(0.1);
    SkeletalAnimatedMesh skeletalAnimatedMesh;
    List<TransformationNode> jointNodesAnimated = new ArrayList<>();
    List<TransformationNode> jointNodesBindPose = new ArrayList<>();
    List<TransformationNode> boneNodes = new ArrayList<>();
    int t = 0;
    float progression =0;

    public DefaultOpenGLScene() {
        super(100, INode.RenderMode.REGULAR);
    }

    @Override
    public void onSetup(InnerNode rootNode) {

        ObjReader reader = new ObjReader();

        skeletalAnimatedMesh = reader.readDae("meshes/cowboy.dae");
        ITriangleMesh mesh = skeletalAnimatedMesh.getMesh();
        mesh.setColor(new Vector(1, 1, 1, 1));
        mesh.computeTriangleNormals();
        mNode = new TriangleMeshNode(mesh);
        meshTransformationNode = new TransformationNode();
        meshTransformationNode.addChild(mNode);
        scaleNode.addChild(meshTransformationNode);
        node.addChild(scaleNode);
        getRoot().addChild(node);
        //SkeletalAnimatedMesh skeletalAnimatedMesh = colladaImporter.importColladaFile(doc);



        /*
        File file = new File(url.getPath());

        Document doc = ColladaImporter.readColladaFile(file);

        SkeletalAnimatedMesh skeletalAnimatedMesh = colladaImporter.importColladaFile(doc);
        //SkeletalAnimatedMesh skeletalAnimatedMesh = colladaImporter.importColladaFile("meshes/cowboy.dae");
        */
    }

    @Override
    public void onTimerTick(int counter) {
        // Timer tick event
    }

    @Override
    public void onSceneRedraw() {
        System.out.println("mulm");



        t++;



        TriangleMesh animatedMesh = skeletalAnimatedMesh.animate(progression);
        progression = progression + 0.05f;
        if(progression>0.8333f){
            progression=0.01f;
        }



        meshTransformationNode.removeChild(mNode);
        mNode = new TriangleMeshNode(animatedMesh);
        meshTransformationNode.addChild(mNode);

        t=0;

    }
}
