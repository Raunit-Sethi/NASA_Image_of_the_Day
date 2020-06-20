package com.example.finalproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class NewsDetails extends AppCompatActivity {
    TextView header;
    TextView headline;
    TextView description;
    TextView link;
    TextView date;
    EditText comment;
    Button save;
    SharedPreferences sharedPreferences;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsdetails);
        Intent intent = getIntent();
        String headline1 = intent.getStringExtra("headline");
        String description1 = intent.getStringExtra("description");
        String link1 = intent.getStringExtra("link");
        String date1 = intent.getStringExtra("date");
        header = findViewById(R.id.header);
        headline = findViewById(R.id.headlineNa);
        description = findViewById(R.id.description);
        link = findViewById(R.id.link);
        comment = findViewById(R.id.comment);
        save = findViewById(R.id.save);
        date = findViewById(R.id.date);
        headline.setText(headline1);
        description.setText(description1);
        link.setText(link1);
        date.setText(date1);
        sharedPreferences = getSharedPreferences("SharedPreferenceFile", Context.MODE_PRIVATE);
        String savedString = sharedPreferences.getString("comment", "");
        comment.setText(savedString);

        save.setOnClickListener(e -> {
            String message = comment.getText().toString();
            Intent intent1 = new Intent(this, SaveddbArticles.class);
            intent1.putExtra("comment", message);
            intent1.putExtra("headline", headline1);
            intent1.putExtra("description", description1);
            intent1.putExtra("link", link1);
            intent1.putExtra("date", date1);
            intent1.setAction("2");
            startActivity(intent1);
        });

    }
    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("comment", comment.getText().toString());
        editor.commit();
    }

}
