package MainApp;

import java.util.Arrays;
import java.util.regex.Pattern;

public class Command extends DBAccess {
    private String command = "";
    private String flags[] = { "-d", "-t", "-delete", "-edit" };
    private String listOfCommands[] = { "ce", "ls", "rm", "find" };
    public Event e = new Event();
    /*
     * Input
     * -> Command
     * > check if command exists
     * True: create Command object based on command
     * Command Object:
     * > verify command based on Command object's validation method
     * Verification True: Gather and set arugments based on command
     * > Execute command
     * Verification False: Return message
     * False: Return message
     * 
     */

    public Command() {

    }

    public Command(String c) {
        sanitizeCommand(c);
    }

    public void setCommand(String c) {
        sanitizeCommand(c);
    }

    public void sanitizeCommand(String c) {
        this.command = "";
        String text[] = c.split(" ");
        for (int i = 0; i < text.length; i++) {
            if (!(text[i].equals(" ") || text[i].equals(""))) {
                this.command += (i == text.length - 1) ? text[i] : text[i] + " ";
            }
        }
    }

    public String getCommand() {
        return this.command;
    }

    public boolean isCommandValid() {
        boolean found = false;
        String text[] = this.command.split(" ");
        if (text.length <= 1) {
            m.outputMessage(m.getErrorMessage("lblCommandNotFound", null), 'e');
            return false;
        }

        for (String c : listOfCommands) {
            if (c.equals(text[1])) {
                found = true;
                break;
            }
        }

        if (!found) {
            m.outputMessage(m.getErrorMessage("lblCommandNotFound", null), 'e');
            return false;
        }

        switch (text[1]) {
            case "ce":
                return CreateEventObject();
            case "ls":
                return ListEventObject();
            case "find":
                return FindEventObject();
        }

        // If for some reason text[1] is found but doesn't go through switch, then
        // return CommandNotFound
        m.outputMessage(m.getErrorMessage("lblCommandNotFound", null), 'e');
        return false;
    }

    /*
     * Initialization of Objects should be refactored
     */
    private boolean FindEventObject() {
        FindEvent obj = new FindEvent(this.command);
        boolean executable = obj.validateCommand();
        if (executable) {
            obj.execute();
        } else {
            m.outputMessage(m.getErrorMessage("lblCommandFailed", this.command), 'e');
        }
        return executable;
    }

    private boolean ListEventObject() {
        ListEvent obj = new ListEvent(this.command);
        boolean executable = obj.validateCommand();
        if (executable) {
            obj.execute();
        } else {
            m.outputMessage(m.getErrorMessage("lblCommandFailed", this.command), 'e');
        }
        return executable;
    }

    private boolean CreateEventObject() {
        CreateEvent obj = new CreateEvent(this.command);
        boolean executable = obj.validateCommand();
        if (executable) {
            obj.execute();
        } else {
            m.outputMessage(m.getErrorMessage("lblCommandFailed", this.command), 'e');
        }
        return executable;
    }

    // Assuming input is like: <'argument'>
    // Catch nullPointerException
    public String sanitizeArgument(String arg) {
        if (arg == null || arg.equals(""))
            return null;

        return (arg.charAt(0) == '\'' && arg.charAt(arg.length() - 1) == '\'') ? arg.substring(1, arg.length() - 1)
                : null;
    }

    public boolean verifyTimeInput(String argument) {
        if (argument == null || argument.length() == 0) {
            return false;
        }

        if (Pattern.matches("[0-9]{3,5}-[0-9]{3,5}", argument)) {
            int startTime = Integer.parseInt(argument.substring(0, argument.indexOf('-')));
            int endTime = Integer.parseInt(argument.substring(argument.indexOf('-') + 1, argument.length()));

            if (!((startTime <= endTime) && (0 <= startTime && startTime <= 2400)
                    && (0 <= endTime && endTime <= 2400))) {
                m.outputMessage(
                        m.getErrorMessage("lblInvalidTime",
                                "Start time cannot be greater than End time OR times should be between [0 and 2400]"),
                        'e');
                return false;
            } else if (!((startTime % 100) % 15 == 0 && (endTime % 100) % 15 == 0)) {
                m.outputMessage(m.getErrorMessage("lblInvalidTime", "Time must be in 15-minute intervals"),
                        'e');
                return false;
            }
            return true;
        }
        m.outputMessage(m.getErrorMessage("lblInvalidTime", null), 'e');
        return false;
    }

    public boolean verifyDateInput(String argument) {
        int dates[][] = { { 1, 31 }, { 2, 28 }, { 3, 31 }, { 4, 30 }, { 5, 31 }, { 6, 30 }, { 7, 31 }, { 8, 31 },
                { 9, 30 }, { 10, 31 }, { 11, 30 }, { 12, 31 } };
        if (Pattern.matches("[0-9]{1,3}-[0-9]{1,3}-[0-9]{4}", argument)) {
            int month = Integer.parseInt(argument.substring(0, argument.indexOf('-')));
            int day = Integer.parseInt(argument.substring(argument.indexOf('-') + 1, argument.lastIndexOf('-')));
            int year = Integer.parseInt(argument.substring(argument.lastIndexOf('-') + 1, argument.length()));
            if ((month > 12 || month <= 0) || year < 2022) {
                m.outputMessage(m.getErrorMessage("lblInvalidDate", "Month or Year are out of scope"), 'e');
                return false;
            }
            if (dates[month - 1][1] >= day && day >= 1) {
                return true;
            }
        }
        m.outputMessage(m.getErrorMessage("lblInvalidDate", null), 'e');
        return false;
    }

    public boolean setDateParameter(String date, Event e) {
        boolean verified = verifyDateInput(date);
        if (verified) {
            String temp[] = date.split("-");
            int dates[] = new int[3];
            for (int i = 0; i < temp.length; i++) {
                dates[i] = Integer.parseInt(temp[i]);
            }
            e.setDates(dates);
        }
        return verified;
    }

    public boolean setTimeParameter(String time, Event e) {
        boolean verified = verifyTimeInput(time);
        if (verified) {
            int start = Integer.parseInt(time.substring(0, time.indexOf('-')));
            int end = Integer.parseInt(time.substring(time.indexOf('-') + 1, time.length()));
            e.setTimes(start, end);
        }
        return verified;
    }

    // 'i' should be the index in front of the command
    public String getArgument(String text[], int i) {
        if (text == null || text.length == 0 || i < 0)
            return null;
        String joined = "";
        for (int j = i; j < text.length; j++) {
            if (isCommand(text[j]) || isFlag(text[j]))
                break;
            joined += text[j] + " ";
        }

        if (joined != "" && joined.charAt(joined.length() - 1) == ' ')
            joined = joined.substring(0, joined.length() - 1);

        return joined;
    }

    private boolean isFlag(String f) {
        if (f.charAt(0) == '-') {
            for (int i = 0; i < this.flags.length; i++) {
                if (this.flags[i].equals(f))
                    return true;
            }
            return false;
        }
        return false;
    }

    private boolean isCommand(String c) {
        for (int i = 0; i < this.listOfCommands.length; i++) {
            if (this.listOfCommands[i].equals(c))
                return true;
        }
        return false;
    }
}
