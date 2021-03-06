package moyu;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;

/**
 * Created by Moyu on 16/7/25.
 */
public class Services {
    public static void main(String[] args) {
        try {
            HttpServer hs = HttpServer.create(new InetSocketAddress(7777), 0);
            hs.createContext("/imageRecognize/data", new Handler());
            hs.setExecutor(null);
            hs.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class Handler implements HttpHandler {
    public void handle(HttpExchange t) throws IOException {
        System.out.println(t.getRequestURI().toString());
        InputStream is = t.getRequestBody();
        OutputStream os = t.getResponseBody();

        BufferedImage image = ImageIO.read(is);
        image = new ImgProcess(image).clip(2,2,image.getWidth()-4,image.getHeight()-4);
        image = new ImgProcess(image).binary().getImage();
        String string = "";
        BufferedImage[] imgs = new ImgProcess(image).spilt(10,17);
        if(imgs.length!=4) imgs = new ImgProcess(image).spiltMock();
        for (BufferedImage img : imgs) {//
            string+=Robot.recognize(img);
        }
        t.sendResponseHeaders(200, string.length());
        System.out.println(string);
        os.write(string.getBytes());
        os.close();
    }
}