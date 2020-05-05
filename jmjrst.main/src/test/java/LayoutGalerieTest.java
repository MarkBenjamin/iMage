//package org.jis.generator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;
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

    private File resourceFolder;

    public LayoutGalerieTest() {
    }

    /**
     * Run before each test class. Initialize every test class.
     */
    @Before
    public final void setUp() throws URISyntaxException {
        galerieUnderTest = new LayoutGalerie(null, null);

        resourceFolder = new File(this.getClass().getResource(File.separator).toURI());
        fromFile = new File(resourceFolder, "from");
        toFile = new File(resourceFolder, "to");

    }

    /**
     * Test method for {@link org.jis.generator.LayoutGalerie#copyFile(File, File)}.
     * Part c)
     */
    @Test
    public final void testCopyFile() {

        try {
            byte[] array = new byte[10];
            new Random().nextBytes(array);
            randomString = new String(array);

            //fromFile.createNewFile();
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

    /**
     * Test method for {@link org.jis.generator.LayoutGalerie#copyFile(File, File)}.
     * Part d)
     */
    @Test(expected = FileNotFoundException.class)
    public final void testCopyFile2() throws IOException {
        galerieUnderTest.copyFile(resourceFolder, toFile);
    }

    /**
     * Test method for {@link org.jis.generator.LayoutGalerie#copyFile(File, File)}.
     * Part d)
     */
    @Test(expected = FileNotFoundException.class)
    public final void testCopyFile3() throws IOException {
        galerieUnderTest.copyFile(fromFile, resourceFolder);
    }

    /**
     * Test method for {@link org.jis.generator.LayoutGalerie#copyFile(File, File)}
     * Part d)
     **/
    @Test(expected = FileNotFoundException.class)
    public final void testCopyFile4() throws IOException {
        fromFile = new File(resourceFolder.toURI());
        galerieUnderTest.copyFile(fromFile, toFile);
    }

    /**
     * Test method for {@link org.jis.generator.LayoutGalerie#copyFile(File, File)}.
     * Part e)
     */
    @Test
    public final void testCopyFile5() throws IOException {
        //Create a existed File named toFile.
        byte[] array = new byte[10];
        new Random().nextBytes(array);
        randomString = new String(array);

        Path toPath = FileSystems.getDefault().getPath(toFile.getPath());
        Files.writeString(toPath, randomString);

        assertTrue(toFile.exists());

        //Create a from file.
        byte[] fromFileArray = new byte[10];
        new Random().nextBytes(fromFileArray);
        randomString = new String(fromFileArray);

        Path fromPath = FileSystems.getDefault().getPath(fromFile.getPath());
        Files.writeString(fromPath, randomString);

        galerieUnderTest.copyFile(fromFile, toFile);

        //fromFile Strings and toFile strings
        Path toFilePath = FileSystems.getDefault().getPath(toFile.getPath());
        String toFileContents = Files.readString(toFilePath);

        Path fromFilePath = FileSystems.getDefault().getPath(fromFile.getPath());
        String fromFileContents = Files.readString(fromFilePath);
        //Compare two files
        assertEquals(fromFileContents,toFileContents);
    }

}