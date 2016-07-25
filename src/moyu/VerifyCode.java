package moyu;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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

        BufferedImage image = ImageIO.read(new URL("http://njnumk.moyuyc.xyz/ranNumber"));
        BufferedImage[] images = new ImgProcess(new ImgProcess(new ImgProcess(image).clip(2,2,image.getWidth()-4,image.getHeight()-4)).binary()).spiltMock();

        for (int i=0;i<images.length;i++) {
            BufferedImage im = images[i];
            ImageIO.write(im,"jpeg",new FileOutputStream("data/spilt"+i+".jpeg"));
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
