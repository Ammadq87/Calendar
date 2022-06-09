package Calendar;

public class event {
    String name = "Unknown Event";
    String date = "Unknown Date";
    int startTime, endTime;

    public event(String n) {
        this.name = n;
    }

    public event(String n, String x) {

        this.name = n;

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
            this.startTime = Integer.parseInt(x.substring(1, cnt));
            this.endTime = Integer.parseInt(x.substring(cnt + 1, x.length() - 1));
        }
    }

    public event(String n, String d, String t) {
        this.name = n;
        this.date = d;

        int dash = t.indexOf('-');
        this.startTime = Integer.parseInt(t.substring(1, dash));
        this.endTime = Integer.parseInt(t.substring(dash + 1, t.length() - 1));
    }

    public String eventToString() {
        return this.name + " from " + this.startTime + " to " + this.endTime + " on " + this.date;
    }

}
