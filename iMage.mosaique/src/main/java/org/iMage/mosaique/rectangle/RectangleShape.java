package org.iMage.mosaique.rectangle;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
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

    private BufferedImage image;

    /**
     * Create a new {@link IMosaiqueShape}.
     *
     * @param image the image to use
     * @param w     the width
     * @param h     the height
     */
    public RectangleShape(BufferedArtImage image, int w, int h) {
        this.image = ImageUtils.scaleAndCrop(image.toBufferedImage(), w, h);
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

        return averageAlpha << 24 + averageRed << 16 + averageGreen << 8 + averageBlue;
    }

    @Override
    public BufferedImage getThumbnail() {
        BufferedImage res = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        Graphics2D g2d = res.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.drawRenderedImage(image, AffineTransform.getScaleInstance(1, 1));
        g2d.dispose();
        res.flush();
        return res;
    }

    @Override
    public void drawMe(BufferedArtImage targetRect) {
        int width = targetRect.getWidth();
        int height = targetRect.getHeight();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                targetRect.setRGB(x, y, image.getRGB(x, y));
            }
        }
    }

    @Override
    public int getHeight() {
        return image.getHeight();
    }

    @Override
    public int getWidth() {
        return image.getWidth();
    }
}