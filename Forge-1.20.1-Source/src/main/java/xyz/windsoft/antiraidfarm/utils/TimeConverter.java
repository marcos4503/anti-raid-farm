package xyz.windsoft.antiraidfarm.utils;

/*
 * This class handle the time conversion to readable by humens
 */

public class TimeConverter {

    //Public static methods

    public static String ConvertTicksToTime(long ticks) {
        //Convert ticks to readable time and return
        long seconds = ticks / 20;
        long minutes = seconds / 60;
        long hours = minutes / 60;

        seconds %= 60;
        minutes %= 60;

        if (hours > 0)
            return String.format("%02dh%02dm%02ds", hours, minutes, seconds);
        else if (minutes > 0)
            return String.format("%02dm%02ds", minutes, seconds);
        else
            return String.format("%02ds", seconds);
    }
}