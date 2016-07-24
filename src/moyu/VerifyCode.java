package moyu;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * Created by Moyu on 16/7/24.
 */
public class VerifyCode {
    public enum SRC_TYPE {
        URL, FILEPATH
    }

    private BufferedImage bufImage;
    public VerifyCode (String path, SRC_TYPE type) throws Exception {
        switch (type) {
            case URL: {
                this.bufImage = ImageIO.read(new URL(path));
                break;
            }
            case FILEPATH: {
                this.bufImage = ImageIO.read(new FileInputStream(path));
                break;
            }
            default: throw new Exception("Argument Error");
        }
    }



    public static void main(String[] args) throws Exception {
        // get Source Image
//
//        String type = args[0].substring(args[0].lastIndexOf('.'));
//        VerifyCode code = new VerifyCode(args[0],SRC_TYPE.FILEPATH);
//        code.bufImage = new ImgProcess(code.bufImage).clip(2,2,code.bufImage.getWidth()-4,code.bufImage.getHeight()-4);
//        if(args.length>1)
//            ImageIO.write(code.bufImage, type, new File(args[1]));
//        for (BufferedImage img : new ImgProcess(new ImgProcess(code.bufImage).binary()).spilt(10,16)) {
//            System.out.print(Robot.recognize(img));
//        }


        // gray
        BufferedImage imageG = ImageIO.read(new FileInputStream("data/1.png"));
        ImageIO.write(new ImgProcess(imageG).gray(), "png", new File("data/gray.png"));

/*
        // reduce noise
        BufferedImage imageR = ImageIO.read(new FileInputStream("data/src.png"));
        ImageIO.write((RenderedImage) new ImgProcess(imageR).reduceNoise2(0, 1.0, 2.1), "png", new File("data/reduceSrc.png"));

        imageR = ImageIO.read(new FileInputStream("data/gray.png"));
        ImageIO.write((RenderedImage) new ImgProcess(imageR).reduceNoise2(0, 1.0, 2.1), "png", new File("data/reduceGray.png"));
*/


        // binary
        BufferedImage image = ImageIO.read(new FileInputStream("data/1.png"));
        ImageIO.write(new ImgProcess(image).binary(), "png", new File("data/srcBin.png"));
//        BufferedImage image2 = ImageIO.read(new FileInputStream("data/reduceSrc.png"));
//        ImageIO.write(new ImgProcess(image2).binary(), "png", new File("data/reduceSrcBin.png"));
        BufferedImage image3 = ImageIO.read(new FileInputStream("data/gray.png"));
        ImageIO.write(new ImgProcess(image3).binary(), "png", new File("data/grayBin.png"));
//        BufferedImage image4 = ImageIO.read(new FileInputStream("data/reduceGray.png"));
//        ImageIO.write(new ImgProcess(image4).binary(), "png", new File("data/reduceGrayBin.png"));

        BufferedImage image5 = ImageIO.read(new FileInputStream("data/srcBin.png"));
        BufferedImage[] images = new ImgProcess(image5).spilt(10 ,16);
        for(int i=0;i<images.length;i++)
            ImageIO.write(images[i], "png", new File("data/srcBinSpilt"+(i+1)+".png"));

        BinImage bin1 = new BinImage(ImageIO.read(new FileInputStream("data/srcBinSpilt1.png")));
        BinImage bin2 = new BinImage(ImageIO.read(new FileInputStream("data/srcBinSpilt2.png")));
        System.out.println(bin1.toString());
        System.out.println(bin2.toString());
        System.out.println(bin1.similarPercentage(ImageIO.read(new FileInputStream("data/srcBinSpilt2.png"))));

        /*
        BufferedImage image5 = ImageIO.read(new FileInputStream("data/srcBin.png"));
        ImageIO.write(new ImgProcess(image5).expend(), "png", new File("data/srcBinExpend.png"));
        ImageIO.write(new ImgProcess(image5).corrode(), "png", new File("data/srcBinCorrode.png"));
        ImageIO.write(new ImgProcess(image5).open(), "png", new File("data/srcBinOpen.png"));
        ImageIO.write(new ImgProcess(image5).close(), "png", new File("data/srcBinClose.png"));
        */
    }
}
