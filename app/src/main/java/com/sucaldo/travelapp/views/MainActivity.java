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
import com.sucaldo.travelapp.views.charts.CountriesCloudFragment;
import com.sucaldo.travelapp.views.charts.KmsAreaChartFragment;
import com.sucaldo.travelapp.views.charts.KmsBubbleChartFragment;
import com.sucaldo.travelapp.views.charts.Top10PlacesFragment;

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

    // When an option is selected, replace the xml object fragment_container with
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
            case R.id.nav_statistics:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new TripStatisticsFragment()).commit();
                getSupportActionBar().setTitle(getString(R.string.navbar_statistics));
                break;
            case R.id.nav_world_map:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new WorldMapFragment()).commit();
                getSupportActionBar().setTitle(getString(R.string.navbar_world_map));
                break;
            case R.id.nav_city_coordinates:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new CityCoordinatesFragment()).commit();
                getSupportActionBar().setTitle(getString(R.string.navbar_city_coordinates));
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

    public void goToStatistics() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new TripStatisticsFragment()).commit();
        getSupportActionBar().setTitle(getString(R.string.navbar_statistics));
    }

    public void goToTop10CitiesChart() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new Top10PlacesFragment()).commit();
        getSupportActionBar().setTitle(getString(R.string.title_top_10_places));
    }

    public void goToCountriesCloudChart() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new CountriesCloudFragment()).commit();
        getSupportActionBar().setTitle(getString(R.string.title_countries_cloud));
    }

    public void goToKmsAreaChart() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new KmsAreaChartFragment()).commit();
        getSupportActionBar().setTitle(getString(R.string.title_kms_area_chart));
    }

    public void goToKmsBubbleChart() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new KmsBubbleChartFragment()).commit();
        getSupportActionBar().setTitle(getString(R.string.title_kms_bubble_chart));
    }

    public void passTripIdToOtherFragments(int tripId, String fragmentRequestKey) {
        Bundle result = new Bundle();
        // toString() cannot be used on a primitive, therefore use String.valueOf()
        result.putString(getString(R.string.fragment_key_trip_id), String.valueOf(tripId));
        getSupportFragmentManager().setFragmentResult(fragmentRequestKey, result);
    }

}

