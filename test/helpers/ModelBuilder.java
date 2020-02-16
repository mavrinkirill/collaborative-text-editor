package helpers;

import models.command.CommandTypeDto;
import models.command.DeleteCommandDto;
import models.command.InsertCommandDto;

public class ModelBuilder {
    public static InsertCommandDto insertCommandDto(long version, int position, String inserted, int authorId){
        InsertCommandDto command = new InsertCommandDto();
        command.version = version;
        command.position = position;
        command.inserted = inserted;
        command.authorId = authorId;
        command.type = CommandTypeDto.INSERT;

        return command;
    }

    public static DeleteCommandDto deleteCommandDto(long version, int position, int count, int authorId){
        DeleteCommandDto command = new DeleteCommandDto();
        command.version = version;
        command.position = position;
        command.count = count;
        command.authorId = authorId;
        command.type = CommandTypeDto.DELETE;

        return command;
    }
}
