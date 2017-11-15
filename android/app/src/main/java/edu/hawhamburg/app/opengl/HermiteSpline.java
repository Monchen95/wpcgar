package edu.hawhamburg.app.opengl;

import java.util.ArrayList;
import java.util.List;

import edu.hawhamburg.shared.math.Vector;

/**
 * Created by abq892 on 15.11.2017.
 */

public class HermiteSpline {
    
    private List<Vector> controlPoints;
    private List<Vector> tangents;

    private Vector p0;
    private Vector p1;
    private Vector m0;
    private Vector m1;
    
    public HermiteSpline(List<Vector> controlPoints, List<Vector> tangents){
        this.controlPoints = controlPoints;
        this.tangents = tangents;
    }
    
    public Vector evaluateCurve(double splineT){
        double t = CurrentParams(splineT);
        
        double h0 = (1-t)*(1-t)*(1+2*t);
        double h1 = t*(1-t)*(1-t);
        double h2 = -t*t*(1-t);
        double h3 = (3-2*t)*t*t;
        
        return p0.multiply(h0).add(m0.multiply(h1)).add(m1.multiply(h2)).add(p1.multiply(h3));
    }
    
    public Vector evaluateTangent(double splineT){
        double t = CurrentParams(splineT);

        double dh0 = 6*t*t-6*t;
        double dh1 = 3*t*t-4*t+1;
        double dh2 = 3*t*t-2*t;
        double dh3 = 6*t-6*t*t;

        return p0.multiply(dh0).add(m0.multiply(dh1)).add(m1.multiply(dh2)).add(p1.multiply(dh3));
    } 
    
    private double CurrentParams(double t){
        double deltaT = 1.0/controlPoints.size();
        int currentIdx = (int)(t/deltaT);
        double currentLocalT = (t-currentIdx*deltaT)/deltaT;
        int nextIdx = SecIndex(currentIdx);

        p0 = controlPoints.get(currentIdx);
        p1 = controlPoints.get(nextIdx);
        m0 = tangents.get(currentIdx);
        m1 = tangents.get(nextIdx);
        return currentLocalT;
    }

    private int SecIndex(int idx){
        return (idx+1)%controlPoints.size();
    }

}
