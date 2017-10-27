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
 * Represents a bounding box in the scene graph
 *
 * @author Philipp Jenke
 */
public class BoundingBoxNode extends LeafNode {

    /**
     * Cube side length
     */
    private AxisAlignedBoundingBox bbox;

    private Vector color = new Vector(0.25, 0.75, 0.25, 1);

    /**
     * VBO.
     */
    private VertexBufferObject vbo = new VertexBufferObject();

    /**
     * Constructor.
     */
    public BoundingBoxNode(AxisAlignedBoundingBox bbox) {
        this.bbox = bbox;
        createVbo();
    }

    private void createVbo() {
        List<RenderVertex> renderVertices = new ArrayList<RenderVertex>();

        Vector ll = bbox.getLL();
        Vector ur = bbox.getUR();

        Vector p0 = new Vector(ll.x(), ll.y(), ll.z());
        Vector p1 = new Vector(ll.x(), ur.y(), ll.z());
        Vector p2 = new Vector(ur.x(), ur.y(), ll.z());
        Vector p3 = new Vector(ur.x(), ll.y(), ll.z());
        Vector p4 = new Vector(ll.x(), ll.y(), ur.z());
        Vector p5 = new Vector(ll.x(), ur.y(), ur.z());
        Vector p6 = new Vector(ur.x(), ur.y(), ur.z());
        Vector p7 = new Vector(ur.x(), ll.y(), ur.z());
        Vector n = new Vector(0, 1, 0);

        renderVertices.add(new RenderVertex(p0, n, color));
        renderVertices.add(new RenderVertex(p1, n, color));
        renderVertices.add(new RenderVertex(p1, n, color));
        renderVertices.add(new RenderVertex(p2, n, color));
        renderVertices.add(new RenderVertex(p2, n, color));
        renderVertices.add(new RenderVertex(p3, n, color));
        renderVertices.add(new RenderVertex(p3, n, color));
        renderVertices.add(new RenderVertex(p0, n, color));
        renderVertices.add(new RenderVertex(p4, n, color));
        renderVertices.add(new RenderVertex(p5, n, color));
        renderVertices.add(new RenderVertex(p5, n, color));
        renderVertices.add(new RenderVertex(p6, n, color));
        renderVertices.add(new RenderVertex(p6, n, color));
        renderVertices.add(new RenderVertex(p7, n, color));
        renderVertices.add(new RenderVertex(p7, n, color));
        renderVertices.add(new RenderVertex(p4, n, color));
        renderVertices.add(new RenderVertex(p0, n, color));
        renderVertices.add(new RenderVertex(p4, n, color));
        renderVertices.add(new RenderVertex(p1, n, color));
        renderVertices.add(new RenderVertex(p5, n, color));
        renderVertices.add(new RenderVertex(p2, n, color));
        renderVertices.add(new RenderVertex(p6, n, color));
        renderVertices.add(new RenderVertex(p3, n, color));
        renderVertices.add(new RenderVertex(p7, n, color));

        vbo.setup(renderVertices, GLES20.GL_LINES);
    }


    @Override
    public void drawGL(RenderMode mode, Matrix modelMatrix) {
        ShaderAttributes.getInstance().setShaderModeParameter(Shader.ShaderMode.NO_LIGHTING);
        if (mode == RenderMode.REGULAR) {
            vbo.draw();
        }
    }

    public void setBoundingBox(AxisAlignedBoundingBox bbox) {
        this.bbox = bbox;
        vbo.invalidate();
        createVbo();
    }

    public void setColor(Vector color) {
        this.color.copy(color);
        vbo.invalidate();
    }

    @Override
    public AxisAlignedBoundingBox getBoundingBox() {
        return bbox;
    }
}
