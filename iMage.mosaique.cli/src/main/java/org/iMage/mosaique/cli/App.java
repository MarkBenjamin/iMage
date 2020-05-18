package org.iMage.mosaique.cli;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.awt.image.BufferedImage;
import java.awt.image.ImagingOpException;
import java.awt.image.RenderedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.iMage.mosaique.MosaiqueEasel;
import org.iMage.mosaique.base.BufferedArtImage;
import org.iMage.mosaique.base.IMosaiqueArtist;
import org.iMage.mosaique.rectangle.RectangleArtist;

import javax.imageio.ImageIO;

/**
 * This class parses all command line parameters and creates a mosaique.
 */
public final class App {
    private App() {
        throw new IllegalAccessError();
    }

    private static final String CMD_OPTION_INPUT_IMAGE = "i";
    private static final String CMD_OPTION_INPUT_TILES_DIR = "t";
    private static final String CMD_OPTION_OUTPUT_IMAGE = "o";

    private static final String CMD_OPTION_TILE_W = "w";
    private static final String CMD_OPTION_TILE_H = "h";

    public static void main(String[] args) {
        // Don't touch...
        CommandLine cmd = null;
        try {
            cmd = App.doCommandLineParsing(args);
        } catch (ParseException e) {
            System.err.println("Wrong command line arguments given: " + e.getMessage());
            System.exit(1);
        }
        // ...this!

        /**
         * Implement me! Remove exception when done!
         *
         * HINT: You have to convert the files from the image folder to Objects of class
         * org.iMage.mosaique.base.BufferedArtImage before you can use Mosaique.
         */

        //input image
        File[] input = null;
        try {
            input = App.processInputFiles(cmd.getOptionValue(App.CMD_OPTION_INPUT_IMAGE));
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        BufferedArtImage inputImage = App.toBufferedArtImage(input)[0];

        //tiles Dir
        File[] tiles = null;
        try {
            tiles = App.processInputFiles(cmd.getOptionValue(App.CMD_OPTION_INPUT_TILES_DIR));
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        if (tiles.length < 10) {
            System.err.println("Too few tile images!");
            System.exit(1);
        }


        //width
        int width = cmd.hasOption(App.CMD_OPTION_TILE_W)
                ? Integer.parseInt(cmd.getOptionValue(App.CMD_OPTION_TILE_W))
                : 0;
        if (width < 0 || width > inputImage.getWidth()) {
            System.err.println("Width is invalid: " + width);
            System.exit(1);
        } else if (width != inputImage.getWidth() / 10) {
            System.err.println("Width should be 10% of input image");
        }
        //height
        int height = cmd.hasOption(App.CMD_OPTION_TILE_H)
                ? Integer.parseInt(cmd.getOptionValue(App.CMD_OPTION_TILE_H))
                : 0;
        if (height < 0 || height > inputImage.getHeight()) {
            System.err.println("Height is invalid: " + width);
            System.exit(1);
        } else if (width != inputImage.getWidth() / 10) {
            System.err.println("Height should be 10% of input image");
        }


        RectangleArtist rectangleArtist = new RectangleArtist(Arrays.asList(App.toBufferedArtImage(tiles)), width, height);
        MosaiqueEasel mosaiqueEasel = new MosaiqueEasel();
        BufferedImage mosaique = mosaiqueEasel.createMosaique(inputImage.toBufferedImage(), rectangleArtist);
        if (mosaique == null) {
            System.err.println("Some error occured while creating mosaique image");
            System.exit(1);
        }

        //output
        File output = null;
        try {
            output = App.ensureFile(cmd.getOptionValue(App.CMD_OPTION_OUTPUT_IMAGE), true);
            ImageIO.write((RenderedImage) mosaique, "png", output);
        } catch (IOException e) {
            System.err.println("Could not save image:" + e.getMessage());
            System.exit(1);
        }


    }

    private static File ensureFile(String path, boolean create) throws IOException, IOException {
        File file = new File(path);
        if (file.exists()) {
            return file;
        }
        if (create) {
            file.createNewFile();
            return file;
        }
        // File not available
        throw new IOException("The specified file does not exist: " + path);
    }

    private static File[] processInputFiles(String dir) throws IOException {
        File directory = App.ensureFile(dir, false);
        List<File> jpgs = new ArrayList<>();
        for (File file : directory.listFiles(f -> f.getName().endsWith(".jpg")))
            jpgs.add(file);
        if (jpgs.size() % 2 == 0 || jpgs.size() <= 1) {
            System.err.println("Found " + jpgs.size() + " files. This isn't an odd value.");
            System.exit(1);
        }
        File[] result = jpgs.toArray(File[]::new);
        for (File image : result) {
            String name = image.getName();
            name = name.substring(0, name.length() - ".jpg".length());
            if (name.length() < 3 || !result[0].getName().startsWith(name.substring(0, 3))) {
                System.err.println("Naming violation: " + image.getName() + " & "
                        + result[0].getName());
                System.exit(1);
            }
        }
        return result;
    }

    private static BufferedArtImage[] toBufferedArtImage(File[] input) {
        BufferedArtImage[] result = new BufferedArtImage[input.length];
        for (int i = 0; i < result.length; i++) {
            try {
                InputStream inputStream = new FileInputStream(input[i]);
                BufferedImage bufferedImage = ImageIO.read(inputStream);
                result[i] = new BufferedArtImage(bufferedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * Parse and check command line arguments
     *
     * @param args command line arguments given by the user
     * @return CommandLine object encapsulating all options
     * @throws ParseException if wrong command line parameters or arguments are given
     */
    private static CommandLine doCommandLineParsing(String[] args) throws ParseException {
        Options options = new Options();
        Option opt;

        /*
         * Define command line options and arguments
         */
        opt = new Option(App.CMD_OPTION_INPUT_IMAGE, "input-images", true, "path to input image");
        opt.setRequired(true);
        opt.setType(String.class);
        options.addOption(opt);

        opt = new Option(App.CMD_OPTION_INPUT_TILES_DIR, "tiles-dir", true, "path to tiles directory");
        opt.setRequired(true);
        opt.setType(String.class);
        options.addOption(opt);

        opt = new Option(App.CMD_OPTION_OUTPUT_IMAGE, "image-output", true, "path to output image");
        opt.setRequired(true);
        opt.setType(String.class);
        options.addOption(opt);

        opt = new Option(App.CMD_OPTION_TILE_W, "tile-width", true, "the width of a tile");
        opt.setRequired(false);
        opt.setType(Integer.class);
        options.addOption(opt);

        opt = new Option(App.CMD_OPTION_TILE_H, "tile-height", true, "the height of a tile");
        opt.setRequired(false);
        opt.setType(Integer.class);
        options.addOption(opt);

        CommandLineParser parser = new DefaultParser();
        return parser.parse(options, args);
    }

}
