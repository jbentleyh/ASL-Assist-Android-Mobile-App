package com.example.sign_lang_ml;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.Objects;

public class LearningLettersActivity extends AppCompatActivity {

    ImageView letter;

    @Override
    //This view sets the individual signs for learning inside of the learning activity view
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learning_letters);

        Bundle bundle = getIntent().getExtras();
        letter = findViewById(R.id.letter_view);

        if (bundle != null) {
            if (Objects.requireNonNull(bundle.getString("LetterName")).equalsIgnoreCase("A")) {
                letter.setImageDrawable(ContextCompat.getDrawable(LearningLettersActivity.this, R.drawable.a));
            } else if (Objects.requireNonNull(bundle.getString("LetterName")).equalsIgnoreCase("B")) {
                letter.setImageDrawable(ContextCompat.getDrawable(LearningLettersActivity.this, R.drawable.b));
            } else if (Objects.requireNonNull(bundle.getString("LetterName")).equalsIgnoreCase("C")) {
                letter.setImageDrawable(ContextCompat.getDrawable(LearningLettersActivity.this, R.drawable.c));
            } else if (Objects.requireNonNull(bundle.getString("LetterName")).equalsIgnoreCase("D")) {
                letter.setImageDrawable(ContextCompat.getDrawable(LearningLettersActivity.this, R.drawable.d));
            } else if (Objects.requireNonNull(bundle.getString("LetterName")).equalsIgnoreCase("E")) {
                letter.setImageDrawable(ContextCompat.getDrawable(LearningLettersActivity.this, R.drawable.e));
            } else if (Objects.requireNonNull(bundle.getString("LetterName")).equalsIgnoreCase("F")) {
                letter.setImageDrawable(ContextCompat.getDrawable(LearningLettersActivity.this, R.drawable.f));
            } else if (Objects.requireNonNull(bundle.getString("LetterName")).equalsIgnoreCase("G")) {
                letter.setImageDrawable(ContextCompat.getDrawable(LearningLettersActivity.this, R.drawable.g));
            } else if (Objects.requireNonNull(bundle.getString("LetterName")).equalsIgnoreCase("H")) {
                letter.setImageDrawable(ContextCompat.getDrawable(LearningLettersActivity.this, R.drawable.h));
            } else if (Objects.requireNonNull(bundle.getString("LetterName")).equalsIgnoreCase("I")) {
                letter.setImageDrawable(ContextCompat.getDrawable(LearningLettersActivity.this, R.drawable.i));
            } else if (Objects.requireNonNull(bundle.getString("LetterName")).equalsIgnoreCase("J")) {
                letter.setImageDrawable(ContextCompat.getDrawable(LearningLettersActivity.this, R.drawable.j));
            } else if (Objects.requireNonNull(bundle.getString("LetterName")).equalsIgnoreCase("K")) {
                letter.setImageDrawable(ContextCompat.getDrawable(LearningLettersActivity.this, R.drawable.k));
            } else if (Objects.requireNonNull(bundle.getString("LetterName")).equalsIgnoreCase("L")) {
                letter.setImageDrawable(ContextCompat.getDrawable(LearningLettersActivity.this, R.drawable.l));
            } else if (Objects.requireNonNull(bundle.getString("LetterName")).equalsIgnoreCase("M")) {
                letter.setImageDrawable(ContextCompat.getDrawable(LearningLettersActivity.this, R.drawable.m));
            } else if (Objects.requireNonNull(bundle.getString("LetterName")).equalsIgnoreCase("N")) {
                letter.setImageDrawable(ContextCompat.getDrawable(LearningLettersActivity.this, R.drawable.n));
            } else if (Objects.requireNonNull(bundle.getString("LetterName")).equalsIgnoreCase("O")) {
                letter.setImageDrawable(ContextCompat.getDrawable(LearningLettersActivity.this, R.drawable.o));
            } else if (Objects.requireNonNull(bundle.getString("LetterName")).equalsIgnoreCase("P")) {
                letter.setImageDrawable(ContextCompat.getDrawable(LearningLettersActivity.this, R.drawable.p));
            } else if (Objects.requireNonNull(bundle.getString("LetterName")).equalsIgnoreCase("Q")) {
                letter.setImageDrawable(ContextCompat.getDrawable(LearningLettersActivity.this, R.drawable.q));
            } else if (Objects.requireNonNull(bundle.getString("LetterName")).equalsIgnoreCase("R")) {
                letter.setImageDrawable(ContextCompat.getDrawable(LearningLettersActivity.this, R.drawable.r));
            } else if (Objects.requireNonNull(bundle.getString("LetterName")).equalsIgnoreCase("S")) {
                letter.setImageDrawable(ContextCompat.getDrawable(LearningLettersActivity.this, R.drawable.s));
            } else if (Objects.requireNonNull(bundle.getString("LetterName")).equalsIgnoreCase("T")) {
                letter.setImageDrawable(ContextCompat.getDrawable(LearningLettersActivity.this, R.drawable.t));
            } else if (Objects.requireNonNull(bundle.getString("LetterName")).equalsIgnoreCase("U")) {
                letter.setImageDrawable(ContextCompat.getDrawable(LearningLettersActivity.this, R.drawable.u));
            } else if (Objects.requireNonNull(bundle.getString("LetterName")).equalsIgnoreCase("V")) {
                letter.setImageDrawable(ContextCompat.getDrawable(LearningLettersActivity.this, R.drawable.v));
            } else if (Objects.requireNonNull(bundle.getString("LetterName")).equalsIgnoreCase("W")) {
                letter.setImageDrawable(ContextCompat.getDrawable(LearningLettersActivity.this, R.drawable.w));
            } else if (Objects.requireNonNull(bundle.getString("LetterName")).equalsIgnoreCase("X")) {
                letter.setImageDrawable(ContextCompat.getDrawable(LearningLettersActivity.this, R.drawable.x));
            } else if (Objects.requireNonNull(bundle.getString("LetterName")).equalsIgnoreCase("Y")) {
                letter.setImageDrawable(ContextCompat.getDrawable(LearningLettersActivity.this, R.drawable.y));
            } else if (Objects.requireNonNull(bundle.getString("LetterName")).equalsIgnoreCase("Z")) {
                letter.setImageDrawable(ContextCompat.getDrawable(LearningLettersActivity.this, R.drawable.z));
            }
        }

    }
}
