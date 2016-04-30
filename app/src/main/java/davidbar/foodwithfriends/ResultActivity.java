package davidbar.foodwithfriends;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class ResultActivity extends AppCompatActivity {

    private static final String TAG = "ResultActivity";

    private ArrayList<String> mFriends;

    private boolean mMessageSent;

    private String mName;
    private String mAddress;
    private String mPhone;

    private Vibrator mVibrator;

    private Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        mMessageSent = false;

        mVibrator = (Vibrator) getApplicationContext().getSystemService(VIBRATOR_SERVICE);

        Intent thisIntent = getIntent();

        TextView nameText = (TextView) findViewById(R.id.result_name_blank);
        TextView addressText = (TextView) findViewById(R.id.result_address_blank);
        TextView phoneText = (TextView) findViewById(R.id.result_phone_blank);

        mFriends = (ArrayList<String>)thisIntent.getSerializableExtra("friends");
        mName = (String)thisIntent.getSerializableExtra("name");
        mAddress = (String)thisIntent.getSerializableExtra("address");
        mPhone = (String)thisIntent.getSerializableExtra("phone");

        nameText.setText(mName);
        addressText.setText(mAddress);
        phoneText.setText(mPhone);

        Log.d(TAG, mFriends.toString());
    }

    public void sendMessage(View view){

        mVibrator.vibrate(HapticFeedbackConstants.VIRTUAL_KEY);

        if (!mMessageSent && mFriends != null){

            mFriends.add(ContactsMagic.getRegisteredUser(this));
            String message = getFriendsMessage();
            SmsManager smsManager = SmsManager.getDefault();

            Log.d(TAG, mFriends.toString());

            for (String num : mFriends) {
                smsManager.sendTextMessage(num, null, message, null, null);
            }
            mMessageSent = true;
        }

        showToast("The message has been sent.");
    }

    //
    private String getFriendsMessage(){

        String message = "Let's grab a bite! Meet me at: " +
                mName + ". The address is  " +
                mAddress + ", and the phone number is " +
                mPhone + ".";

        return message;

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
}
