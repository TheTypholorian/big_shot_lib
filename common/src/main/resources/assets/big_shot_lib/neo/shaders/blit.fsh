#version 430

uniform sampler2D Sampler0;

in vec2 uv;

out vec4 fragColor;

void main() {
    fragColor = vec4(1, 0.5, 0.25, 1);//texture(Sampler0, uv);
}
