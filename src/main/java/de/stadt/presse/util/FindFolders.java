package de.stadt.presse.util;

import io.grpc.internal.IoUtils;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.common.RationalNumber;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.jpeg.exif.ExifRewriter;
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata;
import org.apache.commons.imaging.formats.tiff.constants.ExifTagConstants;
import org.apache.commons.imaging.formats.tiff.constants.MicrosoftTagConstants;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputDirectory;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputSet;
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


    File input = new File("e:\\testimage.jpg");
    File output= new File("e:\\testimage9999.jpg");

    changeExifMetadata(input,output);

    File initialFile = new File("e:\\testimage.jpg");
//  File initialcFile = new File("E:\\pics\\Fotolia\\Baustellen\\Bauplan_Sergey Nivens_Fotolia.jpg");
    InputStream targetStream = FileUtils.openInputStream(initialFile);

    ImageData imageData = new ImageData(targetStream);

    imageData.resize(1000);

    OutputStream outputStream = new FileOutputStream("e:\\testimage222.jpg");
    imageData.writeJPEG(outputStream);


  }



  public static void changeExifMetadata(final File jpegImageFile, final File dst)
    throws IOException, ImageReadException, ImageWriteException {
    OutputStream os = null;
    boolean canThrow = false;
    try {
      TiffOutputSet outputSet = null;

      // note that metadata might be null if no metadata is found.
      final ImageMetadata metadata = Imaging.getMetadata(jpegImageFile);
      final JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;
      if (null != jpegMetadata) {
        // note that exif might be null if no Exif metadata is found.
        final TiffImageMetadata exif = jpegMetadata.getExif();

        if (null != exif) {
          // TiffImageMetadata class is immutable (read-only).
          // TiffOutputSet class represents the Exif data to write.
          //
          // Usually, we want to update existing Exif metadata by
          // changing
          // the values of a few fields, or adding a field.
          // In these cases, it is easiest to use getOutputSet() to
          // start with a "copy" of the fields read from the image.
          outputSet = exif.getOutputSet();
        }
      }

      // if file does not contain any exif metadata, we create an empty
      // set of exif metadata. Otherwise, we keep all of the other
      // existing tags.
      if (null == outputSet) {
        outputSet = new TiffOutputSet();
      }

      {
        // Example of how to add a field/tag to the output set.
        //
        // Note that you should first remove the field/tag if it already
        // exists in this directory, or you may end up with duplicate
        // tags. See above.
        //
        // Certain fields/tags are expected in certain Exif directories;
        // Others can occur in more than one directory (and often have a
        // different meaning in different directories).
        //
        // TagInfo constants often contain a description of what
        // directories are associated with a given tag.
        //
        final TiffOutputDirectory exifDirectory = outputSet.getOrCreateExifDirectory();
        // make sure to remove old value if present (this method will
        // not fail if the tag does not exist).
//        exifDirectory.removeField(ExifTagConstants.EXIF_TAG_APERTURE_VALUE);
        exifDirectory.add(ExifTagConstants.EXIF_TAG_APERTURE_VALUE, new RationalNumber(3, 10));
      }

      {
        // Example of how to add/update GPS info to output set.

        // New York City
        final double longitude = -74.0; // 74 degrees W (in Degrees East)
        final double latitude = 40 + 43 / 60.0; // 40 degrees N (in Degrees
        // North)

        outputSet.setGPSInDegrees(longitude, latitude);
      }



      final TiffOutputDirectory exifDirectory = outputSet
        .getOrCreateRootDirectory();
//      exifDirectory.removeField(MicrosoftTagConstants.EXIF_TAG_XPKEYWORDS);
      exifDirectory.add(MicrosoftTagConstants.EXIF_TAG_XPKEYWORDS,
        "Test ---- 2");

      os = new FileOutputStream(dst);
      os = new BufferedOutputStream(os);

      new ExifRewriter().updateExifMetadataLossless(jpegImageFile, os,
        outputSet);

      canThrow = true;
    } finally {
//      IoUtils.closeQuietly(canThrow, os);
    }
  }
}


