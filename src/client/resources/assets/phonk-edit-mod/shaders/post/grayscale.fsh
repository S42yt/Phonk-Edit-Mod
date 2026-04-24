#version 330

uniform sampler2D InSampler;

in vec2 texCoord;
out vec4 fragColor;

void main() {
    vec4 color = texture(InSampler, texCoord);
    // ITU-R BT.709 luminance formula
    float gray = dot(color.rgb, vec3(0.299, 0.587, 0.114));
    fragColor = vec4(vec3(gray), color.a);
}
