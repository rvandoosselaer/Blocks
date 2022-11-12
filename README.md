# ![Blocks](icon-64.png) Blocks
Blocks is a voxel engine created for [jMonkeyEngine](https://jmonkeyengine.org).

[![Build Status](https://app.travis-ci.com/rvandoosselaer/Blocks.svg?branch=master)](https://app.travis-ci.com/rvandoosselaer/Blocks) [![Codacy Badge](https://api.codacy.com/project/badge/Grade/9ee72fb21dc847dc98b80e1908dd0298)](https://www.codacy.com/manual/rvandoosselaer/Blocks?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=rvandoosselaer/Blocks&amp;utm_campaign=Badge_Grade)

![](https://i.imgur.com/B3v3s3U.jpg)
![](https://i.imgur.com/SIVpZJa.jpg)
![](https://i.imgur.com/vjqOTR5.jpg)
![](https://i.imgur.com/F2EK6lz.jpg)
![](https://i.imgur.com/3d7dpvg.gif)

## Features

### Multiple shapes

Next to the default cube shape, Blocks also supports more complicated shapes like pyramids, rounded blocks, wedges, slabs, stairs, ...
This will make your creations feel even more vibrant!

### Theming and PBR support

Don't like the look and feel of the blocks? You can easily change the default textures by creating and using a different theme. You can even supply your own custom jMonkeyEngine material files. Even physically based rendering (or PBR) of blocks is supported by default.

### Endless terrain

Blocks comes with a fully fledged pager implementation that will load the blocks around the player, blocks that are out of sight will be unloaded to save resources.

### Physics support

Add the generated collision meshes to the physics space of your application to create and simulate solid environments. Blocks uses the [Minie](https://jmonkeystore.com/38308161-c3cf-4e23-8754-528ca8387c11) physics library for real-time physics simulation.

### Optimized for performance
 
Using multithreading, high demanding tasks like chunk generation and mesh creation can be run in parallel to maximize the CPU time.

### Designed for customization

Blocks is build with modularity and reuse in mind. If a part of the framework doesn't match your requirements you can easily swap it out and replace it with something else.

### Built using jMonkeyEngine best practices

The lifecycle of the Blocks framework is managed by AppState objects. This way the Blocks framework can easily and safely be plugged into the StateManager of jMonkeyEngine.

## Documentation
General documentation can be found on the [wiki](https://github.com/rvandoosselaer/Blocks/wiki). The javadoc of the latest stable version can be found at the [gh-pages.](https://rvandoosselaer.github.io/Blocks/1.7.1/javadoc/)
Documentation can only take you so far, so make sure to take a look at the different [examples](https://github.com/rvandoosselaer/Blocks/tree/master/examples/src/main/java/com/rvandoosselaer/blocks/examples).

## Getting help
Make sure to check out the [FAQ](https://github.com/rvandoosselaer/Blocks/wiki/FAQ) page on the wiki. There is also a [Blocks topic](https://hub.jmonkeyengine.org/t/blocks/42465) on the jMonkeyEngine forum. Or just create your own topic.

## Contributing
If you have a bug or an idea, you can create a ticket for it [here.](https://github.com/rvandoosselaer/Blocks/issues)
See the [CONTRIBUTING](CONTRIBUTING.md) file for more details.

## License
This project is licensed under the BSD 3-Clause License - see the [LICENSE](LICENSE) file for details

## Acknowledgements
-   Block textures made by [Soartex](https://soartex.net) from the Soartex Fanver.
-   Block textures made by [Faithful](https://www.curseforge.com/minecraft/texture-packs/faithful-32x).
-   jMonkeyEngine [forum](https://hub.jmonkeyengine.org/) community. Special thanks to ALi_RS, jayfella, sgold and pspeed for their contributions.
