#version 330

uniform sampler2D InSampler;

in vec2 texCoord;
out vec4 fragColor;

void main() {
    vec2 center = vec2(0.5, 0.5);
    vec2 direction = texCoord - center;

    vec4 color = vec4(0.0);
    int samples = 8;

    for (int i = 0; i < samples; i++) {
        float scale = 1.0 - 0.12 * (float(i) / float(samples));
        vec2 offset = center + direction * scale;
        color += texture(InSampler, clamp(offset, 0.0, 1.0));
    }

    fragColor = color / float(samples);
}
