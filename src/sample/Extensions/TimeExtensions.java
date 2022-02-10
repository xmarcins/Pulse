package sample.Extensions;

/**
 * @summary
 * Extension methods
 * convert time into the various types
 * */
public class TimeExtensions {

    /**
     * @summary
     * Convert seconds to string time
     * @param lengthInSeconds
     * Length of song in seconds
     * @return
     * Formatted time = 'XX:XX'
     * */
    public static String ConvertSecondsToStringTime(int lengthInSeconds){
        int seconds = lengthInSeconds % 60;
        int minutes = (lengthInSeconds - seconds)/60;

        return String.format("%02d:%02d", minutes, seconds);
    }

    /***
     * @summary
     * Convert string time to millis
     * Used to create lyrics
     * @param input
     * Time in string hh:mm:ss:xxx
     * @return
     * Time converted to milliseconds
     */
    public static int ConvertToMillis(String input){
        if (input.isEmpty() || input == null)
            return 0;

        String[] time = input.replace(",", ":").trim().split(":");
        int hours = Integer.parseInt(time[0]) * 60 * 60 * 1000;
        int minutes = Integer.parseInt(time[1]) * 60 * 1000;
        int seconds = Integer.parseInt(time[2]) * 1000;
        int milliseconds = Integer.parseInt(time[3]);

        return hours + minutes + seconds + milliseconds;
    }
}
