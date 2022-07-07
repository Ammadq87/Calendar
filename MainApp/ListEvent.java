package MainApp;

public class ListEvent {

    String parameters[]; // (1 or 2) date(s), (1 or 2) time(s)

    public ListEvent(String parameters[]) {
        this.parameters = parameters;
    }

    public void execute() {
        // String query = getQuery(this.parameters);
    }

    /*
     * Case #1: p[].length is 1 (date)
     * Case #2: p[].length is 1 (time interval)
     * Case #3: p[].length is 2 (date interval)
     * Case #4: p[].length is 2-3 (time and date interval)
     */

    public String getQuery(String parameters[], String table) {
        String query = "SELECT * FROM " + table;

        if (parameters.length == 0) {
            return query;
        }

        query += " WHERE ";

        for (int i = 0; i < parameters.length; i++) {
            if (isDate(parameters[i])) {
                query += appendDate(parameters[i]);
            }

            if (isTime(parameters[i])) {
                // query += appendTime(parameters[i]);
            }
        }

        return null;
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