package davidbar.foodwithfriends;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class SetLocationActivity extends AppCompatActivity implements LocationListener{

    private static final String TAG = "SetLocation";

    private LocationManager mLocationManager;
    private LocationListener mLocationListener;

    private double latitude = 30.2849185;
    private double longitude = -97.7340567;
    private String locationStr = "";

    private boolean locationSet = false;
    private boolean mLocationAutoSet = true;

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

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        // Check for API 23 or greater
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

            // If yes, we need to check for permissions
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
            }
            Log.d(TAG, "API 23 or greater");
        } else {
            //locationManager.requestLocationUpdates(locationProvider, 0, 0, this);
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
            Log.d(TAG, "Lower than API 23");
        }

        EditText editText = (EditText) findViewById(R.id.addressText);

        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        // Populate Extras
        Intent intent = getIntent();
        mContactsNumbertoName = (HashMap<String, String>) intent.getSerializableExtra("contacts");
        mSelectedFriends = (HashMap<String, String>) intent.getSerializableExtra("selectedFriends");
        mFriendLikes = (ArrayList) intent.getSerializableExtra("friendLikes");

        //double[] latLon = getNewLatLon();

        //if(latLon != null){
        //    latitude = latLon[0];
        //    longitude = latLon[1];
        //}

        // Hack for now
        //locationSet = true;

        //String addressStr = getAddress(latitude, longitude);

        //if(addressStr != null){
        //    locationStr = addressStr;
        //    updateAddressText();
        //}
    }

    /** Called when the user clicks the Submit button */
    /** Address String is converted to GPS coordinates */
    public void clickSubmitButton(View view) {
        Log.d(TAG, "CLICK!!");

        EditText editText = (EditText) findViewById(R.id.addressText);
        Geocoder coder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;

        try {
            mLocationAutoSet = false;
            addresses = coder.getFromLocationName(editText.getText().toString(), 1);
            Address location = addresses.get(0);
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            locationSet = true;
            showToast(getString(R.string.location_set));
            String str = getAddress(latitude,longitude);

            if(str != null){
                locationStr = str;
                updateAddressText();
            }

            //updateLatLonText();

        } catch (Exception ex) {
            showToast(getString(R.string.address_error));
            mLocationAutoSet = true;
        }
    }

//    /** Called when the user clicks the Submit button */
//    /** Address String is converted to GPS coordinates */
//    public void clickGPSButton(View view) {
//        Log.d(TAG, "CLICK!!");
//
//        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
//        String locationProvider = LocationManager.GPS_PROVIDER;
//        Location location = null;
//
//        // Check for API 23 or greater
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
//
//            // If yes, we need to check for permissions
//            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
//                    || checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//                location = locationManager.getLastKnownLocation(locationProvider);
//            }
//            Log.d(TAG, "API 23 or greater");
//        } else {
//            //locationManager.requestLocationUpdates(locationProvider, 0, 0, this);
//            location = locationManager.getLastKnownLocation(locationProvider);
//            Log.d(TAG, "Lower than API 23");
//        }
//
//        if (location != null) {
//            latitude = location.getLatitude();
//            longitude = location.getLongitude();
//            locationSet = true;
//            Log.d(TAG, "GPS successfully set location");
//            showToast(getString(R.string.location_set));
//            //updateLatLonText();
//        }else{
//            showToast(getString(R.string.gps_inaccessible));
//        }
//    }

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
        if(mToast != null) {
            mToast.cancel();
        }

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

//    // May return null
//    private double[] getLatLon(){
//
//        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
//        //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,this);
//        String locationProvider = LocationManager.GPS_PROVIDER;
//        Location location = null;
//
//        // Check for API 23 or greater
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
//
//            // If yes, we need to check for permissions
//            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
//                    || checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//                location = locationManager.getLastKnownLocation(locationProvider);
//            }
//            Log.d(TAG, "API 23 or greater");
//        } else {
//            //locationManager.requestLocationUpdates(locationProvider, 0, 0, this);
//            location = locationManager.getLastKnownLocation(locationProvider);
//            Log.d(TAG, "Lower than API 23");
//        }
//
//        if (location != null) {
//            double lat = location.getLatitude();
//            double lon = location.getLongitude();
//            Log.d(TAG, "GPS successfully set location");
//            double[] retVal = {lat, lon};
//
//            return retVal;
//        }else{
//            return null;
//        }
//    }

//    private double[] getNewLatLon(){
//        double[] coordinates = new double[2];
//
//        coordinates[0] = latitude;
//        coordinates[1] = longitude;
//
//        return coordinates;
//    }

    // may return null
    private String getAddress(double lat, double lon){

        Geocoder coder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;

        String retStr = "";

        try {
            addresses = coder.getFromLocation(lat,lon,1);
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            //String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();
            //showToast(getString(R.string.location_set));

            //if(knownName != null){
            //    retStr += knownName + "\n";
            //}
            if(address != null){
                retStr += address + "\n";
            }
            if(city != null){
                retStr += city + " ";
            }
            if(state != null){
                retStr += state + " ";
            }
            if(postalCode != null){
                retStr += postalCode;
            }

            return retStr;

        } catch (Exception ex) {
            //showToast(getString(R.string.address_error));
            return null;
        }

    }

    // Updates the address String
    private void updateAddressText(){

        TextView current_address = (TextView) findViewById(R.id.current_location_str);
        current_address.setText(locationStr);
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onLocationChanged(Location location) {

        if(mLocationAutoSet) {
            Log.d(TAG, "Location changed");

            latitude = location.getLatitude();
            longitude = location.getLongitude();
            locationSet = true;

            String addressStr = getAddress(latitude, longitude);

            if (addressStr != null) {
                locationStr = addressStr;
                updateAddressText();
            }
        }

    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d(TAG,"disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d(TAG,"enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d(TAG,"status");
    }
}
