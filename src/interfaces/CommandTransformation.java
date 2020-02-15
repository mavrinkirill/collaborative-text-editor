package interfaces;

import command.DeleteCommand;
import command.InsertCommand;

public interface CommandTransformation {
    InsertCommand transformation(InsertCommand previous, InsertCommand current);

    InsertCommand transformation(DeleteCommand previous, InsertCommand current);

    DeleteCommand transformation(InsertCommand previous, DeleteCommand current);

    DeleteCommand transformation(DeleteCommand previous, DeleteCommand current);
}
