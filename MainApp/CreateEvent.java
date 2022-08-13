package MainApp;

import java.util.*;
import java.util.regex.Pattern;

public class CreateEvent extends Command implements ICommand {
    private List<String[]> params = new ArrayList<String[]>();
    private String command = "";
    private Map<String, String> arguments;

    public CreateEvent(String command) {
        this.command = command;
    }

    @Override
    public void execute() {

        this.arguments = getArguments();

        if (this.arguments == null || this.arguments.size() == 0) {
            m.outputMessage(m.getErrorMessage("lblQueryFailed", this.command), 'e');
            return;
        }

        int errorCount = 0;

        if (this.arguments.containsKey("n"))
            super.e.setName(this.arguments.get("n"));
        if (this.arguments.containsKey("t")) {
            if (!setTimeParameter(this.arguments.get("t"), super.e))
                errorCount++;
        }
        if (this.arguments.containsKey("d")) {
            if (!setDateParameter(this.arguments.get("d"), super.e))
                errorCount++;
        }

        if (errorCount == 0)
            super.executeQuery(createQuery("events"), 1000);
        else {
            m.outputMessage(m.getErrorMessage("lblQueryFailed", this.command), 'e');
            return;
        }
    }

    @Override
    public String createQuery(String table) {
        String query = "INSERT INTO " + table + " VALUES (";
        String value = (super.e.startTime == 0 && super.e.endTime == 2359)
                ? "{0}, \"" + super.e.name + " <All-Day>\", {1}, {2});"
                : "{0}, \"" + super.e.name + "\", {1}, {2});";

        value = value.replace("{2}",
                "\"" + super.e.getDate() + "\", " + super.e._date[0] + ", " + super.e._date[1] + ", "
                        + super.e._date[2]);
        value = value.replace("{1}", super.e.startTime + ", " + super.e.endTime);

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

    public Map<String, String> getArguments() {

        Map<String, String> args = new HashMap<String, String>();

        if (this.command == null || this.command.length() == 0) {
            return null;
        }

        String text[] = this.command.split(" ");
        args.put("n", sanitizeArgument(getArgument(text, 2)));
        for (int i = 0; i < text.length; i++) {
            if (text[i].equals("-t")) {
                args.put("t", sanitizeArgument(getArgument(text, i + 1)));
            } else if (text[i].equals("-d")) {
                args.put("d", sanitizeArgument(getArgument(text, i + 1)));
            }
        }

        return args;

    }
}
