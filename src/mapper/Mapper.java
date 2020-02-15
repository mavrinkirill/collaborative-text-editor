package mapper;

import command.CommandBase;
import command.CommandType;
import command.DeleteCommand;
import command.InsertCommand;
import document.DocumentState;
import exceptions.MapperException;
import models.command.CommandDto;
import models.command.CommandTypeDto;
import models.command.DeleteCommandDto;
import models.command.InsertCommandDto;
import models.document.DocumentDto;


public class Mapper {
    public static DocumentDto Map(DocumentState state){
        if(state == null){
            return null;
        }

        DocumentDto documentDto = new DocumentDto();
        documentDto.version = state.version;
        documentDto.content = state.content;
        return documentDto;
    }

    public static InsertCommand Map(InsertCommandDto commandDto){
        if(commandDto == null){
            return null;
        }

        InsertCommand command = new InsertCommand(commandDto.version, commandDto.position, commandDto.inserted, commandDto.authorId);
        return command;
    }

    public static DeleteCommand Map(DeleteCommandDto commandDto){
        if(commandDto == null){
            return null;
        }

        DeleteCommand command = new DeleteCommand(commandDto.version, commandDto.position, commandDto.count, commandDto.authorId);
        return command;
    }

    public static CommandDto Map(CommandBase command) throws MapperException {
        if(command == null){
            return null;
        }

        CommandDto commandDto = new CommandDto();
        commandDto.version = command.version;
        commandDto.position = command.position;
        commandDto.type = Mapper.Map(command.getType());
        commandDto.authorId = command.authorId;

        return commandDto;
    }

    public static CommandTypeDto Map(CommandType type) throws MapperException {
        if(type == null){
            return null;
        }

        switch (type){
            case INSERT:
                return CommandTypeDto.INSERT;
            case DELETE:
                return CommandTypeDto.DELETE;
            default:
                throw new MapperException();
        }
    }
}
