package interfaces;

import command.CommandType;
import exceptions.NotFoundException;
import transformations.CommandTransformation;

public interface TransformationFactory {
    CommandTransformation getTransformation(CommandType previousCommandType, CommandType currentCommandType) throws NotFoundException;
}
