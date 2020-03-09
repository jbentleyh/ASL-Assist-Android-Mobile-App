package com.example.sign_lang_ml;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toolbar;

public class LearningLettersActivity extends AppCompatActivity {

    ImageView letter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learning_letters);

        Bundle bundle = getIntent().getExtras();
        letter = (ImageView) findViewById(R.id.letter_view);

        if(bundle != null) {
            if(bundle.getString("LetterName").equalsIgnoreCase("A")){
                letter.setImageDrawable(ContextCompat.getDrawable(LearningLettersActivity.this, R.drawable.a));
            } else if(bundle.getString("LetterName").equalsIgnoreCase("B")){
                letter.setImageDrawable(ContextCompat.getDrawable(LearningLettersActivity.this, R.drawable.b));
            } else if(bundle.getString("LetterName").equalsIgnoreCase("C")){
                letter.setImageDrawable(ContextCompat.getDrawable(LearningLettersActivity.this, R.drawable.c));
            } else if(bundle.getString("LetterName").equalsIgnoreCase("D")){
                letter.setImageDrawable(ContextCompat.getDrawable(LearningLettersActivity.this, R.drawable.d));
            } else if(bundle.getString("LetterName").equalsIgnoreCase("E")){
                letter.setImageDrawable(ContextCompat.getDrawable(LearningLettersActivity.this, R.drawable.e));
            } else if(bundle.getString("LetterName").equalsIgnoreCase("F")){
                letter.setImageDrawable(ContextCompat.getDrawable(LearningLettersActivity.this, R.drawable.f));
            } else if(bundle.getString("LetterName").equalsIgnoreCase("G")){
                letter.setImageDrawable(ContextCompat.getDrawable(LearningLettersActivity.this, R.drawable.g));
            } else if(bundle.getString("LetterName").equalsIgnoreCase("H")){
                letter.setImageDrawable(ContextCompat.getDrawable(LearningLettersActivity.this, R.drawable.h));
            } else if(bundle.getString("LetterName").equalsIgnoreCase("I")){
                letter.setImageDrawable(ContextCompat.getDrawable(LearningLettersActivity.this, R.drawable.i));
            } else if(bundle.getString("LetterName").equalsIgnoreCase("J")){
                letter.setImageDrawable(ContextCompat.getDrawable(LearningLettersActivity.this, R.drawable.j));
            } else if(bundle.getString("LetterName").equalsIgnoreCase("K")){
                letter.setImageDrawable(ContextCompat.getDrawable(LearningLettersActivity.this, R.drawable.k));
            } else if(bundle.getString("LetterName").equalsIgnoreCase("L")){
                letter.setImageDrawable(ContextCompat.getDrawable(LearningLettersActivity.this, R.drawable.l));
            } else if(bundle.getString("LetterName").equalsIgnoreCase("M")){
                letter.setImageDrawable(ContextCompat.getDrawable(LearningLettersActivity.this, R.drawable.m));
            } else if(bundle.getString("LetterName").equalsIgnoreCase("N")){
                letter.setImageDrawable(ContextCompat.getDrawable(LearningLettersActivity.this, R.drawable.n));
            } else if(bundle.getString("LetterName").equalsIgnoreCase("O")){
                letter.setImageDrawable(ContextCompat.getDrawable(LearningLettersActivity.this, R.drawable.o));
            } else if(bundle.getString("LetterName").equalsIgnoreCase("P")){
                letter.setImageDrawable(ContextCompat.getDrawable(LearningLettersActivity.this, R.drawable.p));
            } else if(bundle.getString("LetterName").equalsIgnoreCase("Q")){
                letter.setImageDrawable(ContextCompat.getDrawable(LearningLettersActivity.this, R.drawable.q));
            } else if(bundle.getString("LetterName").equalsIgnoreCase("R")){
                letter.setImageDrawable(ContextCompat.getDrawable(LearningLettersActivity.this, R.drawable.r));
            } else if(bundle.getString("LetterName").equalsIgnoreCase("S")){
                letter.setImageDrawable(ContextCompat.getDrawable(LearningLettersActivity.this, R.drawable.s));
            } else if(bundle.getString("LetterName").equalsIgnoreCase("T")){
                letter.setImageDrawable(ContextCompat.getDrawable(LearningLettersActivity.this, R.drawable.t));
            } else if(bundle.getString("LetterName").equalsIgnoreCase("U")){
                letter.setImageDrawable(ContextCompat.getDrawable(LearningLettersActivity.this, R.drawable.u));
            } else if(bundle.getString("LetterName").equalsIgnoreCase("V")){
                letter.setImageDrawable(ContextCompat.getDrawable(LearningLettersActivity.this, R.drawable.v));
            } else if(bundle.getString("LetterName").equalsIgnoreCase("W")){
                letter.setImageDrawable(ContextCompat.getDrawable(LearningLettersActivity.this, R.drawable.w));
            } else if(bundle.getString("LetterName").equalsIgnoreCase("X")){
                letter.setImageDrawable(ContextCompat.getDrawable(LearningLettersActivity.this, R.drawable.x));
            } else if(bundle.getString("LetterName").equalsIgnoreCase("Y")){
                letter.setImageDrawable(ContextCompat.getDrawable(LearningLettersActivity.this, R.drawable.y));
            } else if(bundle.getString("LetterName").equalsIgnoreCase("Z")){
                letter.setImageDrawable(ContextCompat.getDrawable(LearningLettersActivity.this, R.drawable.z));
            }
        }

    }
}
