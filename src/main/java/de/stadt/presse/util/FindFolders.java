package de.stadt.presse.util;

import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata;
import org.apache.commons.io.FileUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FindFolders {

  static int counteOrg;
  static int counteSec;

  public static void main(String[] args) throws IOException, ImageWriteException, ImageReadException {


    File initialFile = new File("E:\\pics\\Fotolia\\Baustellen\\Bauplan_Sergey Nivens_Fotolia.jpg");
    InputStream targetStream = FileUtils.openInputStream(initialFile);

    ImageData imageData = new ImageData(targetStream);

    imageData.resize(1000);

    OutputStream outputStream = new FileOutputStream("e:\\testimage.jpg");
    imageData.writeJPEG(outputStream);


  }

}


