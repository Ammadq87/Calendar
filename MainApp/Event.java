
package MainApp;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.time.LocalDateTime;
/* Event Hierarchy:
 * -> CLASS event
 * 		-> handles basic information about the event
 * 		-> CLASS eventHandler (might be overkill)
 * 			-> checks for validity of event (ex; events do not collide)
 * 			-> stores event into database
 * 
 */

public class Event {
    String name = "Unknown Event";
    String date = "Unknown Date";
    int _date[] = new int[3];
    int startTime, endTime = 2359;
    int uid = 1;

    public Event(String n, int id) {
        this.name = sanitizeString(n);
        this.uid = id;
    }

    public Event(String n, String x, int id) {

        this.name = sanitizeString(n);
        x = sanitizeString(x);
        this.uid = id;

        int cnt = 0;
        for (int i = 0; i < x.length(); i++) {
            if (x.charAt(i) == '-')
                cnt++;
        }

        // x is date
        if (cnt > 1) {
            this.date = x;
        }

        // x is time
        else {
            cnt = x.indexOf('-');
            this.startTime = Integer.parseInt(x.substring(0, cnt));
            this.endTime = Integer.parseInt(x.substring(cnt + 1, x.length()));
        }
    }

    public Event(String n, String d, String t, int id) {
        this.name = sanitizeString(n);
        this.date = sanitizeString(d);
        t = sanitizeString(t);
        this.uid = id;

        int dash = t.indexOf('-');
        this.startTime = Integer.parseInt(t.substring(0, dash));
        this.endTime = Integer.parseInt(t.substring(dash + 1, t.length()));
    }

    public Event() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDateTime now = LocalDateTime.now();

        String defaultValues[] = dtf.format(now).split(" ");
        String dates[] = defaultValues[0].split("/");
        for (int i = 0; i < dates.length; i++) {
            this._date[i] = Integer.parseInt(dates[i]);
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDates(int dates[]) {
        for (int i = 0; i < dates.length; i++) {
            this._date[i] = dates[i];
        }
    }

    public void setTimes(int startTime, int endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public String sanitizeString(String text) {
        return text.substring(1, text.length() - 1);
    }

    public String eventToString() {
        return "(" + this.uid + ") " + this.name + " from [" + getTime() + "] on [" + getDate() + "]";
    }

    public String getTime() {
        return this.startTime + " - " + this.endTime;
    }

    public String getDate() {
        return this._date[0] + "-" + this._date[1] + "-" + this._date[2];
    }

}
