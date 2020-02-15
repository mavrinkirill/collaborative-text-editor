package services;

import command.*;
import exceptions.OperationalTransformationException;
import interfaces.TransformationService;
import interfaces.CommandTransformation;

public class CommandTransformationService implements TransformationService {
    private CommandTransformation commandTransformation;

    public CommandTransformationService(CommandTransformation commandTransformation){
        this.commandTransformation = commandTransformation;
    }

    @Override
    public CommandBase transform(CommandBase previous, CommandBase current) throws Exception {
        if(previous.getType() == CommandType.INSERT && current.getType() == CommandType.INSERT){
            InsertCommand previousInsert = (InsertCommand) previous;
            InsertCommand currentInsert = (InsertCommand) current;

            return commandTransformation.transformation(previousInsert, currentInsert);
        }

        if(previous.getType() == CommandType.INSERT && current.getType() == CommandType.DELETE){
            InsertCommand previousInsert = (InsertCommand) previous;
            DeleteCommand currentDelete = (DeleteCommand) current;

            return commandTransformation.transformation(previousInsert, currentDelete);
        }

        if(previous.getType() == CommandType.DELETE && current.getType() == CommandType.INSERT){
            DeleteCommand previousDelete = (DeleteCommand) previous;
            InsertCommand currentInsert = (InsertCommand) current;

            return commandTransformation.transformation(previousDelete, currentInsert);
        }

        if(previous.getType() == CommandType.DELETE && current.getType() == CommandType.DELETE){
            DeleteCommand previousDelete = (DeleteCommand) previous;
            DeleteCommand currentDelete = (DeleteCommand) current;

            return commandTransformation.transformation(previousDelete, currentDelete);
        }

        throw new OperationalTransformationException("Unknown command type combination");
    }
}
