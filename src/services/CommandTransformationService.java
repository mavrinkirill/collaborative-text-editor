package services;

import command.*;
import exceptions.OperationalTransformationException;
import interfaces.TransformationService;
import transformation.command.CommandTransformation;

public class CommandTransformationService implements TransformationService {
    private CommandTransformation commandTransformation;

    public CommandTransformationService(CommandTransformation commandTransformation){
        this.commandTransformation = commandTransformation;
    }

    @Override
    public CommandBase transform(CommandBase previous, CommandBase current) throws Exception {
        if(previous.getType() == CommandType.INSERT && current.getType() == CommandType.INSERT){
            return commandTransformation.insertAfterInsertTransformation(previous, current);
        }
        if(previous.getType() == CommandType.INSERT && current.getType() == CommandType.DELETE){
            return commandTransformation.deleteAfterInsertTransformation(previous, current);
        }
        if(previous.getType() == CommandType.DELETE && current.getType() == CommandType.INSERT){
            return commandTransformation.insertAfterDeleteTransformation(previous, current);
        }
        if(previous.getType() == CommandType.DELETE && current.getType() == CommandType.DELETE){
            return commandTransformation.deleteAfterDeleteTransformation(previous, current);
        }

        throw new OperationalTransformationException("Unknown command type combination");
    }
}
