package davidbar.foodwithfriends;

import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.HashMap;

public class ChooseFriendsActivity extends AppCompatActivity {

    private static final String TAG = "ChooseFriendsActivity";

    static final int color_green = Color.parseColor("#32CD32");
    static final int color_grey = Color.parseColor("#a8a8a8");

    private HashMap<String, String> mContacts = new HashMap<String, String>();

    // Listener for buttons...
    private View.OnClickListener listener = new View.OnClickListener(){
        public void onClick(View view){

            Button button = (Button) findViewById(view.getId());

            // Get the color of the button
            ColorDrawable drawable = (ColorDrawable)button.getBackground();
            int color = drawable.getColor();

            if (color == color_grey){
                button.setBackgroundColor(color_green);
            } else {
                button.setBackgroundColor(color_grey);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_friends);

        mContacts = ContactsMagic.formatContacts(getUserContacts());

        addFriendButtons(mContacts);
    }

    // Cycles through all of the user's contacts for raw name, phone number data strings
    private HashMap<String, String> getUserContacts(){
        HashMap<String, String> contacts = new HashMap<String, String>();
        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, null);

        while (phones.moveToNext())
        {
            String name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            contacts.put(name, phoneNumber);

            Log.d(TAG,name + " " + phoneNumber);
        }
        phones.close();

        return contacts;
    }

    // Creates buttons in ScrollView from input HashMap
    // Keys are contact names, Values are phone numbers
    private void addFriendButtons(HashMap<String, String> map){

        LinearLayout ll = (LinearLayout)findViewById(R.id.scroll_friends);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(15, 15, 15, 15);
        int idCount = 0;

        for (String key : map.keySet()){

            Button button = new Button(this);
            button.setText(key);
            button.setId(idCount++);
            button.setBackgroundColor(color_grey);
            button.setOnClickListener(listener);

            ll.addView(button,lp);
        }
    }
}
