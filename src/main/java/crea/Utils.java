package crea;

import de.ailis.pherialize.MixedArray;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Utils {

    public static ArrayList<String> convertMapToListOfStrings(JSONObject jsonObject) {
        ArrayList<String> list = new ArrayList<String>();
        for (Iterator it = jsonObject.keySet().iterator(); it.hasNext(); ) {
            String key = (String) it.next();
            String value = (String) jsonObject.get(key);
            list.add(value);
        }
        return list;
    }

    public static ArrayList<Integer> convertMapToListOfInts(JSONObject jsonObject) {
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (Iterator it = jsonObject.keySet().iterator(); it.hasNext(); ) {
            String key = (String) it.next();
            int value = Integer.parseInt((String) jsonObject.get(key));
            list.add(value);
        }
        return list;
    }

    public static ArrayList<String> convertJsonArrayToListOfStrings(JSONArray jsonArray) {
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < jsonArray.size(); i++) {
            list.add((String) jsonArray.get(i));
        }
        return list;
    }

    public static ArrayList<Integer> convertJsonArrayToListOfInts(JSONArray jsonArray) {
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < jsonArray.size(); i++) {
            Object value = jsonArray.get(i);
            if (value instanceof String) {
                String str = (String) value;
                if (!str.equals("")) {
                    long longValue = Long.valueOf(str);
                    list.add((int) longValue);
                }
            } else {
                long longValue = (Long) value;
                list.add((int) longValue);
            }

        }
        return list;
    }


    // http://stackoverflow.com/questions/718554/how-to-convert-an-arraylist-containing-integers-to-primitive-int-array
    public static int[] convertIntegers(List<Integer> integers)
    {
        int[] ret = new int[integers.size()];
        Iterator<Integer> iterator = integers.iterator();
        for (int i = 0; i < ret.length; i++)
        {
            ret[i] = iterator.next().intValue();
        }
        return ret;
    }

    public static ArrayList<Integer> convertIntArrayToArrayList(int[] intArray) {
        ArrayList<Integer> list = new ArrayList<Integer>(intArray.length);
        for (int i = 0; i < intArray.length; i++) {
            list.add(intArray[i]);
        }
        return list;
    }

}
