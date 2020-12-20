package it.polimi.db2.auxiliary;

import it.polimi.db2.auxiliary.smartcrop.CropResult;
import it.polimi.db2.auxiliary.smartcrop.SmartCrop;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageProcessor {

    public static final short STORED_H = 512;
    public static final short STORED_W = 512;
    public static Image AI_Cropper(BufferedImage image) {

        CropResult cropped = new SmartCrop().analyze(image);
        Image result;
        result = cropped.resultImage.getScaledInstance(STORED_H, STORED_W, Image.SCALE_SMOOTH);
        return result;
    }
}
