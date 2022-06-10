package Calendar;

import java.util.*;

public class commands {

    String command;
    String flags[] = { "ce", "-d", "-t", "ls" };
    String commandList[] = { "ce", "ls" };
    boolean printToScreen = false;
    String print = "";
    List<event> list = new ArrayList<event>();

    public commands(String command) {
        this.command = command;
        this.command = sanitizeCommand(command);
    }

    public List<event> getListOfEvents() {
        return this.list;
    }

    public String sanitizeCommand(String command) {
        return this.command.replaceAll("\\s{2,}", " ");
    }

    // Verifies argument after command
    private boolean verifyArgument(String text[], int i) {
        if (text[i].equalsIgnoreCase("ls") && i == text.length - 1) {
            return true;
        }

        return (i < text.length - 1 && (text[i + 1].charAt(0) == text[i + 1].charAt(text[i + 1].length() - 1)
                && text[i + 1].charAt(0) == '\''));
    }

    private boolean isCommand(String text) {
        if (text.length() >= 1) {
            if (text.charAt(0) == '\'')
                return false;
            else if (text.charAt(0) == '-')
                return false;
        }

        return true;
    }

    private boolean isDuplicateOrUnknownValue(String text[], int i) {
        int cnt = 0;

        if (isCommand(text[i])) {
            for (int j = i + 1; j < text.length; j++) {
                if (isCommand(text[j])) {
                    return true;
                }
            }
            return !(i == 1);
        }

        else {
            for (int j = 1; j < text.length; j++) {
                if (j != i && text[j].equalsIgnoreCase(text[i])) {
                    return true;
                }

                if (isCommand(text[j]) && j != i) {
                    cnt++;
                }

            }

            // If the count is greater than 1, there is more than 1 command
            return cnt > 1;
        }

    }

    private boolean boundsCheck(int i, String text[]) {
        if (!text[i].equalsIgnoreCase("ls") && i == text.length - 1) {
            System.out.println("1");
            return false;
        }

        if (isDuplicateOrUnknownValue(text, i)) {
            System.out.println("2");

            return false;
        }

        if (isCommand(text[i]) && !verifyArgument(text, i)) {
            System.out.println("3");

            return false;
        }

        return true;
    }

    public boolean verifyCommand() {
        String text[] = command.split(" ");

        if (!text[0].equalsIgnoreCase("event"))
            return false;

        for (int i = 1; i < text.length; i++) {
            if (text[i].length() <= 1)
                return false;

            // Needs a better way of identifying commands
            if (text[i].equalsIgnoreCase("ls") || text[i].equalsIgnoreCase("ce") || text[i].equalsIgnoreCase("-d")
                    || text[i].equalsIgnoreCase("-t")) {
                if (!boundsCheck(i, text))
                    return false;
            }
        }
        return true;
    }

    public boolean executeCommand(String command) {
        if (!verifyCommand())
            return false;

        // Based on command, execute stuff, then return true
        String text[] = command.split(" ");

        for (int i = 1; i < text.length; i++) {
            if (text[i].equalsIgnoreCase("ce")) {
                String title = text[i + 1];
                String time = null;
                String date = null;
                for (int j = i + 2; j < text.length; j++) {
                    if (text[j].equalsIgnoreCase("-d")) {
                        date = text[j + 1];
                    }

                    if (text[j].equalsIgnoreCase("-t")) {
                        time = text[j + 1];
                    }
                }
                this.list.add(newEvent(title, date, time));
                return true;
            }

            if (text[i].equalsIgnoreCase("ls")) {
                this.printToScreen = true;
                this.print += "Hi\n";
            }

        }
        return false;
    }

    public event newEvent(String name, String date, String time) {
        event e;
        if (date == null && time == null) {
            e = new event(name);
        } else if (date == null) {
            e = new event(name, time);
        } else if (time == null) {
            e = new event(name, date);
        } else {
            e = new event(name, date, time);
        }

        return e;
    }

}
