package MainApp;

import java.util.*;

public interface ICommand {

    public List<String> parameters = new ArrayList<String>();
    public Event e = new Event();

    public void execute();

    public String createQuery(String table);

    public boolean validateCommand();

    public Map<String, String> getArguments();

}
