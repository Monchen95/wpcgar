package edu.hawhamburg.shared.importer.skeleton;

import java.util.List;

import edu.hawhamburg.shared.datastructures.mesh.TriangleMesh;
import edu.hawhamburg.shared.datastructures.mesh.Vertex;
import edu.hawhamburg.shared.importer.math.DualQuaternion;
import edu.hawhamburg.shared.math.Matrix;
import edu.hawhamburg.shared.math.Vector;


public class SkeletalAnimatedMesh {
    private TriangleMesh meshInBindPose;
    private TriangleMesh mesh;
    private Skeleton skeleton;
    private Skeleton defaultSkeleton;
    private SkeletonAnimationController skeletonAnimationController;
    private AnimationHelper animationHelper;
    int checknumber = 300;
    boolean show = false;

    public SkeletalAnimatedMesh(TriangleMesh mesh, Skeleton skeleton, SkeletonAnimationController skeletonAnimationController) {
        //create reference mesh
        meshInBindPose=new TriangleMesh();
        for(int i=0;i<mesh.getNumberOfTriangles();i++){
            meshInBindPose.addTriangle(mesh.getTriangle(i).getVertexIndex(0),
                    mesh.getTriangle(i).getVertexIndex(1),
                    mesh.getTriangle(i).getVertexIndex(2));
        }

        for(int i=0;i<mesh.getNumberOfVertices();i++){
            meshInBindPose.addVertex(new Vertex(new Vector(mesh.getVertex(i).getPosition().x(),
                    mesh.getVertex(i).getPosition().y(),
                    mesh.getVertex(i).getPosition().z()),
                    new Vector(mesh.getVertex(i).getNormal().x(),
                            mesh.getVertex(i).getNormal().y(),
                            mesh.getVertex(i).getNormal().z())));
        }

        this.mesh = mesh;
        this.defaultSkeleton = skeleton;
        this.skeleton = skeleton;
        this.skeletonAnimationController = skeletonAnimationController;
        this.animationHelper= new AnimationHelper();
        //skeleton.rotateX(90);
    }

    public TriangleMesh getMesh() {
        return mesh;
    }

    public void setMesh(TriangleMesh mesh) {
        this.mesh = mesh;
    }

    public Skeleton getSkeleton() {
        return skeleton;
    }

    public void setSkeleton(Skeleton skeleton) {
        this.skeleton = skeleton;
    }

    public SkeletonAnimationController getSkeletonAnimationController() {
        return skeletonAnimationController;
    }

    public void setSkeletonAnimationController(SkeletonAnimationController skeletonAnimationController) {
        this.skeletonAnimationController = skeletonAnimationController;
    }


    /**
     * @param
     *  delta raus, einfach das mesh animieren mit vector.copy
     *           meshvertices ändern
     *           dafür modus rein (dqs, lbs usw.)
     * @return
     */
        public TriangleMesh animate(float delta){
        update(delta);
        return this.mesh;
    }

    private Matrix getKeyFrameAnimationForJoint(){
        return null;
    }

    private void calcuateAnimationTransformForJoint(Matrix animationMatrix){

    }

    private void calculateAnimationTransformsForSkeleton(){
            //hole für jeden joint die der zeit entsprechenden keyframes
        //interpoliere()
    }

    private void updateStub(float delta){
            calculateAnimationTransformsForSkeleton();
    }

    private void applyAnimationTransformsOnMesh(){

    }

    private void linearBlendSkinning(){
        for(int i=0;i<mesh.getNumberOfVertices();i++){
            //neue vertex posi = alte* summe aus  gewichtet * matrize
            Vector positionVector = Vector.makeHomogenious(meshInBindPose.getVertex(i).getPosition());
            Vector normalVector = Vector.makeHomogenious(meshInBindPose.getVertex(i).getNormal());
            Vector sumOfPositionVector = new Vector(0,0,0,0);
            Vector sumOfNormalVector = new Vector(0,0,0,0);

            Matrix blendMatrix = new Matrix(4,4);

            for(int j = 0; j<skeletonAnimationController.vertexWeightControllers[i].jointsThatAffectsVertex.size(); j++){

                Matrix jointMatrix = skeleton.getJoint(skeletonAnimationController.vertexWeightControllers[i].jointsThatAffectsVertex.get(j)).getTransformForCurrentKeyFrame();
                float weight = skeletonAnimationController.vertexWeightControllers[i].weightThatAffectsVertex.get(j);

                blendMatrix = blendMatrix.add(jointMatrix.multiply(weight));

                //sumOfPositionVector =sumOfPositionVector.add(multiplyVectorWithMatrice(positionVector,jointMatrix).multiply(weight));
                //sumOfNormalVector =sumOfNormalVector.add(multiplyVectorWithMatrice(normalVector,jointMatrix).multiply(weight));

            }
            sumOfPositionVector = animationHelper.multiplyVectorWithMatrice(positionVector,blendMatrix);
            sumOfNormalVector = animationHelper.multiplyVectorWithMatrice(normalVector,blendMatrix);
            sumOfPositionVector.set(3,1);
            sumOfNormalVector.set(3,1);
            mesh.getVertex(i).getPosition().copy(sumOfPositionVector);
            mesh.getVertex(i).getNormal().copy(sumOfNormalVector);
            //mesh.getVertex(i).getColor().copy(new Vector(255 / 255.0, 238 / 255.0, 173 / 255.0, 1));
           if(show){
            debug("LBS",i,blendMatrix,skeletonAnimationController.vertexWeightControllers[i].jointsThatAffectsVertex.size());

           }

        }
    }

    private int getHighestWeight(VertexWeightController vertexWeightController){
        int highestWeightJoint = 0;
        for(int i=1;i<vertexWeightController.jointsThatAffectsVertex.size();i++){
            if(vertexWeightController.weightThatAffectsVertex.get(i)>vertexWeightController.weightThatAffectsVertex.get(i-1)){
                highestWeightJoint=i;
            }
        }
        return highestWeightJoint;
    }

    private void dualQuaternionBlendSkinning(){
        for(int i=0;i<mesh.getNumberOfVertices();i++){
            //neue vertex posi = alte* summe aus  gewichtet * matrize
            Vector positionVector = Vector.makeHomogenious(meshInBindPose.getVertex(i).getPosition());
            Vector normalVector = Vector.makeHomogenious(meshInBindPose.getVertex(i).getNormal());

            DualQuaternion blendDQS = new DualQuaternion();

            DualQuaternion pivot = DualQuaternion.getIdentity();
            if(skeletonAnimationController.vertexWeightControllers[i].jointsThatAffectsVertex.size()>0){
                int pivotJoint = getHighestWeight(skeletonAnimationController.vertexWeightControllers[i]);
                pivot = AnimationHelper.convert4x4MatrixToDQ(
                        skeleton.getJoint(skeletonAnimationController.vertexWeightControllers[i].jointsThatAffectsVertex.get(pivotJoint)).getTransformForCurrentKeyFrame().getTransposed());
                        //skeleton.getJoint(skeletonAnimationController.vertexWeightControllers[i].jointsThatAffectsVertex.get(0)).getTransformForCurrentKeyFrame().getTransposed());

              /*
                if(skeletonAnimationController.vertexWeightControllers[i].jointsThatAffectsVertex.size()>1){
                            System.out.println("Vertex: "+i);
                            for(int p=0;p<skeletonAnimationController.vertexWeightControllers[i].jointsThatAffectsVertex.size(); p++) {
                                System.out.println("Joint idx: "+p);
                                System.out.println("Joint number: "+skeletonAnimationController.vertexWeightControllers[i].jointsThatAffectsVertex.get(p));
                                skeletonAnimationController.vertexWeightControllers[i].
                            }
                }
                */
            }



                boolean setColor=false;

            for(int j = 0; j<skeletonAnimationController.vertexWeightControllers[i].jointsThatAffectsVertex.size(); j++){

                Matrix jointMatrix = skeleton.getJoint(skeletonAnimationController.vertexWeightControllers[i].jointsThatAffectsVertex.get(j)).getTransformForCurrentKeyFrame();
                DualQuaternion jointDQ = AnimationHelper.convert4x4MatrixToDQ(jointMatrix.getTransposed());

                float weight = skeletonAnimationController.vertexWeightControllers[i].weightThatAffectsVertex.get(j);

                //kürzester weg
                //if(jointDQ.getRotation().dot(pivot.getRotation())<0.f){
                if(pivot.getRotation().dot(jointDQ.getRotation())<0.f){
                   weight=weight*-1.f;
                    setColor = true;
                }

                jointDQ = jointDQ.multiply(weight);
                blendDQS = blendDQS.add(jointDQ);


            }
            Matrix transform = AnimationHelper.convertDQToMatrix(blendDQS).getTransposed();
            //Matrix transform = AnimationHelper.convertDQToMatrix2(blendDQS);

           // Vector newPosition = animationHelper.multiplyVectorWithMatrice(positionVector,transform);
            Vector newPosition = AnimationHelper.transformVectorWithDQ(blendDQS,positionVector);
            newPosition = Vector.makeHomogenious(newPosition);

            Vector newNormal = animationHelper.multiplyVectorWithMatrice(normalVector,transform);
          //  Vector newNormal = AnimationHelper.transformVectorWithDQ(blendDQS,normalVector);
            newNormal = Vector.makeHomogenious(newNormal);

            //newPosition.set(3,1);
            //newNormal.set(3,1);
            mesh.getVertex(i).getPosition().copy(newPosition);
            mesh.getVertex(i).getNormal().copy(newNormal);
            if(show){
                debug("DQS",i,transform,skeletonAnimationController.vertexWeightControllers[i].jointsThatAffectsVertex.size());
            }

            if(setColor){
              //  mesh.getVertex(i).getPosition().copy(mesh.getVertex(i).getPosition().multiply(2));

            }
        }
    }

    private void debug(String technique, int i, Matrix m, int jointnumber){
        System.out.println("Technique: "+technique);
        System.out.println("Vertex: "+i);
        System.out.println("Matrix: "+m);
        System.out.println("No of Joints: "+jointnumber);
    }

    private void update(float delta){
        //finde für jeden joint den nächsten keyframe
        for(int i=0;i<skeleton.getJointIndexed().size();i++){
            skeleton.getJoint(i).calculateAnimationPoseToWorldMatrix(skeletonAnimationController.keyFrames.get(i).getKeyFrameAnimationForTimeT(delta,"nlerp"));
        }
        //linearBlendSkinning();
        dualQuaternionBlendSkinning();

    }


}
