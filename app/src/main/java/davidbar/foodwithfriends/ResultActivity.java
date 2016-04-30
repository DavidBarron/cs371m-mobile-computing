package davidbar.foodwithfriends;

import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.TextView;


public class ResultActivity extends AppCompatActivity {

    private boolean mMessageSent;

    private String mName;
    private String mAddress;
    private String mPhone;

    private Vibrator mVibrator;

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

        mName = (String)thisIntent.getSerializableExtra("name");
        mAddress = (String)thisIntent.getSerializableExtra("address");
        mPhone = (String)thisIntent.getSerializableExtra("phone");

        nameText.setText(mName);
        addressText.setText(mAddress);
        phoneText.setText(mPhone);
    }

    public void sendMessage(View view){

        mVibrator.vibrate(HapticFeedbackConstants.VIRTUAL_KEY);

        if (!mMessageSent){
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage("817-269-5908", null, "Wooooooooo", null, null);
            mMessageSent = true;
        }else{

        }
    }
}
