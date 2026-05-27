#version 150

uniform sampler2D Sampler0;

uniform vec4 ColorModulator;
uniform vec2 MousePos;
uniform vec2 GuiSize;
uniform vec2 ScreenSize;

uniform vec4 DarkColor;
uniform vec4 LightColor;

in vec2 texCoord0;

out vec4 fragColor;

void main() {
    vec4 color = texture(Sampler0, texCoord0);
    if (color.a == 0.0) {
        discard;
    }

    vec2 pos = gl_FragCoord.xy / ScreenSize * GuiSize;

    float scale = clamp(exp(-abs(MousePos.x - pos.x) / 50), 0, 1);
    vec4 blended = mix(DarkColor, LightColor, scale);

    fragColor = vec4(blended.rgb, blended.a * color.a) * ColorModulator;
}
