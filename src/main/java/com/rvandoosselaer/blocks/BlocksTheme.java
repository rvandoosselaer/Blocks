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
@ToString(onlyExplicitlyIncluded = true)
public class BlocksTheme {

    @ToString.Include
    private String name;
    @ToString.Include
    private String path;
    private String description;
    private String author;
    private String version;

}
