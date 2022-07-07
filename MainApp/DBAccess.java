package MainApp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class DBAccess {

    Connection con = null;
    int currentID = 1;
    String tables[];

    public DBAccess() {
        try {
            String url = "jdbc:mysql://localhost:3306/javaStuff";
            String username = "root";
            String password = "Ammadq87";

            // Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(url, username, password);

            if (con != null) {
                System.out.println("Successfully connected to MySQL database test");
            }

        } catch (SQLException ex) {
            System.out.println("<error: An error occurred while connecting MySQL database >");
            ex.printStackTrace();
        }
    }

    public void executeQuery(String query) {
        try {
            Statement s = con.createStatement();
            s.execute(query);
            System.out.println("\nDone ------------\n");
        } catch (SQLException ex) {
            System.out.println("<error: Could not execute command >");
            ex.printStackTrace();
        }
    }

    public List<String[]> getResultsFromQuery(String query, String... columns) {
        try {
            Statement s = con.createStatement();
            ResultSet r = s.executeQuery(query);
            return printResults(r, columns);
        } catch (SQLException ex) {
            System.out.println("<error: Could not execute command >");
            ex.printStackTrace();
        }

        return null;
    }

    // column = <tableName-i> i-int, b-boolean, s-string, d-double
    private List<String[]> printResults(ResultSet r, String... columns) {
        List<String[]> l = new ArrayList<String[]>();
        try {
            while (r.next()) {
                String result[] = new String[columns.length];
                int i = 0;
                for (String s : columns) {
                    char type = s.charAt(s.indexOf('-') + 1);
                    s = s.substring(0, s.indexOf('-'));
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
                        default:
                            result[i++] = "NULL";
                            break;
                    }
                }
                l.add(result);
            }

        } catch (Exception e) {
            System.out.println("<error: Could not print results>");
            e.printStackTrace();
        }

        return l;
    }

}
