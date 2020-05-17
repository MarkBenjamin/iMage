package org.iMage.mosaique.rectangle;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.iMage.mosaique.base.BufferedArtImage;
import org.iMage.mosaique.base.IMosaiqueArtist;

/**
 * This class represents an {@link IMosaiqueArtist} who uses rectangles as tiles.
 *
 * @author Dominik Fuchss
 */
public class RectangleArtist implements IMosaiqueArtist<BufferedArtImage> {
    private List<BufferedArtImage> tiles = new ArrayList<>();
    private int tileWidth;
    private int tileHeight;

    /**
     * Create an artist who works with {@link RectangleShape RectangleShapes}
     *
     * @param images     the images for the tiles
     * @param tileWidth  the desired width of the tiles
     * @param tileHeight the desired height of the tiles
     * @throws IllegalArgumentException iff tileWidth or tileHeight &lt;= 0, or images is empty.
     */
    public RectangleArtist(Collection<BufferedArtImage> images, int tileWidth, int tileHeight) {
        this.tiles.addAll(images);
        this.tileHeight = tileHeight;
        this.tileWidth = tileWidth;

    }

    @Override
    public List<BufferedImage> getThumbnails() {
        throw new RuntimeException("not implemented");
    }

    @Override
    public BufferedArtImage getTileForRegion(BufferedArtImage region) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public int getTileWidth() {
        throw new RuntimeException("not implemented");
    }

    @Override
    public int getTileHeight() {
        throw new RuntimeException("not implemented");
    }
}
