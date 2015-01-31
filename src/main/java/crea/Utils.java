package crea;

import de.ailis.pherialize.MixedArray;

import java.util.ArrayList;

public class Utils {
    public static ArrayList<String> convertMapToListOfStrings(MixedArray mixedArray) {
        ArrayList<String> list = new ArrayList<String>();
        for (Object m : mixedArray.values()) {
            list.add(m.toString());
        }
        return list;
    }

    public static ArrayList<Object> convertMapToList(MixedArray mixedArray) {
        ArrayList<Object> list = new ArrayList<Object>();
        for (Object m : mixedArray.values()) {
            String s = m.toString();
            Integer i = Integer.parseInt(s);
            list.add(i);
        }
        return list;
    }
}
