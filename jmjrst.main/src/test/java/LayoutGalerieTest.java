//package org.jis.generator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;

import org.jis.generator.LayoutGalerie;


import org.junit.Before;
import org.junit.Test;

public class LayoutGalerieTest {
    private LayoutGalerie galerieUnderTest;

    private File fromFile;
    private File toFile;
    private String randomString;

    /**
     * Run before each test class. Initialize every test class.
     */
    @Before
    public void setUp() throws URISyntaxException {
        galerieUnderTest = new LayoutGalerie(null, null);

        try {
            final File resourceFolder = new File(this.getClass().getResource(File.separator).toURI());
            fromFile = new File(resourceFolder, "from");
            toFile = new File(resourceFolder, "to");

            byte[] array = new byte[10];
            new Random().nextBytes(array);
            randomString = new String(array);
        } catch (URISyntaxException e) {
            fail();
        }
    }

    /**
     * Test method for {@link org.jis.generator.LayoutGalerie#copyFile(File, File)}.
     */
    @Test
    public final void testCopyFile() throws IOException {

        try {
            fromFile.createNewFile();
            Path fromPath = FileSystems.getDefault().getPath(fromFile.getPath());
            Files.writeString(fromPath, randomString);

            galerieUnderTest.copyFile(fromFile, toFile);

            assertTrue(toFile.exists());

            Path toPath = FileSystems.getDefault().getPath(toFile.getPath());
            String contents = Files.readString(toPath);

            assertEquals(randomString, contents);
        } catch (IOException e) {
            fail();
        }

    }

}
