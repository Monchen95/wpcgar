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

import edu.hawhamburg.shared.datastructures.mesh.AbstractTriangle;
import edu.hawhamburg.shared.datastructures.mesh.ITriangleMesh;
import edu.hawhamburg.shared.datastructures.mesh.TriangleMesh;
import edu.hawhamburg.shared.datastructures.mesh.Vertex;
import edu.hawhamburg.shared.math.AxisAlignedBoundingBox;
import edu.hawhamburg.shared.math.Matrix;
import edu.hawhamburg.shared.math.Vector;
import edu.hawhamburg.shared.rendering.RenderVertex;
import edu.hawhamburg.shared.rendering.Shader;
import edu.hawhamburg.shared.rendering.ShaderAttributes;
import edu.hawhamburg.shared.rendering.Texture;
import edu.hawhamburg.shared.rendering.TextureManager;
import edu.hawhamburg.shared.rendering.VertexBufferObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Scene graph node to render ITriangleMesh meshes.
 */
public class TriangleMeshNode extends LeafNode {
    /**
     * Enum for the render mode: describes which normals are used: triangle normals (flat shading)
     * or vertex normal (phong shading).
     */
    public static enum RenderNormals {
        PER_TRIANGLE_NORMAL, PER_VERTEX_NORMAL
    }

    /**
     * Contained triangle mesh to be rendered.
     */
    private ITriangleMesh mesh;

    /**
     * This node is used to render the shadow polygon mesh
     */
    private TriangleMeshNode shadowPolygonNode = null;

    /**
     * This is the shadow polygon mesh.
     */
    private TriangleMesh shadowPolygonMesh = new TriangleMesh();

    /**
     * Debugging: Show normals.
     */
    private boolean showNormals = false;

    /**
     * Select which normals should be used for rendering: triangle vs. vertex normals.
     */
    private RenderNormals renderNormals = RenderNormals.PER_TRIANGLE_NORMAL;

    /**
     * VBOs: triangles
     */
    private VertexBufferObject vbo = new VertexBufferObject();

    /**
     * VBOs: normals
     */
    private VertexBufferObject vboNormals = new VertexBufferObject();

    public TriangleMeshNode(ITriangleMesh mesh) {
        this.mesh = mesh;
        vbo.setup(createRenderVertices(), GLES20.GL_TRIANGLES);
        vboNormals.setup(createRenderVerticesNormals(), GLES20.GL_LINES);
    }

    /**
     * Set the transparency of the mesh.
     */
    public void setTransparency(double transparency) {
        mesh.setTransparency(transparency);
        updateVbo();
    }

    /**
     * Create vbo data for mesh rendering
     */
    private List<RenderVertex> createRenderVertices() {
        List<RenderVertex> renderVertices = new ArrayList<RenderVertex>();
        for (int i = 0; i < mesh.getNumberOfTriangles(); i++) {
            AbstractTriangle t = mesh.getTriangle(i);
            for (int j = 0; j < 3; j++) {
                Vertex vertex = mesh.getVertex(t, j);
                Vector normal = (renderNormals == RenderNormals.PER_VERTEX_NORMAL) ? vertex.getNormal() : t.getNormal();
                RenderVertex renderVertex = null;
                if (t.getTexCoordIndex(j) >= 0) {
                    renderVertex = new RenderVertex(vertex.getPosition(),
                            normal, t.getColor(), mesh.getTextureCoordinate(t.getTexCoordIndex(j)));
                } else {
                    renderVertex = new RenderVertex(vertex.getPosition(),
                            normal, t.getColor(), new Vector(0, 0));
                }
                renderVertices.add(renderVertex);
            }
        }
        return renderVertices;
    }

    /**
     * Create vbo data for normal rendering.
     */
    private List<RenderVertex> createRenderVerticesNormals() {
        List<RenderVertex> renderVertices = new ArrayList<RenderVertex>();
        double normalScale = 0.03;
        Vector color = new Vector(0.5, 0.5, 0.5, 1);
        for (int i = 0; i < mesh.getNumberOfTriangles(); i++) {
            AbstractTriangle t = mesh.getTriangle(i);
            Vector p = mesh.getVertex(t, 0).getPosition()
                    .add(mesh.getVertex(t, 1).getPosition())
                    .add(mesh.getVertex(t, 2).getPosition())
                    .multiply(1.0 / 3.0);
            renderVertices.add(new RenderVertex(p, t.getNormal(), color));
            renderVertices.add(new RenderVertex(
                    p.add(t.getNormal().multiply(normalScale)), t.getNormal(), color));
        }
        return renderVertices;
    }

    @Override
    public void drawGL(RenderMode mode, Matrix modelMatrix) {
        // Use texture if texture object != null

        if (mesh.hasTexture()) {
            Texture texture = mesh.getTexture();
            if (texture != null) {
                mesh.getTexture().bind();
                ShaderAttributes.getInstance().setShaderModeParameter(Shader.ShaderMode.TEXTURE);
            } else {
                ShaderAttributes.getInstance().setShaderModeParameter(Shader.ShaderMode.PHONG);
            }
        } else {
            ShaderAttributes.getInstance().setShaderModeParameter(Shader.ShaderMode.PHONG);
        }

        // Compute transformed light position
        Vector lightPosition = new Vector(1, 1, 1);
//        if (getRootNode() != null) {
//            Matrix invertedTransformation = modelMatrix.getInverse();
//            invertedTransformation = invertedTransformation.getTransposed();
//            Vector light4 = new Vector(getRootNode().getLightPosition().x(),
//                    getRootNode().getLightPosition().y(),
//                    getRootNode().getLightPosition().z(), 1);
//            Vector transformedLight = invertedTransformation.multiply(light4);
//            lightPosition =
//                    transformedLight.xyz().multiply(1.0f / transformedLight.w());
//        }

        if (mode == RenderMode.REGULAR) {
            drawRegular();
        } else if (mode == RenderMode.DEBUG_SHADOW_VOLUME) {
            drawShadowVolume(modelMatrix, lightPosition);
        } else if (mode == RenderMode.SHADOW_VOLUME) {
            drawShadowVolume(modelMatrix, lightPosition);
        } else if (mode == RenderMode.DARK) {
            ShaderAttributes.getInstance().setShaderModeParameter(Shader.ShaderMode.AMBIENT_ONLY);
            drawRegular();
        }
    }

    /**
     * Draw mesh regularly.
     */
    public void drawRegular() {
        vbo.draw();
        if (showNormals) {
            vboNormals.draw();
        }
    }

    /**
     * Render the shadow volumes.
     */
    public void drawShadowVolume(Matrix modelMatrix,
                                 Vector lightPosition) {
        mesh.createShadowPolygons(lightPosition, 500, shadowPolygonMesh);
        if (shadowPolygonNode == null) {
            shadowPolygonNode = new TriangleMeshNode(shadowPolygonMesh);
            shadowPolygonNode.setParentNode(this);
            shadowPolygonNode.vbo.setup(shadowPolygonNode.createRenderVertices(), GLES20.GL_TRIANGLES);
        }
        shadowPolygonNode.traverse(RenderMode.REGULAR, modelMatrix);
    }

    public void setShowNormals(boolean showNormals) {
        this.showNormals = showNormals;
    }

    public void setRenderNormals(RenderNormals renderNormals) {
        this.renderNormals = renderNormals;
        vbo.setup(createRenderVertices(), GLES20.GL_TRIANGLES);
    }

    public void updateVbo() {
        vbo.setup(createRenderVertices(), GLES20.GL_TRIANGLES);
        vboNormals.setup(createRenderVerticesNormals(), GLES20.GL_LINES);
        vbo.invalidate();
    }

    @Override
    public AxisAlignedBoundingBox getBoundingBox() {
        return mesh.getBoundingBox();
    }
}
