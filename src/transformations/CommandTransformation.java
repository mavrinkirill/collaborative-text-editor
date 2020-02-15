package transformations;

import command.CommandBase;
import command.CommandType;

public interface CommandTransformation {
    CommandType getPreviousCommandType();

    CommandType getCurrentCommandType();

    CommandBase transformation(CommandBase previousCommand, CommandBase currentCommand);
}
