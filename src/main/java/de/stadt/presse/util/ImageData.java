package de.stadt.presse.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.imageio.ImageIO;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.jpeg.exif.ExifRewriter;
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata;
import org.apache.commons.imaging.formats.tiff.constants.MicrosoftTagConstants;
import org.apache.commons.imaging.formats.tiff.constants.TiffDirectoryConstants;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputDirectory;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputSet;
import org.apache.commons.io.IOUtils;
import org.imgscalr.Scalr;

public class ImageData {

    private byte[] imageData;
    private TiffOutputSet outputSet ;


    public ImageData(InputStream instream) throws IOException {
        imageData  = IOUtils.toByteArray(instream);
        instream.close();
    }


    public synchronized void resize(int maxDimension) throws IOException, ImageReadException, ImageWriteException {
        // Resize the image if necessary
        BufferedImage image = readImage(imageData);
        if (image.getWidth() > maxDimension || image.getHeight() > maxDimension) {

            // Save existing metadata, if any
          TiffImageMetadata metadata = readExifMetadata(imageData);

//          System.out.println(metadata);
//          System.out.println(metadata.getFieldValue(MicrosoftTagConstants.EXIF_TAG_XPKEYWORDS));
          outputSet = metadata.getOutputSet();
          System.out.println(outputSet.findField(MicrosoftTagConstants.EXIF_TAG_XPKEYWORDS));
          reWriteMetadata();
            imageData = null; // allow immediate GC

            // resize
            image = Scalr.resize(image, maxDimension);

            // rewrite resized image as byte[]
            byte[] resizedData = writeJPEG(image);
            image = null; // allow immediate GC

            // Re-code resizedData + metadata to imageData
            if (metadata != null) {
                this.imageData = writeExifMetadata(metadata, resizedData);
            } else {
                this.imageData = resizedData;
            }
        }
    }

    private TiffImageMetadata readExifMetadata(byte[] jpegData) throws ImageReadException, IOException {
        ImageMetadata imageMetadata = Imaging.getMetadata(jpegData);
        if (imageMetadata == null) {
            return null;
        }
        JpegImageMetadata jpegMetadata = (JpegImageMetadata)imageMetadata;
        TiffImageMetadata exif = jpegMetadata.getExif();
        if (exif == null) {
            return null;
        }
        return exif;
    }


    private byte[] writeExifMetadata(TiffImageMetadata metadata, byte[] jpegData)
                                throws ImageReadException, ImageWriteException, IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        new ExifRewriter().updateExifMetadataLossless(jpegData, out, outputSet);
        out.close();
        return out.toByteArray();
    }


    private BufferedImage readImage(byte[] data) throws IOException {
        return ImageIO.read(new ByteArrayInputStream(data));
    }

    private byte[] writeJPEG(BufferedImage image) throws IOException {
        ByteArrayOutputStream jpegOut = new ByteArrayOutputStream();
        ImageIO.write(image, "JPEG", jpegOut);
        jpegOut.close();
        return jpegOut.toByteArray();
    }

    public synchronized void writeJPEG(OutputStream outstream) throws IOException {
        IOUtils.write(imageData,  outstream);

    }

    public synchronized void reWriteMetadata( ) throws ImageWriteException {

      final TiffOutputDirectory exifDirectory = outputSet.getOrCreateRootDirectory();
      exifDirectory.removeField(MicrosoftTagConstants.EXIF_TAG_XPCOMMENT);
      exifDirectory.add(MicrosoftTagConstants.EXIF_TAG_XPCOMMENT, "SomeKind");


//      TiffOutputDirectory exifDir = outputSet.findDirectory(TiffDirectoryConstants.DIRECTORY_TYPE_EXIF);
//      exifDir.removeField(MicrosoftTagConstants.EXIF_TAG_XPKEYWORDS);
//      exifDir.add(MicrosoftTagConstants.EXIF_TAG_XPKEYWORDS, "Mahmoud;goodbye;");
////      ExifRewriter rewriter = new ExifRewriter();
      System.out.println(outputSet.findField(MicrosoftTagConstants.EXIF_TAG_XPKEYWORDS));

    }

    public synchronized byte[] getJPEGData() {
        return imageData;
    }

}
