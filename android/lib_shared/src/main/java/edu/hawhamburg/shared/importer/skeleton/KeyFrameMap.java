package edu.hawhamburg.shared.importer.skeleton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import edu.hawhamburg.shared.math.Matrix;

public class KeyFrameMap {
    private Map<Double, Matrix> keyFrame;

    public KeyFrameMap() {
        this.keyFrame = new HashMap<>();
    }

    public KeyFrameMap(Map<Double,Matrix> keyFrame) {
        this.keyFrame = keyFrame;
    }

    public void addKeyFrame(Double d, Matrix m){
        keyFrame.put(d,m);
    }

    public Matrix getKeyFrameAnimation(Double d){
        //todo logik rein dass richtiges zurückgegeben wird und irgendwann auslagern
        return keyFrame.get(d);
    }

    private List<Double> sortList(List<Double> list){
        List<Double> dList = new ArrayList<>();
        while(list.size()>0){

        }
        return null;
    }

    public Matrix getKeyFrameAnimationForTimeT(double f, String mode){

        List<Double> keyTime = new ArrayList<>();

        //todo nochmal genau ansehen
        //keyTime.addAll(keyFrame.keySet().stream().sorted((d1,d2)->d1.compareTo(d2)).collect(Collectors.toCollection(ArrayList::new)));
        Set<Double> keyFrameSet = keyFrame.keySet();
        keyTime.addAll(keyFrameSet);
        //keyTime.

        //todo magic numbers hier raus bzw. auf 0...1 normieren
        double key1=0;
        double key2=1;
        for(int i=0;i<keyTime.size();i++){
            if(keyTime.get(i)<=f&&keyTime.get(i)>=key1){
                key1=keyTime.get(i);
            }
            if(keyTime.get(i)>=f&&keyTime.get(i)<=key2){
                key2=keyTime.get(i);
            }
        }

        if(key1==key2){
            return getKeyFrameAnimation(key1);
        }

        Matrix m1 = getKeyFrameAnimation(key1);
        Matrix m2 = getKeyFrameAnimation(key2);
        double numerator = f-key1;
        double denominator = key2-key1;

        double progression = (numerator/denominator);
        Matrix animatedMatrix = AnimationHelper.linearInterpolate4x4Matrices(m1,m2,progression,mode);

        return animatedMatrix;
    }

}
