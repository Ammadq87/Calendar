package MainApp;

import java.util.Arrays;

import javax.lang.model.util.ElementScanner6;

public class CreateEvent extends Command {
    static int id = 6;
    String parameters[] = new String[3]; // name, date/time
    Event e = new Event();

    public CreateEvent(String parameters[]) {
        this.parameters = parameters;
    }

    public void execute() {
        // System.out.println("\nCreating New Event...");
        e.setName(this.parameters[0]);

        for (int i = 1; i < this.parameters.length; i++) {
            if (isTime(this.parameters[i])) {
                setTimeParameter(this.parameters[i]);
            }

            if (this.parameters[i] != null && !isTime(this.parameters[i])) {
                setDateParameter(this.parameters[i]);
            }
        }

        // System.out.println(e.eventToString());
        DBAccess db = new DBAccess();
        db.executeQuery(createQuery("events"));
        // db.insertData("events", e);
        // Submit to db
    }

    public String createQuery(String table) {
        String query = "INSERT INTO " + table + " VALUES (";
        String value = "";

        boolean fullParameterList = true;
        for (int i = 0; i < this.parameters.length; i++) {
            if (this.parameters[i] == null)
                fullParameterList = false;
        }

        if (!fullParameterList) {

        } else {
            value += (this.id++) + ", ";
            value += "\"" + this.e.name + "\", ";

            if (verifyTimeInput(this.parameters[1]) || verifyTimeInput(this.parameters[2]))
                value += e.startTime + ", " + e.endTime + ", ";
            else
                value += "NULL, ";

            if (verifyDateInput(this.parameters[1]) || verifyDateInput(this.parameters[2]))
                value += "\"" + e.getDate() + "\", " + e._date[0] + ", " + e._date[1] + ", " + e._date[2] + ");";
            else
                value += "NULL);";

            query += value;
        }

        System.out.println(query);
        return query;
    }

    public void setDateParameter(String date) {
        String temp[] = date.split("-");
        int dates[] = new int[3];
        for (int i = 0; i < temp.length; i++) {
            dates[i] = Integer.parseInt(temp[i]);
        }
        e.setDates(dates);
    }

    public void setTimeParameter(String time) {
        int start = Integer.parseInt(time.substring(0, time.indexOf('-')));
        int end = Integer.parseInt(time.substring(time.indexOf('-') + 1, time.length()));
        e.setTimes(start, end);
    }

    private boolean isTime(String time) {
        if (time == null || time == "")
            return false;
        return time.length() == 9;
    }

}
