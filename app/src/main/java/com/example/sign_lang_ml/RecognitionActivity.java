package com.example.sign_lang_ml;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.content.res.AssetFileDescriptor;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvException;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.common.FileUtil;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.image.ops.ResizeOp;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;
import org.tensorflow.lite.support.model.Model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteOrder;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.RunnableFuture;
import java.util.List;
import java.util.ArrayList;


public class RecognitionActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {

    CameraBridgeViewBase javaCameraView;
    Mat mRGBA, mRGBAT;
    private MappedByteBuffer tfliteModel;
    private Interpreter tflite;
    private final Interpreter.Options tfliteOptions = new Interpreter.Options();
    private List<String> labels;
    private ByteBuffer imgData = null;
    private int[] imgValues;
    private float[][] probabilities = null;
    private String result = null;
    private String displayString = null;

    private static int IMG_SIZE = 80;
    private static final int IMG_MEAN = 128;
    private static final float IMG_STD = 128.0f;
    private static String TAG = "RecognitionActivity";

    BaseLoaderCallback baseloadercallback = new BaseLoaderCallback(RecognitionActivity.this) {
        @Override
        public void onManagerConnected(int status) {

            switch(status) {
                case BaseLoaderCallback.SUCCESS: {
                    javaCameraView.enableView();
                    break;
                }
                default: {
                    super.onManagerConnected(status);
                    break;
                }
            }
        }
    };

    private List<String> loadLabels() throws IOException {
        List<String> labels = new ArrayList<String>();
        BufferedReader r = new BufferedReader(new InputStreamReader(this.getAssets().open("labels.txt")));
        String line;
        while ((line = r.readLine()) != null) {
            labels.add(line);
        }
        r.close();
        return labels;
    }

    private void convertMatToByteBuffer(Mat mat) {
        if (imgData == null) {
            return;
        }

        imgData.rewind();
        byte[] buffer = new byte[(int) mat.total() * mat.channels()];
        int pixel = 0;
        for (int i = 0; i < IMG_SIZE; ++i) {
            for (int j = 0; j < IMG_SIZE; ++j) {
                Log.d(TAG, "i: " + i + " j: " + j + " Pixel: " + pixel + " Remaining: " + imgData.remaining());
                final int val = buffer[pixel++];
                //Quantized model behaving as non quantized model??
                imgData.putFloat((((val) & 0xFF) - IMG_MEAN) / IMG_STD);
                Log.d(TAG, "Value: " + val);
                //imgData.put((byte) (val & 0xFF));
            }
        }
    }

    /*
     * Passes camera capture through Canny edge detection algorithm
     * Resizes result to fit tensor graph
     */
    private Mat processFrame(Mat frame) {
        Mat edges = new Mat(frame.size(), CvType.CV_8UC1);
        Imgproc.cvtColor(frame, edges, Imgproc.COLOR_RGB2GRAY, 4);
        Imgproc.Canny(edges, edges, 100, 200);
        Imgproc.resize(edges, edges, new Size(IMG_SIZE, IMG_SIZE));
        return edges;
    }

    /*
    * Passes the processed frame to tensor graph
    */
    private void generateProbabilities(Mat frame) {
        convertMatToByteBuffer(frame);
        tflite.run(imgData, probabilities);

    }

    /*
    * Takes the category with highest average probability over last n frames
    * if result is different from previous result, append it to display text
    * else return
    */
    private void displayResult() {
        int max = 0;
        for (int i = 1; i < labels.size(); ++i) {
            if (probabilities[0][i] > probabilities[0][max]) {
                max = i;
            }
        }
        Log.d(TAG, "Guess: " + max + " " + labels.get(max));
        //Finish  displayResult
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recognition);
        try {
            tfliteModel = FileUtil.loadMappedFile(this, "quantizedModel.tflite");
            tflite = new Interpreter(tfliteModel);
            labels = loadLabels();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        imgData = ByteBuffer.allocateDirect( 4 * IMG_SIZE * IMG_SIZE );
        imgData.order(ByteOrder.nativeOrder());
        imgValues = new int[IMG_SIZE * IMG_SIZE];
        probabilities = new float[1][labels.size()];

        javaCameraView = (CameraBridgeViewBase) findViewById(R.id.my_camera_view);
        javaCameraView.setVisibility(SurfaceView.VISIBLE);
        javaCameraView.setCvCameraViewListener(RecognitionActivity.this);
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        mRGBA = new Mat(height, width, CvType.CV_8UC4);
    }

    @Override
    public void onCameraViewStopped() {
        mRGBA.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {

        mRGBA = inputFrame.rgba();
        mRGBAT = mRGBA.t();
        Core.flip(mRGBA.t(), mRGBAT, 1);
        Imgproc.resize(mRGBAT, mRGBAT, mRGBA.size());

        Mat frame = processFrame(mRGBAT);
        generateProbabilities(frame);
        displayResult();

        System.gc();
        return frame;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(javaCameraView != null) {
            javaCameraView.disableView();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(javaCameraView != null) {
            javaCameraView.disableView();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();


        if (OpenCVLoader.initDebug()) {
            Log.d(TAG, "Connected camera.");
            baseloadercallback.onManagerConnected(BaseLoaderCallback.SUCCESS);
        } else {
            Log.d(TAG, "Camera not connected.");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, this, baseloadercallback);
        }

    }
}
