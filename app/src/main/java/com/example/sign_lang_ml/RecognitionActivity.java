package com.example.sign_lang_ml;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvException;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;

public class RecognitionActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2, View.OnClickListener {

    private JavaCameraView openCvCameraView;
    private static String TAG = "RecognitionActivity";
    private int mCameraIndex = 0;
    private Mat mRGBA;
    private Mat intermediate;
    private Mat CNN_input;
    private Classifier classifier;
    private int CAMERA = 10;

    private Button bt;
    private TextView tv;
    private Mat frame;
    private String text = "";

    private BaseLoaderCallback baseloadercallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            super.onManagerConnected(status);
            switch(status) {
                case BaseLoaderCallback.SUCCESS: {
                    openCvCameraView.enableView();
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

        final FrameLayout layout = new FrameLayout(this);
        layout.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        setContentView(layout);

        openCvCameraView = new JavaCameraView(this, mCameraIndex);
        openCvCameraView.setCvCameraViewListener(RecognitionActivity.this);
        openCvCameraView.setVisibility(SurfaceView.VISIBLE);
        openCvCameraView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        layout.addView(openCvCameraView);

        bt = new Button(this);
        bt.setText("Debug");
        bt.setId(7);
        bt.getBackground().setAlpha(64);
        bt.setOnClickListener(this);
        bt.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER));
        layout.addView(bt);

        tv = new TextView(this);
        tv.setTextColor(Color.WHITE);
        tv.setTextSize(20f);
        tv.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT,Gravity.TOP+Gravity.LEFT));
        layout.addView(tv);
    }

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            windowVisibility |= View.SYSTEM_UI_FLAG_IMMERSIVE;
        }
        getWindow().getDecorView().setSystemUiVisibility(windowVisibility);

        try {
            classifier = new Classifier(this);
        } catch (IOException e) {
            Log.e(TAG, "Failed to initialize classifier", e);
        }
    }

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
        intermediate = new Mat();
        CNN_input = new Mat();
    }

    @Override
    public void onCameraViewStopped() {
        if (mRGBA != null) {
            mRGBA.release();
        }
        if (intermediate != null) {
            intermediate.release();
        }
        if (CNN_input != null) {
            CNN_input.release();
        }
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        float mh = mRGBA.height();
        float cw  = (float) Resources.getSystem().getDisplayMetrics().widthPixels;
        float scale = mh/cw*0.7f;

        mRGBA = inputFrame.rgba();
        Imgproc.rectangle(mRGBA,
                new Point(mRGBA.cols()/2 - (mRGBA.cols()*scale/2), mRGBA.rows() / 2 - (mRGBA.cols()*scale/2)),
                new Point(mRGBA.cols() / 2 + (mRGBA.cols()*scale/2), mRGBA.rows() / 2 + (mRGBA.cols()*scale/2)),
                new Scalar(0,255,0),1);

        frame = classifier.processMat(mRGBA);
        System.gc();
        return mRGBA;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case 7:
                if (classifier != null) {
                    Bitmap bmp = null;
                    try {
                        //bmp = Bitmap.createBitmap(frame.cols(), frame.rows(), Bitmap.Config.ARGB_8888);
                        //Utils.matToBitmap(frame, bmp);
                        //MediaStore.Images.Media.insertImage(getContentResolver(), bmp, "test", "test");
                        if (classifier != null) {
                            classifier.classifyMat(frame);
                            if (classifier.getResult() == "SPACE")
                                text += " ";
                            else if (classifier.getResult() == "BACKSPACE")
                                text = text.substring(0, text.length()-1);
                            else if (classifier.getResult() == "NOTHING")
                                text = text;
                            else
                                text += classifier.getResult();
                            tv.setText(text);
                            Log.d(TAG, "Guess: " + classifier.getResult() + " Probability: " + classifier.getProbability());
                        }
                    } catch (CvException e) {
                        Log.d(TAG, "" + e);
                    }
                }
                break;
        }
    }

}