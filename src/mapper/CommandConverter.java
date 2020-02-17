package mapper;

import command.CommandBase;
import command.DeleteCommand;
import command.InsertCommand;
import models.command.CommandBaseDto;
import models.command.CommandDto;
import models.command.DeleteCommandDto;
import models.command.InsertCommandDto;

public class CommandConverter {
    public static CommandBase Convert(CommandBaseDto commandDto){
        if(commandDto == null){
            return null;
        }

        if(commandDto.type == null){
            return null;
        }

        try {
            switch (commandDto.type){
                case INSERT:
                    return Mapper.Map((InsertCommandDto) commandDto);
                case DELETE:
                    return Mapper.Map((DeleteCommandDto) commandDto);
                default:
                    return null;
            }
        }
        catch (Exception e){
            return null;
        }
    }

    public static CommandDto Convert(CommandBase command){
        if(command == null){
            return null;
        }

        if(command.getType() == null){
            return null;
        }

        CommandDto commandDto = Mapper.Map(command);

        try {
            switch (commandDto.type){
                case INSERT:
                    commandDto.inserted = ((InsertCommand) command).inserted;
                    break;
                case DELETE:
                    commandDto.count = ((DeleteCommand) command).count;
                    break;
                default:
                    return null;
            }

            return commandDto;
        }
        catch (Exception e){
            return null;
        }
    }
}
