#version 150

uniform float Width;
uniform float Height;
uniform float X;
uniform float Y;

in vec3 Position;
in vec2 UV;

out vec2 uv;

void main() {
    gl_Position = vec4(Position * vec3(Width, Height, 0) + vec3(X, Y, 0), 1.0);
    uv = UV;
}
