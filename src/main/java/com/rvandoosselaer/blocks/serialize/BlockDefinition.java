package com.rvandoosselaer.blocks.serialize;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * A POJO describing the parameters for creating a collection of blocks.
 *
 * @author: rvandoosselaer
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BlockDefinition {

    private String type;
    private final Set<String> shapes = new HashSet<>();
    private boolean solid = true;
    private boolean transparent = false;
    private boolean multiTexture = false;

    public BlockDefinition addShape(String shape) {
        if (shape != null) {
            shapes.add(shape);
        }
        return this;
    }

    public BlockDefinition addShapes(String... shapes) {
        if (shapes != null) {
            Arrays.stream(shapes).forEach(this::addShape);
        }
        return this;
    }

}
