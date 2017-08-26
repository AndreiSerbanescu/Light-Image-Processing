import java.awt.image.BufferedImage;
import java.io.BufferedReader;

/**
 * Created by andrei on 8/22/17.
 */
public class ImageManipulator {

    private BufferedImage image;
    private int[] standardColours;


    public ImageManipulator(BufferedImage image) {
        this.image = image;
        setStandardColours();
    }

    private void setStandardColours() {
        int black = 0x0;
        int red = 0xff0000;
        int green = 0xff00;
        int yellow = 0xffff00;
        int blue = 0xff;
        int magenta = 0xff00ff;
        int cyan = 0x00ffff;
        int white = 0xffffff;
        int orange = 0xffa500;
        int grey = 0xd3d3d3;
        int beige = 0xf5f5dc;

        int mediumSpringGreen = 0xfa9a;
        //int darkSlateGray = 0x2f4f4f;
        int navy = 0x80;
        int indigo = 0x4b0082;
        int cornflowerBlue = 0x6495ed;


        standardColours = new int[]{red, white, black, blue, green, yellow, magenta, cyan};

    }

    public BufferedImage getHalfTone() {
        return getHalfTone(image.getWidth() / 100, 5, 0x0);
    }

    public BufferedImage getHalfTone(int distance, int radius, int colour) {
        BufferedImage newImage = Utils.cloneImage(image);

        for (int i = 0; i < newImage.getHeight(); i += distance) {
            for (int j = 0; j < newImage.getWidth(); j += distance) {

                colourDot(j, i, radius, colour, newImage);
            }
        }

        return null;
    }

    private void colourDot(int x, int y, int radius, int colour, BufferedImage newImage) {
        int startx = x - radius;
        int endx = x + radius;
        int starty = y - radius;
        int endy = y + radius;

        for (int i = startx; i <= endx; i++) {
            for (int j = starty; j <= endy; j++) {
                if (isInCircle(x, y, i, j, radius)) {

                }
            }
        }
    }
    private boolean isInCircle(int centreX, int centreY, int currX, int currY, int radius) {
        if (currX > centreX + radius) {
            return false;
        }
        if (currX < centreX - radius) {
            return false;
        }
        if (currY > centreY + radius) {
            return false;
        }
        if (currY < centreY - radius) {
            return false;
        }
        return true;
    }

    public BufferedImage image() {
        return image;
    }

    public BufferedImage reduceImage() {
        return reduceImage(standardColours);
    }

    public BufferedImage reduceImage(int... colours) {
        BufferedImage reducedImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        int actualColour;
        int nearestColour;


        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                actualColour = image.getRGB(j, i);
                nearestColour = findNearestColour(actualColour, colours);
                reducedImage.setRGB(j, i, nearestColour);
            }
        }

        return reducedImage;
    }

    public BufferedImage changeContrast(int contrast) {
        assert contrast < 256 && contrast > -256:
                "contrast should be between -255 and 255";

        double factor = calculateFactor(contrast);
        BufferedImage newImage = Utils.cloneImage(image);


        for (int i = 0; i < newImage.getHeight(); i++) {
            for (int j = 0; j < newImage.getWidth(); j++) {
                int colour = image.getRGB(j, i);
                int newRed = contrastNewColour(factor, Utils.getRed(colour));
                int newGreen = contrastNewColour(factor, Utils.getGreen(colour));
                int newBlue = contrastNewColour(factor, Utils.getBlue(colour));

                newImage.setRGB(j, i, Utils.getRGB(newRed, newGreen, newBlue));
            }
        }

        return newImage;
    }

    private int contrastNewColour(double factor, int primaryColour) {
        assert primaryColour >= 0 && primaryColour <= 255:
                "primary colour must be between 0 and 255";

        //System.out.println(primaryColour);
        return normalise((int)Math.floor(factor * (primaryColour - 128) + 128));
    }

    private int normalise(int rgb) {
        if (rgb < 0) {
            return 0;
        } else if (rgb > 255) {
            return 255;
        }
        return rgb;
    }

    private double calculateFactor(int contrast) {
        return (259.0d * (contrast + 255)) / (255.0d * (259 - contrast));
    }

    public BufferedImage diffuseError() {
        return diffuseError(image);
    }

    private BufferedImage diffuseError(BufferedImage givenImage) {
        BufferedImage newImage = Utils.cloneImage(givenImage);

        for (int i = 0; i < newImage.getHeight() - 1; i++) {
            for (int j = 1; j < newImage.getWidth() - 1; j++) {
                int currColour = givenImage.getRGB(j, i);
                int nearestColour = findNearestColour(currColour, standardColours);
                int error = currColour - nearestColour;

                int newPixel1 = (int) Math.floor(givenImage.getRGB(j + 1, i) + 7 / 16 * error);
                int newPixel2 = (int) Math.floor(givenImage.getRGB(j - 1, i + 1) + 3 / 16 * error);
                int newPixel3 = (int) Math.floor(givenImage.getRGB(j, i + 1) + 5 / 16 * error);
                int newPixel4 = (int) Math.floor(givenImage.getRGB(j + 1, i + 1) + 1 / 16 * error);

                newImage.setRGB(j + 1, i, newPixel1);
                newImage.setRGB(j - 1, i + 1, newPixel2);
                newImage.setRGB(j, i + 1, newPixel3);
                newImage.setRGB(j + 1, i + 1, newPixel4);
            }
        }
        return newImage;
    }

    private int findNearestColour(int colour, int[] colours) {
        int minDistance = 255 * 255 * 3 + 1;
        int actualRed = Utils.getRed(colour);
        int actualGreen = Utils.getGreen(colour);
        int actualBlue = Utils.getBlue(colour);
        int nearestColour = -1;


        for (int i = 0; i < colours.length; i++) {
            int currColour = colours[i];
            int diffBlue = actualBlue - Utils.getBlue(currColour);
            int diffGreen = actualGreen - Utils.getGreen(currColour);
            int diffRed = actualRed - Utils.getRed(currColour);

            int distance = diffBlue * diffBlue + diffRed * diffRed + diffGreen * diffGreen;

            if (distance < minDistance) {
                minDistance = distance;
                nearestColour = currColour;
            }
        }

        return nearestColour;
    }
}
