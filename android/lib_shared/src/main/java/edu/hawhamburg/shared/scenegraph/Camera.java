/**
 * Diese Datei gehört zum Android/Java Framework zur Veranstaltung "Computergrafik für
 * Augmented Reality" von Prof. Dr. Philipp Jenke an der Hochschule für Angewandte
 * Wissenschaften (HAW) Hamburg. Weder Teile der Software noch das Framework als Ganzes dürfen
 * ohne die Einwilligung von Philipp Jenke außerhalb von Forschungs- und Lehrprojekten an der HAW
 * Hamburg verwendet werden.
 *
 * This file is part of the Android/Java framework for the course "Computer graphics for augmented
 * reality" by Prof. Dr. Philipp Jenke at the University of Applied (UAS) Sciences Hamburg. Neither
 * parts of the framework nor the complete framework may be used outside of research or student
 * projects at the UAS Hamburg.
 */
package edu.hawhamburg.shared.scenegraph;

import edu.hawhamburg.shared.math.Matrix;
import edu.hawhamburg.shared.math.Vector;
import edu.hawhamburg.shared.rendering.ShaderAttributes;

/**
 * The scene camera (mainly intrinisic camera parameters, since the eye is fixed to the origin in this AR application).
 * <p>
 * @author Philipp Jenke
 */

public class Camera {
    private static Camera instance;
    private float fovyRadiens = (float) (80 / 180.0 * Math.PI);
    private float aspect = 0.75f;
    private int width = 0;
    private int height = 0;
    private float zNear = 0.1f;
    private float zFar = 10.0f;
    private Matrix projectionMatrix;
    private Matrix viewMatrix;

    private Vector eye = new Vector(0,0,0);
    private Vector ref = new Vector(0, 0, -1);
    private Vector up = new Vector(0,1,0);

    private Camera() {
        viewMatrix = Matrix.createIdentityMatrix4();
        projectionMatrix = Matrix.createIdentityMatrix4();
    }

    public static Camera getInstance() {
        if (instance == null) {
            instance = new Camera();
        }
        return instance;
    }

    public float getFovyRadiens() {
        return fovyRadiens;
    }

    public float getFovyDegrees() {
        return fovyRadiens * 180.0f / (float) Math.PI;
    }

    public float getAspectRatio() {
        return aspect;
    }

    public float getZNear() {
        return zNear;
    }

    public float getZFar() {
        return zFar;
    }

    public Matrix getProjectionMatrix() {
        return projectionMatrix;
    }

    public void setClipping(float zNear, float zFar) {
        this.zNear = zNear;
        this.zFar = zFar;
    }

    public void setScreenSize(int width, int height) {
        this.width = width;
        this.height = height;
        this.aspect = (float) width / (float) height;
    }

    public void setFovy() {
        this.fovyRadiens = fovyRadiens;
    }

    public void setProjectionMatrix(Matrix projectionMatrix) {
        this.projectionMatrix = projectionMatrix;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public Matrix getViewMatrix() {
        return viewMatrix;
    }

    public void setViewMatrix(Matrix viewMatrix) {
        this.viewMatrix = viewMatrix;
    }

    public void setViewMatrixFromEyeRefUp() {
        float[] viewMatrixData = new float[16];
        android.opengl.Matrix.setLookAtM(viewMatrixData, 0, (float)eye.x(), (float)eye.y(),
                (float)eye.z(), (float)ref.x(), (float)ref.y(), (float)ref.z(), (float)up.x(),
                (float)up.y(), (float)up.z());
        viewMatrix = new Matrix(viewMatrixData);
    }

    public void setup(Vector eye, Vector ref, Vector up) {
        this.eye = eye;
        this.ref = ref;
        this.up = up;
    }

    public Vector getEye(){
        return eye;
    }

    public Vector getUp(){
        return up;
    }

    public Vector getRef(){
        return ref;
    }

    public void setButtonCameraTransformation() {
        ShaderAttributes.getInstance().setViewMatrixParameter(Matrix.createIdentityMatrix4());
        float [] orthoMatrix = new float[16];
        android.opengl.Matrix.orthoM(orthoMatrix, 0,  -1, 1, -1, 1, -1, 1 );
        ShaderAttributes.getInstance().setProjectionMatrixParameter(
                new Matrix(orthoMatrix));
        ShaderAttributes.getInstance().setModelMatrixParameter(Matrix.createIdentityMatrix4());
    }
}
