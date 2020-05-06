package com.example.sign_lang_ml;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;

public class TrainingActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {

    private static final int CLASSIFY_INTERVAL = 20;
    private static final String TAG = "TrainingActivity";

    private Classifier classifier;
    private Mat frame;
    private Mat mRGBA;
    private JavaCameraView openCvCameraView;

    private TextView scoreTextView;
    private TextView resultTextView;

    private String text = "";
    private int counter = 0;
    private int score = 0;

    private BaseLoaderCallback baseloadercallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            super.onManagerConnected(status);
            if (status == BaseLoaderCallback.SUCCESS)
                openCvCameraView.enableView();
            else
                super.onManagerConnected(status);
        }
    };

    //assign xml fields to objects and set default values
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_training);

        int mCameraIndex = 0;
        openCvCameraView = findViewById(R.id.trainingCam);
        openCvCameraView.setCvCameraViewListener(com.example.sign_lang_ml.TrainingActivity.this);
        openCvCameraView.setVisibility(SurfaceView.VISIBLE);

        resultTextView = findViewById(R.id.resultText);

        scoreTextView = findViewById(R.id.scoreText);
        String s = "Score: " + score + "!";
        scoreTextView.setText(s);
    }

    //resume processes on app re-focus
    @Override
    public void onResume() {
        super.onResume();

        if (OpenCVLoader.initDebug()) {
            Log.d(TAG, "Connected camera.");
            baseloadercallback.onManagerConnected(BaseLoaderCallback.SUCCESS);
        } else {
            Log.d(TAG, "Camera not connected.");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, this, baseloadercallback);
        }

        int windowVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        getWindow().getDecorView().setSystemUiVisibility(windowVisibility);

        try {
            classifier = new Classifier(this);
            text = classifier.getRandomLabel();
            resultTextView.setText(text);
        } catch (IOException e) {
            Log.e(TAG, "Failed to initialize classifier", e);
        }
    }

    //suspend resources when app is not in focus
    @Override
    public void onPause() {
        super.onPause();

        if (openCvCameraView != null) {
            openCvCameraView.disableView();
        }
        if (classifier != null) {
            classifier.close();
        }
    }

    //destroy objects to free resources on destruction
    @Override
    public void onDestroy() {
        super.onDestroy();

        if (openCvCameraView != null) {
            openCvCameraView.disableView();
        }
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        mRGBA = new Mat();
    }

    @Override
    public void onCameraViewStopped() {
        if (mRGBA != null) mRGBA.release();
    }

    //every (20 default) frame(s) run the intepreter on the frame
    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        float mh = mRGBA.height();
        float cw = (float) Resources.getSystem().getDisplayMetrics().widthPixels;
        float scale = mh / cw * 0.7f;

        mRGBA = inputFrame.rgba();
        frame = classifier.processMat(mRGBA);

        if (counter == CLASSIFY_INTERVAL) {
            runInterpreter();
            counter = 0;
        } else {
            counter++;
        }

        //draw the rectangle to show where hand should be placed in the image
        Imgproc.rectangle(mRGBA,
                new Point(mRGBA.cols() / 2f - (mRGBA.cols() * scale / 2),
                        mRGBA.rows() / 2f - (mRGBA.cols() * scale / 2)),
                new Point(mRGBA.cols() / 2f + (mRGBA.cols() * scale / 2),
                        mRGBA.rows() / 2f + (mRGBA.cols() * scale / 2)),
                new Scalar(0, 255, 0), 1);

        System.gc();
        return mRGBA;
    }

    //run predictions and sets / updates score
    private void runInterpreter() {
        if (classifier != null) {
            classifier.classifyMat(frame);
            if (!classifier.getResult().equals("NOTHING") && classifier.getResult().equals(text)) {
                score++;
                text = classifier.getRandomLabel();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String s = "Score: " + score + "!";
                        scoreTextView.setText(s);
                        resultTextView.setText(text);
                    }
                });
            }
            Log.d(TAG, "Guess: " + classifier.getResult() + " Probability: " + classifier.getProbability());
        }
    }
}