package transformations;


import command.CommandBase;
import command.CommandType;
import command.DeleteCommand;

public class DeleteAfterDeleteTransformation implements CommandTransformation {

    @Override
    public CommandType getPreviousCommandType() {
        return CommandType.DELETE;
    }

    @Override
    public CommandType getCurrentCommandType() {
        return CommandType.DELETE;
    }

    @Override
    public CommandBase transformation(CommandBase previousCommand, CommandBase currentCommand) {
        DeleteCommand previous = (DeleteCommand) previousCommand;
        DeleteCommand current = (DeleteCommand) currentCommand;

        /*
         * Its look like line segment intersection problem and we need to find final start and end position*/
        int startPrevious = previous.position;
        int endPrevious = previous.position + previous.count;
        int startCurrent = current.position;
        int endCurrent = current.position + current.count;

        if(current.position + current.count < previous.position){
            return current;
        }
        else if(current.position == previous.position){
            if(previous.count >= current.count){
                return new DeleteCommand(current.version, current.position, 0, current.authorId);
            }
            else{
                return new DeleteCommand(current.version, current.position, current.count - previous.count, current.authorId);
            }
        }
        else {
            if(startCurrent < startPrevious)
            {
                return new DeleteCommand(current.version, startCurrent, Math.min(endCurrent, startPrevious) - startCurrent, current.authorId);
            }
            else{
                int position = Math.max(endPrevious, startCurrent);
                int count = Math.max(endPrevious, endCurrent) - position;
                return new DeleteCommand(current.version, position - previous.count, count, current.authorId);
            }
        }
    }
}
