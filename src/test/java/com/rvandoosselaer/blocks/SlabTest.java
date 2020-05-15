package com.rvandoosselaer.blocks;

import com.rvandoosselaer.blocks.shapes.Slab;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

//TODO: extend
public class SlabTest {

    @Test
    public void testValues() {
        Slab slab = new Slab(0, 1);
        assertEquals(0, slab.getStartY());
        assertEquals(1, slab.getEndY());

        slab = new Slab(-0.2f, 0.9f);
        assertEquals(0, slab.getStartY());
        assertEquals(0.9f, slab.getEndY());

        slab = new Slab(-0.2f, 1.3f);
        assertEquals(0, slab.getStartY());
        assertEquals(1, slab.getEndY());

        slab = new Slab(0.5f, 0.2f);
        assertEquals(0.2f, slab.getStartY());
        assertEquals(0.2f, slab.getEndY());
    }

}
