package MainApp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class DBAccess {
    Messages m = new Messages();
    Connection con = null;
    int currentID = 1;

    public DBAccess() {
        try {
            String url = "jdbc:mysql://localhost:3306/javaStuff";
            String username = "root";
            String password = "Ammadq87";

            // Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(url, username, password);

            if (con != null) {
                List<String[]> l = getResultsFromQuery("select eventID from events order by eventID desc", "eventID-i");
                this.currentID = Integer.parseInt(l.get(0)[0]) + 1;
            }

        } catch (SQLException ex) {
            m.outputMessage(m.getErrorMessage("lblFailedConnection", null), 'e');
            ex.printStackTrace();
        }
    }

    public void executeQuery(String query, int CRUD) {
        if (query.contains("{0}")) {
            query = query.replace("{0}", "" + this.currentID);
        }

        try {
            Statement s = con.createStatement();
            s.executeUpdate(query);
            switch (CRUD) {
                case 1000:
                    m.outputMessage(m.getMessage("lblCreate"), 's');
                    break;
                case 0100:
                    m.outputMessage(m.getMessage("lblRead"), 's');
                    break;
                case 0010:
                    m.outputMessage(m.getMessage("lblUpdate"), 's');
                    break;
                case 0001:
                    m.outputMessage(m.getMessage("lblDelete"), 's');
                    break;
            }
        } catch (SQLException ex) {
            m.outputMessage(m.getErrorMessage("lblQueryFailed", query), 'e');
            ex.printStackTrace();
        }
    }

    public List<String[]> getResultsFromQuery(String query, String... columns) {
        if (query.contains("{0}")) {
            query = query.replace("{0}", "" + this.currentID);
        }

        try {
            Statement s = con.createStatement();
            ResultSet r = s.executeQuery(query);
            return printResults(r, columns);
        } catch (SQLException ex) {
            m.outputMessage(m.getErrorMessage("lblCommandFailed", query), 'e');
            ex.printStackTrace();
        }

        return null;
    }

    // column = <tableName-i> i-int, b-boolean, s-string, d-double
    private List<String[]> printResults(ResultSet r, String... columns) {
        List<String[]> l = new ArrayList<String[]>();
        try {
            while (r.next()) {
                String result[] = new String[columns.length == 0 ? 1 : columns.length];

                int i = 0;

                for (String s : columns) {

                    char type = ' ';

                    if (s.indexOf('-') != -1) {
                        type = s.charAt(s.indexOf('-') + 1);
                        s = s.substring(0, s.indexOf('-'));
                    }

                    switch (type) {
                        case 'i':
                            result[i++] = "" + r.getInt(s);
                            break;
                        case 'b':
                            result[i++] = "" + r.getBoolean(s);
                            break;
                        case 'd':
                            result[i++] = "" + r.getDouble(s);
                            break;
                        case 's':
                            result[i++] = r.getString(s);
                            break;

                    }
                }
                l.add(result);
            }

        } catch (Exception e) {
            m.outputMessage(m.getErrorMessage("lblPrintResultsFailed", null), 'e');
            e.printStackTrace();
        }

        return l;
    }

}
