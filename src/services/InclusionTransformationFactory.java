package services;

import command.CommandBase;
import command.CommandType;
import exceptions.NotFoundException;
import exceptions.transformation.TransformationDuplicateException;
import interfaces.TransformationFactory;
import javafx.util.Pair;
import transformations.CommandTransformation;

import java.util.Collection;
import java.util.HashMap;

public class InclusionTransformationFactory implements TransformationFactory
{
    private HashMap<Pair<CommandType, CommandType>, CommandTransformation> transformationStorage = new HashMap<>();

    public InclusionTransformationFactory(Collection<CommandTransformation> transformations) throws TransformationDuplicateException {
        for (CommandTransformation transformation:transformations){
            CommandType previousCommandType = transformation.getPreviousCommandType();
            CommandType currentCommandType = transformation.getCurrentCommandType();
            Pair<CommandType, CommandType> key = new Pair<>(previousCommandType, currentCommandType);

            if(transformationStorage.containsKey(key)){
                throw new TransformationDuplicateException("Duplicate key " + key);
            }

            transformationStorage.put(key, transformation);
        }
    }

    @Override
    public CommandTransformation getTransformation(CommandBase previousCommand, CommandBase currentCommand) throws NotFoundException {
        CommandType previousCommandType = previousCommand.getType();
        CommandType currentCommandType = currentCommand.getType();
        Pair<CommandType, CommandType> key = new Pair<>(previousCommandType, currentCommandType);

        CommandTransformation commandTransformation = transformationStorage.get(key);

        if(commandTransformation == null){
            throw new NotFoundException("Unknown command type combination " + key);
        }

        return commandTransformation;
    }
}
