package org.iMage.mosaique;

import java.awt.image.BufferedImage;
import java.io.IOException;

import org.iMage.mosaique.base.BufferedArtImage;
import org.iMage.mosaique.base.IMosaiqueArtist;
import org.iMage.mosaique.base.IMosaiqueEasel;
import org.iMage.mosaique.rectangle.RectangleShape;

/**
 * This class defines an {@link IMosaiqueEasel} which operates on {@link BufferedArtImage
 * BufferedArtImages}.
 *
 * @author Dominik Fuchss
 */
public class MosaiqueEasel implements IMosaiqueEasel<BufferedArtImage> {

    @Override
    public BufferedImage createMosaique(BufferedImage input,
                                        IMosaiqueArtist<BufferedArtImage> artist) {

        //1.Get the tileWidth and tileHeight from artist.
        int tileWidth = artist.getTileWidth();
        int tileHeight = artist.getTileHeight();
        int inputWidth = input.getWidth();
        int inputHeight = input.getHeight();

        //2.separate input into 2 dimension array with tileHeight and tileWidth.
        BufferedArtImage[][] inputParts =
                new BufferedArtImage[inputHeight / tileHeight + 1][inputWidth / tileWidth + 1];

        for (int i = 0; i < inputParts.length; i++) {
            for (int j = 0; j < inputParts[0].length; j++) {
                //fulfill every inputPart
                inputParts[i][j] =
                        new BufferedArtImage(input.getSubimage(j * tileWidth, i * tileHeight, tileWidth, tileHeight));
                //3.With artist.getTileForRegion(region) find the best tile image.
                BufferedArtImage tile = artist.getTileForRegion(inputParts[i][j]);
                //4.Change tile into class RectangleShape
                RectangleShape rec = new RectangleShape(tile, tile.getWidth(), tile.getHeight());
                // 5.With RectangleShape.drawMe(target) draw the target into tile.
                rec.drawMe(inputParts[i][j]);
            }
        }
        return input;
        //6.Merge the input parts, which is useless cause the subImage share the same data with input.
        //so we should return the BufferedImage input.

    }

    /**
     * Merge to BufferedImage into one
     *
     * @param img1         first image.
     * @param img2         second image.
     * @param isHorizontal merge direction.
     * @return merged image.
     * @throws IOException if any exception occurs.
     */
    public static BufferedImage mergeImage(BufferedImage img1,
                                           BufferedImage img2, boolean isHorizontal) throws IOException {
        int w1 = img1.getWidth();
        int h1 = img1.getHeight();
        int w2 = img2.getWidth();
        int h2 = img2.getHeight();

        // read RGB from image.
        int[] imageArrayOne = new int[w1 * h1];
        // scan every line of the image and add the RGB into array
        imageArrayOne = img1.getRGB(0, 0, w1, h1, imageArrayOne, 0, w1);
        int[] imageArrayTwo = new int[w2 * h2];
        imageArrayTwo = img2.getRGB(0, 0, w2, h2, imageArrayTwo, 0, w2);

        // create the new image
        BufferedImage destImage;
        // merge horizontal
        if (isHorizontal) {
            destImage = new BufferedImage(w1 + w2, h1, BufferedImage.TYPE_INT_ARGB);
            // set left part RGB of new image
            destImage.setRGB(0, 0, w1, h1, imageArrayOne, 0, w1);
            destImage.setRGB(w1, 0, w2, h2, imageArrayTwo, 0, w2);
        } else { // not horizontal
            destImage = new BufferedImage(w1, h1 + h2, BufferedImage.TYPE_INT_ARGB);
            // set upper part RGB of new image.
            destImage.setRGB(0, 0, w1, h1, imageArrayOne, 0, w1);
            destImage.setRGB(0, h1, w2, h2, imageArrayTwo, 0, w2);
        }

        return destImage;
    }

}
