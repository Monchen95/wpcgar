package edu.hawhamburg.shared.importer.skeleton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.hawhamburg.shared.math.Matrix;
import edu.hawhamburg.shared.math.Vector;

public class SkeletonAnimationController {

    public VertexWeightController[] vertexWeightControllers;
    private List<Vector> vertexGroupList;

    //todo Liste mit einer Map<zeit, matrix> hier bauen
    //diese info aus joint herausholen
    public List<KeyFrameMap> keyFrames = new ArrayList<>();

    public void addKeyFrames(KeyFrameMap k){
        keyFrames.add(k);
    }


    //animationszeit wird im folgenden normiert zu 0...1

    public SkeletonAnimationController(int size) {
        vertexWeightControllers = new VertexWeightController[size];
        vertexGroupList = new ArrayList<>();
    }

    public SkeletonAnimationController(int size, List<Vector> vertexGroupList) {
        vertexWeightControllers = new VertexWeightController[size];
        this.vertexGroupList = vertexGroupList;
    }

    public SkeletonAnimationController(VertexWeightController[] vertexWeightControllers, List<Vector> vertexGroupList, List<KeyFrameMap> keyFrames) {
        this.vertexWeightControllers = vertexWeightControllers;
        this.vertexGroupList = vertexGroupList;
        this.keyFrames = keyFrames;
    }


    @Override
    public String toString() {
        return "SkeletonAnimationController{" +
                "vertexWeightControllers=" + Arrays.toString(vertexWeightControllers) +
                '}';
    }
}
