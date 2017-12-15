package edu.hawhamburg.app.vuforia;

import android.util.Log;

import edu.hawhamburg.shared.datastructures.mesh.AbstractTriangle;
import edu.hawhamburg.shared.datastructures.mesh.ITriangleMesh;
import edu.hawhamburg.shared.datastructures.mesh.TriangleMesh;
import edu.hawhamburg.shared.datastructures.mesh.TriangleMeshFactory;
import edu.hawhamburg.shared.datastructures.mesh.Vertex;
import edu.hawhamburg.shared.math.AxisAlignedBoundingBox;
import edu.hawhamburg.shared.math.Matrix;
import edu.hawhamburg.shared.math.Vector;
import edu.hawhamburg.shared.misc.Constants;
import edu.hawhamburg.shared.misc.Scene;
import edu.hawhamburg.shared.scenegraph.BoundingBoxNode;
import edu.hawhamburg.shared.scenegraph.CubeNode;
import edu.hawhamburg.shared.scenegraph.INode;
import edu.hawhamburg.shared.scenegraph.InnerNode;
import edu.hawhamburg.shared.scenegraph.ScaleNode;
import edu.hawhamburg.shared.scenegraph.TransformationNode;
import edu.hawhamburg.shared.scenegraph.TranslationNode;
import edu.hawhamburg.shared.scenegraph.TriangleMeshNode;
import edu.hawhamburg.vuforia.VuforiaMarkerNode;

/**
 * Created by abm510 on 13.12.2017.
 */

public class BoundingBoxScene extends Scene{

    private VuforiaMarkerNode markerCube;
    private VuforiaMarkerNode markerSphere;

    private Vector positionCubeC;
    private Vector positionSphereS;

    private TranslationNode translationCube;
    private TranslationNode translationSphere;

    private TransformationNode transformationCube;
    private TransformationNode transformationSphere;

    private BoundingBoxNode boundingBoxCube;
    private BoundingBoxNode boundingBoxSphere;

    private TriangleMeshNode cube;
    private TriangleMeshNode sphere;

    private ITriangleMesh cubeMesh;
    private ITriangleMesh sphereMesh;

    private ScaleNode scaleCube;
    private ScaleNode scaleSphere;

    private double sphereRadius;

    public BoundingBoxScene() {
        super(100, INode.RenderMode.REGULAR);

        positionCubeC = new Vector(0,0,0,1);
        positionSphereS = new Vector(0.5,0,0,1);

        markerCube = new VuforiaMarkerNode("campus");
        markerSphere = new VuforiaMarkerNode("elphi");

        translationCube = new TranslationNode(positionCubeC);
        translationSphere = new TranslationNode(positionSphereS);

        scaleCube = new ScaleNode(0.5);
        scaleSphere = new ScaleNode(0.5);

        sphereRadius = 0.5;
    }

    @Override
    public void onSetup(InnerNode rootNode) {

        cubeMesh = new TriangleMesh();
        TriangleMeshFactory.createCube(cubeMesh);
        cube = new TriangleMeshNode(cubeMesh);

        sphereMesh = new TriangleMesh();
        TriangleMeshFactory.createSphere(sphereMesh,sphereRadius,50);
        sphere = new TriangleMeshNode(sphereMesh);

        boundingBoxCube = new BoundingBoxNode(cube.getBoundingBox());
        boundingBoxSphere = new BoundingBoxNode(sphere.getBoundingBox());

        markerCube.addChild(translationCube);
        markerSphere.addChild(translationSphere);

        translationCube.addChild(scaleCube);
        scaleCube.addChild(boundingBoxCube);
        scaleCube.addChild(cube);

        translationSphere.addChild(scaleSphere);
        scaleSphere.addChild(boundingBoxSphere);
        scaleSphere.addChild(sphere);

        rootNode.addChild(markerCube);
        rootNode.addChild(markerSphere);
    }

    boolean collide(AxisAlignedBoundingBox bbox1, Matrix transformation1,
                    AxisAlignedBoundingBox bbox2, Matrix transformation2){

        Vector bbox1URHom = Vector.makeHomogenious(bbox1.getUR());
        Vector bbox2URHom = Vector.makeHomogenious(bbox2.getUR());
        Vector bbox1LLHom = Vector.makeHomogenious(bbox1.getLL());
        Vector bbox2LLHom = Vector.makeHomogenious(bbox2.getLL());

        Log.d(Constants.LOGTAG, "UR1: before" + bbox1URHom);
        Log.d(Constants.LOGTAG, "UR2: before" + bbox2URHom);
        Log.d(Constants.LOGTAG, "LL1: before" + bbox1LLHom);
        Log.d(Constants.LOGTAG, "LL2: before" + bbox2LLHom);

        Matrix transformationToWorld1 = transformation1.getInverse().multiply(transformation2);

        Vector ur1 = transformationToWorld1.multiply(bbox1URHom).xyz();
        Vector ur2 = bbox2URHom.xyz();
        Vector ll1 = transformationToWorld1.multiply(bbox1LLHom).xyz();
        Vector ll2 = bbox2LLHom.xyz();

        Log.d(Constants.LOGTAG, "UR1 after: " + ur1);
        Log.d(Constants.LOGTAG, "UR2 after: " + ur2);
        Log.d(Constants.LOGTAG, "LL1 after: " + ll1);
        Log.d(Constants.LOGTAG, "LL2 after: " + ll2);

        //bbox1.transform(transformation1) alternativ

        if( ur1.x()>ll2.x() && ur2.x()>ll1.x()){
            Log.d(Constants.LOGTAG, "Calcuate x");

            if( ur1.y()>ll2.y() && ur2.y()>ll1.y()){
                Log.d(Constants.LOGTAG, "Calcuate y");

                if( ur1.z()>ll2.z() && ur2.z()>ll1.z()){
                    Log.d(Constants.LOGTAG, "Calcuate z");

                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Calculates the distance between a bone and a point.
     */
    private double calculateDistance(Vector a, Vector b, Vector point) {

        double r = 0.0;
        double distance = 0.0;

        Vector tmp;
        Vector xStroke;

        // Point x
        Vector x = point;

        // Line g: p + r * v
        Vector p1 = a;
        Vector p2 = b;
        Vector p1p2 = p2.subtract(p1);
        Vector v = p1p2.getNormalized();

        // r' = (x - p) * v
        tmp = x.subtract(p1);
        r = tmp.multiply(v);

        // x' = p + r' * v
        tmp = v.multiply(r);
        xStroke = p1.add(tmp);

        // x' = ||x - x'||
        tmp = x.subtract(xStroke);
        distance = tmp.getNorm();


        Vector lengthToStartVec = xStroke.subtract(p1);
        double lengthToStart = lengthToStartVec.getNorm();

        Vector lengthToEndVec = xStroke.subtract(p2);
        double lengthToEnd = lengthToEndVec.getNorm();

        if(lengthToStart<p1p2.getNorm() && lengthToEnd<p1p2.getNorm()){
            return distance;
        }

        Vector distanceStart = point.subtract(p1);
        Vector distanceEnd = point.subtract(p2);

        double distanceStartLength = distanceStart.getNorm();
        double distanceEndLength = distanceEnd.getNorm();

        double tmpSmall = Math.min(distanceStartLength,distanceEndLength);
        return tmpSmall;
    }

    private double getDistance(Vertex a, Vertex b, Vertex c, Vector center) {
        //normal plane
        Vector ab = b.getPosition().subtract(a.getPosition());
        Vector ac = c.getPosition().subtract(a.getPosition());

        Vector planeNormal = ab.cross(ac);
        planeNormal.normalize();

        double an_skalar = a.getPosition().multiply(planeNormal);
        double mn_skalar = center.multiply(planeNormal);
        double nn_skalar = planeNormal.multiply(planeNormal);

        double lambda = (an_skalar-mn_skalar) / nn_skalar;

        Vector x = center.add(planeNormal.multiply(lambda));

        double centerx = center.subtract(x).getNorm();

        double ax = x.subtract(a.getPosition()).getNorm();
        double bx = x.subtract(b.getPosition()).getNorm();
        double cx = x.subtract(c.getPosition()).getNorm();

        double areaABX = (ax*bx)/2;
        double areaBCX = (bx*cx)/2;
        double areaCAX = (cx*ax)/2;

        double areaTotal = (ab.getNorm() * ac.getNorm()) / 2;

        double alpha = areaABX/areaTotal;
        double beta = areaBCX/areaTotal;
        double gamma = areaCAX/areaTotal;

        if(0<=alpha && alpha<=1 && 0<=beta && beta<=1 && 0<=gamma && gamma<=1){
            return centerx;
        }

        //double acenter = center.subtract(a.getPosition()).getNorm();
        //double bcenter = center.subtract(b.getPosition()).getNorm();
        //double ccenter = center.subtract(c.getPosition()).getNorm();
        double dist_ab = calculateDistance(a.getPosition(),b.getPosition(),center);
        double dist_bc = calculateDistance(b.getPosition(),c.getPosition(),center);
        double dist_ca = calculateDistance(c.getPosition(),a.getPosition(),center);

        return Math.min(Math.min(dist_ab,dist_bc),dist_ca);
    }

    boolean collideExact(ITriangleMesh mesh, Matrix meshTransformation,
                         Vector center, double radius, Matrix sphereTransformation){
        double distance = 0.0;
        AbstractTriangle triangle;

        center=Vector.makeHomogenious(center);
        center = sphereTransformation.getInverse().multiply(meshTransformation.multiply(center));
        center=center.xyz();

        for(int i=0;i<mesh.getNumberOfTriangles();i++){
            triangle = mesh.getTriangle(i);
            distance = getDistance(mesh.getVertex(triangle,0),mesh.getVertex(triangle,1),mesh.getVertex(triangle,2),center);
            if(distance<radius){
                Log.d(Constants.LOGTAG,"Exact collision!");
                return true;
            }
        }
        return false;
    }

    @Override
    public void onTimerTick(int counter) {

    }

    @Override
    public void onSceneRedraw() {
       if(collide(cube.getBoundingBox(),
                cube.getTransformation(),
                sphere.getBoundingBox(),
                sphere.getTransformation())){

           boundingBoxCube.setColor(new Vector(1,1,0,1));
           boundingBoxSphere.setColor(new Vector(1,1,0,1));

           Vector sphereLL = boundingBoxSphere.getBoundingBox().getLL();
           Vector sphereUR = boundingBoxSphere.getBoundingBox().getUR();

           Vector sphereCenter = sphereLL.add(sphereUR.subtract(sphereLL).multiply(0.5));

           if(collideExact(cubeMesh,cube.getTransformation(),sphereCenter,sphereRadius,sphere.getTransformation())){
              boundingBoxCube.setColor(new Vector(1,0,0,1));
               boundingBoxSphere.setColor(new Vector(1,0,0,1));
           }

        } else {
           boundingBoxCube.setColor(new Vector(0,1,0,1));
           boundingBoxSphere.setColor(new Vector(0,1,0,1));

       }
    }
}
