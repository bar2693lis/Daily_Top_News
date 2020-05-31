package com.barlis.dailytopnews;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.barlis.dailytopnews.Adapter.ViewPagerAdapter;
import com.barlis.dailytopnews.Common.Common;
import com.barlis.dailytopnews.Fragments.EntertainmentFragment;
import com.barlis.dailytopnews.Fragments.GeneralFragment;
import com.barlis.dailytopnews.Fragments.ScienceFragment;
import com.barlis.dailytopnews.Fragments.SettingsFragment;
import com.barlis.dailytopnews.Fragments.SportsFragment;
import com.barlis.dailytopnews.Fragments.TechnologyFragment;
import com.barlis.dailytopnews.Fragments.TodayWeatherFragment;
import com.barlis.dailytopnews.Fragments.WeatherForecastFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewPagerAdapter viewPagerAdapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private CoordinatorLayout coordinatorLayout;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;
    private Toolbar toolbar;

    final String WEATHER_FRAGMENT_TAG = "weather_fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Tabbed Activity
        tabLayout = findViewById(R.id.tabLayout);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        //ToolBar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar();

        coordinatorLayout = findViewById(R.id.root_view);

        //Request permission
        Dexter.withContext(this).withPermissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION).withListener(new MultiplePermissionsListener()
        {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport)
            {
                if(multiplePermissionsReport.areAllPermissionsGranted())
                {
                    buildLocationRequest();
                    buildLocationCallBack();

                    if(ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                    {
                        return;
                    }
                    fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);
                    fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken)
            {
                Snackbar.make(coordinatorLayout, "Permission denied", Snackbar.LENGTH_LONG).show();
            }
        }).check();
    }

    private void buildLocationCallBack()
    {
        locationCallback = new LocationCallback()
        {
            @Override
            public void onLocationResult(LocationResult locationResult)
            {
                super.onLocationResult(locationResult);

                Common.current_location = locationResult.getLastLocation();

                // Tabbed Activity
                viewPager = findViewById(R.id.viewPager);
                viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

                tabLayout = findViewById(R.id.tabLayout);

                // ViewPager Adapter
                viewPagerAdapter.AddFragment(new GeneralFragment(), "Top News");
                viewPagerAdapter.AddFragment(new TechnologyFragment(), "Technology");
                viewPagerAdapter.AddFragment(new SportsFragment(), "Sports");
                viewPagerAdapter.AddFragment(new ScienceFragment(), "Science");
                viewPagerAdapter.AddFragment(new EntertainmentFragment(), "Entertainment");

                viewPager.setAdapter(viewPagerAdapter);
                tabLayout.setupWithViewPager(viewPager);
                tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

                tabLayout.getTabAt(0).setText("Top News");
                tabLayout.getTabAt(1).setText("Technology");
                tabLayout.getTabAt(2).setText("Sports");
                tabLayout.getTabAt(3).setText("Science");
                tabLayout.getTabAt(4).setText("Entertainment");

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.add(R.id.weatherRoot, new TodayWeatherFragment(), WEATHER_FRAGMENT_TAG);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        };
    }

    private void buildLocationRequest()
    {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setSmallestDisplacement(10.0f);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_settings:
            {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.add(R.id.root_view, new SettingsFragment(), WEATHER_FRAGMENT_TAG);
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            }
            case R.id.action_forecast:
            {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.add(R.id.root_view, new WeatherForecastFragment(), WEATHER_FRAGMENT_TAG);
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            }

        }
        return super.onOptionsItemSelected(item);
    }
}
