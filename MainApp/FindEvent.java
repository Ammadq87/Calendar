package MainApp;

import java.util.*;
import java.util.regex.Pattern;

public class FindEvent extends Command implements ICommand {

    private String command;

    private Map<String, String> arguments;

    public FindEvent(String command) {
        this.command = command;
    }

    @Override
    public void execute() {
        this.arguments = getArguments();

        /*
         * 1. Output Delete Message if successful delete query
         * 2. If EventName not found, output message to user
         * 3. Implement regex operations for eventName to output other options or
         * options with similar queries
         * a. Select which Event to edit
         * b. Edit Screen with options
         * c. more/less to be added
         */

        if (Boolean.parseBoolean(this.arguments.get("delete"))) {
            super.executeQuery(createQuery("events"), 0001);
            return;
        }

        List<String[]> l = super.getResultsFromQuery(createQuery("events"), "name-s", "startTime-i", "endTime-i",
                "eventDate-s");

        if (Boolean.parseBoolean(this.arguments.get("edit"))
                || !Boolean.parseBoolean(this.arguments.get("delete"))) {
            System.out.println(displayResults(l));
            editScreen(l);
        }

        if (Boolean.parseBoolean(this.arguments.get("delete"))) {
            System.out.println(this.arguments.get("name") + " has been deleted");
        }
    }

    public void editScreen(List<String[]> list) {
        m.outputMessage("Select an option from [1 - " + list.size() + "]", 'r');
        Scanner input = new Scanner(System.in);
        int option = Integer.parseInt(input.nextLine());
        if (0 < option && option <= list.size()) {
            String options[] = { "name", "start-time", "end-time", "date" };
            System.out.println("What do you want to edit? ");
            for (int i = 0; i < options.length; i++) {
                System.out.println("[" + (i + 1) + "] " + options[i]);
            }
            option = Integer.parseInt(input.nextLine());
            boolean found = false;
            while (!found) {
                if (0 < option && option <= options.length){
                   found = true; 
                }

                if (!found) {
                    System.out.println("Please select a valid option");
                    option = Integer.parseInt(input.nextLine());
                }
            }

            if (options[option].equals("name")){
                
            }

        }
    }

    public String displayResults(List<String[]> queryResult) {
        String display = "";

        if (queryResult == null || queryResult.size() == 0)
            return "No Results :(";

        for (int i = 0; i < queryResult.size(); i++) {
            display += "[" + (i + 1) + "] ";
            for (int j = 0; j < queryResult.get(i).length; j++) {
                display += queryResult.get(i)[j] + " ";
            }
            display += "\n";
        }
        return display;
    }

    public String createQuery(String table) {
        if (this.arguments == null || this.arguments.isEmpty()) {
            return null;
        }

        String eventName = this.arguments.get("name");
        String sql = "SELECT * FROM " + table + " WHERE name = \"" + eventName + "\"";

        if (this.arguments.containsKey("delete") && Boolean.parseBoolean(this.arguments.get("delete"))) {
            sql = "DELETE FROM " + table + " WHERE name = \"" + eventName + "\"";
        }

        return sql;
    }

    @Override
    public boolean validateCommand() {
        if (this.command == null || this.command.length() == 0) {
            return false;
        }
        if (Pattern.matches(
                "event find '[-_!@#$%^&*()0-9a-zA-Z'\\s+]+' [--]edit",
                this.command))
            return true;
        else if (Pattern.matches(
                "event find '[-_!@#$%^&*()0-9a-zA-Z'\\s+]+' [--]delete",
                this.command)) {
            return true;
        } else if (Pattern.matches("event find '[-_!@#$%^&*()0-9a-zA-Z'\\s+]+'",
                this.command)) {
            return true;
        }
        return false;
    }

    public Map<String, String> getArguments() {
        Map<String, String> args = new HashMap<String, String>();

        if (this.command == null || this.command.length() == 0) {
            args.put("null", "null");
        } else {
            String text[] = command.split(" ");
            args.put("name", sanitizeArgument(getArgument(text, 2)));
            for (int i = 0; i < text.length; i++) {
                if (text[i].equals("-delete"))
                    args.put("delete", "True");
                else if (text[i].equals("-edit"))
                    args.put("edit", "True");
            }
        }

        return args;
    }
}
