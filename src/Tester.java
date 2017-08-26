public class Tester {


    public static void main(String[] args) {
        testGetRGB();
    }

    private static void testGetRGB() {
        int magenta = 0xff00ff;
        int cyan = 0x00ffff;
        int white = 0xffffff;
        int orange = 0xffa500;
        int grey = 0xd3d3d3;
        int beige = 0xf5f5dc;

        int[] colours = new int[]{magenta, cyan, white, orange, grey, beige};


        for (int colour : colours) {
            StringBuilder sb = new StringBuilder("test ");
            sb.append(Integer.toHexString(colour));
            sb.append(" ");
            int red = Utils.getRed(colour);
            int green = Utils.getGreen(colour);
            int blue = Utils.getBlue(colour);
            int newRgb = Utils.getRGB(red, green, blue);

            if (newRgb == colour) {
                sb.append("passed");
            } else {
                sb.append("failed");
            }
            System.out.println(sb.toString());
            System.out.println(Integer.toHexString(newRgb) + " " + Integer.toHexString(colour));
            System.out.println();
        }
    }
}
