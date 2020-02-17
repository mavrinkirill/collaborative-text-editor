package transformations;


import command.CommandBase;
import command.CommandType;
import command.InsertCommand;

public class InsertAfterInsertTransformation implements CommandTransformation {

    @Override
    public CommandType getPreviousCommandType() {
        return CommandType.INSERT;
    }

    @Override
    public CommandType getCurrentCommandType() {
        return CommandType.INSERT;
    }

    @Override
    public CommandBase transform(CommandBase previousCommand, CommandBase currentCommand) {
        InsertCommand previous = (InsertCommand) previousCommand;
        InsertCommand current = (InsertCommand) currentCommand;

        /*
         * We need to use some kind of command identificator (I used authorId) for clearly to resolve conflicts in same position*/
        if(current.position < previous.position){
            return current;
        }
        else if(current.position == previous.position && current.authorId >= previous.authorId){
            return current;
        }
        else {
            current.position = current.position + previous.inserted.length();
        }

        return current;
    }
}
