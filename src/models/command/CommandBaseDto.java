package models.command;

public abstract class CommandBaseDto {
    public long version;

    public int position;

    public int authorId;

    public CommandTypeDto type;
}
