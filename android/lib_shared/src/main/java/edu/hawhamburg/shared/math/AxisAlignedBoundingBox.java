package edu.hawhamburg.shared.math;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an axis-aligned bounding box with lower left and upper right corner.
 *
 * @author Philipp Jenke
 */

public class AxisAlignedBoundingBox {
    /**
     * Lower left corner.
     */
    private Vector ll = null;

    /**
     * Upper right corner
     */
    private Vector ur = null;

    public AxisAlignedBoundingBox() {
    }

    public AxisAlignedBoundingBox(AxisAlignedBoundingBox bbox) {
        this(bbox.getLL(), bbox.getUR());
    }

    public AxisAlignedBoundingBox(Vector ll, Vector ur) {
        if (ll != null) {
            this.ll = new Vector(ll);
        }
        if (ur != null) {
            this.ur = new Vector(ur);
        }
    }

    public Vector getLL() {
        return ll;
    }

    public Vector getUR() {
        return ur;
    }

    public void add(Vector point) {
        if (ll == null) {
            ll = new Vector(point);
        }
        if (ur == null) {
            ur = new Vector(point);
        }
        for (int i = 0; i < 3; i++) {
            if (point.get(i) < ll.get(i)) {
                ll.set(i, point.get(i));
            }
            if (point.get(i) > ur.get(i)) {
                ur.set(i, point.get(i));
            }
        }
    }

    public Vector getExtend() {
        return ur.subtract(ll);
    }

    public Vector getCenter() {
        return ll.add(ur).multiply(0.5);
    }

    public void transform(Matrix transformation) {
        List<Vector> transformedPoints = new ArrayList<>();
        transformedPoints.add(transformation.multiply(new Vector(ll.x(), ll.y(), ll.z(), 1)).xyz());
        transformedPoints.add(transformation.multiply(new Vector(ll.x(), ur.y(), ll.z(), 1)).xyz());
        transformedPoints.add(transformation.multiply(new Vector(ur.x(), ur.y(), ll.z(), 1)).xyz());
        transformedPoints.add(transformation.multiply(new Vector(ur.x(), ll.y(), ll.z(), 1)).xyz());

        transformedPoints.add(transformation.multiply(new Vector(ll.x(), ll.y(), ur.z(), 1)).xyz());
        transformedPoints.add(transformation.multiply(new Vector(ll.x(), ur.y(), ur.z(), 1)).xyz());
        transformedPoints.add(transformation.multiply(new Vector(ur.x(), ur.y(), ur.z(), 1)).xyz());
        transformedPoints.add(transformation.multiply(new Vector(ur.x(), ll.y(), ur.z(), 1)).xyz());

        ll = new Vector(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        ur = new Vector(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
        for (Vector v : transformedPoints) {
            for (int i = 0; i < 3; i++) {
                ll.set(i, Math.min(ll.get(i), v.get(i)));
                ur.set(i, Math.max(ur.get(i), v.get(i)));
            }
        }
    }

    public AxisAlignedBoundingBox clone() {
        return new AxisAlignedBoundingBox(ll, ur);
    }

    /**
     * Unity with second bbox.
     */
    public void add(AxisAlignedBoundingBox boundingBox) {
        add(boundingBox.getLL());
        add(boundingBox.getUR());
    }
}
