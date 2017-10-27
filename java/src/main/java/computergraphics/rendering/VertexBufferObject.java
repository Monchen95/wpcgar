package computergraphics.rendering;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.List;

import com.jogamp.opengl.GL2;

/**
 * Rendering vie vertex buffer objects (VBO).
 */
public class VertexBufferObject {
  /**
   * List containing the fragment vertices to be rendered
   */
  private List<RenderVertex> renderVertices = null;

  /**
   * Use this primitive type for rendering. Attentions: This implies the number
   * of vertices, normals and colors required; e.g. triangles require three
   * vertices each.
   */
  private int primitiveType = GL2.GL_TRIANGLES;

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
  public void Setup(List<RenderVertex> renderVertices, int primitiveType) {
    this.renderVertices = renderVertices;
    this.primitiveType = primitiveType;
  }

  /**
   * Init VBO, called only once (or if the date changed).
   */
  private void init(GL2 gl) {
    if ( renderVertices == null || renderVertices.size() == 0 ){
      return;
    }
    
    positionBuffer = createPositionBuffer(gl);
    normalBuffer = createNormalBuffer(gl);
    colorBuffer = createColorBuffer(gl);
    texCoordsBuffer = createTexCoordsBuffer(gl);
    indexBuffer = createIndexBuffer();
    Shader.checkGlError(gl);
//    System.out.println("Created VBO buffers (vertex, normal, color, texCoords, index).");
  }

  /**
   * Create position buffer from data.
   */
  private FloatBuffer createPositionBuffer(GL2 gl) {
    int dataLength = renderVertices.size() * 3 * FLOAT_SIZE_IN_BYTES;
    ByteBuffer byteBuffer = ByteBuffer.allocateDirect(dataLength);
    float[] data = new float[renderVertices.size() * 3];
    for (int i = 0; i < renderVertices.size(); i++) {
      RenderVertex rv = renderVertices.get(i);
      if (rv.position == null ){
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
  private FloatBuffer createNormalBuffer(GL2 gl) {
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
  private FloatBuffer createColorBuffer(GL2 gl) {
    int dataLength = renderVertices.size() * 4 * FLOAT_SIZE_IN_BYTES;
    ByteBuffer byteBuffer = ByteBuffer.allocateDirect(dataLength);
    float[] data = new float[renderVertices.size() * 4];
    for (int i = 0; i < renderVertices.size(); i++) {
      data[i * 4] = (float) renderVertices.get(i).color.x();
      data[i * 4 + 1] = (float) renderVertices.get(i).color.y();
      data[i * 4 + 2] = (float) renderVertices.get(i).color.z();
      data[i * 4 + 3] = (float) renderVertices.get(i).color.w();
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
  private FloatBuffer createTexCoordsBuffer(GL2 gl) {
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
  public void draw(GL2 gl) {
    if (positionBuffer == null || normalBuffer == null || colorBuffer == null) {
      init(gl);
    }

    gl.glEnableVertexAttribArray(
        ShaderAttributes.getInstance().getVertexLocation());
    gl.glEnableVertexAttribArray(
        ShaderAttributes.getInstance().getNormalLocation());
    gl.glEnableVertexAttribArray(
        ShaderAttributes.getInstance().getColorLocation());
    gl.glEnableVertexAttribArray(
        ShaderAttributes.getInstance().getTexCoordsLocation());

    try {
      gl.glVertexAttribPointer(
          ShaderAttributes.getInstance().getVertexLocation(), 3, GL2.GL_FLOAT,
          false, 0, positionBuffer);
      gl.glVertexAttribPointer(
          ShaderAttributes.getInstance().getNormalLocation(), 3, GL2.GL_FLOAT,
          false, 0, normalBuffer);
      gl.glVertexAttribPointer(
          ShaderAttributes.getInstance().getColorLocation(), 4, GL2.GL_FLOAT,
          false, 0, colorBuffer);
      gl.glVertexAttribPointer(
          ShaderAttributes.getInstance().getTexCoordsLocation(), 2, GL2.GL_FLOAT,
          false, 0, texCoordsBuffer);

      gl.glDrawElements(primitiveType, renderVertices.size(),
          GL2.GL_UNSIGNED_INT, indexBuffer);
    } catch (Exception e) {
      System.out.println("Fehler: " + e);
    }

    Shader.checkGlError(gl);
  }

  /**
   * Delete all buffers.
   */
  public void invalidate() {
    positionBuffer = null;
    normalBuffer = null;
    colorBuffer = null;
    texCoordsBuffer = null;

    System.out.println("TODO: Free allocated mem.");
  }
}
