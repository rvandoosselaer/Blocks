package com.rvandoosselaer.blocks;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.VertexBuffer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: rvandoosselaer
 */
public class TestHelper {

    public static final float precision = 1000f;

    public static List<Vector3f> getPositions(VertexBuffer positionBuffer) {
        List<Vector3f> positions = new ArrayList<>();
        for (int i = 0; i < positionBuffer.getNumElements(); i++) {
            Vector3f v = new Vector3f();
            for (int j = 0; j < positionBuffer.getNumComponents(); j++) {
                float value = (Float) positionBuffer.getElementComponent(i, j);
                switch (j) {
                    case 0:
                        v.setX(Math.round(value * precision) / precision);
                        break;
                    case 1:
                        v.setY(Math.round(value * precision) / precision);
                        break;
                    case 2:
                        v.setZ(Math.round(value * precision) / precision);
                        break;
                }
            }
            positions.add(v);
        }
        return positions;
    }

    public static List<Vector2f> getTexCoords(VertexBuffer uvBuffer) {
        List<Vector2f> texCoords = new ArrayList<>();
        for (int i = 0; i < uvBuffer.getNumElements(); i++) {
            Vector2f v = new Vector2f();
            for (int j = 0; j < uvBuffer.getNumComponents(); j++) {
                float value = (Float) uvBuffer.getElementComponent(i, j);
                switch (j) {
                    case 0:
                        v.setX(Math.round(value * precision) / precision);
                        break;
                    case 1:
                        v.setY(Math.round(value * precision) / precision);
                        break;
                }
            }
            texCoords.add(v);
        }
        return texCoords;
    }

}
