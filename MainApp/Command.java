package MainApp;

import java.util.regex.Pattern;

public class Command extends DBAccess {
    String command;
    String flags[] = { "-d", "-t" };
    String listOfCommands[] = { "ce", "ls", "rm" };

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
        this.command = c;
        sanitizeCommand(this.command);
    }

    public void setCommand(String c) {
        sanitizeCommand(c);
        this.command = c;
    }

    // Gets rid of extra spaces
    public void sanitizeCommand(String c) {
        c.replaceAll("\\s{2,}", " ");
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
        }

        // If for some reason text[1] is found but doesn't go through switch, then
        // return CommandNotFound
        m.outputMessage(m.getErrorMessage("lblCommandNotFound", null), 'e');
        return false;
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
    public String sanitizeArgument(String arg) {
        return arg.substring(1, arg.length() - 1);
    }

    public boolean verifyTimeInput(String argument) {
        if (Pattern.matches("[0-9]{3,5}-[0-9]{3,5}", argument)) {
            int startTime = Integer.parseInt(argument.substring(0, argument.indexOf('-')));
            int endTime = Integer.parseInt(argument.substring(argument.indexOf('-') + 1, argument.length()));

            if (!((startTime <= endTime) && (0 <= startTime && startTime <= 2359)
                    && (0 <= endTime && endTime <= 2359))) {
                m.outputMessage(
                        m.getErrorMessage("lblInvalidTime",
                                "Start time cannot be greater than End time OR times should be between [0 and 2400)"),
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
        // String flag = sanitizeArgument(f);
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
