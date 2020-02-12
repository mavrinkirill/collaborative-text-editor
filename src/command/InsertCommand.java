package command;

import exceptions.*;

public class InsertCommand extends CommandBase {
    public String inserted;

    public InsertCommand(long version, int position, String inserted, int authorId) {
        super(version, position, authorId, CommandType.INSERT);
        this.inserted = inserted;
    }

    @Override
    public String apply(String value) throws Exception {
        ensureValue(value);

        if(inserted == null){
            throw new CommandNullValueException();
        }

        if(position > value.length()){
            throw new CommandOutRangeException("Invalid insert position");
        }

        StringBuffer buffer = new StringBuffer(value);
        buffer.insert(position, inserted);
        return buffer.toString();
    }
}
