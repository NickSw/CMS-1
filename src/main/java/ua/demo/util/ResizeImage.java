package ua.demo.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * provides resize and resize/crop of existing image.
 * it  is used for resizing uploaded images
 *
 * Created by Sergey on 06.06.2015.
 */
public class ResizeImage {
    //size for thumbnail
    private final int desWidth=60;
    private final int desHeight=50;
    //width for large image
    private final int largeWidth=470;

    //load image resize to fit width and save it (overwrite)
    public void fitWidth(String FileName){
        try {
            //load image
            BufferedImage originalImage = ImageIO.read(new File(FileName));
            //get loaded image size
            int orHeight=originalImage.getHeight();
            int orWidth=originalImage.getWidth();


            //calculate scale
            float scale=largeWidth/(float)orWidth;

            int width=Math.round(orWidth*scale);
            int height=Math.round(orHeight*scale);

            //creates image with preserve aspect ratio with size by smallest size
            Image image = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            BufferedImage changedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

            Graphics2D g2d = changedImage.createGraphics();
            g2d.drawImage(image, 0, 0, null);
            g2d.dispose();

            //save image
            ImageIO.write(changedImage, "jpg", new File(FileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //load image, resize it, crop it if needed and save it
    public void resizeAndCrop(String originFileName,String resultFileName){
        try {
            //load image
            BufferedImage originalImage = ImageIO.read(new File(originFileName));
            //get loaded image size
            int orHeight=originalImage.getHeight();
            int orWidth=originalImage.getWidth();


            //calculate scale
            float xScale=desWidth/(float)orWidth;
            float yScale=desHeight/(float)orHeight;

            float scale;
            if (yScale>xScale) scale=yScale;
            else scale=xScale;

            int width=Math.round(orWidth*scale);
            int height=Math.round(orHeight*scale);

            //creates image with preserve aspect ratio with size by smallest size
            Image image = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            BufferedImage changedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

            Graphics2D g2d = changedImage.createGraphics();
            g2d.drawImage(image, 0, 0, null);
            g2d.dispose();



            //crop image to given size
            int x=Math.round(((width-desWidth)/2f));
            int y=Math.round(((height-desHeight)/2f));


            Image cropImage=changedImage.getSubimage(x,y,desWidth,desHeight);
            BufferedImage resImage = new BufferedImage(desWidth, desHeight, BufferedImage.TYPE_INT_RGB);

            Graphics2D resg2d = resImage.createGraphics();
            resg2d.drawImage(cropImage, 0, 0, null);
            resg2d.dispose();

            //save image
            ImageIO.write(resImage, "jpg", new File(resultFileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

