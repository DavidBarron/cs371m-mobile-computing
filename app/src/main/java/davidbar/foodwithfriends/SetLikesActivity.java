package davidbar.foodwithfriends;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class SetLikesActivity extends AppCompatActivity {

    private static final String TAG = "SetLikesDislikes";
    private boolean toggle = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_likes);
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
    }

    public void clickMyselfButton2(View view) {
        //Intent intent = new Intent(this, SetLikesActivity.class);
        //startActivity(intent);

        Button button = (Button) findViewById(R.id.myselfButton2);

        Log.d(TAG,"CLICK!!");

        if(toggle){
            toggle = false;
            button.setBackgroundColor(Color.BLUE);
        }else{
            toggle = true;
            button.setBackgroundColor(Color.RED);
        }
    }
}
