uniform mat4 g_WorldViewProjectionMatrix;
uniform float g_Tpf;
uniform float g_Time;

uniform int m_numTilesX;
uniform int m_numTilesY;
uniform float m_Speed;
uniform int m_numTilesOffsetX;
uniform int m_numTilesOffsetY;
uniform float m_StartTime;
#ifdef LOCATION_OFFSET
    uniform vec3 m_locationOffset;
#endif

attribute vec3 inPosition;
attribute vec2 inTexCoord;

// calculate the correct texture coordinates and pass them to the fragment shader
varying vec2 texCoordAni;

void main(){
    vec3 position = inPosition;
    #ifdef LOCATION_OFFSET
        position = vec3(inPosition) +  m_locationOffset;
    #endif

    gl_Position = g_WorldViewProjectionMatrix * vec4(position, 1.0);

    float tileDistance = float((g_Time-m_StartTime)*m_Speed);
    int selectedTileX = int(mod(float(tileDistance), m_numTilesX))+m_numTilesOffsetX;
    int selectedTileY = (m_numTilesY-1) - (int(mod(float(tileDistance / m_numTilesX), m_numTilesY))+m_numTilesOffsetY);

    texCoordAni.x = (float(float(inTexCoord.x/m_numTilesX) + float(selectedTileX)/float(m_numTilesX)));
    texCoordAni.y = (float(float(inTexCoord.y/m_numTilesY) + float(selectedTileY)/float(m_numTilesY)));
}