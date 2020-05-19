#import "Common/ShaderLib/GLSLCompat.glsllib"
#import "Common/ShaderLib/MultiSample.glsllib"
#import "Blocks/Shaders/BlendFunctions.glsllib"

uniform float g_Time;
uniform vec4 m_FadeColor;
uniform float m_FadeDepth;
uniform float m_ShorelineSize;
uniform vec4 m_ShorelineColor;
uniform sampler2D m_Texture;
uniform vec2 g_FrustumNearFar;
uniform sampler2D m_FluidDepthTexture;
uniform sampler2D m_SceneDepthTexture;
uniform float m_DistortionStrengthX;
uniform float m_DistortionStrengthY;
uniform float m_DistortionAmplitudeX;
uniform float m_DistortionAmplitudeY;
uniform float m_DistortionSpeed;

varying vec2 texCoord;

uniform sampler2D m_ReflectionMap;
uniform mat4 g_ViewProjectionMatrixInverse;
uniform mat4 m_TextureProjMatrix;
uniform vec3 m_CameraPosition;
uniform float m_WaterHeight;
uniform float m_ReflectionStrength;

vec3 getPosition(in float depth, in vec2 uv){
    vec4 pos = vec4(uv, depth, 1.0) * 2.0 - 1.0;
    pos = g_ViewProjectionMatrixInverse * pos;
    return pos.xyz / pos.w;
}

void main(){
    vec4 scene = getColor(m_Texture, texCoord);
    float depth = getDepth(m_SceneDepthTexture, texCoord).r;
    float waterDepth = getDepth(m_FluidDepthTexture, texCoord).r;

    vec3 position = getPosition(depth, texCoord);

    vec3 eyeVec = position - m_CameraPosition;
    vec3 eyeVecNorm = normalize(eyeVec);

    float t = (m_WaterHeight - m_CameraPosition.y) / eyeVecNorm.y;
    vec3 surfacePoint = m_CameraPosition + eyeVecNorm * t;

    vec3 waterPosition = surfacePoint.xyz;

    vec4 texCoordProj = m_TextureProjMatrix * vec4(waterPosition, 1.0);
    texCoordProj /= texCoordProj.w;
    texCoordProj.y = 1.0 - texCoordProj.y;

    vec3 reflection = getColor(m_ReflectionMap, texCoordProj.xy).rgb;

    // when distortion is enabled, we simulate the underwater effect. We should also apply this effect to the depth
    // textures, and not only on the scene texture!
    #ifdef DISTORTION
        // distortion for depth and waterDepth textures
        float X = texCoord.x * m_DistortionAmplitudeX + g_Time * m_DistortionSpeed;
        float Y = texCoord.y * m_DistortionAmplitudeY + g_Time * m_DistortionSpeed;
        vec2 texCoordDistorted = vec2(texCoord);
        texCoordDistorted.x += sin(X - Y) * m_DistortionStrengthX * sin(Y);
        texCoordDistorted.y += cos(X + Y) * m_DistortionStrengthY * cos(Y);

        depth = getDepth(m_SceneDepthTexture, texCoordDistorted).r;
        waterDepth = getDepth(m_FluidDepthTexture, texCoordDistorted).r;

        // distortion for reflection texture
        float xReflection = texCoordProj.x * m_DistortionAmplitudeX + g_Time * m_DistortionSpeed;
        float yReflection = texCoordProj.y * m_DistortionAmplitudeY + g_Time * m_DistortionSpeed;
        vec2 texCoordDistortedReflection = vec2(texCoordProj);
        texCoordDistortedReflection.x += sin(xReflection - yReflection) * m_DistortionStrengthX * sin(yReflection);
        texCoordDistortedReflection.y += cos(xReflection + yReflection) * m_DistortionStrengthY * cos(yReflection);

        reflection = getColor(m_ReflectionMap, texCoordDistortedReflection.xy).rgb;
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

    if (waterDepth > 0.01 && distanceWater < distanceScene) {
        // when distortion is enabled, we simulate the underwater effect. We displace the scene texture
        #ifdef DISTORTION
            float X = texCoord.x * m_DistortionAmplitudeX + g_Time * m_DistortionSpeed;
            float Y = texCoord.y * m_DistortionAmplitudeY + g_Time * m_DistortionSpeed;
            vec2 texCoordDistorted = vec2(texCoord);
            texCoordDistorted.x += sin(X - Y) * m_DistortionStrengthX * sin(Y);
            texCoordDistorted.y += cos(X + Y) * m_DistortionStrengthY * cos(Y);

            scene = getColor(m_Texture, texCoordDistorted);
        #endif

        scene.a += m_ReflectionStrength;
        scene = mix(scene, vec4(reflection, 1.0), m_ReflectionStrength);

        // calculate the depth difference between the water and the scene (aka shore). When this is small, we are at
        // the shoreline.
        //float shore = abs(distanceWater - distanceScene);
        if (depthDifference >= 0.0 && depthDifference <= m_ShorelineSize) {
            float shorelineMixFactor = clamp(depthDifference / m_ShorelineSize, 0.0, 1.0);
            // 0 = Shore start
            // 1 = Shore end / deep water
            // blend the shoreline color with the scene
            gl_FragColor = mix(m_ShorelineColor, scene, shorelineMixFactor);
        } else {
            #ifdef FADE
                // blend the scene with the fade color in the depth
                vec4 color = mix(scene, m_FadeColor, depthMixFactor);

                // blending the fade color to the scene
                #ifdef BLEND_OVERLAY
                    color = blend_overlay(color, scene);
                #else
                    color = blend_layer(color, scene);
                #endif

            #else
                vec4 color = scene;
            #endif

            gl_FragColor = color;
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