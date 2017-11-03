package edu.hawhamburg.app.vuforia;

import android.support.graphics.drawable.VectorDrawableCompat;

import java.util.List;

import edu.hawhamburg.shared.datastructures.mesh.ITriangleMesh;
import edu.hawhamburg.shared.datastructures.mesh.ObjReader;
import edu.hawhamburg.shared.datastructures.mesh.TriangleMesh;
import edu.hawhamburg.shared.datastructures.mesh.TriangleMeshFactory;
import edu.hawhamburg.shared.datastructures.mesh.TriangleMeshTools;
import edu.hawhamburg.shared.math.Matrix;
import edu.hawhamburg.shared.math.Vector;
import edu.hawhamburg.shared.scenegraph.CubeNode;
import edu.hawhamburg.shared.scenegraph.INode;
import edu.hawhamburg.shared.misc.Scene;
import edu.hawhamburg.shared.scenegraph.InnerNode;
import edu.hawhamburg.shared.scenegraph.ScaleNode;
import edu.hawhamburg.shared.scenegraph.SphereNode;
import edu.hawhamburg.shared.scenegraph.TransformationNode;
import edu.hawhamburg.shared.scenegraph.TranslationNode;
import edu.hawhamburg.shared.scenegraph.TriangleMeshNode;
import edu.hawhamburg.vuforia.VuforiaMarkerNode;

/**
 * Created by abm510 on 01.11.2017.
 */

public class OberverScene extends Scene {

    Vector pObserverOView;
    Vector pTargetTView;

    VuforiaMarkerNode markerTarget;
    VuforiaMarkerNode markerObserver;


    TranslationNode observerTranslation;
    TranslationNode targetTranslation;

    TransformationNode observerTransformation;

    protected INode observerNode = null;
    protected INode targetNode = null;


    public OberverScene() {
        super(100, INode.RenderMode.REGULAR);

        pObserverOView = new Vector(0,0,0,1);
        pTargetTView = new Vector(0,0,0,1);

        markerTarget = new VuforiaMarkerNode("campus");
        markerObserver = new VuforiaMarkerNode("elphi");

        observerTranslation = new TranslationNode(pObserverOView);
        targetTranslation = new TranslationNode(pTargetTView);

        observerTransformation = new TransformationNode();

    }

    @Override
    public void onSetup(InnerNode rootNode) {



        ObjReader reader = new ObjReader();
        List<ITriangleMesh> meshesO = reader.read("meshes/max_planck.obj");
        InnerNode deerNodeO = new InnerNode();
        for (ITriangleMesh mesh : meshesO) {
            TriangleMeshNode meshNode = new TriangleMeshNode(mesh);
            deerNodeO.addChild(meshNode);
        }

        ScaleNode scaleO = new ScaleNode(0.3);
        scaleO.addChild(deerNodeO);
        observerNode = scaleO;

        List<ITriangleMesh> meshesT = reader.read("meshes/deer.obj");
        InnerNode headNodeT = new InnerNode();
        for (ITriangleMesh mesh : meshesT) {
            TriangleMeshNode meshNode = new TriangleMeshNode(mesh);
            headNodeT.addChild(meshNode);
        }

        ScaleNode scaleT = new ScaleNode(0.03);
        scaleT.addChild(headNodeT);
        targetNode = scaleT;

        markerTarget.addChild(targetTranslation);
        targetTranslation.addChild(targetNode);

        rootNode.addChild(markerTarget);

        markerObserver.addChild(observerTranslation);
        observerTranslation.addChild(observerTransformation);
        observerTransformation.addChild(observerNode);

        rootNode.addChild(markerObserver);

    }

    protected Vector findSightLine(){

        Matrix tmp = markerObserver.getTransformation().getInverse().multiply(markerTarget.getTransformation());
        Vector pTargetOView = tmp.multiply(pTargetTView);

        Vector sightLine = pTargetOView.subtract(pObserverOView);

        sightLine.normalize();

        return sightLine;
    }

    protected Matrix getTransformationSightLine(Vector sightLine){
        Vector x = sightLine.xyz();
        Vector z = x.cross(new Vector(0,1,0));
        Vector y = z.cross(x);

        z.normalize();
        y.normalize();

        Matrix transMatrix = new Matrix(x,y,z);
        Matrix retMatrix = Matrix.makeHomogenious(transMatrix);

        return retMatrix;
    }

    @Override
    public void onTimerTick(int counter) {
        // Timer tick event
    }

    @Override
    public void onSceneRedraw() {

        observerTransformation.setTransformation(getTransformationSightLine(findSightLine()));

    }
}
