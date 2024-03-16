package lk.AM.util;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import java.awt.image.BufferedImage;

public class ImageAutoCrop {
    public static Image autoCropCenter(Image inputImage, int targetWidth, int targetHeight) {
        BufferedImage originalImage = SwingFXUtils.fromFXImage(inputImage, null);

        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();

        // Calculate the center coordinates
        int centerX = originalWidth / 2;
        int centerY = originalHeight / 2;

        // Calculate the crop region
        int cropX = Math.max(0, centerX - targetWidth / 2);
        int cropY = Math.max(0, centerY - targetHeight / 2);
        int cropWidth = Math.min(targetWidth, originalWidth - cropX);
        int cropHeight = Math.min(targetHeight, originalHeight - cropY);

        // Crop the image
        BufferedImage croppedImage = originalImage.getSubimage(cropX, cropY, cropWidth, cropHeight);

        return SwingFXUtils.toFXImage(croppedImage, null);
    }
}
