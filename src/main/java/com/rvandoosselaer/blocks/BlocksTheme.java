package com.rvandoosselaer.blocks;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * A Blocks theme.
 *
 * @author rvandoosselaer
 */
@Getter
@Setter
@Builder
@ToString
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
