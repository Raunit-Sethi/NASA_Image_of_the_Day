package com.example.finalproject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Articles extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ListView articleList;
    ProgressBar progressBar;
    ArrayList<NewsMessage> newsList = new ArrayList<>();
    TextView headlines;
    Button saveFvrt;
    MyOwnAdapter adapter;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articles);
        Toolbar tBar = findViewById(R.id.toolbar);
        //This loads the toolbar, which calls onCreateOptionsMenu below:
        setSupportActionBar(tBar);
        //For NavigationDrawer:
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer, tBar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        articleList = findViewById(R.id.articleList);
        adapter = new MyOwnAdapter(this, newsList);
        new MyQuery().execute();
        articleList.setAdapter(adapter);
        progressBar.setVisibility(View.INVISIBLE);
        articleList.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            Intent intent = new Intent(this , NewsDetails.class);
            intent.putExtra("headline", newsList.get(position).getHeadline());
            intent.putExtra("link", newsList.get(position).getLink());
            intent.putExtra("description", newsList.get(position).getDescription());
            intent.putExtra("date", newsList.get(position).getDate());
            startActivityForResult(intent,200);
        });
        saveFvrt = findViewById(R.id.savefvrt);
        saveFvrt.setOnClickListener(e -> {
            Intent intent = new Intent(Articles.this, SaveddbArticles.class);
            startActivity(intent);
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_menu, menu);


	    /* slide 15 material:
	    MenuItem searchItem = menu.findItem(R.id.search_item);
        SearchView sView = (SearchView)searchItem.getActionView();
        sView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }  });
	    */

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String message = null;
        //Look at your menu XML file. Put a case for every id in that file:
        if (item.getItemId() == R.id.help){
                message = getString(R.string.about1) + "\n" + getString(R.string.about2)+ "\n" + getString(R.string.about3);
        }
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        return true;
    }

    // Needed for the OnNavigationItemSelected interface:
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        String message = "";
        if(item.getItemId() == R.id.help){
                message = getString(R.string.instr1) +"\n" + getString(R.string.instr2)+ "\n" +getString(R.string.instr3) + "\n" + getString(R.string.instr4);
                AlertDialog.Builder alert = new AlertDialog.Builder(
                        Articles.this);
                alert.setTitle("Helping instruction");
                alert.setMessage(message);

                alert.setPositiveButton("ok", (DialogInterface dialog, int which) -> {
                    dialog.dismiss();
                });
                alert.show();
                return true;
        }

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    private class MyQuery extends AsyncTask<String, Integer , String> {
        String title, description, link, date;
        long id;
        @Override
        protected String doInBackground(String... params) {
            try {
                String myUrl = "http://feeds.bbci.co.uk/news/world/us_and_canada/rss.xml";
                URL url = new URL(myUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                InputStream inStream = conn.getInputStream();
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(inStream, "UTF-8");
                while (xpp.getEventType() != XmlPullParser.END_DOCUMENT)
                {
                    if(xpp.getEventType() == XmlPullParser.START_TAG){
                            String name = xpp.getName();
                            if (name.equals("title")) {
                                xpp.next();
                                String tempTitle = xpp.getText();
                                if (!tempTitle.contains("BBC News - US & Canada")) {
                                    title = tempTitle;
                                    publishProgress(30);
                                    Log.e("Title is:", title);
                                }
                            }
                            else if (name.equals("link")) {
                                xpp.next();
                                String tempLink = xpp.getText();
                                if(!tempLink.equals("https://www.bbc.co.uk/news/")){
                                    link = tempLink;
                                    Log.e("Link is:", link);
                                    publishProgress(30);
                                }
                            }
                            else if (name.equals("description")) {
                                xpp.next();
                                String tempDescription = xpp.getText();
                                if(!tempDescription.contains("BBC News - US & Canada")){
                                    description = tempDescription;
                                    Log.e("Description is:", description);
                                    publishProgress(30);
                                }
                            }
                            else if (name.equals("pubDate")) {
                                xpp.next();
                                date = xpp.getText();
                                publishProgress(30);
                                Log.e("Publish Date is: ", date);
                                publishProgress(30);
                            }
                            if(title!=null && description!=null && date!=null) {
                                newsList.add(new NewsMessage(id, title, link ,description, date));
                                title = null;
                                link = null;
                                description = null;
                                date = null;

                            }
                    }
                    xpp.next();//advance to next XML event
                }
                publishProgress(100);
            } catch (Exception e) {
                Log.e("Exception", e.getMessage());
            }
            return "finished";
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(values[0]);
        }
        @Override
        protected void onPostExecute(String s) {
            adapter.notifyDataSetChanged();
            progressBar.setVisibility(View.INVISIBLE);
        }
    }


    class MyOwnAdapter extends BaseAdapter {
    private static final String TAG = "MyOwnAdapter";
    private Context context;
    ArrayList<NewsMessage> newsList = new ArrayList<>();
    int resource;

    public MyOwnAdapter(@NonNull Context context, @NonNull ArrayList<NewsMessage> objects) {
        //super(context, resource, objects);
        this.context = context;
        this.newsList = objects;
    }

    @Override
    public int getCount() {
        return newsList.size();
    }

    @Override
    public NewsMessage getItem(int position) {
        return newsList.get(position);
    }

        @Override
        public long getItemId(int position) {
            return 0;
        }

         public View getView(int position, View convertView, ViewGroup parent){
             LayoutInflater inflater = Articles.this.getLayoutInflater();
             View result = inflater.inflate(R.layout.activity_headlines, null);
             headlines = (TextView)result.findViewById(R.id.headlines);
             headlines.setText(getItem(position).getHeadline()); // get the string at position
             return result;
         }
     }}
