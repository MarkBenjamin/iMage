package org.iMage.mosaique.rectangle;

import org.iMage.mosaique.base.BufferedArtImage;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import javax.imageio.*;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class RectangleArtistTest {

    private RectangleArtist rectangleArtist;
    private static final File TEST_DIR = new File("target/test");
    private static final String tichy_in = "/tichy_in.jpg";

    private static final String[] testTileNames = new String[]{"/0002.jpg", "/0003.jpg", "/0004.jpg", "/0005.jpg", "/0006.jpg", "/0007.jpg", "/0008.jpg"};
    private String imageName;
    private List<BufferedArtImage> testTiles = new ArrayList<>();


    /**
     * Input for test cases
     */
    private BufferedImage testImage;
    /**
     * Metadata for saving the image
     */
    private IIOMetadata imeta;
    /**
     * output from test cases
     */
    private BufferedImage imageTestResult;

    @BeforeClass
    public static void beforeClass() {
        if (TEST_DIR.exists()) {
            for (File f : TEST_DIR.listFiles()) {
                f.delete();
            }
        } else {
            TEST_DIR.mkdirs();
        }
    }

    @Before
    public void setUp() {

        //read all images in the folder
        for (String str : testTileNames) {
            final URL imageResource = this.getClass().getResource(str);
            try (ImageInputStream iis = ImageIO.createImageInputStream(imageResource.openStream())) {
                ImageReader reader = ImageIO.getImageReadersByFormatName("jpg").next();
                reader.setInput(iis, true);
                ImageReadParam params = reader.getDefaultReadParam();
                this.testTiles.add(new BufferedArtImage(reader.read(0, params)));
                //this.imeta = reader.getImageMetadata(0);
                reader.dispose();
            } catch (IOException e) {
                fail(e.getMessage());
            }
        }


        this.rectangleArtist = new RectangleArtist(testTiles, 10, 10);
        this.testImage = null;
        this.imeta = null;
        this.imageTestResult = null;

        final URL imageResource = this.getClass().getResource(tichy_in);
        imageName = extractFileNameWithoutExtension(new File(imageResource.getFile()));


    }

    private String extractFileNameWithoutExtension(File file) {
        String fileName = file.getName();
        if (fileName.indexOf(".") > 0) {
            return fileName.substring(0, fileName.lastIndexOf("."));
        } else {
            return fileName;
        }
    }

    /**
     * Automatisches Speichern von testImage.
     */
    @After
    public void tearDown() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd_HH.mm.ss.SSS");
        String time = sdf.format(new Date());

        File outputFile = new File(
                MessageFormat.format("{0}/{1}_tested_{2}.jpg", TEST_DIR, imageName, time));

        if (this.imageTestResult != null) {
            try (FileOutputStream fos = new FileOutputStream(outputFile);
                 ImageOutputStream ios = ImageIO.createImageOutputStream(fos)) {
                ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();
                writer.setOutput(ios);

                ImageWriteParam iwparam = new JPEGImageWriteParam(Locale.getDefault());
                iwparam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT); // mode explicit necessary

                // set JPEG Quality
                iwparam.setCompressionQuality(1f);
                writer.write(this.imeta, new IIOImage(this.imageTestResult, null, null), iwparam);
                writer.dispose();
            } catch (IOException e) {
                fail();
            }
        }
    }


    @Test
    public void testTilesAdd() {
        assertEquals(7, testTiles.size());
    }

    @Test(expected = NullPointerException.class)
    public void testTileForRegionNull() {
        rectangleArtist.getTileForRegion(null);
    }

    @Test
    public void testTileForRegionRight() {
        BufferedArtImage ret = rectangleArtist.getTileForRegion(testTiles.get(1));
        int width = rectangleArtist.getTileWidth();
        int heigth = rectangleArtist.getTileHeight();

        for (int i = 0; i < heigth; i++) {
            for (int j = 0; j < width; j++) {
                assertEquals(ret.getRGB(i, j), testTiles.get(1).getRGB(i, j));
            }
        }

    }

    @Test
    public void testTileForRegionWrong() {
        BufferedArtImage ret = rectangleArtist.getTileForRegion(testTiles.get(0));
        int width = rectangleArtist.getTileWidth();
        int heigth = rectangleArtist.getTileHeight();

        for (int i = 0; i < heigth; i++) {
            for (int j = 0; j < width; j++) {
                assertNotEquals(ret.getRGB(i, j), testTiles.get(1).getRGB(i, j));
            }
        }

    }


}
