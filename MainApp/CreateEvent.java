package MainApp;

import java.util.*;
import java.util.regex.Pattern;

public class CreateEvent extends Command implements ICommand {
    Event e = new Event();
    List<String[]> params = new ArrayList<String[]>();
    String command = "";

    public CreateEvent(String command) {
        this.command = command;
    }

    @Override
    public void execute() {

        // Set parameters
        this.params = arguments();

        // Then execute query
        int errorCount = 0;
        for (int i = 0; i < this.params.size(); i++) {
            String param = this.params.get(i)[0];
            String arg = this.params.get(i)[1];

            if (param.equals("n")) {
                this.e.setName(arg);
            } else if (param.equals("t")) {
                if (!setTimeParameter(arg, this.e)) {
                    errorCount++;
                }
            } else if (param.equals("d")) {
                if (!setDateParameter(arg, this.e)) {
                    errorCount++;
                }
            }
        }

        if (errorCount == 0)
            super.executeQuery(createQuery("events"));
    }

    @Override
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

    @Override
    public boolean validateCommand() {
        if (this.command == null || this.command.length() == 0) {
            return false;
        }
        if (Pattern.matches(
                "event [a-z]{2,4} '[-_!@#$%^&*()0-9a-zA-Z'\\s+]+' [--][d] '[0-9]{1,3}-[0-9]{1,3}-[0-9]{4}'{1} [-][t] '[0-9]{3,5}-[0-9]{3,5}'{1}",
                this.command))
            return true;
        else if (Pattern.matches(
                "event [a-z]{2,4} '[-_!@#$%^&*()0-9a-zA-Z'\\s+]+' [-][t] '[0-9]{3,5}-[0-9]{3,5}'{1} [--][d] '[0-9]{1,3}-[0-9]{1,3}-[0-9]{4}'{1}",
                this.command)) {
            return true;
        } else if (Pattern.matches("event [a-z]{2,4} '[-_!@#$%^&*()0-9a-zA-Z'\\s+]+' [-][t] '[0-9]{3,5}-[0-9]{3,5}'{1}",
                this.command)) {
            return true;
        } else if (Pattern.matches(
                "event [a-z]{2,4} '[-_!@#$%^&*()0-9a-zA-Z'\\s+]+' [--][d] '[0-9]{1,3}-[0-9]{1,3}-[0-9]{4}'{1}",
                this.command)) {
            return true;
        } else if (Pattern.matches("event [a-z]{2,4} '[[.]-_!@#$%^&*()0-9a-zA-Z'\\s+]+'", this.command)) {
            return true;
        }
        return false;
    }

    @Override
    public List<String[]> arguments() {
        if (this.command == null || this.command.length() == 0) {
            return null;
        }

        String text[] = command.split(" ");
        List<String[]> params = new ArrayList<String[]>();

        String name[] = { "n", sanitizeArgument(getArgument(text, 2)) };
        params.add(name);

        for (int i = 1; i < text.length; i++) {
            if (text[i].equals("-t")) {
                String time[] = { "t", sanitizeArgument(getArgument(text, i + 1)) };
                params.add(time);
            } else if (text[i].equals("-d")) {
                String date[] = { "d", sanitizeArgument(getArgument(text, i + 1)) };
                params.add(date);
            }
        }

        return params;
    }
}
