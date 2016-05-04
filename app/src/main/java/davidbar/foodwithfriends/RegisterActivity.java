package davidbar.foodwithfriends;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class RegisterActivity extends AppCompatActivity {

    private boolean phoneNumSet = false;

    private EditText mEt;
    private Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mEt = (EditText) findViewById(R.id.myPhoneNumberEditText);

        //String num = ContactsMagic.getMyPhoneNumber(this);

        //if(!num.equals("PHONE_NUMBER_UNKNOWN")){
        //    EditText et = (EditText) findViewById(R.id.myPhoneNumberEditText);
        //    et.setText(ContactsMagic.formatPhoneNumber(num));
        //}
    }

    public void clickOK(View view){
        String num = ContactsMagic.formatPhoneNumber(mEt.getText().toString());

        if (!num.equals("")){
            KumulosWrapper.registerUser(this, num);
            finish();
        }else{
            showToast("Invalid phone number.");
        }
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
