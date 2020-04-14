package com.example.sign_lang_ml;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvException;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
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

import java.io.File;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.util.concurrent.RunnableFuture;


public class RecognitionActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {

    JavaCameraView javaCameraView;
    Mat mRGBA, mRGBAT;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recognition);

        javaCameraView = (JavaCameraView) findViewById(R.id.my_camera_view);
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

        // We should keep only image processing here i think
        // move conversion and actual interpretation to another function?
        // also consider using a to call this function?

        mRGBA = inputFrame.rgba();
        mRGBAT = mRGBA.t();
        Core.flip(mRGBA.t(), mRGBAT, 1);
        Imgproc.resize(mRGBAT, mRGBAT, mRGBA.size());

        ImageProcessor imageProcessor =
                new ImageProcessor.Builder()
                        .add(new ResizeOp(80, 80, ResizeOp.ResizeMethod.BILINEAR))
                        .build();

        TensorImage tImage = new TensorImage(DataType.UINT8);

        Bitmap bmp = null;
        Mat tmp = new Mat (80, 80, CvType.CV_8U, new Scalar(4));
        try {
            Imgproc.cvtColor(mRGBAT, tmp, Imgproc.COLOR_GRAY2RGBA, 4);
            bmp = Bitmap.createBitmap(tmp.cols(), tmp.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(tmp, bmp);
        }
        catch (CvException e){Log.d("Exception",e.getMessage());}

        tImage.load(bmp);
        tImage = imageProcessor.process(tImage);

        TensorBuffer probabilityBuffer = TensorBuffer.createFixedSize(new int[]{1, 1001}, DataType.UINT8);
        File tfmodel_file = new File("sign_language.tflite");
        Interpreter tflite = new Interpreter(tfmodel_file);

        if(null != tflite) {
            tflite.run(tImage.getBuffer(), probabilityBuffer.getBuffer());
        }

        tflite.close();
        return mRGBAT;
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
