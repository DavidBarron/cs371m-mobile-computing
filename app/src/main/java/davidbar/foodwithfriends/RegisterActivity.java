package davidbar.foodwithfriends;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {

    private boolean phoneNumSet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        String num = ContactsMagic.getMyPhoneNumber(this);

        if(!num.equals("PHONE_NUMBER_UNKNOWN")){
            EditText et = (EditText) findViewById(R.id.myPhoneNumberEditText);
            et.setText(ContactsMagic.formatPhoneNumber(num));
        }

    }

}
