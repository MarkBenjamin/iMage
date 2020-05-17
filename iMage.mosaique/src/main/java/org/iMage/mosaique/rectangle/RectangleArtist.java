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
        int[] regionAverageColor = getAverageColor(region);
        double minDistance = euclideanDistance(regionAverageColor, getAverageColor(tiles.get(0)));
        BufferedArtImage targetTile = tiles.get(0);

        for (BufferedArtImage tile : this.tiles) {
            int[] tileColor = getAverageColor(tile);
            if (euclideanDistance(tileColor, regionAverageColor) < minDistance) {
                minDistance = euclideanDistance(tileColor, regionAverageColor);
                targetTile = tile;
            }
        }
        return targetTile;

    }

    @Override
    public int getTileWidth() {
        throw new RuntimeException("not implemented");
    }

    @Override
    public int getTileHeight() {
        throw new RuntimeException("not implemented");
    }

    public int[] getAverageColor(BufferedArtImage bufferedArtImage) {
        int height = bufferedArtImage.getHeight();
        int width = bufferedArtImage.getWidth();

        int sumAlpha = 0;
        int sumRed = 0;
        int sumGreen = 0;
        int sumBlue = 0;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                sumAlpha += (bufferedArtImage.getRGB(x, y) & 0xff000000) >> 24;
                sumRed += (bufferedArtImage.getRGB(x, y) & 0x00ff0000) >> 16;
                sumGreen += (bufferedArtImage.getRGB(x, y) & 0x0000ff00) >> 8;
                sumBlue += (bufferedArtImage.getRGB(x, y) & 0x000000ff);
            }
        }

        int averageAlpha = sumAlpha / (height * width);
        int averageRed = sumRed / (height * width);
        int averageGreen = sumGreen / (height * width);
        int averageBlue = sumBlue / (height * width);

        int[] averageColor = new int[4];
        averageColor[0] = averageAlpha;
        averageColor[1] = averageRed;
        averageColor[2] = averageGreen;
        averageColor[3] = averageBlue;
        return averageColor;
    }

    double euclideanDistance(int[] point1, int[] point2) {
        double distanceSquare = 0;
        for (int i = 0; i < point1.length; i++) {
            distanceSquare += (point1[i] - point2[i]) ^ 2;
        }
        return Math.sqrt(distanceSquare);
    }
}
