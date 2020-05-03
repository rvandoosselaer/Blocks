#import "Common/ShaderLib/GLSLCompat.glsllib"
#import "Common/ShaderLib/MultiSample.glsllib"

uniform float g_Time;
uniform vec4 m_FadeColor;
uniform float m_FadeDepth;
uniform float m_ShorelineSize;
uniform vec4 m_ShorelineColor;
uniform sampler2D m_Texture;
uniform vec2 g_FrustumNearFar;
uniform sampler2D m_WaterDepthTexture;
uniform sampler2D m_SceneDepthTexture;
uniform float m_DistortionStrengthX;
uniform float m_DistortionStrengthY;
uniform float m_DistortionAmplitudeX;
uniform float m_DistortionAmplitudeY;
uniform float m_DistortionSpeed;

varying vec2 texCoord;

// an overlay function. Put the foreground color above the background color.
vec4 layer(vec4 foreground, vec4 background) {
    return foreground * foreground.a + background * (1.0 - foreground.a);
}

void main(){
    vec4 scene = getColor(m_Texture, texCoord);
    float depth = getDepth(m_SceneDepthTexture, texCoord).r;
    float waterDepth = getDepth(m_WaterDepthTexture, texCoord).r;

    // when distortion is enabled, we simulate the underwater effect. We should also apply this effect to the depth
    // textures, and not only on the scene texture!
    #ifdef DISTORTION
        float X = texCoord.x * m_DistortionAmplitudeX + g_Time * m_DistortionSpeed;
        float Y = texCoord.y * m_DistortionAmplitudeY + g_Time * m_DistortionSpeed;
        vec2 texCoordDistorted = vec2(texCoord);
        texCoordDistorted.x += sin(X - Y) * m_DistortionStrengthX * sin(Y);
        texCoordDistorted.y += cos(X + Y) * m_DistortionStrengthY * cos(Y);

        depth = getDepth(m_SceneDepthTexture, texCoordDistorted).r;
        waterDepth = getDepth(m_WaterDepthTexture, texCoordDistorted).r;
    #endif

    // logic from DepthOfField.frag:
    // z_buffer_value = a + b / z;
    //
    // Where:
    //  a = zFar / ( zFar - zNear )
    //  b = zFar * zNear / ( zNear - zFar )
    //  z = distance from the eye to the object
    //
    // Which means:
    // zb - a = b / z;
    // z * (zb - a) = b
    // z = b / (zb - a)
    //
    float a = g_FrustumNearFar.y / (g_FrustumNearFar.y - g_FrustumNearFar.x);
    float b = g_FrustumNearFar.y * g_FrustumNearFar.x / (g_FrustumNearFar.x - g_FrustumNearFar.y);
    float distanceScene = b / (depth - a);
    float distanceWater = b / (waterDepth - a);

    float depthDifference = distanceScene - distanceWater;
    float depthMixFactor = clamp(depthDifference / m_FadeDepth, 0.0, 1.0);
    // depthMixFactor = 0 == surface
    // depthMixFactor = 1 == bottom (m_FadeDepth)

    if (distanceWater < distanceScene) {

        // when distortion is enabled, we simulate the underwater effect. We displace the scene texture
        #ifdef DISTORTION
            float X = texCoord.x * m_DistortionAmplitudeX + g_Time * m_DistortionSpeed;
            float Y = texCoord.y * m_DistortionAmplitudeY + g_Time * m_DistortionSpeed;
            vec2 texCoordDistorted = vec2(texCoord);
            texCoordDistorted.x += sin(X - Y) * m_DistortionStrengthX * sin(Y);
            texCoordDistorted.y += cos(X + Y) * m_DistortionStrengthY * cos(Y);

            scene = getColor(m_Texture, texCoordDistorted);
        #endif

        // calculate the depth difference between the water and the scene (aka shore). When this is small, we are at
        // the shoreline.
        //float shore = abs(distanceWater - distanceScene);
        if (depthDifference >= 0.0 && depthDifference <= m_ShorelineSize) {
            float shorelineMixFactor = clamp(depthDifference / m_ShorelineSize, 0.0, 1.0);
            // 0 = Shore start
            // 1 = Shore end / deep water
            // blend the shoreline color with the scene
            vec4 color = mix(m_ShorelineColor, scene, shorelineMixFactor);
            gl_FragColor = layer(color, scene);
        } else {
            // blend the scene with the fade color in the depth
            vec4 color = mix(scene, m_FadeColor, depthMixFactor);
            gl_FragColor = layer(color, scene);
        }
    } else {
        gl_FragColor = scene;
    }

    //
    // DEBUG
    //

    // render depthMixFactor
    //gl_FragColor = vec4(depthMixFactor, depthMixFactor, depthMixFactor, 1.0);

    // render depth texture
    // the value is inverted to make it more obvious
    //gl_FragColor = vec4(1.0 - depth, 1.0 - depth, 1.0 - depth, 1.0);

    // render the water depth texture
    // the value is inverted to make it more obvious
    //gl_FragColor = vec4(1.0 - waterDepth, 1.0 - waterDepth, 1.0 - waterDepth, 1.0);

    // render the scene
    //gl_FragColor = scene;
}