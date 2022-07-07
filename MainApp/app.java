package MainApp;

import java.util.*;
import java.sql.*;

public class app {

    public static void main(String args[]) {
        new app();
    }

    public Connection con = null;

    public app() {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter Command. Type -h for help");
        String cmd = input.nextLine();

        Command c = new Command(cmd);

        while (!cmd.equals("-q")) {
            if (!c.isCommandValid()) {
                System.out.println("<error: " + c.errorMessage + " >");
            } else {
                c.executeCommand();
            }

            cmd = input.nextLine();
            c.setCommand(cmd);
        }

        input.close();

    }

}
