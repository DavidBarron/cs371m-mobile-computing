package davidbar.foodwithfriends;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.kumulos.android.jsonclient.Kumulos;
import com.kumulos.android.jsonclient.ResponseHandler;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    // Probably should obfuscate...
    private static final String KUMULOS_API_KEY = "mqnkbgy7ckgr2vwcwyf6prjtqnfwp4pf";
    private static final String KUMULOS_SECRET_KEY = "p6vtb134";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Only call this ONCE in here, never in other Activities
        Kumulos.initWithAPIKeyAndSecretKey(KUMULOS_API_KEY, KUMULOS_SECRET_KEY, this);

        if (!ContactsMagic.checkRegisteredUser(this)) {
            String num = ContactsMagic.getMyPhoneNumber(this);
            if(!num.equals("PHONE_NUMBER_UNKNOWN")){
                Log.d(TAG, "ABOUT TO REGISTER USER");
                KumulosWrapper.registerUser(this,num);
            }else {
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
            }
        }else{
            Log.d(TAG, "User already registered.");
        }
    }

    /** Called when the user clicks the Myself button */
    public void clickMyselfButton(View view) {
        Log.d(TAG, "CLICK!!");
        Intent intent = new Intent(this, SetLikesActivity.class);
        startActivity(intent);
    }

    /** Called when the user clicks the Send button */
    public void clickFriendsButton(View view) {
        Log.d(TAG, "CLICK!!");
        Intent intent = new Intent(this, ChooseFriendsActivity.class);
        startActivity(intent);
    }

    private void registerUser(String num){
        HashMap<String,String> map = new HashMap<>();
        String defaultLikes = CuisineMagic.getDefaultLikes();

        // Note ALL function and param names MUST be in lowerCamelCase
        map.put("userNumber",num);
        map.put("likes",defaultLikes);

        Log.d(TAG, "NUMBER: " + num );
        Log.d(TAG, "DEF: " + defaultLikes );
        Log.d(TAG, "MAP: " + map.toString());

        Kumulos.call("createUser",map,new ResponseHandler(){
            @Override
            public void didCompleteWithResult(Object result) {
                Log.d(TAG, "RESULT: " + result.toString());
            }
        });
        ContactsMagic.setRegisteredUser(this, num);
        Log.d(TAG, "REGISTER USER DONE");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
