package com.rvandoosselaer.blocks;

import com.simsilica.mathd.Vec3i;
import lombok.Getter;

@Getter
public enum Direction {
    TOP(0, 1, 0),
    BOTTOM(0, -1, 0),
    LEFT(-1, 0, 0),
    RIGHT(1, 0, 0),
    FRONT(0, 0, 1),
    BACK(0, 0, -1);

    private final Vec3i position;

    Direction(int x, int y, int z) {
        this.position = new Vec3i(x, y, z);
    }

}
