package pressBiro;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.iptc.IptcDirectory;


import javax.activation.MimetypesFileTypeMap;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Iterator;
import java.util.List;

public class ImageProcessing {
  public static void main(String[] args)  {
    String drive = "E:/Fotolia/Hintergründe/",
      n = "",
      orgName = "Abstraktes Muster_aSuruwataRi_Fotolia",
      type = ".jpg";

    String prefix = drive + n + orgName;

    String org = prefix + type,
      compressed = prefix + "-c" + type,
      watermark = prefix + "-wmT" + type,
      watermarkImage = prefix + "-wmI" + type;

    String waterImage = "E:/foloParent/tmp/logo.png";

    File filename = new File(org);
    File desFileCompress = new File(compressed);
    File desFileWatermark = new File(watermark);
    File desFileWatermarkImage = new File(watermarkImage);
    File inWaterImageFile = new File(waterImage);

    readImageMetadata(filename);
//    compressImageThump(filename, desFileCompress);
//    addTextWatermark("Press Oldenburg", desFileCompress, desFileWatermark);
//    addImageWatermark(inWaterImageFile, desFileCompress, desFileWatermarkImage);
//
  }


  /**
   * Read keywords from image metadata
   *
   * @param image Image File.
   */
  private static void readImageMetadata(File image) {
    try {
      Metadata metadata = ImageMetadataReader.readMetadata(image);
      List<String> keyword = metadata.getFirstDirectoryOfType(IptcDirectory.class).getKeywords();
      System.out.println(keyword);
    } catch (ImageProcessingException | IOException e) {
      System.out.println("can't find the file ...");
    }
  }

  private static void compressImageThump(File imageFile, File compressedImageFile)  {
    try {
      BufferedImage image = ImageIO.read(imageFile);

      OutputStream os = new FileOutputStream(compressedImageFile);

      Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
      ImageWriter writer = writers.next();

      ImageOutputStream ios = ImageIO.createImageOutputStream(os);
      writer.setOutput(ios);

      ImageWriteParam param = writer.getDefaultWriteParam();

      param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
      param.setCompressionQuality(0.2f);
      writer.write(null, new IIOImage(image, null, null), param);

      os.close();
      ios.close();
      writer.dispose();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  /**
   * Add text watermark to Image
   *
   * @param text            : string watermark text.
   * @param sourceImageFile : Original image
   * @param destImageFile   : where to write processed image
   */
  private static void addTextWatermark(String text, File sourceImageFile, File destImageFile) {
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
      ex.printStackTrace();
    }
  }

  /**
   * Add image watermark to Image
   *
   * @param watermarkImageFile :File path to Logo image.
   * @param sourceImageFile    :File Original image
   * @param destImageFile      :File where to write processed image
   */
  private static void addImageWatermark(File watermarkImageFile, File sourceImageFile, File destImageFile) {
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
      System.err.println("addImageWatermark :" + ex);
    }
  }


  /**
   * @param filePath file path String.
   * @return boolean true : if image, false : if not Image
   */
  private static boolean isImage(String filePath) {
    File f = new File(filePath);
    MimetypesFileTypeMap mediatype = new MimetypesFileTypeMap();
    mediatype.addMimeTypes("image/psd psd");
    String mimetype = mediatype.getContentType(f);
    String type = mimetype.split("/")[0];
    if (type.equals("image")) {
      System.out.println("It's an image");
      return false;
    } else {
      System.out.println("It's NOT an image");
      return true;
    }
  }


  public static void imagePyrDown() {
//    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
//    Mat source = Highgui.imread("e:/02-compressed.jpg", Highgui.CV_LOAD_IMAGE_COLOR);
//
//    Mat destination = new Mat(source.rows() / 2, source.cols() / 2, source.type());
////    Mat destination = new Mat(source.rows() / 2, source.cols() / 2, source.type());
//    destination = source;
//    Imgproc.pyrDown(source, destination, new Size(source.cols() / 2, source.rows() / 2));
//    Highgui.imwrite("e:/pyrDown.jpg", destination);
  }


}

