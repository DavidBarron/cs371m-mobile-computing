package davidbar.foodwithfriends;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class SetLocationActivity extends AppCompatActivity {

    private static final String TAG = "SetLocation";

    private double latitude = 34.06018;
    private double longitude = -118.41835;

    private boolean locationSet = false;

    private HashMap<String, String> mContactsNumbertoName;

    // Number to Likes map
    private HashMap<String, String> mSelectedFriends;

    private ArrayList<HashMap> mFriendLikes;

    private Toast mToast;

    @Override
    @SuppressWarnings("unchecked")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_location);

        // Populate Extras
        Intent intent = getIntent();
        mContactsNumbertoName = (HashMap<String, String>) intent.getSerializableExtra("contacts");
        mSelectedFriends = (HashMap<String, String>) intent.getSerializableExtra("selectedFriends");
        mFriendLikes = (ArrayList) intent.getSerializableExtra("friendLikes");
    }

    /** Called when the user clicks the Submit button */
    /** Address String is converted to GPS coordinates */
    public void clickSubmitButton(View view) {
        Log.d(TAG, "CLICK!!");

        EditText editText = (EditText) findViewById(R.id.addressText);
        Geocoder coder = new Geocoder(this, Locale.getDefault());
        List<Address> address;

        try {
            address = coder.getFromLocationName(editText.getText().toString(), 1);
            Address location = address.get(0);
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            locationSet = true;
            showToast(getString(R.string.location_set));
            updateLatLonText();

        } catch (Exception ex) {
            showToast(getString(R.string.address_error));
        }
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
            Log.d(TAG, "API 23 or greater");
        } else {
            //locationManager.requestLocationUpdates(locationProvider, 0, 0, this);
            location = locationManager.getLastKnownLocation(locationProvider);
            Log.d(TAG, "Lower than API 23");
        }

        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            locationSet = true;
            Log.d(TAG, "GPS successfully set location");
            showToast(getString(R.string.location_set));
            updateLatLonText();
        }else{
            showToast(getString(R.string.gps_inaccessible));
        }
    }

    /** Called when the user clicks the Next button */
    @SuppressWarnings("unchecked")
    public void clickNextButton(View view) {
        Log.d(TAG, "CLICK!!");
        if(locationSet) {

            Intent intent = new Intent(this, ScoreAPIActivity.class);
            //Intent i = getIntent();

            //HashMap<String, Integer> map = (HashMap) i.getSerializableExtra("likes");
            //Log.d(TAG, "MAP in SetLocation is: " + map.toString());
            intent.putExtra("latitude", latitude);
            intent.putExtra("longitude", longitude);
            //intent.putExtra("likes", map);

            intent.putExtra("friendLikes", mFriendLikes);
            intent.putExtra("selectedFriends", mSelectedFriends);
            intent.putExtra("contacts", mContactsNumbertoName);

            startActivity(intent);
        } else {
            showToast(getString(R.string.location_not_set));
        }
    }

    /** Called when the user clicks the Next button */
    public void clickBackButton(View view) {
        Log.d(TAG, "CLICK!!");
        //Intent intent = new Intent(this, SetLikesActivity.class);
        if(mToast != null) {
            mToast.cancel();
        }
        //startActivity(intent);
        finish();
    }

    // Display toast with input text
    private void showToast(String text){
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        if(mToast != null){
            mToast.cancel();
        }

        mToast = Toast.makeText(context, text, duration);
        mToast.show();
    }

    // Updates the Lat and Lon strings
    private void updateLatLonText(){
        TextView lat = (TextView) findViewById(R.id.lat);
        TextView lon = (TextView) findViewById(R.id.lon);
        String str_lat = getString(R.string.string_lat);
        String str_lon = getString(R.string.string_lon);

        lat.setText(String.format(str_lat, latitude));
        lon.setText(String.format(str_lon, longitude));
    }
}
