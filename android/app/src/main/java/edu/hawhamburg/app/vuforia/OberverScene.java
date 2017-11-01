package edu.hawhamburg.app.vuforia;

import android.support.graphics.drawable.VectorDrawableCompat;

import edu.hawhamburg.shared.datastructures.mesh.ITriangleMesh;
import edu.hawhamburg.shared.datastructures.mesh.TriangleMesh;
import edu.hawhamburg.shared.datastructures.mesh.TriangleMeshFactory;
import edu.hawhamburg.shared.datastructures.mesh.TriangleMeshTools;
import edu.hawhamburg.shared.math.Matrix;
import edu.hawhamburg.shared.math.Vector;
import edu.hawhamburg.shared.scenegraph.CubeNode;
import edu.hawhamburg.shared.scenegraph.INode;
import edu.hawhamburg.shared.misc.Scene;
import edu.hawhamburg.shared.scenegraph.InnerNode;
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

    INode oberserverSphere;
    INode targetSphere;

    public OberverScene() {
        super(100, INode.RenderMode.REGULAR);

        pObserverOView = new Vector(0,0,0,1);
        pTargetTView = new Vector(0,0,0,1);

        markerTarget = new VuforiaMarkerNode("campus");
        markerObserver = new VuforiaMarkerNode("elphi");

        observerTranslation = new TranslationNode(pObserverOView);
        targetTranslation = new TranslationNode(pTargetTView);

        observerTransformation = new TransformationNode();

        oberserverSphere = new SphereNode(0.5f,20);
        targetSphere = new SphereNode(0.5f,20);
    }

    @Override
    public void onSetup(InnerNode rootNode) {




        markerTarget.addChild(targetTranslation);
        targetTranslation.addChild(targetSphere);

        rootNode.addChild(markerTarget);


        markerObserver.addChild(observerTranslation);
        observerTranslation.addChild(observerTransformation);
        observerTransformation.addChild(oberserverSphere);

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

    /*    TranslationNode testTransNode = new TranslationNode(findSightLine().add(pObserverOView));
        INode testCube = new CubeNode(2);

        markerObserver.addChild(testTransNode);
        testTransNode.addChild(observerTransformation);
        observerTransformation.addChild(testCube);

        this.getRoot().addChild(markerObserver);*/



        observerTransformation = new TransformationNode(getTransformationSightLine(findSightLine()));
        this.getRoot().addChild(markerObserver);
    }
}
