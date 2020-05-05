package com.example.sign_lang_ml;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;
import java.io.InputStream;

public class RecognitionActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2, View.OnClickListener {

    private static final int BUTTON_SIZE = 80;
    private static final int CLASSIFY_INTERVAL = 20;
    private static final String CAPTURE_BUTTON = "captureButton.png";
    private static final String DEBUG_BUTTON = "debugButton.png";
    private static final String EDGE_BUTTON = "edgeButton.png";
    private static final String HELP_BUTTON = "helpButton.png";
    private static final String SAVE_BUTTON = "saveButton.png";
    private static final String TAG = "RecognitionActivity";

    private Classifier classifier;
    private Mat frame;
    private Mat mRGBA;
    private JavaCameraView openCvCameraView;

    private LinearLayout buttonLayout;
    private LinearLayout debugLayout;
    private TextView probTextView;
    private TextView resultTextView;
    private AlertDialog dialog;
//    private ImageButton debugButton;
//    private ImageButton helpButton;
//    private ImageButton captureButton;
//    private ImageButton saveButton;
//    private ImageButton edgeButton;

    private Boolean isDebug = false;
    private Boolean isEdge = false;
    private Boolean isSave = false;

    private String text = "";
    private int counter = 0;

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

    //set the assign vew id's to variables and set a few defaults
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        final FrameLayout layout = new FrameLayout(this);
//        layout.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
//                FrameLayout.LayoutParams.MATCH_PARENT));
//        setContentView(layout);
        setContentView(R.layout.activity_recognition);


        int mCameraIndex = 0;
        openCvCameraView = findViewById(R.id.my_camera_view);
//        openCvCameraView = new JavaCameraView(this, mCameraIndex);
        openCvCameraView.setCvCameraViewListener(RecognitionActivity.this);
        openCvCameraView.setVisibility(SurfaceView.VISIBLE);
//        openCvCameraView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
//                FrameLayout.LayoutParams.MATCH_PARENT));
//        layout.addView(openCvCameraView);

        buttonLayout = findViewById(R.id.buttonLayout);
        debugLayout = findViewById(R.id.debugLayout);

//        debugButton = findViewById(R.id.debugButton);
//        helpButton = findViewById(R.id.helpButton);
//        captureButton = findViewById(R.id.captureButton);
//        saveButton = findViewById(R.id.saveButton);
//        edgeButton = findViewById(R.id.edgeButton);


//        buttonLayout = new LinearLayout(this);
//        buttonLayout.setOrientation(LinearLayout.HORIZONTAL);
//        debugLayout = new LinearLayout(this);
//        debugLayout.setOrientation(LinearLayout.HORIZONTAL);
        debugLayout.setVisibility(View.INVISIBLE);
//
        debugLayout.addView(createButton(CAPTURE_BUTTON));
        debugLayout.addView(createButton(SAVE_BUTTON));
        debugLayout.addView(createButton(EDGE_BUTTON));
//        buttonLayout.addView(debugLayout);
        buttonLayout.addView(createButton(DEBUG_BUTTON));
        buttonLayout.addView(createButton(HELP_BUTTON));
//        buttonLayout.setPadding(25, 25, 25, 25);
//        buttonLayout.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
//                FrameLayout.LayoutParams.WRAP_CONTENT,
//                Gravity.BOTTOM + Gravity.END));
//        layout.addView(buttonLayout);


        resultTextView = findViewById(R.id.resultText);
        probTextView = findViewById(R.id.probabilityText);

//        resultTextView = new TextView(this);
//        resultTextView.setTextColor(Color.WHITE);
//        resultTextView.setTextSize(20f);
//        resultTextView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
//                FrameLayout.LayoutParams.WRAP_CONTENT,
//                Gravity.TOP + Gravity.CENTER_HORIZONTAL));
//        layout.addView(resultTextView);
//
//        probTextView = new TextView(this);
//        probTextView.setTextColor(Color.WHITE);
//        probTextView.setTextSize(20f);
//        probTextView.setPadding(0, 0, 0, BUTTON_SIZE + 150);
//        probTextView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
//                FrameLayout.LayoutParams.WRAP_CONTENT,
//                Gravity.BOTTOM + Gravity.CENTER_HORIZONTAL));
//        layout.addView(probTextView);

        dialog = new AlertDialog.Builder(this)
                .setTitle("Help")
                .setMessage("Make sure sign is fully inside green box.  For best results, use in a well lit area with an empty background.")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        int windowVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_FULLSCREEN;
                        getWindow().getDecorView().setSystemUiVisibility(windowVisibility);
                    }
                })
                .create();
    }

    //re-instantiate
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
        } catch (IOException e) {
            Log.e(TAG, "Failed to initialize classifier", e);
        }
    }

    //save resources when app is not in focus
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

    //destructor to free memory
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

    /* Every (20 default) frame(s) gets passed to the interpreter for evaluation*/
    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        float mh = mRGBA.height();
        float cw = (float) Resources.getSystem().getDisplayMetrics().widthPixels;
        float scale = mh / cw * 0.7f;

        mRGBA = inputFrame.rgba();
        frame = classifier.processMat(mRGBA);

        //check for the proper interval
        if (!isDebug) {
            if (counter == CLASSIFY_INTERVAL) {
                runInterpreter();
                counter = 0;
            } else {
                counter++;
            }
        }

        //set up a visible edge where the model will be looking
        Imgproc.rectangle(mRGBA,
                new Point(mRGBA.cols() / 2f - (mRGBA.cols() * scale / 2),
                        mRGBA.rows() / 2f - (mRGBA.cols() * scale / 2)),
                new Point(mRGBA.cols() / 2f + (mRGBA.cols() * scale / 2),
                        mRGBA.rows() / 2f + (mRGBA.cols() * scale / 2)),
                new Scalar(0, 255, 0), 1);
        if (isEdge) {
            mRGBA = classifier.debugMat(mRGBA);
        }

        System.gc();
        return mRGBA;
    }

    //set button actions
    @Override
    public void onClick(View view) {
        switch ((String) view.getTag()) {
            //The help button gives a brief description of how to use the app
            case HELP_BUTTON:
                dialog.show();
                TextView textView = dialog.findViewById(android.R.id.message);
                assert textView != null;
                textView.setScroller(new Scroller(this));
                textView.setVerticalScrollBarEnabled(true);
                textView.setMovementMethod(new ScrollingMovementMethod());
                break;
                //sets save state
            case SAVE_BUTTON:
                isSave = !isSave;
                setButton(SAVE_BUTTON, isSave);
                break;
                //sets if edge detection should be shown
            case EDGE_BUTTON:
                isEdge = !isEdge;
                setButton(EDGE_BUTTON, isEdge);
                break;
                //shows debug layout and menu
            case DEBUG_BUTTON:
                isDebug = !isDebug;
                if (isDebug) {
                    debugLayout.setVisibility(View.VISIBLE);
                    probTextView.setVisibility(View.VISIBLE);
                } else {
                    isSave = false;
                    setButton(SAVE_BUTTON, false);
                    isEdge = false;
                    setButton(EDGE_BUTTON, false);
                    debugLayout.setVisibility(View.INVISIBLE);
                    probTextView.setVisibility(View.INVISIBLE);
                }
                setButton(DEBUG_BUTTON, isDebug);
                break;
                //force probability on this frame
            case CAPTURE_BUTTON:
                try {
                    runInterpreter();
                    String t = "Probability: " + classifier.getProbability();
                    probTextView.setText(t);
                    if (isSave) {
                        Bitmap bmp = Bitmap.createBitmap(frame.cols(), frame.rows(), Bitmap.Config.ARGB_8888);
                        Utils.matToBitmap(frame, bmp);
                        MediaStore.Images.Media.insertImage(getContentResolver(), bmp,
                                classifier.getResult(), "" + classifier.getProbability());
                    }
                } catch (Exception e) {
                    Log.d(TAG, "" + e);
                }
                break;
        }
    }

    //create a new Image Button to be added to a view
    private ImageButton createButton(String tag) {
        ImageButton button = new ImageButton(this);
        button.setTag(tag);

        try {
            InputStream stream = getAssets().open(tag);
            Bitmap bmp = BitmapFactory.decodeStream(stream);
            button.setImageBitmap(Bitmap.createScaledBitmap(bmp, BUTTON_SIZE, BUTTON_SIZE, false));
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        }

        button.setPadding(25, 25, 25, 25);
        button.getBackground().setAlpha(0);
        button.setOnClickListener(this);
        return button;
    }

    //for buttons with toggle this changes the values /  image
    private void setButton(String tag, Boolean isOn) {
        String path = tag;
        if (isOn) {
            path = path.substring(0, path.length() - 4) + "On.png";
        }
        try {
            InputStream stream = getAssets().open(path);
            Bitmap bmp = BitmapFactory.decodeStream(stream);
            ImageButton button = buttonLayout.findViewWithTag(tag);
            button.setImageBitmap(Bitmap.createScaledBitmap(bmp, 80, 80, false));
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        }
    }

    //run predictions
    private void runInterpreter() {
        if (classifier != null) {
            classifier.classifyMat(frame);
            switch (classifier.getResult()) {
                case "SPACE":
                    text += " ";
                    break;
                case "BACKSPACE":
                    text = text.substring(0, text.length() - 1);
                    break;
                case "NOTHING":
                    break;
                default:
                    text += classifier.getResult();
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    resultTextView.setText(text);
                }
            });
            Log.d(TAG, "Guess: " + classifier.getResult() + " Probability: " + classifier.getProbability());
        }
    }
}