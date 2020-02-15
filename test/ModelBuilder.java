import models.command.DeleteCommandDto;
import models.command.InsertCommandDto;

public class ModelBuilder {
    public static InsertCommandDto insertCommandDto(long version, int position, String inserted, int authorId){
        InsertCommandDto command = new InsertCommandDto();
        command.version = version;
        command.position = position;
        command.inserted = inserted;
        command.authorId = authorId;

        return command;
    }

    public static DeleteCommandDto deleteCommandDto(long version, int position, int count, int authorId){
        DeleteCommandDto command = new DeleteCommandDto();
        command.version = version;
        command.position = position;
        command.count = count;
        command.authorId = authorId;

        return command;
    }
}
