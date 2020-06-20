package com.example.finalproject;
        import androidx.annotation.NonNull;
        import androidx.appcompat.app.ActionBarDrawerToggle;
        import androidx.appcompat.app.AppCompatActivity;
        import androidx.appcompat.widget.Toolbar;
        import androidx.core.view.GravityCompat;
        import androidx.drawerlayout.widget.DrawerLayout;

        import android.content.Intent;
        import android.os.Bundle;
        import android.view.MenuItem;
        import android.widget.Button;
        import android.widget.Toast;

        import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        Button btn1 = findViewById(R.id.bbc);
        btn1.setOnClickListener(click -> {
            Intent nextActivity = new Intent(this, Articles.class);
            startActivityForResult(nextActivity, 100);
        });
        Button b = findViewById(R.id.button);
        b.setOnClickListener(v -> {
            Intent i = new Intent(this,DatePicker.class);
            startActivity(i);
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        String message = null;
        switch(item.getItemId())
        {
            case R.id.NasaImage:
                message="you clicked on Nasa image of the day";
                Intent nasaimage = new Intent(this, DatePicker.class);
                startActivity(nasaimage);
                break;
            case R.id.BBC:
                message="you clicked on BBC News reader";
                Intent nextActivity = new Intent(this, Articles.class);
                startActivityForResult(nextActivity, 100);
                break;

        }
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);

        Toast.makeText(this, "NavigationDrawer: " + message, Toast.LENGTH_LONG).show();
        return false;
    }
}



