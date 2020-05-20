#import "Common/ShaderLib/GLSLCompat.glsllib"
#import "Common/ShaderLib/Instancing.glsllib"
#import "Common/ShaderLib/Skinning.glsllib"
#import "Common/ShaderLib/MorphAnim.glsllib"

//
// waves and animated texture / sprite settings
//
uniform float g_Time;
uniform float m_WaveSpeed;
uniform float m_WaveHeight;
uniform float m_WaveSize;
uniform float m_TextureScrollSpeedX;
uniform float m_TextureScrollSpeedY;
varying vec2 texCoordAni;
// end

uniform vec4 m_BaseColor;
uniform vec4 g_AmbientLightColor;
varying vec2 texCoord;

#ifdef SEPARATE_TEXCOORD
  varying vec2 texCoord2;
  attribute vec2 inTexCoord2;
#endif

varying vec4 Color;

attribute vec3 inPosition;
attribute vec2 inTexCoord;
attribute vec3 inNormal;

#ifdef VERTEX_COLOR
  attribute vec4 inColor;
#endif

varying vec3 wNormal;
varying vec3 wPosition;
#if defined(NORMALMAP) || defined(PARALLAXMAP)
    attribute vec4 inTangent;
    varying vec4 wTangent;
#endif

void main(){
    vec4 modelSpacePos = vec4(inPosition, 1.0);

    //
    // adapt vertices position to simulate waves
    //
    modelSpacePos.y += cos(modelSpacePos.z * m_WaveSize + (m_WaveSpeed * g_Time)) * m_WaveHeight * sin(modelSpacePos.x * m_WaveSize + (m_WaveSpeed * g_Time));
    //

    //
    // animated texture settings
    //
    float speedX = g_Time * m_TextureScrollSpeedX;
    float speedY = g_Time * m_TextureScrollSpeedY;
    texCoordAni.x = inTexCoord.x + speedX;
    texCoordAni.y = inTexCoord.y + speedY;
    //

    vec3 modelSpaceNorm = inNormal;

    #if  ( defined(NORMALMAP) || defined(PARALLAXMAP)) && !defined(VERTEX_LIGHTING)
         vec3 modelSpaceTan  = inTangent.xyz;
    #endif

    #ifdef NUM_MORPH_TARGETS
         #if defined(NORMALMAP) && !defined(VERTEX_LIGHTING)
            Morph_Compute(modelSpacePos, modelSpaceNorm, modelSpaceTan);
         #else
            Morph_Compute(modelSpacePos, modelSpaceNorm);
         #endif
    #endif

    #ifdef NUM_BONES
         #if defined(NORMALMAP) && !defined(VERTEX_LIGHTING)
            Skinning_Compute(modelSpacePos, modelSpaceNorm, modelSpaceTan);
         #else
            Skinning_Compute(modelSpacePos, modelSpaceNorm);
         #endif
    #endif

    gl_Position = TransformWorldViewProjection(modelSpacePos);
    texCoord = inTexCoord;
    #ifdef SEPARATE_TEXCOORD
       texCoord2 = inTexCoord2;
    #endif

    wPosition = TransformWorld(modelSpacePos).xyz;
    wNormal  = TransformWorldNormal(modelSpaceNorm);

    #if defined(NORMALMAP) || defined(PARALLAXMAP)
      wTangent = vec4(TransformWorldNormal(modelSpaceTan),inTangent.w);
    #endif

    Color = m_BaseColor;

    #ifdef VERTEX_COLOR
        Color *= inColor;
    #endif
}
