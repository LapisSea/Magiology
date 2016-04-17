#version 120

uniform sampler2D DiffuseSampler;

varying vec2 texCoord;
varying vec2 oneTexel;

uniform vec2 InSize;

uniform float tolerance;
uniform float intensity;

uniform vec3 color;

void main(){
    vec4 normalColor=texture2D(DiffuseSampler, texCoord);
    
    float difference=max(abs(normalColor.x-color.x),max(abs(normalColor.y-color.y),abs(normalColor.z-color.z)))-tolerance;
    if(difference>0){
        float off=1-difference/intensity;
        gl_FragColor=normalColor*off;
    }else{
        gl_FragColor=normalColor;
    }
}
