package computergraphics.datastructures.mesh;

import computergraphics.math.Vector;

public class TriangleMeshFactory {
  /**
   * Create sphere.
   */
  public static void createSphere(ITriangleMesh mesh, float radius,
      int resolution) {
    mesh.clear();
    float dTheta = (float) (Math.PI / (resolution + 1));
    float dPhi = (float) (Math.PI * 2.0 / resolution);
    // Create vertices

    // 0-180 degrees: i, theta
    for (int i = 0; i < resolution; i++) {

      // 0-360 degres: j, phi
      for (int j = 0; j < resolution; j++) {
        Vector p0 = evaluateSpherePoint((i + 1) * dTheta, j * dPhi, radius);
        mesh.addVertex(p0);
      }
    }
    int leftIndex = mesh.addVertex(new Vector(0, 0, radius));
    int rightIndex = mesh.addVertex(new Vector(0, 0, -radius));
    // Add triangles
    for (int i = 0; i < resolution - 1; i++) {
      for (int j = 0; j < resolution; j++) {
        mesh.addTriangle(getSphereIndex(i, j, resolution),
            getSphereIndex(i + 1, j, resolution),
            getSphereIndex(i + 1, j + 1, resolution));
        mesh.addTriangle(getSphereIndex(i, j, resolution),
            getSphereIndex(i + 1, j + 1, resolution),
            getSphereIndex(i, j + 1, resolution));
      }
    }
    for (int j = 0; j < resolution; j++) {
      mesh.addTriangle(getSphereIndex(0, j, resolution),
          getSphereIndex(0, (j + 1) % resolution, resolution), leftIndex);
      mesh.addTriangle(getSphereIndex(resolution - 1, j, resolution),
          rightIndex,
          getSphereIndex(resolution - 1, (j + 1) % resolution, resolution));
    }

    mesh.computeTriangleNormals();
  }

  private static Vector evaluateSpherePoint(float theta, float phi,
      float radius) {
    float x = (float) (radius * Math.sin(theta) * Math.cos(phi));
    float y = (float) (radius * Math.sin(theta) * Math.sin(phi));
    float z = (float) (radius * Math.cos(theta));
    return new Vector(x, y, z);
  }

  private static int getSphereIndex(int i, int j, int resolution) {
    return (i % resolution) * resolution + (j % resolution);
  }

  public static void createSquare(ITriangleMesh mesh, float extend) {
    mesh.clear();
    mesh.addVertex(new Vector(-extend, 0, -extend));
    mesh.addVertex(new Vector(extend, 0, -extend));
    mesh.addVertex(new Vector(extend, 0, extend));
    mesh.addVertex(new Vector(-extend, 0, extend));
    mesh.addTriangle(0, 2, 1);
    mesh.addTriangle(0, 3, 2);
    mesh.computeTriangleNormals();
  }
}
