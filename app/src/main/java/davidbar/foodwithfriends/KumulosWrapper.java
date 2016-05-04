package davidbar.foodwithfriends;

import android.content.Context;
import android.util.Log;

import com.kumulos.android.jsonclient.Kumulos;
import com.kumulos.android.jsonclient.ResponseHandler;

import java.util.ArrayList;
import java.util.HashMap;

// Helper wrapper functions for Kumulos calls.
public class KumulosWrapper {

    public static final String TAG = "KumulosWrapper";

    // Register a new user by adding them to the database with default Likes
    protected static void registerUser(Context context, String num){

        // input params
        HashMap<String,String> params = new HashMap<>();
        String defaultLikes = CuisineMagic.getDefaultLikes();

        // Note ALL function and param names MUST be in lowerCamelCase
        params.put("userNumber",num);
        params.put("likes",defaultLikes);

        Log.d(TAG, "NUMBER: " + num );
        Log.d(TAG, "DEF: " + defaultLikes );
        Log.d(TAG, "MAP: " + params.toString());

        Kumulos.call("queryUser", params, new ResponseHandler(){

            @Override
            public void didCompleteWithResult(Object result){

                if (result != null && ((ArrayList)result).size() == 0){
                    Kumulos.call("createUser", params, new ResponseHandler(){

                        @Override
                        public void didCompleteWithResult(Object result) {
                            Log.d(TAG, "RESULT: " + result.toString());
                        }
                    });
                }else{
                    Log.d(TAG, "User already registered.");
                }
            }

        });

        //Kumulos.call("createUser", params, new ResponseHandler(){

        //    @Override
        //    public void didCompleteWithResult(Object result) {
        //        Log.d(TAG, "RESULT: " + result.toString());
        //    }
        //});

        ContactsMagic.setRegisteredUser(context, num);

        Log.d(TAG, "REGISTER USER DONE");
    }

    // Call to update a user'd likes stored in database
    // result Object should be Integer with number of records updated
    protected static void updateUser(String num, String likes){

        // input params
        HashMap<String, String> params = new HashMap<>();
        params.put("userNumber",num);
        params.put("likes",likes);

        Kumulos.call("updateUser", params, new ResponseHandler(){
            @Override
            public void didCompleteWithResult(Object result){
                // Should do some error reporting later
            }
        });
    }

    // Can't be called here, asynchronous call
    protected static HashMap<String, String> getFriends(HashMap<String, String> contacts){
        HashMap<String, String> friends = new HashMap<>();
        HashMap<String, String> params = new HashMap<>();

        for (String s : contacts.keySet()){

            String num = contacts.get(s);
            params.clear();
            params.put("userNumber",num);
            Log.d(TAG,s);
            Log.d(TAG,params.toString());

            Kumulos.call("queryUser",params,new ResponseHandler(){

                @Override
                public void didCompleteWithResult(Object result){

                    if( result != null){
                        Log.d(TAG, result.getClass().toString());
                        Log.d(TAG, result.toString());
                    }

                }

            });

        }

        Log.d(TAG, "RETURN FROM FIND FRIENDS");
        return friends;
    }

}
