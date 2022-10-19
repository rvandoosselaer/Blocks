package com.rvandoosselaer.blocks;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A Blocks theme.
 *
 * @author rvandoosselaer
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlocksTheme {

    /**
     * The name of the theme
     */
    private String name;

    /**
     * The path to the texture folder of the theme, starting from an AssetManager accessible folder.
     */
    private String path;

}
