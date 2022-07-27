package MainApp;

import java.util.List;

public class FindEvent extends Command {

    String parameters[];

    public FindEvent(String parameters[]) {
        this.parameters = parameters;
    }

    public void execute() {
        List<String[]> l = super.getResultsFromQuery(createQuery("events"), "name-s", "startTime-i", "endTime-i",
                "eventDate-s");
        for (String s[] : l) {
            for (int i = 0; i < s.length; i++) {

                switch (i) {
                    case 0:
                        System.out.print(">> " + (i + 1) + ". " + s[i]);
                        break;
                    case 1:
                        System.out
                                .print(" occuring at " + s[i] + "" + (Integer.parseInt(s[i]) < 12 ? "am" : "pm") + "-");
                        break;
                    case 2:
                        System.out.print(s[i] + "" + (Integer.parseInt(s[i]) < 12 ? "am" : "pm"));
                        break;
                    case 3:
                        System.out.print(" on " + s[i]);
                }
            }
            System.out.println("");
        }
    }

    public String createQuery(String table) {
        String query = "SELECT * FROM " + table + " WHERE name = \"" + parameters[0] + "\"";
        return query;
    }

}
