
package Calendar;

import java.util.*;

public class app {
    public static void main(String args[]) {
        new app();
    }

    public app() {

        Scanner input = new Scanner(System.in);
        System.out.println("Enter Command. Type -h for help");
        String cmd = input.nextLine();

        commands c;

        while (!cmd.equals("-q")) {
            c = new commands(cmd);

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
        }

    }

    public void print(List<event> list) {
        for (event e : list) {
            System.out.println(e.eventToString());
        }
    }

}
