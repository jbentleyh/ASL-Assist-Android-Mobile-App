package com.example.sign_lang_ml;

import android.app.Activity;
import android.content.res.Resources;
import android.util.Log;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.common.FileUtil;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.util.List;
import java.util.Random;

class Classifier {
    private static final String TAG = "Tflite";
    private static final String MODEL = "mobilenet2.tflite";
    private static final String LABEL = "labels.txt";
    private static final int DIM_HEIGHT = 100;
    private static final int DIM_WIDTH = 100;
    private static final int BYTES = 4;
    private static String result;
    private static float probability;
    private Interpreter tflite;
    private List<String> labels;
    private ByteBuffer imgData;
    private float[][] probArray;

    //Constructor, opens tflite model and sets default parameters
    Classifier(Activity activity) throws IOException {
        MappedByteBuffer tfliteModel = FileUtil.loadMappedFile(activity, MODEL);
        Interpreter.Options tfliteOptions = new Interpreter.Options();
        tfliteOptions.setNumThreads(4);
        tflite = new Interpreter(tfliteModel, tfliteOptions);
        labels = FileUtil.loadLabels(activity, LABEL);
        imgData = ByteBuffer.allocateDirect(DIM_HEIGHT * DIM_WIDTH * BYTES);
        imgData.order(ByteOrder.nativeOrder());
        probArray = new float[1][labels.size()];
    }

    //this method takes mat converts it to a format (bytebuffer) that is readable by the model, and runs it's predictions
    void classifyMat(Mat mat) {
        if (tflite != null) {
            convertMatToByteBuffer(mat);
            runInterface();
        }
    }

    //returns the result of a prediction as a string
    String getResult() {
        return result;
    }

    //returns the models assumed probability for the last prediction
    float getProbability() {
        return probability;
    }

    //used to close the model when done interpreting to save system resources
    void close() {
        if (tflite != null) {
            tflite.close();
            tflite = null;
        }
    }

    //Image Processing
    //Scaling, color correction and edge detection are applied to the mat
    Mat processMat(Mat mat) {
        float mh = mat.height();
        float cw = (float) Resources.getSystem().getDisplayMetrics().widthPixels;
        float scale = mh / cw * 0.7f;
        Rect roi = new Rect((int) (mat.cols() / 2 - (mat.cols() * scale / 2)),
                (int) (mat.rows() / 2 - (mat.cols() * scale / 2)),
                (int) (mat.cols() * scale),
                (int) (mat.cols() * scale));
        Mat sub = mat.submat(roi);
        sub.copyTo(mat.submat(roi));

        Mat edges = new Mat(sub.size(), CvType.CV_8UC1);
        Imgproc.Canny(sub, edges, 50, 200);

        Mat element1 = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new org.opencv.core.Size(3, 3));
        Imgproc.dilate(edges, edges, element1);

        Core.rotate(edges, edges, Core.ROTATE_90_CLOCKWISE);
        Imgproc.resize(edges, edges, new Size(DIM_WIDTH, DIM_HEIGHT));
        return edges;
    }

    //Used to show edge detection on display
    Mat debugMat(Mat mat) {

        Mat edges = new Mat(mat.size(), CvType.CV_8UC1);
        Imgproc.Canny(mat, edges, 50, 200);

        Mat element1 = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new org.opencv.core.Size(3, 3));
        Imgproc.dilate(edges, edges, element1);

        return edges;
    }

    //returns a random label
    String getRandomLabel() {
        Random r = new Random();
        String rt = "";
        while (rt.equals("") || rt.equals("NOTHING"))
            rt = labels.get(r.nextInt(labels.size()));
        return rt;
    }

    //Converts mat to bytebuffer format which the model can use
    private void convertMatToByteBuffer(Mat mat) {
        imgData.rewind();
        for (int i = 0; i < DIM_HEIGHT; ++i) {
            for (int j = 0; j < DIM_WIDTH; ++j) {
                Log.d(TAG, "" + mat.get(i, j)[0]);
                imgData.putFloat((float) mat.get(i, j)[0] / 255.0f);
            }
        }
    }

    //run the model against the processed image
    private void runInterface() {
        if (imgData != null) {
            tflite.run(imgData, probArray);
        }
        processResults(probArray[0]);
        for (int i = 0; i < labels.size(); i++) {
            Log.d(TAG, labels.get(i) + ": " + probArray[0][i]);
        }
        Log.d(TAG, "Guess: " + getResult());
    }

    //sets result of the guess to label and probability.
    //if the model is 80% or more confident it sets those values
    //otherwise nothing is given
    private void processResults(float[] prob) {
        int max = 0;
        for (int i = 0; i < prob.length; ++i) {
            if (prob[i] > prob[max]) {
                max = i;
            }
        }
        if (prob[max] > 0.8f) {
            result = labels.get(max);
            probability = prob[max];
        } else {
            result = "NOTHING";
            probability = 1.0f;
        }
    }
}
