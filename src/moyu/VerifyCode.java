package moyu;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URL;

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

      /*  BufferedImage image = ImageIO.read(new File("data/hard/src.png"));
        image = new ImgProcess(image).clip(2,2,image.getWidth()-4,image.getHeight()-4);
        ImageIO.write(image,"png",new FileOutputStream("data/src.png"));
        ImageIO.write(new ImgProcess(image).average().getImage(),"png",new FileOutputStream("data/hard/average.png"));
        ImageIO.write(new ImgProcess(image).average().binary().getImage(),"png",new FileOutputStream("data/hard/averagebin.png"));
        ImageIO.write(new ImgProcess(image).binary().average().getImage(),"png",new FileOutputStream("data/hard/binaverage.png"));
        ImageIO.write(new ImgProcess(image).binary().middle().getImage(),"png",new FileOutputStream("data/hard/binmiddle.png"));
        ImageIO.write(new ImgProcess(image).middle().binary().getImage(),"png",new FileOutputStream("data/hard/middlebin.png"));
        ImageIO.write(new ImgProcess(image).middle().getImage(),"png",new FileOutputStream("data/hard/middle.png"));

        BufferedImage images[] = new ImgProcess(image).binary().spiltMock();
        for(int i = 0;i<images.length;i++) {
            ImageIO.write(images[i], "png", new FileOutputStream("data/hard/spiltMock"+i+".png"));
        }
        images = new ImgProcess(image).binary().spilt(10,17);
        for(int i = 0;i<images.length;i++) {
            ImageIO.write(images[i], "png", new FileOutputStream("data/hard/spiltMock"+i+".png"));
        }*/
        BufferedImage image = ImageIO.read(new URL("http://njnumk.moyuyc.xyz/ranNumber"));
        image = new ImgProcess(image).clip(2,2,image.getWidth()-4,image.getHeight()-4);
//        ImageIO.write(new ImgProcess(image).corrodeTwo().getImage(), "png", new FileOutputStream("data/hard/corrode2.png"));
//        ImageIO.write(new ImgProcess(image).corrodeEight().getImage(), "png", new FileOutputStream("data/hard/corrode8.png"));
//        ImageIO.write(new ImgProcess(image).expendTwo().getImage(), "png", new FileOutputStream("data/hard/expend2.png"));
//        ImageIO.write(new ImgProcess(image).expendEight().getImage(), "png", new FileOutputStream("data/hard/expend8.png"));
        ImageIO.write(image, "png", new FileOutputStream("data/hard/src.png"));
        image = new ImgProcess(image).middle().binary().zhangThin().getImage();
        ImageIO.write(image, "png", new FileOutputStream("data/hard/thin.png"));
        BufferedImage[]images = new ImgProcess(image).binary().spiltMock();
        for(int i = 0;i<images.length;i++) {
            ImageIO.write(images[i], "png", new FileOutputStream("data/hard/spilt"+i+".png"));
        }




        /*
        BufferedImage image5 = ImageIO.read(new FileInputStream("data/srcBin.png"));
        ImageIO.write(new ImgProcess(image5).expend(), "png", new File("data/srcBinExpend.png"));
        ImageIO.write(new ImgProcess(image5).corrode(), "png", new File("data/srcBinCorrode.png"));
        ImageIO.write(new ImgProcess(image5).open(), "png", new File("data/srcBinOpen.png"));
        ImageIO.write(new ImgProcess(image5).close(), "png", new File("data/srcBinClose.png"));
        */
    }
}
