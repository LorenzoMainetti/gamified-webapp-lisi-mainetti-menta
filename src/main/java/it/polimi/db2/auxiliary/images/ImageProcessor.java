package it.polimi.db2.auxiliary.images;

import it.polimi.db2.auxiliary.images.smartcrop.CropResult;
import it.polimi.db2.auxiliary.images.smartcrop.SmartCrop;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class ImageProcessor {

    public static final short STORED_H = 512;
    public static final short STORED_W = 512;

    public static byte[] AI_Cropper(byte [] imageToCrop) {

        BufferedImage bufferedImage = fromByteArray(imageToCrop);
        CropResult cropped = new SmartCrop().analyze(bufferedImage);
        BufferedImage result;
        result = (BufferedImage) cropped.resultImage.getScaledInstance(STORED_H, STORED_W, Image.SCALE_SMOOTH);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ImageIO.write(result, "jpg", bos );
        } catch (IOException e) {
            e.printStackTrace(); //todo
        }
        return bos.toByteArray();

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
