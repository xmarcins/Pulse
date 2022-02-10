package sample.Extensions;

import java.util.ArrayList;
import java.util.List;

/**
 * @summary
 * Extension methods for arrays
 * */
public class ArrayExtensions {

    /**
     * @summary
     * Convert liked songs from DB to list of integers
     * @param input
     * Liked songs divided with ';'
     * @return
     * Liked songs in list
     * */
    public static List<Integer> ConvertStringToIntegerList(String input){
        List<Integer> result = new ArrayList<>();

        if (!input.isEmpty())
            for (String item : input.split(";"))
                result.add(Integer.parseInt(item));

        return result;
    }

    /**
     * @summary
     * Convert list of integers to string that is inserted to DB
     * @param input
     * Liked songs in list
     * @return
     * Integers divided by ';'
     * */
    public static String ConvertIntegerListToString(List<Integer> input){
        if (input == null)
            return "";

        if (input.size() == 1)
            return input.get(0).toString();

        StringBuilder output = new StringBuilder();

        for (Integer item : input) {
            output.append(item);
            if (input.indexOf(item) < input.size() - 1)
                output.append(";"); // append only if not last
        }

        return output.toString();
    }
}
