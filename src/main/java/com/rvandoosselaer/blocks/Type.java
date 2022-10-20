package com.rvandoosselaer.blocks;

import com.jme3.material.Material;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.function.Function;

@Data
@RequiredArgsConstructor
public class Type {

    private final String name;
    private final Material material;
    private final Function<Direction, TextureCoordinates> textureCoordinatesFunction;

    public Type(String name, Material material) {
        this(name, material, direction -> new TextureCoordinates(0, 1, 0, 1));
    }

}
