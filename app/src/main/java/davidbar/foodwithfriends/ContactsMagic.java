package davidbar.foodwithfriends;

import java.util.HashMap;

/**
 * Created by David on 4/19/2016.
 */

// Anything related to doing work with user contacts goes here.
public class ContactsMagic {

    // Contacts data acquired from device likely does not have nice format
    // Take care of duplicate entries and varied phone entry formats
    public static HashMap<String, String> formatContacts(HashMap<String, String> map){

        HashMap<String, String> retVal = new HashMap<String, String>();

    for(String key : map.keySet()){

        String tempPhoneNum = map.get(key);
        tempPhoneNum = formatPhoneNumber(tempPhoneNum);

        if(!tempPhoneNum.equals("") && !retVal.containsKey(key)){
            retVal.put(key, tempPhoneNum);
        }
    }
        return retVal;
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

}
