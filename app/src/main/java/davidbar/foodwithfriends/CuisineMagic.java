package davidbar.foodwithfriends;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by David Barron on 4/2/2016.
 */
public class CuisineMagic {

    private static final String TAG = "CuisineMagic";

    // Cuisines user currently can set
    public static final String[] CUISINES = {"American", "Chinese", "Cuban", "Indian", "Italian",
            "Japanese", "Korean", "Mexican", "Taiwanese", "Thai"};

    // Sort single user likes stored in map, like and neutral
    // Return two Lists in HashMap
    public static HashMap<String, List> sortSingleUserLikes(HashMap<String, Integer> map){

        List like = new ArrayList();
        List neutral = new ArrayList();
        HashMap<String, List> retMap =  new HashMap<String, List>();

        // Iterate through each entry pair
        for (HashMap.Entry<String, Integer> entry : map.entrySet()) {

            String key = entry.getKey();
            Integer value = entry.getValue();

            if(value == 1){
                like.add(key);
            } else if (value == 0){
                neutral.add(key);
            }
        }

        retMap.put("like", like);
        retMap.put("neutral", neutral);

        return retMap;
    }

    // Sort multiple user likes stored in list of maps
    // Return HashMap of added up scores
    public static HashMap<String, Integer> addUserLikes(List<HashMap<String, Integer>> maps){

        HashMap<String, Integer> retMap =  new HashMap<>();

        // Iterate though each map
        for (HashMap<String, Integer> map: maps){

            // Iterate through each entry pair
            for (HashMap.Entry<String, Integer> entry : map.entrySet()) {

                String key = entry.getKey();
                Integer value = entry.getValue();

                // if K,V pair doesn't exist, add it. If it does, update it
                if (!retMap.containsKey(key)){
                    retMap.put(key, value);
                } else {

                    Integer tmpVal = retMap.get(key);
                    tmpVal += value;

                    // replace() method doesn't seems to exist...
                    retMap.put(key, tmpVal);
                }
            }
        }

        Log.d(TAG,"MAP: " + retMap.toString());
        return retMap;
    }

    // Untested
    public static LinkedList<LinkedList<String>> binMap(HashMap<String,Integer> map){

        HashMap<Integer, LinkedList<String>> tmpMap = new HashMap<>();
        LinkedList<LinkedList<String>> retList = new LinkedList<>();

        for (HashMap.Entry<String, Integer> entry : map.entrySet()) {

            String cuisine = entry.getKey();
            Integer score = entry.getValue();

            if (!tmpMap.containsKey(score)){

                LinkedList<String> list = new LinkedList<>();
                list.add(cuisine);
                tmpMap.put(score,list);

            } else {

                LinkedList<String> list = tmpMap.get(score);
                list.add(cuisine);

                // update the map...
                tmpMap.put(score, list);

            }
        }

        // more ugly code...

        Integer[] arr = (Integer[])tmpMap.keySet().toArray();

        Arrays.sort(arr);

        for ( int i = arr.length-1; i >= 0; i--){
            Integer key = arr[i];
            retList.add(tmpMap.get(key));
        }

        return retList;
    }

    public static String chooseRandomCuisine(List list){

        Random rand = new Random();
        int randIndex = rand.nextInt(list.size());

        return (String) list.get(randIndex);
    }

    // Default String for storing in Database
    public static String getDefaultLikes(){
        String retVal = "";

        for (String s : CUISINES){
            retVal += s + ":0,";
        }

        // Clip off extra ',' at end
        return retVal.substring(0,retVal.length()-1);
    }

    //
    public static HashMap<String, Integer> convertLikesStringToMap(String likes){

        HashMap<String, Integer> retVal = new HashMap<>();

        String[] pairs = likes.split(",");

        for (String s : pairs){
            String[] arr = s.split(":");
            retVal.put(arr[0],Integer.valueOf(arr[1]));
        }

        return retVal;
    }

    //
    public static String convertLikesMapToString(HashMap<String, Integer> likes){
        String retVal = "";

        for (String s : likes.keySet()){
            int score = likes.get(s);
            retVal += s + ":" + score + ",";
        }

        return retVal.substring(0,retVal.length()-1);
    }
}
