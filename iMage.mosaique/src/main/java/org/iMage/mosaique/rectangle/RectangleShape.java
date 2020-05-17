package org.iMage.mosaique.rectangle;

import java.awt.image.BufferedImage;

import org.iMage.mosaique.base.BufferedArtImage;
import org.iMage.mosaique.base.IMosaiqueShape;
import org.iMage.mosaique.base.ImageUtils;

/**
 * This class represents a rectangle as {@link IMosaiqueShape} based on an {@link BufferedArtImage}.
 *
 * @author Dominik Fuchss
 */
public class RectangleShape implements IMosaiqueShape<BufferedArtImage> {

    private BufferedImage image = null;

    /**
     * Create a new {@link IMosaiqueShape}.
     *
     * @param image the image to use
     * @param w     the width
     * @param h     the height
     */
    public BufferedImage RectangleShape(BufferedArtImage image, int w, int h) {
        this.image = ImageUtils.scaleAndCrop(image.toBufferedImage(), w, h);
        return this.image;
    }

    @Override
    public int getAverageColor() {
        int height = image.getHeight();
        int width = image.getWidth();

        int sumAlpha = 0;
        int sumRed = 0;
        int sumGreen = 0;
        int sumBlue = 0;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                sumAlpha += (image.getRGB(x, y) & 0xff000000) >> 24;
                sumRed += (image.getRGB(x, y) & 0x00ff0000) >> 16;
                sumGreen += (image.getRGB(x, y) & 0x0000ff00) >> 8;
                sumBlue += (image.getRGB(x, y) & 0x000000ff);
            }
        }

        int averageAlpha = sumAlpha / (height * width);
        int averageRed = sumRed / (height * width);
        int averageGreen = sumGreen / (height * width);
        int averageBlue = sumBlue / (height * width);

        int averageARGB = averageAlpha << 24 + averageRed << 16 + averageGreen + averageBlue;
        return averageARGB;

    }

    @Override
    public BufferedImage getThumbnail() {
        throw new RuntimeException("not implemented");
    }

    @Override
    public void drawMe(BufferedArtImage targetRect) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public int getHeight() {
        throw new RuntimeException("not implemented");
    }

    @Override
    public int getWidth() {
        throw new RuntimeException("not implemented");
    }
}