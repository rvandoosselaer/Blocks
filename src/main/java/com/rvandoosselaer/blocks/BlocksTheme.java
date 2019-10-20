package com.rvandoosselaer.blocks;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author rvandoosselaer
 */
@Getter
@Setter
@Builder
@ToString
public class BlocksTheme {

    /**
     * The name of the theme
     */
    private String name;

    /**
     * The path to the texture folder, relative from a directory the assetmanager can acces.
     */
    private String path;

}
