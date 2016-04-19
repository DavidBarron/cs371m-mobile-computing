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

public class ChooseFriendsActivity extends AppCompatActivity {

    private static final String TAG = "ChooseFriendsActivity";

    static final int color_green = Color.parseColor("#32CD32");
    static final int color_grey = Color.parseColor("#a8a8a8");

    private View.OnClickListener l = new View.OnClickListener(){
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
        LinearLayout ll = (LinearLayout)findViewById(R.id.scroll_friends);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(10, 20, 30, 40);

        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, null);
        int idCount = 0;
        while (phones.moveToNext())
        {
            String name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            Button btn = new Button(this);
            btn.setText(name);
            btn.setId(idCount++);
            btn.setBackgroundColor(color_grey);
            btn.setOnClickListener(l);
            //setMargins(btn,15,15,15,15);

            ll.addView(btn,lp);

            Log.d(TAG,name + " " + phoneNumber);
        }
        phones.close();
    }

    private void setMargins (View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }
}
