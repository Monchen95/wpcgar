package edu.hawhamburg.vuforia;

import android.util.Log;

import com.vuforia.Matrix44F;
import com.vuforia.State;
import com.vuforia.Tool;
import com.vuforia.Trackable;
import com.vuforia.TrackableResult;
import com.vuforia.TrackerManager;

import edu.hawhamburg.shared.math.Matrix;
import edu.hawhamburg.shared.math.Vector;
import edu.hawhamburg.shared.misc.Constants;
import edu.hawhamburg.shared.scenegraph.InnerNode;

/**
 * Represents a marker and its pose in 6DOF. The model matrix is controlled by the marker pose.
 * <p>
 * Attention: model matrix from scene graph nodes above is ignored!
 *
 * @author Philipp Jenke
 */

public class VuforiaMarkerNode extends InnerNode {

    /**
     * This matrix is used to adjust the coordinate system such that the up-vector of the marker
     * points into y-direction.
     */
    private Matrix adjustCoordinateSystem;

    /**
     * Latest transformation from the global coordinate system into the marker coordinate system.
     */
    private Matrix markerCoordinateSystem = Matrix.createIdentityMatrix4();

    /**
     * Only the marker with the given target name is handled in this node.
     */
    private final String targetName;
    private boolean active = true;

    public VuforiaMarkerNode(String targetName) {
        this.targetName = targetName;
        adjustCoordinateSystem = Matrix.createRotationMatrix4(
                new edu.hawhamburg.shared.math.Vector(-1, 0, 0), Math.PI / 2);
    }

    @Override
    public void traverse(RenderMode mode, Matrix modelMatrix) {
        if (isActive()) {
            State state = TrackerManager.getInstance().getStateUpdater().updateState();
            for (int tIdx = 0; tIdx < state.getNumTrackableResults(); tIdx++) {
                TrackableResult result = state.getTrackableResult(tIdx);
                Trackable trackable = result.getTrackable();
                if (trackable.getName().equals(targetName)) {
                    Matrix44F vuforiaViewMatrix = Tool.convertPose2GLMatrix(result.getPose());
                    float[] vuforiaViewMatrixData = vuforiaViewMatrix.getData();
                    markerCoordinateSystem = (adjustCoordinateSystem.multiply(new Matrix(
                            vuforiaViewMatrixData[0], vuforiaViewMatrixData[1], vuforiaViewMatrixData[2], vuforiaViewMatrixData[3],
                            vuforiaViewMatrixData[4], vuforiaViewMatrixData[5], vuforiaViewMatrixData[6], vuforiaViewMatrixData[7],
                            vuforiaViewMatrixData[8], vuforiaViewMatrixData[9], vuforiaViewMatrixData[10], vuforiaViewMatrixData[11],
                            vuforiaViewMatrixData[12], vuforiaViewMatrixData[13], vuforiaViewMatrixData[14], vuforiaViewMatrixData[15]))).getTransposed();
                    super.traverse(mode, markerCoordinateSystem);
                }
            }
        }
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Special case: transformations above the marker node are ignored
     */
    @Override
    public Matrix getTransformation() {
        return markerCoordinateSystem;
    }
}
