package davidbar.foodwithfriends;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by David Barron on 4/2/2016.
 */
public class CuisineMagic {

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

    public static String chooseRandomCuisine(List list){

        Random rand = new Random();
        int randIndex = rand.nextInt(list.size());

        return (String) list.get(randIndex);
    }
}
