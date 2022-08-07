package MainApp;

// A test for merging
public class Messages {
    private String message = "lblIntro";

    public Messages() {

    }

    public void outputMessage(String message, char type) {
        switch (type) {
            case 'e':
                System.out.print(Color.RED_BOLD_BRIGHT);
                System.out.print(Color.RED_UNDERLINED);
                System.out.println(message);
                System.out.print(Color.RESET);
                break;
            case 's':
                System.out.print(Color.GREEN_BOLD_BRIGHT);
                System.out.println(message);
                System.out.print(Color.RESET);
                break;
            case 'r':
                System.out.print(Color.BLUE_BOLD_BRIGHT);
                System.out.println(message);
                System.out.print(Color.RESET);
                break;
        }
    }

    public String getCurrentMessage(String lbl) {
        message = getMessage(lbl);
        return message;
    }

    public String getErrorMessage(String lbl, String opt) {

        if (lbl == null || lbl.equals("")) {
            return "> Unknown Error...";
        }

        if (opt != null) {
            opt = " \"" + opt + "\" ";
        } else {
            opt = " ";
        }

        String labels[][] = {
                { "lblCommandNotFound", "Command" + opt + "Not Found" },
                { "lblInvalidDate", "Invalid Date > " + opt },
                { "lblInvalidTime", "Invalid Time > " + opt },
                { "lblInvalidParameter", "Invalid Parameter" },
                { "lblInvalidArgument", "Invalid Arguemnt" },
                { "lblNonNumerical", "Non-numerical Value Provided" },
                { "lblFailedConnection", "Failed to Connect to MySql Database" },
                { "lblQueryFailed", "Failed to Execute Query:" + opt },
                { "lblCommandFailed", "Failed to Execute Command:" + opt },
                { "lblPrintResultsFailed", "Failed to Print Results:" + opt },
                { "lblDuplicateOrUnknownValue", "Duplucate or Unknown Value/Parameter Entered" },
                { "lblCustom", opt }

        };

        for (int i = 0; i < labels.length; i++) {
            if (lbl.equals(labels[i][0])) {
                return "> Error: " + labels[i][1];
            }
        }

        return "> Unknwon Error...";
    }

    public String getMessage(String lbl) {
        String labels[][] = {
                { "lblIntro", "Enter Command. Type -h for help" },
                { "lblSuccess", "Success" },
                { "lblSavedSuccessfully", "Value(s) Saved Successfully" },
                { "lblCreate", "Event Created" },
                { "lblRead", "Event Found" },
                { "lblUpdate", "Event Updated" },
                { "lblDelete", "Event Deleted" },
                { "lblOptionList", "Select an option" },
                { "lblExit", "Exited application" }
        };

        if (lbl.equals("lblExit"))
            return "\n> " + labels[labels.length - 1][1];

        for (int i = 0; i < labels.length; i++) {
            if (lbl.equals(labels[i][0])) {
                return "> " + labels[i][1];
            }
        }

        return "> Hello...";
    }

}

enum Color {
    // Color end string, color reset
    RESET("\033[0m"),

    // Regular Colors. Normal color, no bold, background color etc.
    BLACK("\033[0;30m"), // BLACK
    RED("\033[0;31m"), // RED
    GREEN("\033[0;32m"), // GREEN
    YELLOW("\033[0;33m"), // YELLOW
    BLUE("\033[0;34m"), // BLUE
    MAGENTA("\033[0;35m"), // MAGENTA
    CYAN("\033[0;36m"), // CYAN
    WHITE("\033[0;37m"), // WHITE

    // Bold
    BLACK_BOLD("\033[1;30m"), // BLACK
    RED_BOLD("\033[1;31m"), // RED
    GREEN_BOLD("\033[1;32m"), // GREEN
    YELLOW_BOLD("\033[1;33m"), // YELLOW
    BLUE_BOLD("\033[1;34m"), // BLUE
    MAGENTA_BOLD("\033[1;35m"), // MAGENTA
    CYAN_BOLD("\033[1;36m"), // CYAN
    WHITE_BOLD("\033[1;37m"), // WHITE

    // Underline
    BLACK_UNDERLINED("\033[4;30m"), // BLACK
    RED_UNDERLINED("\033[4;31m"), // RED
    GREEN_UNDERLINED("\033[4;32m"), // GREEN
    YELLOW_UNDERLINED("\033[4;33m"), // YELLOW
    BLUE_UNDERLINED("\033[4;34m"), // BLUE
    MAGENTA_UNDERLINED("\033[4;35m"), // MAGENTA
    CYAN_UNDERLINED("\033[4;36m"), // CYAN
    WHITE_UNDERLINED("\033[4;37m"), // WHITE

    // Background
    BLACK_BACKGROUND("\033[40m"), // BLACK
    RED_BACKGROUND("\033[41m"), // RED
    GREEN_BACKGROUND("\033[42m"), // GREEN
    YELLOW_BACKGROUND("\033[43m"), // YELLOW
    BLUE_BACKGROUND("\033[44m"), // BLUE
    MAGENTA_BACKGROUND("\033[45m"), // MAGENTA
    CYAN_BACKGROUND("\033[46m"), // CYAN
    WHITE_BACKGROUND("\033[47m"), // WHITE

    // High Intensity
    BLACK_BRIGHT("\033[0;90m"), // BLACK
    RED_BRIGHT("\033[0;91m"), // RED
    GREEN_BRIGHT("\033[0;92m"), // GREEN
    YELLOW_BRIGHT("\033[0;93m"), // YELLOW
    BLUE_BRIGHT("\033[0;94m"), // BLUE
    MAGENTA_BRIGHT("\033[0;95m"), // MAGENTA
    CYAN_BRIGHT("\033[0;96m"), // CYAN
    WHITE_BRIGHT("\033[0;97m"), // WHITE

    // Bold High Intensity
    BLACK_BOLD_BRIGHT("\033[1;90m"), // BLACK
    RED_BOLD_BRIGHT("\033[1;91m"), // RED
    GREEN_BOLD_BRIGHT("\033[1;92m"), // GREEN
    YELLOW_BOLD_BRIGHT("\033[1;93m"), // YELLOW
    BLUE_BOLD_BRIGHT("\033[1;94m"), // BLUE
    MAGENTA_BOLD_BRIGHT("\033[1;95m"), // MAGENTA
    CYAN_BOLD_BRIGHT("\033[1;96m"), // CYAN
    WHITE_BOLD_BRIGHT("\033[1;97m"), // WHITE

    // High Intensity backgrounds
    BLACK_BACKGROUND_BRIGHT("\033[0;100m"), // BLACK
    RED_BACKGROUND_BRIGHT("\033[0;101m"), // RED
    GREEN_BACKGROUND_BRIGHT("\033[0;102m"), // GREEN
    YELLOW_BACKGROUND_BRIGHT("\033[0;103m"), // YELLOW
    BLUE_BACKGROUND_BRIGHT("\033[0;104m"), // BLUE
    MAGENTA_BACKGROUND_BRIGHT("\033[0;105m"), // MAGENTA
    CYAN_BACKGROUND_BRIGHT("\033[0;106m"), // CYAN
    WHITE_BACKGROUND_BRIGHT("\033[0;107m"); // WHITE

    private final String code;

    Color(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return code;
    }
}
