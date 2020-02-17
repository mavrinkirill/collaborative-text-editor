package transformations;

import command.CommandBase;
import command.CommandType;

public interface CommandTransformation {
    CommandType getPreviousCommandType();

    CommandType getCurrentCommandType();

    CommandBase transform(CommandBase previousCommand, CommandBase currentCommand);
}
