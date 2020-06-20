package com.example.finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.snackbar.Snackbar;
import java.util.ArrayList;
import static com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_LONG;
import static com.google.android.material.snackbar.Snackbar.make;


public class SaveddbArticles extends AppCompatActivity {
    ListView view;
    TextView header;
    ArrayList<NewsMessage> newsList = new ArrayList<>();
    MyDatabaseOpenHelper dbOpener = new MyDatabaseOpenHelper(this);
    TextView headlines;
    TextView description;
    TextView commentR;
    TextView link;
    TextView date;
    public static final String F_HEADLINE= "headline";
    public static final String F_ID = "id";
    MyOwnAdapter adapter;
    SQLiteDatabase db;
    long Id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saveddbarticles);
        header = findViewById(R.id.header);
        view = findViewById(R.id.savedList);
        dbOpener = new MyDatabaseOpenHelper(this);
        db = dbOpener.getWritableDatabase();
        String comment = getIntent().getStringExtra("comment");
        commentR = findViewById(R.id.commentR);
        if(comment == null){
            commentR.setText(getString(R.string.rsn)+" its uniqueness");
        }else{
        commentR.setText(getString(R.string.rsn)+" "+comment);}
        String headline1 = getIntent().getStringExtra("headline");
        String description1 = getIntent().getStringExtra("description");
        String link1 = getIntent().getStringExtra("link");
        String date1 = getIntent().getStringExtra("date");
       if(getIntent().getAction() == "2"){
            ContentValues newRowValues = new ContentValues();

            //put string name in the NAME column:
            newRowValues.put(dbOpener.COL_HEADLINE, headline1);
            //put string email in the EMAIL column:
            newRowValues.put(dbOpener.COL_DESCRIPTION, description1);
            //insert in the database:
            newRowValues.put(dbOpener.COL_LINK, link1);

           newRowValues.put(dbOpener.COL_DATE, date1);

            Id = db.insert(dbOpener.TABLE_NAME, null, newRowValues);
            Toast.makeText(this,getString(R.string.Toast1),Toast.LENGTH_SHORT).show();
        }
       else{
           Snackbar snackbar = make(view, getString(R.string.snackbar1), LENGTH_LONG);
           snackbar.show();
       }
        loadDataFromDatabase();
        view.setOnItemLongClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            AlertDialog.Builder alert = new AlertDialog.Builder(
                    SaveddbArticles.this);
            alert.setTitle("Delete");
            alert.setMessage("Do you want delete this item?");
            alert.setPositiveButton("Yes", (DialogInterface dialog, int which) -> {
                db.delete(dbOpener.TABLE_NAME, dbOpener.COL_ID + "=?", new String[]{Long.toString(newsList.get(position).getId())});
                newsList.remove(position);
                adapter.notifyDataSetChanged();
            });
            alert.setNegativeButton("No", (DialogInterface dialog, int which) -> {
                dialog.dismiss();
            });
            alert.setMessage("The selected row is:" + position + "\nthe  database id:" + newsList.get(position).getId());
            alert.show();
            return true;

        });
        boolean isTablet = findViewById(R.id.fragmentLocation) != null;
        view.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            Bundle dataToPass = new Bundle();
            dataToPass.putString(F_HEADLINE, newsList.get(position).getHeadline());
            dataToPass.putLong(F_ID, newsList.get(position).getId());
            dataToPass.putBoolean("isTablet",isTablet);
            if(isTablet)
            {
                DetailsFragment dFragment = new DetailsFragment(); //add a DetailFragment
                dFragment.setArguments( dataToPass ); //pass it a bundle for information
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentLocation, dFragment) //Add the fragment in FrameLayout
                        .commit(); //actually load the fragment.
            }
            else {
                Intent nextActivity = new Intent(this, EmptyActivity.class);
                nextActivity.putExtras(dataToPass); //send data to next activity
                startActivity(nextActivity); //make the transition
            }
        });
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
            LayoutInflater inflater = SaveddbArticles.this.getLayoutInflater();
            View result = inflater.inflate(R.layout.showcasenews, null);
            headlines = result.findViewById(R.id.sHeadline);
            headlines.setText(getItem(position).getHeadline());
            description = result.findViewById(R.id.sDescription);
            description.setText(getItem(position).getDescription());
            link = result.findViewById(R.id.sLink);
            link.setText(getItem(position).getLink());
            date = result.findViewById(R.id.sDate);
            date.setText(getItem(position).getDate());// get the string at position
            return result;
        }
    }
    public void loadDataFromDatabase() {
        String[] columns = {dbOpener.COL_ID, dbOpener.COL_HEADLINE, dbOpener.COL_LINK, dbOpener.COL_DESCRIPTION,dbOpener.COL_DATE};
        db = dbOpener.getWritableDatabase();
        Cursor results = db.query(false, dbOpener.TABLE_NAME, columns, null, null, null, null, null, null);
        while (results.moveToNext()) {
            int HeadlineColIndex = results.getColumnIndex(dbOpener.COL_HEADLINE);
            int descriptionColIndex = results.getColumnIndex(dbOpener.COL_DESCRIPTION);
            int linkColIndex = results.getColumnIndex(dbOpener.COL_LINK);
            int dateColIndex = results.getColumnIndex(dbOpener.COL_DATE);
            int idColIndex = results.getColumnIndex(dbOpener.COL_ID);
            String headline = results.getString(HeadlineColIndex);
            String description = results.getString(descriptionColIndex);
            String link = results.getString(linkColIndex);
            String date = results.getString(dateColIndex);
            long id = results.getLong(idColIndex);
            newsList.add(new NewsMessage(id, headline, link, description,date));
            adapter = new MyOwnAdapter(this, newsList);
            view.setAdapter(adapter);
        }
    }

}
