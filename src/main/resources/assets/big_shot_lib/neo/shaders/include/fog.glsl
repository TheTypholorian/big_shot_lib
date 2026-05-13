#include "minecraft:fog"

//? if <1.21.6 {
/*uniform float FogStart;
uniform float FogEnd;
uniform int FogShape;
uniform vec4 FogColor;

vec4 applyFog(vec4 inColor, vec3 pos) {
    return linear_fog(inColor, fog_distance(pos, FogShape), FogStart, FogEnd, FogColor);
}

float fogFade(vec3 pos) {
    return linear_fog_fade(fog_distance(pos, FogShape), FogStart, FogEnd);
}
*///? } else {
vec4 applyFog(vec4 inColor, vec3 pos) {
    return apply_fog(inColor, fog_spherical_distance(pos), fog_cylindrical_distance(pos), FogEnvironmentalStart, FogEnvironmentalEnd, FogRenderDistanceStart, FogRenderDistanceEnd, FogColor);
}

float fogFade(vec3 pos) {
    return 1 - total_fog_value(fog_spherical_distance(pos), fog_cylindrical_distance(pos), FogEnvironmentalStart, FogEnvironmentalEnd, FogRenderDistanceStart, FogRenderDistanceEnd);
}
//? }