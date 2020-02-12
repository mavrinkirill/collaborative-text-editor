package transformation.command;

import command.*;

public class InclusionTransformation implements CommandTransformation {

    @Override
    public InsertCommand transformation(InsertCommand previous, InsertCommand current) {
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

    @Override
    public InsertCommand transformation(DeleteCommand previous, InsertCommand current) {
        if(current.position < previous.position){
            return current;
        }
        else if(current.position == previous.position){
            return current;
        }
        else {
            current.position = current.position - previous.count;
        }

        return current;
    }

    @Override
    public DeleteCommand transformation(InsertCommand previous, DeleteCommand current) {
        if(current.position < previous.position){
            return current;
        }

        current.position = current.position + previous.inserted.length();

        return current;
    }

    @Override
    public DeleteCommand transformation(DeleteCommand previous, DeleteCommand current) {
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
