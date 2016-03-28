package davidbar.foodwithfriends;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.HashMap;
import java.util.Map;

public class SetLikesActivity extends AppCompatActivity {

    private static final String TAG = "SetLikesDislikes";

    // Cuisines user currently can set
    private static final String[] CUISINES = {"American", "Chinese", "Cuban", "Indian", "Italian",
                                              "Japanese", "Korean", "Mexican", "Taiwanese", "Thai"};

    // Used to store user scores
    private Map<String, Integer> likes;

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

        likes = initLikes();
    }

    // Initialize our likes Map with cuisine keys and values of zero
    private Map<String, Integer> initLikes(){

        Map<String, Integer> map = new HashMap<String, Integer>();

        for(String s : CUISINES){
            map.put(s, 0);
        }

        return map;
    }

    // Called when user clicks one of the cuisines to set like or dislike
    // Changes the color of the button to green, yellow, or red on like, neutral, or dislike
    // Updates respective likes Map key to 1, 0, or -1
    public void clickFoodButton(View view) {

        Button button = (Button) findViewById(view.getId());

        // Get the color of the button
        ColorDrawable drawable = (ColorDrawable)button.getBackground();
        int color = drawable.getColor();

        // Get text of button as String
        String buttonText = (String) button.getText();

        // Debug info
        Log.d(TAG,"CLICK!!");
        Log.d(TAG,"Color is : ".concat(String.valueOf(color)));
        Log.d(TAG,"Text is : ".concat(buttonText));

        // Flip through the 3 colors
        switch(color){
            case Color.YELLOW:
                button.setBackgroundColor(Color.GREEN);
                button.setTextColor(Color.WHITE);
                likes.put(buttonText, 1);
                break;
            case Color.GREEN:
                button.setBackgroundColor(Color.RED);
                button.setTextColor(Color.WHITE);
                likes.put(buttonText, -1);
                break;
            case Color.RED:
                button.setBackgroundColor(Color.YELLOW);
                button.setTextColor(Color.BLACK);
                likes.put(buttonText, 0);
                break;
        }
    }

    /** Called when the user clicks the Next button */
    public void clickNextButton(View view) {
        Log.d(TAG, "CLICK!!");
        Log.d(TAG, "FINAL MAP: " + likes.toString());
        Intent intent = new Intent(this, SetLocationActivity.class);
        startActivity(intent);
    }
}
