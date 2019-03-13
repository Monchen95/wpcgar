package edu.hawhamburg.shared.importer.skeleton;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VertexWeightController {
    List<Integer> jointsThatAffectsVertex;
    List<Float> weightThatAffectsVertex;
    Map<Integer,Float> jointWeightMap;

    public VertexWeightController(List<Integer> jointThatAffectsVertex, List<Float> weightThatAffectsVertex) {
        this.jointsThatAffectsVertex = jointThatAffectsVertex;
        this.weightThatAffectsVertex = weightThatAffectsVertex;
        this.jointWeightMap = new HashMap<>();
    }

    public void limitWeight(int limit){
        if(jointWeightMap.size()>limit){

        }
    }

    public List<Integer> getJointsThatAffectsVertex() {
        return jointsThatAffectsVertex;
    }

    public List<Float> getWeightThatAffectsVertex() {
        return weightThatAffectsVertex;
    }

    @Override
    public String toString() {
        return "VertexWeightController{" +
                "jointsThatAffectsVertex=" + jointsThatAffectsVertex.size() +
                ", weightThatAffectsVertex=" + weightThatAffectsVertex.size() +
                '}'+"\n";
    }
}
