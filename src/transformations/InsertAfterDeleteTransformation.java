package transformations;


import command.CommandBase;
import command.CommandType;
import command.DeleteCommand;
import command.InsertCommand;

public class InsertAfterDeleteTransformation implements CommandTransformation {

    @Override
    public CommandType getPreviousCommandType() {
        return CommandType.DELETE;
    }

    @Override
    public CommandType getCurrentCommandType() {
        return CommandType.INSERT;
    }

    @Override
    public CommandBase transformation(CommandBase previousCommand, CommandBase currentCommand) {
        DeleteCommand previous = (DeleteCommand) previousCommand;
        InsertCommand current = (InsertCommand) currentCommand;

        if(current.position <= previous.position){
            return current;
        }
        else {
            current.position = current.position - previous.count;
        }

        return current;
    }
}
