package com.rvandoosselaer.blocks;

import com.jme3.math.Vector2f;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TextureCoordinates {

    protected final Vector2f min;
    protected final Vector2f max;

    public TextureCoordinates(float minX, float maxX, float minY, float maxY) {
        this(new Vector2f(minX, minY), new Vector2f(maxX, maxY));
    }

}
