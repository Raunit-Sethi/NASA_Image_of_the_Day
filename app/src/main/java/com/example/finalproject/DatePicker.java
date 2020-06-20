package com.example.finalproject;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class DatePicker extends AppCompatActivity {

    EditText date ;
    int day,month,year;
    DatePickerDialog datePicker;
    SharedPreferences preferences;
    Calendar calendar ;
    Button go,saved ;
    String text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_picker);
        date = findViewById(R.id.date_picker);

        preferences = getApplicationContext().getSharedPreferences("prefID",0);
        text = preferences.getString("date","");
        date.setText(text);
        date.setOnClickListener(v -> {
            calendar = Calendar.getInstance();
            day = calendar.get(Calendar.DAY_OF_MONTH);
            month = calendar.get(Calendar.MONTH);
            year = calendar.get(Calendar.YEAR);

            datePicker = new DatePickerDialog(DatePicker.this,(view, year, monthOfYear, dayOfMonth) -> {

                if(monthOfYear+1<10 && dayOfMonth < 10){
                    text = year + "-0" + (monthOfYear + 1) + "-0" + dayOfMonth;}
                else if(monthOfYear+1<10){
                    text = year + "-0" + (monthOfYear + 1) + "-" + dayOfMonth;}
                else if(dayOfMonth < 10){
                    text = year + "-" + (monthOfYear + 1) + "-0" + dayOfMonth;}
                else
                    text = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                date.setText(text);

                Toast.makeText(getApplicationContext(), "Date selected", Toast.LENGTH_LONG).show();


            }, year, month, day);
            datePicker.show();
        });

        go = findViewById(R.id.go_button);
        go.setOnClickListener(v -> {
            Intent nextActivity = new Intent(this,ImageSearch.class);
            nextActivity.putExtra("date",text);
            startActivity(nextActivity);
        });

        saved = findViewById(R.id.picker_button);
        saved.setOnClickListener(v -> {
            Intent i = new Intent(this,SavedImages.class);
            startActivity(i);
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        EditText date = findViewById(R.id.date_picker);
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("prefID",0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("date",date.getText().toString());
        editor.apply();
    }
}
