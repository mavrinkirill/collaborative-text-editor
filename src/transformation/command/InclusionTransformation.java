package transformation.command;

import command.*;

public class InclusionTransformation implements CommandTransformation {
    @Override
    public CommandBase insertAfterInsertTransformation(CommandBase previous, CommandBase current) {
        if(current.position < previous.position){
            return current;
        }
        else if(current.position == previous.position && current.authorId >= previous.authorId){
            return current;
        }
        else {
            current.position = current.position + previous.getOffset();
        }

        return current;
    }

    @Override
    public CommandBase insertAfterDeleteTransformation(CommandBase previous, CommandBase current) {
        if(current.position < previous.position){
            return current;
        }
        else if(current.position == previous.position){
            return current;
        }
        else {
            current.position = current.position - previous.getOffset();
        }

        return current;
    }

    @Override
    public CommandBase deleteAfterInsertTransformation(CommandBase previous, CommandBase current) {
        if(current.position < previous.position){
            return current;
        }

        current.position = current.position + previous.getOffset();

        return current;
    }

    @Override
    public CommandBase deleteAfterDeleteTransformation(CommandBase previous, CommandBase current) {
        int startPrevious = previous.position;
        int endPrevious = previous.position + previous.getOffset();
        int startCurrent = current.position;
        int endCurrent = current.position + current.getOffset();

        if(current.position + current.getOffset() < previous.position){
            return current;
        }
        else if(current.position == previous.position){
            if(previous.getOffset() >= current.getOffset()){
                return new DeleteCommand(current.version, current.position, 0, current.authorId);
            }
            else{
                return new DeleteCommand(current.version, current.position, current.getOffset() - previous.getOffset(), current.authorId);
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
                return new DeleteCommand(current.version, position - previous.getOffset(), count, current.authorId);
            }
        }
    }
}
