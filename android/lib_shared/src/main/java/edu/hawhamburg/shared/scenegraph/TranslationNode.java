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

import edu.hawhamburg.shared.math.Matrix;
import edu.hawhamburg.shared.math.Vector;

/**
 * Translate all child nodes.
 *
 * @author Philipp Jenke
 */
public class TranslationNode extends InnerNode {

    /**
     * Translation matrix (model matrix)
     */
    private Matrix translation;

    public TranslationNode(Vector translation) {
        this.translation = Matrix.createTranslationMatrix4(translation);
    }

    public void setTranslation(Vector t) {
        this.translation = Matrix.createTranslationMatrix4(t);
    }

    public void traverse(RenderMode mode, Matrix modelMatrix) {
        super.traverse(mode, modelMatrix.multiply(translation));
    }

    public void timerTick(int counter) {
        super.timerTick(counter);
    }

    @Override
    public Matrix getTransformation() {
        return (getParentNode() == null) ? translation :
                getParentNode().getTransformation().multiply(translation
                );
    }

}
