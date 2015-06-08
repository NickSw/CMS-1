package ua.demo.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
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
            double scale=largeWidth/(double)orWidth;

            int width=(int)Math.floor(orWidth*scale);
            int height=(int)Math.floor(orHeight*scale);

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
            double xScale=desWidth/(double)orWidth;
            double yScale=desHeight/(double)orHeight;

            double scale;
            if (yScale>xScale) scale=yScale;
            else scale=xScale;

            int width=(int)Math.floor(orWidth*scale);
            int height=(int)Math.floor(orHeight*scale);

            //creates image with preserve aspect ratio with size by smallest size
            Image image = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            BufferedImage changedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

            Graphics2D g2d = changedImage.createGraphics();
            g2d.drawImage(image, 0, 0, null);
            g2d.dispose();



            //crop image to given size
            int x=(int)Math.floor(((width-desWidth)/2d));
            int y=(int)Math.floor(((height-desHeight)/2d));

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

