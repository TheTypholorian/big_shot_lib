#include "minecraft:fog.glsl"

vec4 bigShotFog(vec4 inColor, vec3 delta) {
    return bigShotFog(inColor, delta, FogColor);
}

vec4 bigShotFog(vec4 inColor, vec3 delta, vec4 fogColor) {
    return apply_fog(inColor, fog_spherical_distance(delta), fog_cylindrical_distance(delta), FogEnvironmentalStart, FogEnvironmentalEnd, FogRenderDistanceStart, FogRenderDistanceEnd, fogColor);
}