package moyu;


import java.awt.image.BufferedImage;
import java.util.*;

/**
 * Created by Moyu on 16/7/24.
 */
public class ImgProcess {
    private BufferedImage image;

    public BufferedImage getImage() {return image;}

    public ImgProcess(BufferedImage img) {
        this.image = img;
    }
    public ImgProcess gray () {
        BufferedImage outImg = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        for(int j = 0;j < image.getHeight();j++) {
            for(int i = 0;i < image.getWidth();i++) {
                int[] rgba = ImgUtil.IntARGB2Array(image.getRGB(i,j));
                rgba[1] = rgba[2] = rgba[3] = (rgba[1]+rgba[2]+rgba[3]) / 3;
                outImg.setRGB(i, j, ImgUtil.Array2IntARGB(rgba));
            }
        }
        return new ImgProcess(outImg);
    }

    public ImgProcess binary () {
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
        return new ImgProcess(outImg);
    }

    // 膨胀算法
    public ImgProcess expendEight () {
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
        return new ImgProcess(outImg);
    }
    public ImgProcess expendTwo () {
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
        return new ImgProcess(outImg);
    }

    // 腐蚀算法
    public ImgProcess corrodeEight () {
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
        return new ImgProcess(outImg);
    }

    public ImgProcess corrodeTwo () {
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
        return new ImgProcess(outImg);
    }
    // 开运算
    public ImgProcess openEight () {
        return corrodeEight().expendEight();
    }
    public ImgProcess openTwo () {
        return corrodeTwo().expendTwo();
    }

    // 闭运算
    public ImgProcess closeEight () {
        return expendEight().corrodeEight();
    }
    public ImgProcess closeTwo () {
        return expendTwo().corrodeTwo();
    }

    public BufferedImage clip (int startx,int starty,int w,int h) {
        BufferedImage outImg = new BufferedImage(w, h, image.getType());
        for(int j = 0;j < h;j++)
            for (int i = 0; i < w; i++) {
                outImg.setRGB(i,j,image.getRGB(i+startx,j+starty));
            }
        return outImg;
    }
    // 中值滤波
    public ImgProcess middle () {
        BufferedImage outImg = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        for(int j = 0;j < image.getHeight();j++)
            for (int i = 0; i < image.getWidth(); i++) {
                if(i==0||j==0||i==image.getWidth()-1||j==image.getHeight()-1) {
                    outImg.setRGB(i,j,image.getRGB(i,j));
                }else {
                    outImg.setRGB(i,j,middle(new int[]{
                            image.getRGB(i-1,j-1),image.getRGB(i,j-1),image.getRGB(i+1,j-1),
                            image.getRGB(i-1,j),image.getRGB(i,j),image.getRGB(i+1,j),
                            image.getRGB(i-1,j+1),image.getRGB(i,j+1),image.getRGB(i+1,j+1)
                    }));
                }
            }
        return new ImgProcess(outImg);
    }

    private int middle (int[] argbs) {
        int[] rgbs = new int[argbs.length];
        for (int i = 0;i<argbs.length;i++) {
            int argb = argbs[i];
            int[] arr = ImgUtil.IntARGB2Array(argb);
            int rgb = arr[1]<<16|arr[2]<<8|arr[3];
            rgbs[i] = rgb;
        }
        Arrays.sort(rgbs);
        return (-1<<24)|rgbs[rgbs.length/2];
    }
    // 均值滤波
    public ImgProcess average () {
        BufferedImage outImg = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        for(int j = 0;j < image.getHeight();j++)
            for (int i = 0; i < image.getWidth(); i++) {
                if(i==0||j==0||i==image.getWidth()-1||j==image.getHeight()-1) {
                    outImg.setRGB(i,j,image.getRGB(i,j));
                }else {
                    outImg.setRGB(i,j,average(new int[]{
                        image.getRGB(i-1,j-1),image.getRGB(i,j-1),image.getRGB(i+1,j-1),
                        image.getRGB(i-1,j),image.getRGB(i,j),image.getRGB(i+1,j),
                        image.getRGB(i-1,j+1),image.getRGB(i,j+1),image.getRGB(i+1,j+1)
                    }));
                }
            }
        return new ImgProcess(outImg);
    }

    private int average (int[] argbs) {
        int a=0,r=0,g=0,b=0;
        for(int argb : argbs) {
            int[] arr = ImgUtil.IntARGB2Array(argb);
            a+=arr[0];
            r+=arr[1];
            g+=arr[2];
            b+=arr[3];
        }
        return ImgUtil.Array2IntARGB(new int[]{Math.round(a/9),Math.round(r/9),Math.round(g/9),Math.round(b/9)});
    }

    public BufferedImage[] spiltMock () {
        return new BufferedImage[]{
            this.clip(4,6,10,17),
            this.clip(20,6,10,17),
            this.clip(34,6,10,17),
            this.clip(50,6,10,17)
        };
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

    private int toBinary(int argb) {
        return ImgUtil.isBlack(argb)?0:1;
    }

    private int[] zhangValidCommon(int i,int j) {
        int[] p = {
            toBinary(image.getRGB(i, j)),toBinary(image.getRGB(i, j-1)),toBinary(image.getRGB(i+1, j-1)),
            toBinary(image.getRGB(i+1, j)),toBinary(image.getRGB(i+1, j+1)),toBinary(image.getRGB(i, j+1)),
            toBinary(image.getRGB(i-1, j+1)),toBinary(image.getRGB(i-1, j)),toBinary(image.getRGB(i-1, j-1))
        };
        int sum = 0,quantity = 0;
        for (int l = 1; l < p.length; l++) {
            sum += p[l];
            if(l<p.length-1 && p[l]==0 && p[l+1]==1)
                quantity++;
        }
        return (sum>=2 && sum<=6 && quantity==1)?p:null;
    }
    private boolean isZhangValidFirst(int i, int j) {
        int[] p = zhangValidCommon(i, j);
        if(p!= null) {
            return p[1]*p[3]*p[5]==0&&p[3]*p[5]*p[7]==0;
        }
        return false;
    }
    private boolean isZhangValidSecond(int i, int j) {
        int[] p = zhangValidCommon(i, j);
        if(p!= null) {
            return p[1]*p[3]*p[7]==0&&p[1]*p[5]*p[7]==0;
        }
        return false;
    }

    public ImgProcess zhangThin() {
        BufferedImage outImg = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        ImgUtil.copyTo(image,outImg);
//        int times = 0;
        List<Point> delList = new LinkedList<>();
        while (true) {
            boolean hasChange = false;
            for (int j = 1; j < outImg.getHeight() - 1; j++)
                for (int i = 1; i < outImg.getWidth() - 1; i++) {
                    if (ImgUtil.isBlack(outImg.getRGB(i, j))) {
                        if (isZhangValidFirst(i, j)) {
                            hasChange = true;
                            delList.add(new Point(i,j));
                        }
                    }
                }
            System.out.println(delList.size());
            for (Point p:delList){
                outImg.setRGB(p.x, p.y, WHITE_ARGB);
            }
            delList.clear();
            for (int j = 1; j < outImg.getHeight() - 1; j++)
                for (int i = 1; i < outImg.getWidth() - 1; i++) {
                    outImg.setRGB(i, j, outImg.getRGB(i, j));
                    if (ImgUtil.isBlack(outImg.getRGB(i, j))) {
                        if (isZhangValidSecond(i, j)) {
                            hasChange = true;
                            delList.add(new Point(i,j));
                        }
                    }
                }
            System.out.println(delList.size());
            for (Point p:delList){
                outImg.setRGB(p.x, p.y, WHITE_ARGB);
            }
            delList.clear();
            if(!hasChange) break;
        }
        return new ImgProcess(outImg);
    }


    public final int BLACK_ARGB = -16777216;
    public final int WHITE_ARGB = -1;



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