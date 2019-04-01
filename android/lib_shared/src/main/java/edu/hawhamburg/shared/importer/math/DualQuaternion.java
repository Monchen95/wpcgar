package edu.hawhamburg.shared.importer.math;

public class DualQuaternion {
    //naming real is rotation, dual is displacement, fits quite good to computergraphics
    private Quaternion rotation;
    private Quaternion displacement;

    public DualQuaternion(){
        this.rotation = new Quaternion();
        this.displacement = new Quaternion();
    }

    public DualQuaternion(Quaternion rotation, Quaternion displacement) {
        this.rotation = rotation;
        this.displacement = displacement;
    }

    public DualQuaternion normalize(){
        double norm = rotation.lengthEuclidian();
        Quaternion rotation = this.rotation.multiply(1/norm);
        Quaternion displacement = this.displacement.multiply(1/norm);
        return new DualQuaternion(rotation,displacement);
    }

    public DualQuaternion multiply(double f){
        Quaternion rotation= this.rotation.multiply(f);
        Quaternion displacement= this.displacement.multiply(f);
        return new DualQuaternion(rotation,displacement);
    }

    public DualQuaternion add(DualQuaternion other){
        return new DualQuaternion(this.rotation.add(other.rotation),this.displacement.add(other.displacement));
    }

    public static DualQuaternion getIdentity(){
        return new DualQuaternion(new Quaternion(1,0,0,0),new Quaternion(0,0,0,0));
    }

    public Quaternion getRotation() {
        return rotation;
    }

    public void setRotation(Quaternion rotation) {
        this.rotation = rotation;
    }

    public Quaternion getDisplacement() {
        return displacement;
    }

    public void setDisplacement(Quaternion displacement) {
        this.displacement = displacement;
    }

    @Override
    public String toString() {
        return "DualQuaternion{" +
                "rotation=" + rotation +
                ", displacement=" + displacement +
                '}';
    }
}
