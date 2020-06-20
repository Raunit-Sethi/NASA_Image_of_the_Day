package com.example.finalproject;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ImageSearch extends AppCompatActivity {

    static final String API = "https://api.nasa.gov/planetary/apod?api_key=NpnoI99OhQPB7IHcGtN3Q9EdvhNg9sXghgpka6IA&date=";
    ImageView image ;
    TextView dateText , urlText , hdUrlText ;
    String innerDate, url, hdUrl;
    Button save,saved,help ;
    Bitmap map;
    SQLiteDatabase db;
    ContentValues values;
    Snackbar snack ;
    Cursor c;
    ProgressBar loading;
    ImageDatabaseHelper helper;
    AlertDialog.Builder alert;
    byte[] bArray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_search);
        helper = new ImageDatabaseHelper(this);
        db = helper.getWritableDatabase();
        values = new ContentValues();
        image = findViewById(R.id.image);

        loading = findViewById(R.id.loading);
        loading.setVisibility(View.VISIBLE);
        dateText = findViewById(R.id.date_image);
        urlText = findViewById(R.id.url);
        hdUrlText = findViewById(R.id.hd_url);
        String date = getIntent().getStringExtra("date");
        dateText.setText(date);
        ImageQuery query = new ImageQuery();
        query.execute(API+date);
//        b.setText(date);
        save = findViewById(R.id.save_button);
        save.setOnClickListener( v -> {
            alert = new AlertDialog.Builder(this);
            alert.setTitle("Save Image");
            alert.setMessage("Do you want to save this image?");
            alert.setPositiveButton("Yes", (DialogInterface dialog,int which) -> {
                values.put(helper.URL,url);
                values.put(helper.HD_URL,hdUrl);
                values.put(helper.DATE,date);
                values.put(helper.IMAGE,bArray);
                db.insert(helper.TABLE,null,values);
                snack.make(save,"Image has been saved",Snackbar.LENGTH_SHORT).show();
            });
            alert.setNegativeButton("No",(DialogInterface dialog,int which) -> {

            });
            alert.show();
        });

        saved = findViewById(R.id.saved_images_button);
        saved.setOnClickListener( v -> {
            Intent i = new Intent(ImageSearch.this,SavedImages.class);
            startActivity(i);
        });


        help = findViewById(R.id.help);
        help.setOnClickListener(v -> {
            alert = new AlertDialog.Builder(this);
            alert.setTitle("Help");
            alert.setMessage("The image along with the date can be viewed.\nImage can be opened in the browser in normal or HD resolution.\nSave Image button saves the image to your Saved Images List.\nYou can view your saved images list by clicking \"GO TO SAVED IMAGES\".");
            alert.setNeutralButton("Got It", (DialogInterface dialog,int which) -> {

            });
            alert.show();
        });
    }


    private class ImageQuery extends AsyncTask<String,Integer,String> {


        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url1 = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url1.openConnection();
                InputStream stream = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(stream);
                BufferedReader buffer = new BufferedReader(reader);
                StringBuilder stringBuilder = new StringBuilder();
                String line ;
                while((line = buffer.readLine())!=null){
                    stringBuilder.append(line+"\n");
                }
                String result = stringBuilder.toString();
                JSONObject json = new JSONObject((result));
                innerDate = json.getString("date");
                publishProgress(25);
                hdUrl = json.getString("hdurl");
                publishProgress(50);
                url = json.getString("url");
                publishProgress(75);

                URL url2 = new URL(url);
                HttpURLConnection imageConnection = (HttpURLConnection) url2.openConnection();
                imageConnection.connect();
                int responseCode = connection.getResponseCode();
                if(responseCode == 200){
                    map = BitmapFactory.decodeStream(imageConnection.getInputStream());
                }
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                map.compress(Bitmap.CompressFormat.PNG, 100, bos);
                bArray = bos.toByteArray();
                publishProgress(100);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return "done";
        }

        @Override
        protected void onPostExecute(String s) {
            dateText.setText(innerDate);
            urlText.setText(url);
            hdUrlText.setText(hdUrl);
            image.setImageBitmap(map);
            loading.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            loading.setVisibility(View.VISIBLE);
            loading.setProgress(values[0]);
        }
    }

}
