package moyu;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

/**
 * Created by Moyu on 16/7/24.
 */
public class ImgUtil {
    public static boolean isWhite(int argb) {
        return (argb&0x00ffffff) == 0xffffff;
    }
    public static boolean isBlack(int argb) {
        return (argb&0x00ffffff) == 0;
    }
    public static int[] IntARGB2Array(int rgba) {
        return new int[]{rgba>>24,(rgba>>16)&0xff,(rgba>>8)&0xff,rgba&0xff};
    }
    public static int Array2IntARGB(int[] rgba) {
        return (rgba[0]<<24)|(rgba[1]<<16)|(rgba[2]<<8)|rgba[3];
    }

    public static void copyTo(BufferedImage image,BufferedImage dest) {
        for(int j = 0;j < image.getHeight();j++)
            for (int i = 0; i < image.getWidth(); i++) {
                dest.setRGB(i,j,image.getRGB(i, j));
            }
    }
    public static void main(String[] args) {
//        System.out.println(Arrays.toString(IntRGBA2Array(-1)));
    }
}
