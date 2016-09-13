package moyu;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Moyu on 16/7/24.
 */
public class Robot {
    private static Map<String,List<BinImage>> loadSamples (String samplePath){
        Map<String,List<BinImage>> rlt = new HashMap();
        File dir = new File(samplePath);
        for (File file : dir.listFiles()) {
            if(file.isDirectory() && file.getName().matches("\\d+")) {
                List<BinImage> list = new ArrayList<>();
                for (File img : file.listFiles()) {
                    if(img.isHidden()) continue;
                    try {
                        list.add(new BinImage(ImageIO.read(new FileInputStream(img))));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                rlt.put(file.getName(),list);
            }
        }
        return rlt;
    }

    private static Map<String,List<BinImage>> knowledge = loadSamples("data/sample");

    public static String recognize(BufferedImage img) {
        int maxP = 0;
        String guess = "";
        for (String key : knowledge.keySet()) {
            for (BinImage binImage : knowledge.get(key)) {
                int p = binImage.similarPercentage(img);
                if(maxP<p) {
                    maxP = p;
                    guess = key;
                }
            }
        }
        return guess;
    }
}
