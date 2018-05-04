package image;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.iptc.IptcDirectory;
import com.sun.org.apache.xml.internal.dtm.ref.sax2dtm.SAX2DTM;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;

public class ImageProcessing {
  public static void main(String[] args) throws IOException {
    String n="01";
    String org = "e:/"+n+"-image.jpg", compressed = "e:/"+n+"-compressed.jpg", watermark = "e:/"+n+"-watermark.jpg",
    watermarkImage = "e:/"+n+"-watermarkImage.jpg";
    String waterImage = "e:/CodeJavaLogo.png";
    File filename = new File(org);
    File desFileCompress = new File(compressed);
    File desFileWatermark = new File(watermark);
    File desFileWatermarkImage = new File(watermarkImage);

    File inWaterImageFile = new File(waterImage);
//    readImageMetadata(filename);
    creatImageThump(filename,desFileCompress);
    addTextWatermark("Press Oldenburg",desFileCompress,desFileWatermark);
    addImageWatermark(inWaterImageFile,desFileCompress,desFileWatermarkImage);
  }


//  public static void imagePyrDown() {
//    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
//    Mat source = Highgui.imread("e:/compress.jpg", Highgui.CV_LOAD_IMAGE_COLOR);
//
//    Mat destination = new Mat(source.rows() / 2, source.cols() / 2, source.type());
//    destination = source;
//    Imgproc.pyrDown(source, destination, new Size(source.cols() / 2, source.rows() / 2));
//    Highgui.imwrite("e:/pyrDown.jpg", destination);
//  }


  public static void creatImageThump(File imageFile,File compressedImageFile) throws IOException {
    BufferedImage image = ImageIO.read(imageFile);

    OutputStream os = new FileOutputStream(compressedImageFile);

    Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
    ImageWriter writer = (ImageWriter) writers.next();

    ImageOutputStream ios = ImageIO.createImageOutputStream(os);
    writer.setOutput(ios);

    ImageWriteParam param = writer.getDefaultWriteParam();

    param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
    param.setCompressionQuality(0.2f);
    writer.write(null, new IIOImage(image, null, null), param);

    os.close();
    ios.close();
    writer.dispose();
  }


  /**
   * @param image Image File.
   *
   * @throws IOException              if you don't enter an data type amount for the voltage
   * @throws ImageProcessingException if you giv wrong file type
   */
  public static void readImageMetadata(File image) {
    try {
      Metadata metadata = ImageMetadataReader.readMetadata(image);
      List<String> keyword = metadata.getFirstDirectoryOfType(IptcDirectory.class).getKeywords();
      System.out.println(keyword);
    } catch (ImageProcessingException e) {
      System.out.println(e);
    } catch (IOException ioE) {
      System.out.println(ioE);
    }
  }

  public static void addTextWatermark(String text, File sourceImageFile, File destImageFile) {
    try {
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

      ImageIO.write(sourceImage, "jpg", destImageFile);
      g2d.dispose();

      System.out.println("The tex watermark is added to the image.");

    } catch (IOException ex) {
      System.err.println(ex);
    }
  }

  static void addImageWatermark(File watermarkImageFile, File sourceImageFile, File destImageFile) {
    try {
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

      ImageIO.write(sourceImage, "jpg", destImageFile);
      g2d.dispose();

      System.out.println("The image watermark is added to the image.");

    } catch (IOException ex) {
      System.err.println(ex);
    }
  }


}
