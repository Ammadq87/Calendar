package MainApp;

import java.util.*;
import java.sql.*;

public class app {

    Messages messageDisplay = new Messages();

    public static void main(String args[]) {
        new app();
    }

    public Connection con = null;

    public app() {
        Scanner input = new Scanner(System.in);
        messageDisplay.outputMessage("Enter Command. Type -h for help", 'r');
        String cmd = input.nextLine();

        Command c = new Command(cmd);

        while (!cmd.equals("-q")) {
            if (!c.isCommandValid()) {
                messageDisplay.outputMessage("<error: " + c.errorMessage + " > ", 'w');
            } else {
                c.executeCommand();
            }

            cmd = input.nextLine();
            c.setCommand(cmd);
        }

        input.close();

    }

}
