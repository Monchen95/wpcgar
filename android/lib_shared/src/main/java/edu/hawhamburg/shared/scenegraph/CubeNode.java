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
 * Representation of a cuboid with different dimensions in x-, y- and
 * z-direction.
 *
 * @author Philipp Jenke
 */
public class CubeNode extends LeafNode {

    /**
     * Cube side length
     */
    private double sideLength;

    /**
     * VBO.
     */
    private VertexBufferObject vbo = new VertexBufferObject();

    /**
     * Constructor.
     */
    public CubeNode(double sideLength) {
        this.sideLength = sideLength;
        createVbo();
    }

    private void createVbo() {
        List<RenderVertex> renderVertices = new ArrayList<RenderVertex>();

        Vector p0 = new Vector(-sideLength, -sideLength, -sideLength);
        Vector p1 = new Vector(sideLength, -sideLength, -sideLength);
        Vector p2 = new Vector(sideLength, sideLength, -sideLength);
        Vector p3 = new Vector(-sideLength, sideLength, -sideLength);
        Vector p4 = new Vector(-sideLength, -sideLength, sideLength);
        Vector p5 = new Vector(sideLength, -sideLength, sideLength);
        Vector p6 = new Vector(sideLength, sideLength, sideLength);
        Vector p7 = new Vector(-sideLength, sideLength, sideLength);
        Vector n0 = new Vector(0, 0, -1);
        Vector n1 = new Vector(1, 0, 0);
        Vector n2 = new Vector(0, 0, 1);
        Vector n3 = new Vector(-1, 0, 0);
        Vector n4 = new Vector(0, 1, 0);
        Vector n5 = new Vector(0, -1, 0);
        Vector color = new Vector(0.25, 0.75, 0.25, 1);

        AddSideVertices(renderVertices, p0, p1, p2, p3, n0, color);
        AddSideVertices(renderVertices, p1, p5, p6, p2, n1, color);
        AddSideVertices(renderVertices, p4, p7, p6, p5, n2, color);
        AddSideVertices(renderVertices, p0, p3, p7, p4, n3, color);
        AddSideVertices(renderVertices, p2, p6, p7, p3, n4, color);
        AddSideVertices(renderVertices, p5, p1, p0, p4, n5, color);

        vbo.setup(renderVertices, GLES20.GL_TRIANGLES);
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

    @Override
    public void drawGL(RenderMode mode, Matrix modelMatrix) {
        ShaderAttributes.getInstance().setShaderModeParameter(Shader.ShaderMode.PHONG);
        if (mode == RenderMode.REGULAR) {
            vbo.draw();
        }
    }

    @Override
    public AxisAlignedBoundingBox getBoundingBox() {
        return new AxisAlignedBoundingBox(new Vector(-sideLength, -sideLength, -sideLength),
                new Vector(sideLength, sideLength, sideLength));
    }
}
