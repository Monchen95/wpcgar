/**
 * Diese Datei gehört zum Android/Java Framework zur Veranstaltung "Computergrafik für
 * Augmented Reality" von Prof. Dr. Philipp Jenke an der Hochschule für Angewandte
 * Wissenschaften (HAW) Hamburg. Weder Teile der Software noch das Framework als Ganzes dürfen
 * ohne die Einwilligung von Philipp Jenke außerhalb von Forschungs- und Lehrprojekten an der HAW
 * Hamburg verwendet werden.
 *
 * This file is part of the Android/Java framework for the course "Computer graphics for augmented
 * reality" by Prof. Dr. Philipp Jenke at the University of Applied (UAS) Sciences Hamburg. Neither
 * parts of the framework nor the complete framework may be used outside of research or student
 * projects at the UAS Hamburg.
 *
 * This software was built upon the Vuforia sample code:
 * || Copyright (c) 2016 PTC Inc. All Rights Reserved.
 * || Copyright (c) 2012-2014 Qualcomm Connected Experiences, Inc. All Rights Reserved.
 * || Vuforia is a trademark of PTC Inc., registered in the United States and other
 * || countries.
 */
package edu.hawhamburg.vuforia;

/**
 * Shader to render the background (live stream) image.
 */
public class VideoBackgroundShader
{
    
    public static final String VB_VERTEX_SHADER =          
        "attribute vec4 vertexPosition;\n" +
        "attribute vec2 vertexTexCoord;\n" +
        "uniform mat4 projectionMatrix;\n" +
    
        "varying vec2 texCoord;\n" +
       
        "void main()\n" +
        "{\n" +
        "    gl_Position = projectionMatrix * vertexPosition;\n" +
        "    texCoord = vertexTexCoord;\n" +
        "}\n";
    
    public static final String VB_FRAGMENT_SHADER =
        "precision mediump float;\n" +
        "varying vec2 texCoord;\n" +
        "uniform sampler2D texSampler2D;\n" +
        "void main ()\n" +
        "{\n" +
        "    gl_FragColor = texture2D(texSampler2D, texCoord);\n" +
        "}\n";

}
