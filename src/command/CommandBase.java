package command;
import exceptions.*;

public abstract class CommandBase {
    public int position;

    public long version;

    public int authorId;

    private CommandType type;

    protected CommandBase(long version, int position, int authorId, CommandType commandType){
        this.version = version;
        this.position = position;
        this.authorId = authorId;
        this.type = commandType;
    }

    public final CommandType getType(){
        return type;
    }

    public abstract String apply(String value) throws Exception;

    public abstract int getOffset();

    public abstract String getInserted() throws CommandNotImplementedException;

    public abstract int getCount() throws CommandNotImplementedException;

    protected void ensureValue(String value) throws Exception {
        if(value == null){
            throw new CommandNullValueException();
        }
    }
}
