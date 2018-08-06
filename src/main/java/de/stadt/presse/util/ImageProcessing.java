package de.stadt.presse.util;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.iptc.IptcDirectory;

import javax.activation.MimetypesFileTypeMap;
import javax.imageio.*;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ImageProcessing {
//  public static void main(String[] args) throws IOException {
//    String drive = "E:/Fotolia/Hintergründe/",
//      n = "",
//      orgName = "Abstraktes Muster_aSuruwataRi_Fotolia",
//      type = ".jpg";
//
//    String prefix = drive + n + orgName;
//
//    String org = prefix + type,
//      compressed = prefix + "-c" + type,
//      watermark = prefix + "-wmT" + type,
//      watermarkImage = prefix + "-wmI" + type;
//
//    String waterImage = "E:/foloParent/tmp/logo.png";
//
//    File filename = new File(org);
//    File desFileCompress = new File(compressed);
//    File desFileWatermark = new File(watermark);
//    File desFileWatermarkImage = new File(watermarkImage);
//    File inWaterImageFile = new File(waterImage);
//
////    readImageMetadata(org);
////    compressImageThump(filename, desFileCompress);
////    addTextWatermark("Press Oldenburg", desFileCompress, desFileWatermark);
////    addImageWatermark(inWaterImageFile, desFileCompress, desFileWatermarkImage);
////
//    resize("D:\\pics\\Fotolia\\Icons\\Einzelne\\Abl.jpg", "d:/01-i.jpg", 1000);
//  }


  /**
   * Read keywords from image metadata
   *
   * @param org Image String path.
   */
  public static String readImageMetadata(String org) {
    try {
      File image = new File(org);
      Metadata metadata = ImageMetadataReader.readMetadata(image);
      List<String> keyword = metadata.getFirstDirectoryOfType(IptcDirectory.class).getKeywords();

      //remove duplicated values
      Set<String> keys = new HashSet<>();
      for (String key : keyword) {
        keys.add(key.toLowerCase());
      }

      return String.join(";", keys);
    } catch (Exception e) {
      return "null";
    }

  }

  public static boolean compressImageThump(String imagePath, String compressedImagePath) {
    try {
      File imageFile = new File(imagePath);
      File compressedImageFile = new File(compressedImagePath);

      BufferedImage image = ImageIO.read(imageFile);

      OutputStream os = new FileOutputStream(compressedImageFile);
      String formatName = imagePath.substring(imagePath.lastIndexOf(".") + 1);

      Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName(formatName);
      ImageWriter writer = writers.next();

      ImageOutputStream ios = ImageIO.createImageOutputStream(os);
      writer.setOutput(ios);

      ImageWriteParam param = writer.getDefaultWriteParam();

      param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
      param.setCompressionQuality(0.5f);
      writer.write(null, new IIOImage(image, null, null), param);

      os.close();
      ios.close();
      writer.dispose();
      return true;
    } catch (Exception e) {
      System.out.println(" compressImageThump ..... " + e.getClass());
      return false;
    }
  }


  /**
   * Add text watermark to Image
   *
   * @param text            : string watermark text.
   * @param sourceImagePath : Original image
   * @param destImagePath   : where to write processed image
   */
  public static String addTextWatermark(String text, String sourceImagePath, String destImagePath) {
    try {
      File sourceImageFile = new File(sourceImagePath);
      File destImageFile = new File(destImagePath);

      BufferedImage sourceImage = ImageIO.read(sourceImageFile);
      Graphics2D g2d = (Graphics2D) sourceImage.getGraphics();

      // initializes necessary graphic properties
      AlphaComposite alphaChannel = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
      g2d.setComposite(alphaChannel);
      g2d.setColor(Color.BLACK);
      g2d.setFont(new Font("Arial", Font.BOLD, 500));
      FontMetrics fontMetrics = g2d.getFontMetrics();
      Rectangle2D rect = fontMetrics.getStringBounds(text, g2d);

      // calculates the coordinate where the String is painted
      int centerX = (sourceImage.getWidth() - (int) rect.getWidth()) / 2;
      int centerY = sourceImage.getHeight() / 2;

      // paints the textual watermark
      g2d.drawString(text, centerX, centerY);
      String formatName = sourceImagePath.substring(sourceImagePath.lastIndexOf(".") + 1);

      ImageIO.write(sourceImage, formatName, destImageFile);
      g2d.dispose();

      return destImageFile.getPath();

    } catch (Exception ex) {
      //TODO java.lang.NullPointerException
      System.out.println("sourceImagePath   : " + sourceImagePath + "destImagePath  : " + destImagePath + " .... " + ex.getClass());
      return "null";
    }
  }

  /**
   * Add image watermark to Image
   *
   * @param watermarkImagePath :String path to Logo image.
   * @param sourceImagePath    :String Original image
   * @param destImagePath      :String where to write processed image
   */
  public static void addImageWatermark(String watermarkImagePath, String sourceImagePath, String destImagePath) {
    try {
      File watermarkImageFile = new File(watermarkImagePath);
      File sourceImageFile = new File(sourceImagePath);
      File destImageFile = new File(destImagePath);

      BufferedImage sourceImage = ImageIO.read(sourceImageFile);
      BufferedImage watermarkImage = ImageIO.read(watermarkImageFile);

      // initializes necessary graphic properties
      Graphics2D g2d = (Graphics2D) sourceImage.getGraphics();
      AlphaComposite alphaChannel = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
      g2d.setComposite(alphaChannel);

      // calculates the coordinate where the image is painted
      int topLeftX = (sourceImage.getWidth() - watermarkImage.getWidth()) / 2;
      int topLeftY = (sourceImage.getHeight() - watermarkImage.getHeight()) / 2;

      // paints the image watermark
      g2d.drawImage(watermarkImage, topLeftX, topLeftY, null);

      String formatName = sourceImagePath.substring(sourceImagePath.lastIndexOf(".") + 1);

      ImageIO.write(sourceImage, formatName, destImageFile);
      g2d.dispose();

    } catch (Exception ex) {
      System.err.println("addImageWatermark :" + ex.getClass());
    }
  }

  private static int count = 0;

  public static boolean copyImage(File src, File dst) {

    try {
      InputStream in = new FileInputStream(src);
      OutputStream out = new FileOutputStream(dst + "/" + src.getName());

      // Transfer bytes from in to out
      byte[] buf = new byte[1024];
      int len;
      while ((len = in.read(buf)) > 0) {
        out.write(buf, 0, len);
      }
      in.close();
      out.close();
      return true;
    } catch (Exception e) {
      System.out.println("image NOT coped  ..." + e.getClass());
      return false;
    } finally {

      System.out.printf("image %s coped to %s..... count = %d %n", src, dst, count++);
    }

  }


  public static String determineImageFormat(String imagePath) {
    try {
      // get image format in a file
      File file = new File(imagePath);

      // create an image input stream from the specified file
      ImageInputStream iis = ImageIO.createImageInputStream(file);

      // get all currently registered readers that recognize the image format
      Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);

      if (!iter.hasNext()) {
        throw new RuntimeException("No readers found!");
      }
      // get the first reader
      ImageReader reader = iter.next();
      String format = reader.getFormatName();
      // close stream
      iis.close();
      return format;

    } catch (Exception e) {
      System.out.println(".....IOException determineImageFormat ...." + e.getClass());
      return null;
    }
  }

  /**
   * @param filePath file path String.
   * @return boolean true : if image, false : if not Image
   */
  public static boolean isImage(String filePath) {
    File f = new File(filePath);
    MimetypesFileTypeMap mediatype = new MimetypesFileTypeMap();
    mediatype.addMimeTypes("image/jpeg jpeg");
    mediatype.addMimeTypes("image/png png");
    mediatype.addMimeTypes("image/BMP BMP");
    mediatype.addMimeTypes("image/RAW RAW");
    mediatype.addMimeTypes("image/WEBP WEBP");
    mediatype.addMimeTypes("image/GIF  GIF");
    mediatype.addMimeTypes("image/ICO ICO");
    String mimetype = mediatype.getContentType(f);
    String type = mimetype.split("/")[0];
    return type.equals("image");
  }


  public static boolean resize(String inputImagePath, String outputImagePath, int scaledHeight) {

    // reads input image

    try {
      File inputFile = new File(inputImagePath);
      BufferedImage inputImage = ImageIO.read(inputFile);
      BufferedImage outputImage;
      int scaledWidth;

      if (inputImage.getHeight() > scaledHeight * 2) {
        scaledWidth = scaledHeight * inputImage.getWidth() / inputImage.getHeight();
        // creates output image
        outputImage = new BufferedImage(scaledWidth, scaledHeight, inputImage.getType());
        // scales the input image to the output image
        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);
        g2d.dispose();
      } else {
        outputImage = inputImage;
      }

      // extracts extension of output file
      String formatName = outputImagePath.substring(outputImagePath.lastIndexOf(".") + 1);
      // writes to output file
      ImageIO.write(outputImage, formatName, new File(outputImagePath));
      return true;
    } catch (NullPointerException | IOException e) {
      System.out.println("error IO.........resize.......... " + inputImagePath);
      return false;
    }
  }


}

