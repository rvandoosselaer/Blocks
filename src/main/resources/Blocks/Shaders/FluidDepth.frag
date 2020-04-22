#import "Common/ShaderLib/GLSLCompat.glsllib"
#import "Common/ShaderLib/MultiSample.glsllib"

uniform vec4 m_FadeColor;
uniform float m_FadeDepth;
uniform float m_ShorelineSize;
uniform vec4 m_ShorelineColor;
uniform sampler2D m_Texture;
uniform vec2 g_FrustumNearFar;
uniform sampler2D m_WaterDepthTexture;
uniform sampler2D m_SceneDepthTexture;

varying vec2 texCoord;

void main(){
    vec4 scene = getColor(m_Texture, texCoord);
    float depth = getDepth(m_SceneDepthTexture, texCoord).r;
    float waterDepth = getDepth(m_WaterDepthTexture, texCoord).r;

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
    float depthMixFactor = clamp(depthDifference / m_FadeDepth, 0, 1.0);
    // depthMixFactor = 0 == surface
    // depthMixFactor = 1 == bottom (m_FadeDepth)

    if (distanceWater < distanceScene) {
        // calculate the depth difference between the water and the scene (aka shore). When this is small, we are at
        // the shoreline.
        //float shore = abs(distanceWater - distanceScene);
        if (depthDifference >= 0 && depthDifference <= m_ShorelineSize) {
            float shorelineMixFactor = clamp(depthDifference / m_ShorelineSize, 0, 1.0);
            gl_FragColor = mix(m_ShorelineColor, scene, shorelineMixFactor);
        } else {
            gl_FragColor = mix(scene, m_FadeColor, depthMixFactor);
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