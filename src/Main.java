import com.sun.org.apache.regexp.internal.REDebugCompiler;

import javax.rmi.CORBA.Util;
import java.awt.image.BufferedImage;

/**
 * Created by andrei on 8/22/17.
 */
public class Main {

    private static final int NOTSPECIFIED = -1;
    private static final int REDUCE = 1;
    private static final int ERRORDIFFUSE = 2;
    private static final int CONTRAST = 3;

    public static void main(String[] args) throws Exception {
        System.out.println("alive!");

        String loadPath;
        String imageFormat;
        String imageName;
        boolean reduce = false;
        int contrast = -1;
        int command;


        if (args.length == 0) {
            loadPath = "./image.jpg";
            imageFormat = "jpg";
            imageName = "reduced";
            command = REDUCE;
        } else if (args.length >= 3) {
            loadPath = args[0];
            imageFormat = args[1];
            imageName = args[2];

            switch (args[3]) {
                case "-reduce":
                    command = REDUCE;
                    break;
                case "-contrast":
                    command = CONTRAST;
                    contrast = Integer.valueOf(args[4]).intValue();
                    break;
                case "-ediffuse":
                    command = ERRORDIFFUSE;
                    break;
                default:
                    command = NOTSPECIFIED;
            }
        } else {
            throw new Exception();
        }

        BufferedImage image = Utils.loadImage(loadPath);
        ImageManipulator imgMan = new ImageManipulator(image);
        BufferedImage newImage;

        switch (command) {
            case NOTSPECIFIED:
                System.out.println("No command was specified!");
                return;
            case REDUCE:
                newImage = imgMan.reduceImage();
                break;
            case ERRORDIFFUSE:
                newImage = imgMan.diffuseError();
                break;
            case CONTRAST:
                newImage = imgMan.changeContrast(contrast);
                break;
            default:
                newImage = null;
        }

        Utils.saveImage(newImage, imageFormat, imageName + "." + imageFormat);
    }
}
