package document;

import command.CommandBase;
import java.util.ArrayList;

public interface Document {
    DocumentState getState();

    void applyCommand(CommandBase command) throws Exception;

    ArrayList<CommandBase> getHistory(long version);
}