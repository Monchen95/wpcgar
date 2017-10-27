/**
 * Diese Datei gehört zum Android/Java Framework zur Veranstaltung "Computergrafik für
 * Augmented Reality" von Prof. Dr. Philipp Jenke an der Hochschule für Angewandte
 * Wissenschaften (HAW) Hamburg. Weder Teile der Software noch das Framework als Ganzes dürfen
 * ohne die Einwilligung von Philipp Jenke außerhalb von Forschungs- und Lehrprojekten an der HAW
 * Hamburg verwendet werden.
 * <p>
 * This file is part of the Android/Java framework for the course "Computer graphics for augmented
 * reality" by Prof. Dr. Philipp Jenke at the University of Applied (UAS) Sciences Hamburg. Neither
 * parts of the framework nor the complete framework may be used outside of research or student
 * projects at the UAS Hamburg.
 */
package edu.hawhamburg.shared.rendering;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.List;

import android.opengl.GLES20;
import android.util.Log;

import edu.hawhamburg.shared.math.Vector;
import edu.hawhamburg.shared.misc.Constants;

/**
 * Rendering vie vertex buffer objects (VBO).
 */
public class VertexBufferObject {

    private static final String LOGTAG = "WP Computer Graphics AR";

    /**
     * List containing the fragment vertices to be rendered
     */
    private List<RenderVertex> renderVertices = null;

    /**
     * Use this primitive type for rendering. Attentions: This implies the number
     * of vertices, normals and colors required; e.g. triangles require three
     * vertices each.
     */
    private int primitiveType = GLES20.GL_TRIANGLES;

    private static final int FLOAT_SIZE_IN_BYTES = 4;
    private static final int INT_SIZE_IN_BYTES = 4;

    private FloatBuffer positionBuffer = null;
    private FloatBuffer normalBuffer = null;
    private FloatBuffer colorBuffer = null;
    private FloatBuffer texCoordsBuffer = null;
    private IntBuffer indexBuffer = null;

    public VertexBufferObject() {
    }

    /**
     * Set the data for the Buffer. The format is described together with the
     * vertices, normals and colors attributes.
     */
    public void setup(List<RenderVertex> renderVertices, int primitiveType) {
        this.renderVertices = renderVertices;
        this.primitiveType = primitiveType;
    }

    /**
     * Init VBO, called only once (or if the date changed).
     */
    private void init() {
        if (renderVertices == null || renderVertices.size() == 0) {
            return;
        }

        positionBuffer = createPositionBuffer();
        normalBuffer = createNormalBuffer();
        colorBuffer = createColorBuffer();
        texCoordsBuffer = createTexCoordsBuffer();
        indexBuffer = createIndexBuffer();
        Shader.checkGlError();
        //Log.i(Constants.LOGTAG, "Created VBO buffers for " + renderVertices.size() + " render vertices.");
    }

    /**
     * Create position buffer from data.
     */
    private FloatBuffer createPositionBuffer() {
        int dataLength = renderVertices.size() * 3 * FLOAT_SIZE_IN_BYTES;
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(dataLength);
        float[] data = new float[renderVertices.size() * 3];
        for (int i = 0; i < renderVertices.size(); i++) {
            RenderVertex rv = renderVertices.get(i);
            if (rv.position == null) {
                System.out.println("Fehler!");
            }
            data[i * 3] = (float) renderVertices.get(i).position.x();
            data[i * 3 + 1] = (float) renderVertices.get(i).position.y();
            data[i * 3 + 2] = (float) renderVertices.get(i).position.z();
        }
        byteBuffer.order(ByteOrder.nativeOrder());
        FloatBuffer floatBuffer = byteBuffer.asFloatBuffer();
        floatBuffer.put(data);
        floatBuffer.position(0);
        return floatBuffer;
    }

    /**
     * Create normal buffer from data.
     */
    private FloatBuffer createNormalBuffer() {
        int dataLength = renderVertices.size() * 3 * FLOAT_SIZE_IN_BYTES;
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(dataLength);
        float[] data = new float[renderVertices.size() * 3];
        for (int i = 0; i < renderVertices.size(); i++) {
            data[i * 3] = (float) renderVertices.get(i).normal.x();
            data[i * 3 + 1] = (float) renderVertices.get(i).normal.y();
            data[i * 3 + 2] = (float) renderVertices.get(i).normal.z();
        }
        byteBuffer.order(ByteOrder.nativeOrder());
        FloatBuffer floatBuffer = byteBuffer.asFloatBuffer();
        floatBuffer.put(data);
        floatBuffer.position(0);
        return floatBuffer;
    }

    /**
     * Create color buffer from data.
     */
    private FloatBuffer createColorBuffer() {
        int dataLength = renderVertices.size() * 4 * FLOAT_SIZE_IN_BYTES;
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(dataLength);
        float[] data = new float[renderVertices.size() * 4];
        for (int i = 0; i < renderVertices.size(); i++) {
            Vector color = renderVertices.get(i).color;
            if (color.getDimension() < 4) {
                Log.i(Constants.LOGTAG, "Invalid color vector, must be RGBA format.");
                break;
            }
            data[i * 4] = (float) color.x();
            data[i * 4 + 1] = (float) color.y();
            data[i * 4 + 2] = (float) color.z();
            data[i * 4 + 3] = (float) color.w();
        }
        byteBuffer.order(ByteOrder.nativeOrder());
        FloatBuffer floatBuffer = byteBuffer.asFloatBuffer();
        floatBuffer.put(data);
        floatBuffer.position(0);
        return floatBuffer;
    }

    /**
     * Create texture coordinates buffer from data.
     */
    private FloatBuffer createTexCoordsBuffer() {
        int dataLength = renderVertices.size() * 2 * FLOAT_SIZE_IN_BYTES;
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(dataLength);
        float[] data = new float[renderVertices.size() * 2];
        for (int i = 0; i < renderVertices.size(); i++) {
            data[i * 2] = (float) renderVertices.get(i).texCoords.x();
            data[i * 2 + 1] = (float) renderVertices.get(i).texCoords.y();
        }
        byteBuffer.order(ByteOrder.nativeOrder());
        FloatBuffer floatBuffer = byteBuffer.asFloatBuffer();
        floatBuffer.put(data);
        floatBuffer.position(0);
        return floatBuffer;
    }

    private IntBuffer createIndexBuffer() {
        ByteBuffer ibb =
                ByteBuffer.allocateDirect(renderVertices.size() * INT_SIZE_IN_BYTES);
        ibb.order(ByteOrder.nativeOrder());
        IntBuffer indicesBuf = ibb.asIntBuffer();
        for (int i = 0; i < renderVertices.size(); i++) {
            indicesBuf.put(i);
        }
        indicesBuf.position(0);
        return indicesBuf;
    }

    /**
     * Draw using the VBO
     */
    public void draw() {
        if (positionBuffer == null || normalBuffer == null || colorBuffer == null) {
            init();
        }

        GLES20.glEnableVertexAttribArray(
                ShaderAttributes.getInstance().getVertexLocation());
        GLES20.glEnableVertexAttribArray(
                ShaderAttributes.getInstance().getNormalLocation());
        GLES20.glEnableVertexAttribArray(
                ShaderAttributes.getInstance().getColorLocation());
        GLES20.glEnableVertexAttribArray(
                ShaderAttributes.getInstance().getTexCoordsLocation());

        try {
            GLES20.glVertexAttribPointer(
                    ShaderAttributes.getInstance().getVertexLocation(), 3, GLES20.GL_FLOAT,
                    false, 0, positionBuffer);
            GLES20.glVertexAttribPointer(
                    ShaderAttributes.getInstance().getNormalLocation(), 3, GLES20.GL_FLOAT,
                    false, 0, normalBuffer);
            GLES20.glVertexAttribPointer(
                    ShaderAttributes.getInstance().getColorLocation(), 4, GLES20.GL_FLOAT,
                    false, 0, colorBuffer);
            GLES20.glVertexAttribPointer(
                    ShaderAttributes.getInstance().getTexCoordsLocation(), 2, GLES20.GL_FLOAT,
                    false, 0, texCoordsBuffer);

            GLES20.glDrawElements(primitiveType, renderVertices.size(),
                    GLES20.GL_UNSIGNED_INT, indexBuffer);
        } catch (Exception e) {
            Log.i(LOGTAG, "Fehler: " + e);
        }

        Shader.checkGlError();
    }

    /**
     * Updates the values in the position buffer from the data array.
     *
     * @param newPositions Float array containing the positions in xyzxyy... format
     */
    public void updatePositionBuffer(float[] newPositions, float[] newTextureCoordinates) {
        if (positionBuffer != null && texCoordsBuffer != null) {
            positionBuffer.put(newPositions);
            positionBuffer.position(0);
            texCoordsBuffer.put(newTextureCoordinates);
            texCoordsBuffer.position(0);
        }
    }

    /**
     * Delete all buffers.
     */
    public void invalidate() {
        positionBuffer = null;
        normalBuffer = null;
        colorBuffer = null;
        texCoordsBuffer = null;
    }
}
