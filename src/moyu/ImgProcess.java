package moyu;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;
import java.util.*;

/**
 * Created by Moyu on 16/7/24.
 */
public class ImgProcess {
    private BufferedImage image;
    public ImgProcess(BufferedImage img) {
        this.image = img;
    }
    public BufferedImage gray () {
        BufferedImage outImg = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        for(int j = 0;j < image.getHeight();j++) {
            for(int i = 0;i < image.getWidth();i++) {
                int[] rgba = ImgUtil.IntARGB2Array(image.getRGB(i,j));
                rgba[1] = rgba[2] = rgba[3] = (rgba[1]+rgba[2]+rgba[3]) / 3;
                outImg.setRGB(i, j, ImgUtil.Array2IntARGB(rgba));
            }
        }
        return outImg;
    }

    public BufferedImage binary () {
        BufferedImage outImg = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        for(int j = 0;j < image.getHeight();j++) {
            for(int i = 0;i < image.getWidth();i++) {
                int[] rgba = ImgUtil.IntARGB2Array(image.getRGB(i,j));
                int v = (rgba[1]+rgba[2]+rgba[3]) / 3;
                if(v >= 128) {
                    rgba[1] = rgba[2] = rgba[3] = 255;
                    outImg.setRGB(i, j, ImgUtil.Array2IntARGB(rgba));
                }else {
                    rgba[1] = rgba[2] = rgba[3] = 0;
                    outImg.setRGB(i, j, ImgUtil.Array2IntARGB(rgba));
                }

            }
        }
        return outImg;
    }

    // 膨胀算法
    public BufferedImage expendEight () {
        BufferedImage outImg = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        for(int j = 1;j < image.getHeight()-1;j++)
            for (int i = 1; i < image.getWidth()-1; i++) {
                int argb = image.getRGB(i, j);
                outImg.setRGB(i,j,argb);
                if(ImgUtil.isWhite(argb)) {
                    OUT:
                    for(int q=j-1;q<=j+1;q++){
                        for(int p=i-1;p<=i+1;p++){
                            if(ImgUtil.isBlack(image.getRGB(p,q))) {
                                outImg.setRGB(i,j,BLACK_ARGB);
                                break OUT;
                            }
                        }
                    }
                }
            }
        return outImg;
    }
    public BufferedImage expendTwo () {
        BufferedImage outImg = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        for(int j = 0;j < image.getHeight();j++)
            for (int i = 1; i < image.getWidth()-1; i++) {
                int argb = image.getRGB(i, j);
                outImg.setRGB(i,j,argb);
                if(ImgUtil.isWhite(argb)) {
                    if(ImgUtil.isBlack(image.getRGB(i-1,j))||ImgUtil.isBlack(image.getRGB(i+1,j))) {
                        outImg.setRGB(i,j,BLACK_ARGB);
                    }
                }
            }
        return outImg;
    }

    // 腐蚀算法
    public BufferedImage corrodeEight () {
        BufferedImage outImg = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        for(int j = 1;j < image.getHeight()-1;j++)
            for (int i = 1; i < image.getWidth()-1; i++) {
                int argb = image.getRGB(i, j);
                outImg.setRGB(i,j,argb);
                if(ImgUtil.isBlack(argb)) {
                    OUT:
                    for(int q=j-1;q<=j+1;q++){
                        for(int p=i-1;p<=i+1;p++){
                            if(ImgUtil.isWhite(image.getRGB(p,q))) {
                                outImg.setRGB(i,j,WHITE_ARGB);
                                break OUT;
                            }
                        }
                    }
                }
            }
        return outImg;
    }

    public BufferedImage corrodeTwo () {
        BufferedImage outImg = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        for(int j = 0;j < image.getHeight();j++)
            for (int i = 1; i < image.getWidth()-1; i++) {
                int argb = image.getRGB(i, j);
                outImg.setRGB(i,j,argb);
                if(ImgUtil.isBlack(argb)) {
                    if(ImgUtil.isWhite(image.getRGB(i-1,j))||ImgUtil.isWhite(image.getRGB(i+1,j))) {
                        outImg.setRGB(i,j,WHITE_ARGB);
                    }
                }
            }
        return outImg;
    }
    // 开运算
    public BufferedImage openEight () {
        return new ImgProcess(corrodeEight()).expendEight();
    }
    public BufferedImage openTwo () {
        return new ImgProcess(corrodeTwo()).expendTwo();
    }

    // 闭运算
    public BufferedImage closeEight () {
        return new ImgProcess(expendEight()).corrodeEight();
    }
    public BufferedImage closeTwo () {
        return new ImgProcess(expendTwo()).corrodeTwo();
    }

    public BufferedImage clip (int startx,int starty,int w,int h) {
        BufferedImage outImg = new BufferedImage(w, h, image.getType());
        for(int j = 0;j < h;j++)
            for (int i = 0; i < w; i++) {
                outImg.setRGB(i,j,image.getRGB(i+startx,j+starty));
            }
        return outImg;
    }


    public BufferedImage[] spilt (int needW, int needH) {
        LinkedList<BufferedImage> outImgs = new LinkedList<>();
        LinkedList<Integer> xs = new LinkedList<>();
        LinkedList<Integer> ys = new LinkedList<>();
        Stack<Point> stack = new Stack<>();
        HashMap<Point,Boolean> isDone = new HashMap<>();
        int w = image.getWidth(), h = image.getHeight();
        for(int j = 0;j < h;j++)
            for (int i = 0; i < w; i++) {
                int argb = image.getRGB(i, j);
                if(ImgUtil.isBlack(argb)){
                    stack.push(new Point(i,j));
                    if(!xs.isEmpty()) {
                        Integer[] sortxs = xs.toArray(new Integer[]{});
                        Integer[] sortys = ys.toArray(new Integer[]{});
                        xs.clear();
                        ys.clear();

                        int maxX = sortxs[0];
                        int minX = sortxs[0];
                        for(int x : sortxs) {
                            if(x>maxX)
                                maxX = x;
                            if(x<minX)
                                minX = x;
                        }
                        int maxY = sortys[0];
                        int minY = sortys[0];
                        for(int y : sortys) {
                            if(y>maxY)
                                maxY = y;
                            if(y<minY)
                                minY = y;
                        }
                        int x = minX,y=minY,wid=maxX-minX+1,height=maxY-minY+1;
                        x-=(needW-wid)/2;
                        y-=(needH-height)/2;
                        if(wid>5 && height>5)
                            outImgs.add(this.clip(x,y,needW,needH));
                    }
                    while (!stack.isEmpty()) {
                        Point point = stack.pop();
                        if(isDone.get(point)!=null)
                            continue;
                        isDone.put(point,true);
                        int p = point.x, q = point.y;
                        xs.add(p); ys.add(q);
                        if(q-1>=0) {
                            if(ImgUtil.isBlack(image.getRGB(p,q-1))) {
                                stack.push(new Point(p, q - 1));
                            }
                            if(p-1>=0)
                                if(ImgUtil.isBlack(image.getRGB(p-1,q-1))) {
                                    stack.push(new Point(p - 1, q - 1));
//                                    xs.add(p-1);
//                                    ys.add(q-1);
                                }
                            if(p+1<w)
                                if(ImgUtil.isBlack(image.getRGB(p+1,q-1))) {
                                    stack.push(new Point(p + 1, q - 1));
//                                    xs.add(p+1);
//                                    ys.add(q-1);
                                }
                        }
                        if(p+1<w) {
                            if(ImgUtil.isBlack(image.getRGB(p+1,q))) {
                                stack.push(new Point(p + 1, q));
//                                xs.add(p+1);
//                                ys.add(q);
                            }
                            if(q+1<h)
                                if(ImgUtil.isBlack(image.getRGB(p+1,q+1))) {
                                    stack.push(new Point(p + 1, q + 1));
//                                    xs.add(p+1);
//                                    ys.add(q+1);
                                }
                        }
                        if(q+1<h){
                            if(ImgUtil.isBlack(image.getRGB(p,q+1))) {
                                stack.push(new Point(p, q + 1));
//                                xs.add(p);
//                                ys.add(q+1);
                            }
                            if(p-1>=0)
                                if(ImgUtil.isBlack(image.getRGB(p-1,q+1))) {
                                    stack.push(new Point(p - 1, q + 1));
//                                    xs.add(p-1);
//                                    ys.add(q+1);
                                }
                        }
                        if(p-1>=0)
                            if(ImgUtil.isBlack(image.getRGB(p-1,q))) {
                                stack.push(new Point(p - 1, q));
//                                xs.add(p-1);
//                                ys.add(q);
                            }
                    }
                }
            }

        return outImgs.toArray(new BufferedImage[]{});
    }


    public final int BLACK_ARGB = -16777216;
    public final int WHITE_ARGB = -1;


    public Image reduceNoise(double fa, double fb, double fc) {
        Image sourceImage = image;
        BufferedImage bi = new BufferedImage(sourceImage.getWidth(null),
                sourceImage.getHeight(null), BufferedImage.TYPE_INT_RGB);
        Graphics2D biContext = bi.createGraphics();

        // get a BufferedImage object from an Image object
        biContext.drawImage(sourceImage, 0, 0, null);
        biContext.dispose();

        // create an array of int type to store rgb values of each pixel
        int[] rgbs = new int[bi.getWidth() * bi.getHeight()];

        bi.getRGB(0, 0, bi.getWidth(), bi.getHeight(), rgbs, 0, bi.getWidth());

        int height = bi.getHeight(), width = bi.getWidth();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                double dd;
                int d1, d2, d3;

                d1 = 2;
                if ((rgbs[i * width + j] & 0x00ffffff) == 0)
                    d1 = -2;
                d2 = 0;
                int rc[][] = { { -1, 0 }, { 0, 1 }, { 1, 0 }, { 0, -1 } };
                for (int k = 0; k < 4; k++) {
                    if (isValid(i + rc[k][0], j + rc[k][1], height, width)) {

                        if ((rgbs[(i + rc[k][0]) * width + j + rc[k][1]] & 0x00ffffff) == (rgbs[i
                                * width + j] & 0x00ffffff))
                            d2 += -2;
                        else
                            d2 += 2;
                    }
                }
                d2 *= 2;
                d3 = -2;

                dd = fa * d1 - fb * d2 - fc * d3;
                if (dd < 0)
                    rgbs[i * width + j] = (rgbs[i * width + j] & 0xff000000)
                            | (~rgbs[i * width + j] & 0x00ffffff);
            }
        }

        // create a new Image object
        Toolkit kit = Toolkit.getDefaultToolkit();
        Image image = kit.createImage(new MemoryImageSource(bi.getWidth(), bi
                .getHeight(), rgbs, 0, bi.getWidth()));

        return image;
    }

    public Image reduceNoise2(double fa, double fb, double fc) {
        BufferedImage bi = new BufferedImage(image.getWidth(null),
                image.getHeight(null), image.getType());
        int height = bi.getHeight(), width = bi.getWidth();

        //bi.setRGB(0,0,width,height,image.getRGB(0,0,width,height,null,8,1),8,1);

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                bi.setRGB(j,i,image.getRGB(j,i));
                double dd;
                int d1, d2, d3;

                d1 = 2;
                if ((image.getRGB(j, i) & 0x00ffffff) == 0)
                    d1 = -2;
                d2 = 0;
                int rc[][] = { { -1, 0 }, { 0, 1 }, { 1, 0 }, { 0, -1 } };
                for (int k = 0; k < 4; k++) {
                    if (isValid(i + rc[k][0], j + rc[k][1], height, width)) {

                        if ((image.getRGB(j + rc[k][1],i + rc[k][0]) & 0x00ffffff) == (image.getRGB(j,i) & 0x00ffffff))
                            d2 += -2;
                        else
                            d2 += 2;
                    }
                }
                d2 *= 2;
                d3 = -2;

                dd = fa * d1 - fb * d2 - fc * d3;
                if (dd < 0)
                    bi.setRGB(j,i,(image.getRGB(j,i) & 0xff000000) | (~image.getRGB(j,i) & 0x00ffffff));
            }
        }

        return bi;
    }

    private boolean isValid(int row, int col, int height, int width) {
        return (row >= 0 && row < height && col >= 0 && col < width);
    }

    // 训练样本

}

class Point {
    int x,y;
    public Point(int x,int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Point point = (Point) o;

        if (x != point.x) return false;
        return y == point.y;

    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }
}