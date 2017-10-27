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
package edu.hawhamburg.shared.scenegraph;

import android.opengl.GLES20;

import java.util.ArrayList;
import java.util.List;

import edu.hawhamburg.shared.math.AxisAlignedBoundingBox;
import edu.hawhamburg.shared.math.Matrix;
import edu.hawhamburg.shared.math.Vector;
import edu.hawhamburg.shared.rendering.RenderVertex;
import edu.hawhamburg.shared.rendering.Shader;
import edu.hawhamburg.shared.rendering.ShaderAttributes;
import edu.hawhamburg.shared.rendering.VertexBufferObject;

/**
 * Geometry node for a sphere with arbitary radius, centered at the origin.
 *
 * @author Philipp Jenke
 */
public class SphereNode extends LeafNode {

    /**
     * Sphere radius.
     */
    private double radius;

    /**
     * Resolution (in one dimension) of the mesh.
     */
    private int resolution;

    private Vector color = new Vector(0.75, 0.25, 0.25, 1);

    /**
     * VBO.
     */
    private VertexBufferObject vbo;

    /**
     * Constructor.
     */
    public SphereNode(double radius, int resolution) {
        this.radius = radius;
        this.resolution = resolution;
        vbo = new VertexBufferObject();
        createVbo();
    }

    private void createVbo() {
        List<RenderVertex> renderVertices = new ArrayList<RenderVertex>();

        float dTheta = (float) (Math.PI / resolution);
        float dPhi = (float) (Math.PI * 2.0 / resolution);
        for (int i = 0; i < resolution; i++) {
            for (int j = 0; j < resolution; j++) {
                Vector p0 = evaluateSpherePoint(i * dTheta, j * dPhi);
                Vector p1 = evaluateSpherePoint(i * dTheta, (j + 1) * dPhi);
                Vector p2 = evaluateSpherePoint((i + 1) * dTheta, (j + 1) * dPhi);
                Vector p3 = evaluateSpherePoint((i + 1) * dTheta, j * dPhi);
                Vector normal = evaluateSpherePoint((i + 0.5f) * dTheta,
                        (j + 0.5f) * dPhi).getNormalized();
                AddSideVertices(renderVertices, p0, p1, p2, p3, normal, color);
            }
        }
        vbo.setup(renderVertices, GLES20.GL_TRIANGLES);
    }

    @Override
    public void drawGL(RenderMode mode, Matrix modelMatrix) {
        ShaderAttributes.getInstance().setShaderModeParameter(Shader.ShaderMode.PHONG);
        if (mode == RenderMode.REGULAR) {
            vbo.draw();
        }
    }

    /**
     * Compute a surface point for given sphere coordinates.
     */
    private Vector evaluateSpherePoint(float theta, float phi) {
        float x = (float) (radius * Math.sin(theta) * Math.cos(phi));
        float y = (float) (radius * Math.sin(theta) * Math.sin(phi));
        float z = (float) (radius * Math.cos(theta));
        return new Vector(x, y, z);
    }

    /**
     * Add 4 vertices to the array
     */
    private void AddSideVertices(List<RenderVertex> renderVertices, Vector p0,
                                 Vector p1, Vector p2, Vector p3, Vector normal, Vector color) {
        renderVertices.add(new RenderVertex(p3, normal, color));
        renderVertices.add(new RenderVertex(p2, normal, color));
        renderVertices.add(new RenderVertex(p0, normal, color));
        renderVertices.add(new RenderVertex(p2, normal, color));
        renderVertices.add(new RenderVertex(p1, normal, color));
        renderVertices.add(new RenderVertex(p0, normal, color));
    }

    public void setColor(Vector color) {
        this.color = color;
        createVbo();
    }

    @Override
    public AxisAlignedBoundingBox getBoundingBox() {
        return new AxisAlignedBoundingBox(new Vector(-radius, -radius, -radius),
                new Vector(radius, radius, radius));
    }
}
