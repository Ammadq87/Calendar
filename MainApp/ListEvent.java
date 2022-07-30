package MainApp;

import java.util.*;
import java.util.regex.Pattern;

public class ListEvent extends Command implements ICommand {

    String command = "";
    String parameters[]; // (1 or 2) date(s), (1 or 2) time(s)
    List<String[]> params = new ArrayList<String[]>();
    Event e = new Event();
    String schedule[][] = new String[96][2];

    public ListEvent(String command) {
        this.command = command;
    }

    @Override
    public void execute() {

        String query = createQuery("events");

        if (query == null) {
            m.outputMessage(m.getErrorMessage("lblQueryFailed", "null"), 'e');
            m.outputMessage(m.getErrorMessage("lblInvalidArgument", null), 'e');
            return;
        }

        List<String[]> l = super.getResultsFromQuery(query, "name-s", "startTime-i",
                "endTime-i", "eventDate-s");

        if (this.command.contains("-t")) {
            initializeScheduleFormat(convertTimeToIndex(this.e.startTime), convertTimeToIndex(this.e.endTime));
        } else {
            initializeScheduleFormat(0, this.schedule.length);
        }

        for (String s[] : l) {

            int start = convertTimeToIndex(s[1]);
            int end = convertTimeToIndex(s[2]);

            for (int i = start; i <= end; i++) {
                if (i == start) {
                    this.schedule[i][1] = this.schedule[i][1] != null ? this.schedule[i][1] + " CONFLICTS WITH " + s[0]
                            : s[0];
                } else if (i != start && i != end) {
                    this.schedule[i][1] = this.schedule[i][1] != null ? this.schedule[i][1] + " CONFLICTS WITH " + s[0]
                            : formatTitle('|', s[0].length());
                } else if (i == end) {
                    this.schedule[i][1] = this.schedule[i][1] != null ? this.schedule[i][1] + " CONFLICTS WITH " + s[0]
                            : formatTitle('V', s[0].length());
                }
            }
        }
        printSchedule();
    }

    private int convertTimeToIndex(Object obj) {
        if (obj == null)
            return 0;
        if (obj instanceof String) {
            return ((Integer.parseInt((String) obj)) % 100) / 15 + ((Integer.parseInt((String) obj)) / 100) * 4;
        } else if (obj instanceof Integer) {
            return (((Integer) obj) % 100) / 15 + ((((Integer) obj)) / 100) * 4;
        }
        return 0;
    }

    private void initializeScheduleFormat(int start, int end) {
        int hour = -1;
        for (int i = 0; i < this.schedule.length; i++) {
            if ((i * 15) % 60 == 0) {
                hour += 1;
                this.schedule[i][0] = "[" + (hour < 10 ? "0" + hour : "" + hour) + ":00]";
            } else {

                this.schedule[i][0] = "[" + (hour < 10 ? "0" + hour : "" + hour) + ":" + (i * 15) % 60 + "]";
            }

            if (!(start <= i && i <= end)) {
                this.schedule[i][1] = " ";
            } else {
                // this.schedule[i][0] += " > ";
            }
        }

    }

    public void printSchedule() {
        for (int i = 0; i < this.schedule.length; i++) {
            String output = this.schedule[i][0] + " " + (this.schedule[i][1] == null ? " " : this.schedule[i][1]);
            System.out.println(output);
        }
    }

    public String formatTitle(char n, int spc) {
        String output = "";
        for (int i = 0; i < spc / 2; i++) {
            output += " ";
        }
        output += n;
        return output;
    }

    // Fix query statements
    @Override
    public String createQuery(String table) {
        this.params = arguments();
        String query = "SELECT * FROM " + table;

        if (this.params == null || this.params.size() == 0) {
            return query + " WHERE month = " + this.e._date[0] + " AND day = " + this.e._date[1]
                    + " AND year = "
                    + this.e._date[2];
        }

        // 0 date, 1 AND, 2 time
        query += " WHERE {0} {1} {2};";

        int errorCount = 0;

        for (int i = 0; i < this.params.size(); i++) {
            String param = this.params.get(i)[0];
            String arg = this.params.get(i)[1];
            if (param.equals("t")) {
                if (!setTimeParameter(arg, this.e)) {
                    errorCount++;
                } else {
                    query = query.replace("{2}",
                            "startTime >= " + this.e.startTime + " AND endTime <= " + this.e.endTime);
                }
            } else if (param.equals("d")) {
                if (!setDateParameter(arg, this.e)) {
                    errorCount++;
                } else {
                    query = query.replace("{0}",
                            "month = " + this.e._date[0] + " AND day = " + this.e._date[1]
                                    + " AND year = "
                                    + this.e._date[2]);
                }
            }
        }

        if (errorCount == 0) {
            // Only Time Option
            if (query.contains("{0}") && !query.contains("{2}")) {
                query = query.replace("{0}", "month = " + this.e._date[0] + " AND day = " + this.e._date[1]
                        + " AND year = "
                        + this.e._date[2]);
                query = query.replace("{1}", "AND");
            }

            // Only Date Option
            else if (!query.contains("{0}") && query.contains("{2}")) {
                query = query.replace("{2}", "");
                query = query.replace("{1}", "");
            }

            else if (!query.contains("{0}") && !query.contains("{2}")) {
                query = query.replace("{1}", "AND");
            }
        } else {
            query = null;
        }

        return query;
    }

    // TODO: include feature where a time and/or date interval can be included
    @Override
    public boolean validateCommand() {
        if (this.command == null || this.command.length() == 0)
            return false;
        if (Pattern.matches("event ls [--][d] '[0-9]{1,3}-[0-9]{1,3}-[0-9]{4}'{1} [-][t] '[0-9]{3,5}-[0-9]{3,5}'{1}",
                this.command))
            return true;
        else if (Pattern.matches(
                "event ls [-][t] '[0-9]{3,5}-[0-9]{3,5}'{1} [--][d] '[0-9]{1,3}-[0-9]{1,3}-[0-9]{4}'{1}", this.command))
            return true;
        else if (Pattern.matches("event ls [--][d] '[0-9]{1,3}-[0-9]{1,3}-[0-9]{4}'{1}", this.command))
            return true;
        else if (Pattern.matches("event ls [-][t] '[0-9]{3,5}-[0-9]{3,5}'{1}", this.command))
            return true;
        else if (Pattern.matches("event ls", this.command))
            return true;
        return false;
    }

    @Override
    public List<String[]> arguments() {
        String text[] = this.command.split(" ");
        List<String[]> params = new ArrayList<String[]>();

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