package MainApp;

import java.util.Arrays;
import java.util.regex.Pattern;

import javax.lang.model.util.ElementScanner6;

import MainApp1.Create;

public class CreateEvent extends Command {
    static int id = 7;
    String parameters[] = new String[3]; // name, date/time
    Event e = new Event();

    public CreateEvent() {

    }

    public CreateEvent(String parameters[]) {
        this.parameters = parameters;
    }

    public void execute() {
        e.setName(this.parameters[0]);

        for (int i = 1; i < this.parameters.length; i++) {
            if (this.parameters[i] != null && verifyTimeInput(this.parameters[i])) {
                setTimeParameter(this.parameters[i]);
            }

            else if (this.parameters[i] != null && verifyDateInput(this.parameters[i])) {
                setDateParameter(this.parameters[i]);
            }
        }

        super.executeQuery(createQuery("events"));
    }

    public String createQuery(String table) {
        String query = "INSERT INTO " + table + " VALUES (";
        String value = (this.e.startTime == 0 && this.e.endTime == 2359)
                ? "{0}, \"" + this.e.name + " <All-Day>\", {1}, {2});"
                : "{0}, \"" + this.e.name + "\", {1}, {2});";

        value = value.replace("{2}",
                "\"" + e.getDate() + "\", " + e._date[0] + ", " + e._date[1] + ", " + e._date[2]);
        value = value.replace("{1}", e.startTime + ", " + e.endTime);

        query += value;
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

    public boolean validateCommand(String command) {
        if (Pattern.matches(
                "[a-z]{2,4} '[-_!@#$%^&*()0-9a-zA-Z'\\s+]+' [--][d] '[0-9]{1,3}-[0-9]{1,3}-[0-9]{4}'{1} [-][t] '[0-9]{3,5}-[0-9]{3,5}'{1}",
                command))
            return true;
        else if (Pattern.matches(
                "[a-z]{2,4} '[-_!@#$%^&*()0-9a-zA-Z'\\s+]+' [-][t] '[0-9]{3,5}-[0-9]{3,5}'{1} [--][d] '[0-9]{1,3}-[0-9]{1,3}-[0-9]{4}'{1}",
                command)) {
            return true;
        } else if (Pattern.matches("[a-z]{2,4} '[-_!@#$%^&*()0-9a-zA-Z'\\s+]+' [-][t] '[0-9]{3,5}-[0-9]{3,5}'{1}",
                command)) {
            return true;
        } else if (Pattern.matches(
                "[a-z]{2,4} '[-_!@#$%^&*()0-9a-zA-Z'\\s+]+' [--][d] '[0-9]{1,3}-[0-9]{1,3}-[0-9]{4}'{1}", command)) {
            return true;
        } else if (Pattern.matches("[a-z]{2,4} '[-_!@#$%^&*()0-9a-zA-Z'\\s+]+'", command)) {
            return true;
        }
        return false;
    }
}
