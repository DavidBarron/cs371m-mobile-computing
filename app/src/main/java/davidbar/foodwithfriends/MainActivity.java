package davidbar.foodwithfriends;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.HashMap;

import com.kumulos.android.jsonclient.Kumulos;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    // Probably should obfuscate...
    private static final String KUMULOS_API_KEY = "mqnkbgy7ckgr2vwcwyf6prjtqnfwp4pf";
    private static final String KUMULOS_SECRET_KEY = "p6vtb134";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Only call this ONCE in here, never in other Activities
        Kumulos.initWithAPIKeyAndSecretKey(KUMULOS_API_KEY, KUMULOS_SECRET_KEY, this);

        // Check to if user is registered
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

        // Empty here only...
        ArrayList<HashMap> friendLikes = new ArrayList<>();
        HashMap<String, String> blankMap = new HashMap<>();

        intent.putExtra("friendLikes", friendLikes);
        intent.putExtra("selectedFriends", blankMap);
        intent.putExtra("contacts", blankMap);

        startActivity(intent);
    }

    /** Called when the user clicks the Send button */
    public void clickFriendsButton(View view) {
        Log.d(TAG, "CLICK!!");
        Intent intent = new Intent(this, ChooseFriendsActivity.class);
        startActivity(intent);
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
