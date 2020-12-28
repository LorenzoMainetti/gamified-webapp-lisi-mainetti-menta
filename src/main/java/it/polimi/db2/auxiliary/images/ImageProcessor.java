package it.polimi.db2.auxiliary.images;



import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLConnection;

public class ImageProcessor {

    public static final short STORED_H = 512;
    public static final short STORED_W = 512;

    private static BufferedImage crop(BufferedImage src, int width, int height) throws IOException {
        int x = src.getWidth()/2 - width/2;
        int y = src.getHeight()/2 - height/2;

//        System.out.println("---" + src.getWidth() + " - " + src.getHeight() + " - " + x + " - " + y);

        BufferedImage clipping = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);//src.getType());
        Graphics2D area = (Graphics2D) clipping.getGraphics().create();
        area.drawImage(src, 0, 0, clipping.getWidth(), clipping.getHeight(), x, y, x + clipping.getWidth(),
                y + clipping.getHeight(), null);
        area.dispose();

        return clipping;
    }

    private static byte [] forceToJPEG (BufferedImage in) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ImageIO.write(in, "jpg", bos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bos.toByteArray();
    }

    public static BufferedImage prepareImage(byte [] imageArray, boolean convert) throws IOException {

        BufferedImage cropped = crop(fromByteArray(imageArray), STORED_H, STORED_W);
        if (convert) return fromByteArray(forceToJPEG(cropped));
        else return cropped;



    }


    public static String  getImageType(byte [] imageByteArray) throws IOException {
        return URLConnection.guessContentTypeFromStream(new ByteArrayInputStream(imageByteArray));
    }

    public static byte [] toByteArray(BufferedImage image, String format) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, format, bos );
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte [] data = bos.toByteArray();
        return data;
    }
    
    protected static BufferedImage fromByteArray(byte [] array) {
        ByteArrayInputStream bis = new ByteArrayInputStream(array);
        try {
            return ImageIO.read(bis);
        } catch (IOException e) {

            e.printStackTrace(); //todo
            return null; //TODO !!!!
        }

    }


}
