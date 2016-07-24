package moyu;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Moyu on 16/7/24.
 */
public class BinImage {
    private int width;
    private String string;

    private ArrayList<ArrayList<Integer>> adjlist;

    public BinImage(BufferedImage img) {
        this.width = img.getWidth();
        this.adjlist = new ArrayList<>();

        for (int j = 0; j < img.getHeight(); j++) {
            ArrayList<Integer> list = new ArrayList<>();
            for (int i = 0; i < this.width; i++) {
                int argb = img.getRGB(i, j);
                if(ImgUtil.isBlack(argb)) {
                    list.add(i);
                }
            }
            this.adjlist.add(list);
        }
    }

    @Override
    public String toString() {
        if(this.string!=null) return string;
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < adjlist.size(); i++) {
            ArrayList<Integer> list = adjlist.get(i);
            int pos = 0;
            for (int j = 0; j < width; j++) {
                if(pos>=list.size()) {
                    sb.append(0);
                }else {
                    int index = list.get(pos);
                    if(index==j) {
                        sb.append(1);
                        pos++;
                    }else {
                        sb.append(0);
                    }
                }
            }
            sb.append("\n");
        }
        this.string = sb.toString();
        return sb.toString();
    }

    public int similarPercentage(BufferedImage img) {
        String selfStr = this.toString();
        String thatStr = new BinImage(img).toString();
        int min = Math.min(selfStr.length(), thatStr.length());
        int sameNum = 0,base = 0;
        for (int i = 0; i < min; i++) {
            char ch1 = selfStr.charAt(i);
            char ch2 = thatStr.charAt(i);
            if(ch1 == '1'){
                base++;
                if(ch2=='1')
                    sameNum++;
            }
        }
        if(base==0) return 0;
        return sameNum * 100 /base;
    }
}

class Node<T>{
    public Node(){}
    public Node(T data) {
        this.data = data;
        this.next = null;
    }
    public Node(T data,Node next) {
        this.data = data;
        this.next = next;
    }
    public Node append(Node<T> node) {
        this.next = node;
        return node;
    }
    T data;
    Node next;
}