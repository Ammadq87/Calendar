package MainApp;

import java.util.regex.Pattern;

public class Command extends DBAccess {
    String command;
    String flags[] = { "-d", "-t" };
    String listOfCommands[] = { "ce", "ls", "rm" };

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

    // Assuming input is like: <'argument'>
    public String sanitizeArgument(String arg) {
        return arg.substring(1, arg.length() - 1);
    }

    public void executeCommand() {
        String text[] = this.command.split(" ");
        createCommand(text, 1);
    }

    private void createCommand(String text[], int i) {
        String parameters[];
        int cnt = 0;
        if (text[i].equals("ce")) {
            parameters = new String[3];
            parameters[0] = sanitizeArgument(getArgument(text, i + 1));
            cnt = 1;
            for (int j = 1; j < text.length; j++) {
                if (text[j].equals("-t"))
                    parameters[cnt++] = sanitizeArgument(getArgument(text, j + 1));
                else if (text[j].equals("-d"))
                    parameters[cnt++] = sanitizeArgument(getArgument(text, j + 1));
            }
            // System.out.println(Arrays.toString(parameters));
            CreateEvent obj = new CreateEvent(parameters);
            obj.execute();
        }

        else if (text[i].equals("ls")) {
            parameters = new String[2];
            for (int j = 1; j < text.length; j++) {
                if (text[j].equals("-t"))
                    parameters[cnt++] = sanitizeArgument(getArgument(text, j + 1));
                else if (text[j].equals("-d"))
                    parameters[cnt++] = sanitizeArgument(getArgument(text, j + 1));
            }

            ListEvent obj = new ListEvent(parameters);
            obj.execute();

        }

        else if (text[i].equals("find")) {
            parameters = new String[1];
            parameters[0] = sanitizeArgument(getArgument(text, i + 1));
            FindEvent obj = new FindEvent(parameters);
            obj.execute();

        }

    }

    public boolean isCommandValid() {
        String text[] = this.command.split(" ");
        if (!text[0].equalsIgnoreCase("event")) {
            return false;
        }

        for (int i = 1; i < text.length; i++) {
            if (text[i].length() <= 1) {
                m.outputMessage(m.getErrorMessage("lblCommandNotFound", text[i]), 'e');
                return false;
            }

            if (isFlag(text[i]) || isCommand(text[i])) {
                if (!check(text, i)) {
                    return false;
                }
            }
        }

        return true;
    }

    public boolean check(String text[], int i) {
        if (!text[i].equalsIgnoreCase("ls") && i == text.length - 1) {
            m.outputMessage(m.getErrorMessage("lblCommandNotFound", text[i]), 'e');
            return false;
        }

        if (isDuplicateOrUnknownValue(text, i)) {
            m.outputMessage(m.getErrorMessage("lblDuplicateOrUnknownValue", text[i]), 'e');
            return false;
        }

        if ((isCommand(text[i]) || isFlag(text[i])) && !verifyArgument(text, i)) {
            m.outputMessage(m.getErrorMessage("lblInvalidArgument", null), 'e');
            return false;
        }

        return true;
    }

    private boolean verifyArgument(String text[], int i) {
        if (text[i].equalsIgnoreCase("ls") && i == text.length - 1) {
            return true;
        }
        try {
            String argument = getArgument(text, i + 1);
            if (Pattern.matches("'[-_!@#$%^&*()0-9a-zA-Z'\\s+]+'", argument)) {
                argument = sanitizeArgument(argument);

                if (text[i].equalsIgnoreCase("-d")) {
                    return verifyDateInput(argument);
                }

                else if (text[i].equalsIgnoreCase("-t")) {
                    return verifyTimeInput(argument);
                }

                return true;
            }

        } catch (StringIndexOutOfBoundsException e) {
            m.outputMessage(m.getErrorMessage(null, null), 'e');
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public boolean verifyTimeInput(String argument) {
        if (Pattern.matches("[0-9]{3,5}-[0-9]{3,5}", argument)) {
            int startTime = Integer.parseInt(argument.substring(0, argument.indexOf('-')));
            int endTime = Integer.parseInt(argument.substring(argument.indexOf('-') + 1, argument.length()));

            if (!((startTime <= endTime) && (0 <= startTime && startTime <= 2359)
                    && (0 <= endTime && endTime <= 2359))) {
                m.outputMessage(m.getErrorMessage("lblInvalidTime", null), 'e');
                return false;
            }
            return true;
        }
        return false;
    }

    public boolean verifyDateInput(String argument) {
        int dates[][] = { { 1, 31 }, { 2, 28 }, { 3, 31 }, { 4, 30 }, { 5, 31 }, { 6, 30 }, { 7, 31 }, { 8, 31 },
                { 9, 30 }, { 10, 31 }, { 11, 30 }, { 12, 31 } };
        if (Pattern.matches("[0-9]{1,3}-[0-9]{1,3}-[0-9]{4}", argument)) {
            int month = Integer.parseInt(argument.substring(0, argument.indexOf('-')));
            int day = Integer.parseInt(argument.substring(argument.indexOf('-') + 1, argument.lastIndexOf('-')));
            int year = Integer.parseInt(argument.substring(argument.lastIndexOf('-') + 1, argument.length()));
            if (month > 12 || year < 2022) {
                m.outputMessage(m.getErrorMessage("lblInvalidDate", null), 'e');
                return false;
            }
            if (dates[month - 1][1] >= day && day >= 1) {
                return true;
            }
        }
        m.outputMessage(m.getErrorMessage("lblInvalidDate", null), 'e');
        return false;
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

    private boolean isDuplicateOrUnknownValue(String text[], int i) {
        int cnt = 0;
        if (isCommand(text[i])) {
            for (int j = i + 1; j < text.length; j++) {
                if (isCommand(text[j]))
                    return true;
            }
            return !(i == 1);
        } else {
            for (int j = 1; j < text.length; j++) {
                if (j != i && text[j].equalsIgnoreCase(text[i]))
                    return true;

                if (isCommand(text[j]) && j != i)
                    cnt++;
            }
            // If the count is greater than 1, there is more than 1 command
            return cnt > 1;
        }
    }

    public boolean isFlag(String f) {
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

    public boolean isCommand(String c) {
        for (int i = 0; i < this.listOfCommands.length; i++) {
            if (this.listOfCommands[i].equals(c))
                return true;
        }
        return false;
    }
}
