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
package edu.hawhamburg.shared.misc;

import org.w3c.dom.Text;

import edu.hawhamburg.shared.datastructures.mesh.ITriangleMesh;
import edu.hawhamburg.shared.datastructures.mesh.TriangleMesh;
import edu.hawhamburg.shared.rendering.Shader;
import edu.hawhamburg.shared.rendering.TextureManager;
import edu.hawhamburg.shared.scenegraph.TriangleMeshNode;
import edu.hawhamburg.shared.math.Matrix;
import edu.hawhamburg.shared.math.Vector;
import edu.hawhamburg.shared.rendering.Texture;
import edu.hawhamburg.shared.scenegraph.Camera;
import edu.hawhamburg.shared.scenegraph.INode;

/**
 * A button displays a square on the display which can react to touch events.
 */
public class Button {

    private ITriangleMesh mesh = null;
    private TriangleMeshNode meshNode = null;
    private ButtonHandler handler = null;

    private Vector pos = new Vector(0, 0, 0);
    private float sizeHorizontal = 0.5f;
    private String textureName = null;

    public Button(String textureName, double horizontalPos, double verticalPos, double sizeHorizontal, ButtonHandler handler) {
        this.textureName = textureName;
        this.pos = new Vector(horizontalPos, verticalPos, 0);
        this.sizeHorizontal = (float) sizeHorizontal;
        this.handler = handler;
    }

    public void draw() {
        Shader.checkGlError();
        if (mesh == null) {
            createMesh();
            meshNode = new TriangleMeshNode(mesh);
            meshNode.updateVbo();
        }
        Shader.checkGlError();
        meshNode.drawGL(INode.RenderMode.REGULAR, Matrix.createIdentityMatrix4());
        Shader.checkGlError();
    }

    public void createMesh() {
        float sizeVertical = sizeHorizontal * Camera.getInstance().getAspectRatio();
        mesh = new TriangleMesh();
        mesh.addVertex(pos);
        mesh.addVertex(pos.add(new Vector(sizeHorizontal, 0, 0)));
        mesh.addVertex(pos.add(new Vector(sizeHorizontal, sizeVertical, 0)));
        mesh.addVertex(pos.add(new Vector(0, sizeVertical, 0)));

        mesh.addTextureCoordinate(new Vector(0, 1));
        mesh.addTextureCoordinate(new Vector(1, 1));
        mesh.addTextureCoordinate(new Vector(1, 0));
        mesh.addTextureCoordinate(new Vector(0, 0));

        mesh.addTriangle(0, 1, 2, 0, 1, 2);
        mesh.addTriangle(0, 2, 3, 0, 2, 3);

        mesh.setTextureName(textureName);
    }

    public boolean wasTouched(double buttonX, double buttonY) {
        float sizeVertical = sizeHorizontal * Camera.getInstance().getAspectRatio();
        return buttonX >= pos.x() && buttonX <= pos.x() + sizeHorizontal &&
                buttonY >= pos.y() && buttonY <= pos.y() + sizeVertical;
    }

    public void handleTouch() {
        handler.handle();
    }

    public void invalidate() {
        mesh = null;
        meshNode = null;
    }
}
