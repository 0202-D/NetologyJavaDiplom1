package ru.netology.graphics.image.impl;

import ru.netology.graphics.image.BadImageSizeException;
import ru.netology.graphics.image.TextColorSchema;
import ru.netology.graphics.image.TextGraphicsConverter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * @author Dm.Petrov
 * DATE: 14.05.2022
 */
public class TextGraphicConverterImpl implements TextGraphicsConverter {

    private int maxWidth;
    private int maxHeight;
    private double maxRatio;
    char[][] picture;
    private TextColorSchema schema = new TextColorSchemaImpl();

    @Override
    public String convert(String url) throws IOException, BadImageSizeException {
        int newWidth;
        int newHeight;
        int color;
        BufferedImage img = ImageIO.read(new URL(url));
        int width = img.getWidth();
        int height = img.getHeight();
        if (!checkPicture(width, height) || width == 0 || height == 0) {
            throw new BadImageSizeException((double) width / height, maxRatio);
        }
        double proportion = calculateProportion(width, height);
        newWidth = (int) settingCorrectSize(width, proportion);
        newHeight = (int) settingCorrectSize(height, proportion);

        Image scaledImage = img.getScaledInstance(newWidth, newHeight, BufferedImage.SCALE_SMOOTH);
        BufferedImage bwImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D graphics = bwImg.createGraphics();
        graphics.drawImage(scaledImage, 0, 0, null);
        ImageIO.write(bwImg, "png", new File("out.png"));
        WritableRaster bwRaster = bwImg.getRaster();

        picture = new char[newHeight][newWidth];
        for (int i = 0; i < newHeight; i++) {
            for (int j = 0; j < newWidth; j++) {
                color = bwRaster.getPixel(j, i, new int[3])[0];
                char c = schema.convert(color);
                picture[i][j] = c;
            }
        }

        StringBuilder finalPicture = new StringBuilder();
        return stringAssembly(picture, finalPicture);
    }

    public String stringAssembly(char[][] picture, StringBuilder finalPicture) {
        for (char[] chars : picture) {
            for (char aChar : chars) {
                finalPicture.append(aChar);
                finalPicture.append(aChar);
            }
            finalPicture.append("\n");
        }
        return finalPicture.toString();
    }

    public boolean checkPicture(int width, int height) {
        return !((double) width / (double) height > maxRatio) || ((double) height / (double) width > maxRatio);
    }

    public double calculateProportion(int width, int height) {
        if (width <= maxWidth && height <= maxHeight) {
            return 1;
        } else if (width - maxWidth > height - maxHeight) {
            return (double) width / (double) maxWidth;
        } else {
            return (double) height / (double) maxHeight;
        }
    }


    public double settingCorrectSize(double parameter, double proportion) {
        return parameter / proportion;
    }


    @Override
    public void setMaxWidth(int width) {
        this.maxWidth = width;
    }

    @Override
    public void setMaxHeight(int height) {
        this.maxHeight = height;
    }

    @Override
    public void setMaxRatio(double maxRatio) {
        this.maxRatio = maxRatio;
    }

    @Override
    public void setTextColorSchema(TextColorSchema schema) {
        this.schema = schema;
    }

}
