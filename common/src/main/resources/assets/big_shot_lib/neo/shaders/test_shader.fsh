#version 430

uniform float Time;
uniform sampler2D TestSampler;

in vec2 uv;

out vec4 fragColor;

void main() {
    fragColor = texture(TestSampler, uv);
}
