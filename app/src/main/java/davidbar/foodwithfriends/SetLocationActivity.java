package davidbar.foodwithfriends;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import java.util.HashMap;

public class SetLocationActivity extends AppCompatActivity {

    private static final String TAG = "SetLocation";

    private double latitude = 34.06018;
    private double longitude = -118.41835;

    private boolean locationSet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_location);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        //FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        //fab.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View view) {
        //        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
        //                .setAction("Action", null).show();
        //    }
        //});
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /** Called when the user clicks the Submit button */
    /** Address String is converted to GPS coordinates */
    public void clickSubmitButton(View view) {
        Log.d(TAG, "CLICK!!");
    }

    /** Called when the user clicks the Submit button */
    /** Address String is converted to GPS coordinates */
    public void clickGPSButton(View view) {
        Log.d(TAG, "CLICK!!");

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        String locationProvider = LocationManager.GPS_PROVIDER;
        Location location = null;

        // Check for API 23 or greater
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

            // If yes, we need to check for permissions
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                location = locationManager.getLastKnownLocation(locationProvider);
            }
        } else {
            location = locationManager.getLastKnownLocation(locationProvider);
        }

        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            locationSet = true;
            Log.d(TAG, "GPS successfully set location");
        }
    }

    /** Called when the user clicks the Next button */
    @SuppressWarnings("unchecked")
    public void clickNextButton(View view) {
        Log.d(TAG, "CLICK!!");
        if(locationSet) {

            Intent intent = new Intent(this, ScoreAPIActivity.class);
            Intent i = getIntent();

            HashMap<String, Integer> map = (HashMap) i.getSerializableExtra("likes");
            Log.d(TAG, "MAP in SetLocation is: " + map.toString());
            intent.putExtra("latitude", latitude);
            intent.putExtra("longitude", longitude);
            startActivity(intent);
        } else {
            // Notify user location not set
        }
    }

    /** Called when the user clicks the Next button */
    public void clickBackButton(View view) {
        Log.d(TAG, "CLICK!!");
        Intent intent = new Intent(this, SetLikesActivity.class);
        startActivity(intent);
    }
}
