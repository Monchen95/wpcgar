package edu.hawhamburg.shared.importer.skeleton;

import edu.hawhamburg.shared.importer.math.DualQuaternion;
import edu.hawhamburg.shared.importer.math.Quaternion;
import edu.hawhamburg.shared.importer.skeleton.deprecated.EulerAngle;
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

        DualQuaternion dualQuaternion1 = new DualQuaternion(new Quaternion(1,2,3,4), new Quaternion(1,2,3,4));
        DualQuaternion dualQuaternion2 = new DualQuaternion(new Quaternion(1,2,3,4), new Quaternion(1,2,3,4));

    }

    public AnimationHelper(){

    }

    public static Matrix linearInterpolate4x4Matrices(Matrix A, Matrix B, double progression, String mode){
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

        double m0 = (double) A.get(0,0);
        double m1 = (double) A.get(0,1);
        double m2 = (double) A.get(0,2);
        double m4 = (double) A.get(1,0);
        double m5 = (double) A.get(1,1);
        double m6 = (double) A.get(1,2);
        double m8 = (double) A.get(2,0);
        double m9 = (double) A.get(2,1);
        double m10 = (double) A.get(2,2);


        double x=0;
        double y=0;
        double z=0;
        double w=0;
        double s=0;

        double trace = m0+m5+m10+1;
        if(trace>0.00000001){
            s=(double) (0.5 / Math.sqrt(trace));
            w=0.25/s;
            x=(m9-m6)*s;
            y=(m2-m8)*s;
            z=(m4-m1)*s;
        } else {

            if(m0>=m5&&m0>=m10){
                s  = (double)Math.sqrt( 1.0 + m0 - m5 - m10 ) * 2;

                x = 0.5 / s;
                y = (m1 + m4 ) / s;
                z = (m2 + m8 ) / s;
                w = (m6 + m9 ) / s;
            } else if(m5>=m0&&m5>=m10){
                s  = (double) Math.sqrt( 1.0 + m5 - m0 - m10 ) * 2;

                x = (m1 + m4 ) / s;
                y = 0.5 / s;
                z = (m6 + m9 ) / s;
                w = (m2 + m8 ) / s;
            }
            else if(m10>=m0&&m10>m5){
                s  = (double) Math.sqrt( 1.0 + m10 - m0 - m5 ) * 2;

                x = (m2 + m8 ) / s;
                y = (m6 + m9 ) / s;
                z = 0.5 / s;
                w = (m1 + m4 ) / s;
            }

        }

        Quaternion ret =new Quaternion(w,x,y,z);
        ret = ret.normalize();
        return ret;

    }

    public static Quaternion convert4x4MatrixToQuaternion2(Matrix A) {
        double m00 = (double) A.get(0,0);
        double m01 = (double) A.get(0,1);
        double m02 = (double) A.get(0,2);
        double m10 = (double) A.get(1,0);
        double m11 = (double) A.get(1,1);
        double m12 = (double) A.get(1,2);
        double m20 = (double) A.get(2,0);
        double m21 = (double) A.get(2,1);
        double m22 = (double) A.get(2,2);

        double w, x, y, z;
        double diagonal = m00 + m11 + m22;
        if (diagonal > 0) {
            double w4 = (double) (Math.sqrt(diagonal + 1) * 2);
            w = w4 / 4;
            x = (m21 - m12) / w4;
            y = (m02 - m20) / w4;
            z = (m10 - m01) / w4;
        } else if ((m00 > m11) && (m00 > m22)) {
            double x4 = (double) (Math.sqrt(1 + m00 - m11 - m22) * 2);
            w = (m21 - m12) / x4;
            x = x4 / 4;
            y = (m01 + m10) / x4;
            z = (m02 + m20) / x4;
        } else if (m11 > m22) {
            double y4 = (double) (Math.sqrt(1 + m11 - m00 - m22) * 2);
            w = (m02 - m20) / y4;
            x = (m01 + m10) / y4;
            y = y4 / 4;
            z = (m12 + m21) / y4;
        } else {
            double z4 = (double) (Math.sqrt(1 + m22 - m00 - m11) * 2);
            w = (m10 - m01) / z4;
            x = (m02 + m20) / z4;
            y = (m12 + m21) / z4;
            z = z4 / 4;
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

    public static Quaternion quaternioenLerp(Quaternion a, Quaternion b, double t ){
        double t_ = 1-t;

        double rw = t_*a.getW() + t*b.getW();
        double rx = t_*a.getX() + t*b.getX();
        double ry = t_*a.getY() + t*b.getY();
        double rz = t_*a.getZ() + t*b.getZ();
        //todo normalize vllt rein
        Quaternion ret =new Quaternion(rw,rx,ry,rz);
        ret = ret.normalize();
        return ret;
    }

    public static Quaternion quaternioenSLerp(Quaternion a, Quaternion b, double t) {
        double rx;
        double ry;
        double rz;
        double rw;

        // Calculate angle between them.
        double cosHalfTheta = a.getW() * b.getW() + a.getX() * b.getX() + a.getY() * b.getY() + a.getZ() * b.getZ();
        // if qa=qb or qa=-qb then theta = 0 and we can return qa
        if (Math.abs(cosHalfTheta) >= 1.0){
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
        double sinHalfTheta = Math.sqrt(1.0 - cosHalfTheta*cosHalfTheta);
        // if theta = 180 degrees then result is not fully defined
        // we could rotate around any axis normal to qa or qb
        if (Math.abs(sinHalfTheta) < 0.001){ // fabs is floating point absolute //todo fabs implementieren statt abs
            rw = (a.getW() * 0.5 + b.getW() * 0.5);
            rx = (a.getX() * 0.5 + b.getX() * 0.5);
            ry = (a.getY() * 0.5 + b.getY() * 0.5);
            rz = (a.getZ() * 0.5 + b.getZ() * 0.5);
            Quaternion ret =new Quaternion(rw,rx,ry,rz);
            ret = ret.normalize();
            return ret;
        }
        double ratioA = (double) (Math.sin((1 - t) * halfTheta) / sinHalfTheta);
        double ratioB = (double) (Math.sin(t * halfTheta) / sinHalfTheta);
        //calculate Quaternion.
        rw = (a.getW() * ratioA + b.getW() * ratioB);
        rx = (a.getX() * ratioA + b.getX() * ratioB);
        ry = (a.getY() * ratioA + b.getY() * ratioB);
        rz = (a.getZ() * ratioA + b.getZ() * ratioB);
        Quaternion ret =new Quaternion(rw,rx,ry,rz);
        ret = ret.normalize();
        return ret;


    }

    public static Quaternion quaternioenSLerp2(Quaternion a, Quaternion b, double t) {
        double rx;
        double ry;
        double rz;
        double rw;
        double t_ = 1 - t;
        double Wa, Wb;
        double theta = (double) Math.acos(a.getX()*b.getX() + a.getY()*b.getY() + a.getZ()*b.getZ() + a.getW()*b.getW());
        double sn = (double) Math.sin(theta);
        Wa = (double) Math.sin(t_*theta) / sn;
        Wb = (double) Math.sin(t*theta) / sn;
        rx = Wa*a.getX() + Wb*b.getX();
        ry = Wa*a.getY() + Wb*b.getY();
        rz = Wa*a.getZ() + Wb*b.getZ();
        rw = Wa*a.getW() + Wb*b.getW();
        Quaternion ret =new Quaternion(rw,rx,ry,rz);
        ret = ret.normalize();
        return ret;
    }

    public static Quaternion quaternioenSLerp3(Quaternion a, Quaternion b, double t) {

        double dot = a.getW() * b.getW() + a.getX() * b.getX() + a.getY() * b.getY() + a.getZ() * b.getZ();
        double result = dot;

        if (result < 0.0000001) {
            // Negate the second quaternion and the result of the dot product
            //todo check
            b=b.multiply(-1);
            result = -result;
        }

        // Set the first and second scale for the interpolation
        double scale0 = 1 - t;
        double scale1 = t;

        // Check if the angle between the 2 quaternions was big enough to
        // warrant such calculations
        if ((1 - result) > 0.0000001) {// Get the angle between the 2 quaternions,
            // and then store the sin() of that angle
            final double theta = Math.acos(result);
            final double invSinTheta = 1 / Math.sin(theta);

            // Calculate the scale for q1 and q2, according to the angle and
            // it's sine value
            scale0 = (double)(Math.sin((1 - t) * theta) * invSinTheta);
            scale1 = (double)(Math.sin((t * theta)) * invSinTheta);
        }

        // Calculate the x, y, z and w values for the quaternion by using a
        // special form of linear interpolation for quaternions.
        double rx = (scale0 * a.getX()) + (scale1 * b.getX());
        double ry = (scale0 * a.getY()) + (scale1 * b.getY());
        double rz = (scale0 * a.getZ()) + (scale1 * b.getZ());
        double rw = (scale0 * a.getW()) + (scale1 * b.getW());

        // Return the interpolated quaternion
        Quaternion ret =new Quaternion(rw,rx,ry,rz);
        // ret = ret.normalize();
        return ret;
    }

    public static Quaternion quaternioenNLerp(Quaternion a, Quaternion b, double t) {
        double rw;
        double rx;
        double ry;
        double rz;
        double dot = a.getW() * b.getW() + a.getX() * b.getX() + a.getY() * b.getY() + a.getZ() * b.getZ();
        double t_ = 1 - t;
        if (dot < 0) {
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
        double w=1;
        double x = (double) v.x()/2;
        double y = (double) v.y()/2;
        double z = (double) v.z()/2;
        return new Quaternion(w,x,y,z);
    }


    /**
     *From Kavan et alumni 2005
     * Conversion from two Quaternion to one Dual Quaternion
     * (1+(ε/2)(t0i+t1 j +t2k))q0 = q0+(ε/2)(t0i+t1 j +t2k)q0
     */
    public static DualQuaternion convertQuaternionAndVectorToDQ2(Quaternion q, Vector t){

        double w_d = (double) (-0.5*(t.get(0)*q.getByIdx(1) + t.get(1)*q.getByIdx(2) + t.get(2)*q.getByIdx(3)));
        double x_d = (double) (0.5*(t.get(0)*q.getByIdx(0) + t.get(1)*q.getByIdx(3) - t.get(2)*q.getByIdx(3)));
        double y_d = (double) (0.5*(-t.get(0)*q.getByIdx(3) + t.get(1)*q.getByIdx(0) + t.get(2)*q.getByIdx(1)));
        double z_d = (double) (0.5*(t.get(0)*q.getByIdx(2) - t.get(1)*q.getByIdx(1) + t.get(2)*q.getByIdx(0)));

        Quaternion rotation = new Quaternion(q.getW(),q.getX(),q.getY(),q.getZ());
        Quaternion displacement =new Quaternion(w_d,x_d,y_d,z_d);

        return new DualQuaternion(rotation,displacement);
    }

    public static DualQuaternion convertQuaternionAndVectorToDQ(Quaternion q, Vector t){

        double x_d = (double) (0.5*(t.get(0)*q.getByIdx(0) + t.get(1)*q.getByIdx(3) - t.get(2)*q.getByIdx(2)));
        double y_d = (double) (0.5*(-t.get(0)*q.getByIdx(3) + t.get(1)*q.getByIdx(0) + t.get(2)*q.getByIdx(1)));
        double z_d = (double) (0.5*(t.get(0)*q.getByIdx(2) - t.get(1)*q.getByIdx(1) + t.get(2)*q.getByIdx(0)));
        double w_d = (double) (-0.5*(t.get(0)*q.getByIdx(1) + t.get(1)*q.getByIdx(2) + t.get(2)*q.getByIdx(3)));

        Quaternion rotation = new Quaternion(q.getW(),q.getX(),q.getY(),q.getZ());
        Quaternion displacement =new Quaternion(w_d,x_d,y_d,z_d);

        return new DualQuaternion(rotation,displacement);
    }

    public static DualQuaternion convert4x4MatrixToDQ2(Matrix m){
        //Vector t = getTranslationFromTransformationMatrix(m);
        Vector t = getTranslationFromTransformationMatrix(m.getTransposed());
        Quaternion q = convert4x4MatrixToQuaternion2(m);
        return convertQuaternionAndVectorToDQ(q,t);
    }

    public static DualQuaternion convert4x4MatrixToDQ(Matrix m){
        Vector t = getTranslationFromTransformationMatrix(m);
        //Vector t = getTranslationFromTransformationMatrix(m.getTransposed());
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

        Vector translation = ve.multiply(q0.getW()).subtract(v0.multiply(qe.getW())).add(v0.cross(ve)).multiply(2);
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
        double len2 = dq.getRotation().dot(dq.getRotation());
        double w = dq.getRotation().getW();
        double x = dq.getRotation().getX();
        double y = dq.getRotation().getY();
        double z = dq.getRotation().getZ();
        double t0 = dq.getDisplacement().getW();
        double t1 = dq.getDisplacement().getX();
        double t2 = dq.getDisplacement().getY();
        double t3 = dq.getDisplacement().getZ();
        double m00;
        double m01;
        double m02;
        double m03;
        double m10;
        double m11;
        double m12;
        double m13;
        double m20;
        double m21;
        double m22;
        double m23;
        double m30;
        double m31;
        double m32;
        double m33;
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
