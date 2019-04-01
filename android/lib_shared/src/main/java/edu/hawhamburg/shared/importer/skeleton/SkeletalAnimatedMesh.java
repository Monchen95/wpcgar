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
    public TriangleMesh animate(double delta, String mode){
        update(delta,mode);
        return this.mesh;
    }

    public void animate2(double delta, String mode){
        update2(delta,mode);
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

                Matrix jointMatrix = skeleton.getJoint(skeletonAnimationController.vertexWeightControllers[i].jointsThatAffectsVertex.get(j)).getTransformForCurrentKeyFrame5();
                float weight = skeletonAnimationController.vertexWeightControllers[i].weightThatAffectsVertex.get(j);

                blendMatrix = blendMatrix.add(jointMatrix.multiply(weight));


            }
            Matrix inbetween = blendMatrix.multiply(skeleton.getBsm());
            sumOfPositionVector = inbetween.multiply(positionVector);
            sumOfNormalVector = inbetween.multiply(normalVector);
            sumOfPositionVector.set(3,1);
            sumOfNormalVector.set(3,1);
            Vector tmpPos = new Vector(sumOfPositionVector.x(),sumOfPositionVector.y(),sumOfPositionVector.z());
            Vector tmpNor = new Vector(sumOfNormalVector.x(),sumOfNormalVector.y(),sumOfNormalVector.z());
            mesh.getVertex(i).getPosition().copy(tmpPos);
            mesh.getVertex(i).getNormal().copy(tmpNor);


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
                        skeleton.getJoint(skeletonAnimationController.vertexWeightControllers[i].jointsThatAffectsVertex.get(pivotJoint)).getTransformForCurrentKeyFrame5());
            }




            for(int j = 0; j<skeletonAnimationController.vertexWeightControllers[i].jointsThatAffectsVertex.size(); j++){

                Matrix jointMatrix = skeleton.getJoint(skeletonAnimationController.vertexWeightControllers[i].jointsThatAffectsVertex.get(j)).getTransformForCurrentKeyFrame5();
                Matrix inbetween = jointMatrix.multiply(skeleton.getBsm());
                DualQuaternion jointDQ = AnimationHelper.convert4x4MatrixToDQ(inbetween);



                double weight = skeletonAnimationController.vertexWeightControllers[i].weightThatAffectsVertex.get(j);

                //kürzester weg
                //if(jointDQ.getRotation().dot(pivot.getRotation())<0.f){
                if(pivot.getRotation().dot(jointDQ.getRotation())<0.0){
                    weight=weight*-1.0;

                }

                jointDQ = jointDQ.multiply(weight);
                blendDQS = blendDQS.add(jointDQ);


            }

            Matrix transform = AnimationHelper.convertDQToMatrix(blendDQS);
            //Matrix transform = AnimationHelper.convertDQToMatrix2(blendDQS);
            //Vector newPosition = transform.multiply(positionVector);
            Vector newPosition = AnimationHelper.transformVectorWithDQ(blendDQS,positionVector);
            newPosition = Vector.makeHomogenious(newPosition);

            //Vector newNormal = transform.multiply(normalVector);
            Vector newNormal = AnimationHelper.transformVectorWithDQ(blendDQS,normalVector);
            newNormal = Vector.makeHomogenious(newNormal);

            mesh.getVertex(i).getColor().copy(new Vector(1, 1, 1, 1));
            mesh.getVertex(i).getPosition().copy(newPosition);
            mesh.getVertex(i).getNormal().copy(newNormal);


        }
    }

    private void update(double delta, String mode){
        //finde für jeden joint den nächsten keyframe
        for(int i=0;i<skeleton.getJointIndexed().size();i++){
            skeleton.getJoint(i).calculateAnimationPoseToWorldMatrix(skeletonAnimationController.keyFrames.get(i).getKeyFrameAnimationForTimeT(delta,"slerp"));
        }
        if(mode.equalsIgnoreCase("lbs")){
            linearBlendSkinning();
        } else {
            dualQuaternionBlendSkinning();
        }

    }

    private void update2(double delta, String mode){
        //finde für jeden joint den nächsten keyframe
        for(int i=0;i<skeleton.getJointIndexed().size();i++){
            skeleton.getJoint(i).calculateAnimationPoseToWorldMatrix(skeletonAnimationController.keyFrames.get(i).getKeyFrameAnimationForTimeT(delta,"slerp"));
        }
        if(mode.equalsIgnoreCase("lbs")){
            linearBlendSkinning();
        } else {
            dualQuaternionBlendSkinning();
        }

    }


}