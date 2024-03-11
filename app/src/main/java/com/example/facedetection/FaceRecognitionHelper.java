package com.example.facedetection;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.Log;

import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;

import java.io.IOException;

public class FaceRecognitionHelper {
    private FaceRecognitionModel faceRecognitionModel;

    public FaceRecognitionHelper(Context context) {
        try {
            faceRecognitionModel = new FaceRecognitionModel(context.getAssets(), "face_recognition.tflite");
            Log.i("FaceRecognitionHelper", "Initialized successfully");
        } catch (IOException e) {
            Log.e("FaceRecognitionHelper", "Initialization error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public interface EmbeddingsCallback {
        void onEmbeddingsReady(float[] embeddings);
    }

    public void getEmbeddings(Bitmap bitmap, EmbeddingsCallback callback) {
        if (faceRecognitionModel == null) {
            callback.onEmbeddingsReady(new float[0]); // Return empty array if model initialization fails
            return;
        }

        InputImage inputImage = InputImage.fromBitmap(bitmap, 0);

        FaceDetectorOptions options = new FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_NONE)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
            .build();

        FaceDetector detector = FaceDetection.getClient(options);

        // Handle face detection
        detector.process(inputImage)
            .addOnSuccessListener(faces -> {
                for (Face face : faces) {
                    Log.i("face", "getEmbeddings: "+face);
                    Bitmap faceBitmap = cropFace(bitmap, face);
                    if (faceBitmap != null) {
                        try {
                            float[] embeddings = faceRecognitionModel.getEmbeddings(faceBitmap);
                            Log.i("FaceRecognitionHelper", "Embeddings computed successfully");
                            callback.onEmbeddingsReady(embeddings);
                            return; // Return after the first face's embeddings are ready
                        } catch (IOException e) {
                            Log.e("FaceRecognitionHelper", "Embeddings computation error: " + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                }
                // If no face is detected or embeddings are not extracted, return empty array
                callback.onEmbeddingsReady(new float[0]);
            })
            .addOnFailureListener(e -> {
                Log.e("FaceRecognitionHelper", "Face detection failure: " + e.getMessage());
                e.printStackTrace();
                callback.onEmbeddingsReady(new float[0]); // Return empty array in case of failure
            });
    }

    private Bitmap cropFace(Bitmap bitmap, Face face) {
        if (bitmap == null || face == null) {
            return null;
        }

        Rect boundingBox = face.getBoundingBox();

        if (boundingBox == null || boundingBox.isEmpty()) {
            return null;
        }

        try {
            return Bitmap.createBitmap(bitmap, boundingBox.left, boundingBox.top,
                boundingBox.width(), boundingBox.height());
        } catch (IllegalArgumentException e) {
            Log.e("FaceRecognitionHelper", "Face cropping error: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
