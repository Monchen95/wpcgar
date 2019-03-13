varying vec3 N; // Normal vector
varying vec3 p; // Surface point
varying vec4 color; // Surface color
varying vec2 texture_coordinate; // Texture coordinate

attribute vec3 inVertex;
attribute vec3 inNormal;
attribute vec4 inColor;
attribute vec2 inTexCoords;

uniform mat4 modelMatrix;
uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;

/**
 * Vertex shader: Phong lighting model, Phong shading.
 */
void main(void)
{
    mat4 viewModelMatrix = viewMatrix * modelMatrix;
	vec4 p4 = modelMatrix * vec4(inVertex.x, inVertex.y, inVertex.z, 1) ; 
	p4 = p4 / p4.w;
    p  = p4.xyz;
    N = normalize((viewModelMatrix * vec4( inNormal.x, inNormal.y, inNormal.z, 0.0)).xyz);
    color = inColor;
    texture_coordinate = inTexCoords;
    gl_PointSize = 10.0;
    gl_Position = projectionMatrix * ( viewModelMatrix * vec4(inVertex.x, inVertex.y, inVertex.z, 1));
}