package com.rvandoosselaer.blocks.serialize;

import com.rvandoosselaer.blocks.BlockIds;
import com.rvandoosselaer.blocks.ShapeIds;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A POJO for deserializing a block from file.
 *
 * @author: rvandoosselaer
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BlockDTO {

    private String name;
    private String type;
    private String shape = ShapeIds.CUBE;
    private boolean solid = true;
    private boolean transparent = false;
    private boolean multiTexture = false;

    public String getName() {
        if (name == null) {
            return BlockIds.getName(type, shape);
        }
        return name;
    }

}
