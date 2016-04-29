package davidbar.foodwithfriends;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
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

        Intent thisIntent = getIntent();

        TextView nameText = (TextView) findViewById(R.id.result_name_blank);
        TextView addressText = (TextView) findViewById(R.id.result_address_blank);
        TextView phoneText = (TextView) findViewById(R.id.result_phone_blank);

        nameText.setText((String)thisIntent.getSerializableExtra("name"));
        addressText.setText((String)thisIntent.getSerializableExtra("address"));
        phoneText.setText((String)thisIntent.getSerializableExtra("phone"));
    }

    public void sendMessage(View view){
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage("817-269-5908", null, "Wooooooooo", null, null);
    }
}
