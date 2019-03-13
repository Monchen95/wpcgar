package edu.hawhamburg.shared.importer.skeleton.deprecated;

public class Matrix {
    private double x1;
    private double x2;
    private double x3;
    private double x4;
    private double y1;
    private double y2;
    private double y3;
    private double y4;
    private double z1;
    private double z2;
    private double z3;
    private double z4;
    private double t1;
    private double t2;
    private double t3;
    private double t4;

    public Matrix(double x1, double x2, double x3, double x4, double y1, double y2, double y3, double y4, double z1, double z2, double z3, double z4, double t1, double t2, double t3, double t4) {
        this.x1 = x1;
        this.x2 = x2;
        this.x3 = x3;
        this.x4 = x4;
        this.y1 = y1;
        this.y2 = y2;
        this.y3 = y3;
        this.y4 = y4;
        this.z1 = z1;
        this.z2 = z2;
        this.z3 = z3;
        this.z4 = z4;
        this.t1 = t1;
        this.t2 = t2;
        this.t3 = t3;
        this.t4 = t4;
    }

    public static Matrix create4x4MatrixFromString(String matrixString){
        String[] splittedMatrixString = matrixString.split(" ");
        double[] matrixElements = new double[splittedMatrixString.length];
        for(int i=0;i<splittedMatrixString.length;i++){
            matrixElements[i]=Double.valueOf(splittedMatrixString[i]);
        }
        return new Matrix(matrixElements[0],matrixElements[1],matrixElements[2],matrixElements[3],
                matrixElements[4],matrixElements[5],matrixElements[6],matrixElements[7],
                matrixElements[8],matrixElements[9],matrixElements[10],matrixElements[11],
                matrixElements[12],matrixElements[13],matrixElements[14],matrixElements[15]);
    }

    @Override
    public String toString() {
        return "Matrix{" +
                "x1=" + x1 +
                ", x2=" + x2 +
                ", x3=" + x3 +
                ", x4=" + x4 +
                ", y1=" + y1 +
                ", y2=" + y2 +
                ", y3=" + y3 +
                ", y4=" + y4 +
                ", z1=" + z1 +
                ", z2=" + z2 +
                ", z3=" + z3 +
                ", z4=" + z4 +
                ", t1=" + t1 +
                ", t2=" + t2 +
                ", t3=" + t3 +
                ", t4=" + t4 +
                '}';
    }
}
