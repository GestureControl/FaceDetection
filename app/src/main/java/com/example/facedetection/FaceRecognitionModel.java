package com.example.facedetection;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.util.Log;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class FaceRecognitionModel {
    private Interpreter interpreter;
    private int inputSize;

    public FaceRecognitionModel(AssetManager assetManager, String modelPath) throws IOException {
        Interpreter.Options options = new Interpreter.Options();
        options.setNumThreads(4);
        interpreter = new Interpreter(loadModelFile(assetManager, modelPath), options);
        inputSize = interpreter.getInputTensor(0).shape()[1];
    }

    private MappedByteBuffer loadModelFile(AssetManager assetManager, String modelPath) throws IOException {
        try (AssetFileDescriptor fileDescriptor = assetManager.openFd(modelPath);
             FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor())) {
            FileChannel fileChannel = inputStream.getChannel();
            long startOffset = fileDescriptor.getStartOffset();
            long declaredLength = fileDescriptor.getDeclaredLength();
            return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
        }
    }

    public float[] getEmbeddings(Bitmap bitmap) throws IOException {
        float[][][][] inputArray = preprocessInput(bitmap);
        Log.i("inputArray1", "getEmbeddings: "+inputArray[0][1][1][0]);
        float[][] output = new float[1][192]; // Adjusted output size to match the model's output shape
        interpreter.run(inputArray, output);
        return output[0];
    }

    private float[][][][] preprocessInput(Bitmap bitmap) {
        inputSize = interpreter.getInputTensor(0).shape()[1]    ; // Retrieve input size from the model
        int[] pixels = new int[inputSize * inputSize];
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, inputSize , inputSize, true);
        resizedBitmap.getPixels(pixels, 0, inputSize, 0, 0, inputSize, inputSize);

        float[][][][] inputArray = new float[1][inputSize][inputSize][3];
        for (int i = 0; i < inputSize; i++) {
            for (int j = 0; j < inputSize; j++) {
                int pixelValue = pixels[i * inputSize + j];
                inputArray[0][i][j][0] = ((pixelValue >> 16) & 0xFF) / 255.f;
                inputArray[0][i][j][1] = ((pixelValue >> 8) & 0xFF) / 255.f;
                inputArray[0][i][j][2] = (pixelValue & 0xFF) / 255.f;
            }
        }
        return inputArray;
    }
}