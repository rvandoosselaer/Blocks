package com.rvandoosselaer.blocks;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SlabTest {

    @Test
    public void testValues() {
        Slab slab = new Slab(0, 1);
        Assertions.assertEquals(0, slab.getStartY());
        Assertions.assertEquals(1, slab.getEndY());

        slab = new Slab(-0.2f, 0.9f);
        Assertions.assertEquals(0, slab.getStartY());
        Assertions.assertEquals(0.9f, slab.getEndY());

        slab = new Slab(-0.2f, 1.3f);
        Assertions.assertEquals(0, slab.getStartY());
        Assertions.assertEquals(1, slab.getEndY());

        slab = new Slab(0.5f, 0.2f);
        Assertions.assertEquals(0.2f, slab.getStartY());
        Assertions.assertEquals(0.2f, slab.getEndY());
    }

}
