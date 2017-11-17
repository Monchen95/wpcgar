package edu.hawhamburg.app.opengl;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import edu.hawhamburg.shared.datastructures.halfEdgeMesh.HalfEdgeTriangleMesh;
import edu.hawhamburg.shared.datastructures.halfEdgeMesh.HalfEdgeUtility;
import edu.hawhamburg.shared.datastructures.mesh.ITriangleMesh;
import edu.hawhamburg.shared.datastructures.mesh.ObjReader;
import edu.hawhamburg.shared.datastructures.mesh.TriangleMesh;
import edu.hawhamburg.shared.datastructures.mesh.TriangleMeshFactory;
import edu.hawhamburg.shared.math.Matrix;
import edu.hawhamburg.shared.math.Vector;
import edu.hawhamburg.shared.misc.Button;
import edu.hawhamburg.shared.misc.ButtonHandler;
import edu.hawhamburg.shared.misc.Constants;
import edu.hawhamburg.shared.misc.Scene;
import edu.hawhamburg.shared.scenegraph.INode;
import edu.hawhamburg.shared.scenegraph.InnerNode;
import edu.hawhamburg.shared.scenegraph.LineStripNode;
import edu.hawhamburg.shared.scenegraph.ScaleNode;
import edu.hawhamburg.shared.scenegraph.TransformationNode;
import edu.hawhamburg.shared.scenegraph.TranslationNode;
import edu.hawhamburg.shared.scenegraph.TriangleMeshNode;

/**
 * Created by abq892 on 15.11.2017.
 */

public class HermiteSplineScene extends Scene {

    private INode planeNode;

    private TransformationNode transformNode;
    private TranslationNode translateNode;

    private int slowness = 2;
    private int segments;
    private double counter;

    private HermiteSpline spline;

    public HermiteSplineScene(){
        super(100, INode.RenderMode.REGULAR);

        transformNode = new TransformationNode();
        translateNode = new TranslationNode(new Vector(0,0,0,1));

        segments = 100;
        counter = 0.0;

        spline = new HermiteSpline(null, null);
    }

    @Override
    public void onSetup(InnerNode rootNode) {

        List<Vector> points = new ArrayList<>();
        List<Vector> tangents = new ArrayList<>();

        points.add(new Vector(0.0, 0.1, -0.80));
        points.add(new Vector(0.80, 0.1, 0.0));
        points.add(new Vector(0.0, 0.1, 0.80));
        points.add(new Vector(-0.80, 0.1, 0.0));

        tangents.add(new Vector(0.80, 0.0, 0.0));
        tangents.add(new Vector(0.0, 0.0, 0.80));
        tangents.add(new Vector(-0.80, 0.0, 0.0));
        tangents.add(new Vector(0.0, 0.0, -0.80));

        spline.setControlPoints(points);
        spline.setTangents(tangents);

        for(double t = 0.0; t<1.0; t+=1.0/segments){
            ITriangleMesh mesh = new TriangleMesh();
            TriangleMeshFactory.createSphere(mesh, 0.01, 7);
            HalfEdgeTriangleMesh newMesh = HalfEdgeUtility.convert(mesh);
            TriangleMeshNode node = new TriangleMeshNode(newMesh);
            TranslationNode spherePos = new TranslationNode(spline.evaluateCurve(t));
            spherePos.addChild(node);
            rootNode.addChild(spherePos);
        }

        //LineStripNode line = new LineStripNode(points);

        ObjReader reader = new ObjReader();
        List<ITriangleMesh> meshesO = reader.read("meshes/plane.obj");
        InnerNode planeNodeO = new InnerNode();
        for (ITriangleMesh mesh : meshesO) {
            TriangleMeshNode meshNode = new TriangleMeshNode(mesh);
            planeNodeO.addChild(meshNode);
        }

        ScaleNode scaleO = new ScaleNode(0.3);
        scaleO.addChild(planeNodeO);
        planeNode = scaleO;

        translateNode.addChild(transformNode);
        transformNode.addChild(planeNode);
        rootNode.addChild(translateNode);
      //  rootNode.addChild(line);
    }

    @Override
    public void onTimerTick(int counter) {
        // Timer tick event
    }

    @Override
    public void onSceneRedraw() {
        counter = (counter+(1.0/(segments*slowness)))%1.0;

        Vector position =  spline.evaluateCurve(counter);
        Vector orientation = spline.evaluateTangent(counter);

        orientation.normalize();

        Vector x = orientation;
        Vector y = new Vector(0,1,0);
        Vector z = x.cross(y);
        y = z.cross(x);

        Matrix transMatrix = new Matrix(x,y,z);

        transMatrix = Matrix.makeHomogenious(transMatrix);


        translateNode.setTranslation(position);
        transformNode.setTransformation(transMatrix);

        Log.d(Constants.LOGTAG,"Counter: " + counter);
        Log.d(Constants.LOGTAG,"Position: " + position);
        Log.d(Constants.LOGTAG,"Orientation: " + orientation);
        Log.d(Constants.LOGTAG,"Transmatrix: " + transMatrix);
    }
}
