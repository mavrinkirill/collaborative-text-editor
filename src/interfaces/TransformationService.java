package interfaces;

import command.CommandBase;

public interface TransformationService {
    CommandBase transform(CommandBase previous, CommandBase current) throws Exception;
}
