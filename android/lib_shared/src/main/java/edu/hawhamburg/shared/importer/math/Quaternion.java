package edu.hawhamburg.shared.importer.math;

import edu.hawhamburg.shared.math.Vector;

public class Quaternion {
    private float w;
    private float x;
    private float y;
    private float z;

    public Quaternion(){
        this.w=0;
        this.x=0;
        this.y=0;
        this.z=0;
    }

    public Quaternion(Vector v){
        this.w=0;
        this.x=(float) v.get(0);
        this.y=(float) v.get(1);
        this.z=(float) v.get(2);
    }

    public Quaternion(float w, float x, float y, float z) {
        this.w = w;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public float getW() {
        return w;
    }

    public void setW(float w) {
        this.w = w;
    }

    public float lengthEuclidian(){
        return (float) Math.sqrt(lenghtNoSqrt());
    }

    public float lenghtNoSqrt(){
        return x*x+y*y+z*z+w*w;
    }

    public Quaternion normalize() {
        float mag = (float) Math.sqrt(w * w + x * x + y * y + z * z);
        w /= mag;
        x /= mag;
        y /= mag;
        z /= mag;
        return new Quaternion(w,x,y,z);
    }

    public float getByIdx(int idx){
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

    public Quaternion normalize2(){
        if(lenghtNoSqrt()!=0.0 && lenghtNoSqrt()!=1.0){
            w /= lenghtNoSqrt();
            x /= lenghtNoSqrt();
            y /= lenghtNoSqrt();
            z /= lenghtNoSqrt();
        }
        return this;
    }

    public Quaternion multiply(float a){

        float w = this.w * a;
        float x = this.x * a;
        float y = this.y * a;
        float z = this.z * a;

        return new Quaternion(w,x,y,z);
    }

    public Quaternion multiply(Quaternion other){
        return hamiltonProduct(other);
    }

    public Quaternion mul3 (float scalar) {
        this.x *= scalar;
        this.y *= scalar;
        this.z *= scalar;
        this.w *= scalar;
        return this;
    }

    public Quaternion add(Quaternion other){
        float w = this.w+other.getW();
        float x = this.x+other.getX();
        float y = this.y+other.getY();
        float z = this.z+other.getZ();
        return new Quaternion(w,x,y,z);
    }

    public float dot(Quaternion other){
        return this.w*other.getW()+this.x*other.getX()+this.y*other.getY()+this.z*other.getZ();
    }

    public Vector getComplex(){
        return new Vector(x,y,z);
    }

    public Quaternion hamiltonProduct(Quaternion other){
        float w = this.w*other.getW()-this.x*other.getX()-this.y*other.getY()-this.z*other.getZ();
        float x = this.w*other.getX()+this.x*other.getW()+this.y*other.getZ()-this.z*other.getY();
        float y = this.w*other.getY()-this.x*other.getZ()+this.y*other.getW()+this.z*other.getX();
        float z = this.w*other.getZ()+this.x*other.getY()-this.y*other.getX()+this.z*other.getW();
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
