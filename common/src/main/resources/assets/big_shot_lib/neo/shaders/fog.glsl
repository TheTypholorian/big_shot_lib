#include "minecraft:fog.glsl"

uniform float FogStart;
uniform float FogEnd;
uniform int FogShape;
uniform vec4 FogColor;

vec4 bigShotFog(vec4 inColor, vec3 delta) {
    return linear_fog(inColor, fog_distance(delta, FogShape), FogStart, FogEnd, FogColor);
}