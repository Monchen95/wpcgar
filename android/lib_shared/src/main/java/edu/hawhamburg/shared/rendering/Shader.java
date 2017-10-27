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

import android.opengl.GLES20;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;

import edu.hawhamburg.shared.misc.AssetPath;
import edu.hawhamburg.shared.misc.Constants;

/**
 * Representation of GLSL shader.
 *
 * @author Philipp Jenke
 */
public class Shader {

    private static final String LOGTAG = "WP Computer Graphics AR";

    /**
     * Shader constants.
     */
    private static final int COMPILE_STATUS_OK = 1;

    public static enum ShaderType {
        VERTEX, FRAGMENT
    }

    ;

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

    public Shader() {
        this("shader/vertex_shader.glsl",
                "shader/fragment_shader.glsl");
    }

    public Shader(String vertexShaderFilename, String fragmentShaderFilename) {
        this.vertexShaderFilename = vertexShaderFilename;
        this.fragmentShaderFilename = fragmentShaderFilename;
    }

    /**
     * Compile and link the shaders.
     */
    public void compileAndLink() {
        compiled = true;

        checkGlError("before compile shader");

        // Compile
        int v = compileShader(getGlShaderType(Shader.ShaderType.VERTEX),
                vertexShaderFilename);
        int f = compileShader(getGlShaderType(Shader.ShaderType.FRAGMENT),
                fragmentShaderFilename);
        if (v < 0 || f < 0) {
            Log.e(LOGTAG, "Shader not created.");
            return;
        }

        checkGlError("before link shader");

        // Link
        shaderProgram = linkProgram(v, f);
        if (shaderProgram < 0) {
            Log.e(LOGTAG, "Shader not created.");
            return;
        }

        checkGlError("after link shader");

        Log.i(LOGTAG, "Successfully created shader from vertex shader filename "
                + vertexShaderFilename + " and fragment shader fileame "
                + fragmentShaderFilename);
    }

    /**
     * Activate the shader
     */
    public void use() {
        if (!isCompiled()) {
            compileAndLink();
            checkGlError();
        }
        checkGlError();
        GLES20.glUseProgram(shaderProgram);
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


        return AssetPath.getInstance().readTextFileToString(shaderFilename);


    /*
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
    */
    }

    /**
     * Convert to GL shader constants.
     */
    private int getGlShaderType(Shader.ShaderType type) {
        if (type == Shader.ShaderType.VERTEX) {
            return GLES20.GL_VERTEX_SHADER;
        } else if (type == Shader.ShaderType.FRAGMENT) {
            return GLES20.GL_FRAGMENT_SHADER;
        } else {
            return -1;
        }
    }

    /**
     * Link the vertex and fragment shaders.
     */
    private int linkProgram(int vertexShaderId, int fragmentShaderId) {
        checkGlError("before link shader");
        int shaderProgram = GLES20.glCreateProgram();
        checkGlError("before attach shader");
        GLES20.glAttachShader(shaderProgram, vertexShaderId);
        GLES20.glAttachShader(shaderProgram, fragmentShaderId);
        checkGlError("before link shader program");
        GLES20.glLinkProgram(shaderProgram);
        checkGlError("before validate shader program");
        GLES20.glValidateProgram(shaderProgram);
        checkGlError("after shader link");
        return shaderProgram;
    }

    /**
     * Compile the specified shader from the filename and return the OpenGL id.
     */
    private int compileShader(int shaderType, String shaderFilename) {
        String vsrc = Shader.readShaderSource(shaderFilename);
        int id = compileShaderFromSource(shaderType, vsrc);
        if (id < 0) {
            Log.e(LOGTAG, "Compile error in shader file " + shaderFilename + " of type " + shaderType);
        }
        return id;
    }

    /**
     * Compile the specified shader from the filename and return the OpenGL id.
     */
    private int compileShaderFromSource(int shaderType,
                                        String shaderSource) {
        int id = GLES20.glCreateShader(shaderType);
        GLES20.glShaderSource(id, shaderSource);
        GLES20.glCompileShader(id);
        if (checkCompileError(id)) {
            String errorMsg = getCompileErrorMessage(id);
            Log.e(LOGTAG, "Shader " + shaderType + " compile error!\n" + errorMsg);
            return -1;
        }
        return id;
    }

    /**
     * Extract the error message.
     */
    private String getCompileErrorMessage(int id) {
        IntBuffer intBuffer = IntBuffer.allocate(1);
        GLES20.glGetShaderiv(id, GLES20.GL_INFO_LOG_LENGTH, intBuffer);
        int size = intBuffer.get(0);
        String errorMessage = "";
        if (size > 0) {
            ByteBuffer byteBuffer = ByteBuffer.allocate(size);
            return GLES20.glGetShaderInfoLog(id);
        }
        return errorMessage;
    }

    /**
     * Check if a compile error (vertex or fragment shader) occurred?
     */
    private boolean checkCompileError(int id) {
        IntBuffer intBuffer = IntBuffer.allocate(1);
        GLES20.glGetShaderiv(id, GLES20.GL_COMPILE_STATUS, intBuffer);
        return intBuffer.get(0) != COMPILE_STATUS_OK;
    }

    public static void checkGlError() {
        checkGlError("");
    }

    public static void checkGlError(String msg) {
        Map<Integer, String> glErrorMap = new HashMap<Integer, String>();
        glErrorMap.put(GLES20.GL_NO_ERROR, "GL_NO_ERROR");
        glErrorMap.put(GLES20.GL_INVALID_ENUM, "GL_INVALID_ENUM");
        glErrorMap.put(GLES20.GL_INVALID_VALUE, "GL_INVALID_VALUE");
        glErrorMap.put(GLES20.GL_INVALID_OPERATION, "GL_INVALID_OPERATION");
        glErrorMap.put(GLES20.GL_OUT_OF_MEMORY, "GL_OUT_OF_MEMORY");
        glErrorMap.put(GLES20.GL_INVALID_FRAMEBUFFER_OPERATION,
                "GL_INVALID_FRAMEBUFFER_OPERATION");
        int err = GLES20.GL_NO_ERROR;
        do {
            err = GLES20.glGetError();
            if (err != GLES20.GL_NO_ERROR) {
                if (glErrorMap.containsKey(err)) {
                    Log.e(Constants.LOGTAG, "OpenGL ES error (" + msg + "): " + glErrorMap.get(err));
                } else {
                    Log.e(Constants.LOGTAG, "OpenGL ES error (" + msg + "): " + err);
                }
            }
        } while (err != GLES20.GL_NO_ERROR);
    }

    public int getProgram() {
        return shaderProgram;
    }

}
