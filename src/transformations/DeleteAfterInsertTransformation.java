package transformations;


import command.CommandBase;
import command.CommandType;
import command.DeleteCommand;
import command.InsertCommand;

public class DeleteAfterInsertTransformation implements CommandTransformation {

    @Override
    public CommandType getPreviousCommandType() {
        return CommandType.INSERT;
    }

    @Override
    public CommandType getCurrentCommandType() {
        return CommandType.DELETE;
    }

    @Override
    public CommandBase transformation(CommandBase previousCommand, CommandBase currentCommand) {
        InsertCommand previous = (InsertCommand) previousCommand;
        DeleteCommand current = (DeleteCommand) currentCommand;

        if(current.position < previous.position){
            return current;
        }

        current.position = current.position + previous.inserted.length();

        return current;
    }
}
