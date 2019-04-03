package edu.hawhamburg.shared.importer.skeleton;

public class AnimationConfig {
    public static boolean animate = true;
    public static String skinning = "lbs";
    public static String mode = "slerp";
    public static double speed = 0.05;
    private static double delta = 0.05;

    public static synchronized boolean getAnimate(){
        return animate;
    }

    public static void speedUp(){
        if(speed==0.5){
            return;
        }
        speed += delta;
    }
    public static void slowDown(){
        if(speed==0.0){
            return;
        }
        speed -= delta;
    }

    public static synchronized void startStopAnimation(){
        animate=!animate;
    }

    public static void toggleSkinning(){
        System.out.println("now: "+skinning);
        if(skinning.equalsIgnoreCase("lbs")){
            skinning="dqs";
        } else {
            skinning="lbs";
        }
    }

    public static void toggleMode(){
        if(mode.equalsIgnoreCase("slerp")){
            mode="lerp";
        } else {
            mode="slerp";
        }
    }
}
