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
 * Representation of a coordinate frame with lines for the three axis in RBG.
 */
public class CoordinateFrameNode extends LeafNode {
    private VertexBufferObject vbo = new VertexBufferObject();

    public CoordinateFrameNode() {
        this(1);
    }

    public CoordinateFrameNode(float scale) {
        List<RenderVertex> renderVertices = new ArrayList<RenderVertex>();
        renderVertices.add(new RenderVertex(new Vector(0, 0, 0), new Vector(0, 1, 0), new Vector(1, 0, 0, 1)));
        renderVertices.add(new RenderVertex(new Vector(scale, 0, 0), new Vector(0, 1, 0), new Vector(1, 0, 0, 1)));
        renderVertices.add(new RenderVertex(new Vector(0, 0, 0), new Vector(0, 1, 0), new Vector(0, 1, 0, 1)));
        renderVertices.add(new RenderVertex(new Vector(0, scale, 0), new Vector(0, 1, 0), new Vector(0, 1, 0, 1)));
        renderVertices.add(new RenderVertex(new Vector(0, 0, 0), new Vector(0, 1, 0), new Vector(0, 0, 1, 1)));
        renderVertices.add(new RenderVertex(new Vector(0, 0, scale), new Vector(0, 1, 0), new Vector(0, 0, 1, 1)));
        vbo.setup(renderVertices, GLES20.GL_LINES);
    }

    @Override
    public void drawGL(RenderMode mode, Matrix modelMatrix) {
        ShaderAttributes.getInstance().setShaderModeParameter(Shader.ShaderMode.NO_LIGHTING);
        GLES20.glLineWidth(6);
        vbo.draw();
    }

    @Override
    public void timerTick(int counter) {
    }

    @Override
    public AxisAlignedBoundingBox getBoundingBox() {
        return new AxisAlignedBoundingBox(new Vector(0, 0, 0), new Vector(1, 1, 1));
    }
}
