package edu.hawhamburg.app.opengl;

public class AnimationConfig {
    public static boolean animate = true;
    public static String skinning = "lbs";
    public static String mode = "slerp";

    public static void startStopAnimation(){
        animate=!animate;
    }

    public static void toggleSkinning(){
        if(skinning.equalsIgnoreCase("lbs")){
            skinning="dqs";
        } else {
            skinning="lbs";
        }
    }

    public static void toggleMode(){
        if(skinning.equalsIgnoreCase("slerp")){
            skinning="lerp";
        } else {
            skinning="slerp";
        }
    }
}
