package document;

import command.*;
import java.util.ArrayList;

public interface ConcurrentDocument {
    DocumentState getState();

    void command(CommandBase command) throws Exception;

    ArrayList<CommandBase> getHistory(long version);
}