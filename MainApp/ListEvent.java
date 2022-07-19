package MainApp;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class ListEvent extends Command {

    String parameters[]; // (1 or 2) date(s), (1 or 2) time(s)

    /*
     * 
     * [Date: 07-07-2022]
     * 
     * [10:00] [Umer Bday]
     * | |
     * | |
     * | ______ V ________
     * [11:00]
     * |
     * | [Eid Prayer]
     * | |
     * [12:00] |
     * | |
     * | _______ V ________
     * | [Lunch Date]
     * [13:00] _______ V ________
     * | |
     * | |
     * | |
     * [14:00]
     * 
     * 
     * [15:00]
     * 
     * 
     * 
     */

    Event e = new Event();

    public ListEvent(String parameters[]) {
        this.parameters = parameters;
    }

    public void execute() {
        List<String[]> l = super.getResultsFromQuery(getQuery(parameters, "events", null), "name-s", "startTime-i",
                "endTime-i", "eventDate-s");
        for (String s[] : l) {
            for (int i = 0; i < s.length; i++) {
                System.out.print(s[i] + " | ");
            }
            System.out.println("");
        }
    }

    /*
     * Case #1: p[].length is 1 (date)
     * Case #2: p[].length is 1 (time interval)
     * Case #3: p[].length is 2 (date interval)
     * Case #4: p[].length is 2-3 (time and date interval)
     */

    public String getQuery(String parameters[], String table, String columns) {
        String query = "SELECT * FROM " + table;

        // If no parameters provided, output current day's events
        if (parameters[0] == null && parameters[1] == null) {
            query += " WHERE month = " + this.e._date[0] + " AND day = " + this.e._date[1] + " AND year = "
                    + this.e._date[2];
        }

        return query + ";";
    }

    private String appendDate(String date) {
        // String d = date.split("-");
        // return "month < "+d[]
        return null;
    }

    private boolean isTime(String time) {
        if (time == null)
            return false;
        String t[] = time.split("-");
        return t.length == 1;
    }

    private boolean isDate(String date) {
        if (date == null)
            return false;
        String d[] = date.split("-");
        return (d.length == 3 || d.length == 5);
    }

}