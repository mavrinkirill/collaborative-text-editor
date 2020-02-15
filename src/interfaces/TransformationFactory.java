package interfaces;

import command.CommandBase;
import exceptions.NotFoundException;
import transformations.CommandTransformation;

public interface TransformationFactory {
    CommandTransformation getTransformation(CommandBase previousCommand, CommandBase currentCommand) throws NotFoundException;
}
