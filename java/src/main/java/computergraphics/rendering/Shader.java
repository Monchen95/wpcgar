/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * 
 * Base framework for "WP Computergrafik".
 */
package computergraphics.rendering;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;

/**
 * Representation of GLSL shader.
 * 
 * @author Philipp Jenke
 */
public class Shader {

  /**
   * Shader constants.
   */
  private static final int COMPILE_STATUS_OK = 1;

  public static enum ShaderType {
    VERTEX, FRAGMENT
  };

  public static enum ShaderMode {
    PHONG, TEXTURE, NO_LIGHTING, AMBIENT_ONLY
  }

  /**
   * Flag for the state of the shaders
   */
  private boolean compiled = false;

  /**
   * ID of the shader program.
   */
  private int shaderProgram = -1;

  /**
   * Filename of the vertex shader source
   */
  private String vertexShaderFilename = "";

  /**
   * Filename of the pixel shader source
   */
  private String fragmentShaderFilename = "";

  /**
   * Current shader mode
   */
  private ShaderMode mode = ShaderMode.PHONG;

  public Shader(ShaderMode mode) {
    this("assets/shader/vertex_shader.glsl",
        "assets/shader/fragment_shader.glsl", mode);
  }

  public Shader(String vertexShaderFilename, String fragmentShaderFilename,
      ShaderMode mode) {
    this.mode = mode;
    this.vertexShaderFilename = vertexShaderFilename;
    this.fragmentShaderFilename = fragmentShaderFilename;
  }

  /**
   * Compile and link the shaders.
   */
  public void compileAndLink(GL2 gl) {
    compiled = true;

    checkGlError(gl);

    // Compile
    int v = compileShader(gl, getGlShaderType(Shader.ShaderType.VERTEX),
        vertexShaderFilename);
    int f = compileShader(gl, getGlShaderType(Shader.ShaderType.FRAGMENT),
        fragmentShaderFilename);
    if (v < 0 || f < 0) {
      System.out.println("Shader not created.");
      return;
    }

    checkGlError(gl);

    // Link
    shaderProgram = linkProgram(gl, v, f);
    if (shaderProgram < 0) {
      System.out.println("Shader not created.");
      return;
    }

    System.out
        .println("Successfully created shader from vertex shader filename "
            + vertexShaderFilename + " and fragment shader fileame "
            + fragmentShaderFilename);
  }

  /**
   * Activate the shader
   */
  public void use(GL2 gl) {
    if (!isCompiled()) {
      compileAndLink(gl);
      checkGlError(gl);
    }
    checkGlError(gl);
    gl.glUseProgram(shaderProgram);
  }

  /**
   * Getter.
   */
  public boolean isCompiled() {
    return compiled;
  }

  /**
   * Read a shader code from a source file to a String.
   */
  private static String readShaderSource(String shaderFilename) {
    String absoluteShaderFilename = shaderFilename;
    String shaderSource = "";
    if (absoluteShaderFilename == null) {
      System.out.println("Shader source " + shaderFilename
          + " not found - cannot read shader.");
      return shaderSource;
    }

    BufferedReader br = null;
    try {
      br = new BufferedReader(new FileReader(absoluteShaderFilename));
      StringBuilder sb = new StringBuilder();
      String line = br.readLine();
      while (line != null) {
        sb.append(line);
        sb.append("\n");
        line = br.readLine();
      }
      br.close();
      shaderSource = sb.toString();
    } catch (FileNotFoundException e) {
      System.out
          .println("Failed to read shader source " + absoluteShaderFilename);
      e.printStackTrace();
    } catch (IOException e) {
      System.out
          .println("Failed to read shader source " + absoluteShaderFilename);
      e.printStackTrace();
    }
    return shaderSource;
  }

  /**
   * Convert to GL shader constants.
   */
  private int getGlShaderType(Shader.ShaderType type) {
    if (type == Shader.ShaderType.VERTEX) {
      return GL2.GL_VERTEX_SHADER;
    } else if (type == Shader.ShaderType.FRAGMENT) {
      return GL2.GL_FRAGMENT_SHADER;
    } else {
      return -1;
    }
  }

  /**
   * Link the vertex and fragment shaders.
   */
  private int linkProgram(GL2 gl, int vertexShaderId, int fragmentShaderId) {
    checkGlError(gl);
    int shaderProgram = gl.glCreateProgram();
    gl.glAttachShader(shaderProgram, vertexShaderId);
    gl.glAttachShader(shaderProgram, fragmentShaderId);
    gl.glLinkProgram(shaderProgram);
    gl.glValidateProgram(shaderProgram);
    checkGlError(gl);
    return shaderProgram;
  }

  /**
   * Compile the specified shader from the filename and return the OpenGL id.
   */
  private int compileShader(GL2 gl, int shaderType, String shaderFilename) {
    String vsrc = Shader.readShaderSource(shaderFilename);
    int id = compileShaderFromSource(gl, shaderType, vsrc);
    if (id < 0) {
      System.out.println("Compile error in shader file " + shaderFilename);
    }
    return id;
  }

  /**
   * Compile the specified shader from the filename and return the OpenGL id.
   */
  private int compileShaderFromSource(GL2 gl, int shaderType,
      String shaderSource) {
    int id = gl.glCreateShader(shaderType);
    gl.glShaderSource(id, 1, new String[] { shaderSource }, (int[]) null, 0);
    gl.glCompileShader(id);
    if (checkCompileError(id, gl)) {
      String errorMsg = getCompileErrorMessage(id, gl);
      System.out.println(errorMsg);
      return -1;
    }
    return id;
  }

  /**
   * Extract the error message.
   */
  private String getCompileErrorMessage(int id, GL2 gl) {
    IntBuffer intBuffer = IntBuffer.allocate(1);
    gl.glGetShaderiv(id, GL2.GL_INFO_LOG_LENGTH, intBuffer);
    int size = intBuffer.get(0);
    String errorMessage = "";
    if (size > 0) {
      ByteBuffer byteBuffer = ByteBuffer.allocate(size);
      gl.glGetShaderInfoLog(id, size, intBuffer, byteBuffer);
      for (byte b : byteBuffer.array()) {
        errorMessage += (char) b;
      }
    }
    return errorMessage;
  }

  /**
   * Check if a compile error (vertex or fragment shader) occurred?
   */
  private boolean checkCompileError(int id, GL2 gl) {
    IntBuffer intBuffer = IntBuffer.allocate(1);
    gl.glGetShaderiv(id, GL2.GL_COMPILE_STATUS, intBuffer);
    return intBuffer.get(0) != COMPILE_STATUS_OK;
  }

  public static void checkGlError(GL2 gl) {
    Map<Integer, String> glErrorMap = new HashMap<Integer, String>();
    glErrorMap.put(GL2.GL_NO_ERROR, "GL_NO_ERROR");
    glErrorMap.put(GL2.GL_INVALID_ENUM, "GL_INVALID_ENUM");
    glErrorMap.put(GL2.GL_INVALID_VALUE, "GL_INVALID_VALUE");
    glErrorMap.put(GL2.GL_INVALID_OPERATION, "GL_INVALID_OPERATION");
    glErrorMap.put(GL2.GL_OUT_OF_MEMORY, "GL_OUT_OF_MEMORY");
    glErrorMap.put(GL2.GL_INVALID_FRAMEBUFFER_OPERATION,
        "GL_INVALID_FRAMEBUFFER_OPERATION");
    glErrorMap.put(GL2.GL_TABLE_TOO_LARGE, "GL_TABLE_TOO_LARGE");
    int err = GL.GL_NO_ERROR;
    do {
      err = gl.glGetError();
      if (err != GL2.GL_NO_ERROR) {
        if (glErrorMap.containsKey(err)) {
          System.out.println("GL error: " + glErrorMap.get(err));
        } else {
          // System.out.println("Undefined GL error: " + glErrorMap.get(err));
        }
      }
    } while (err != GL2.GL_NO_ERROR);
  }

  public void setMode(ShaderMode mode) {
    this.mode = mode;
  }

  public ShaderMode getMode() {
    return mode;
  }

  public int getProgram() {
    return shaderProgram;
  }

}
