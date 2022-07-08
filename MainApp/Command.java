package MainApp;

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
        // fix this to tackle when name arg is more than 1 word
        if (text[i].equals("ce")) {

            String parameters[] = new String[3];
            parameters[0] = sanitizeArgument(getArgument(text, i + 1));
            int cnt = 1;

            for (int j = 1; j < text.length; j++) {
                if (text[j].equals("-t"))
                    parameters[cnt++] = sanitizeArgument(getArgument(text, j + 1));
                if (text[j].equals("-d"))
                    parameters[cnt++] = sanitizeArgument(getArgument(text, j + 1));
            }
            // System.out.println(Arrays.toString(parameters));
            CreateEvent obj = new CreateEvent(parameters);
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
            if (argument.charAt(0) == '\'' && argument.charAt(argument.length() - 1) == '\'') {
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
        // Should work without this
        if (argument.length() != 9) {
            // m.outputMessage(m.getErrorMessage("lblInvalidTime", null), 'e');
            return false;
        }

        try {
            int startTime = Integer.parseInt(argument.substring(0, argument.indexOf('-')));
            int endTime = Integer.parseInt(argument.substring(argument.indexOf('-') + 1, argument.length()));

            if (!((startTime <= endTime) && (0 <= startTime && startTime <= 2359)
                    && (0 <= endTime && endTime <= 2359))) {
                m.outputMessage(m.getErrorMessage("lblInvalidTime", null), 'e');
                return false;
            }
        } catch (NumberFormatException e) {
            m.outputMessage(m.getErrorMessage("lblNonNumerical", null), 'e');
            return false;
        }
        return true;
    }

    // ! Fix Verification of Date input -> Error message appears when no date is
    // ! supplied -> no error message should appear
    public boolean verifyDateInput(String argument) {
        int values[] = new int[3];
        int cnt = 0;
        int index = 0;
        for (int j = 0; j < argument.length(); j++) {
            try {
                if (argument.charAt(j) == '-') {
                    values[cnt++] = Integer.parseInt(argument.substring(index, j));
                    index = j + 1;
                }

                if (j == argument.length() - 1) {
                    values[cnt++] = Integer.parseInt(argument.substring(index, j + 1));
                }

            } catch (NumberFormatException e) {
                m.outputMessage(m.getErrorMessage("lblNonNumerical", null), 'e');
                return false;
            }
        }

        if (!((1 <= values[0] && values[0] <= 12) && (1 <= values[1] && values[1] <= 31)
                && (2022 <= values[2] && values[2] <= 3022))) {
            m.outputMessage(m.getErrorMessage("lblInvalidDate", null), 'e');
            return false;
        }

        return true;
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
