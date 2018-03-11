package edu.hawhamburg.shared.math;

/**
 * Created by Devran on 10.03.2018.
 */

public class Comparison {
    public static boolean LTLT(double a, double b, double c){
        if(a<=b && b<=c){
            return true;
        }
        return false;
    }

    public static boolean GTGT(double a, double b, double c){
        if(a>=b && b>=c){
            return true;
        }
        return false;
    }
}
