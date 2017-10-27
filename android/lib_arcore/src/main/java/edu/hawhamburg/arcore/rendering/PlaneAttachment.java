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
 * <p>
 * This software was built upon the Google ARCore sample code:
 * <p>
 * Copyright 2017 Google Inc. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.hawhamburg.arcore.rendering;

import com.google.ar.core.Anchor;
import com.google.ar.core.Plane;
import com.google.ar.core.Pose;

/**
 * This class tracks the attachment of object's Anchor to a Plane. It will construct a pose
 * that will stay on the plane (in Y direction), while still properly tracking the XZ changes
 * from the anchor updates.
 */
public class PlaneAttachment {
    private final Plane mPlane;
    private final Anchor mAnchor;

    // Allocate temporary storage to avoid multiple allocations per frame.
    private final float[] mPoseTranslation = new float[3];
    private final float[] mPoseRotation = new float[4];

    public PlaneAttachment(Plane plane, Anchor anchor) {
        mPlane = plane;
        mAnchor = anchor;
    }

    public boolean isTracking() {
        return /*true if*/
            mPlane.getTrackingState() == Plane.TrackingState.TRACKING &&
            mAnchor.getTrackingState() == Anchor.TrackingState.TRACKING;
    }

    public Pose getPose() {
        Pose pose = mAnchor.getPose();
        pose.getTranslation(mPoseTranslation, 0);
        pose.getRotationQuaternion(mPoseRotation, 0);
        mPoseTranslation[1] = mPlane.getCenterPose().ty();
        return new Pose(mPoseTranslation, mPoseRotation);
    }

    public Anchor getAnchor() {
        return mAnchor;
    }
}
