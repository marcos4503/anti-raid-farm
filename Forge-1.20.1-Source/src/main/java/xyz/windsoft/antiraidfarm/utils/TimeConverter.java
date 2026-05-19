package xyz.windsoft.antiraidfarm.utils;

/*
 * This class handle the time conversion to readable by humans
 *
 * Information about side that this Class will run:
 * [ ] Only in Client at all - [ ] Only in Server at all - [X] Both at all - [ ] In Both sides, but some Standard/Events/Overrides Methods run on Client and Server at SAME time AND some Standard/Events/Overrides Methods run ONLY on Client OR Server.
 *                                                                               The Synchronization of some variables/properties from this Class, running in the Server to Clients running this, MAY be needed, according to needs of this Class
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