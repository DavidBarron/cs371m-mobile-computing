package davidbar.foodwithfriends;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ProgressBar;

import com.factual.driver.Circle;
import com.factual.driver.Factual;
import com.factual.driver.Query;
import com.factual.driver.ReadResponse;

import com.google.common.collect.Lists;

public class ScoreAPIActivity extends AppCompatActivity {

    // Factual API keys (free account)
    // Should obfuscate this ASAP!!!
    private final String authKey = "Y0NdQGfaA3M05zw0LGtPY2y5lp7CrDklONLSXsa3";
    private final String authSecret = "d08VLmXB9jBCX0wcAb25BUVx3we2FEQiBaqHBKtW";

    protected Factual factual = new Factual(authKey, authSecret);

    private static final String TAG = "ScoreAPIActivity";

    private HashMap<String, String> mContactsNumbertoName;

    // Number to Likes map
    private HashMap<String, String> mSelectedFriends;

    private ArrayList<HashMap<String, Integer>> mFriendLikes;

    private ProgressBar mSpinner;

    @Override
    @SuppressWarnings("unchecked")
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_api);

        mSpinner = (ProgressBar)findViewById(R.id.progressBar1);

        FactualRetrievalTask task = new FactualRetrievalTask();
        //Intent thisIntent = getIntent();

        // Populate Extras
        Intent intent = getIntent();
        mContactsNumbertoName = (HashMap<String, String>) intent.getSerializableExtra("contacts");
        mSelectedFriends = (HashMap<String, String>) intent.getSerializableExtra("selectedFriends");
        mFriendLikes = (ArrayList) intent.getSerializableExtra("friendLikes");

        double latitude = (Double)intent.getSerializableExtra("latitude");
        double longitude = (Double)intent.getSerializableExtra("longitude");
        int meters = 5000;      // hardcode location to 5 km

        // UGLY CODE BELOW!!! :(

        //HashMap<String, Integer> map = (HashMap) thisIntent.getSerializableExtra("likes");

        //HashMap<String, List> likes = CuisineMagic.sortSingleUserLikes(map);

        ArrayList<ArrayList<String>> queryList = CuisineMagic.binMap(CuisineMagic.addUserLikes(mFriendLikes));
        ArrayList<String> topList = queryList.get(0);



        //List queryList = likes.get("like");
        //if(queryList.size() == 0){
        //    queryList = likes.get("neutral");
        //}

        String cuisine = CuisineMagic.chooseRandomCuisine(topList);

        //if(queryList.size() != 0) {
        //    cuisine = CuisineMagic.chooseRandomCuisine(queryList);
        //} else{
        //    cuisine = "Italian";
        //}

        Log.d(TAG, "LAT: " + latitude);
        Log.d(TAG, "LON: " + longitude);

        Query query = new Query()
                .within(new Circle(latitude, longitude, meters))
                .field("cuisine").equal(cuisine)
                .sortAsc("$distance")
                .only("name", "address", "tel","locality", "region", "postcode");

        task.execute(query);
    }

    protected class FactualRetrievalTask extends AsyncTask<Query, Integer, List<ReadResponse>> {
        @Override
        protected List<ReadResponse> doInBackground(Query... params) {
            List<ReadResponse> results = Lists.newArrayList();
            for (Query q : params) {
                results.add(factual.fetch("restaurants-us", q));
            }
            return results;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
        }

        @Override
        protected void onPostExecute(List<ReadResponse> responses) {
            StringBuffer sb = new StringBuffer();
            for (ReadResponse response : responses) {
                for (Map<String, Object> restaurant : response.getData()) {
                    Log.d(TAG, restaurant.toString());
                    String name = (String) restaurant.get("name");
                    String address = (String) restaurant.get("address");
                    String phone = (String) restaurant.get("tel");
                    Number distance = (Number) restaurant.get("$distance");
                    sb.append(distance + " meters away: "+name+" @ " +address + ", call "+phone);
                    sb.append(System.getProperty("line.separator"));
                }
            }
            Log.d(TAG,sb.toString());
            Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
            Map<String, Object> restaurant = responses.get(0).getData().get(0);
            intent.putExtra("name", (String) restaurant.get("name"));
            intent.putExtra("address", (String)restaurant.get("address") + ", "
                    + (String)restaurant.get("locality") + " "
                    + (String)restaurant.get("region") + ", "
                    + (String)restaurant.get("postcode"));
            intent.putExtra("phone", (String) restaurant.get("tel"));

            //mSpinner.setVisibility(View.GONE);

            startActivity(intent);
        }
    }
}
