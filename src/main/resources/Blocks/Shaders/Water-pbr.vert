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
uniform int m_numTilesX;
uniform int m_numTilesY;
uniform float m_TextureScrollSpeed;
varying vec2 texCoordAni;
uniform float m_OffsetY;
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
    // add an offset to the y-coordinate, to have a 'better' water effect. The block itself should behave like a cube,
    // it's only a visual aid. That's why it's done in the shader.
    //
    modelSpacePos.y += m_OffsetY;
    //

    //
    // animated sprite texture settings
    //
    float tileDistance = g_Time * m_TextureScrollSpeed;
    int selectedTileX = int(mod(tileDistance, m_numTilesX));
    int selectedTileY = (m_numTilesY-1) - int(mod(tileDistance / m_numTilesX, m_numTilesY));

    texCoordAni.x = (float(float(inTexCoord.x/m_numTilesX) + float(selectedTileX)/float(m_numTilesX)));
    texCoordAni.y = (float(float(inTexCoord.y/m_numTilesY) + float(selectedTileY)/float(m_numTilesY)));
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
