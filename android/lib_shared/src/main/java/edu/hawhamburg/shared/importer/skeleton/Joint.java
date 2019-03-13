package edu.hawhamburg.shared.importer.skeleton;

import java.util.HashMap;
import java.util.Map;

import edu.hawhamburg.shared.math.Matrix;
import edu.hawhamburg.shared.math.Vector;

public class Joint {
    private String name;

    private Joint parentJoint;
    //private List<Joint> childJoints;

    //inverse of bind matrix already in relation to root
    private Matrix inversBindMatrix;

    //bind matrix in relation to parent bone
    private Matrix bindPoseToParentMatrix;

    //matrix der aktuellen animationspose
    private Matrix animationPoseToWorldMatrix;

    private Map<Double, Matrix> keyFrame;

    public void calculateAnimationPoseToWorldMatrix(Matrix keyFrameMatrix){
        animationPoseToWorldMatrix =keyFrameMatrix;
        if(parentJoint!=null){
            animationPoseToWorldMatrix =parentJoint.getAnimationPoseToWorldMatrix().multiply(animationPoseToWorldMatrix);
        }
    }

    public Matrix getDefaultPose(){
    if(parentJoint==null){
            return inversBindMatrix.multiply(bindPoseToParentMatrix);
        }
        return inversBindMatrix.multiply(getBindPoseToWorldMatrix());
    }

    public Matrix getBindPoseToWorldMatrix(){

        if(parentJoint==null){
            return bindPoseToParentMatrix;
        }
        return parentJoint.getBindPoseToWorldMatrix().multiply(bindPoseToParentMatrix);
    }

    public Matrix getTransformForCurrentKeyFrame(){
        //beide matrizen transponieren, weil openGL
        return inversBindMatrix.getTransposed().multiply(animationPoseToWorldMatrix.getTransposed());
    }

    public Matrix getTransformForCurrentKeyFrame2(){
        //beide matrizen transponieren, weil openGL
        return inversBindMatrix.multiply(animationPoseToWorldMatrix);
    }


    public Matrix getKeyFrameAnimation(Double d){
        //todo logik rein dass richtiges zurückgegeben wird und irgendwann auslagern
        return keyFrame.get(d);
    }

    public int numberOfKeyFrames(){
        return keyFrame.size();
    }

    public Map<Double,Matrix> getKeyFrame(){
        return keyFrame;
    }

    public void addKeyFrame(Double d, Matrix m){
        keyFrame.put(d,m);
    }

    public Matrix getInversBindMatrix() {
        return inversBindMatrix;
    }

    public void setInversBindMatrix(Matrix inversBindMatrix) {
        this.inversBindMatrix = inversBindMatrix;
    }

    public Matrix getAnimationPoseToWorldMatrix() {
        return animationPoseToWorldMatrix;
    }

    public void setAnimationPoseToWorldMatrix(Matrix animationPoseToWorldMatrix) {
        this.animationPoseToWorldMatrix = animationPoseToWorldMatrix;
    }

    public Matrix getBindPoseToParentMatrix() {
        return bindPoseToParentMatrix;
    }

    public void setBindPoseToParentMatrix(Matrix bindPoseToParentMatrix) {
        this.bindPoseToParentMatrix = bindPoseToParentMatrix;
    }

    public Joint(String name, Joint parentJoint, Matrix transformMatrix) {
        this.name = name;
        this.parentJoint = parentJoint;
        this.bindPoseToParentMatrix = transformMatrix;
        this.keyFrame = new HashMap<>();
        this.animationPoseToWorldMatrix = Matrix.createIdentityMatrix4();
    }


    public Joint(String name, Matrix transformMatrix) {
        this.name = name;
        this.bindPoseToParentMatrix = transformMatrix;
        this.parentJoint=null; //todo default joint erstellen
        this.keyFrame = new HashMap<>();
        this.animationPoseToWorldMatrix = Matrix.createIdentityMatrix4();

    }



    public boolean hasParentJoint(){
        return parentJoint==null?false:true;
    }

    public Joint getParentJoint() {
        return parentJoint;
    }

    public void setParentJoint(Joint parentJoint) {
        this.parentJoint = parentJoint;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Joint{" +
                "name='" + name + '\'' +
                ", parentJoint=" + parentJoint +
                ", inversBindMatrix=" + inversBindMatrix +
                ", bindPoseToParentMatrix=" + bindPoseToParentMatrix +
                '}';
    }

    public String toStringNoMatrices() {
        String s = "Joint{" + "name='" + name + '\'';
        if (hasParentJoint()){
            s+=", parentJoint=" + parentJoint.getName() + '}';
        }

        return s;
    }


}
