package edu.hawhamburg.shared.importer.skeleton;

import java.util.List;

import edu.hawhamburg.shared.math.Matrix;

public class Skeleton {
    private Joint rootJoint;
    private List<Joint> jointIndexed;

    public Skeleton(Joint rootJoint, List<Joint> jointIndexed) {
        this.rootJoint = rootJoint;
        this.jointIndexed=jointIndexed;
    }

    public Joint getRootJoint() {
        return rootJoint;
    }

    public void setRootJoint(Joint rootJoint) {
        this.rootJoint = rootJoint;
    }

    public List<Joint> getJointIndexed() {
        return jointIndexed;
    }

    public void rotateX(double degree){
      /*Matrix rotX = new Matrix(1,0,0,0,
                                0,Math.cos(degree),-1*Math.sin(degree),0,
                                       0,Math.sin(degree),Math.cos(degree),0,
                0,0,0,1);
        for(Joint j: jointIndexed){
            j.setBindPoseToParentMatrix(j.getBindPoseToParentMatrix().multiply(rotX));
          // j.setInversBindMatrix(rotX.multiply(j.getInversBindMatrix()));
        }*/
    }

    public void setJointIndexed(List<Joint> jointIndexed) {
        this.jointIndexed = jointIndexed;
    }

    public Joint getJoint(int index){
        return jointIndexed.get(index);
    }

    @Override
    public String toString() {
        String s = "ROOT JOINT IS: " + rootJoint.toString()+"\n\n";
        for(Joint j: jointIndexed){
            s+=j.toString();
        }
        s+="\n";

        return s;
    }

    public String toStringNoMatrices() {
        String s = "ROOT JOINT IS: " + rootJoint.toStringNoMatrices()+"\n\n";
        for(Joint j: jointIndexed){
            s+=j.toStringNoMatrices();
        }
        s+="\n";

        return s;
    }
}
