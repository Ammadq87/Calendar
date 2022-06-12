package Calendar;

import java.util.*;
import java.sql.*;

public class app {

    public static void main(String args[]) {
        new app();
    }

    Connection connection = null;

    public app() {
        connectToDB();
        Scanner input = new Scanner(System.in);
        System.out.println("Enter Command. Type -h for help");
        String cmd = input.nextLine();

        commands c = new commands(cmd);

        while (!cmd.equals("-q")) {
            if (!c.executeCommand(cmd)) {
                if (!c.verifyCommand())
                    System.out.println("Command <" + cmd + "> could not be executed");
            }

            if (c.printToScreen) {
                System.out.println(c.print);
                c.printToScreen = false;
            }

            print(c.getListOfEvents());
            cmd = input.nextLine();
            c.setCommand(cmd);
        }

        input.close();

    }

    public void print(List<event> list) {
        for (event e : list) {
            System.out.println(e.eventToString());
        }
    }

    public void connectToDB() {
        Connection con = null;

        try {
            String url = "jdbc:mysql://localhost:3306/sys";
            String username = "root";
            String password = "Ammadq87";

            // Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(url, username, password);

            if (con != null) {
                System.out.println("Successfully connected to MySQL database test");
                Statement stmnt = con.createStatement();
                ResultSet r = stmnt.executeQuery("select * from students");
                while (r.next()) {
                    String data = "";
                    for (int i = 1; i <= 5; i++) {
                        data += r.getString(i) + ":";
                    }
                    System.out.println(data);
                }
            }

        } catch (SQLException ex) {
            System.out.println("An error occurred while connecting MySQL databse");
            ex.printStackTrace();
        }
    }

}
