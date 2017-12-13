package edu.hawhamburg.app.vuforia;

import edu.hawhamburg.shared.datastructures.mesh.AbstractTriangle;
import edu.hawhamburg.shared.datastructures.mesh.ITriangleMesh;
import edu.hawhamburg.shared.datastructures.mesh.TriangleMesh;
import edu.hawhamburg.shared.datastructures.mesh.TriangleMeshFactory;
import edu.hawhamburg.shared.datastructures.mesh.Vertex;
import edu.hawhamburg.shared.math.AxisAlignedBoundingBox;
import edu.hawhamburg.shared.math.Matrix;
import edu.hawhamburg.shared.math.Vector;
import edu.hawhamburg.shared.misc.Scene;
import edu.hawhamburg.shared.scenegraph.BoundingBoxNode;
import edu.hawhamburg.shared.scenegraph.CubeNode;
import edu.hawhamburg.shared.scenegraph.INode;
import edu.hawhamburg.shared.scenegraph.InnerNode;
import edu.hawhamburg.shared.scenegraph.TransformationNode;
import edu.hawhamburg.shared.scenegraph.TranslationNode;
import edu.hawhamburg.shared.scenegraph.TriangleMeshNode;
import edu.hawhamburg.vuforia.VuforiaMarkerNode;

/**
 * Created by abm510 on 13.12.2017.
 */

public class BoundingBoxScene extends Scene{


    private VuforiaMarkerNode markerCube;
    private     VuforiaMarkerNode markerSphere;

    private     Vector positionCubeC;
    private     Vector positionSphereS;

    private     TranslationNode translationCube;
    private     TranslationNode translationSphere;

    private    TransformationNode transformationCube;
    private    TransformationNode transformationSphere;

    private     BoundingBoxNode boundingBoxCube;
    private    BoundingBoxNode boundingBoxSphere;

    private     TriangleMeshNode cube;
    private     TriangleMeshNode sphere;

    private     ITriangleMesh cubeMesh;
    private     ITriangleMesh sphereMesh;

    private    double sphereRadius;

    public BoundingBoxScene() {
        super(100, INode.RenderMode.REGULAR);

        positionCubeC = new Vector(0,0,0,1);
        positionSphereS = new Vector(0,0,0,1);

        markerCube = new VuforiaMarkerNode("campus");
        markerSphere = new VuforiaMarkerNode("elphi");

        translationCube = new TranslationNode(positionCubeC);
        translationSphere = new TranslationNode(positionSphereS);

        transformationCube = new TransformationNode();
        transformationSphere = new TransformationNode();

        sphereRadius = 0.5;

    }

    @Override
    public void onSetup(InnerNode rootNode) {

        cubeMesh = new TriangleMesh();
        TriangleMeshFactory.createCube(cubeMesh);
        cube = new TriangleMeshNode(cubeMesh);

        sphereMesh = new TriangleMesh();
        TriangleMeshFactory.createSphere(sphereMesh,sphereRadius,7);
        sphere = new TriangleMeshNode(sphereMesh);

        boundingBoxCube = new BoundingBoxNode(cube.getBoundingBox());
        boundingBoxSphere = new BoundingBoxNode(sphere.getBoundingBox());


        markerCube.addChild(translationCube);
        markerSphere.addChild(translationSphere);

        translationCube.addChild(transformationCube);
        transformationCube.addChild(boundingBoxCube);
        transformationCube.addChild(cube);

        translationSphere.addChild(transformationSphere);
        transformationSphere.addChild(boundingBoxSphere);
        transformationSphere.addChild(sphere);

        rootNode.addChild(markerCube);
        rootNode.addChild(markerSphere);

    }

    boolean collide(AxisAlignedBoundingBox bbox1, Matrix transformation1,
                    AxisAlignedBoundingBox bbox2, Matrix transformation2){


        Matrix transformationToWorld1 = transformation1.getInverse();
        Matrix transformationToWorld2 = transformation2.getInverse();

        Vector ur1 = transformationToWorld1.multiply(bbox1.getUR());
        Vector ur2 = transformationToWorld2.multiply(bbox2.getUR());
        Vector ll1 = transformationToWorld1.multiply(bbox1.getLL());
        Vector ll2 = transformationToWorld2.multiply(bbox2.getLL());

        //bbox1.transform(transformation1) alternativ

        if( ur1.x()>ll2.x() && ur2.x()>ll1.x()){
            if( ur1.y()>ll2.y() && ur2.y()>ll1.y()){
                if( ur1.z()>ll2.z() && ur2.z()>ll1.z()){
                    return true;
                }
            }
        }
        return false;
    }

    private double getDistance(Vertex a, Vertex b, Vertex c, Vector center){
        //normal plane
        Vector ab = b.getPosition().subtract(a.getPosition());
        Vector ac = c.getPosition().subtract(a.getPosition());


        Vector planeNormal = ab.cross(ac);
        planeNormal.normalize();

        Vector am = a.getPosition().subtract(center);

        double lamdax = am.x()/planeNormal.x();
        double lamday = am.y()/planeNormal.y();
        double lamdaz = am.z()/planeNormal.z();

        double lamda = lamdax+lamday+lamdaz;

        Vector x = center.add(planeNormal.multiply(lamda));

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

        double acenter = center.subtract(a.getPosition()).getNorm();
        double bcenter = center.subtract(b.getPosition()).getNorm();
        double ccenter = center.subtract(c.getPosition()).getNorm();


        return Math.min(Math.min(acenter,bcenter),ccenter);
    }

    boolean collideExact(ITriangleMesh mesh, Matrix meshTransformation,
                         Vector center, double radius, Matrix sphereTransformation){
        double distance = 0.0;
        AbstractTriangle triangle;

        for(int i=0;i<mesh.getNumberOfTriangles();i++){
            triangle = mesh.getTriangle(i);
            distance = getDistance(mesh.getVertex(triangle,0),mesh.getVertex(triangle,1),mesh.getVertex(triangle,2),center);
            if(distance<radius){
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
        if(collide(boundingBoxCube.getBoundingBox(),
                cube.getTransformation(),
                boundingBoxSphere.getBoundingBox(),
                sphere.getTransformation())){

            //farbe gelb
            //TO-DO find colors
            boundingBoxCube.setColor(null);
            boundingBoxSphere.setColor(null);



            if(collideExact(cubeMesh,cube.getTransformation(),positionSphereS,sphereRadius,sphere.getTransformation())){
                //farbe roooooooot
            }


        } else {
            //farbe grün
        }
    }
}
