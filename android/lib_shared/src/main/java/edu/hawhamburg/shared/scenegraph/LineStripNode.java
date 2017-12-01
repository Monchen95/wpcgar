package edu.hawhamburg.shared.scenegraph;

import edu.hawhamburg.shared.math.AxisAlignedBoundingBox;
import edu.hawhamburg.shared.math.Matrix;

/**
 * Created by abm510 on 01.12.2017.
 */

public class LineStripNode extends LeafNode {

    public LineStripNode(){

    }

    @Override
    public void drawGL(RenderMode mode, Matrix modelMatrix) {

    }

    @Override
    public AxisAlignedBoundingBox getBoundingBox() {
        return null;
    }
}
