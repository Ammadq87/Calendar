package JDBC;

public class event {
    String name = "Unknown Event";
    String date = "Unknown Date";
    int startTime, endTime;
    int uid = 1;

    public event(String n, int id) {
        this.name = sanitizeString(n);
        this.uid = id;
    }

    public event(String n, String x, int id) {

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

    public event(String n, String d, String t, int id) {
        this.name = sanitizeString(n);
        this.date = sanitizeString(d);
        t = sanitizeString(t);
        this.uid = id;

        int dash = t.indexOf('-');
        this.startTime = Integer.parseInt(t.substring(0, dash));
        this.endTime = Integer.parseInt(t.substring(dash + 1, t.length()));
    }

    public String sanitizeString(String text) {
        return text.substring(1, text.length() - 1);
    }

    public String eventToString() {
        return "(" + this.uid + ") " + this.name + " from " + getTime() + " on " + this.date;
    }

    public String getTime() {
        return "[" + this.startTime + " - " + this.endTime + "]";
    }

}
