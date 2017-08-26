import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

import static javax.swing.JFrame.EXIT_ON_CLOSE;

/**
 * Created by andrei on 7/20/17.
 */
public class Utils {

    public static int getBlue(int rgb) {
        return rgb & 0xff;
    }
    public static int getGreen(int rgb) {
        return (rgb >> 8) & 0xff;
    }
    public static int getRed(int rgb) {
        return (rgb >> 16) & 0xff;
    }
    public static int getAlpha(int rgb) {
        return (rgb >> 24) & 0xff;
    }

    public static BufferedImage imageToGreyscale(BufferedImage image) {
        BufferedImage imageGrey = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());


        for (int i = 0; i < imageGrey.getHeight(); i++) {
            for (int j = 0; j < imageGrey.getWidth(); j++) {
                int rgb = image.getRGB(j, i);
                rgb = greyify(rgb);
                imageGrey.setRGB(j, i, rgb);
            }
        }

        return imageGrey;
    }
    public static void saveImage(RenderedImage image, String format, String fileName) {
        saveImage(image, format, new File(fileName));
    }

    public static void initSwingFrame(JFrame frame) {
        frame.setSize(1000, 1000);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        //frame.pack();
        //frame.getContentPane().add(label);
        frame.setVisible(true);
    }
    public static JLabel addImage(JFrame frame, BufferedImage image) {

        JLabel label = new JLabel(new ImageIcon(image));
        frame.getContentPane().add(label);
        frame.pack();
        return label;
    }
    public static BufferedImage loadImage(String filepath) {
        try {
            return ImageIO.read(new File(filepath));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    //pre: red, green, blue <= 255 and >= 0
    public static int coloursToRGB(int red, int green, int blue) {
        return (red << 16) + (green << 8) + blue;
    }

    /* HELPER METHODS */

    private static void saveImage(RenderedImage image, String format, File outputfile) {
        try {
            ImageIO.write(image, format, outputfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int greyify(int rgb) {
        int blue = rgb & 0xff;
        int green = rgb & 0xff00;
        int red = rgb & 0xff0000;
        int alpha = rgb & 0xf000000;

        double newColourD = rgbToGrey(blue, green >> 8, red >> 16);

        int newColour = (int) newColourD;
        int newRgb = newColour + (newColour << 8) + (newColour << 16) + (alpha << 24);
        return newRgb;
    }
    private static double rgbToGrey(int blue, int green, int red) {
        //return red * 0.2126 + green * 0.7152 + red * 0.0722;
        return (red + green + blue) / 3;
    }

    //PRE: the image is already in greyscale
    public static int getGreyscale(BufferedImage image, int x, int y) {

        return image.getRGB(x, y) & 0xff;
    }

    public static BufferedImage cloneImage(BufferedImage image) {
        BufferedImage clone = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        java.awt.Graphics g = clone.getGraphics();
        g.drawImage(image, 0, 0, null);
        return clone;
    }

    public static int getRGB(int red, int green, int blue) {
        return Utils.getRGB(red, green, blue, 0);
    }

    public static int getRGB(int red, int green, int blue, int alpha) {
        int rgb = blue;
        rgb = rgb | (green << 8);
        rgb = rgb | (red << 16);
        rgb = rgb | (alpha << 24);
        return rgb;
    }
}
