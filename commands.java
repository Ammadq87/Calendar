package Calendar;

import java.util.*;

/* 
HAVE TO CHANGE ce to something else as a command terme

 * (1) New Event → event ce 'test'
 *      - n = new
 *      - ce (mandatory)
 *      - value after ce is optional
 *          - if value not present after ce, new event is named 'New Event'
 * 
 * (2) Add time to Event → event -t '1230-2100' 'Event'
 *      - t = time
 *      - 2 values
 *          - time
 *              - value must be provided as <'xxxx-xxxx'>
 *          - event name  
 *              - if ce is before -t, then there is no need for event name 
 *                  - event ce 'event' -t 'xxxx-xxxx' 
 * (3) Add a date to the Event → event -d 'dd-mm-yyyy' 'Event'
 *      - d = date
 *      - 2 values
 *          - date
 *              - value must be provided as <'dd-mm-yyyy'>
 *          - event name
 *              - if ce is before -d, then there is no need for event name
 *                  - event ce 'event' -d 'dd-mm-yyyy'
 * 
 * Conjunction of 1,2,3 → event ce 'Birthday' -d '07-07-2002' -t '0-1200'
 * 
 * (4) View the list of events in a day → ls 'dd-mm-yyyy'
 *      - ls = list
 *      - 1 parameter (optional)
 *          - date
 *              - value must be provided as <'dd-mm-yyyy'>
 *          - no parameters
 *              - list current day events
 *      - Possible Combos:
 *              - ls '04-20-2020'
 *              - ls
 *  
 */

public class commands {

    String command;
    String flags[] = { "ce", "-d", "-t" };
    List<event> list = new ArrayList<event>();

    public commands(String command) {
        this.command = command;
        this.command = sanitizeCommand(command);
    }

    public List<event> getListOfEvents() {
        return this.list;
    }

    public String sanitizeCommand(String command) {
        return this.command.replaceAll("\\s{2,}", " ");
    }

    private boolean boundsCheck(int i, String text[]) {
        if (i == text.length - 1) {
            return false;
        }

        // Checks if value after flag is an actual title, not a garbage value
        else if (!(i < text.length - 1 && (text[i + 1].charAt(0) == text[i + 1].charAt(text[i + 1].length() - 1)
                && text[i + 1].charAt(0) == '\''))) {
            return false;
        }

        else if (text[i].equalsIgnoreCase("-d") || text[i].equalsIgnoreCase("-t")) {
            int cnt = 0;
            for (int j = i - 1; j > 0; j--) {
                if (text[j].equalsIgnoreCase("ce")) {
                    cnt++;
                }
            }
            if (cnt > 1)
                return false;
        }

        else if (text[i].equalsIgnoreCase("ce")) {
            for (int j = i - 1; j > 0; j--) {
                if (text[j].equalsIgnoreCase("ce") || text[j].equalsIgnoreCase("-d")
                        || text[j].equalsIgnoreCase("-t")) {
                    return false;
                }
            }
        }

        // Keeps track of duplicate flags
        int duplicates[] = new int[flags.length];

        for (int j = i + 1; j < text.length; j++) {
            if (text[j].equalsIgnoreCase("ce")) {
                return false;
            } else if (text[j].equalsIgnoreCase(flags[1])) {
                duplicates[1]++;
                if (duplicates[1] >= 2) {
                    return false;
                }
            } else if (text[j].equalsIgnoreCase(flags[2])) {
                duplicates[2]++;
                if (duplicates[2] >= 2) {
                    return false;
                }
            }
        }

        return true;
    }

    public boolean verifyCommand() {
        String text[] = command.split(" ");

        if (!text[0].equalsIgnoreCase("event"))
            return false;

        for (int i = 1; i < text.length; i++) {

            if (text[i].length() <= 1)
                return false;

            if (text[i].equalsIgnoreCase("ce") || text[i].equalsIgnoreCase("-d") || text[i].equalsIgnoreCase("-t")) {
                if (!boundsCheck(i, text))
                    return false;
            }
        }
        return true;
    }

    public boolean executeCommand(String command) {
        if (!verifyCommand())
            return false;

        // Based on command, execute stuff, then return true
        String text[] = command.split(" ");

        for (int i = 1; i < text.length; i++) {
            if (text[i].equalsIgnoreCase("ce")) {
                String title = text[i + 1];
                String time = null;
                String date = null;
                for (int j = i + 2; j < text.length; j++) {
                    if (text[j].equalsIgnoreCase("-d")) {
                        date = text[j + 1];
                    }

                    if (text[j].equalsIgnoreCase("-t")) {
                        time = text[j + 1];
                    }
                }
                this.list.add(newEvent(title, date, time));
                return true;
            }

        }
        return false;
    }

    public event newEvent(String name, String date, String time) {
        event e;
        if (date == null && time == null) {
            e = new event(name);
        } else if (date == null) {
            e = new event(name, time);
        } else if (time == null) {
            e = new event(name, date);
        } else {
            e = new event(name, date, time);
        }

        return e;
    }

}
