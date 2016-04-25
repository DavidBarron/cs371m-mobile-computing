package davidbar.foodwithfriends;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.kumulos.android.jsonclient.Kumulos;
import com.kumulos.android.jsonclient.ResponseHandler;

import java.util.HashMap;

// Anything related to doing work with user contacts goes here.
public class ContactsMagic {

    public static final String TAG = "ContactsMagic";

    // Cycles through all of the user's contacts for raw name, phone number data strings
    // Returns map of contact name as key, contact number as value
    // 800 numbers and other undesired contacts are omitted
    protected static HashMap<String, String> getContactsNameToNumber(Context context){
        HashMap<String, String> contacts = new HashMap<String, String>();
        Cursor phones = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, null);

        while (phones.moveToNext())
        {
            String name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            phoneNumber = formatPhoneNumber(phoneNumber);

            if (!contacts.containsKey(name) && !phoneNumber.equals("")) {
                contacts.put(name, phoneNumber);
                Log.d(TAG,name + " " + phoneNumber);
            }
        }

        phones.close();

        return contacts;
    }

    // Cycles through all of the user's contacts for raw name, phone number data strings
    // Returns map of contact number as key, contact name as value
    // 800 numbers and other undesired contacts are omitted
    protected static HashMap<String, String> getContactsNumberToName(Context context){
        HashMap<String, String> contacts = new HashMap<String, String>();
        Cursor phones = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, null);

        while (phones.moveToNext())
        {
            String name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            phoneNumber = formatPhoneNumber(phoneNumber);

            if (!phoneNumber.equals("") && !contacts.containsKey(name)) {
                contacts.put(phoneNumber, name);
                Log.d(TAG,phoneNumber + " " + name);
            }
        }

        phones.close();

        return contacts;
    }

    // Takes in String representing a phone number of any format
    // Return number in format of "123-555-9876"
    // Returns empty string for some numbers
    // Ex: 411, 911, 1-800..., etc
    // Toll free codes: 800, 888, 877, 866, 855 and 844
    public static String formatPhoneNumber(String phoneNumber){
        String retVal = "";
        char[] arr = phoneNumber.toCharArray();
        int numLen = 0;
        for (int i = arr.length-1; i >= 0; i--){
            if(Character.isDigit(arr[i])){
                retVal += arr[i];
                numLen++;
                if(numLen == 4 || numLen == 7){
                    retVal += "-";
                }
                if(numLen == 10){
                    retVal = reverseString(retVal);
                    break;
                }
            }
        }

        if(numLen != 10
                || retVal.startsWith("800")
                || retVal.startsWith("888")
                || retVal.startsWith("877")
                || retVal.startsWith("866")
                || retVal.startsWith("855")
                || retVal.startsWith("844")){
            return "";
        }

        return retVal;
    }

    // Reverse a String
    // Source: http://stackoverflow.com/questions/7569335/reverse-a-string-in-java
    public static String reverseString(String source) {
        int i, len = source.length();
        StringBuilder dest = new StringBuilder(len);

        for (i = (len - 1); i >= 0; i--){
            dest.append(source.charAt(i));
        }

        return dest.toString();
    }

    // Try to get the device's phone number
    // Varies by phone and carrier, doesn't always work
    public static String getMyPhoneNumber(Context context){
        try {
            TelephonyManager tMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            return formatPhoneNumber(tMgr.getLine1Number());
        } catch (Exception e){
            return "PHONE_NUMBER_UNKNOWN";
        }
    }

    //
    public static void setRegisteredUser(Context context, String num){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("fwf_phone_number",num);
        editor.apply();
    }

    // Check to see if the user has previously registered their phone number
    public static boolean checkRegisteredUser(Context context){

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String phoneNumber = prefs.getString("fwf_phone_number",null);

        if (phoneNumber != null){
            Log.d(TAG, "Registered user found: " + phoneNumber);
            return true;
        } else {
            Log.d(TAG, "User not registered");
            return false;
        }
    }

    // Get the phone number that was used to register with database
    public static String getRegisteredUser(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String phoneNumber = prefs.getString("fwf_phone_number",null);

        if (phoneNumber != null){
            Log.d(TAG, "Registered user found: " + phoneNumber);
            return phoneNumber;
        } else {
            Log.d(TAG, "User not registered");
            return null;
        }
    }

//    protected static void registerUser(Context context, String num){
//        HashMap<String,String> map = new HashMap<>();
//        String defaultLikes = CuisineMagic.getDefaultLikes();
//
//        // Note ALL function and param names MUST be in lowerCamelCase
//        map.put("userNumber",num);
//        map.put("likes",defaultLikes);
//
//        Log.d(TAG, "NUMBER: " + num );
//        Log.d(TAG, "DEF: " + defaultLikes );
//        Log.d(TAG, "MAP: " + map.toString());
//
//        Kumulos.call("createUser",map,new ResponseHandler(){
//            @Override
//            public void didCompleteWithResult(Object result) {
//                Log.d(TAG, "RESULT: " + result.toString());
//            }
//        });
//        ContactsMagic.setRegisteredUser(context, num);
//        Log.d(TAG, "REGISTER USER DONE");
//    }

//    // Contacts data acquired from device likely does not have nice format
//    // Take care of duplicate entries and varied phone entry formats
//    public static HashMap<String, String> formatContacts(HashMap<String, String> map){
//
//        HashMap<String, String> retVal = new HashMap<String, String>();
//
//        for(String key : map.keySet()){
//
//            String tempPhoneNum = map.get(key);
//            tempPhoneNum = formatPhoneNumber(tempPhoneNum);
//
//            if(!tempPhoneNum.equals("") && !retVal.containsKey(key)){
//                retVal.put(key, tempPhoneNum);
//            }
//        }
//        return retVal;
//    }

}
