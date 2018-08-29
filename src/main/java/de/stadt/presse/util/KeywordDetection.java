package de.stadt.presse.util;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionScopes;
import com.google.api.services.vision.v1.model.*;
import com.google.common.collect.ImmutableList;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class KeywordDetection {

    private static final String APPLICATION_NAME = "StadtOldenburg/1.0";

    private static final int MAX_LABELS = 500;

    private static Set<String> keywords = new HashSet<>();


    public static void main(String[] args) throws IOException, GeneralSecurityException {

        Path imagePath = Paths.get("data\\cat3.png");
        startDetection(imagePath);

    }

    private static void startDetection(Path imagePath) throws IOException, GeneralSecurityException {

        KeywordDetection app = new KeywordDetection(getVisionService());
        AnnotateImageResponse allAnnotation = app.labelImage(imagePath);
        printLabels(imagePath, allAnnotation.getLabelAnnotations());
        printWeb(allAnnotation.getWebDetection().getWebEntities());
        printfaces(allAnnotation.getFaceAnnotations());
//        printProp(allAnnotation.getImagePropertiesAnnotation().getDominantColors());

    }

    private static void printProp(DominantColorsAnnotation colors_string) {

//        for (ColorInfo color : colors_string.getColors()) {
//            System.out.println(color.getColor());
//        }

    }


    private static void printLabels(Path imagePath, List<EntityAnnotation> labels) {
        if (!labels.isEmpty()) {
            for (EntityAnnotation label : labels) {
                if (label.getDescription() != null)
                    keywords.add(label.getDescription());
            }
        }
    }


    private static void printWeb(List<WebEntity> webs) {

        if (!webs.isEmpty()) {
            for (WebEntity web : webs) {
                if (web.getDescription() != null)
                    keywords.add(web.getDescription());
            }
        }
    }

    private static void printfaces(List<FaceAnnotation> faces) {
        if (!faces.isEmpty()) {
            for (FaceAnnotation face : faces) {
                if (face.getJoyLikelihood().equalsIgnoreCase("POSSIBLE") ||
                        face.getJoyLikelihood().equalsIgnoreCase("VERY_LIKELY")) {
                    keywords.add("Freude");
                }
                if (face.getAngerLikelihood().equalsIgnoreCase("POSSIBLE") ||
                        face.getAngerLikelihood().equalsIgnoreCase("VERY_LIKELY")) {
                    keywords.add("Ärger");
                }
                if (face.getSurpriseLikelihood().equalsIgnoreCase("POSSIBLE") ||
                        face.getSurpriseLikelihood().equalsIgnoreCase("VERY_LIKELY")) {
                    keywords.add("Überraschung");
                }
                if (face.getBlurredLikelihood().equalsIgnoreCase("POSSIBLE") ||
                        face.getBlurredLikelihood().equalsIgnoreCase("VERY_LIKELY")) {
                    keywords.add("Blurre");
                }
                if (face.getHeadwearLikelihood().equalsIgnoreCase("POSSIBLE") ||
                        face.getHeadwearLikelihood().equalsIgnoreCase("VERY_LIKELY")) {
                    keywords.add("Kopfbedeckungen");
                }
                if (face.getSorrowLikelihood().equalsIgnoreCase("POSSIBLE") ||
                        face.getSorrowLikelihood().equalsIgnoreCase("VERY_LIKELY")) {
                    keywords.add("Trauer");
                }
                if (face.getUnderExposedLikelihood().equalsIgnoreCase("POSSIBLE") ||
                        face.getUnderExposedLikelihood().equalsIgnoreCase("VERY_LIKELY")) {
                    keywords.add("Unterbelichtet");
                }
            }
        }
        System.out.println(keywords);
      System.out.println(keywords.size());
    }


    private static Vision getVisionService() throws IOException, GeneralSecurityException {
        GoogleCredential credential =
                GoogleCredential.getApplicationDefault().createScoped(VisionScopes.all());
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        return new Vision.Builder(GoogleNetHttpTransport.newTrustedTransport(), jsonFactory, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }


    private final Vision vision;


    private KeywordDetection(Vision vision) {
        this.vision = vision;
    }


    private AnnotateImageResponse labelImage(Path path) throws IOException {
        byte[] data = Files.readAllBytes(path);
        Feature featureLabel = new Feature();
        Feature featureFace = new Feature();
        Feature featureWeb = new Feature();
        Feature featureProp = new Feature();

        featureLabel.setType("LABEL_DETECTION").setMaxResults(KeywordDetection.MAX_LABELS);
        featureFace.setType("FACE_DETECTION").setMaxResults(KeywordDetection.MAX_LABELS);
        featureWeb.setType("WEB_DETECTION").setMaxResults(KeywordDetection.MAX_LABELS);
//        featureProp.setType("IMAGE_PROPERTIES").setMaxResults(KeywordDetection.MAX_LABELS);


        AnnotateImageRequest request =
                new AnnotateImageRequest()
                        .setImage(new Image().encodeContent(data))
                        .setFeatures(ImmutableList.of(featureLabel, featureFace, featureWeb, featureProp));
        Vision.Images.Annotate annotate =
                vision.images()
                        .annotate(new BatchAnnotateImagesRequest().setRequests(ImmutableList.of(request)));
        annotate.setDisableGZipContent(true);

        BatchAnnotateImagesResponse batchResponse = annotate.execute();
        assert batchResponse.getResponses().size() == 2;
        AnnotateImageResponse response = batchResponse.getResponses().get(0);
        if (response.getLabelAnnotations() == null) {
            throw new IOException(
                    response.getError() != null
                            ? response.getError().getMessage()
                            : "Unknown error getting image annotations");
        }

        return response;
    }


}

