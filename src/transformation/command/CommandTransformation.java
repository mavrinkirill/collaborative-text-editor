package transformation.command;

import command.CommandBase;

public interface CommandTransformation {
    CommandBase insertAfterInsertTransformation(CommandBase previous, CommandBase current);

    CommandBase insertAfterDeleteTransformation(CommandBase previous, CommandBase current);

    CommandBase deleteAfterInsertTransformation(CommandBase previous, CommandBase current);

    CommandBase deleteAfterDeleteTransformation(CommandBase previous, CommandBase current);
}
