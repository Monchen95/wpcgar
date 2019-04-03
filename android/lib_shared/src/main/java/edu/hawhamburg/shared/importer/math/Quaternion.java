package edu.hawhamburg.shared.importer.math;

import edu.hawhamburg.shared.math.Vector;

public class Quaternion {
    private double w;
    private double x;
    private double y;
    private double z;

    public Quaternion(){
        this.w=0;
        this.x=0;
        this.y=0;
        this.z=0;
    }

    public Quaternion(Vector v){
        this.w=0;
        this.x=(double) v.get(0);
        this.y=(double) v.get(1);
        this.z=(double) v.get(2);
    }

    public Quaternion(double w, double x, double y, double z) {
        this.w = w;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public double getW() {
        return w;
    }

    public void setW(double w) {
        this.w = w;
    }

    public double lengthEuclidian(){
        return (double) Math.sqrt(lenghtNoSqrt());
    }

    public double lenghtNoSqrt(){
        return x*x+y*y+z*z+w*w;
    }

    public Quaternion normalize() {
        double mag = (double) Math.sqrt(w * w + x * x + y * y + z * z);
        w /= mag;
        x /= mag;
        y /= mag;
        z /= mag;
        return new Quaternion(w,x,y,z);
    }

    public double getByIdx(int idx){
        if(idx==0){
            return w;
        } else if(idx==0){
            return w;
        } else if(idx==1){
            return x;
        } else if(idx==2){
            return y;
        } else {
            return z;
        }
    }



    public Quaternion multiply(double a){

        double w = this.w * a;
        double x = this.x * a;
        double y = this.y * a;
        double z = this.z * a;

        return new Quaternion(w,x,y,z);
    }

    public Quaternion multiply(Quaternion other){
        return hamiltonProduct(other);
    }



    public Quaternion add(Quaternion other){
        double w = this.w+other.getW();
        double x = this.x+other.getX();
        double y = this.y+other.getY();
        double z = this.z+other.getZ();
        return new Quaternion(w,x,y,z);
    }

    public double dot(Quaternion other){
        return this.w*other.getW()+this.x*other.getX()+this.y*other.getY()+this.z*other.getZ();
    }

    public Vector getComplex(){
        return new Vector(x,y,z);
    }

    public Quaternion hamiltonProduct(Quaternion other){
        double w = this.w*other.getW()-this.x*other.getX()-this.y*other.getY()-this.z*other.getZ();
        double x = this.w*other.getX()+this.x*other.getW()+this.y*other.getZ()-this.z*other.getY();
        double y = this.w*other.getY()-this.x*other.getZ()+this.y*other.getW()+this.z*other.getX();
        double z = this.w*other.getZ()+this.x*other.getY()-this.y*other.getX()+this.z*other.getW();
        return new Quaternion(w,x,y,z);
    }

    public Quaternion getConjugate(){
        return new Quaternion(w,-x,-y,-z);
    }

    public Vector extractVector(){
        return new Vector(x,y,z);
    }

    @Override
    public String toString() {
        return "Quaternion{" +
                "real w=" + w +
                ",imag x=" + x +
                ",imag y=" + y +
                ",imag z=" + z +
                '}';
    }
}
