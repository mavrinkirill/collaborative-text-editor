package command;

import exceptions.*;

public class DeleteCommand extends CommandBase {
    public int count;

    public DeleteCommand(long version, int position, int count, int authorId) {
        super(version, position, authorId, CommandType.DELETE);
        this.count = count;
    }

    @Override
    public String apply(String value) throws Exception {
        ensureValue(value);

        if(position + count > value.length()){
            throw new CommandOutRangeException("Invalid delete position");
        }

        return value.substring(0, position) + value.substring(position + count);
    }
}
