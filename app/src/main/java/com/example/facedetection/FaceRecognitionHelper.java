package com.example.facedetection;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceContour;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;
import com.google.mlkit.vision.face.FaceLandmark;

import java.io.IOException;
import java.util.List;

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

    public interface LandmarkCallback {
        void onLandMarkReady(Bitmap landmarkedImage);
    }

    public void getEmbeddings(Bitmap bitmap, EmbeddingsCallback callback, LandmarkCallback callback2 ) {
        if (faceRecognitionModel == null) {
            callback.onEmbeddingsReady(new float[0]); // Return empty array if model initialization fails
            return;
        }

        InputImage inputImage = InputImage.fromBitmap(bitmap, 0);

        FaceDetectorOptions options = new FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
            .build();

        FaceDetector detector = FaceDetection.getClient(options);

        // Handle face detection
        detector.process(inputImage)
            .addOnSuccessListener(faces -> {
                for (Face face : faces) {
                    callback2.onLandMarkReady(drawLandmarksAndContoursOnBitmap(bitmap,face.getAllLandmarks(),face.getAllContours(),face.getBoundingBox()));
                    Log.i("face", "getEmbeddings: "+face);
                    face.getLandmark(0);
                    Bitmap faceBitmap = cropFace(bitmap, face);
                    if (faceBitmap != null) {
                        try {
                            float[] embeddings = faceRecognitionModel.getEmbeddings(faceBitmap);
                            Log.i("FaceRecognitionHelper", "Embeddings computed successfully"+embeddings);
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

    /*private Bitmap drawLandmarksOnBitmap(Bitmap originalBitmap, List<FaceLandmark> landmarks) {
        Bitmap bitmapWithLandmarks = originalBitmap.copy(originalBitmap.getConfig(), true);
        Canvas canvas = new Canvas(bitmapWithLandmarks);
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(5);

        // Draw landmarks on the bitmap
        for (FaceLandmark landmark : landmarks) {
            PointF point = landmark.getPosition();
            canvas.drawCircle(point.x, point.y, 2, paint); // Adjust the radius as needed
        }

        return bitmapWithLandmarks;
    }*/

    /*private Bitmap drawLandmarksAndContoursOnBitmap(Bitmap originalBitmap, List<FaceLandmark> landmarks, List<FaceContour> contours) {
        Bitmap bitmapWithLandmarksAndContours = originalBitmap.copy(originalBitmap.getConfig(), true);
        Canvas canvas = new Canvas(bitmapWithLandmarksAndContours);
        Paint landmarkPaint = new Paint();
        landmarkPaint.setColor(Color.RED);
        landmarkPaint.setStyle(Paint.Style.FILL);
        landmarkPaint.setStrokeWidth(5);

        Paint contourPaint = new Paint();
        contourPaint.setColor(Color.BLUE);
        contourPaint.setStyle(Paint.Style.STROKE);
        contourPaint.setStrokeWidth(5);

        // Draw landmarks on the bitmap
        for (FaceLandmark landmark : landmarks) {
            PointF point = landmark.getPosition();
            canvas.drawCircle(point.x, point.y, 2, landmarkPaint); // Adjust the radius as needed
        }

        // Draw contours on the bitmap
        for (FaceContour contour : contours) {
            List<PointF> points = contour.getPoints();
            Path contourPath = new Path();
            contourPath.moveTo(points.get(0).x, points.get(0).y);
            for (int i = 1; i < points.size(); i++) {
                PointF point = points.get(i);
                contourPath.lineTo(point.x, point.y);
            }
            canvas.drawPath(contourPath, contourPaint);
        }

        return bitmapWithLandmarksAndContours;
    }*/

    private Bitmap drawLandmarksAndContoursOnBitmap(Bitmap originalBitmap, List<FaceLandmark> landmarks, List<FaceContour> contours, Rect boundingBox) {
        Bitmap bitmapWithBoundingBox = originalBitmap.copy(originalBitmap.getConfig(), true);
        Canvas canvas = new Canvas(bitmapWithBoundingBox);
        Paint landmarkPaint = new Paint();
        landmarkPaint.setColor(Color.RED);
        landmarkPaint.setStyle(Paint.Style.FILL);
        landmarkPaint.setStrokeWidth(5);

        Paint contourPaint = new Paint();
        contourPaint.setColor(Color.BLUE);
        contourPaint.setStyle(Paint.Style.STROKE);
        contourPaint.setStrokeWidth(5);


        // Draw landmarks on the bitmap
        for (FaceLandmark landmark : landmarks) {
            PointF point = landmark.getPosition();
            canvas.drawCircle(point.x, point.y, 2, landmarkPaint); // Adjust the radius as needed
            // Update bounding box
        }

        // Draw contours on the bitmap
        for (FaceContour contour : contours) {
            List<PointF> points = contour.getPoints();
            Path contourPath = new Path();
            contourPath.moveTo(points.get(0).x, points.get(0).y);
            for (int i = 1; i < points.size(); i++) {
                PointF point = points.get(i);
                contourPath.lineTo(point.x, point.y);
                // Update bounding box
            }
            canvas.drawPath(contourPath, contourPaint);
        }

        // Draw bounding box
        canvas.drawRect(boundingBox, contourPaint);

        return bitmapWithBoundingBox;
    }


}
