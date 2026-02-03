#version 430

uniform sampler2D Sampler0;

in vec2 uv;

out vec4 fragColor;

void main() {
    fragColor = texture(Sampler0, uv);
}
