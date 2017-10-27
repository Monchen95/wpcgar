/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * 
 * Base framework for "WP Computergrafik".
 */

package computergraphics.datastructures.mesh;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import computergraphics.math.Vector;
import computergraphics.misc.AssetPath;
import computergraphics.projects.trianglemesh.Material;
import computergraphics.projects.trianglemesh.TriangleMesh;

/**
 * Read OBJ file and fill triangle mesh with the content.
 * 
 * @author Philipp Jenke
 */
public class ObjReader {

  private static final String OP_MAT_LIB = "mtllib";
  private static final String OP_VERTEX = "v";
  private static final String OP_FACET = "f";
  private static final String OP_TEXTURE_COORDINATE = "vt";
  private static final String OP_MAT_NEW_MAT = "newmtl";
  private static final String OP_MAT_KD = "Kd";
  private static final String OP_MAT_TEXTURE = "map_Kd";
  private static final String OP_NEW_OBJECT = "o";
  private static final String OP_USE_MTL = "usemtl";

  // Subdirectory used to look for the material file (extracted from mesh file).
  private String directory = "";

  // State of the import process
  private ITriangleMesh currentMesh = null;
  private Material currentMaterial = null;
  private Map<String, Material> materials = null;
  private List<ITriangleMesh> meshes = new ArrayList<ITriangleMesh>();
  private int vertexIndexOffset = 0;
  private int texCoordOffset = 0;

  /**
   * Lesen eines Dreiecksnetzes aus einer OBJ-Datei. Die Information wird in das
   * Dreiecksnetz 'mesh' geschrieben.
   * 
   * Returns true if the mesh is successfully read.
   */
  public List<ITriangleMesh> read(final String filename) {
    // Setup
    meshes.clear();
    directory = new File(filename).getParent() + "/";
    currentMesh = new TriangleMesh();
    meshes.add(currentMesh);
    materials = new HashMap<String, Material>();
    vertexIndexOffset = 0;
    texCoordOffset = 0;

    // Read input
    System.out.println("Trying to read OBJ file " + filename);
    InputStream inputStream = getInputStream(filename);
    try {
      String strLine = "";
      DataInputStream in = new DataInputStream(inputStream);
      BufferedReader br = new BufferedReader(new InputStreamReader(in));
      while ((strLine = br.readLine()) != null) {
        parseLine(strLine);
      }
      in.close();
    } catch (Exception e) {
      System.out.println("Error reading from the OBJ file.");
      return meshes;
    }

    // Post-process meshes
    for (Iterator<ITriangleMesh> it = meshes.iterator(); it.hasNext();) {
      ITriangleMesh mesh = it.next();
      if (mesh.getNumberOfTriangles() == 0) {
        it.remove();
        continue;
      }
      mesh.computeTriangleNormals();
      System.out.println("Successfully created triangle mesh with "
          + mesh.getNumberOfVertices() + " vertices and "
          + mesh.getNumberOfTriangles() + " triangles.");
    }

    if (meshes.size() == 0) {
      System.out.println("Could not find any meshes in OBJ file " + filename);
    }
    return meshes;
  }

  /**
   * Get in input stream from a file.
   */
  private InputStream getInputStream(String filename) {
    String path = AssetPath.getPathToAsset(filename);
    if (!(new File(path).exists())) {
      System.out.println(
          "Mesh file " + filename + " with path " + path + " cannot be found.");
      return null;
    }

    InputStream is;
    try {
      is = new FileInputStream(path);
      return is;
    } catch (FileNotFoundException e) {
      return null;
    }
  }

  /**
   * Einlesen einer Zeile aus der OBJ-Datei.
   */
  private void parseLine(String strLine) {
    String line = trim(strLine);
    String operator = getOperator(line);
    if (operator.equals(OP_MAT_LIB)) {
      // Lesen der Materialdatei (Texturname)
      parseUseMaterial(line);
    }
    if (operator.equals(OP_NEW_OBJECT)) {
      // Neues (Teil-)Objekt
      vertexIndexOffset += currentMesh.getNumberOfVertices();
      texCoordOffset += currentMesh.getNumberOfTextureCoordinates();
      currentMesh = new TriangleMesh();
      meshes.add(currentMesh);
    } else if (operator.equals(OP_USE_MTL)) {
      String[] components = line.split("\\s+");
      if (components.length > 1) {
        String materalId = components[1];
        // System.out.println("Assigned material: " + materalId);
        Material mat = materials.get(materalId);
        if (mat != null) {
          currentMesh.setColor(mat.getColor());
          if (mat.getTextureFilename() != null
              && mat.getTextureFilename().length() > 0) {
            System.out.println("TODO: set texture " + mat.getTextureFilename());
          }
        }
      }
    } else if (operator.equals(OP_VERTEX)) {
      // Lesen eines Vertex
      Vector position = parseVertex(line);
      if (position != null) {
        currentMesh.addVertex(position);
      }
    } else if (operator.equals(OP_FACET)) {
      try {
        // Lesen einer Facette (Dreieck)
        parseFacet(line);
      } catch (Exception e) {
        System.out.println("Failed to parse facet.");
      }
    } else if (operator.equals(OP_TEXTURE_COORDINATE)) {
      // Lesen einer Texturkoordinate
      Vector t = parseTextureCoordinate(line);
      if (t != null) {
        currentMesh.addTextureCoordinate(t);
      }
    }
  }

  /**
   * Einlesen einer Materialdatei mit Texturinformtion. Liefert den Namen der
   * Texturdatei. Liefert null, falls keine Textur gefunden wurde.
   */
  private void parseUseMaterial(String line) {
    String[] components = line.split("\\s+");
    if (components.length == 2) {
      String materialFilename = components[1];
      parseMaterialFile(directory + materialFilename);
    }
  }

  private void parseMaterialFile(String materialFilename) {
    System.out.println("Trying to read material file " + materialFilename);
    InputStream is = getInputStream(materialFilename);
    try {
      String strLine = "";
      DataInputStream in = new DataInputStream(is);
      BufferedReader br = new BufferedReader(new InputStreamReader(in));
      while ((strLine = br.readLine()) != null) {
        parseMaterialLine(strLine);
      }
      in.close();
    } catch (Exception e) {
      System.out.println(
          "Error reading from the material file " + materialFilename + ".");
      return;
    }

    // System.out.println("Materials:");
    // for (Material mat : materials.values()) {
    // System.out.println(" - " + mat);
    // }
  }

  private void parseMaterialLine(String strLine) {
    String line = trim(strLine);
    String operator = getOperator(line);
    if (operator.equals(OP_MAT_NEW_MAT)) {
      String[] components = strLine.split("\\s+");
      if (components.length >= 2) {
        String materialId = components[1];
        currentMaterial = new Material(materialId);
        materials.put(materialId, currentMaterial);
        // System.out.println("Found material " + materialId);
      }
    } else if (operator.equals(OP_MAT_KD)) {
      String[] components = strLine.split("\\s+");
      if (components.length >= 4) {
        Vector color = new Vector(Float.parseFloat(components[1]),
            Float.parseFloat(components[2]), Float.parseFloat(components[3]),
            1);
        currentMaterial.setColor(color);
      }
    } else if (operator.equals(OP_MAT_TEXTURE)) {
      String[] components = strLine.split("\\s+");
      if (components.length >= 2) {
        String textureFilename = components[1];
        if (textureFilename != null) {
          textureFilename = textureFilename.trim();
          if (textureFilename.length() != 0) {
            currentMaterial.setTextureFilename(textureFilename);
          }
        }
      }
    }
  }

  /**
   * Entfernen doppelter Leerzeichen aus einer Zeile.
   */
  private static String trim(String strLine) {
    String line = strLine.trim();
    line = line.replaceAll("  ", " ");
    int l = line.length();
    while (l < line.length()) {
      l = line.length();
      line = line.replaceAll("  ", " ");
    }
    return line;
  }

  /**
   * Auslesen einer Zeile, die eine Texturkoordinate beinhaltet.
   */
  private Vector parseTextureCoordinate(String strLine) {
    String line = trim(strLine);
    String[] allCoords = line.split("\\s+");
    float u = 0;
    float v = 0;
    if (allCoords.length >= 3) {
      u = getFloatValue(allCoords[1]);
      v = getFloatValue(allCoords[2]);
      return new Vector(u, v, 0);
    }

    return null;
  }

  /**
   * String -> float.
   */
  private float getFloatValue(String string) {
    if (string.length() == 0) {
      return 0;
    }
    return Float.valueOf(string);
  }

  /**
   * Lesen einer Zeile, die ein Dreieck repräsentiert (Indices der Eckpunkte,
   * Indices der Texturkoordinaten).
   */
  private void parseFacet(String strLine) {
    String[] allCoords = strLine.split("\\s+");
    if (allCoords.length == 4) {
      createTriangle(allCoords[1], allCoords[2], allCoords[3]);
    } else if (allCoords.length == 5) {
      createTriangle(allCoords[1], allCoords[2], allCoords[3]);
      createTriangle(allCoords[1], allCoords[3], allCoords[4]);
    }
  }

  private void createTriangle(String token1, String token2, String token3) {
    Triangle t = new Triangle(getVertexIndexFromToken(token1),
        getVertexIndexFromToken(token2), getVertexIndexFromToken(token3),
        getTexCoordIndexFromToken(token1), getTexCoordIndexFromToken(token2),
        getTexCoordIndexFromToken(token3));
    t.setColor(currentMesh.getColor());
    currentMesh.addTriangle(t);
  }

  private int getVertexIndexFromToken(String token) {
    String[] tokens = token.split("/");
    if (tokens.length > 0 && tokens[0].length() > 0) {
      return Integer.parseInt(tokens[0]) - 1 - vertexIndexOffset;
    } else {
      return -1;
    }
  }

  private int getTexCoordIndexFromToken(String token) {
    String[] tokens = token.split("/");
    if (tokens.length > 1 && tokens[1].length() > 0) {
      return Integer.parseInt(tokens[1]) - 1 - texCoordOffset;
    } else {
      return -1;
    }
  }

  /**
   * Lesen einer Zeile, die einen Vertex repräsentiert.
   */
  private Vector parseVertex(String strLine) {
    String[] components = strLine.split("\\s+");
    if (components.length >= 4) {
      float x = Float.parseFloat(components[1]);
      float y = Float.parseFloat(components[2]);
      float z = Float.parseFloat(components[3]);
      return new Vector(x, y, z);
    }
    return null;
  }

  /**
   * Extract the operator char from a line.
   * 
   * @param strLine
   * @return String representing the operator
   */
  private String getOperator(String strLine) {
    String[] components = strLine.split("\\s+");
    if (components.length > 0) {
      return components[0];
    } else {
      return "";
    }
  }
}
