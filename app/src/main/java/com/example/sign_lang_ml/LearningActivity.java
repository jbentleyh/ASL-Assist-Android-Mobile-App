package com.example.sign_lang_ml;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class LearningActivity extends AppCompatActivity {

    ListView listview;

    //Here we use our layout view to set a container to display the learning information
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learning);

        listview = findViewById(R.id.list_view);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(LearningActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.letters));

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(LearningActivity.this, LearningLettersActivity.class);
                intent.putExtra("LetterName", listview.getItemAtPosition(i).toString());
                startActivity(intent);
            }
        });

        listview.setAdapter(adapter);

    }
}
