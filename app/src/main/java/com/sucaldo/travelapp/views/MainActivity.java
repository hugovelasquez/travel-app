package com.sucaldo.travelapp.views;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.sucaldo.travelapp.R;

// implements "Navigation...Listener" needed for defining the drawer menu listeners
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    public NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // link xml layout to this activity
        setContentView(R.layout.activity_main);

        // Activate toolbar on top
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Assign xml object to drawer variable
        drawer = findViewById(R.id.drawer_layout);

        // Define Listener for drawer menu
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Add toggle capability to the drawer
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Show My Trips as a default
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new MyTripsFragment()).commit();
            // Show as selected in the drawer menu
            navigationView.setCheckedItem(R.id.nav_my_trips);
        }
        getSupportActionBar().setTitle(getString(R.string.navbar_my_trips));

    }

    // Definition of listeners for each option in drawer menu.
    // When option is selected, replace the xml object fragment_container with
    // the layout within the corresponding fragment class
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_my_trips:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new MyTripsFragment()).commit();
                getSupportActionBar().setTitle(getString(R.string.navbar_my_trips));
                break;
            case R.id.nav_add_trip:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new AddTripFragment()).commit();
                getSupportActionBar().setTitle(getString(R.string.navbar_add_trip));
                break;
            case R.id.nav_settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new SettingsFragment()).commit();
                getSupportActionBar().setTitle(getString(R.string.navbar_settings));
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // For displaying purposes only (close drawer if user presses back button)
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void goToMyTrips() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new MyTripsFragment()).commit();
        getSupportActionBar().setTitle(getString(R.string.navbar_my_trips));
        navigationView.setCheckedItem(R.id.nav_my_trips);
    }

    public void goToAddTrip() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new AddTripFragment()).commit();
        getSupportActionBar().setTitle(getString(R.string.navbar_add_trip));
    }

    public void passTripIdToOtherFragments(int tripId, String fragmentRequestKey) {
        Bundle result = new Bundle();
        // toString() cannot be used on a primitive, therefore use String.valueOf()
        result.putString(getString(R.string.fragment_key_trip_id), String.valueOf(tripId));
        getSupportFragmentManager().setFragmentResult(fragmentRequestKey, result);
    }

}

