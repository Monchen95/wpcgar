package edu.hawhamburg.shared.importer.skeleton;

import edu.hawhamburg.shared.importer.math.DualQuaternion;
import edu.hawhamburg.shared.importer.math.Quaternion;
import edu.hawhamburg.shared.math.Matrix;
import edu.hawhamburg.shared.math.Vector;

public class AnimationHelper {

    public static void main(String... a){
        Matrix m = Matrix.createIdentityMatrix4();
        Quaternion q = new Quaternion(1,0,0,0);
        Vector t = new Vector(0,0,0);

        DualQuaternion dq = AnimationHelper.convertQuaternionAndVectorToDQ(q,t);
        System.out.println(dq);
        dq.normalize();
        System.out.println(dq);

        System.out.println("--------------------");

        Vector q2 = new Vector(1,0,0);
        Quaternion r = new Quaternion(0.7071068f,0,0.7071068f,0);
        System.out.println(rotateVectorByQ(r,q2));

        System.out.println("--------------------");

        DualQuaternion dq2 = convert4x4MatrixToDQ(m);
        System.out.println(dq2);
        System.out.println(convertDQToMatrix(dq2));
        DualQuaternion dq3 = new DualQuaternion(new Quaternion(1,2,3,4),new Quaternion(5,7,8,9));

        System.out.println("--------------------");

        Vector v = new Vector(1,0,0);
        Matrix rot1 = new Matrix(-0.0000000,  0.0000000,  1.0000000,
        0.0000000,  1.0000000,  0.0000000,
        -1.0000000,  0.0000000, -0.0000000);
        Vector trans1 = new Vector(0,0,0);
        Matrix rot2 = Matrix.createRotationMatrix4(new Vector(0,1,0),180);
        Vector trans2 = new Vector(2,2,2);

        DualQuaternion dqTransf1 = AnimationHelper.convertQuaternionAndVectorToDQ(AnimationHelper.convert4x4MatrixToQuaternion2(rot1),trans1);
        DualQuaternion dqTransf2 = AnimationHelper.convertQuaternionAndVectorToDQ(AnimationHelper.convert4x4MatrixToQuaternion2(rot2),trans2);

        System.out.println(rot1);
        System.out.println(convert4x4MatrixToQuaternion2(rot1).normalize());
        System.out.println(AnimationHelper.rotateVectorByQ(AnimationHelper.convert4x4MatrixToQuaternion2(rot1),v));
        v = AnimationHelper.transformVectorWithDQ(dqTransf1,v);
        System.out.println(v);
        v = AnimationHelper.transformVectorWithDQ(dqTransf2,v);
        System.out.println(v);
    }

    public AnimationHelper(){

    }

    public static Matrix linearInterpolate4x4Matrices(Matrix A, Matrix B, float progression, String mode){
        Vector translationVectorA = getTranslationFromTransformationMatrix(A);
        Vector translationVectorB = getTranslationFromTransformationMatrix(B);

        Quaternion rotationQuaternionA = convert4x4MatrixToQuaternion2(A);
        Quaternion rotationQuaternionB = convert4x4MatrixToQuaternion2(B);

        Vector interpolatedTranslation = vectorLerp(translationVectorA,translationVectorB,progression);
        Quaternion interpolatedRotation = new Quaternion(0,0,0,0);

        if(mode.equalsIgnoreCase("lerp")){
            interpolatedRotation = quaternioenLerp(rotationQuaternionA,rotationQuaternionB,progression);
        }
        if(mode.equalsIgnoreCase("slerp")){
            interpolatedRotation = quaternioenSLerp3(rotationQuaternionA,rotationQuaternionB,progression);
        }
        if(mode.equalsIgnoreCase("nlerp")){
            interpolatedRotation = quaternioenNLerp(rotationQuaternionA,rotationQuaternionB,progression);
        }

        Matrix interpolatedMatrix = convertQuaternionTo4x4Matric(interpolatedRotation);
        interpolatedMatrix.set(0,3,interpolatedTranslation.get(0));
        interpolatedMatrix.set(1,3,interpolatedTranslation.get(1));
        interpolatedMatrix.set(2,3,interpolatedTranslation.get(2));

        return interpolatedMatrix;
    }

    //todo
    public static Vector getTranslationFromTransformationMatrix(Matrix A){
        return new Vector(A.get(0,3),A.get(1,3),A.get(2,3));
    }

    public static Vector TgetTranslationFromTransformationMatrix(Matrix A){
        return new Vector(A.get(3,1),A.get(3,2),A.get(3,3));
    }

    public static Matrix directLinearInterpolate4x4Matrices(Matrix A, Matrix B,double progression){
        Matrix interpolatedMatrix = A.multiply(1-progression).add(B.multiply(progression));

        return interpolatedMatrix;
    }

    public static Matrix convertQuaternionTo4x4Matric(Quaternion q){

        //nicht sicher ob muss
        //q=q.normalize();

        double W = q.getW();
        double X = q.getX();
        double Y = q.getY();
        double Z = q.getZ();

        double xx  = X * X;
        double xy  = X * Y;
        double xz  = X * Z;
        double xw  = X * W;

        double yy  = Y * Y;
        double yz  = Y * Z;
        double yw  = Y * W;

        double zz  = Z * Z;
        double zw  = Z * W;

        double m00 = 1 - 2 * ( yy + zz );
        double m01 =     2 * ( xy - zw );
        double m02 =     2 * ( xz + yw );

        double m10 =     2 * ( xy + zw );
        double m11 = 1 - 2 * ( xx + zz );
        double m12 =     2 * ( yz - xw );

        double m20 =     2 * ( xz - yw );
        double m21 =     2 * ( yz + xw );
        double m22 = 1 - 2 * ( xx + yy );

        double m03 = 0;
        double m13 = 0;
        double m23 = 0;
        double m30 = 0;
        double m31 = 0;
        double m32 = 0;
        double m33 = 1;

        return new Matrix(m00,m01,m02,m03,m10,m11,m12,m13,m20,m21,m22,m23,m30,m31,m32,m33);
    }

    public static Matrix TconvertQuaternionTo4x4Matric(Quaternion q){

        //nicht sicher ob muss
        //q=q.normalize();

        double W = q.getW();
        double X = q.getX();
        double Y = q.getY();
        double Z = q.getZ();

        double xx  = X * X;
        double xy  = X * Y;
        double xz  = X * Z;
        double xw  = X * W;

        double yy  = Y * Y;
        double yz  = Y * Z;
        double yw  = Y * W;

        double zz  = Z * Z;
        double zw  = Z * W;

        double m00 = 1 - 2 * ( yy + zz );
        double m10 =     2 * ( xy - zw );
        double m20 =     2 * ( xz + yw );

        double m01 =     2 * ( xy + zw );
        double m11 = 1 - 2 * ( xx + zz );
        double m21 =     2 * ( yz - xw );

        double m02 =     2 * ( xz - yw );
        double m12 =     2 * ( yz + xw );
        double m22 = 1 - 2 * ( xx + yy );

        double m03 = 0;
        double m13 = 0;
        double m23 = 0;
        double m30 = 0;
        double m31 = 0;
        double m32 = 0;
        double m33 = 1;

        return new Matrix(m00,m01,m02,m03,m10,m11,m12,m13,m20,m21,m22,m23,m30,m31,m32,m33);
    }


    //todo
    public static Quaternion convert4x4MatrixToQuaternion(Matrix A){

        float m0 = (float) A.get(0,0);
        float m1 = (float) A.get(0,1);
        float m2 = (float) A.get(0,2);
        float m4 = (float) A.get(1,0);
        float m5 = (float) A.get(1,1);
        float m6 = (float) A.get(1,2);
        float m8 = (float) A.get(2,0);
        float m9 = (float) A.get(2,1);
        float m10 = (float) A.get(2,2);


        float x=0;
        float y=0;
        float z=0;
        float w=0;
        float s=0;

        float trace = m0+m5+m10+1f;
        if(trace>0.00000001f){
            s=(float) (0.5f / Math.sqrt(trace));
            w=0.25f/s;
            x=(m9-m6)*s;
            y=(m2-m8)*s;
            z=(m4-m1)*s;
        } else {

        if(m0>=m5&&m0>=m10){
            s  = (float)Math.sqrt( 1.0f + m0 - m5 - m10 ) * 2f;

            x = 0.5f / s;
            y = (m1 + m4 ) / s;
            z = (m2 + m8 ) / s;
            w = (m6 + m9 ) / s;
        } else if(m5>=m0&&m5>=m10){
                s  = (float) Math.sqrt( 1.0f + m5 - m0 - m10 ) * 2f;

                x = (m1 + m4 ) / s;
                y = 0.5f / s;
                z = (m6 + m9 ) / s;
                w = (m2 + m8 ) / s;
            }
            else if(m10>=m0&&m10>m5){
                s  = (float) Math.sqrt( 1.0f + m10 - m0 - m5 ) * 2f;

                x = (m2 + m8 ) / s;
                y = (m6 + m9 ) / s;
                z = 0.5f / s;
                w = (m1 + m4 ) / s;
            }

        }

        Quaternion ret =new Quaternion(w,x,y,z);
        ret = ret.normalize();
        return ret;

    }

    public static Quaternion convert4x4MatrixToQuaternion2(Matrix A) {
        float m00 = (float) A.get(0,0);
        float m01 = (float) A.get(0,1);
        float m02 = (float) A.get(0,2);
        float m10 = (float) A.get(1,0);
        float m11 = (float) A.get(1,1);
        float m12 = (float) A.get(1,2);
        float m20 = (float) A.get(2,0);
        float m21 = (float) A.get(2,1);
        float m22 = (float) A.get(2,2);

        float w, x, y, z;
        float diagonal = m00 + m11 + m22;
        if (diagonal > 0) {
            float w4 = (float) (Math.sqrt(diagonal + 1f) * 2f);
            w = w4 / 4f;
            x = (m21 - m12) / w4;
            y = (m02 - m20) / w4;
            z = (m10 - m01) / w4;
        } else if ((m00 > m11) && (m00 > m22)) {
            float x4 = (float) (Math.sqrt(1f + m00 - m11 - m22) * 2f);
            w = (m21 - m12) / x4;
            x = x4 / 4f;
            y = (m01 + m10) / x4;
            z = (m02 + m20) / x4;
        } else if (m11 > m22) {
            float y4 = (float) (Math.sqrt(1 + m11 - m00 - m22) * 2f);
            w = (m02 - m20) / y4;
            x = (m01 + m10) / y4;
            y = y4 / 4f;
            z = (m12 + m21) / y4;
        } else {
            float z4 = (float) (Math.sqrt(1 + m22 - m00 - m11) * 2f);
            w = (m10 - m01) / z4;
            x = (m02 + m20) / z4;
            y = (m12 + m21) / z4;
            z = z4 / 4f;
        }
        Quaternion ret = new Quaternion(w,x, y, z);
        ret.normalize();
        return ret;
    }

    public static Quaternion Tconvert4x4MatrixToQuaternion2(Matrix A) {
        float m00 = (float) A.get(0,0);
        float m10 = (float) A.get(0,1);
        float m20 = (float) A.get(0,2);
        float m01 = (float) A.get(1,0);
        float m11 = (float) A.get(1,1);
        float m21 = (float) A.get(1,2);
        float m02 = (float) A.get(2,0);
        float m12 = (float) A.get(2,1);
        float m22 = (float) A.get(2,2);

        float w, x, y, z;
        float diagonal = m00 + m11 + m22;
        if (diagonal > 0) {
            float w4 = (float) (Math.sqrt(diagonal + 1f) * 2f);
            w = w4 / 4f;
            x = (m21 - m12) / w4;
            y = (m02 - m20) / w4;
            z = (m10 - m01) / w4;
        } else if ((m00 > m11) && (m00 > m22)) {
            float x4 = (float) (Math.sqrt(1f + m00 - m11 - m22) * 2f);
            w = (m21 - m12) / x4;
            x = x4 / 4f;
            y = (m01 + m10) / x4;
            z = (m02 + m20) / x4;
        } else if (m11 > m22) {
            float y4 = (float) (Math.sqrt(1 + m11 - m00 - m22) * 2f);
            w = (m02 - m20) / y4;
            x = (m01 + m10) / y4;
            y = y4 / 4f;
            z = (m12 + m21) / y4;
        } else {
            float z4 = (float) (Math.sqrt(1 + m22 - m00 - m11) * 2f);
            w = (m10 - m01) / z4;
            x = (m02 + m20) / z4;
            y = (m12 + m21) / z4;
            z = z4 / 4f;
        }
        Quaternion ret = new Quaternion(w,x, y, z);
        ret.normalize();
        return ret;
    }


    public static Vector vectorLerp(Vector a, Vector b, double t){
        double t_ = 1-t;
        double rx = t_*a.x() + t*b.x();
        double ry = t_*a.y() + t*b.y();
        double rz = t_*a.z() + t*b.z();
        return new Vector(rx,ry,rz);
    }

    public static Quaternion quaternioenLerp(Quaternion a, Quaternion b, float t ){
        float t_ = 1f-t;

        float rw = t_*a.getW() + t*b.getW();
        float rx = t_*a.getX() + t*b.getX();
        float ry = t_*a.getY() + t*b.getY();
        float rz = t_*a.getZ() + t*b.getZ();
        //todo normalize vllt rein
        Quaternion ret =new Quaternion(rw,rx,ry,rz);
        ret = ret.normalize();
        return ret;
    }

    public static Quaternion quaternioenSLerp(Quaternion a, Quaternion b, float t) {
        float rx;
        float ry;
        float rz;
        float rw;

            // Calculate angle between them.
        float cosHalfTheta = a.getW() * b.getW() + a.getX() * b.getX() + a.getY() * b.getY() + a.getZ() * b.getZ();
            // if qa=qb or qa=-qb then theta = 0 and we can return qa
            if (Math.abs(cosHalfTheta) >= 1.0f){
                rw = a.getW();
                rx = a.getX();
                ry = a.getY();
                rz = a.getZ();
                Quaternion ret =new Quaternion(rw,rx,ry,rz);
                ret = ret.normalize();
                return ret;
            }
            // Calculate temporary values.
            double halfTheta = Math.acos(cosHalfTheta);
            double sinHalfTheta = Math.sqrt(1.0f - cosHalfTheta*cosHalfTheta);
            // if theta = 180 degrees then result is not fully defined
            // we could rotate around any axis normal to qa or qb
            if (Math.abs(sinHalfTheta) < 0.001f){ // fabs is floating point absolute //todo fabs implementieren statt abs
                rw = (a.getW() * 0.5f + b.getW() * 0.5f);
                rx = (a.getX() * 0.5f + b.getX() * 0.5f);
                ry = (a.getY() * 0.5f + b.getY() * 0.5f);
                rz = (a.getZ() * 0.5f + b.getZ() * 0.5f);
                Quaternion ret =new Quaternion(rw,rx,ry,rz);
                ret = ret.normalize();
                return ret;
            }
            float ratioA = (float) (Math.sin((1f - t) * halfTheta) / sinHalfTheta);
            float ratioB = (float) (Math.sin(t * halfTheta) / sinHalfTheta);
            //calculate Quaternion.
            rw = (a.getW() * ratioA + b.getW() * ratioB);
            rx = (a.getX() * ratioA + b.getX() * ratioB);
            ry = (a.getY() * ratioA + b.getY() * ratioB);
            rz = (a.getZ() * ratioA + b.getZ() * ratioB);
        Quaternion ret =new Quaternion(rw,rx,ry,rz);
        ret = ret.normalize();
        return ret;


    }

    public static Quaternion quaternioenSLerp2(Quaternion a, Quaternion b, float t) {
        float rx;
        float ry;
        float rz;
        float rw;
        float t_ = 1f - t;
        float Wa, Wb;
        float theta = (float) Math.acos(a.getX()*b.getX() + a.getY()*b.getY() + a.getZ()*b.getZ() + a.getW()*b.getW());
        float sn = (float) Math.sin(theta);
        Wa = (float) Math.sin(t_*theta) / sn;
        Wb = (float) Math.sin(t*theta) / sn;
        rx = Wa*a.getX() + Wb*b.getX();
        ry = Wa*a.getY() + Wb*b.getY();
        rz = Wa*a.getZ() + Wb*b.getZ();
        rw = Wa*a.getW() + Wb*b.getW();
        Quaternion ret =new Quaternion(rw,rx,ry,rz);
        ret = ret.normalize();
        return ret;
    }

    public static Quaternion quaternioenSLerp3(Quaternion a, Quaternion b, float t) {

        float dot = a.getW() * b.getW() + a.getX() * b.getX() + a.getY() * b.getY() + a.getZ() * b.getZ();
        float result = dot;

        if (result < 0.0000001f) {
            // Negate the second quaternion and the result of the dot product
            //todo check
            b=b.multiply(-1f);
            result = -result;
        }

        // Set the first and second scale for the interpolation
        float scale0 = 1f - t;
        float scale1 = t;

        // Check if the angle between the 2 quaternions was big enough to
        // warrant such calculations
        if ((1f - result) > 0.0000001f) {// Get the angle between the 2 quaternions,
            // and then store the sin() of that angle
            final double theta = Math.acos(result);
            final double invSinTheta = 1f / Math.sin(theta);

            // Calculate the scale for q1 and q2, according to the angle and
            // it's sine value
            scale0 = (float)(Math.sin((1f - t) * theta) * invSinTheta);
            scale1 = (float)(Math.sin((t * theta)) * invSinTheta);
        }

        // Calculate the x, y, z and w values for the quaternion by using a
        // special form of linear interpolation for quaternions.
        float rx = (scale0 * a.getX()) + (scale1 * b.getX());
        float ry = (scale0 * a.getY()) + (scale1 * b.getY());
        float rz = (scale0 * a.getZ()) + (scale1 * b.getZ());
        float rw = (scale0 * a.getW()) + (scale1 * b.getW());

        // Return the interpolated quaternion
        Quaternion ret =new Quaternion(rw,rx,ry,rz);
       // ret = ret.normalize();
        return ret;
    }

    public static Quaternion quaternioenNLerp(Quaternion a, Quaternion b, float t) {
        float rw;
        float rx;
        float ry;
        float rz;
        float dot = a.getW() * b.getW() + a.getX() * b.getX() + a.getY() * b.getY() + a.getZ() * b.getZ();
        float t_ = 1f - t;
        if (dot < 0f) {
            rw = t_ * a.getW() + t * -b.getW();
            rx = t_ * a.getX() + t * -b.getX();
            ry = t_ * a.getY() + t * -b.getY();
            rz = t_ * a.getZ() + t * -b.getZ();
        } else {
            rw = t_ * a.getW() + t * b.getW();
            rx = t_ * a.getX() + t * b.getX();
            ry = t_ * a.getY() + t * b.getY();
            rz = t_ * a.getZ() + t * b.getZ();
        }
        Quaternion result = new Quaternion(rw,rx,ry,rz);
        result = result.normalize();
        return result;
    }

    public static Quaternion convertTranslationVectorToQuaternion(Vector v){
        float w=1;
        float x = (float) v.x()/2;
        float y = (float) v.y()/2;
        float z = (float) v.z()/2;
        return new Quaternion(w,x,y,z);
    }


    /**
     *From Kavan et alumni 2005
     * Conversion from two Quaternion to one Dual Quaternion
     * (1+(ε/2)(t0i+t1 j +t2k))q0 = q0+(ε/2)(t0i+t1 j +t2k)q0
     */
    public static DualQuaternion convertQuaternionAndVectorToDQ(Quaternion q, Vector t){

        float w_d = (float) (-0.5f*(t.get(0)*q.getByIdx(1) + t.get(1)*q.getByIdx(2) + t.get(2)*q.getByIdx(3)));
        float x_d = (float) (0.5f*(t.get(0)*q.getByIdx(0) + t.get(1)*q.getByIdx(3) - t.get(2)*q.getByIdx(3)));
        float y_d = (float) (0.5f*(-t.get(0)*q.getByIdx(3) + t.get(1)*q.getByIdx(0) + t.get(2)*q.getByIdx(1)));
        float z_d = (float) (0.5f*(t.get(0)*q.getByIdx(2) - t.get(1)*q.getByIdx(1) + t.get(2)*q.getByIdx(0)));

        Quaternion rotation = new Quaternion(q.getW(),q.getX(),q.getY(),q.getZ());
        Quaternion displacement =new Quaternion(w_d,x_d,y_d,z_d);

        return new DualQuaternion(rotation,displacement);
    }



    public static DualQuaternion convert4x4MatrixToDQ(Matrix m){
        Vector t = getTranslationFromTransformationMatrix(m);
        Quaternion q = convert4x4MatrixToQuaternion2(m);
        return convertQuaternionAndVectorToDQ(q,t);
    }

    public static Vector transformVectorWithDQ(DualQuaternion dq, Vector v){
        //todo falls noch nicht normiert
        DualQuaternion dq_norm = dq.normalize();
        Quaternion q0 = dq_norm.getRotation();
        Quaternion qe = dq_norm.getDisplacement();
        v = new Vector(v.x(),v.y(),v.z());
        //berechne translation
        // 2.f * qblend_e * conjugate(qblend_0)
        Vector v0 = dq_norm.getRotation().extractVector();
        Vector ve = dq_norm.getDisplacement().extractVector();

        Vector translation = ve.multiply(q0.getW()).subtract(v0.multiply(qe.getW())).add(v0.cross(ve)).multiply(2f);
        return rotateVectorByQ(q0,v).add(translation);
    }

    public static Vector rotateVectorByQ(Quaternion q, Vector v){
        Quaternion v_q = new Quaternion(v);
        Quaternion q_conjugate = q.getConjugate();
        Quaternion stp1 = q.hamiltonProduct(v_q);
        Quaternion stp2 = stp1.hamiltonProduct(q_conjugate);
        return stp2.extractVector();
    }

    public static Matrix convertDQToMatrix(DualQuaternion dq){

        DualQuaternion dq_n = dq.normalize();
        //DualQuaternion dq_n = dq;
        Quaternion rotation = dq_n.getRotation();
        Quaternion displacement = dq_n.getDisplacement();
        double w0 = rotation.getW();
        double x0 = rotation.getX();
        double y0 = rotation.getY();
        double z0 = rotation.getZ();

        double we = displacement.getW();
        double xe = displacement.getX();
        double ye = displacement.getY();
        double ze = displacement.getZ();

        double t0 = 2*(-we*x0 + xe*w0 - ye*z0 + ze*y0);
        double t1 = 2*(-we*y0 + xe*z0 + ye*w0 - ze*x0);
        double t2 = 2*(-we*z0 - xe*y0 + ye*x0 + ze*w0);


        //rotation ist einfach das non duale quaternion
        Matrix transformMatrix = convertQuaternionTo4x4Matric(rotation);
        transformMatrix.set(0,3,t0);
        transformMatrix.set(1,3,t1);
        transformMatrix.set(2,3,t2);

        return transformMatrix;

    }

    public static Matrix convertDQToMatrix2(DualQuaternion dq){
            float len2 = dq.getRotation().dot(dq.getRotation());
            float w = dq.getRotation().getW();
            float x = dq.getRotation().getX();
            float y = dq.getRotation().getY();
            float z = dq.getRotation().getZ();
            float t0 = dq.getDisplacement().getW();
            float t1 = dq.getDisplacement().getX();
            float t2 = dq.getDisplacement().getY();
            float t3 = dq.getDisplacement().getZ();
            float m00;
            float m01;
            float m02;
            float m03;
            float m10;
            float m11;
            float m12;
            float m13;
            float m20;
            float m21;
            float m22;
            float m23;
            float m30;
            float m31;
            float m32;
            float m33;
            m00 = w*w + x*x - y*y - z*z;
            m01 =  2 * x * y + 2 * w * z;
            m02 =  2 * x * z - 2 * w * y;
            m03 = 0;
            m10 =  2 * x * y - 2 * w * z;
            m11 = w * w + y * y - x * x - z * z;
            m12 =  2 * y * z + 2 * w * x;
            m13 =  0;
            m20 =  2 * x * z + 2 * w * y;
            m21 =  2 * y * z - 2 * w * x;
            m22 =  w * w + z * z - x * x - y * y;
            m23 = 0;
            m30 =  -2 * t0 * x + 2 * w * t1 - 2 * t2 * z + 2 * y * t3;
            m31 =  -2 * t0 * y + 2 * t1 * z - 2 * x * t3 + 2 * w * t2;
            m32 =  -2 * t0 * z + 2 * x * t2 + 2 * w * t3 - 2 * t1 * y;
            m33 =  len2;
            Matrix m = new Matrix(m00,m01,m02,m03,
                    m10,m11,m12,m13,
                    m20,m21,m22,m23,
                    m30,m31,m32,m33);
            m = m.multiply(1/len2);
            return m;

    }

    Vector multiplyVectorWithMatrice(Vector v, Matrix m){
            if(v.getDimension()!=m.getNumberOfColumns()){
                return new Vector(0,0,0,0);
            }
            double v0 = v.get(0);
            double v1 = v.get(1);
            double v2 = v.get(2);
            double v3 = v.get(3);


            double x = v0*m.get(0,0)+v1*m.get(1,0)+v2*m.get(2,0)+v3*m.get(3,0);
            double y = v0*m.get(0,1)+v1*m.get(1,1)+v2*m.get(2,1)+v3*m.get(3,1);
            double z = v0*m.get(0,2)+v1*m.get(1,2)+v2*m.get(2,2)+v3*m.get(3,2);
            double w = v0*m.get(0,3)+v1*m.get(1,3)+v2*m.get(2,3)+v3*m.get(3,3);

            return new Vector(x,y,z,w);
        }
}
