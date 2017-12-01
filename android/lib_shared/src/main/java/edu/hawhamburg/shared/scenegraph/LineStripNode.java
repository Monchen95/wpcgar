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
 * Created by abm510 on 01.12.2017.
 */

public class LineStripNode extends LeafNode {

    private VertexBufferObject vbo = new VertexBufferObject();


    private List<Vector> vertecies = new ArrayList<>();
    private Vector normal;
    private Vector color;

    public LineStripNode(List<Vector> vertecies, Vector color){
        this.vertecies = vertecies;
        this.color = color;
        normal = new Vector(0,1,0);
        createVbo();
    }

    public LineStripNode(Vector vertexA, Vector vertexB, Vector color){
        vertecies.add(vertexA);
        vertecies.add(vertexB);
        this.color = color;
        normal = new Vector(0,1,0);
        createVbo();
    }

    private void createVbo() {
        List<RenderVertex> renderVertices = new ArrayList<RenderVertex>();

//        for(Vector v: vertecies){
 //           renderVertices.add(new RenderVertex(v,normal,color));
  //      }

        ///////////
        Vector tmpVector = vertecies.remove(0);
        vertecies.add(tmpVector);
        for(Vector v: vertecies){
            renderVertices.add(new RenderVertex(v,normal,color));
        }
        ///////////
        vbo.setup(renderVertices, GLES20.GL_LINES);
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
        return null;
    }
}
