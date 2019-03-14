package edu.hawhamburg.shared.importer.util;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import edu.hawhamburg.shared.datastructures.mesh.Triangle;
import edu.hawhamburg.shared.datastructures.mesh.TriangleMesh;
import edu.hawhamburg.shared.datastructures.mesh.Vertex;
import edu.hawhamburg.shared.importer.skeleton.Joint;
import edu.hawhamburg.shared.importer.skeleton.KeyFrameMap;
import edu.hawhamburg.shared.importer.skeleton.SkeletalAnimatedMesh;
import edu.hawhamburg.shared.importer.skeleton.Skeleton;
import edu.hawhamburg.shared.importer.skeleton.SkeletonAnimationController;
import edu.hawhamburg.shared.importer.skeleton.VertexWeightController;
import edu.hawhamburg.shared.math.Matrix;
import edu.hawhamburg.shared.math.Vector;

public class ColladaImporter2 {

    private List<Vector> positions = new ArrayList<>();
    public static void main(String... args){
        ColladaImporter2 colladaImporter = new ColladaImporter2();
        //SkeletalAnimatedMesh skeletalAnimatedMesh = colladaImporter.importColladaFile("C:\\Users\\Devran\\AndroidStudioProjects\\cg_vr_ar\\cg_vr_ar_android\\lib_shared\\src\\main\\java\\edu\\hawhamburg\\shared\\importer\\resources\\cowboy.dae");
        SkeletalAnimatedMesh skeletalAnimatedMesh = colladaImporter.importColladaFile("C:\\Users\\Devran\\AndroidStudioProjects\\new_cg\\wpcgar\\android\\lib_shared\\src\\main\\java\\edu\\hawhamburg\\shared\\importer\\resources\\cowboy.dae");
        Skeleton skeleton = skeletalAnimatedMesh.getSkeleton();
        Joint j = skeleton.getJointIndexed().get(2);


        //ColladaImporter colladaImporter = new ColladaImporter("C:/Users/Devran/AndroidStudioProjects/TestAppl/app/src/main/java/resources/Wolf_dae.dae");

    }

    public Node getNodeByName(NodeList nodeList, String nodeName){
        Node node = null;
        for(int i=0;i<nodeList.getLength();i++){
            if(nodeList.item(i).getNodeName().equals(nodeName)){
                node=nodeList.item(i);
            }
        }
        return node;
    }

    public Node getNodeByContainsString(NodeList nodeList, String nodeName){
        Node node = null;
        for(int i=0;i<nodeList.getLength();i++){
            if(nodeList.item(i).getNodeName().contains(nodeName)){
                node=nodeList.item(i);
            }
        }
        return node;
    }

    public String getAttributeFromNodeByKey2(Node node, String key){
        if(!node.hasAttributes()){
            return "";
        }
        Node tmp = node.getAttributes().getNamedItem(key);
        String tmpStr ="";
        if(tmp!=null){
            tmpStr = tmp.toString();
            tmpStr = tmpStr.replace(key+"=","").replace("\"","");
            return tmpStr;
        } else {
            return "";
        }
    }

    public String getAttributeFromNodeByKey(Node node, String key){
        if(!node.hasAttributes()){
            return "";
        }
        Node tmp = node.getAttributes().getNamedItem(key);
        String tmpStr ="";
        if(tmp!=null){
            tmpStr = tmp.getTextContent();
            tmpStr = tmpStr.replace(key+"=","").replace("\"","");
            return tmpStr;
        } else {
            return "";
        }
    }


    public boolean nodeHasAttribute(Node node, String key, String value){
        if(!node.hasAttributes()){
            return false;
        }
        Node tmp = node.getAttributes().getNamedItem(key);
        String tmpStr ="";
        if(tmp!=null){
            tmpStr = tmp.getTextContent();
            if(tmpStr.contains(value)) {
                return true;
            }
        }

        return false;
    }

    public boolean nodeHasAttribute(Node node, String key){
        if(!node.hasAttributes()){
            return false;
        }
        Node tmp = node.getAttributes().getNamedItem(key);
        if(tmp!=null){
            return true;
        }
        return false;
    }

    public void printNodeList(NodeList nodeList){
        for(int i=0;i<nodeList.getLength();i++){
            System.out.println(nodeList.item(i).getNodeName());
        }
    }

    public void printNodeAttribute(Node node){
        for(int i=0;i<node.getAttributes().getLength();i++){
            System.out.println(node.getAttributes().item(i));
        }
    }

    public int getPolyStructureOffset(NodeList nodeList){
        int offset=0;
        for(int i=0;i<nodeList.getLength();i++){
            if(nodeHasAttribute(nodeList.item(i),"offset")){
                String str = nodeList.item(i).getAttributes().getNamedItem("offset").getTextContent();
                str=str.replace("offset=","").replace("\"","");
                offset=Math.max(offset,Integer.parseInt(str));
            }
        }
        return offset;
    }

    public Node getNodeByAttribute2(NodeList nodeList, String key, String value){
        Node node = null;

        for(int i=0;i<nodeList.getLength();i++){
            if(nodeHasAttribute(nodeList.item(i),key,value)){
                node=nodeList.item(i);
            }
        }
        return node;
    }

    public Node getNodeByAttribute(NodeList nodeList, String key, String value){
        Node node = null;
        for(int i=0;i<nodeList.getLength();i++){
            if( nodeList.item(i).getAttributes()!=null){
                NamedNodeMap nnm = nodeList.item(i).getAttributes();
                Node attrNode = nnm.getNamedItem(key);
                if(attrNode!=null){
                    //hier mit contains versuchen todo
                    if(attrNode.getTextContent().equalsIgnoreCase(value)){
                        //System.out.println("Found the Node");
                        //System.out.println(attrNode.getTextContent());
                        node=nodeList.item(i);
                    }
                }
            }
        }
        return node;
    }

    public NamedNodeMap findNamedNodeMapByKeyAndValue(NodeList nodeList, String key, String value){

        for(int i=0;i<nodeList.getLength();i++){
            if( nodeList.item(i).getAttributes()!=null){
                NamedNodeMap nnm = nodeList.item(i).getAttributes();
                Node attrNode = nnm.getNamedItem(key);
                if(attrNode!=null){
                    if(attrNode.getTextContent().equalsIgnoreCase(value)){
                        System.out.println(attrNode.getTextContent());
                        return nnm;
                    }
                }
            }
        }
        return null;
    }


    public String getSourceFromNode(Node node){
        String notStripped = node.getAttributes().getNamedItem("source").getTextContent();
        return notStripped.replace("source=","").replace("\"","").replace("#","");
    }

    public String cleanNodeContext(Node node){
        String notStripped = node.getTextContent();
        return notStripped.replace("source=","").replace("\"","").replace("#","");
    }

    public List<String> removeElements(String[] input, String delete) {
        List<String> result = new ArrayList<>();

        for(String item : input)
            if(!delete.equals(item))
                result.add(item);
        return result;
    }

    public ColladaImporter2(){
    }

    public SkeletalAnimatedMesh importColladaFile(String filepath){
        Document doc = readColladaFile(filepath);
        TriangleMesh mesh = readMeshFromColladaFile(doc);
        Skeleton skeleton = readSkeletonFromColladaFile(doc);
        SkeletonAnimationController skeletonAnimationController = readAnimationControlFromColladaFile(doc,mesh,skeleton);
        SkeletalAnimatedMesh skeletalAnimatedMesh = new SkeletalAnimatedMesh(mesh,skeleton,skeletonAnimationController);

        return skeletalAnimatedMesh;
    }

    public SkeletalAnimatedMesh importColladaFile(Document doc){
        TriangleMesh mesh = readMeshFromColladaFile(doc);
        Skeleton skeleton = readSkeletonFromColladaFile(doc);
        SkeletonAnimationController skeletonAnimationController = readAnimationControlFromColladaFile(doc,mesh,skeleton);

        SkeletalAnimatedMesh skeletalAnimatedMesh = new SkeletalAnimatedMesh(mesh,skeleton,skeletonAnimationController);

        return skeletalAnimatedMesh;
    }

    public static Document readColladaFile(String filepath) {
        File colladaFile = new File(filepath);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = null;
        Document doc = null;

        try {
            dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(colladaFile);
        } catch (IOException | SAXException | ParserConfigurationException e) {
            e.printStackTrace();
        }
        return doc;
    }

    public static Document readColladaFile(InputStream is) {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = null;
        Document doc = null;

        try {
            dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(is);
        } catch (IOException | SAXException | ParserConfigurationException e) {
            e.printStackTrace();
        }
        return doc;
    }

    public static Document readColladaFile(File colladaFile) {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = null;
        Document doc = null;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(colladaFile);
        } catch (IOException | SAXException | ParserConfigurationException e) {
            e.printStackTrace();
        }
        return doc;
    }

    public TriangleMesh readMeshFromColladaFile(Document document){
        Node libraryGeometries = document.getElementsByTagName("library_geometries").item(0);
        Node geometry = getNodeByName(libraryGeometries.getChildNodes(),"geometry");
        Node meshNode = getNodeByName(geometry.getChildNodes(),"mesh");

        Node polyList = getNodeByName(meshNode.getChildNodes(),"polylist");

        Node normalsFromPolyList = getNodeByAttribute(polyList.getChildNodes(),"semantic","NORMAL");
        String normalSourceName = getSourceFromNode(normalsFromPolyList);
        Node verticiesFromPolyList = getNodeByAttribute(polyList.getChildNodes(),"semantic", "VERTEX");
        String verticiesSourceName = getSourceFromNode(verticiesFromPolyList);


        //verticies
        Node verticiesNode = getNodeByAttribute(meshNode.getChildNodes(),"id",verticiesSourceName);
        Node positionFromVerticiesNode = getNodeByAttribute(verticiesNode.getChildNodes(),"semantic","POSITION");
        String positionSourceName = getSourceFromNode(positionFromVerticiesNode);

        Node positionNode = getNodeByAttribute(meshNode.getChildNodes(),"id",positionSourceName);
        Node positionArrayNode = getNodeByAttribute(positionNode.getChildNodes(),"id",positionSourceName+"-array");

        String[] positionElementArrayAsString = positionArrayNode.getTextContent().split(" ");
        double[] positionElementArray = new double[positionElementArrayAsString.length];
        int amountOfPositionsElements = Integer.valueOf(getAttributeFromNodeByKey(positionArrayNode,"count"));
        int amountOfPosition = amountOfPositionsElements/3;//3 weil 3 dimensional

        //normals
        Node normals = getNodeByAttribute(meshNode.getChildNodes(),"id",normalSourceName);
        Node normalsArrayNode = getNodeByAttribute(normals.getChildNodes(),"id",normalSourceName+"-array");
        String[] normalsElementsArrayAsString = normals.getTextContent().split(" ");
        List<String> normalsElementList = new ArrayList<>();

        //todo mit cleanuparray methode lösen
        //cleanup empty elements
        for(int i=0;i<normalsElementsArrayAsString.length;i++){
            if(!normalsElementsArrayAsString[i].isEmpty()&&!normalsElementsArrayAsString[i].equals("\n")){
                normalsElementList.add(normalsElementsArrayAsString[i]);
            }
        }

        double[] normalsElementArray = new double[normalsElementList.size()];
        int amountOfNormalsElements = Integer.valueOf(getAttributeFromNodeByKey(normalsArrayNode,"count"));
        int amountOfNormals = amountOfNormalsElements/3; //3 weil 3 dimensional


        for(int i=0;i<amountOfPositionsElements;i++){
            positionElementArray[i]=Double.valueOf(positionElementArrayAsString[i]);
        }

        for(int i=0;i<amountOfNormalsElements;i++){
            normalsElementArray[i]=Double.valueOf(normalsElementList.get(i));
        }

        //erstelle listen mit position/normals aus den listen mit den elementen
        Vector[] positionArray = new Vector[amountOfPosition];
        Vector[] normalsArray = new Vector[amountOfNormals];

        for(int i=0;i<amountOfPosition;i++){
            positionArray[i]=new Vector(positionElementArray[i*3],positionElementArray[i*3+1],positionElementArray[i*3+2]);
         }
        System.out.println("-----------------------");
        System.out.println(positionArray[0]);
        System.out.println(positionArray[amountOfPosition-1]);

        for(int i=0;i<amountOfNormals;i++){
            normalsArray[i]=new Vector(normalsElementArray[i*3],normalsElementArray[i*3+1],normalsElementArray[i*3+2]);
        }
        System.out.println("-----------------------");
        System.out.println(normalsArray[0]);
        System.out.println(normalsArray[amountOfNormals-1]);


        //verticies/normals/mesh erstellen
        //todo magic number 12 raus, generisch für alle mesh machen
        Node triangleComposition = getNodeByName(polyList.getChildNodes(),"p");
        String[] triangleCompositionElementsArray = triangleComposition.getTextContent().split(" ");
        int[] triangleCompositionArray =  new int[triangleCompositionElementsArray.length];
        int amountOfTriangleCompositionElements = triangleCompositionElementsArray.length;
        int amountOfTriangles = amountOfTriangleCompositionElements/12;



        for(int i=0;i<amountOfTriangleCompositionElements;i++){
            triangleCompositionArray[i]=Integer.valueOf(triangleCompositionElementsArray[i]);
        }


        //offset im aufbau der triangle - vertecies
        int offset = getPolyStructureOffset(polyList.getChildNodes());
        int absoluteOffset = offset+1;
        int amountOfVerticies = Integer.valueOf(getAttributeFromNodeByKey(polyList,"count"));
        List<Vertex> vertexList = new ArrayList<>();
        List<Triangle> triangleList = new ArrayList<>();

        //erstellen der verticies und triangles durch indizes
        for(int i=0;i<amountOfTriangles;i++){
            Vertex v1 = new Vertex(positionArray[triangleCompositionArray[i*12]],normalsArray[triangleCompositionArray[i*12+1]]);
            Vertex v2 = new Vertex(positionArray[triangleCompositionArray[i*12+absoluteOffset]],normalsArray[triangleCompositionArray[i*12+absoluteOffset+1]]);
            Vertex v3 = new Vertex(positionArray[triangleCompositionArray[i*12+absoluteOffset*2]],normalsArray[triangleCompositionArray[i*12+absoluteOffset*2+1]]);
            vertexList.add(v1);
            vertexList.add(v2);
            vertexList.add(v3);

            triangleList.add(new Triangle(vertexList.indexOf(v1),vertexList.indexOf(v2),vertexList.indexOf(v3)));

        }

        System.out.println(vertexList.size());
        System.out.println(triangleList.size());

        TriangleMesh mesh = new TriangleMesh();

        for(int i=0;i<vertexList.size();i++){
            mesh.addVertex(vertexList.get(i));
        }
        for(int i=0;i<triangleList.size();i++){
            mesh.addTriangle(triangleList.get(i));
        }



        int duplicatePositions=0;
        int firstDuplicateIndex=0;
        int secondDuplicateIndex=0;
        boolean firstDuplicateIndexFound=false;

        for(int i=0;i<vertexList.size();i++){
            for(int j=0;j<vertexList.size();j++){
                if((i!=j)&&(vertexList.get(i).getPosition().equals(vertexList.get(j).getPosition()))){
                    duplicatePositions++;
                    if(!firstDuplicateIndexFound){
                        firstDuplicateIndex=j;
                        secondDuplicateIndex=i;
                        firstDuplicateIndexFound=!firstDuplicateIndexFound;
                        System.out.println("First duplicate found");
                    }
                }

            }
        }

        System.out.println(firstDuplicateIndex);
        System.out.println(secondDuplicateIndex);
        System.out.println("Duplicate positions: " + duplicatePositions);
        System.out.println(vertexList.get(0).getPosition());
        System.out.println(vertexList.get(78).getPosition());

        System.out.println("Triangle 0");
        //System.out.println(mesh.getTriangle(0).getVertexIndices()[0]);



        this.positions= Arrays.asList(positionArray);



        return mesh;
    }

    public Node findFirstSkeletonJoint(Node node){
        for(int i=0;i<node.getChildNodes().getLength();i++){
            for(int j=0;j<node.getChildNodes().item(i).getChildNodes().getLength();j++){
                if(nodeHasAttribute(node.getChildNodes().item(i).getChildNodes().item(j),"type","JOINT")){
                    return node.getChildNodes().item(i).getChildNodes().item(j);
                }
            }
        }
        return null;
    }

    public List<Node> findJointNodes(Node node, List<Node> toFillList){
            for(int i=0;i<node.getChildNodes().getLength();i++){
                if(nodeHasAttribute(node.getChildNodes().item(i),"type","JOINT")){
                    toFillList.add(node.getChildNodes().item(i));
                    if(node.hasChildNodes()){
                            toFillList=findJointNodes(node.getChildNodes().item(i),toFillList);
                        }
                }
            }
        return toFillList;
    }

    public Node findSkeletonEntryNode(Node node){
        for(int i=0;i<node.getChildNodes().getLength();i++){
            for(int j=0;j<node.getChildNodes().item(i).getChildNodes().getLength();j++){
                if(nodeHasAttribute(node.getChildNodes().item(i).getChildNodes().item(j),"type","JOINT")){
                    return node.getChildNodes().item(i);
                }
            }
        }
        return null;
    }

    public boolean isChildOf(Node n2, Node n1){
        if(n2.getParentNode().equals(n1)){
            return true;
        }
        return false;
    }

    public Matrix getMatrixFromNode(Node node){

        for(int i=0;i<node.getChildNodes().getLength();i++){
            if(nodeHasAttribute(node.getChildNodes().item(i),"sid","transform")){

                String[] matrixAsStringArr = node.getChildNodes().item(i).getTextContent().replace("\n","").split(" ");
                List<Double> matrixAsStringList = new ArrayList<>();
                for(int j=0;j<matrixAsStringArr.length;j++){
                    if(!matrixAsStringArr[j].isEmpty()){
                        matrixAsStringList.add(Double.valueOf(matrixAsStringArr[j]));
                    }
                }


                return new Matrix(matrixAsStringList.get(0),matrixAsStringList.get(1),
                        matrixAsStringList.get(2),matrixAsStringList.get(3),
                        matrixAsStringList.get(4),matrixAsStringList.get(5),
                        matrixAsStringList.get(6),matrixAsStringList.get(7),
                        matrixAsStringList.get(8),matrixAsStringList.get(9),
                        matrixAsStringList.get(10),matrixAsStringList.get(11),
                        matrixAsStringList.get(12),matrixAsStringList.get(13),
                        matrixAsStringList.get(14),matrixAsStringList.get(15));
                //todo nicht sicher ob immer transponiert
            }
        }

        return null;
    }

    public Skeleton createSkeletonFromNodeList(List<Node> nodeList){
        List<Joint> jointIndexed = new ArrayList<>();
        for(int i=0;i<nodeList.size();i++){
            jointIndexed.add(createJointFromNode(nodeList.get(i)));
        }

        for(int i=0;i<nodeList.size();i++){
            for(int j=0;j<nodeList.size();j++){
                if(i!=j){
                    if(isChildOf(nodeList.get(j),nodeList.get(i))){
                        jointIndexed.get(j).setParentJoint(jointIndexed.get(i));
                    }
                }
            }
        }

        return new Skeleton(jointIndexed.get(0),jointIndexed);
    }

    public Joint createJointFromNode(Node node){
        String name = getAttributeFromNodeByKey(node,"id");
        //andere matrize und inversen selbst berechnen
        Matrix transform = getMatrixFromNode(node);
        Joint joint = new Joint(name,transform);
        return joint;
    }

    public Skeleton readSkeletonFromColladaFile(Document document){

        Node libraryVisualScenes = document.getElementsByTagName("library_visual_scenes").item(0);
        Node visualSceneNode = getNodeByAttribute(libraryVisualScenes.getChildNodes(),"id","Scene");
        Node skeletonEntryNode = findSkeletonEntryNode(visualSceneNode);
        List<Node> jointNodes = new ArrayList<>();
        jointNodes = findJointNodes(skeletonEntryNode,jointNodes);
        Skeleton skeleton = createSkeletonFromNodeList(jointNodes);
        String jointPrefix = getAttributeFromNodeByKey(skeletonEntryNode,"name");


        //todo mit firstskeletonjoint.getparentnode ggf. eine matrix zum initialen platzieren finden
        //System.out.println(firstSkeletonJoint.getParentNode().getNodeName());

        for (int i=0;i<skeleton.getJointIndexed().size();i++){
            skeleton.getJointIndexed().get(i).setName(jointPrefix+"_"+skeleton.getJointIndexed().get(i).getName());
        }

        Node libraryControllers = document.getElementsByTagName("library_controllers").item(0);
        Node controllerNode = getNodeByName(libraryControllers.getChildNodes(),"controller");
        Node skinNode = getNodeByName(controllerNode.getChildNodes(),"skin");
        Node jointsNode = getNodeByName(skinNode.getChildNodes(),"joints");
        printNodeAttribute(controllerNode);
        Node invBindMatrixSourceNode = getNodeByAttribute(jointsNode.getChildNodes(),"semantic","INV_BIND_MATRIX");
        String invBindMatrixSource = getSourceFromNode(invBindMatrixSourceNode);
        Node invBindMatrixNode = getNodeByAttribute(skinNode.getChildNodes(),"id",invBindMatrixSource);

        String[] invBindMatrixAsStringArr = invBindMatrixNode.getTextContent().replace("\n","").split(" ");
        List<Double> invBindMatrixAsStringList = new ArrayList<>();
        for(int j=0;j<invBindMatrixAsStringArr.length;j++){
            if(!invBindMatrixAsStringArr[j].isEmpty()){
                invBindMatrixAsStringList.add(Double.valueOf(invBindMatrixAsStringArr[j]));
            }
        }

        for(int i=0;i<skeleton.getJointIndexed().size();i++){
            skeleton.getJointIndexed().get(i).setInversBindMatrix(new Matrix(invBindMatrixAsStringList.get(16*i),invBindMatrixAsStringList.get(16*i+1),
                    invBindMatrixAsStringList.get(16*i+2),invBindMatrixAsStringList.get(16*i+3),
                    invBindMatrixAsStringList.get(16*i+4),invBindMatrixAsStringList.get(16*i+5),
                    invBindMatrixAsStringList.get(16*i+6),invBindMatrixAsStringList.get(16*i+7),
                    invBindMatrixAsStringList.get(16*i+8),invBindMatrixAsStringList.get(16*i+9),
                    invBindMatrixAsStringList.get(16*i+10),invBindMatrixAsStringList.get(16*i+11),
                    invBindMatrixAsStringList.get(16*i+12),invBindMatrixAsStringList.get(16*i+13),
                    invBindMatrixAsStringList.get(16*i+14),invBindMatrixAsStringList.get(16*i+15)));
            //todo nicht sicher ob immer transponieren
        }



        skeleton=addAnimationInterpolationToSkeleton(document, skeleton);

        return skeleton;
    }

    //todo zu generic machen leere einträge erkennen und löschen (stirng.emtpy oder null)
    public String[] cleanUpStringArray(String[] stringArray){
        List<String> stringList = new ArrayList<>();
        for(int i=0;i<stringArray.length;i++){
            if (!stringArray[i].isEmpty()&&!stringArray[i].equals("\n")){
                stringList.add(stringArray[i]);
            }
        }
        String[] retArr = new String[stringList.size()];
        for(int i=0;i<stringList.size();i++){
            retArr[i]=stringList.get(i);
        }

        return retArr;
    }


    public Skeleton addAnimationInterpolationToSkeleton(Document document, Skeleton skeleton){
       Node libraryAnimation = document.getElementsByTagName("library_animations").item(0);
       NodeList animationInformationNodes = libraryAnimation.getChildNodes();
       String jointAnimationInformationSuffix = "_pose_matrix";


       for(int i=0;i<skeleton.getJointIndexed().size();i++){
           Node jointAnimationInformationNode = getNodeByAttribute(animationInformationNodes,"id",skeleton.getJointIndexed().get(i).getName()+jointAnimationInformationSuffix);
           Node samplerNode = getNodeByAttribute(jointAnimationInformationNode.getChildNodes(),"id",skeleton.getJointIndexed().get(i).getName()+jointAnimationInformationSuffix+"-sampler");

           // Node samplerNode = getNodeByAttribute(jointAnimationInformationNode.getChildNodes(),"id","sampler");
           Node inputFromSamplerNode = getNodeByAttribute(samplerNode.getChildNodes(),"semantic","INPUT");
           String inputSourceName = getSourceFromNode(inputFromSamplerNode);
           Node outputFromSamplerNode = getNodeByAttribute(samplerNode.getChildNodes(),"semantic","OUTPUT");
           String outputSourceName = getSourceFromNode(outputFromSamplerNode);

           Node animationTimeKeysNode = getNodeByAttribute(jointAnimationInformationNode.getChildNodes(),"id",inputSourceName);
           Node animationTransformationsNode = getNodeByAttribute(jointAnimationInformationNode.getChildNodes(),"id",outputSourceName);

           String[] animationTimeKeysStringArr = animationTimeKeysNode.getTextContent().replace("\n","").split(" ");
           List<Double> animationTimeKeysList = new ArrayList<>();
           for(int j=0;j<animationTimeKeysStringArr.length;j++){
               if(!animationTimeKeysStringArr[j].isEmpty()){
                   animationTimeKeysList.add(Double.valueOf(animationTimeKeysStringArr[j]));
               }
           }
           //get stride

           String[] animationTransformationsStringArr = animationTransformationsNode.getTextContent().replace("\n","").split(" ");
           List<Double> animationTransformationsList = new ArrayList<>();
           List<Matrix> animationTransformationMatrices = new ArrayList<>();
           for(int j=0;j<animationTransformationsStringArr.length;j++){
               if(!animationTransformationsStringArr[j].isEmpty()){
                   animationTransformationsList.add(Double.valueOf(animationTransformationsStringArr[j]));
               }
           }
           for(int j=0;j<animationTransformationsList.size();j+=16){
               animationTransformationMatrices.add(new Matrix(animationTransformationsList.get(j),animationTransformationsList.get(j+1),
                       animationTransformationsList.get(j+2),animationTransformationsList.get(j+3),
                       animationTransformationsList.get(j+4),animationTransformationsList.get(j+5),
                       animationTransformationsList.get(j+6),animationTransformationsList.get(j+7),
                       animationTransformationsList.get(j+8),animationTransformationsList.get(j+9),
                       animationTransformationsList.get(j+10),animationTransformationsList.get(j+11),
                       animationTransformationsList.get(j+12),animationTransformationsList.get(j+13),
                       animationTransformationsList.get(j+14),animationTransformationsList.get(j+15)));
               //todo nicht immer sicher ob transponiert
           }

           for(int j=0;j<animationTimeKeysList.size();j++){
               skeleton.getJointIndexed().get(i).addKeyFrame(animationTimeKeysList.get(j),animationTransformationMatrices.get(j));
           }

       }



        return skeleton;
    }


    public SkeletonAnimationController readAnimationControlFromColladaFile(Document document, TriangleMesh mesh, Skeleton skeleton){
        Node libraryAnimationContol = document.getElementsByTagName("library_controllers").item(0);
        Node controllerNode = getNodeByName(libraryAnimationContol.getChildNodes(),"controller");
        Node skinNode = getNodeByName(controllerNode.getChildNodes(),"skin");

        //todo gibt die inverser bind matrix
        Node vertexSkinningInformationNode = getNodeByName(skinNode.getChildNodes(),"vertex_weights");
        Node linkToVertexWeightsSourceNode = getNodeByAttribute(vertexSkinningInformationNode.getChildNodes(),"semantic","WEIGHT");
        String sourceOfVertexWeightsNode = getSourceFromNode(linkToVertexWeightsSourceNode);
        String sourceOfVertexWeightsArray = sourceOfVertexWeightsNode+"-array";



        //todo warum geht es mit sourceofvertexweightsnodes und nicht mit dem array
        //Node vertexWeightsNode = getNodeByName(skinNode.getChildNodes(),sourceOfVertexWeightsNode);
        Node vertexWeightsNode = getNodeByAttribute(skinNode.getChildNodes(),"id",sourceOfVertexWeightsNode);
        String[] vertexWeights = cleanUpStringArray(vertexWeightsNode.getTextContent().split(" "));


        //offset für die vertex-joints-weights beziehung herausfinden
        int offset = getPolyStructureOffset(vertexSkinningInformationNode.getChildNodes());
        int amountOfVertexWeights = Integer.valueOf(getAttributeFromNodeByKey(vertexSkinningInformationNode,"count"));


        String[] vertexAffectedByJointsCount = getNodeByName(vertexSkinningInformationNode.getChildNodes(), "vcount").getTextContent().split(" ");
        String[] vertexToJointAndWeightAssignment = getNodeByName(vertexSkinningInformationNode.getChildNodes(), "v").getTextContent().split(" ");

        System.out.println("----------------------vertex joint weight---------");
        System.out.println(vertexAffectedByJointsCount[478]);
        System.out.println(amountOfVertexWeights);
        System.out.println(vertexToJointAndWeightAssignment.length);

        /*
        *Vertexgruppen bauen
         */

        List<Vector> vertexGroupList = new ArrayList<>();
        for(int i=0;i<positions.size();i++){
            if(!vertexGroupList.contains(positions.get(i))){
                vertexGroupList.add(positions.get(i));
            }
        }
        System.out.println("Size of Groups: " + vertexGroupList.size());

        VertexWeightController[] vertexWeightGroupControllers = new VertexWeightController[vertexGroupList.size()];
        VertexWeightController[] vertexWeightControllers = new VertexWeightController[mesh.getNumberOfVertices()];

        System.out.println("Amount of vertex weights");
        System.out.println(vertexAffectedByJointsCount.length);
        for(int i=0;i<vertexAffectedByJointsCount.length;i++){
         //   System.out.println("V "+i+": "+vertexAffectedByJointsCount[i]);
        }



        int vertexToJointAndWeightAssignmentPosition=0;
        for(int i=0;i<vertexWeightGroupControllers.length;i++) {
            List<Integer> jointsThatAffectsVertex = new ArrayList<>();
            List<Float> weightThatAffectsVertex = new ArrayList<>();

            for (int j = 0; j < Integer.valueOf(vertexAffectedByJointsCount[i]); j++) {

                //System.out.println(vertexToJointAndWeightAssignmentPosition);
                jointsThatAffectsVertex.add(Integer.valueOf(vertexToJointAndWeightAssignment[vertexToJointAndWeightAssignmentPosition]));
                vertexToJointAndWeightAssignmentPosition++;
               // System.out.println(vertexToJointAndWeightAssignmentPosition);
                weightThatAffectsVertex.add(Float.valueOf(vertexWeights[Integer.valueOf(vertexToJointAndWeightAssignment[vertexToJointAndWeightAssignmentPosition])]));
                vertexToJointAndWeightAssignmentPosition++;
            }
            vertexWeightGroupControllers[i] = new VertexWeightController(jointsThatAffectsVertex, weightThatAffectsVertex);
        }
            System.out.println(vertexToJointAndWeightAssignment.length);

        for(int i=0;i<vertexGroupList.size();i++){
            for(int j=0;j<mesh.getNumberOfVertices();j++){
                if(vertexGroupList.get(i).equals(mesh.getVertex(j).getPosition())){
                    vertexWeightControllers[j]=vertexWeightGroupControllers[i];
                    //System.out.println("Matching Vertexgroup: "+vertexGroupList.get(i));
                    //System.out.println("And Vertex"+j+" with: "+mesh.getVertex(j).getPosition());
                }
            }
        }

        for(int i=0;i<vertexWeightControllers.length;i++){
            //System.out.println("VertexWeightController: "+i+"\n"+ vertexWeightControllers[i]);
        }

        List<KeyFrameMap> keyFrames = new ArrayList<>();
        for(int i=0;i<skeleton.getJointIndexed().size();i++){
            keyFrames.add(new KeyFrameMap(skeleton.getJoint(i).getKeyFrame()));
        }
        SkeletonAnimationController skeletonAnimationController = new SkeletonAnimationController(vertexWeightControllers,vertexGroupList,keyFrames);

        return skeletonAnimationController;
    }

    public static SkeletalAnimatedMesh getStub(){
        return null;
    }
}
