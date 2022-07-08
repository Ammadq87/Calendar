package MainApp;

import java.util.*;

public class app {

    Messages messageDisplay = new Messages();

    public static void main(String args[]) {
        new app();
    }

    public app() {
        Scanner input = new Scanner(System.in);
        messageDisplay.outputMessage(messageDisplay.getMessage("lblIntro"), 'r');
        String cmd = input.nextLine();

        Command c = new Command(cmd);

        while (!cmd.equals("-q")) {
            if (!c.isCommandValid()) {
            } else {
                c.executeCommand();
            }

            cmd = input.nextLine();
            c.setCommand(cmd);
        }

        input.close();

    }

}
